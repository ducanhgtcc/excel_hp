package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.finance.fees.ExOrderHistoryKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.repository.OrderKidsHistoryRepository;
import com.example.onekids_project.response.finance.order.OrderKidsHistoryResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.OrderKidsHistoryService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-03-02 15:08
 *
 * @author lavanviet
 */
@Service
public class OrderKidsHistoryServiceImpl implements OrderKidsHistoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderKidsHistoryRepository orderKidsHistoryRepository;

    @Override
    public OrderKidsHistory saveOrderKidsHistory(String category, String name, LocalDate date, double moneyInput, double moneyWallet, String description, CashBookHistory cashBookHistory, FnOrderKids fnOrderKids, String paymentType, double moneyCash, double moneyTransfer) {
        OrderKidsHistory orderKidsHistory = new OrderKidsHistory();
        orderKidsHistory.setCategory(category);
        orderKidsHistory.setName(name);
        orderKidsHistory.setDate(date);
        orderKidsHistory.setMoneyInput(moneyInput);
        orderKidsHistory.setMoneyWallet(category.equals(FinanceConstant.CATEGORY_IN) ? moneyWallet : 0);
        orderKidsHistory.setDescription(description);
        orderKidsHistory.setCashBookHistory(cashBookHistory);
        orderKidsHistory.setFnOrderKids(fnOrderKids);
        orderKidsHistory.setPaymentType(paymentType);
        double moneyTotal = moneyInput + moneyWallet;
        if (FinanceConstant.PAYMENT_CASH.equals(paymentType)) {
            orderKidsHistory.setMoneyCash(moneyTotal);
            orderKidsHistory.setMoneyTransfer(0d);
        } else if (FinanceConstant.PAYMENT_TRANSFER.equals(paymentType)) {
            orderKidsHistory.setMoneyCash(0d);
            orderKidsHistory.setMoneyTransfer(moneyTotal);
        } else if (FinanceConstant.PAYMENT_BOTH.equals(paymentType)) {
            orderKidsHistory.setMoneyCash(moneyCash);
            orderKidsHistory.setMoneyTransfer(moneyTransfer);
        }
        return orderKidsHistoryRepository.save(orderKidsHistory);
    }

    @Override
    public List<OrderKidsHistoryResponse> findOrderKidsHistory(UserPrincipal principal, Long idOrder) {
        CommonValidate.checkDataPlus(principal);
        List<OrderKidsHistory> orderKidsHistoryList = orderKidsHistoryRepository.findByFnOrderKidsIdOrderByIdDesc(idOrder);
        List<OrderKidsHistoryResponse> responseList = new ArrayList<>();
        orderKidsHistoryList.forEach(x -> {
            OrderKidsHistoryResponse model = modelMapper.map(x, OrderKidsHistoryResponse.class);
            double moneyPaymentTotal = x.getExOrderHistoryKidsPackageList().stream().mapToDouble(ExOrderHistoryKidsPackage::getMoney).sum();
            model.setMoneyPayment(moneyPaymentTotal);
            responseList.add(model);
        });
        return responseList;
    }


}
