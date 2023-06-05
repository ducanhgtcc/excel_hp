package com.example.onekids_project.teacher.service.servicecustom;

import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.teacher.response.statistical.StatisticalDataResponse;

import java.time.LocalDate;

/**
 * date 2021-04-19 10:41
 *
 * @author lavanviet
 */
public interface StatisticalService {
    StatisticalDataResponse getStatisticalData(UserPrincipal principal, LocalDate date);
}
