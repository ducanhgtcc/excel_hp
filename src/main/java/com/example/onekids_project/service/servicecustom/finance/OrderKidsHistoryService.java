package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.response.finance.order.OrderKidsHistoryResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-02 15:08
 *
 * @author lavanviet
 */
public interface OrderKidsHistoryService {
    OrderKidsHistory saveOrderKidsHistory(String category, String name, LocalDate date, double moneyInput, double moneyWallet, String description, CashBookHistory cashBookHistory, FnOrderKids fnOrderKids, String paymentType, double moneyCash, double moneyTransfer);

    List<OrderKidsHistoryResponse> findOrderKidsHistory(UserPrincipal principal, Long idOrder);


}
