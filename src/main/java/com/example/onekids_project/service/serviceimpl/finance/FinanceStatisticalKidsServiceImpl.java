package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.FnOrderKidsRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalMiniRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalMiniResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.FinanceStatisticalKidsService;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-03-19 09:30
 *
 * @author lavanviet
 */
@Service
public class FinanceStatisticalKidsServiceImpl implements FinanceStatisticalKidsService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;

    @Override
    public FinanceKidsStatisticalResponse statisticalFinanceKids(UserPrincipal principal, FinanceKidsStatisticalRequest request) {
        CommonValidate.checkDataPlus(principal);
        int year = request.getYear();
        int startMonth = request.getStartMonth();
        int endMonth = request.getEndMonth();
        this.checkStartEndMonth(startMonth, endMonth);
        FinanceKidsStatisticalResponse response = new FinanceKidsStatisticalResponse();
        List<Kids> kidsList = kidsRepository.findByIdSchool(principal.getIdSchoolLogin());
        int kidsNumber = 0;
        int orderNumber = 0;
        List<FnKidsPackage> fnKidsPackageAllList = new ArrayList<>();
        for (Kids x : kidsList) {
            List<FnKidsPackage> fnKidsPackageList = x.getFnKidsPackageList().stream().filter(a -> a.isApproved() && a.getYear() == year && a.getMonth() >= startMonth && a.getMonth() <= endMonth).collect(Collectors.toList());
            fnKidsPackageAllList.addAll(fnKidsPackageList);
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
            if (orderNumberModel.getEnoughNumber() != orderNumberModel.getTotalNumber()) {
                kidsNumber++;
            }
            List<Integer> monthList = fnKidsPackageList.stream().filter(a -> a.getPaid() == 0 || a.getPaid() < FinanceUltils.getMoneyCalculate(a)).map(FnKidsPackage::getMonth).distinct().collect(Collectors.toList());
            orderNumber += fnOrderKidsRepository.countByKidsIdAndMonthIn(x.getId(), monthList);
        }
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageAllList);
        modelMapper.map(orderMoneyModel, response);
        response.setKidsNumber(kidsNumber);
        response.setOrderNumber(orderNumber);
        return response;
    }

    @Override
    public FinanceKidsStatisticalMiniResponse statisticalFinanceKidsMini(UserPrincipal principal, FinanceKidsStatisticalMiniRequest request) {
        CommonValidate.checkDataPlus(principal);
        int year = request.getYear();
        int startMonth = request.getStartMonth();
        int endMonth = request.getEndMonth();
        this.checkStartEndMonth(startMonth, endMonth);
        FinanceKidsStatisticalMiniResponse response = new FinanceKidsStatisticalMiniResponse();
        List<Kids> kidsList = kidsRepository.findByKidsIdSchoolWithClassWithStatus(principal.getIdSchoolLogin(), request.getIdClass(), request.getStatus());
        List<FnKidsPackage> fnKidsPackageAllList = new ArrayList<>();
        for (Kids x : kidsList) {
            List<FnKidsPackage> fnKidsPackageList = x.getFnKidsPackageList().stream().filter(a -> a.isApproved() && a.getYear() == year && a.getMonth() >= startMonth && a.getMonth() <= endMonth).collect(Collectors.toList());
            fnKidsPackageAllList.addAll(fnKidsPackageList);
        }
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageAllList);
        modelMapper.map(orderMoneyModel, response);
        return response;
    }

    private void checkStartEndMonth(int startMonth, int endMonth) {
        if (startMonth > endMonth) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tháng bắt đầu không thể lớn hơn tháng kết thúc");
        }
    }
}
