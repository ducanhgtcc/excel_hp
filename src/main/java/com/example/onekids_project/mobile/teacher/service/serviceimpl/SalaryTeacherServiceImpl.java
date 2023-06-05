package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.FinanceMobileConstant;
import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.entity.employee.ConfigAttendanceEmployee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.response.attendance.AttendanceMonthResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.ListOrderKidsCustom;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsParentCustom1;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsParentResponse;
import com.example.onekids_project.mobile.plus.response.salary.NumberSalaryPlusResponse;
import com.example.onekids_project.mobile.teacher.response.absentletter.NumberSalaryTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.salary.ListAttendanceTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.SalaryTeacherService;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.attendanceemployee.AttendanceEmployeeConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * date 2021-04-20 09:07
 *
 * @author lavanviet
 */
@Service
public class SalaryTeacherServiceImpl implements SalaryTeacherService {

    @Autowired
    private AttendanceEmployeeRepository attendanceEmployeeRepository;
    @Autowired
    private ConfigAttendanceEmployeeRepository configAttendanceEmployeeRepository;
    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;
    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private AbsentTeacherRepository absentTeacherRepository;

    @Override
    public ListAttendanceTeacherResponse getAttendanceTeacher(UserPrincipal principal, LocalDate localDate, Long idInfo) {
        ListAttendanceTeacherResponse response = new ListAttendanceTeacherResponse();
        LocalDate dateStart = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);
        InfoEmployeeSchool infoEmployeeSchool;
        if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
            infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal);
        } else if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
            infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(idInfo, principal.getIdSchoolLogin()).orElseThrow();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khống tìm thấy giáo viên");
        }
        Long idInfoEmployee = infoEmployeeSchool.getId();
        List<AttendanceEmployee> attendanceKidsList = attendanceEmployeeRepository.findByInfoEmployeeSchoolIdAndDateGreaterThanEqualAndDateLessThanEqual(idInfoEmployee, dateStart, dateEnd);
        List<Integer> noAttendanceList = new ArrayList<>();
        List<AttendanceMonthResponse> allList = new ArrayList<>();
        List<AttendanceMonthResponse> morningList = new ArrayList<>();
        List<AttendanceMonthResponse> afternoonList = new ArrayList<>();
        List<AttendanceMonthResponse> eveningList = new ArrayList<>();
        attendanceKidsList.forEach(x -> {
            ConfigAttendanceEmployee attendanceConfig = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(idInfoEmployee, x.getDate()).orElseThrow(() -> new NoSuchElementException("not found config attendance employee"));
            AttendanceEmployeeConfigResponse config = ConvertData.convertAttendanceEmployeeConfig(attendanceConfig, x.getDate());
            //có đi học ít nhất 1 buổi trong ngày
            if (config.isMorning() || config.isAfternoon() || config.isEvening()) {
                int dateNumber = x.getDate().getDayOfMonth();
                if (!x.isMorning() && !x.isAfternoon() && !x.isEvening() && !x.isMorningYes() && !x.isAfternoonYes() && !x.isEveningYes() && !x.isMorningNo() && !x.isAfternoonNo() && !x.isEveningNo()) {
                    //ngày chưa điểm danh
                    noAttendanceList.add(dateNumber);
                } else if (config.isMorning() == x.isMorningYes() && config.isAfternoon() == x.isAfternoonYes() && config.isEvening() == x.isEveningYes()) {
                    //nghỉ cả ngày có phép
                    AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                    allList.add(model);
                } else if (config.isMorning() == x.isMorningNo() && config.isAfternoon() == x.isAfternoonNo() && config.isEvening() == x.isEveningNo()) {
                    //nghỉ cả ngày không phép
                    AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                    allList.add(model);
                } else {
                    //trường học sáng
                    if (config.isMorning()) {
                        //nghỉ sáng có phép
                        if (x.isMorningYes()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                            morningList.add(model);
                        }
                        //nghỉ sáng không phép
                        if (x.isMorningNo()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                            morningList.add(model);
                        }
                    }
                    //trường học chiều
                    if (config.isAfternoon()) {
                        //nghỉ chiều có phép
                        if (x.isAfternoonYes()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                            afternoonList.add(model);
                        }
                        //nghỉ chiều không phép
                        if (x.isAfternoonNo()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                            afternoonList.add(model);
                        }
                    }
                    //trường học tối
                    if (config.isEvening()) {
                        //nghỉ tối có phép
                        if (x.isEveningYes()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                            eveningList.add(model);
                        }
                        //nghỉ tối không phép
                        if (x.isEveningNo()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                            eveningList.add(model);
                        }
                    }
                }
            }
        });
        response.setNoAttendanceList(noAttendanceList);
        response.setAllList(allList);
        response.setMorningList(morningList);
        response.setAfternoonList(afternoonList);
        response.setEveningList(eveningList);
        response.setAll(allList.size());
        response.setMorning(morningList.size());
        response.setAfternoon(afternoonList.size());
        response.setEvening(eveningList.size());
        return response;
    }

    @Override
    public List<OrderKidsParentResponse> searchOrderTeacher(UserPrincipal principal, OrderKidsParentRequest request, Long idInfo) {
        List<OrderKidsParentResponse> responseList = new ArrayList<>();
        InfoEmployeeSchool infoEmployeeSchool;
        if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
            infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal);
        } else if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
            infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(idInfo, principal.getIdSchoolLogin()).orElseThrow();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khống tìm thấy giáo viên");
        }
        Long idInfoEmployee = infoEmployeeSchool.getId();
        List<FnOrderEmployee> fnOrderKidsList = fnOrderEmployeeRepository.searchOrderEmployeeYear(idInfoEmployee, request.getYear());
        fnOrderKidsList.forEach(x -> {
            OrderKidsParentResponse response = new OrderKidsParentResponse();
            ListOrderKidsCustom dataIn = new ListOrderKidsCustom();
            ListOrderKidsCustom dataOut = new ListOrderKidsCustom();
            List<OrderKidsParentCustom1> inList = new ArrayList<>();
            List<OrderKidsParentCustom1> outList = new ArrayList<>();
            long moneyIn = 0;
            long moneyPaidIn = 0;
            long moneyRemainIn = 0;
            long moneyOut = 0;
            long moneyPaidOut = 0;
            long moneyRemainOut = 0;
            List<FnEmployeeSalary> fnKidsPackageList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idInfoEmployee, x.getMonth(), x.getYear());
            for (FnEmployeeSalary y : fnKidsPackageList) {
                OrderKidsParentCustom1 model = new OrderKidsParentCustom1();
                model.setId(y.getId());
                model.setName(y.getName());
                model.setCategory(y.getCategory());
                model.setMoney(FinanceUltils.getMoneySalary(y));
                model.setMoneyPaid((long) y.getPaid());
                model.setMoneyRemain(model.getMoney() - model.getMoneyPaid());
                model.setPaidStatus(this.getPaidStatus(model.getMoneyPaid(), model.getMoneyRemain()));
                if (FinanceConstant.CATEGORY_IN.equals(model.getCategory())) {
                    moneyIn += model.getMoney();
                    moneyPaidIn += model.getMoneyPaid();
                    moneyRemainIn += model.getMoneyRemain();

                    inList.add(model);
                } else if (FinanceConstant.CATEGORY_OUT.equals(model.getCategory())) {
                    moneyOut += model.getMoney();
                    moneyPaidOut += model.getMoneyPaid();
                    moneyRemainOut += model.getMoneyRemain();
                    outList.add(model);
                }
            }
            long moneyTotal = moneyIn - moneyOut;
            long moneyRemainTotal = moneyRemainIn - moneyRemainOut;
            dataIn.setMoney(moneyIn);
            dataIn.setMoneyPaid(moneyPaidIn);
            dataIn.setMoneyRemain(moneyRemainIn);
            dataOut.setMoney(moneyOut);
            dataOut.setMoneyPaid(moneyPaidOut);
            dataOut.setMoneyRemain(moneyRemainOut);
            dataIn.setDataList(inList);
            dataOut.setDataList(outList);

            response.setId(x.getId());
            response.setMonth(x.getMonth());
            response.setParentRead(x.isEmployeeRead());
            response.setMoneyTotal(moneyTotal);
            response.setMoneyRemainTotal(moneyRemainTotal);
            response.setDataIn(dataIn);
            response.setDataOut(dataOut);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public NumberSalaryTeacherResponse showNumberSalary(UserPrincipal principal) {
        NumberSalaryTeacherResponse response = new NumberSalaryTeacherResponse();
        InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal);
        Long idInfoEmployee = infoEmployeeSchool.getId();
        int absentNumber = absentTeacherRepository.countByInfoEmployeeSchoolIdAndTeacherReadFalseAndDelActiveTrue(idInfoEmployee);
        List<FnOrderEmployee> fnOrderEmployeeList = fnOrderEmployeeRepository.findByInfoEmployeeSchoolIdAndYear(idInfoEmployee, LocalDate.now().getYear());
        int salaryNumber = 0;
        for (FnOrderEmployee x : fnOrderEmployeeList) {
            List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idInfoEmployee, x.getMonth(), x.getYear());
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
            if (orderNumberModel.getTotalNumber() > orderNumberModel.getEnoughNumber()) {
                salaryNumber++;
            }
        }
        response.setAbsentNumber(absentNumber);
        response.setSalaryNumber(salaryNumber);
        return response;
    }

    @Override
    public NumberSalaryPlusResponse showNumberPlus(UserPrincipal principal) {
        NumberSalaryPlusResponse response = new NumberSalaryPlusResponse();
        Long idSchool = principal.getIdSchoolLogin();
        int absentNumber = absentTeacherRepository.countByIdSchoolAndConfirmStatusFalseAndInfoEmployeeSchoolDelActiveTrueAndDelActiveTrue(idSchool);
        response.setAbsentNumber(absentNumber);
        return response;
    }

    private AttendanceMonthResponse getDataModel(int dateNumber, boolean status) {
        AttendanceMonthResponse model = new AttendanceMonthResponse();
        model.setDate(dateNumber);
        model.setStatus(status);
        return model;
    }

    private String getPaidStatus(long moneyPaid, long moneyRemain) {
        if (moneyPaid == 0) {
            return FinanceMobileConstant.PAID_EMPTY;
        } else if (moneyRemain > 0) {
            return FinanceMobileConstant.PAID_PART;
        }
        return FinanceMobileConstant.PAID_FULL;
    }
}
