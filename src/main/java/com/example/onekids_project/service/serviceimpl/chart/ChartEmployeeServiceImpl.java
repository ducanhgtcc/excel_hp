package com.example.onekids_project.service.serviceimpl.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.chart.AttendanceEmployeeChartRequest;
import com.example.onekids_project.request.chart.FeesKidsChartRequest;
import com.example.onekids_project.request.chart.StatusEmployeeChartRequest;
import com.example.onekids_project.response.chart.ChartAttendanceResponse;
import com.example.onekids_project.response.chart.ChartFeesResponse;
import com.example.onekids_project.response.chart.ChartStatusEmployeeResponse;
import com.example.onekids_project.response.chart.ListChartStatusEmployeeResponse;
import com.example.onekids_project.service.servicecustom.chart.ChartEmployeeService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FinanceUltils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-09-29 11:21 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class ChartEmployeeServiceImpl implements ChartEmployeeService {

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private AttendanceEmployeeRepository attendanceEmployeeRepository;
    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;
    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;

    @Override
    public ListChartStatusEmployeeResponse findAllStatusEmployee(Long idSchool) {
        ListChartStatusEmployeeResponse response = new ListChartStatusEmployeeResponse();
        List<Long> longList = new ArrayList<>();
        long statusWorking = infoEmployeeSchoolRepository.countByEmployeeStatusAndSchoolIdAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(EmployeeConstant.STATUS_WORKING, idSchool, AppTypeConstant.TEACHER);
        long statusRetain = infoEmployeeSchoolRepository.countByEmployeeStatusAndSchoolIdAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(EmployeeConstant.STATUS_RETAIN, idSchool, AppTypeConstant.TEACHER);
        long statusLeave = infoEmployeeSchoolRepository.countByEmployeeStatusAndSchoolIdAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(EmployeeConstant.STATUS_LEAVE, idSchool, AppTypeConstant.TEACHER);
        longList.add(0, statusWorking);
        longList.add(1, statusRetain);
        longList.add(2, statusLeave);
        List<ChartStatusEmployeeResponse> chartStatusEmployeeResponseList = new ArrayList<>();
        List<Department> departmentList = departmentRepository.findBySchoolIdAndDelActiveTrue(idSchool);
        departmentList.forEach(x -> {
            ChartStatusEmployeeResponse chartStatusEmployeeResponse = new ChartStatusEmployeeResponse();
            chartStatusEmployeeResponse.setName(x.getDepartmentName());
            long working = x.getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getInfoEmployeeSchool).filter(a -> a.isDelActive() && EmployeeConstant.STATUS_WORKING.equals(a.getEmployeeStatus()) && AppTypeConstant.TEACHER.equals(a.getAppType())).count();
            long retain = x.getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getInfoEmployeeSchool).filter(a -> a.isDelActive() && EmployeeConstant.STATUS_RETAIN.equals(a.getEmployeeStatus()) && AppTypeConstant.TEACHER.equals(a.getAppType())).count();
            long leave = x.getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getInfoEmployeeSchool).filter(a -> a.isDelActive() && EmployeeConstant.STATUS_LEAVE.equals(a.getEmployeeStatus()) && AppTypeConstant.TEACHER.equals(a.getAppType())).count();
            chartStatusEmployeeResponse.setStatusWorking(working);
            chartStatusEmployeeResponse.setStatusRetain(retain);
            chartStatusEmployeeResponse.setStatusLeave(leave);
            chartStatusEmployeeResponseList.add(chartStatusEmployeeResponse);
        });
        response.setStatusAll(longList);
        response.setDataList(chartStatusEmployeeResponseList);
        return response;
    }

    @Override
    public List<ChartStatusEmployeeResponse> findDetailStatusEmployee(Long idSchool, StatusEmployeeChartRequest request) {
        List<ChartStatusEmployeeResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        maClassList.forEach(x -> {
            ChartStatusEmployeeResponse response = new ChartStatusEmployeeResponse();
            response.setName(x.getClassName());
            long working = x.getExEmployeeClassList().stream().map(ExEmployeeClass::getInfoEmployeeSchool).filter(a -> a.isDelActive() && EmployeeConstant.STATUS_WORKING.equals(a.getEmployeeStatus()) && AppTypeConstant.TEACHER.equals(a.getAppType())).count();
            long retain = x.getExEmployeeClassList().stream().map(ExEmployeeClass::getInfoEmployeeSchool).filter(a -> a.isDelActive() && EmployeeConstant.STATUS_RETAIN.equals(a.getEmployeeStatus()) && AppTypeConstant.TEACHER.equals(a.getAppType())).count();
            long leave = x.getExEmployeeClassList().stream().map(ExEmployeeClass::getInfoEmployeeSchool).filter(a -> a.isDelActive() && EmployeeConstant.STATUS_LEAVE.equals(a.getEmployeeStatus()) && AppTypeConstant.TEACHER.equals(a.getAppType())).count();
            response.setStatusWorking(working);
            response.setStatusRetain(retain);
            response.setStatusLeave(leave);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartAttendanceResponse> getAttendanceChart(Long idSchool, AttendanceEmployeeChartRequest request) {
        List<ChartAttendanceResponse> responseList = new ArrayList<>();
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (AppConstant.CHART_DATE.equals(request.getType())) {
            startDate = request.getDateList().get(0);
            endDate = request.getDateList().get(1);
        } else if (AppConstant.CHART_WEEK.equals(request.getType())) {
            startDate = request.getWeek();
            endDate = request.getWeek().plusDays(6);
        } else if (AppConstant.CHART_MONTH.equals(request.getType())) {
            startDate = request.getMonth();
            endDate = request.getMonth().plusMonths(1).plusDays(-1);
        }
        assert startDate != null;
        int[] dates = new int[]{1, 5, 10, 15, 20};
        List<LocalDate> dateList = AppConstant.CHART_YEAR.equals(request.getType()) ? this.setListDateCustomDate(dates, request.getYear().getYear()) : this.setListDate(startDate, endDate);
        List<AttendanceEmployee> attendanceEmployeeList = attendanceEmployeeRepository.searchAttendanceEmployeeChart(idSchool, request.getIdDepartment(), dateList);
        dateList.forEach(x -> {
            ChartAttendanceResponse response = new ChartAttendanceResponse();
            List<AttendanceEmployee> attendanceEmployeeList1 = attendanceEmployeeList.stream().filter(a -> x.equals(a.getDate())).collect(Collectors.toList());
            response.setName(ConvertData.convertLocalDateToString(x));
            long arrive = attendanceEmployeeList1.stream().filter(this::checkArrive).count();
            long arriveYes = attendanceEmployeeList1.stream().filter(a -> this.checkArriveYes(a) && !this.checkArrive(a)).count();
            long arriveNo = attendanceEmployeeList1.stream().filter(a -> this.checkArriveNo(a) && !this.checkArrive(a) && !this.checkArriveYes(a)).count();
            response.setAttendance(arrive);
            response.setAttendanceYes(arriveYes);
            response.setAttendanceNo(arriveNo);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartFeesResponse> getFinanceEmployeeChart(Long idSchool, FeesKidsChartRequest request) {
        List<ChartFeesResponse> responseList = new ArrayList<>();
        //Tháng 1 - 12
        for (int i = 1; i <= 12; i++) {
            ChartFeesResponse response = new ChartFeesResponse();
            List<FnOrderEmployee> fnOrderEmployeeList = fnOrderEmployeeRepository.findByYearAndMonthAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(request.getYear(), i, idSchool);
            List<InfoEmployeeSchool> infoEmployeeSchoolList = fnOrderEmployeeList.stream().map(FnOrderEmployee::getInfoEmployeeSchool).filter(a -> EmployeeConstant.STATUS_WORKING.equals(a.getEmployeeStatus())).collect(Collectors.toList());
            long numberSuccess = 0;
            long numberNoSuccess = 0;
            long numberUnSuccess = 0;
            long totalPaidIn = 0;
            long totalPaidOut = 0;
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), i, request.getYear());
                OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
                if (orderNumberModel.getTotalNumber() == 0) {
                    //chưa có khoảng nào
                    numberUnSuccess++;
                } else if (orderNumberModel.getEnoughNumber() == orderNumberModel.getTotalNumber()) {
                    //đã hoàn thành hóa đơn
                    numberSuccess++;
                } else if (orderNumberModel.getEnoughNumber() < orderNumberModel.getTotalNumber()) {
                    //chưa hoàn thành hóa đơn
                    numberNoSuccess++;
                }
                List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(a -> FinanceConstant.CATEGORY_IN.equals(a.getCategory())).collect(Collectors.toList());
                List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(a -> FinanceConstant.CATEGORY_OUT.equals(a.getCategory())).collect(Collectors.toList());
                totalPaidIn += inList.stream().mapToLong(y -> (long) y.getPaid()).sum();
                totalPaidOut += outList.stream().mapToLong(y -> (long) y.getPaid()).sum();

            }
            response.setMoneyIn(totalPaidIn);
            response.setMoneyOut(totalPaidOut);
            response.setName("Tháng " + i);
            response.setFeesUn(numberUnSuccess);
            response.setFeesYes(numberSuccess);
            response.setFeesNo(numberNoSuccess);
            responseList.add(response);
        }
        return responseList;
    }

    private boolean checkArrive(AttendanceEmployee attendanceEmployee) {
        return attendanceEmployee.isMorning() || attendanceEmployee.isAfternoon() || attendanceEmployee.isEvening();
    }

    private boolean checkArriveYes(AttendanceEmployee attendanceEmployee) {
        return attendanceEmployee.isMorningYes() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isEveningYes();
    }

    private boolean checkArriveNo(AttendanceEmployee attendanceEmployee) {
        return attendanceEmployee.isMorningNo() || attendanceEmployee.isAfternoonNo() || attendanceEmployee.isEveningNo();
    }

    private List<LocalDate> setListDate(LocalDate start, LocalDate end) {
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }

    private List<LocalDate> setListDateCustomDate(int[] dates, int year) {
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 1; i <= (LocalDate.now().getYear() == year ? LocalDate.now().getMonthValue() : 12); i++) {
            for (int k : dates) {
                LocalDate date = LocalDate.of(year, i, k);
                dateList.add(date);
            }
        }
        return dateList;
    }
}
