package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.mobile.plus.response.fees.FeesClassResponse;
import com.example.onekids_project.mobile.request.IdAndDateRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalMiniRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalMiniResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-19 09:30
 *
 * @author lavanviet
 */
public interface FinanceStatisticalKidsService {
    FinanceKidsStatisticalResponse statisticalFinanceKids(UserPrincipal principal, FinanceKidsStatisticalRequest request);
    FinanceKidsStatisticalMiniResponse statisticalFinanceKidsMini(UserPrincipal principal, FinanceKidsStatisticalMiniRequest request);
}
