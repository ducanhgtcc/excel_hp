package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.repository.repositorycustom.AbsentDateRepositoryCustom;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsentDateRepositoryImpl extends BaseRepositoryimpl<AbsentDate> implements AbsentDateRepositoryCustom {


    @Override
    public List<AbsentDate> findAllAbsentDate(Long idSchoolLogin, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public  List<AbsentDate> findByIdAbsentDate(Long idSchoolLogin, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (id != null) {
            queryStr.append("and id_absent_letter=:id");
            mapParams.put("id", id);
        }
        List<AbsentDate> absentDateList = findAllNoPaging(queryStr.toString(), mapParams);
        return absentDateList;
    }
}
