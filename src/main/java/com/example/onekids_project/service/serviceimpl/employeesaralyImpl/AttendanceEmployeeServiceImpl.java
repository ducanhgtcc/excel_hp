package com.example.onekids_project.service.serviceimpl.employeesaralyImpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.employee.*;
import com.example.onekids_project.importexport.model.AttendanceEmployeeDate;
import com.example.onekids_project.importexport.model.AttendanceEmployeeDetailMonth;
import com.example.onekids_project.importexport.model.AttendanceEmployeeMonth;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AttendanceEmployeeRepository;
import com.example.onekids_project.repository.ConfigAttendanceEmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.request.attendanceemployee.*;
import com.example.onekids_project.request.employeeSalary.AttendanceEmployeeConfigRequest;
import com.example.onekids_project.request.employeeSalary.EmployeeConfigSearchRequest;
import com.example.onekids_project.response.attendanceemployee.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.eclipse.jetty.util.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * date 2021-03-08 1:59 CH
 *
 * @author ADMIN
 */
@Service
public class AttendanceEmployeeServiceImpl implements AttendanceEmployeeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private ConfigAttendanceEmployeeRepository configAttendanceEmployeeRepository;

    @Autowired
    private AttendanceEmployeeRepository attendanceEmployeeRepository;

    @Override
    public List<AttendanceConfigEmployeeResponse> searchAttendanceConfigEmployee(UserPrincipal principal, EmployeeConfigSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<AttendanceConfigEmployeeResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchEmployeeSalaryNew(principal.getIdSchoolLogin(), request.getEmployeeStatus(), request.getIdDepartment(), request.getEmployeeNameOrPhone());
        infoEmployeeSchoolList.forEach(x -> {
            AttendanceConfigEmployeeResponse data = new AttendanceConfigEmployeeResponse();
            data.setFullName(x.getFullName());
            data.setPhone(x.getPhone());
            data.setId(x.getId());
            Optional<ConfigAttendanceEmployee> configAttendanceEmployeeOptional = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdOrderByCreatedDateDesc(x.getId());
            configAttendanceEmployeeOptional.ifPresent(attendanceEmployee -> data.setAttendanceConfig(modelMapper.map(attendanceEmployee, AttendanceConfigSampleResponse.class)));
            responseList.add(data);
        });
        return responseList;
    }

    @Override
    public boolean updateAttendanceConfigEmployee(UserPrincipal principal, Long id, AttendanceEmployeeConfigRequest request) {
        CommonValidate.checkDataPlus(principal);
        Optional<ConfigAttendanceEmployee> configAttendanceEmployee = configAttendanceEmployeeRepository.findByInfoEmployeeSchoolIdAndDate(id, LocalDate.now());
        if (configAttendanceEmployee.isPresent()) {
            ConfigAttendanceEmployee configAttendanceEmployeeUpdate = configAttendanceEmployee.get();
            modelMapper.map(request, configAttendanceEmployeeUpdate);
            configAttendanceEmployeeRepository.save(configAttendanceEmployeeUpdate);
        } else {
            ConfigAttendanceEmployee configAttendanceEmployeeNew = modelMapper.map(request, ConfigAttendanceEmployee.class);
            configAttendanceEmployeeNew.setId(null);
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
            configAttendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
            configAttendanceEmployeeRepository.save(configAttendanceEmployeeNew);
        }
        return true;
    }

    @Override
    public List<AttendanceArriveEmployeeResponse> searchAttendanceArrive(UserPrincipal principal, AttendanceEmployeeSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate date = request.getDate();
        List<AttendanceArriveEmployeeResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDateAndNameOrPhone(idSchool, date, request.getNameOrPhone());
        AttendanceTimeConfigResponse schoolConfig = modelMapper.map(principal.getSchoolConfig(), AttendanceTimeConfigResponse.class);
        infoEmployeeSchoolList.forEach(x -> {
            AttendanceArriveEmployeeResponse data = new AttendanceArriveEmployeeResponse();
            data.setAttendanceTimeConfig(schoolConfig);
            data.setAttendanceInfoEmployee(modelMapper.map(x, AttendanceInfoEmployeeResponse.class));
            Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            data.setAttendanceConfig(attendanceEmployeeConfigResponse);
            boolean check = this.checkAttendance(attendanceEmployee);
            data.setArrive(check);

            data.setArriveEmployeeDate(new AttendanceArriveEmployeeDateResponse());
            if (request.getStatus() != null && request.getStatus() == AppConstant.APP_TRUE && check) {
                data.setArriveEmployeeDate(modelMapper.map(attendanceEmployee.get(), AttendanceArriveEmployeeDateResponse.class));
                responseList.add(data);
            } else if (request.getStatus() != null && request.getStatus() == AppConstant.APP_FALSE && !check) {
                data.setArriveEmployeeDate(new AttendanceArriveEmployeeDateResponse());
                responseList.add(data);
            } else if (request.getStatus() == null) {
                data.setArriveEmployeeDate(attendanceEmployee.isPresent() ? modelMapper.map(attendanceEmployee.get(), AttendanceArriveEmployeeDateResponse.class) : new AttendanceArriveEmployeeDateResponse());
                responseList.add(data);
            }
            data.getArriveEmployeeDate().setIdInfo(x.getId());
            data.setShowEdit(data.getArriveEmployeeDate().getArriveTime() != null);
        });
        return responseList;
    }

    @Override
    public List<AttendanceDetailDayEmployeeResponse> searchAttendanceDetailDay(UserPrincipal principal, AttendanceEmployeeSearchRequest request) {
        LocalDate date = request.getDate();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDateAndNameOrPhone(principal.getIdSchoolLogin(), date, request.getNameOrPhone());
        List<AttendanceDetailDayEmployeeResponse> responseList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x -> {
            Optional<AttendanceEmployee> attendanceDetail = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            AttendanceDetailDayEmployeeResponse data = new AttendanceDetailDayEmployeeResponse();
            data.setInfoEmployeeResponse(modelMapper.map(x, AttendanceInfoEmployeeResponse.class));
            boolean check = this.checkAttendance(attendanceDetail);
            data.setArrive(check);
            if (request.getStatus() != null && request.getStatus() == AppConstant.APP_TRUE && check) {
                data.setAttendanceDetail(modelMapper.map(attendanceDetail.get(), AttendanceDetailResponse.class));
                responseList.add(data);
            } else if (request.getStatus() != null && request.getStatus() == AppConstant.APP_FALSE && !check) {
                data.setAttendanceDetail(new AttendanceDetailResponse());
                responseList.add(data);
            } else if (request.getStatus() == null) {
                data.setAttendanceDetail(attendanceDetail.isPresent() ? modelMapper.map(attendanceDetail.get(), AttendanceDetailResponse.class) : new AttendanceDetailResponse());
                responseList.add(data);
            }

        });
        return responseList;
    }

    @Override
    public boolean saveAttendanceArrive(UserPrincipal principal, AttendanceEmployeeArriveRequest request) {
        CommonValidate.checkDataPlus(principal);
        this.saveAttendanceEmployee(request, principal);
        return true;
    }

    @Override
    public boolean saveContentAttendanceArrive(UserPrincipal principal, AttendanceEmployeeContentRequest request) throws IOException {
        CommonValidate.checkDataPlus(principal);
        AttendanceEmployee attendanceEmployee = attendanceEmployeeRepository.findById(request.getId()).orElseThrow();
        if (request.getMultipartFile() != null) {
            HandleFileUtils.deletePictureInFolder(attendanceEmployee.getArriveLocalPicture());
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getMultipartFile(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
            attendanceEmployee.setArrivePicture(handleFileResponse.getUrlWeb());
            attendanceEmployee.setArriveLocalPicture(handleFileResponse.getUrlLocal());
        }
        attendanceEmployee.setArriveContent(StringUtil.isNotBlank(request.getArriveContent()) ? request.getArriveContent() : null);
        attendanceEmployeeRepository.save(attendanceEmployee);
        return true;
    }

    @Override
    public boolean saveMultiAttendanceArrive(UserPrincipal principal, List<AttendanceEmployeeArriveRequest> request) {
        CommonValidate.checkDataPlus(principal);
        request.forEach(x -> {
            this.saveAttendanceEmployee(x, principal);
        });
        return true;
    }

    @Override
    public List<AttendanceLeaveEmployeeResponse> searchAttendanceLeave(UserPrincipal principal, AttendanceEmployeeSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate date = request.getDate();
        List<AttendanceLeaveEmployeeResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDateAndNameOrPhone(idSchool, date, request.getNameOrPhone());
        AttendanceTimeConfigResponse schoolConfig = modelMapper.map(principal.getSchoolConfig(), AttendanceTimeConfigResponse.class);
        infoEmployeeSchoolList.forEach(x -> {
            AttendanceLeaveEmployeeResponse data = new AttendanceLeaveEmployeeResponse();
            data.setAttendanceTimeConfig(schoolConfig);
            data.setAttendanceInfoEmployee(modelMapper.map(x, AttendanceInfoEmployeeResponse.class));
            Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            boolean isArrive = this.checkAttendanceArrive(attendanceEmployee);
            data.setStatus(this.checkAttendance(attendanceEmployee));

            data.setArriveEmployeeDate(isArrive ? modelMapper.map(attendanceEmployee.get(), AttendanceArriveEmployeeDateResponse.class) : null);


            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            data.setAttendanceEmployeeConfig(attendanceEmployeeConfigResponse);
            data.setArrive(isArrive);
            data.setAttendanceLeaveEmployee(new AttendanceLeaveEmployeeDateResponse());
            if (request.getStatus() != null && request.getStatus() == AppConstant.APP_TRUE && isArrive && attendanceEmployee.get().getLeaveTime() != null) {
                data.setAttendanceLeaveEmployee(modelMapper.map(attendanceEmployee.get(), AttendanceLeaveEmployeeDateResponse.class));
                data.getAttendanceLeaveEmployee().setLeave(AppConstant.APP_TRUE);
                responseList.add(data);
            } else if (request.getStatus() != null && request.getStatus() == AppConstant.APP_FALSE && (!isArrive || (isArrive && attendanceEmployee.get().getLeaveTime() == null))) {
                data.setAttendanceLeaveEmployee(new AttendanceLeaveEmployeeDateResponse());
                responseList.add(data);
            } else if (request.getStatus() == null) {
                data.setAttendanceLeaveEmployee(isArrive ? modelMapper.map(attendanceEmployee.get(), AttendanceLeaveEmployeeDateResponse.class) : new AttendanceLeaveEmployeeDateResponse());
                data.getAttendanceLeaveEmployee().setLeave((isArrive && attendanceEmployee.get().getLeaveTime() != null) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                responseList.add(data);
            }
            data.getAttendanceLeaveEmployee().setIdInfo(x.getId());
            data.setShowEdit(data.getAttendanceLeaveEmployee().getLeaveTime() != null);
        });
        return responseList;
    }

    @Override
    public boolean saveAttendanceLeave(UserPrincipal principal, AttendanceEmployeeLeaveRequest x) {
        this.saveAttendanceLeaveEmployee(x, principal);
        return true;
    }

    @Override
    public boolean saveMultiAttendanceLeave(UserPrincipal principal, List<AttendanceEmployeeLeaveRequest> request) {
        request.forEach(x -> {
            this.saveAttendanceLeaveEmployee(x, principal);
        });
        return true;
    }

    @Override
    public boolean saveContentAttendanceLeave(UserPrincipal principal, AttendanceLeaveEmployeeContentRequest request) throws IOException {
        CommonValidate.checkDataPlus(principal);
        AttendanceEmployee attendanceEmployee = attendanceEmployeeRepository.findById(request.getId()).orElseThrow();
        if (request.getMultipartFile() != null) {
            HandleFileUtils.deletePictureInFolder(attendanceEmployee.getArriveLocalPicture());
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getMultipartFile(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
            attendanceEmployee.setLeavePicture(handleFileResponse.getUrlWeb());
            attendanceEmployee.setLeaveLocalPicture(handleFileResponse.getUrlLocal());
        }
        attendanceEmployee.setLeaveContent(StringUtil.isNotBlank(request.getLeaveContent()) ? request.getLeaveContent() : null);
        attendanceEmployeeRepository.save(attendanceEmployee);
        return true;
    }

    @Override
    public List<AttendanceEatEmployeeResponse> searchAttendanceEat(UserPrincipal principal, AttendanceEmployeeSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate date = request.getDate();
        List<AttendanceEatEmployeeResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDateAndNameOrPhone(idSchool, date, request.getNameOrPhone());
//        AttendanceTimeConfigResponse schoolConfig = modelMapper.map(principal.getSchoolConfig(), AttendanceTimeConfigResponse.class);
        infoEmployeeSchoolList.forEach(x -> {
            AttendanceEatEmployeeResponse data = new AttendanceEatEmployeeResponse();
            data.setAttendanceInfoEmployee(modelMapper.map(x, AttendanceInfoEmployeeResponse.class));
            Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            boolean isEat = this.checkAttendanceEat(attendanceEmployee);
            data.setArriveEmployeeDate(attendanceEmployee.isPresent() ? modelMapper.map(attendanceEmployee.get(), AttendanceArriveEmployeeDateResponse.class) : null);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            data.setAttendanceEmployeeConfig(attendanceEmployeeConfigResponse);
            data.setAttendanceEatEmployee(new AttendanceEatEmployeeDateResponse());
            if (request.getStatus() != null && request.getStatus() == AppConstant.APP_TRUE && isEat) {
                data.setAttendanceEatEmployee(modelMapper.map(attendanceEmployee.get(), AttendanceEatEmployeeDateResponse.class));
                data.getAttendanceEatEmployee().setEat(AppConstant.APP_TRUE);
                responseList.add(data);
            } else if (request.getStatus() != null && request.getStatus() == AppConstant.APP_FALSE && !isEat) {
                data.setAttendanceEatEmployee(new AttendanceEatEmployeeDateResponse());
                responseList.add(data);
            } else if (request.getStatus() == null) {
                data.setAttendanceEatEmployee(isEat ? modelMapper.map(attendanceEmployee.get(), AttendanceEatEmployeeDateResponse.class) : new AttendanceEatEmployeeDateResponse());
                data.getAttendanceEatEmployee().setEat(isEat);
                responseList.add(data);
            }
            data.getAttendanceEatEmployee().setIdInfo(x.getId());
        });
        return responseList;
    }

    @Override
    public boolean saveAttendanceEat(UserPrincipal principal, AttendanceEmployeeEatRequest x) {
        this.saveAttendanceEatProperties(x, principal);
        return true;
    }

    @Override
    public boolean saveAttendanceEatMulti(UserPrincipal principal, List<AttendanceEmployeeEatRequest> request) {
        request.forEach(x -> {
            this.saveAttendanceEatProperties(x, principal);
        });
        return true;
    }

    @Override
    public AttendanceEmployeesStatisticalResponse attendanceEmployeesStatistical(InfoEmployeeSchool infoEmployeeSchool, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATE);
        }
        List<AttendanceEmployee> attendanceEmployeeList = attendanceEmployeeRepository.findByInfoEmployeeSchoolIdAndDateBetween(infoEmployeeSchool.getId(), startDate, endDate);
        AttendanceEmployeesStatisticalResponse response = new AttendanceEmployeesStatisticalResponse();
        this.setAttendanceCommon(attendanceEmployeeList, response);
        return response;
    }

    @Override
    public List<AttendanceEmployeeDate> searchAllAttendanceDate(UserPrincipal principal, AttendanceEmployeeSearchRequest request) {
        List<AttendanceEmployeeDate> modelList = new ArrayList<>();
        List<AttendanceEmployee> attendanceEmployeeList = attendanceEmployeeRepository.findByInfoEmployeeSchool_School_IdAndDate(principal.getIdSchoolLogin(), request.getDate());

        attendanceEmployeeList.forEach(x -> {
            AttendanceEmployeeDate model = modelMapper.map(x, AttendanceEmployeeDate.class);
            model.setName(x.getInfoEmployeeSchool().getFullName());
            model.setGoSchool(checkAttendanceExcel(x).equals("1"));
            model.setAbsentYes(checkAttendanceExcel(x).equals("2"));
            model.setAbsentNo(checkAttendanceExcel(x).equals("3"));
            modelList.add(model);
        });
        return modelList;
    }

    @Override
    public List<AttendanceEmployeeMonth> searchAllAttendanceMonth(UserPrincipal principal, AttendanceEmployeeSearchRequest request) {
        List<AttendanceEmployeeMonth> modelList = new ArrayList<>();
        List<AttendanceEmployee> dataList = attendanceEmployeeRepository.searchAttendanceEmployeeMonth(principal.getIdSchoolLogin(), request.getDate());
        Map<InfoEmployeeSchool, List<AttendanceEmployee>> result = dataList.stream()
                .collect(Collectors.groupingBy((AttendanceEmployee::getInfoEmployeeSchool), Collectors.toList()));
        result.forEach((key, value) -> {
            AttendanceEmployeeMonth model = new AttendanceEmployeeMonth();
            model.setId(key.getId());
            model.setName(key.getFullName());
            model.setAttendanceEmployeeDateList(listMapper.mapList(value, AttendanceEmployeeDetailMonth.class));
            model.setAttendanceEmployeeDateList(model.getAttendanceEmployeeDateList().stream().sorted(Comparator.comparing(AttendanceEmployeeDetailMonth::getDate)).collect(Collectors.toList()));
            modelList.add(model);
        });
        return modelList;
    }

    @Override
    public void createAttendanceEmployeeFromConfirm(AbsentTeacher absentTeacher) {
        List<AbsentDateTeacher> absentDateTeacherList = absentTeacher.getAbsentDateTeacherList();
        InfoEmployeeSchool infoEmployeeSchool = absentTeacher.getInfoEmployeeSchool();
        absentDateTeacherList.forEach(x -> {
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(infoEmployeeSchool.getId(), x.getDate());
            AttendanceEmployee attendanceEmployee;
            if (attendanceEmployeeOptional.isPresent()) {
                attendanceEmployee = attendanceEmployeeOptional.get();
                if (x.isMorning()) {
                    attendanceEmployee.setMorningYes(AppConstant.APP_TRUE);
                    attendanceEmployee.setMorning(AppConstant.APP_FALSE);
                    attendanceEmployee.setMorningNo(AppConstant.APP_FALSE);
                }
                if (x.isAfternoon()) {
                    attendanceEmployee.setAfternoonYes(AppConstant.APP_TRUE);
                    attendanceEmployee.setAfternoon(AppConstant.APP_FALSE);
                    attendanceEmployee.setAfternoonNo(AppConstant.APP_FALSE);
                }
                if (x.isEvening()) {
                    attendanceEmployee.setEveningYes(AppConstant.APP_TRUE);
                    attendanceEmployee.setEvening(AppConstant.APP_FALSE);
                    attendanceEmployee.setEveningNo(AppConstant.APP_FALSE);
                }

            } else {
                attendanceEmployee = new AttendanceEmployee();
                attendanceEmployee.setMorningYes(x.isMorning());
                attendanceEmployee.setAfternoonYes(x.isAfternoon());
                attendanceEmployee.setEveningYes(x.isEvening());
                attendanceEmployee.setDate(x.getDate());
                attendanceEmployee.setInfoEmployeeSchool(infoEmployeeSchool);
            }
            attendanceEmployeeRepository.save(attendanceEmployee);
        });
    }

    private void saveAttendanceEatProperties(AttendanceEmployeeEatRequest x, UserPrincipal principal) {
        Long idInfo = x.getIdInfo();
        LocalDate date = x.getDate();
        Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(idInfo, date);
        if (attendanceEmployee.isPresent()) {
            Long idOld = attendanceEmployee.get().getId();
            modelMapper.map(x, attendanceEmployee.get());
            attendanceEmployee.get().setId(idOld);
            attendanceEmployeeRepository.save(attendanceEmployee.get());
        } else {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
            AttendanceEmployee attendanceEmployeeNew = modelMapper.map(x, AttendanceEmployee.class);
            attendanceEmployeeNew.setCreatedBy(principal.getFullName());
            attendanceEmployeeNew.setCreatedDate(LocalDateTime.now());
            attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
            attendanceEmployeeRepository.save(attendanceEmployeeNew);
        }
    }

    private void setAttendanceCommon(List<AttendanceEmployee> attendanceEmployeeList, AttendanceEmployeesStatisticalResponse response) {
        int allDay = 0;
        int allDayYes = 0;
        int allDayNo = 0;
        int morning = 0;
        int morningYes = 0;
        int morningNo = 0;
        int afternoon = 0;
        int afternoonYes = 0;
        int afternoonNo = 0;
        int evening = 0;
        int eveningYes = 0;
        int eveningNo = 0;

        int eatAllDay = 0;
        int eatMorning = 0;
        int eatAfternoon = 0;
        int eatEvening = 0;

        int minutesLate = 0;
        int minutesSoon = 0;

        float goSchoolTime = 0;
        float absentTime = 0;

        for (AttendanceEmployee x : attendanceEmployeeList) {
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getInfoEmployeeSchool().getId(), x.getDate()).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, x.getDate());
            //điểm danh đi học
            //trường đi học ít nhất 1 buổi trong ngày
            if (attendanceConfigResponse.isMorning() || attendanceConfigResponse.isAfternoon() || attendanceConfigResponse.isEvening()) {
                if (x.isMorning() && x.isAfternoon()) {
                    goSchoolTime++;
                } else if (x.isMorning() || x.isAfternoon()) {
                    goSchoolTime += 0.5;
                }
                if ((x.isMorningYes() || x.isMorningNo()) && (x.isAfternoonYes() || x.isAfternoonNo())) {
                    absentTime++;
                } else if ((x.isMorningYes() || x.isMorningNo()) || (x.isAfternoonYes() || x.isAfternoonNo())) {
                    absentTime += 0.5;
                }
                //cả ngày
                if (attendanceConfigResponse.isMorning() == x.isMorning() &&
                        attendanceConfigResponse.isAfternoon() == x.isAfternoon() &&
                        attendanceConfigResponse.isEvening() == x.isEvening()) {
                    allDay++;
                } else if (attendanceConfigResponse.isMorning() == x.isMorningYes() &&
                        attendanceConfigResponse.isAfternoon() == x.isAfternoonYes() &&
                        attendanceConfigResponse.isEvening() == x.isEveningYes()) {
                    allDayYes++;
                } else if (attendanceConfigResponse.isMorning() == x.isMorningNo() &&
                        attendanceConfigResponse.isAfternoon() == x.isAfternoonNo() &&
                        attendanceConfigResponse.isEvening() == x.isEveningNo()) {
                    allDayNo++;
                } else {
                    //sáng
                    if (attendanceConfigResponse.isMorning()) {
                        if (x.isMorning()) {
                            morning++;
                        }
                        if (x.isMorningYes()) {
                            morningYes++;
                        }
                        if (x.isMorningNo()) {
                            morningNo++;
                        }
                    }
                    //chiều
                    if (attendanceConfigResponse.isAfternoon()) {
                        if (x.isAfternoon()) {
                            afternoon++;
                        }
                        if (x.isAfternoonYes()) {
                            afternoonYes++;
                        }
                        if (x.isAfternoonNo()) {
                            afternoonNo++;
                        }
                    }
                    //tối
                    if (attendanceConfigResponse.isEvening()) {
                        if (x.isEvening()) {
                            evening++;
                        }
                        if (x.isEveningYes()) {
                            eveningYes++;
                        }
                        if (x.isEveningNo()) {
                            eveningNo++;
                        }
                    }
                }

            }
            //đếm số điểm danh ăn
            if (attendanceConfigResponse.isBreakfast() || attendanceConfigResponse.isLunch() || attendanceConfigResponse.isDinner()) {
                boolean checkEatMorning = attendanceConfigResponse.isBreakfast() ? x.isBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatNoon = attendanceConfigResponse.isLunch() ? x.isLunch() : AppConstant.APP_TRUE;
                boolean checkEatEvening = attendanceConfigResponse.isDinner() ? x.isDinner() : AppConstant.APP_TRUE;
                if (checkEatMorning && checkEatNoon && checkEatEvening) {
                    eatAllDay++;
                } else {
                    if (attendanceConfigResponse.isBreakfast() && x.isBreakfast()) {
                        eatMorning++;
                    }
                    if (attendanceConfigResponse.isLunch() && x.isLunch()) {
                        eatAfternoon++;
                    }
                    if (attendanceConfigResponse.isDinner() && x.isDinner()) {
                        eatEvening++;
                    }
                }
            }
            //đếm số phút điểm danh muộn
            minutesLate += x.getMinuteArriveLate();
            minutesSoon += x.getMinuteLeaveSoon();
        }
        response.setMorning(morning);
        response.setMorningYes(morningYes);
        response.setMorningNo(morningNo);
        response.setAfternoon(afternoon);
        response.setAfternoonYes(afternoonYes);
        response.setAfternoonNo(afternoonNo);
        response.setEvening(evening);
        response.setEveningYes(eveningYes);
        response.setEveningNo(eveningNo);
        response.setAllDay(allDay);
        response.setAllDayYes(allDayYes);
        response.setAllDayNo(allDayNo);

        response.setEatMorning(eatMorning);
        response.setEatAfternoon(eatAfternoon);
        response.setEatEvening(eatEvening);
        response.setEatAllDay(eatAllDay);

        response.setMinutesArriveLate(minutesLate);
        response.setMinutesLeaveSoon(minutesSoon);

        response.setGoSchoolTime(goSchoolTime);
        response.setAbsentTime(absentTime);
    }

    private boolean checkAttendanceEat(Optional<AttendanceEmployee> attendanceEmployee) {
        if (attendanceEmployee.isEmpty()) {
            return false;
        } else if (attendanceEmployee.get().isBreakfast() || attendanceEmployee.get().isLunch() || attendanceEmployee.get().isDinner()) {
            return true;
        } else return false;
    }

    /**
     * check có đi làm hay không
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkAttendanceArrive(Optional<AttendanceEmployee> attendanceEmployee) {
        if (attendanceEmployee.isEmpty()) {
            return false;
        } else
            return attendanceEmployee.isPresent() && (attendanceEmployee.get().isAfternoon() || attendanceEmployee.get().isMorning() || attendanceEmployee.get().isEvening());
    }

    /**
     * check trạng thái đi làm xuất excel
     *
     * @param attendanceEmployee
     * @return
     */
    private String checkAttendanceExcel(AttendanceEmployee attendanceEmployee) {
        if (attendanceEmployee.isAfternoon() || attendanceEmployee.isMorning() || attendanceEmployee.isEvening()) {
            return "1";
        } else if (attendanceEmployee.isMorningYes() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isEveningYes()) {
            return "2";
        } else return "3";
    }

    /**
     * check đã điểm danh hay chưa( đi làm or nghỉ)
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkAttendance(Optional<AttendanceEmployee> attendanceEmployee) {
        if (attendanceEmployee.isEmpty()) {
            return false;
        } else
            return attendanceEmployee.isPresent() && (attendanceEmployee.get().isAfternoon() || attendanceEmployee.get().isMorning() || attendanceEmployee.get().isEvening() || attendanceEmployee.get().isEveningNo() || attendanceEmployee.get().isEveningYes() || attendanceEmployee.get().isMorningNo() || attendanceEmployee.get().isMorningYes() || attendanceEmployee.get().isAfternoonNo() || attendanceEmployee.get().isAfternoonYes());
    }

    private void saveAttendanceLeaveEmployee(AttendanceEmployeeLeaveRequest x, UserPrincipal principal) {
        Long idInfo = x.getIdInfo();
        LocalDate date = x.getDate();
        AttendanceEmployee attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(idInfo, date).orElseThrow();
        modelMapper.map(x, attendanceEmployee);
        attendanceEmployee.setLastModifieBy(principal.getFullName());
        attendanceEmployee.setLastModifieDate(LocalDateTime.now());
        attendanceEmployeeRepository.save(attendanceEmployee);
    }

    private void saveAttendanceEmployee(AttendanceEmployeeArriveRequest x, UserPrincipal principal) {
        Long idInfo = x.getIdInfo();
        LocalDate date = x.getDate();
        Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(idInfo, date);
        if (attendanceEmployee.isPresent()) {
            Long id = attendanceEmployee.get().getId();
            modelMapper.map(x, attendanceEmployee.get());
            boolean check = this.checkAttendanceArrive(attendanceEmployee);
            if (!check) {
                attendanceEmployee.get().setLeaveContent(null);
                attendanceEmployee.get().setLeavePicture(null);
                attendanceEmployee.get().setLeaveTime(null);
                attendanceEmployee.get().setMinuteLeaveSoon(0);
            }
            attendanceEmployee.get().setId(id);
            attendanceEmployee.get().setLastModifieBy(principal.getFullName());
            attendanceEmployee.get().setLastModifieDate(LocalDateTime.now());
            attendanceEmployeeRepository.save(attendanceEmployee.get());
        } else {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
            AttendanceEmployee attendanceEmployeeNew = modelMapper.map(x, AttendanceEmployee.class);
            attendanceEmployeeNew.setCreatedBy(principal.getFullName());
            attendanceEmployeeNew.setCreatedDate(LocalDateTime.now());
            attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
            attendanceEmployeeRepository.save(attendanceEmployeeNew);
        }
    }
}
