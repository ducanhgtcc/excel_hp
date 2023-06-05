package com.example.onekids_project.service.serviceimpl.employeesaralyImpl;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.FnEmployeeSalaryRepository;
import com.example.onekids_project.repository.FnOrderEmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.finance.exportimport.ExportStatisticalSalaryRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.FinanceStatisticalEmployeeService;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * date 2021-04-07 10:56
 *
 * @author lavanviet
 */
@Service
public class FinanceStatisticalEmployeeServiceImpl implements FinanceStatisticalEmployeeService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;
    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;

    @Override
    public FinanceKidsStatisticalResponse statisticalFinanceEmployee(UserPrincipal principal, FinanceKidsStatisticalRequest request) {
        CommonValidate.checkDataPlus(principal);
        int year = request.getYear();
        int startMonth = request.getStartMonth();
        int endMonth = request.getEndMonth();
        this.checkStartEndMonth(startMonth, endMonth);
        FinanceKidsStatisticalResponse response = new FinanceKidsStatisticalResponse();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolId(principal.getIdSchoolLogin());
        int kidsNumber = 0;
        int orderNumber = 0;
        List<FnEmployeeSalary> fnEmployeeSalaryAllList = new ArrayList<>();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
            List<FnEmployeeSalary> fnEmployeeSalaryList = x.getFnEmployeeSalaryList().stream().filter(a -> a.isApproved() && a.getYear() == year && a.getMonth() >= startMonth && a.getMonth() <= endMonth).collect(Collectors.toList());
            fnEmployeeSalaryAllList.addAll(fnEmployeeSalaryList);
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
            if (orderNumberModel.getEnoughNumber() != orderNumberModel.getTotalNumber()) {
                kidsNumber++;
            }
            List<Integer> monthList = fnEmployeeSalaryList.stream().filter(a -> a.getPaid() < FinanceUltils.getMoneySalary(a)).map(FnEmployeeSalary::getMonth).distinct().collect(Collectors.toList());
            orderNumber += fnOrderEmployeeRepository.countByInfoEmployeeSchoolIdAndMonthIn(x.getId(), monthList);
        }
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnEmployeeSalaryAllList);
        modelMapper.map(orderMoneyModel, response);
        response.setKidsNumber(kidsNumber);
        response.setOrderNumber(orderNumber);
        return response;
    }

    @Override
    public List<ExcelResponse> exportExcelOrder(UserPrincipal principal, ExportStatisticalSalaryRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        int startMonth = request.getStartMonth();
        int endMonth = request.getEndMonth();
        int year = request.getYear();
        School school = schoolRepository.findByIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
        String schoolName = "Trường: ".concat(school.getSchoolName());
        String fromToDate = FinanceUltils.getDateString(request.getStartMonth(), request.getEndMonth(), request.getYear());
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchEmployeeWidthStatus(principal.getIdSchoolLogin(), request.getStatus());
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(x -> x.getFnEmployeeSalaryList().size() > 0).collect(Collectors.toList());
        if (request.getType().equals(FinanceConstant.EXPORT_ORDER)) {
            response.setSheetName("Hóa đơn chưa hoàn thành");
            response.setHeaderList(this.getHeader("DANH SÁCH NHÂN SỰ CÓ HÓA ĐƠN CHƯA HOÀN THÀNH", schoolName, fromToDate));
            response.setBodyList(this.getBodyOrder(infoEmployeeSchoolList, startMonth, endMonth, year));
        } else if (request.getType().equals(FinanceConstant.EXPORT_IN)) {
            response.setSheetName("Chưa hoàn thành các khoản thu");
            response.setHeaderList(this.getHeader("DANH SÁCH NHÂN SỰ CHƯA HOÀN THÀNH KHOẢN THU", schoolName, fromToDate));
            response.setBodyList(this.getBodyInOrOut(infoEmployeeSchoolList, startMonth, endMonth, year, FinanceConstant.CATEGORY_IN));
        } else if (request.getType().equals(FinanceConstant.EXPORT_OUT)) {
            response.setSheetName("Chưa hoàn thành các khoản chi");
            response.setHeaderList(this.getHeader("DANH SÁCH NHÂN SỰ CHƯA HOÀN THÀNH KHOẢN CHI", schoolName, fromToDate));
            response.setBodyList(this.getBodyInOrOut(infoEmployeeSchoolList, startMonth, endMonth, year, FinanceConstant.CATEGORY_OUT));
        } else if (request.getType().equals(FinanceConstant.EXPORT_INOUT)) {
            response.setSheetName("Tổng hợp");
            response.setHeaderList(this.getHeader("DANH SÁCH TỔNG HỢP THU CHI NHÂN SỰ CHƯA HOÀN THÀNH", schoolName, fromToDate));
            response.setBodyList(this.getBodyInAndOut(infoEmployeeSchoolList, startMonth, endMonth, year));
        } else if (request.getType().equals(FinanceConstant.EXPORT_INOUT_TRUE)) {
            response.setSheetName("Tổng hợp");
            response.setHeaderList(this.getHeader("DANH SÁCH TỔNG HỢP THU CHI NHÂN SỰ ĐÃ HOÀN THÀNH", schoolName, fromToDate));
            response.setBodyList(this.getBodyInAndOutTrue(infoEmployeeSchoolList, startMonth, endMonth, year));
        } else if (request.getType().equals(FinanceConstant.EXPORT_INOUT_TOTAL)) {
            response.setSheetName("Tổng hợp");
            response.setHeaderList(this.getHeader("DANH SÁCH TỔNG HỢP THU CHI NHÂN SỰ", schoolName, fromToDate));
            response.setBodyList(this.getBodyInAndOutTotal(infoEmployeeSchoolList, startMonth, endMonth, year));
        }
        responseList.add(response);
        return responseList;
    }

    private List<ExcelData> getBodyOrder(List<InfoEmployeeSchool> infoEmployeeSchoolList, int startMonth, int endMonth, int year) {
        List<ExcelData> bodyList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        infoEmployeeSchoolList.forEach(x -> {
            List<String> codeList = new ArrayList<>();
            List<FnOrderEmployee> orderList = fnOrderEmployeeRepository.searchOrderWidthMonthYear(x.getId(), startMonth, endMonth, year);
            orderList.forEach(y -> {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                if (FinanceUltils.getOrderStatusEmployee(fnEmployeeSalaryList)) {
                    codeList.add(y.getCode());
                }
            });
            if (CollectionUtils.isNotEmpty(codeList)) {
                i.getAndIncrement();
                String code = codeList.toString().replace("[", "");
                String dataCode = code.replace("]", "");
                ExcelData model = new ExcelData();
                model.setPro1(i.toString());
                model.setPro2(x.getFullName());
                model.setPro3(x.getPhone());
                model.setPro4(dataCode);
                bodyList.add(model);
            }
        });
        return bodyList;
    }

    private List<ExcelData> getBodyInOrOut(List<InfoEmployeeSchool> infoEmployeeSchoolList, int startMonth, int endMonth, int year, String category) {
        List<ExcelData> bodyList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        infoEmployeeSchoolList.forEach(x -> {
            List<String> codeList = new ArrayList<>();
            double money = 0;
            double moneyPaid = 0;
            List<FnOrderEmployee> orderList = fnOrderEmployeeRepository.searchOrderWidthMonthYear(x.getId(), startMonth, endMonth, year);
            for (FnOrderEmployee y : orderList) {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndCategoryAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear(), category);
                if (FinanceUltils.getOrderStatusEmployee(fnEmployeeSalaryList)) {
                    money += fnEmployeeSalaryList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                    moneyPaid += fnEmployeeSalaryList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum();
                    codeList.add(y.getCode());
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                String code = codeList.toString().replace("[", "");
                String dataCode = code.replace("]", "");
                i.getAndIncrement();
                ExcelData model = new ExcelData();
                model.setPro1(i.toString());
                model.setPro2(x.getFullName());
                model.setPro3(x.getPhone());
                model.setPro4(dataCode);
                model.setPro5(FinanceUltils.formatMoney((long) money));
                model.setPro6(FinanceUltils.formatMoney((long) moneyPaid));
                model.setPro7(FinanceUltils.formatMoney((long) (money - moneyPaid)));
                bodyList.add(model);
            }
        });
        return bodyList;
    }

    private List<ExcelData> getBodyInAndOut(List<InfoEmployeeSchool> infoEmployeeSchoolList, int startMonth, int endMonth, int year) {
        List<ExcelData> bodyList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        infoEmployeeSchoolList.forEach(x -> {
            List<String> codeList = new ArrayList<>();
            double moneyIn = 0;
            double moneyOut = 0;
            double moneyPaidIn = 0;
            double moneyPaidOut = 0;
            List<FnOrderEmployee> orderList = fnOrderEmployeeRepository.searchOrderWidthMonthYear(x.getId(), startMonth, endMonth, year);
            for (FnOrderEmployee y : orderList) {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                if (FinanceUltils.getOrderStatusEmployee(fnEmployeeSalaryList)) {
                    List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                    List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                    moneyIn += inList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                    moneyOut += outList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                    moneyPaidIn += inList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum();
                    moneyPaidOut += outList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum();
                    codeList.add(y.getCode());
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                String code = codeList.toString().replace("[", "");
                String dataCode = code.replace("]", "");
                i.getAndIncrement();
                ExcelData model = new ExcelData();
                model.setPro1(i.toString());
                model.setPro2(x.getFullName());
                model.setPro3(x.getPhone());
                model.setPro4(dataCode);
                model.setPro5(FinanceUltils.formatMoney((long) (moneyOut - moneyIn)));
                model.setPro6(FinanceUltils.formatMoney((long) (moneyPaidOut - moneyPaidIn)));
                double totalRemain = (moneyPaidIn - moneyPaidOut) - (moneyIn - moneyOut);
                if (totalRemain > 0) {
                    model.setPro7(FinanceUltils.formatMoney((long) totalRemain));
                    model.setPro8("0");
                } else {
                    model.setPro7("0");
                    model.setPro8(FinanceUltils.formatMoney((long) Math.abs(totalRemain)));
                }
                bodyList.add(model);
            }
        });
        return bodyList;
    }

    private List<ExcelData> getBodyInAndOutTrue(List<InfoEmployeeSchool> infoEmployeeSchoolList, int startMonth, int endMonth, int year) {
        List<ExcelData> bodyList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        infoEmployeeSchoolList.forEach(x -> {
            List<String> codeList = new ArrayList<>();
            double moneyIn = 0;
            double moneyOut = 0;
            List<FnOrderEmployee> orderList = fnOrderEmployeeRepository.searchOrderWidthMonthYear(x.getId(), startMonth, endMonth, year);
            for (FnOrderEmployee y : orderList) {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
                if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getTotalNumber() == orderNumberModel.getEnoughNumber()) {
                    List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                    List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                    moneyIn += inList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                    moneyOut += outList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                    codeList.add(y.getCode());
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                String code = codeList.toString().replace("[", "");
                String dataCode = code.replace("]", "");
                i.getAndIncrement();
                ExcelData model = new ExcelData();
                model.setPro1(i.toString());
                model.setPro2(x.getFullName());
                model.setPro3(x.getPhone());
                model.setPro4(dataCode);
                model.setPro5(FinanceUltils.formatMoney((long) (moneyOut - moneyIn)));
                bodyList.add(model);
            }
        });
        return bodyList;
    }

    private List<ExcelData> getBodyInAndOutTotal(List<InfoEmployeeSchool> infoEmployeeSchoolList, int startMonth, int endMonth, int year) {
        List<ExcelData> bodyList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        infoEmployeeSchoolList.forEach(x -> {
            List<String> codeList = new ArrayList<>();
            double moneyIn = 0;
            double moneyOut = 0;
            double moneyPaidIn = 0;
            double moneyPaidOut = 0;
            List<FnOrderEmployee> orderList = fnOrderEmployeeRepository.searchOrderWidthMonthYear(x.getId(), startMonth, endMonth, year);
            for (FnOrderEmployee y : orderList) {
                List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                List<FnEmployeeSalary> inList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                List<FnEmployeeSalary> outList = fnEmployeeSalaryList.stream().filter(a -> a.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                moneyIn += inList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                moneyOut += outList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum();
                moneyPaidIn += inList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum();
                moneyPaidOut += outList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum();
                codeList.add(y.getCode());
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                String code = codeList.toString().replace("[", "");
                String dataCode = code.replace("]", "");
                i.getAndIncrement();
                ExcelData model = new ExcelData();
                model.setPro1(i.toString());
                model.setPro2(x.getFullName());
                model.setPro3(x.getPhone());
                model.setPro4(dataCode);
                model.setPro5(FinanceUltils.formatMoney((long) (moneyOut - moneyIn)));
                model.setPro6(FinanceUltils.formatMoney((long) (moneyPaidOut - moneyPaidIn)));
                double totalRemain = (moneyPaidIn - moneyPaidOut) - (moneyIn - moneyOut);
                if (totalRemain > 0) {
                    model.setPro7(FinanceUltils.formatMoney((long) totalRemain));
                    model.setPro8("0");
                } else {
                    model.setPro7("0");
                    model.setPro8(FinanceUltils.formatMoney((long) Math.abs(totalRemain)));
                }
                bodyList.add(model);
            }
        });
        return bodyList;
    }

    private List<ExcelData> getHeader(String str1, String str2, String str3) {
        List<ExcelData> headerList = new ArrayList<>();
        ExcelData excelData1 = new ExcelData();
        excelData1.setPro1(str1);
        ExcelData excelData2 = new ExcelData();
        excelData2.setPro1(str2);
        ExcelData excelData3 = new ExcelData();
        excelData3.setPro1(str3);
        headerList.add(excelData1);
        headerList.add(excelData2);
        headerList.add(excelData3);
        return headerList;
    }

    private void checkStartEndMonth(int startMonth, int endMonth) {
        if (startMonth > endMonth) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tháng bắt đầu không thể lớn hơn tháng kết thúc");
        }
    }
}
