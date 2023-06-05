package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.repository.repositorycustom.GradeRepositoryCustom;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.grade.SearchGradeRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GradeRepositoryImpl extends BaseRepositoryimpl<Grade> implements GradeRepositoryCustom {

    @Override
    public List<Grade> findGradeInSchool(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by grade_name collate utf8_vietnamese_ci");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Grade> findAllGradeCommon(Long idSchool) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            queryStr.append("order by lower(grade_name) asc");
            mapParams.put("idSchool", idSchool);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Grade> findAllGrade(Long idSchool, PageNumberWebRequest request) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            queryStr.append("order by lower(grade_name) asc");
            mapParams.put("idSchool", idSchool);
        }
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countGrade(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            queryStr.append("order by lower(grade_name) asc");
            mapParams.put("idSchool", idSchool);
        }
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<Grade> findByIdGrade(Long idSchool, Long id) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Grade> gradeList = findAllNoPaging(queryStr.toString(), mapParams);
        if (gradeList.size() > 0) {
            return Optional.ofNullable(gradeList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Grade> searchGrade(Long idSchool, Pageable pageable, SearchGradeRequest searchGradeRequest) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchGradeRequest != null) {

            if (idSchool != null) {
                queryStr.append("and id_school=:idSchool ");
                mapParams.put("idSchool", idSchool);
            }

            if (StringUtils.isNotBlank(searchGradeRequest.getGradeName())) {
                queryStr.append("and grade_name like :gradeName");
                mapParams.put("gradeName", "%" + searchGradeRequest.getGradeName() + "%");
            }

        }

        return findAll(queryStr.toString(), mapParams, pageable);
    }
}
