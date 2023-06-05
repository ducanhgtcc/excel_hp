package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.repositorycustom.SchoolRepositoryCustom;
import com.example.onekids_project.request.brand.SearchBrandClickSchoolConfigRequest;
import com.example.onekids_project.request.brand.SearchSchoolBrandRequest;
import com.example.onekids_project.request.school.SearchSchoolRequest;
import com.example.onekids_project.request.system.SchoolConfigSeachRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolRepositoryImpl extends BaseRepositoryimpl<School> implements SchoolRepositoryCustom {


    @Override
    public List<School> findAllSchool(Pageable pageable) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public List<School> searchSchool(SearchSchoolRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSchool(queryStr, mapParams, request);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), request.isDeleteStatus());
    }

    @Override
    public long countsearchSchool(SearchSchoolRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSchool(queryStr, mapParams, request);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, request.isDeleteStatus());
    }

    private void setSearchSchool(StringBuilder queryStr, Map<String, Object> mapParams, SearchSchoolRequest request) {
        if (request != null) {
            if (request.getIdAgent() != null) {
                queryStr.append("and id_agent =:idAgent ");
                mapParams.put("idAgent", request.getIdAgent());
            }
            if (request.getIdSchool() != null) {
                queryStr.append("and id =:idSchool ");
                mapParams.put("idSchool", request.getIdSchool());
            }
            if (request.getActivated() != null) {
                queryStr.append("and school_active =:activeSchool ");
                mapParams.put("activeSchool", request.getActivated());
            }
            if (StringUtils.isNotBlank(request.getName())) {
                String CodeOrName = request.getName().trim();
                queryStr.append("and lower(school_name) like lower(:schoolName) ");
                mapParams.put("schoolName", "%" + CodeOrName + "%");
            }
            queryStr.append("order by id desc");
        }
    }

    @Override
    public List<School> searchSchoolForConfigIcon(SchoolConfigSeachRequest schoolConfigSeachRequest) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (schoolConfigSeachRequest.getIdAgent() != null) {
            queryStr.append("and id_agent=:idAgent ");
            mapParams.put("idAgent", schoolConfigSeachRequest.getIdAgent());
        }
        if (StringUtils.isNotBlank(schoolConfigSeachRequest.getSchoolName())) {
            String schoolName = schoolConfigSeachRequest.getSchoolName();
            if (StringUtils.isNotBlank(schoolName)) {
                schoolName = schoolName.trim();
                queryStr.append("and school_name like :schoolName ");
                mapParams.put("schoolName", "%" + schoolName + "%");
            }
        }
        queryStr.append("order by school_name asc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<School> searchSchoolBrand(SearchSchoolBrandRequest searchSchoolBrandRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchSchoolBrandRequest != null) {
            if (searchSchoolBrandRequest.getIdAgent() != null) {
                queryStr.append("and id_agent=:idAgent ");
                mapParams.put("idAgent", searchSchoolBrandRequest.getIdAgent());
            }
            if (StringUtils.isNotBlank(searchSchoolBrandRequest.getName())) {
                queryStr.append("and school_name like :schoolName ");
                mapParams.put("schoolName", "%" + searchSchoolBrandRequest.getName() + "%");
            }
            queryStr.append("order by id_agent ASC ");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<School> searchSchoolBrand1(SearchBrandClickSchoolConfigRequest searchBrandClickSchoolConfigRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchBrandClickSchoolConfigRequest != null) {

        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<School> getSchoolExpired(int number) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and school_active=true ");
        queryStr.append("and case when trial_status=true and demo_end is not null then datediff(date(demo_end), :nowDate)>=0 and datediff(date(demo_end), :nowDate)<=:number " +
                "when trial_status=false and date_end is not null then datediff(date(date_end), :nowDate)>=0 and datediff(date(date_end), :nowDate)<=:number else false end ");
        mapParams.put("nowDate", LocalDate.now());
        mapParams.put("number", number);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<School> getSchoolExpiredHandle() {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and school_active=true ");
        queryStr.append("and case when trial_status=true and demo_end is not null then datediff(date(demo_end), current_date)<=0 " +
                "when trial_status=false and date_end is not null then datediff(date(date_end), current_date)<=0 else false end ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
