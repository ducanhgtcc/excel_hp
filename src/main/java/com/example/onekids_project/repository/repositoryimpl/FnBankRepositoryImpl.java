package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.repository.repositorycustom.FnBankRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnBankRepositoryImpl extends BaseRepositoryimpl<FnBank> implements FnBankRepositoryCustom {

    @Override
    public List<FnBank> searchBank(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by id desc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

}
