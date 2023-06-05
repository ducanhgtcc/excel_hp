package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalaryDefault;
import com.example.onekids_project.entity.finance.employeesalary.FnSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnSalaryGroup;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.finance.fees.FnPackageGroup;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.cashbook.SearchCashBookHistoryRequest;
import com.example.onekids_project.request.finance.financegroup.*;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.financegroup.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.FnPackageGroupService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.FinanceUltils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-06-01 14:09
 *
 * @author lavanviet
 */
@Service
public class FnPackageGroupServiceImpl implements FnPackageGroupService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private FnPackageGroupRepository fnPackageGroupRepository;
    @Autowired
    private FnPackageRepository fnPackageRepository;
    @Autowired
    private FnSalaryGroupRepository fnSalaryGroupRepository;
    @Autowired
    private FnSalaryRepository fnSalaryRepository;
    @Autowired
    private FnEmployeeSalaryDefaultRepository fnEmployeeSalaryDefaultRepository;
    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;
    @Autowired
    private CashBookHistoryRepository cashBookHistoryRepository;
    @Autowired
    private FnCashBookRepository fnCashBookRepository;
    @Autowired
    private FnCashInternalSchoolRepository fnCashInternalSchoolRepository;

    @Override
    public List<PackageGroupResponse> searchPackageGroup(UserPrincipal principal, String name) {
        List<FnPackageGroup> fnPackageGroupList = fnPackageGroupRepository.findByIdSchoolAndNameContainingAndDelActiveTrueOrderByIdDesc(principal.getIdSchoolLogin(), name);
        List<PackageGroupResponse> responseList = new ArrayList<>();
        fnPackageGroupList.forEach(x -> {
            PackageGroupResponse model = modelMapper.map(x, PackageGroupResponse.class);
            model.setNumber((int) x.getFnPackageList().stream().filter(BaseEntity::isDelActive).count());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public PackageGroupResponse getPackageGroupById(UserPrincipal principal, Long id) {
        FnPackageGroup fnPackageGroup = fnPackageGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        return modelMapper.map(fnPackageGroup, PackageGroupResponse.class);
    }

    @Override
    public boolean createPackageGroup(UserPrincipal principal, PackageGroupCreateRequest request) {
        FnPackageGroup fnPackageGroup = modelMapper.map(request, FnPackageGroup.class);
        fnPackageGroup.setIdSchool(principal.getIdSchoolLogin());
        fnPackageGroupRepository.save(fnPackageGroup);
        return true;
    }

    @Override
    public boolean updatePackageGroup(UserPrincipal principal, PackageGroupUpdateRequest request) {
        FnPackageGroup fnPackageGroup = fnPackageGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), principal.getIdSchoolLogin()).orElseThrow();
        modelMapper.map(request, fnPackageGroup);
        fnPackageGroupRepository.save(fnPackageGroup);
        return true;
    }

    @Override
    public boolean deletePackageGroupById(UserPrincipal principal, Long id) {
        FnPackageGroup fnPackageGroup = fnPackageGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        long count = fnPackageGroup.getFnPackageList().stream().filter(BaseEntity::isDelActive).count();
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tồn tại " + count + " khoản đang áp dụng");
        }
        fnPackageGroup.setDelActive(AppConstant.APP_FALSE);
        fnPackageGroupRepository.save(fnPackageGroup);
        return true;
    }

    @Override
    public List<FnKidsPackageForGroupResponse> getPackageForAdd(UserPrincipal principal) {
        List<FnPackage> fnPackageList = fnPackageRepository.findByIdSchoolAndDelActiveTrueOrderByCategory(principal.getIdSchoolLogin());
        fnPackageList = fnPackageList.stream().filter(x -> x.getFnPackageGroup() == null).collect(Collectors.toList());
        return listMapper.mapList(fnPackageList, FnKidsPackageForGroupResponse.class);
    }

    @Override
    public boolean addPackageIntoGroup(UserPrincipal principal, Long idGroup, List<Long> idList) {
        FnPackageGroup fnPackageGroup = fnPackageGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(idGroup, principal.getIdSchoolLogin()).orElseThrow();
        List<FnPackage> fnPackageList = fnPackageRepository.findByIdInAndIdSchoolAndDelActiveTrue(idList, principal.getIdSchoolLogin());
        fnPackageList.forEach(x -> x.setFnPackageGroup(fnPackageGroup));
        fnPackageRepository.saveAll(fnPackageList);
        return true;
    }

    @Override
    public List<FnKidsPackageForGroupResponse> getPackageForRemove(UserPrincipal principal, Long idGroup) {
        FnPackageGroup fnPackageGroup = fnPackageGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(idGroup, principal.getIdSchoolLogin()).orElseThrow();
        return listMapper.mapList(fnPackageGroup.getFnPackageList(), FnKidsPackageForGroupResponse.class);
    }

    @Override
    public boolean removePackageInGroup(UserPrincipal principal, List<Long> idList) {
        List<FnPackage> fnPackageList = fnPackageRepository.findByIdInAndIdSchoolAndDelActiveTrue(idList, principal.getIdSchoolLogin());
        fnPackageList.forEach(x -> x.setFnPackageGroup(null));
        fnPackageRepository.saveAll(fnPackageList);
        return true;
    }

    @Override
    public List<PackageGroupStatisticalResponse> getStatisticalFees(UserPrincipal principal, PackageGroupSearchRequest request) {
        List<FnPackageGroup> fnPackageGroupList = fnPackageGroupRepository.findByIdSchoolAndNameContainingAndDelActiveTrueOrderByIdDesc(principal.getIdSchoolLogin(), request.getName());
        List<PackageGroupStatisticalResponse> responseList = new ArrayList<>();
        fnPackageGroupList.forEach(x -> {
            List<FnKidsPackage> fnKidsPackageList = new ArrayList<>();
            x.getFnPackageList().forEach(y -> fnKidsPackageList.addAll(FinanceUltils.getFnKidsPackageFromFnPackageAndMonthYear(y, request.getStartMonth(), request.getEndMonth(), request.getYear())));
            OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
            PackageGroupStatisticalResponse model = modelMapper.map(x, PackageGroupStatisticalResponse.class);
            modelMapper.map(orderMoneyModel, model);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<ExcelResponse> exportStatisticalFees(UserPrincipal principal, ExportRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        String schoolName = principal.getSchool().getSchoolName();
        String dateString = "Thời gian: từ tháng " + ConvertData.convertMonth(request.getStartMonth()) + " đến tháng " + ConvertData.convertMonth(request.getEndMonth()) + " năm " + request.getYear();
        List<String> stringList = Arrays.asList("SỐ LIỆU TỔNG HỢP CÁC NHÓM HỌC PHÍ DỊCH VỤ", "Trường: " + schoolName, dateString);
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(stringList);
        List<ExcelData> bodyList = new ArrayList<>();
        List<FnPackageGroup> fnPackageGroupList = fnPackageGroupRepository.findByIdInAndIdSchoolAndDelActiveTrue(request.getIdGroupList(), principal.getIdSchoolLogin());
        double moneyTotal = 0;
        double moneyPaid = 0;
        double moneyRemain = 0;
        int i = 0;
        for (FnPackageGroup x : fnPackageGroupList) {
            i++;
            List<FnKidsPackage> fnKidsPackageList = new ArrayList<>();
            x.getFnPackageList().forEach(y -> fnKidsPackageList.addAll(FinanceUltils.getFnKidsPackageFromFnPackageAndMonthYear(y, request.getStartMonth(), request.getEndMonth(), request.getYear())));
            OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
            PackageGroupStatisticalResponse model = modelMapper.map(x, PackageGroupStatisticalResponse.class);
            modelMapper.map(orderMoneyModel, model);
            moneyTotal += model.getMoneyTotalInOut();
            moneyPaid += model.getMoneyTotalInOutPaid();
            moneyRemain += model.getMoneyTotalInOutRemain();
            List<String> bodyStringList = Arrays.asList(String.valueOf(i), model.getName(), FinanceUltils.formatMoney((long) model.getMoneyTotalInOut()), FinanceUltils.formatMoney((long) model.getMoneyTotalInOutPaid()), FinanceUltils.formatMoney((long) model.getMoneyTotalInOutRemain()));
            ExcelData excelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(excelData);
        }
        List<String> bodyStringList = Arrays.asList("Tổng cộng", "", FinanceUltils.formatMoney((long) moneyTotal), FinanceUltils.formatMoney((long) moneyPaid), FinanceUltils.formatMoney((long) moneyRemain));
        ExcelData excelData = ExportExcelUtils.setBodyExcel(bodyStringList);
        bodyList.add(excelData);
        response.setSheetName("Nhóm học phí");
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<PackageGroupStatisticalResponse> getStatisticalSalary(UserPrincipal principal, PackageGroupSearchRequest request) {
        List<FnSalaryGroup> fnSalaryGroupList = fnSalaryGroupRepository.findByIdSchoolAndNameContainingAndDelActiveTrueOrderByIdDesc(principal.getIdSchoolLogin(), request.getName());
        List<PackageGroupStatisticalResponse> responseList = new ArrayList<>();
        fnSalaryGroupList.forEach(x -> {
            List<FnEmployeeSalary> fnKidsPackageList = FinanceUltils.getFnEmployeeSalaryFromFnSalaryAndMonthYear(x.getFnEmployeeSalaryList(), request.getStartMonth(), request.getEndMonth(), request.getYear());
            OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnKidsPackageList);
            PackageGroupStatisticalResponse model = modelMapper.map(x, PackageGroupStatisticalResponse.class);
            modelMapper.map(orderMoneyModel, model);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<ExcelResponse> exportStatisticalSalary(UserPrincipal principal, ExportRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        String schoolName = principal.getSchool().getSchoolName();
        String dateString = "Thời gian: từ tháng " + ConvertData.convertMonth(request.getStartMonth()) + " đến tháng " + ConvertData.convertMonth(request.getEndMonth()) + " năm " + request.getYear();
        List<String> stringList = Arrays.asList("SỐ LIỆU TỔNG HỢP CÁC NHÓM CÔNG LƯƠNG", "Trường: " + schoolName, dateString);
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(stringList);
        List<ExcelData> bodyList = new ArrayList<>();
        List<FnSalaryGroup> fnSalaryGroupList = fnSalaryGroupRepository.findByIdInAndIdSchoolAndDelActiveTrue(request.getIdGroupList(), principal.getIdSchoolLogin());
        double moneyTotal = 0;
        double moneyPaid = 0;
        double moneyRemain = 0;
        int i = 0;
        for (FnSalaryGroup x : fnSalaryGroupList) {
            i++;
            List<FnEmployeeSalary> fnKidsPackageList = FinanceUltils.getFnEmployeeSalaryFromFnSalaryAndMonthYear(x.getFnEmployeeSalaryList(), request.getStartMonth(), request.getEndMonth(), request.getYear());
            OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnKidsPackageList);
            PackageGroupStatisticalResponse model = modelMapper.map(x, PackageGroupStatisticalResponse.class);
            modelMapper.map(orderMoneyModel, model);
            moneyTotal += model.getMoneyTotalInOut();
            moneyPaid += model.getMoneyTotalInOutPaid();
            moneyRemain += model.getMoneyTotalInOutRemain();
            List<String> bodyStringList = Arrays.asList(String.valueOf(i), model.getName(), FinanceUltils.formatMoney((long) model.getMoneyTotalInOut()), FinanceUltils.formatMoney((long) model.getMoneyTotalInOutPaid()), FinanceUltils.formatMoney((long) model.getMoneyTotalInOutRemain()));
            ExcelData excelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(excelData);
        }
        List<String> bodyStringList = Arrays.asList("Tổng cộng", "", FinanceUltils.formatMoney((long) moneyTotal), FinanceUltils.formatMoney((long) moneyPaid), FinanceUltils.formatMoney((long) moneyRemain));
        ExcelData excelData = ExportExcelUtils.setBodyExcel(bodyStringList);
        bodyList.add(excelData);
        response.setSheetName("Nhóm công lương");
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<PackageGroupResponse> searchSalaryGroup(UserPrincipal principal, String name) {
        List<FnSalaryGroup> fnSalaryGroupList = fnSalaryGroupRepository.findByIdSchoolAndNameContainingAndDelActiveTrueOrderByIdDesc(principal.getIdSchoolLogin(), name);
        List<PackageGroupResponse> responseList = new ArrayList<>();
        fnSalaryGroupList.forEach(x -> {
            PackageGroupResponse model = modelMapper.map(x, PackageGroupResponse.class);
            model.setNumber((int) x.getFnEmployeeSalaryList().stream().filter(BaseEntity::isDelActive).count());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public PackageGroupResponse getSalaryGroupById(UserPrincipal principal, Long id) {
        FnSalaryGroup fnSalaryGroup = fnSalaryGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        return modelMapper.map(fnSalaryGroup, PackageGroupResponse.class);
    }

    @Override
    public boolean createSalaryGroup(UserPrincipal principal, PackageGroupCreateRequest request) {
        FnSalaryGroup fnSalaryGroup = modelMapper.map(request, FnSalaryGroup.class);
        fnSalaryGroup.setIdSchool(principal.getIdSchoolLogin());
        fnSalaryGroupRepository.save(fnSalaryGroup);
        return true;
    }

    @Override
    public boolean updateSalaryGroup(UserPrincipal principal, PackageGroupUpdateRequest request) {
        FnSalaryGroup fnSalaryGroup = fnSalaryGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), principal.getIdSchoolLogin()).orElseThrow();
        modelMapper.map(request, fnSalaryGroup);
        fnSalaryGroupRepository.save(fnSalaryGroup);
        return true;
    }

    @Override
    public boolean deleteSalaryGroupById(UserPrincipal principal, Long id) {
        FnSalaryGroup fnPackageGroup = fnSalaryGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        long count = fnPackageGroup.getFnEmployeeSalaryList().stream().filter(BaseEntity::isDelActive).count();
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tồn tại " + count + " khoản đang áp dụng");
        }
        fnPackageGroup.setDelActive(AppConstant.APP_FALSE);
        fnSalaryGroupRepository.save(fnPackageGroup);
        return true;
    }

    @Override
    public boolean addSalarySampleIntoGroup(UserPrincipal principal, Long idGroup, List<Long> idList, String type) {
        Long idSchool = principal.getIdSchoolLogin();
        FnSalaryGroup fnSalaryGroup = fnSalaryGroupRepository.findByIdAndIdSchoolAndDelActiveTrue(idGroup, idSchool).orElseThrow();
        if ("sample".equals(type)) {
            List<FnSalary> fnSalaryList = fnSalaryRepository.findByIdInAndSchoolIdAndDelActiveTrue(idList, idSchool);
            fnSalaryList.forEach(x -> x.setFnSalaryGroup(fnSalaryGroup));
            fnSalaryRepository.saveAll(fnSalaryList);
        } else if ("default".equals(type)) {
            List<FnEmployeeSalaryDefault> defaultList = fnEmployeeSalaryDefaultRepository.findByIdInAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(idList, idSchool);
            defaultList.forEach(x -> x.setFnSalaryGroup(fnSalaryGroup));
            fnEmployeeSalaryDefaultRepository.saveAll(defaultList);
        } else if ("salary".equals(type)) {
            List<FnEmployeeSalary> salaryList = fnEmployeeSalaryRepository.findByIdInAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(idList, idSchool);
            salaryList.forEach(x -> x.setFnSalaryGroup(fnSalaryGroup));
            fnEmployeeSalaryRepository.saveAll(salaryList);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "không có kiểu phù hợp");
        }
        return true;
    }

    @Override
    public List<PackageGroupResponse> getSalaryGroup(UserPrincipal principal) {
        List<FnSalaryGroup> fnSalaryGroupList = fnSalaryGroupRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(principal.getIdSchoolLogin());
        return listMapper.mapList(fnSalaryGroupList, PackageGroupResponse.class);
    }

    @Override
    public CashBookMoneyResponse getCashBookStatistical(UserPrincipal principal, CashBookMoneyRequest request) {
        CashBookMoneyResponse response = new CashBookMoneyResponse();
        FnCashBook fnCashBook = fnCashBookRepository.findBySchoolIdAndYear(principal.getIdSchoolLogin(), request.getYear()).orElseThrow();
        SearchCashBookHistoryRequest requestMapper = modelMapper.map(request, SearchCashBookHistoryRequest.class);
        requestMapper.setIdCashBook(fnCashBook.getId());
        List<CashBookHistory> cashBookHistoryCalculateList = cashBookHistoryRepository.searchCashBookHistory(fnCashBook.getYear(), requestMapper);
        List<CashBookHistory> cashBookHistoryBeforeList = cashBookHistoryRepository.findCashBookHistoryBefore(fnCashBook.getYear(), requestMapper);
        double moneyIn = cashBookHistoryCalculateList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
        double moneyOut = cashBookHistoryCalculateList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();

        double moneyInBefore = cashBookHistoryBeforeList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
        double moneyOutBefore = cashBookHistoryBeforeList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();
        double moneyStart = fnCashBook.getMoneyStart() + (moneyInBefore - moneyOutBefore);
        double moneyEnd = moneyStart + (moneyIn - moneyOut);

        response.setMoneyIn(moneyIn);
        response.setMoneyOut(moneyOut);
        response.setMoneyStart(moneyStart);
        response.setMoneyEnd(moneyEnd);
        return response;
    }

    @Override
    public InternalMoneyResponse getInternalStatistical(UserPrincipal principal, FinanceKidsStatisticalRequest request) {
        InternalMoneyResponse response=new InternalMoneyResponse();
        List<FnCashInternal> fnCashInternalList = fnCashInternalSchoolRepository.searchCashInternalStartEndMonth(principal.getIdSchoolLogin(), request.getStartMonth(), request.getEndMonth(), request.getYear());
        double moneyIn=fnCashInternalList.stream().filter(x->x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(FnCashInternal::getMoney).sum();
        double moneyOut=fnCashInternalList.stream().filter(x->x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(FnCashInternal::getMoney).sum();
        response.setMoneyIn(moneyIn);
        response.setMoneyOut(moneyOut);
        return response;
    }
}
