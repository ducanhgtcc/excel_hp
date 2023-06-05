package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.employee.Subject;
import com.example.onekids_project.repository.repositorycustom.SubjectRepositoryCustom;
import com.example.onekids_project.response.subject.SubjectSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectRepositoryImpl extends BaseRepositoryimpl<Subject> implements SubjectRepositoryCustom {


    @Override
    public List<Subject> findAllSubject(Long idSchool, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school =:idSchool");
            mapParams.put("idSchool", idSchool);
        }

        return findAll(queryStr.toString(), mapParams, pageable);
    }
    @Override
    public List<Subject> findByIdsSubject(Long idSchool, List<Long> ids) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (!CollectionUtils.isEmpty(ids)) {
            queryStr.append("and id in (:ids)");
            mapParams.put("ids", ids);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Subject> searchSubject(Long idSchool, SubjectSearchRequest subjectSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        String name = subjectSearchRequest.getSubjectName();
        if (StringUtils.isNotBlank(name)) {
            name = name.trim();
            queryStr.append("and lower(subject_name) like lower(:subjectName) ");
            mapParams.put("subjectName", "%" + name + "%");
        }
        queryStr.append("order by subject_name asc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}

