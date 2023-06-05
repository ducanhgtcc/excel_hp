package com.example.onekids_project.service.serviceimpl.employeesaralyImpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.*;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.request.common.YearIdRequest;
import com.example.onekids_project.request.employeeSalary.*;
import com.example.onekids_project.request.finance.approved.IdAndDateNotNullRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.response.attendanceemployee.AttendanceEmployeesStatisticalResponse;
import com.example.onekids_project.response.employeesalary.*;
import com.example.onekids_project.response.finance.order.KidsPackageCustom2;
import com.example.onekids_project.response.finance.order.OrderPrintResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.cashbook.CashBookHistoryService;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.example.onekids_project.service.servicecustom.employeesaraly.EmployeeSalaryService;
import com.example.onekids_project.service.servicecustom.employeesaraly.OrderEmployeeHistoryService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.objectdata.FnMonthObject;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private FnEmployeeSalaryDefaultRepository saralyDefaultRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;

    @Autowired
    private FnEmployeeSalaryDefaultRepository fnEmployeeSalaryDefaultRepository;

    @Autowired
    private FnSalaryRepository fnSalaryRepository;

    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;

    @Autowired
    private OrderEmployeeHistoryRepository orderEmployeeHistoryRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private CashBookHistoryService cashBookHistoryService;

    @Autowired
    private OrderEmployeeHistoryService orderEmployeeHistoryService;

    @Autowired
    private ExOrderHistoryEmployeeSalaryRepository exOrderHistoryEmployeeSalaryRepository;

    @Autowired
    private AttendanceEmployeeService attendanceEmployeeService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private AppSendService appSendService;
    @Autowired
    private SmsDataService smsDataService;
    @Autowired
    private AttendanceEmployeeRepository attendanceEmployeeRepository;

    @Override
    public boolean createEmployeeSalaryDefault(UserPrincipal principal, Long idInfoEmployee, EmployeeSalaryDefaultCreateRequest request) {
        CommonValidate.checkDataPlus(principal);
        if (!this.checkRequestMonth(request)) {
            return false;
        }
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfoEmployee).orElseThrow();
        FnEmployeeSalaryDefault response = modelMapper.map(request, FnEmployeeSalaryDefault.class);
        response.setInfoEmployeeSchool(infoEmployeeSchool);
        saralyDefaultRepository.save(response);
        return true;
    }

    @Override
    public List<EmployeeSalarySettingResponse> searchEmployeeSalarySetting(UserPrincipal principal, EmployeeSalaryApplySearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<EmployeeSalarySettingResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchEmployeeSalaryNew(principal.getIdSchoolLogin(), request.getEmployeeStatus(), request.getIdDepartment(), request.getEmployeeNameOrPhone());
        infoEmployeeSchoolList.forEach(x -> {
            EmployeeSalarySettingResponse model = modelMapper.map(x, EmployeeSalarySettingResponse.class);
            List<FnEmployeeSalary> fnEmployeeSalaryList = FinanceUltils.getEmployeeSalaryListFromInfoEmployee(x, request.getDate(), AppConstant.APP_FALSE);
            model.setFnEmployeeSalaryList(listMapper.mapList(fnEmployeeSalaryList, EmployeeSalaryCustom2.class));
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<EmployeeSalaryResponse> searchEmployeeSalaryApproved(UserPrincipal principal, EmployeeSalaryApplySearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchEmployeeSalaryNew(principal.getIdSchoolLogin(), request.getEmployeeStatus(), request.getIdDepartment(), request.getEmployeeNameOrPhone(), request.getIdList());
        return this.getEmployeeSalaryApproved(infoEmployeeSchoolList, request.getDate());
    }

    @Override
    public List<EmployeeSalaryResponse> searchEmployeeSalaryApprovedDetail(UserPrincipal principal, IdAndDateNotNullRequest request) {
        CommonValidate.checkDataPlus(principal);
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findById(request.getId()).orElseThrow();
        return this.getEmployeeSalaryApproved(Collections.singletonList(infoEmployeeSchool), request.getDate());
    }

    private List<EmployeeSalaryResponse> getEmployeeSalaryApproved(List<InfoEmployeeSchool> infoEmployeeSchoolList, LocalDate date) {
        List<EmployeeSalaryResponse> responseList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x -> {
            List<FnEmployeeSalary> fnEmployeeSalaryList = FinanceUltils.getEmployeeSalaryListFromInfoEmployee(x, date, AppConstant.APP_FALSE);
            EmployeeSalaryResponse model = new EmployeeSalaryResponse();
            OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnEmployeeSalaryList);
            modelMapper.map(orderMoneyModel, model);
            List<FnEmployeeSalary> approvedList = fnEmployeeSalaryList.stream().filter(FnEmployeeSalary::isApproved).collect(Collectors.toList());
            OrderMoneyModel orderMoneyModelApproved = FinanceUltils.getOrderMoneyEmployeeModel(approvedList);
            double moneyRemain = orderMoneyModelApproved.getMoneyTotalInOutRemain();
            if (moneyRemain > 0) {
                model.setTotalMoneyRemainOut(moneyRemain);
            } else {
                model.setTotalMoneyRemainIn(Math.abs(moneyRemain));
            }
            model.setFullName(x.getFullName());
            model.setPhone(x.getPhone());
            model.setId(x.getId());
            String approvedNumber = fnEmployeeSalaryList.stream().filter(FnEmployeeSalary::isApproved).count() + "/" + fnEmployeeSalaryList.size();
            String LockedNumber = fnEmployeeSalaryList.stream().filter(FnEmployeeSalary::isLocked).count() + "/" + fnEmployeeSalaryList.size();
            model.setNumberApproved(approvedNumber);
            model.setNumberLocked(LockedNumber);
            List<EmployeeSalaryCustom1> dataList = new ArrayList<>();
            fnEmployeeSalaryList.forEach(y -> {
                float calculateNumber = this.getCalculateNumber(x, y);
                EmployeeSalaryCustom1 fnEmployeeSalaryResponse = modelMapper.map(y, EmployeeSalaryCustom1.class);
                fnEmployeeSalaryResponse.setCalculateNumber(calculateNumber);
                fnEmployeeSalaryResponse.setShowNumber(y.getUserNumber());
                fnEmployeeSalaryResponse.setMoneyTemp(FinanceUltils.getMoneyWidthNumberEmployee(y, calculateNumber));
                fnEmployeeSalaryResponse.setMoney(FinanceUltils.getMoneySalary(y));
                dataList.add(fnEmployeeSalaryResponse);
            });
            model.setFnEmployeeSalaryList(dataList);
            responseList.add(model);
        });
        return responseList;
    }

    private float getCalculateNumber(InfoEmployeeSchool infoEmployeeSchool, FnEmployeeSalary fnEmployeeSalary) {
        float number = 1;
        int year = fnEmployeeSalary.getYear();
        int month = fnEmployeeSalary.getMonth();
        List<LocalDate> dateList = FinanceUltils.getRangeDateSalary(month, year);
        LocalDate startDate = dateList.get(0);
        LocalDate endDate = dateList.get(1).minusDays(1);
//        LocalDate startDate = LocalDate.of(fnEmployeeSalary.getYear(), fnEmployeeSalary.getMonth(), 1);
//        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        AttendanceEmployeesStatisticalResponse statistical = attendanceEmployeeService.attendanceEmployeesStatistical(infoEmployeeSchool, startDate, endDate);
        if (fnEmployeeSalary.isAttendance()) {
            String attendanceType = fnEmployeeSalary.getAttendanceType();
            String attendanceDetail = fnEmployeeSalary.getAttendanceDetail();
            switch (attendanceType) {
                //đi học
                case FinanceConstant.ATTENDANCE_GO_WORK:
                    switch (attendanceDetail) {
                        case FinanceConstant.DAY_MORNING:
                            number = statistical.getMorning();
                            break;
                        case FinanceConstant.DAY_AFTERNOON:
                            number = statistical.getAfternoon();
                            break;
                        case FinanceConstant.DAY_EVENING:
                            number = statistical.getEvening();
                            break;
                        case FinanceConstant.ALL_DAY:
                            number = statistical.getAllDay();
                            break;
                        case FinanceConstant.GO_SCHOOL_TIME:
                            number = statistical.getGoSchoolTime();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 1");
                    }
                    break;
                //nghỉ có phép
                case FinanceConstant.ATTENDANCE_ABSENT_YES:
                    switch (attendanceDetail) {
                        case FinanceConstant.DAY_MORNING:
                            number = statistical.getMorningYes();
                            break;
                        case FinanceConstant.DAY_AFTERNOON:
                            number = statistical.getAfternoonYes();
                            break;
                        case FinanceConstant.DAY_EVENING:
                            number = statistical.getEveningYes();
                            break;
                        case FinanceConstant.ALL_DAY:
                            number = statistical.getAllDayYes();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 2");
                    }
                    break;
                //nghỉ không phép
                case FinanceConstant.ATTENDANCE_ABSENT_NO:
                    switch (attendanceDetail) {
                        case FinanceConstant.DAY_MORNING:
                            number = statistical.getMorningNo();
                            break;
                        case FinanceConstant.DAY_AFTERNOON:
                            number = statistical.getAfternoonNo();
                            break;
                        case FinanceConstant.DAY_EVENING:
                            number = statistical.getEveningNo();
                            break;
                        case FinanceConstant.ALL_DAY:
                            number = statistical.getAllDayNo();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 3");
                    }
                    break;
                //điểm danh ăn
                case FinanceConstant.ATTENDANCE_EAT:
                    switch (attendanceDetail) {
                        case FinanceConstant.EAT_BREAKFAST:
                            number = statistical.getEatMorning();
                            break;
                        case FinanceConstant.EAT_LUNCH:
                            number = statistical.getEatNoon();
                            break;
                        case FinanceConstant.EAT_DINNER:
                            number = statistical.getEatEvening();
                            break;
                        case FinanceConstant.ALL_DAY:
                            number = statistical.getEatAllDay();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 4");
                    }
                    break;
                //điểm danh đến muộn
                case FinanceConstant.ATTENDANCE_GO_SCHOOL:
                    switch (attendanceDetail) {
                        case FinanceConstant.ATTENDANCE_PICKUP_LATE:
                            number = statistical.getMinutesArriveLate();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 5");
                    }
                    break;
                //điểm danh về sớm
                case FinanceConstant.ATTENDANCE_LEAVE:
                    switch (attendanceDetail) {
                        case FinanceConstant.ATTENDANCE_PICKUP_SOON:
                            number = statistical.getMinutesLeaveSoon();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 6");
                    }
                    break;
                case FinanceConstant.ABSENT_COMMON:
                    switch (attendanceDetail) {
                        case FinanceConstant.ABSENT_TIME:
                            number = statistical.getAbsentTime();
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 7");
                    }
                    break;
                case FinanceConstant.ABSENT_DATE_TO_DATE:
                    switch (attendanceDetail) {
                        case FinanceConstant.ABSENT_DATE_TO_DATE_NO26:
                        case FinanceConstant.ABSENT_DATE_TO_DATE_YES_NO26:
                        case FinanceConstant.ABSENT_DATE_TO_DATE_NO78:
                        case FinanceConstant.ABSENT_DATE_TO_DATE_YES_NO78:
                            List<AttendanceEmployee> attendanceEmployeeList = attendanceEmployeeRepository.findByInfoEmployeeSchoolIdAndDateBetween(infoEmployeeSchool.getId(), startDate, endDate);
                            List<LocalDate> saturdaySundayListList = FinanceUltils.getSaturdaySundayList(startDate, endDate);
                            if (StringUtils.equalsAny(attendanceDetail, FinanceConstant.ABSENT_DATE_TO_DATE_NO26, FinanceConstant.ABSENT_DATE_TO_DATE_YES_NO26)) {
                                attendanceEmployeeList = attendanceEmployeeList.stream().filter(x -> saturdaySundayListList.stream().noneMatch(a -> a.equals(x.getDate()))).collect(Collectors.toList());
                            } else if (StringUtils.equalsAny(attendanceDetail, FinanceConstant.ABSENT_DATE_TO_DATE_NO78, FinanceConstant.ABSENT_DATE_TO_DATE_YES_NO78)) {
                                attendanceEmployeeList = attendanceEmployeeList.stream().filter(x -> saturdaySundayListList.stream().anyMatch(a -> a.equals(x.getDate()))).collect(Collectors.toList());
                            }
                            if (StringUtils.equalsAny(attendanceDetail, FinanceConstant.ABSENT_DATE_TO_DATE_NO26, FinanceConstant.ABSENT_DATE_TO_DATE_NO78)) {
                                long count1 = attendanceEmployeeList.stream().filter(x -> x.isMorningNo() && x.isAfternoonNo()).count();
                                long count2 = attendanceEmployeeList.stream().filter(x -> x.isMorningNo() || x.isAfternoonNo()).count();
                                float count3 = (float) ((count2 - count1) * 0.5);
                                number = count1 + count3;
                            } else if (StringUtils.equalsAny(attendanceDetail, FinanceConstant.ABSENT_DATE_TO_DATE_YES_NO26, FinanceConstant.ABSENT_DATE_TO_DATE_YES_NO78)) {
                                long count1 = attendanceEmployeeList.stream().filter(x -> (x.isMorningNo() || x.isMorningYes()) && (x.isAfternoonNo() || x.isAfternoonYes())).count();
                                long count2 = attendanceEmployeeList.stream().filter(x -> x.isMorningNo() || x.isMorningYes() || x.isAfternoonNo() || x.isAfternoonYes()).count();
                                float count3 = (float) ((count2 - count1) * 0.5);
                                number = count1 + count3;
                            }
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 8");
                    }
                    break;
                case FinanceConstant.GO_WORK_DATE_TO_DATE:
                    switch (attendanceDetail) {
                        case FinanceConstant.GO_WORK_DATE_TO_DATE26:
                        case FinanceConstant.GO_WORK_DATE_TO_DATE78:
                            List<AttendanceEmployee> attendanceEmployeeList = attendanceEmployeeRepository.findByInfoEmployeeSchoolIdAndDateBetween(infoEmployeeSchool.getId(), startDate, endDate);
                            List<LocalDate> saturdaySundayListList = FinanceUltils.getSaturdaySundayList(startDate, endDate);
                            if (attendanceDetail.equals(FinanceConstant.GO_WORK_DATE_TO_DATE26)) {
                                attendanceEmployeeList = attendanceEmployeeList.stream().filter(x -> saturdaySundayListList.stream().noneMatch(a -> a.equals(x.getDate()))).collect(Collectors.toList());
                            } else {
                                attendanceEmployeeList = attendanceEmployeeList.stream().filter(x -> saturdaySundayListList.stream().anyMatch(a -> a.equals(x.getDate()))).collect(Collectors.toList());
                            }
                            long count1 = attendanceEmployeeList.stream().filter(x -> x.isMorning() && x.isAfternoon()).count();
                            long count2 = attendanceEmployeeList.stream().filter(x -> x.isMorning() || x.isAfternoon()).count();
                            float count3 = (float) ((count2 - count1) * 0.5);
                            number = count1 + count3;
                            break;
                        default:
                            System.out.println(attendanceDetail);
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE + " 9");
                    }
                    break;
            }
        }
        return number;
    }


    @Override
    public boolean updateEmployeeSalary(UserPrincipal principal, EmployeeSalaryUpdateRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnEmployeeSalaryDefault saralyDefault = saralyDefaultRepository.findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(request.getId(), principal.getIdSchoolLogin()).orElseThrow();
        modelMapper.map(request, saralyDefault);
        saralyDefaultRepository.save(saralyDefault);
        return true;
    }

    @Override
    public boolean deleteEmployeeSalaryDefault(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        this.deleteSalaryDefaultCommon(id, principal.getIdSchoolLogin());
        return true;
    }

    @Override
    public boolean deleteEmployeeSalaryDefaultMany(UserPrincipal principal, List<Long> request) {
        Long idSchool = principal.getIdSchoolLogin();
        request.forEach(x -> this.deleteSalaryDefaultCommon(x, idSchool));
        return true;
    }

    private void deleteSalaryDefaultCommon(Long id, Long idSchool) {
        FnEmployeeSalaryDefault salaryDefault = saralyDefaultRepository.findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(id, idSchool).orElseThrow();
        salaryDefault.setDelActive(AppConstant.APP_FALSE);
        saralyDefaultRepository.save(salaryDefault);
    }

    @Transactional
    @Override
    public List<EmployeeStatisticalDefaultResponse> statisticalDefault(UserPrincipal principal, IdObjectRequest request) {
        CommonValidate.checkDataPlus(principal);
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        List<FnEmployeeSalaryDefault> salaryDefaultList = infoEmployeeSchool.getFnEmployeeSalaryDefaultList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        salaryDefaultList = salaryDefaultList.stream().sorted(Comparator.comparing(FnEmployeeSalaryDefault::getCategory).reversed()).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(listMapper.mapList(salaryDefaultList, EmployeeStatisticalDefaultResponse.class)) ? listMapper.mapList(salaryDefaultList, EmployeeStatisticalDefaultResponse.class) : new ArrayList<>();
    }

//    @Transactional
//    @Override
//    public boolean generateEmployeeSalary(UserPrincipal principal, EmployeeSalaryCreateRequest request) {
//        CommonValidate.checkDataPlus(principal);
//        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getIdInfoEmployee()).orElseThrow();
//        LocalDate monthGenerated = FinanceUltils.getDate(request.getStatus());
//        this.generateEmployeeSalaryCommon(infoEmployeeSchool, monthGenerated.getMonthValue(), monthGenerated.getYear(), principal.getId(), FinanceConstant.GENERATE_MANUAL);
//        return true;
//    }

    @Transactional
    @Override
    public int generateEmployeeSalaryCommon(InfoEmployeeSchool infoEmployeeSchool, int month, int year, Long idUser, String generateType) {
        //lấy ra các khoản chưa bị xóa và được chọn active=true
        AtomicInteger number = new AtomicInteger();
        List<FnEmployeeSalaryDefault> salaryDefaultList = infoEmployeeSchool.getFnEmployeeSalaryDefaultList().stream().filter(x -> x.isDelActive() && x.isActive()).collect(Collectors.toList());
        salaryDefaultList.forEach(x -> {
            FnMonthObject fnMonthObject = modelMapper.map(x, FnMonthObject.class);
            boolean checkMonth = FinanceUltils.getStatusOfMonth(month, fnMonthObject);
            if (checkMonth) {
                Optional<FnEmployeeSalary> fnEmployeeSalaryOld = fnEmployeeSalaryRepository.findByFnEmployeeSalaryDefaultIdAndYearAndMonthAndDelActiveTrue(x.getId(), year, month);
                if (fnEmployeeSalaryOld.isEmpty()) {
                    number.getAndIncrement();
                    FnEmployeeSalary fnEmployeeSalary = modelMapper.map(x, FnEmployeeSalary.class);
                    fnEmployeeSalary.setId(null);
                    fnEmployeeSalary.setYear(year);
                    fnEmployeeSalary.setMonth(month);
                    fnEmployeeSalary.setFnEmployeeSalaryDefault(x);
                    if (generateType.equals(FinanceConstant.GENERATE_AUTO)) {
                        fnEmployeeSalary.setIdCreated(idUser);
                        fnEmployeeSalary.setCreatedDate(LocalDateTime.now());
                        fnEmployeeSalary.setIdModified(idUser);
                        fnEmployeeSalary.setLastModifieDate(LocalDateTime.now());
                    }
                    fnEmployeeSalaryRepository.save(fnEmployeeSalary);
                }
            }
        });
        return number.get();
    }

    @Transactional
    @Override
    public int generateEmployeeSalaryFromDefault(UserPrincipal principal, GenerateEmployeeSalaryFromDefaultRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idUser = principal.getId();
        LocalDate date = request.getDate();
//        FinanceUltils.checkDateGenerateOrder(date);
        int month = date.getMonthValue();
        int year = date.getYear();
        int number = 0;
        for (Long x : request.getIdEmployeeList()) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(x, idSchool).orElseThrow();
            int count = this.generateEmployeeSalaryCommon(infoEmployeeSchool, month, year, idUser, FinanceConstant.GENERATE_MANUAL);
            number += count;
        }
        return number;
    }

    @Override
    public boolean generateEmployeeSalaryFromSchool(UserPrincipal principal, GenerateEmployeeSalaryFromSchoolRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate date = request.getDate();
//        FinanceUltils.checkDateGenerateOrder(date);
        int month = date.getMonthValue();
        int year = date.getYear();
        request.getIdEmployeeList().forEach(x -> {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(x, idSchool).orElseThrow();
            request.getIdSalaryList().forEach(y -> {
                FnSalary fnSalary = fnSalaryRepository.findByIdAndSchoolIdAndDelActiveTrue(y, idSchool).orElseThrow();
                FnEmployeeSalary fnEmployeeSalary = modelMapper.map(fnSalary, FnEmployeeSalary.class);
                fnEmployeeSalary.setId(null);
                fnEmployeeSalary.setMonth(month);
                fnEmployeeSalary.setYear(year);
                fnEmployeeSalary.setInfoEmployeeSchool(infoEmployeeSchool);
                fnEmployeeSalaryRepository.save(fnEmployeeSalary);
            });
        });
        return true;
    }

    @Transactional
    @Override
    public int generateEmployeeSalaryDefault(UserPrincipal principal, GenerateEmployeeSalaryDefaultRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        AtomicInteger number = new AtomicInteger();
        request.getIdEmployeeList().forEach(x -> {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(x, idSchool).orElseThrow();
            request.getIdSalaryList().forEach(y -> {
                Optional<FnEmployeeSalaryDefault> fnEmployeeSalaryDefaultOptional = fnEmployeeSalaryDefaultRepository.findByInfoEmployeeSchoolIdAndFnSalaryIdAndDelActiveTrue(x, y);
                if (fnEmployeeSalaryDefaultOptional.isEmpty()) {
                    number.getAndIncrement();
                    FnSalary fnSalary = fnSalaryRepository.findByIdAndSchoolIdAndDelActiveTrue(y, idSchool).orElseThrow();
                    FnEmployeeSalaryDefault fnEmployeeSalaryDefault = modelMapper.map(fnSalary, FnEmployeeSalaryDefault.class);
                    fnEmployeeSalaryDefault.setId(null);
                    fnEmployeeSalaryDefault.setFnSalary(fnSalary);
                    fnEmployeeSalaryDefault.setInfoEmployeeSchool(infoEmployeeSchool);
                    fnEmployeeSalaryDefaultRepository.save(fnEmployeeSalaryDefault);
                }
            });
        });
        return number.get();
    }


    @Override
    public List<EmployeeStatisticalApplyResponse> statisticalApply(UserPrincipal principal, EmployeeSalaryApplyRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<FnEmployeeSalary> data = fnEmployeeSalaryRepository.findPackageApply(request.getIdInfoEmployee(), request.getLocalDate());
        data = data.stream().sorted(Comparator.comparing(FnEmployeeSalary::getCategory).reversed()).collect(Collectors.toList());
        return listMapper.mapList(data, EmployeeStatisticalApplyResponse.class);
    }

    @Override
    public boolean deleteEmployeeSalaryApply(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        this.checkMultiple(fnEmployeeSalary);
        this.deleteEmployeeSalaryApplyCommon(fnEmployeeSalary);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteEmployeeSalaryApplyMany(UserPrincipal principal, List<Long> request) {
        Long idSchool = principal.getIdSchoolLogin();
        request.forEach(x -> {
            FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(x, idSchool).orElseThrow();
            if (!fnEmployeeSalary.isApproved() && !fnEmployeeSalary.isLocked() && fnEmployeeSalary.getPaid() == 0) {
                this.deleteEmployeeSalaryApplyCommon(fnEmployeeSalary);
            }
        });
        return true;
    }

    private void deleteEmployeeSalaryApplyCommon(FnEmployeeSalary fnEmployeeSalary) {
        fnEmployeeSalary.setDelActive(AppConstant.APP_FALSE);
        fnEmployeeSalaryRepository.save(fnEmployeeSalary);
    }

    @Override
    public boolean updateEmployeeSalaryApply(UserPrincipal principal, EmployeeSalaryApplyUpdateRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.checkMultiple(fnEmployeeSalary);
        modelMapper.map(request, fnEmployeeSalary);
        fnEmployeeSalaryRepository.save(fnEmployeeSalary);
        return true;
    }

    @Override
    public ActiveSalaryResponse activeSalaryDefault(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        ActiveSalaryResponse response = new ActiveSalaryResponse();
        FnEmployeeSalaryDefault fnEmployeeSalaryDefault = saralyDefaultRepository.findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        fnEmployeeSalaryDefault.setActive(fnEmployeeSalaryDefault.isActive() ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
        fnEmployeeSalaryDefault = saralyDefaultRepository.save(fnEmployeeSalaryDefault);
        response.setActive(fnEmployeeSalaryDefault.isActive() ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        return response;
    }

    @Override
    public FnEmployeeSalaryDefaultResponse searchSalaryDefault(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        return modelMapper.map(saralyDefaultRepository.findByIdAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(), FnEmployeeSalaryDefaultResponse.class);
    }

    @Override
    public EmployeeStatisticalApplyResponse searchSalaryApply(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        return modelMapper.map(fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(id).orElseThrow(), EmployeeStatisticalApplyResponse.class);
    }

    @Override
    public boolean createEmployeeSalaryCustom(UserPrincipal principal, Long idInfoEmployee, EmployeeSalaryApplyCreateRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
//        FinanceUltils.checkDateGenerateOrder(date);
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(idInfoEmployee, principal.getIdSchoolLogin()).orElseThrow();
        FnEmployeeSalary fnEmployeeSalary = modelMapper.map(request, FnEmployeeSalary.class);
        fnEmployeeSalary.setMonth(date.getMonthValue());
        fnEmployeeSalary.setYear(date.getYear());
        fnEmployeeSalary.setInfoEmployeeSchool(infoEmployeeSchool);
        fnEmployeeSalaryRepository.save(fnEmployeeSalary);
        return true;
    }

    @Override
    public List<SalarySampleResponse> searchSalarySample(UserPrincipal principal, String name) {
        CommonValidate.checkDataPlus(principal);
        List<FnSalary> dataList = fnSalaryRepository.findBySchoolWithName(principal.getIdSchoolLogin(), name);
        return listMapper.mapList(dataList, SalarySampleResponse.class);
    }

    @Override
    public List<FnSalarySampleBriefResponse> searchSalarySampleBrief(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<FnSalary> fnSalaryList = fnSalaryRepository.findBySchoolIdAndDelActiveTrueOrderByCategoryDesc(principal.getIdSchoolLogin());
        return listMapper.mapList(fnSalaryList, FnSalarySampleBriefResponse.class);
    }

    @Override
    public SalarySampleResponse detailSalarySample(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnSalary data = fnSalaryRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        return modelMapper.map(data, SalarySampleResponse.class);
    }


    @Override
    public boolean createSalarySample(UserPrincipal principal, SalarySampleCreateRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnSalary fnSalary = modelMapper.map(request, FnSalary.class);
        School school = schoolRepository.findByIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
        fnSalary.setSchool(school);
        fnSalaryRepository.save(fnSalary);
        return true;
    }

    @Override
    public boolean updateSalarySample(UserPrincipal principal, SalarySampleUpdateRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnSalary fnSalary = fnSalaryRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        modelMapper.map(request, fnSalary);
        fnSalaryRepository.save(fnSalary);
        return true;
    }

    @Override
    public boolean deleteSalarySample(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnSalary fnSalary = fnSalaryRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        fnSalary.setDelActive(AppConstant.APP_FALSE);
        fnSalaryRepository.save(fnSalary);
        return true;
    }

    @Override
    public boolean approvedSalary(UserPrincipal principal, IdListRequest request) {
        CommonValidate.checkDataPlus(principal);
        request.getIdList().forEach(x -> {
            FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            fnEmployeeSalary.setApproved(fnEmployeeSalary.isApproved() ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            fnEmployeeSalaryRepository.save(fnEmployeeSalary);
        });
        return true;
    }

    @Override
    public boolean approvedOneSalary(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        this.checkMoneyZero(fnEmployeeSalary);
        this.checkLocked(fnEmployeeSalary);
        this.checkPayment(fnEmployeeSalary);
        this.saveApproved(principal.getId(), !fnEmployeeSalary.isApproved(), fnEmployeeSalary);
        return fnEmployeeSalary.isApproved();
    }

    @Override
    public boolean approvedAllSalary(UserPrincipal principal, SalaryStatusRequest request) {
        CommonValidate.checkDataPlus(principal);
        boolean status = request.getStatus();
        request.getEmployeeList().forEach(x -> x.getFnEmployeeSalaryList().forEach(y -> {
            FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
            if (!fnEmployeeSalary.isLocked() && fnEmployeeSalary.getPaid() == 0 && fnEmployeeSalary.getUserNumber() > 0 && fnEmployeeSalary.getPrice() > 0) {
                this.saveApproved(principal.getId(), status, fnEmployeeSalary);
            }
        }));
        return true;
    }

    @Override
    public boolean lockedOneSalary(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        this.setPropertiesLock(principal.getId(), !fnEmployeeSalary.isLocked(), fnEmployeeSalary);
        return fnEmployeeSalary.isLocked();
    }

    @Override
    public boolean lockedAllSalary(UserPrincipal principal, SalaryStatusRequest request) {
        CommonValidate.checkDataPlus(principal);
        boolean status = request.getStatus();
        request.getEmployeeList().forEach(x -> x.getFnEmployeeSalaryList().forEach(y -> {
            FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
            this.setPropertiesLock(principal.getId(), status, fnEmployeeSalary);
        }));
        return true;
    }

    private void saveApproved(Long idUser, boolean status, FnEmployeeSalary fnEmployeeSalary) {
        if (fnEmployeeSalary.isApproved() != status) {
            fnEmployeeSalary.setApproved(status);
            fnEmployeeSalary.setIdApproved(idUser);
            fnEmployeeSalary.setTimeApproved(LocalDateTime.now());
            fnEmployeeSalaryRepository.save(fnEmployeeSalary);
        }
    }

    private void setPropertiesLock(Long idUser, boolean status, FnEmployeeSalary fnEmployeeSalary) {
        if (fnEmployeeSalary.isLocked() != status) {
            fnEmployeeSalary.setLocked(status);
            fnEmployeeSalary.setIdLocked(idUser);
            fnEmployeeSalary.setTimeLocked(LocalDateTime.now());
            fnEmployeeSalaryRepository.save(fnEmployeeSalary);
        }
    }

    @Override
    public boolean numberUser(UserPrincipal principal, SalaryNumberUserRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.checkMultiple(fnEmployeeSalary);
        fnEmployeeSalary.setUserNumber(request.getUserNumber());
        if (!this.checkMoneyZeroResult(fnEmployeeSalary)) {
            this.saveApproved(principal.getId(), AppConstant.APP_TRUE, fnEmployeeSalary);
        } else {
            fnEmployeeSalaryRepository.save(fnEmployeeSalary);
        }
        return true;
    }

    @Override
    public List<EmployeeSalaryPaidResponse> searchOrderEmployee(UserPrincipal principal, EmployeeSalaryApplySearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        int month = request.getDate().getMonthValue();
        int year = request.getDate().getYear();
        List<EmployeeSalaryPaidResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchEmployeeSalaryNew(principal.getIdSchoolLogin(), request.getEmployeeStatus(), request.getIdDepartment(), request.getEmployeeNameOrPhone());
        infoEmployeeSchoolList.forEach(x -> {
            Optional<FnOrderEmployee> fnOrderEmployee = fnOrderEmployeeRepository.findByInfoEmployeeSchoolIdAndMonthAndYear(x.getId(), month, year);
            EmployeeSalaryPaidResponse data = new EmployeeSalaryPaidResponse();
            if (fnOrderEmployee.isPresent()) {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), month, year);
                OrderEmployeeCustom1 model = new OrderEmployeeCustom1();
                model.setId(fnOrderEmployee.get().getId());
                model.setView(fnOrderEmployee.get().isView());
                model.setLocked(fnOrderEmployee.get().isLocked());
                model.setCode(fnOrderEmployee.get().getCode());
                OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
                OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnEmployeeSalaryList);
                modelMapper.map(orderNumberModel, model);
                modelMapper.map(orderMoneyModel, model);
                double moneyRemain = orderMoneyModel.getMoneyTotalInOutRemain();
                if (moneyRemain > 0) {
                    model.setTotalMoneyRemainOut(moneyRemain);
                } else {
                    model.setTotalMoneyRemainIn(Math.abs(moneyRemain));
                }
                data.setBillSampleResponse(model);
            }
            data.setFullName(x.getFullName());
            data.setPhone(x.getPhone());
            data.setId(x.getId());
            responseList.add(data);
        });
        return responseList;
    }


    @Override
    public List<EmployeeSalaryDetailYearResponse> searchOrderMonth(UserPrincipal principal, YearIdRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<EmployeeSalaryDetailYearResponse> responseList = new ArrayList<>();
        Long idInfoEmployee = request.getId();
        List<FnOrderEmployee> orderList = fnOrderEmployeeRepository.findByInfoEmployeeSchoolIdAndInfoEmployeeSchoolSchoolIdAndYearOrderByMonthDesc(idInfoEmployee, principal.getIdSchoolLogin(), request.getYear());
        orderList.forEach(x -> {
            List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idInfoEmployee, x.getMonth(), x.getYear());
            EmployeeSalaryDetailYearResponse model = modelMapper.map(x, EmployeeSalaryDetailYearResponse.class);
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
            OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnEmployeeSalaryList);
            modelMapper.map(orderNumberModel, model);
            modelMapper.map(orderMoneyModel, model);
            responseList.add(model);
        });
        return responseList;


    }

    @Override
    public boolean generateBill(UserPrincipal principal, SalaryMultiRequest request) {
        CommonValidate.checkDataPlus(principal);
//        FinanceUltils.checkDateGenerateOrder(request.getDate());
        for (Long x : request.getIdList()) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            this.generateBillCommon(infoEmployeeSchool, request.getDate(), FinanceConstant.GENERATE_MANUAL);
        }
        return true;
    }

    @Override
    public boolean sendNotifyOrder(UserPrincipal principal, SalaryMultiRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
//        FinanceUltils.checkDateGenerateOrder(date);
        Long idSchool = principal.getIdSchoolLogin();
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(72L).orElseThrow();
        String title = webSystemTitle.getTitle();
        for (Long x : request.getIdList()) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<FnOrderEmployee> fnOrderEmployeeList = infoEmployeeSchool.getFnOrderEmployeeList().stream().filter(a -> a.getMonth() == date.getMonthValue() && a.getYear() == date.getYear()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fnOrderEmployeeList)) {
                FnOrderEmployee fnOrderEmployee = fnOrderEmployeeList.get(0);
                List<FnEmployeeSalary> fnEmployeeSalaryList = FinanceUltils.getEmployeeSalaryListFromInfoEmployee(infoEmployeeSchool, date, AppConstant.APP_TRUE);
                List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                long totalMoneyIn = inList.stream().mapToLong(FinanceUltils::getMoneySalary).sum();
                long totalPaidIn = inList.stream().mapToLong(a -> (long) a.getPaid()).sum();
                long totalMoneyOut = outList.stream().mapToLong(FinanceUltils::getMoneySalary).sum();
                long totalPaidOut = outList.stream().mapToLong(a -> (long) a.getPaid()).sum();
                long totalMoney = totalMoneyOut - totalMoneyIn;
                long totalRemain = (totalMoneyOut - totalPaidOut) - (totalMoneyIn - totalPaidIn);
                String totalMoneyString = totalMoney >= 0 ? "được nhận" : "phải đóng";
                String totalRemainString = totalRemain >= 0 ? "được nhận" : "phải đóng";
                String content = webSystemTitle.getContent().replace("{order_code}", fnOrderEmployee.getCode()).replace("{in_out_salary}", totalMoneyString).replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderEmployee.getMonth(), fnOrderEmployee.getYear())).replace("{money_total}", FinanceUltils.formatMoney(Math.abs(totalMoney))).replace("{in_out}", totalRemainString).replace("{money_pay}", FinanceUltils.formatMoney(Math.abs(totalRemain)));
                appSendService.saveToAppSendEmployee(principal, infoEmployeeSchool, title, content, AppSendConstant.TYPE_FINANCE);
                firebaseFunctionService.sendTeacherCommon(Collections.singletonList(infoEmployeeSchool), title, content, idSchool, FirebaseConstant.CATEGORY_ORDER_NOTIFY);
                //gửi Sms
                if (webSystemTitle.isSms()) {
                    SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                    smsNotifyDataRequest.setSendContent(content);
                    smsDataService.sendSmsEmployee(Collections.singletonList(infoEmployeeSchool), idSchool, smsNotifyDataRequest, principal.getAppType());
                }
            }
        }
        return true;
    }

    @Override
    public void generateBillAuto(List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        LocalDate date = LocalDate.now();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
            this.generateBillCommon(x, date, FinanceConstant.GENERATE_AUTO);
        }
    }

    @Transactional
    @Override
    public boolean viewBill(UserPrincipal principal, StatusListRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        boolean status = request.getStatus();
        LocalDateTime dateTime = LocalDateTime.now();
        Long idUser = principal.getId();
        List<FnOrderEmployee> orderEmployeeList = fnOrderEmployeeRepository.findByIdInAndInfoEmployeeSchoolSchoolId(request.getIdList(), principal.getIdSchoolLogin());
        for (FnOrderEmployee x : orderEmployeeList) {
            if (status != x.isView()) {
                boolean sendFirebase = status && x.getIdView() == null;
                x.setIdView(idUser);
                x.setTimeView(dateTime);
                x.setView(status);
                fnOrderEmployeeRepository.save(x);
                if (sendFirebase) {
                    this.sendFirebaseOrderView(principal, x);
                }
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean lockedBill(UserPrincipal principal, StatusListRequest request) {
        CommonValidate.checkDataPlus(principal);
        boolean status = request.getStatus();
        LocalDateTime dateTime = LocalDateTime.now();
        Long idUser = principal.getId();
        List<FnOrderEmployee> orderEmployeeList = fnOrderEmployeeRepository.findByIdInAndInfoEmployeeSchoolSchoolId(request.getIdList(), principal.getIdSchoolLogin());
        orderEmployeeList.forEach(x -> {
            if (status != x.isLocked()) {
                x.setIdLocked(idUser);
                x.setTimeLock(dateTime);
                x.setLocked(status);
                fnOrderEmployeeRepository.save(x);
            }
        });
        return true;
    }

    @Override
    public FnEmployeeSalaryResponse searchOrderPayment(UserPrincipal principal, Long idOrder, BillPaidRequest request) {
        FnEmployeeSalaryResponse response = new FnEmployeeSalaryResponse();
        List<SalaryForPaymentResponse> dataList = new ArrayList<>();
        FnOrderEmployee fnOrderEmployee = fnOrderEmployeeRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment"));
        int year = fnOrderEmployee.getYear();
        int month = fnOrderEmployee.getMonth();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        List<FnEmployeeSalary> fnEmployeeSalary = fnEmployeeSalaryRepository.getEmployeeSalaryApprovedWithMonth(request.getIdInfoEmployee(), month, year, request.getCategory());
        for (FnEmployeeSalary x : fnEmployeeSalary) {
            SalaryForPaymentResponse model = modelMapper.map(x, SalaryForPaymentResponse.class);
            model.setMoney(FinanceUltils.getMoneySalary(x));
            if (request.getCategory().equals(FinanceConstant.CATEGOTY_BOTH) && x.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
                model.setMoney(model.getMoney() * (-1));
                model.setPaid(model.getPaid() * (-1));
            }
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        response.setMoneyTotal(moneyTotal);
        response.setMoneyTotalPaid(moneyTotalPaid);
        response.setDataList(dataList);
        response.setDateTime(fnOrderEmployee.getTimePayment());
        response.setDescription(fnOrderEmployee.getDescription());
        return response;
    }

    @Override
    public OrderPrintResponse getPrintOrder(UserPrincipal principal, BillPaidRequest request, IdListRequest idList) {
        OrderPrintResponse response = new OrderPrintResponse();
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(request.getIdInfoEmployee(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id infoemployee in school"));
        List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByIdInAndInfoEmployeeSchoolSchoolIdAndApprovedTrueAndDelActiveTrueOrderByCategoryDesc(idList.getIdList(), principal.getIdSchoolLogin());
        List<KidsPackageCustom2> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnEmployeeSalary x : fnEmployeeSalaryList) {
            KidsPackageCustom2 model = new KidsPackageCustom2();
            model.setName(x.getName());
            model.setMoney(FinanceUltils.getMoneySalary(x));
            model.setPaid(x.getPaid());
            model.setNumber(x.getUserNumber());
            model.setPrice(FinanceUltils.getPriceDataEmployee(x));
            if (request.getCategory().equals(FinanceConstant.CATEGOTY_BOTH) && x.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
                model.setMoney(model.getMoney() * (-1));
                model.setPaid(model.getPaid() * (-1));
            }
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        double moneyTotalRemain = moneyTotal - moneyTotalPaid;

        KidsPackageCustom2 model = new KidsPackageCustom2();
        model.setName("Tổng cộng");
        model.setMoney(moneyTotal);
        model.setPaid(moneyTotalPaid);
        model.setRemain(moneyTotalRemain);
        dataList.add(model);

        KidsPackageCustom2 model2 = new KidsPackageCustom2();
        model2.setName("Số tiền đã trả");
        model2.setMoney(moneyTotalPaid);
        dataList.add(model2);

        KidsPackageCustom2 model3 = new KidsPackageCustom2();
        model3.setName("Số tiền còn thiếu");
        model3.setMoney(moneyTotalRemain);
        dataList.add(model3);

        response.setDataList(dataList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<FnBank> fnBankList = school.getFnBankList().stream().filter(x -> x.isDelActive() && x.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fnBankList)) {
            FnBank fnBank = fnBankList.get(0);
            response.setBankNameBank(fnBank.getBankName());
            response.setAccountNumberBank(fnBank.getAccountNumber());
            response.setFullNameBank(fnBank.getFullName());
        }
        response.setFullName(infoEmployeeSchool.getFullName());
        response.setPhone(infoEmployeeSchool.getPhone());
        response.setBankInfo(school.getSchoolInfo().getNote());
        response.setUserCreate(principal.getFullName());
        response.setSchoolName(school.getSchoolName());
        response.setSchoolAddress(school.getSchoolAddress());
        return response;
    }

    @Override
    public OrderPrintResponse getPrintOrderIn(UserPrincipal principal, BillPaidRequest request, IdListRequest idList) {
        OrderPrintResponse response = new OrderPrintResponse();
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(request.getIdInfoEmployee(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id infoemployee in school"));
        List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByIdInAndInfoEmployeeSchoolSchoolIdAndApprovedTrueAndDelActiveTrueOrderByCategoryDesc(idList.getIdList(), principal.getIdSchoolLogin());
        List<KidsPackageCustom2> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnEmployeeSalary x : fnEmployeeSalaryList) {
            KidsPackageCustom2 model = new KidsPackageCustom2();
            model.setName(x.getName());
            model.setMoney(FinanceUltils.getMoneySalary(x));
            model.setPaid(x.getPaid());
            model.setNumber(x.getUserNumber());
            model.setPrice(FinanceUltils.getPriceDataEmployee(x));
//            if (request.getCategory().equals(FinanceConstant.CATEGORY_IN) && x.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
//                model.setMoney(model.getMoney() * (-1));
//                model.setPaid(model.getPaid() * (-1));
//            }
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        double moneyTotalRemain = moneyTotal - moneyTotalPaid;

        KidsPackageCustom2 model = new KidsPackageCustom2();
        model.setName("Tổng cộng");
        model.setMoney(moneyTotal);
        model.setPaid(moneyTotalPaid);
        model.setRemain(moneyTotalRemain);
        dataList.add(model);

        KidsPackageCustom2 model2 = new KidsPackageCustom2();
        model2.setName("Số tiền đã trả");
        model2.setMoney(moneyTotalPaid);
        dataList.add(model2);

        KidsPackageCustom2 model3 = new KidsPackageCustom2();
        model3.setName("Số tiền còn thiếu");
        model3.setMoney(moneyTotalRemain);
        dataList.add(model3);

        response.setDataList(dataList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<FnBank> fnBankList = school.getFnBankList().stream().filter(x -> x.isDelActive() && x.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fnBankList)) {
            FnBank fnBank = fnBankList.get(0);
            response.setBankNameBank(fnBank.getBankName());
            response.setAccountNumberBank(fnBank.getAccountNumber());
            response.setFullNameBank(fnBank.getFullName());
        }
        response.setFullName(infoEmployeeSchool.getFullName());
        response.setPhone(infoEmployeeSchool.getPhone());
        response.setBankInfo(school.getSchoolInfo().getNote());
        response.setUserCreate(principal.getFullName());
        response.setSchoolName(school.getSchoolName());
        response.setSchoolAddress(school.getSchoolAddress());
        return response;
    }

    @Override
    public OrderPrintResponse getPrintOrderOut(UserPrincipal principal, BillPaidRequest request, IdListRequest idList) {
        OrderPrintResponse response = new OrderPrintResponse();
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(request.getIdInfoEmployee(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id infoemployee in school"));
        List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByIdInAndInfoEmployeeSchoolSchoolIdAndApprovedTrueAndDelActiveTrueOrderByCategoryDesc(idList.getIdList(), principal.getIdSchoolLogin());
        List<KidsPackageCustom2> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnEmployeeSalary x : fnEmployeeSalaryList) {
            KidsPackageCustom2 model = new KidsPackageCustom2();
            model.setName(x.getName());
            model.setMoney(FinanceUltils.getMoneySalary(x));
            model.setPaid(x.getPaid());
            model.setNumber(x.getUserNumber());
            model.setPrice(FinanceUltils.getPriceDataEmployee(x));
//            if (request.getCategory().equals(FinanceConstant.CATEGORY_OUT) && x.getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
//                model.setMoney(model.getMoney() * (-1));
//                model.setPaid(model.getPaid() * (-1));
//            }
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        double moneyTotalRemain = moneyTotal - moneyTotalPaid;

        KidsPackageCustom2 model = new KidsPackageCustom2();
        model.setName("Tổng cộng");
        model.setMoney(moneyTotal);
        model.setPaid(moneyTotalPaid);
        model.setRemain(moneyTotalRemain);
        dataList.add(model);

        KidsPackageCustom2 model2 = new KidsPackageCustom2();
        model2.setName("Số tiền đã trả");
        model2.setMoney(moneyTotalPaid);
        dataList.add(model2);

        KidsPackageCustom2 model3 = new KidsPackageCustom2();
        model3.setName("Số tiền còn thiếu");
        model3.setMoney(moneyTotalRemain);
        dataList.add(model3);

        response.setDataList(dataList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<FnBank> fnBankList = school.getFnBankList().stream().filter(x -> x.isDelActive() && x.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fnBankList)) {
            FnBank fnBank = fnBankList.get(0);
            response.setBankNameBank(fnBank.getBankName());
            response.setAccountNumberBank(fnBank.getAccountNumber());
            response.setFullNameBank(fnBank.getFullName());
        }
        response.setFullName(infoEmployeeSchool.getFullName());
        response.setPhone(infoEmployeeSchool.getPhone());
        response.setBankInfo(school.getSchoolInfo().getNote());
        response.setUserCreate(principal.getFullName());
        response.setSchoolName(school.getSchoolName());
        response.setSchoolAddress(school.getSchoolAddress());
        return response;
    }

    @Transactional
    @Override
    public boolean orderSalaryPayment(UserPrincipal principal, Long idOrder, OrderSalaryPaymentRequest request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        String category = request.getCategory();
        Long idSchool = principal.getIdSchoolLogin();
        boolean showOrder = principal.getSchoolConfig().isAutoLockSalarySuccess();
        FnOrderEmployee fnOrderEmployee = fnOrderEmployeeRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment"));
        this.checkOrder(fnOrderEmployee, request.getDateTime());
        this.checkInputInfo(request);
        List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByIdInAndDelActiveTrue(request.getIdEmployeeSalaryList());
        //check số khoản thu truyền vào có bằng số tìm kiếm được trong DB hay không
        if (fnEmployeeSalaryList.size() < request.getIdEmployeeSalaryList().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_ENOUGH_PACKAGE);
        }
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(request.getIdInfoEmployee(), idSchool).orElseThrow();
        //số tiền thanh toán trong mỗi lần thanh toán
        double moneyPaymentAll = 0;
        //với kiểu thanh toán tổng hợp có cả thu và chi
        if (category.equals(FinanceConstant.CATEGOTY_BOTH)) {
            List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
            List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
            long moneyOut = outList.stream().mapToLong(x -> (long) (FinanceUltils.getMoneySalary(x) - x.getPaid())).sum();
            long moneyIn = inList.stream().mapToLong(x -> (long) (FinanceUltils.getMoneySalary(x) - x.getPaid())).sum();
            if (moneyIn > moneyOut) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PAYMENT_NEGATIVE);
            }
            OrderSalaryPaymentRequest requestOut = new OrderSalaryPaymentRequest();
            OrderSalaryPaymentRequest requestIn = new OrderSalaryPaymentRequest();
            modelMapper.map(request, requestOut);
            modelMapper.map(request, requestIn);
            //thanh toán khoản chi trước, tiền sẽ được cho vào ví
            double moneyPayOut = 0;
            //thanh toán khoản thu
            if (CollectionUtils.isNotEmpty(inList)) {
                requestIn.setCategory(FinanceConstant.CATEGORY_IN);
                requestIn.setMoneyInput(moneyIn);
                moneyPaymentAll -= this.paymentOrderWithCategory(principal, inList, requestIn, fnOrderEmployee, showOrder);
            }
            if (CollectionUtils.isNotEmpty(outList)) {
                requestOut.setCategory(FinanceConstant.CATEGORY_OUT);
                moneyPayOut = request.getMoneyInput() + moneyIn;
                requestOut.setMoneyInput(moneyPayOut);
                moneyPaymentAll += this.paymentOrderWithCategory(principal, outList, requestOut, fnOrderEmployee, showOrder);
            }

        } else {
            //trường hợp thanh toán thu, chi riêng
            moneyPaymentAll += this.paymentOrderWithCategory(principal, fnEmployeeSalaryList, request, fnOrderEmployee, showOrder);
        }
        //send firebase and sms
        this.sendFirebaseAndSmsOrderPayment(principal, infoEmployeeSchool, request.getCategory(), (long) moneyPaymentAll, fnOrderEmployee.getCode(), idSchool);
        return true;
    }

    private void generateBillCommon(InfoEmployeeSchool infoEmployeeSchool, LocalDate date, String generateType) {
        int month = date.getMonthValue();
        int year = date.getYear();
        List<FnOrderEmployee> fnOrderEmployeeList = infoEmployeeSchool.getFnOrderEmployeeList().stream().filter(x -> x.getMonth() == month && x.getYear() == year).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fnOrderEmployeeList)) {
            FnOrderEmployee fnOrderEmployee = new FnOrderEmployee();
            fnOrderEmployee.setYear(year);
            fnOrderEmployee.setMonth(month);
            fnOrderEmployee.setInfoEmployeeSchool(infoEmployeeSchool);
            fnOrderEmployee.setCode(this.getCodeOrder(date));
            if (FinanceConstant.GENERATE_AUTO.equals(generateType)) {
                fnOrderEmployee.setIdCreated(1L);
                fnOrderEmployee.setCreatedDate(LocalDateTime.now());
                fnOrderEmployee.setIdModified(1L);
                fnOrderEmployee.setLastModifieDate(LocalDateTime.now());
            }
            fnOrderEmployeeRepository.save(fnOrderEmployee);
        }
    }

    private void sendFirebaseOrderView(UserPrincipal principal, FnOrderEmployee fnOrderEmployee) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        Long idSchool = principal.getIdSchoolLogin();
        InfoEmployeeSchool infoEmployeeSchool = fnOrderEmployee.getInfoEmployeeSchool();
        LocalDate date = LocalDate.of(fnOrderEmployee.getYear(), fnOrderEmployee.getMonth(), 1);
        List<FnEmployeeSalary> fnEmployeeSalaryList = FinanceUltils.getEmployeeSalaryListFromInfoEmployee(infoEmployeeSchool, date, AppConstant.APP_TRUE);
        long money = this.getMoneyTotalInMinusOut(fnEmployeeSalaryList);
        String inOutString = money >= 0 ? "được nhận" : "phải đóng";
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(20L).orElseThrow();
        String title = webSystemTitle.getTitle();
        String content = webSystemTitle.getContent().replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderEmployee.getMonth(), fnOrderEmployee.getYear())).replace("{in_out_salary}", inOutString).replace("{money}", FinanceUltils.formatMoney(Math.abs(money)));
        appSendService.saveToAppSendEmployee(principal, infoEmployeeSchool, title, content, AppSendConstant.TYPE_FINANCE);
        firebaseFunctionService.sendTeacherCommon(Collections.singletonList(infoEmployeeSchool), title, content, idSchool, FirebaseConstant.CATEGORY_ORDER_SHOW);
        //gửi Sms
        if (webSystemTitle.isSms()) {
            SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
            smsNotifyDataRequest.setSendContent(content);
            smsDataService.sendSmsEmployee(Collections.singletonList(infoEmployeeSchool), idSchool, smsNotifyDataRequest, principal.getAppType());
        }
    }

    /**
     * lấy tổng chi trừ tổng thu
     *
     * @param fnEmployeeSalaryList
     * @return
     */
    private long getMoneyTotalInMinusOut(List<FnEmployeeSalary> fnEmployeeSalaryList) {
        List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
        List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
        long moneyTotalIn = inList.stream().mapToLong(FinanceUltils::getMoneySalary).sum();
        long moneyTotalOut = outList.stream().mapToLong(FinanceUltils::getMoneySalary).sum();
        return moneyTotalOut - moneyTotalIn;
    }

    private void sendFirebaseAndSmsOrderPayment(UserPrincipal principal, InfoEmployeeSchool infoEmployeeSchool, String category, long money, String orderCode, Long idSchool) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(21L).orElseThrow();
        String title = webSystemTitle.getTitle();
        String categoryType = category.equals(FinanceConstant.CATEGORY_IN) ? "thu" : "trả";
        String content = webSystemTitle.getContent().replace("{in_out_salary}", categoryType).replace("{money}", FinanceUltils.formatMoney(money)).replace("{order_code}", orderCode);
        appSendService.saveToAppSendEmployee(principal, infoEmployeeSchool, title, content, AppSendConstant.TYPE_FINANCE);
        //gửi firebase
        firebaseFunctionService.sendTeacherCommon(Collections.singletonList(infoEmployeeSchool), title, content, idSchool, FirebaseConstant.CATEGORY_ORDER_PAYMENT);
        //gửi Sms
        if (webSystemTitle.isSms()) {
            SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
            smsNotifyDataRequest.setSendContent(content);
            smsDataService.sendSmsEmployee(Collections.singletonList(infoEmployeeSchool), infoEmployeeSchool.getSchool().getId(), smsNotifyDataRequest, principal.getAppType());
        }
    }

    private double paymentOrderWithCategory(UserPrincipal principal, List<FnEmployeeSalary> fnEmployeeSalaryList, OrderSalaryPaymentRequest request, FnOrderEmployee fnOrderEmployee, boolean orderLocked) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idUser = principal.getId();
        String name = request.getName();
        LocalDate date = request.getDate();
        String category = request.getCategory();
        /**
         *  (1) create cashbook history với money=0
         */
        CashBookHistory cashBookHistory = cashBookHistoryService.saveCashBookHistory(idSchool, category, FinanceConstant.CASH_BOOK_EMP, date, 0, FinanceConstant.CASH_BOOK_ORIGIN_EMPLOYEE_PAYMENT, null);
        /**
         * (2) create order_employee_history
         */
        OrderEmployeeHistory orderEmployeeHistory = orderEmployeeHistoryService.saveOrderEmployeeHistory(category, name, date, request.getMoneyInput(), request.getDescription(), cashBookHistory, fnOrderEmployee);

        double moneyInputTotal = request.getMoneyInput();
        double moneyPaymentTotal = 0;
        /**
         * (3) tạo bảng ex_order_history_employee_salary và update paid trong employee_salary
         */
        for (FnEmployeeSalary x : fnEmployeeSalaryList) {
            //số tiền còn thiếu của khoản thu
            double moneyRemain = FinanceUltils.getMoneySalary(x) - x.getPaid();
            //số tiền thanh toán cho khoản thu
            double moneyPayment = Math.min(moneyInputTotal, moneyRemain);
            if (moneyPayment == 0) {
                break;
            }
            moneyInputTotal = moneyInputTotal - moneyPayment;
            moneyPaymentTotal = moneyPaymentTotal + moneyPayment;
            x.setPaid(x.getPaid() + moneyPayment);
            ExOrderHistoryEmployeeSalary exOrderHistoryEmployeeSalary = new ExOrderHistoryEmployeeSalary();
            exOrderHistoryEmployeeSalary.setMoney(moneyPayment);
            exOrderHistoryEmployeeSalary.setFnEmployeeSalary(x);
            exOrderHistoryEmployeeSalary.setOrderEmployeeHistory(orderEmployeeHistory);
            //create ex_order_history_employee_salary
            exOrderHistoryEmployeeSalaryRepository.save(exOrderHistoryEmployeeSalary);
            //update employee_salary
            //khi thanh toán đủ, khoản thu auto khóa
            if (moneyPayment == moneyRemain) {
//                schoolConfigResponse.isAutoLockSalarySuccess()
                if (orderLocked) {
                    x.setLocked(AppConstant.APP_TRUE);
                    x.setIdLocked(idUser);
                    x.setTimeLocked(LocalDateTime.now());
                }
            }
            fnEmployeeSalaryRepository.save(x);
        }
        /**
         * (4) update money trong fn_cash_book_history update cash_book
         */
        cashBookHistory.setMoney(moneyPaymentTotal);
        cashBookHistoryService.updateCashBookHistory(cashBookHistory);

        /**
         * (5) update lastModifiedDate in fnOrderEmployee
         */
        fnOrderEmployee.setTimePayment(LocalDateTime.now());
        fnOrderEmployeeRepository.save(fnOrderEmployee);
        return moneyPaymentTotal;
    }

    @Override
    public boolean saveUseNumberMany(UserPrincipal principal, List<SaveNumberManyEmployeeRequest> requestList) {
        requestList.forEach(x -> x.getFnEmployeeSalaryList().forEach(y -> {
            FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
            if (!fnEmployeeSalary.isApproved() && !fnEmployeeSalary.isLocked() && fnEmployeeSalary.getPaid() == 0) {
                fnEmployeeSalary.setUserNumber(y.getUserNumber());
                if (!this.checkMoneyZeroResult(fnEmployeeSalary)) {
                    this.saveApproved(principal.getId(), AppConstant.APP_TRUE, fnEmployeeSalary);
                } else {
                    fnEmployeeSalaryRepository.save(fnEmployeeSalary);
                }
//                fnEmployeeSalaryRepository.save(fnEmployeeSalary);
            }
        }));
        return true;
    }

    @Override
    public boolean saveTransferNumberMany(UserPrincipal principal, List<TransferNumberManyEmployeeRequest> requestList) {
        requestList.forEach(x -> x.getFnEmployeeSalaryList().forEach(y -> {
            FnEmployeeSalary fnEmployeeSalary = fnEmployeeSalaryRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
            if (!fnEmployeeSalary.isApproved() && !fnEmployeeSalary.isLocked() && fnEmployeeSalary.getPaid() == 0) {
                fnEmployeeSalary.setUserNumber(y.getCalculateNumber());
                if (!this.checkMoneyZeroResult(fnEmployeeSalary)) {
                    this.saveApproved(principal.getId(), AppConstant.APP_TRUE, fnEmployeeSalary);
                } else {
                    fnEmployeeSalaryRepository.save(fnEmployeeSalary);
                }
            }
        }));
        return true;
    }

    private void checkOrder(FnOrderEmployee fnOrderEmployee, LocalDateTime dateTime) {
        //check trạng thái khóa của hóa đơn
        if (fnOrderEmployee.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ORDER_LOCKED);
        }

        //check thời gian chỉnh sửa hóa đơn
        if (!dateTime.isEqual(fnOrderEmployee.getTimePayment())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ORDER_MODIFIED);
        }

    }

    /**
     * kiểm tra thông tin trước khi thanh toán
     *
     * @param request
     */
    private void checkInputInfo(OrderSalaryPaymentRequest request) {
        if (request.getMoneyInput() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PAYMENT_ZERO);
        }
    }

    private String getCodeOrder(LocalDate date) {
        String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue() : String.valueOf(date.getMonthValue());
        String year = String.valueOf(date.getYear());
        String code = "E".concat(month).concat(year).concat("-");
        String index;
        Optional<FnOrderEmployee> orderEmployeeOptional = fnOrderEmployeeRepository.findFirstByOrderByIdDesc();
        if (orderEmployeeOptional.isEmpty()) {
            index = "1";
        } else {
            String codeBefore = orderEmployeeOptional.get().getCode();
            int indexBefore = Integer.parseInt(codeBefore.substring(codeBefore.lastIndexOf("-") + 1));
            index = String.valueOf(indexBefore + 1);
        }
        return code.concat(index);
    }

    private BillSampleResponse setPropertiesBill(List<FnEmployeeSalary> fnEmployeeSalaryList) {
        BillSampleResponse data = new BillSampleResponse();
        double totalIn = 0;
        double totalOut = 0;
        double paidOut = 0;
        double paidIn = 0;
        int checkApproved = 0;
        int checkLocked = 0;
        int noPaid = 0;
        int yesPaid = 0;
        int lackPaid = 0;
        double moneyPackageIn = 0;
        double moneyPackageOut = 0;
        fnEmployeeSalaryList = fnEmployeeSalaryList.stream().filter(x -> x.isApproved()).collect(Collectors.toList());
        for (FnEmployeeSalary k : fnEmployeeSalaryList) {
            if (k.isApproved()) {
                if (k.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
                    moneyPackageIn = FinanceUltils.getMoneySalary(k);
                    totalIn += moneyPackageIn;
                    paidIn += k.getPaid();
                }
                if (k.getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                    moneyPackageOut = FinanceUltils.getMoneySalary(k);
                    totalOut += moneyPackageOut;
                    paidOut += k.getPaid();
                }
                if (k.getPaid() == 0) {
                    noPaid++;
                } else if (k.getPaid() == moneyPackageIn || k.getPaid() == moneyPackageOut) {
                    yesPaid++;
                } else if (k.getPaid() < moneyPackageIn || k.getPaid() < moneyPackageOut) {
                    lackPaid++;
                }
            }
            checkApproved += k.isApproved() ? 1 : 0;
            checkLocked += k.isLocked() ? 1 : 0;
        }
        data.setPaidIn(paidIn);
        data.setPaidOut(paidOut);
        data.setNoPaid(noPaid);
        data.setYesPaid(yesPaid);
        data.setLackPaid(lackPaid);
        data.setNumberApproved(checkApproved + "/" + fnEmployeeSalaryList.size());
        data.setNumberLocked(checkLocked + "/" + fnEmployeeSalaryList.size());
        data.setApproved(checkApproved == fnEmployeeSalaryList.size() ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        data.setTotalIn(totalIn);
        data.setTotalOut(totalOut);
        return data;
    }

    private void checkApproved(FnEmployeeSalary fnEmployeeSalary) {
        if (fnEmployeeSalary.isApproved()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPROVED_SALARY);
        }
    }

    private void checkLocked(FnEmployeeSalary fnEmployeeSalary) {
        if (fnEmployeeSalary.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.LOCKED_SALARY);
        }
    }

    private void checkMoneyZero(FnEmployeeSalary fnEmployeeSalary) {
        if (fnEmployeeSalary.getUserNumber() == 0 || fnEmployeeSalary.getPrice() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ZERO_MONEY);
        }
    }

    private void checkPayment(FnEmployeeSalary fnEmployeeSalary) {
        if (fnEmployeeSalary.getPaid() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PAYMENT_KIDS_PACKAGE);
        }
    }

    private void checkMultiple(FnEmployeeSalary fnEmployeeSalary) {
        this.checkApproved(fnEmployeeSalary);
        this.checkLocked(fnEmployeeSalary);
        this.checkPayment(fnEmployeeSalary);
    }

    private boolean checkRequestMonth(EmployeeSalaryDefaultCreateRequest request) {
        return request.isT1() || request.isT2() || request.isT3() || request.isT4() || request.isT5() || request.isT6() || request.isT7() || request.isT8() || request.isT9() || request.isT10() || request.isT11() || request.isT12();
    }

    private boolean checkMoneyZeroResult(FnEmployeeSalary fnEmployeeSalary) {
        //các khoản có giá bằng 0 thì ko cho duyệt
        if (fnEmployeeSalary.getUserNumber() == 0 || fnEmployeeSalary.getPrice() == 0) {
            return true;
        }
        //các khoản đã duyệt thì ko được duyệt thêm
        if (fnEmployeeSalary.isApproved()) {
            return true;
        }
        return false;
    }
}
