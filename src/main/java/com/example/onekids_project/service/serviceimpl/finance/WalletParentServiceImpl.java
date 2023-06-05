package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.WalletParentHistoryRepository;
import com.example.onekids_project.repository.WalletParentRepository;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.common.SearchKidsCommonExcelRequest;
import com.example.onekids_project.request.common.SearchKidsCommonRequest;
import com.example.onekids_project.request.finance.wallet.WalletParentStatisticalRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.wallet.ListWalletParentStatisticalResponse;
import com.example.onekids_project.response.finance.wallet.WalletParentCustom1;
import com.example.onekids_project.response.finance.wallet.WalletParentResponse;
import com.example.onekids_project.response.finance.wallet.WalletParentStatisticalResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.finance.WalletParentService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-02-24 15:53
 *
 * @author lavanviet
 */
@Service
public class WalletParentServiceImpl implements WalletParentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private WalletParentRepository walletParentRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;

    @Autowired
    private SchoolService schoolService;

    @Override
    public List<WalletParentResponse> searchWalletParent(UserPrincipal principal, SearchKidsCommonRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusName(request.getIdClass(), request.getStatus(), request.getFullName());
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<WalletParentResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            WalletParentResponse model = modelMapper.map(x, WalletParentResponse.class);
            WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
            long number = walletParentHistoryRepository.countWalletParentHistoryFalse(walletParent.getId());
            WalletParentCustom1 walletParentCustom1 = modelMapper.map(walletParent, WalletParentCustom1.class);
            model.setWalletParent(walletParentCustom1);
            model.setNumberStatus(number);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<WalletParentResponse> searchWalletParentUnConfirm(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndParentIsNotNullAndDelActiveTrue(principal.getIdSchoolLogin());
        List<WalletParentResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            WalletParentResponse model = modelMapper.map(x, WalletParentResponse.class);
            model.setClassName(x.getMaClass().getClassName());
            WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
            long number = walletParentHistoryRepository.countWalletParentHistoryFalse(walletParent.getId());
            WalletParentCustom1 walletParentCustom1 = modelMapper.map(walletParent, WalletParentCustom1.class);
            model.setWalletParent(walletParentCustom1);
            model.setNumberStatus(number);
            if (number > 0){
                responseList.add(model);
            }
        });
        return responseList;
    }

    @Override
    public ListWalletParentStatisticalResponse searchWalletParentStatistical(UserPrincipal principal, WalletParentStatisticalRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<WalletParent> walletParentAllList = walletParentRepository.findByIdSchool(idSchool);
        List<WalletParent> walletParentList = walletParentRepository.searchWalletParentStatistical(idSchool, request);
        long total = walletParentRepository.countWalletParentStatistical(idSchool, request);
        ListWalletParentStatisticalResponse response = new ListWalletParentStatisticalResponse();
        List<WalletParentStatisticalResponse> dataList = new ArrayList<>();
        for (WalletParent x : walletParentList) {
            WalletParentStatisticalResponse model = modelMapper.map(x, WalletParentStatisticalResponse.class);
            model.setParentName(x.getParent().getMaUser().getFullName());
            model.setKidsNameList(x.getParent().getKidsList().stream().map(Kids::getFullName).collect(Collectors.toList()));
            dataList.add(model);
        }
        double moneyInTotal = walletParentAllList.stream().mapToDouble(WalletParent::getMoneyIn).sum();
        double moneyOutTotal = walletParentAllList.stream().mapToDouble(WalletParent::getMoneyOut).sum();
        response.setMoneyInTotal(moneyInTotal);
        response.setMoneyOutTotal(moneyOutTotal);
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public List<WalletParentResponse> searchWalletParentExcel(IdListRequest request) {
        List<Kids> kidsList = kidsRepository.findByIdInAndDelActiveTrue(request.getIdList());
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<WalletParentResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            WalletParentResponse model = modelMapper.map(x, WalletParentResponse.class);
            WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
            long number = walletParentHistoryRepository.countWalletParentHistoryFalse(walletParent.getId());
            WalletParentCustom1 walletParentCustom1 = modelMapper.map(walletParent, WalletParentCustom1.class);
            model.setWalletParent(walletParentCustom1);
            model.setNumberStatus(number);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<WalletParentResponse> searchWalletParentExcelProviso(SearchKidsCommonExcelRequest request) {
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusNameExcel(request.getIdClass(), request.getStatus());
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<WalletParentResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            WalletParentResponse model = modelMapper.map(x, WalletParentResponse.class);
            WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
            long number = walletParentHistoryRepository.countWalletParentHistoryFalse(walletParent.getId());
            WalletParentCustom1 walletParentCustom1 = modelMapper.map(walletParent, WalletParentCustom1.class);
            model.setWalletParent(walletParentCustom1);
            model.setNumberStatus(number);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<ExcelResponse> exportWalletParentExcel(UserPrincipal principal, List<WalletParentResponse> request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        List<ExcelData> headerList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";

        response.setSheetName("DS Ví");
        int i = 1;
        for (WalletParentResponse model : request){
            List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CÓ VÍ", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_DATE.concat(ConvertData.convertDateString(LocalDate.now())));
            headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), model.getWalletParent().getCode(), model.getFullName(), ConvertData.convertDateString(model.getBirthDay()),
                    FinanceUltils.formatMoney((long) model.getWalletParent().getMoneyIn()),
                    FinanceUltils.formatMoney((long) model.getWalletParent().getMoneyOut()),
                    FinanceUltils.formatMoney((long) (model.getWalletParent().getMoneyIn() - model.getWalletParent().getMoneyOut())),
                    String.valueOf(model.getNumberStatus()));
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

}
