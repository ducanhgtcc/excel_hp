package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.CycleMoneyConstant;
import com.example.onekids_project.entity.school.CycleMoney;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.CycleMoneyRepository;
import com.example.onekids_project.request.cyclemoney.CycleMoneyRequest;
import com.example.onekids_project.response.cyclemoney.CycleMoneyResponse;
import com.example.onekids_project.service.servicecustom.finance.CycleMoneyService;
import com.example.onekids_project.util.SchoolUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lavanviet
 */
@Service
public class CycleMoneyServiceImpl implements CycleMoneyService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CycleMoneyRepository cycleMoneyRepository;

    @Override
    public void createCycleMoneyDefault(School school) {
        CycleMoney entity = new CycleMoney();
        entity.setSchool(school);
        entity.setTypeFees(CycleMoneyConstant.TYPE_DEFAULT);
        entity.setStartDateFees(10);
        entity.setEndDateFees(10);
        entity.setRangeFees(CycleMoneyConstant.RANGE2);
        entity.setTypeSalary(CycleMoneyConstant.TYPE_DEFAULT);
        entity.setStartDateSalary(5);
        entity.setEndDateSalary(5);
        entity.setRangeSalary(CycleMoneyConstant.RANGE1);
        entity.setTransferMoneyType(CycleMoneyConstant.TRANSFER_MONEY_WALLET);
        cycleMoneyRepository.save(entity);
    }

    @Override
    public CycleMoneyResponse getCycleMoneyService() {
        Long idSchool = SchoolUtils.getIdSchool();
        CycleMoney cycleMoney = cycleMoneyRepository.findBySchoolId(idSchool).orElseThrow();
        return modelMapper.map(cycleMoney, CycleMoneyResponse.class);
    }

    @Override
    public void updateCycleMoneyService(CycleMoneyRequest request) {
        Long idSchool = SchoolUtils.getIdSchool();
        CycleMoney cycleMoney = cycleMoneyRepository.findBySchoolId(idSchool).orElseThrow();
        modelMapper.map(request, cycleMoney);
        cycleMoneyRepository.save(cycleMoney);
    }
}
