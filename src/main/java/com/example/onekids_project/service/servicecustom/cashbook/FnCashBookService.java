package com.example.onekids_project.service.servicecustom.cashbook;

import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.cashbook.CashBookLockedRequest;
import com.example.onekids_project.request.cashbook.CashBookUpdateMoneyStartRequest;
import com.example.onekids_project.response.cashbook.CashBookResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-03-01 12:00
 *
 * @author lavanviet
 */
public interface FnCashBookService {
    void createCashBook(School school);

    void saveMoneyToCashBook(FnCashBook fnCashBook, String type, double money);

    List<CashBookResponse> findCashBook(UserPrincipal principal);

    boolean updateMoneyStart(UserPrincipal principal, CashBookUpdateMoneyStartRequest request);

    boolean updateLocked(UserPrincipal principal, CashBookLockedRequest request);
}
