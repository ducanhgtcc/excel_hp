package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.entity.finance.fees.ExOrderHistoryKidsPackage;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.ExOrderHistoryKidsPackageRepository;
import com.example.onekids_project.response.finance.order.KidsPackagePaymentDetailResponse;
import com.example.onekids_project.response.finance.order.OrderKidsHistoryDetailResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.ExOrderHistoryKidsPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-03-04 11:07
 *
 * @author lavanviet
 */
@Service
public class ExOrderHistoryKidsPackageServiceImpl implements ExOrderHistoryKidsPackageService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ExOrderHistoryKidsPackageRepository exOrderHistoryKidsPackageRepository;

    @Override
    public List<OrderKidsHistoryDetailResponse> findOrderKidsHistoryDetail(UserPrincipal principal, Long idOrderHistory) {
        List<ExOrderHistoryKidsPackage> exOrderHistoryKidsPackageList = exOrderHistoryKidsPackageRepository.findByOrderKidsHistoryId(idOrderHistory);
        List<OrderKidsHistoryDetailResponse> responseList = new ArrayList<>();
        exOrderHistoryKidsPackageList.forEach(x -> {
            OrderKidsHistoryDetailResponse model = new OrderKidsHistoryDetailResponse();
            model.setId(x.getId());
            model.setMoney(x.getMoney());
            model.setName(x.getFnKidsPackage().getFnPackage().getName());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<KidsPackagePaymentDetailResponse> findKidsPackagePaymentDetail(UserPrincipal principal, Long idKidsPackage) {
        List<ExOrderHistoryKidsPackage> exOrderHistoryKidsPackageList = exOrderHistoryKidsPackageRepository.findByFnKidsPackageIdOrderByIdDesc(idKidsPackage);
        return listMapper.mapList(exOrderHistoryKidsPackageList, KidsPackagePaymentDetailResponse.class);
    }
}
