package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.cyclemoney.CycleMoneyRequest;
import com.example.onekids_project.response.cyclemoney.CycleMoneyResponse;

/**
 * @author lavanviet
 */
public interface CycleMoneyService {
    void createCycleMoneyDefault(School school);

    CycleMoneyResponse getCycleMoneyService();

    void updateCycleMoneyService(CycleMoneyRequest request);
}
