package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.usermaster.SchoolMaster;
import com.example.onekids_project.master.request.SchoolMasterSearchRequest;
import com.example.onekids_project.repository.repositorycustom.SchoolMasterRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolMasterRepositoryImpl extends BaseRepositoryimpl<SchoolMaster> implements SchoolMasterRepositoryCustom {
    @Override
    public List<SchoolMaster> findAllSchoolMaster(SchoolMasterSearchRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSchoolMaster(queryStr, mapParams, request, idSchoolList);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), request.isDeleteStatus());
    }

    @Override
    public long countSchoolMaster(SchoolMasterSearchRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSchoolMaster(queryStr, mapParams, request, idSchoolList);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, request.isDeleteStatus());
    }

    private void setSearchSchoolMaster(StringBuilder queryStr, Map<String, Object> mapParams, SchoolMasterSearchRequest request, List<Long> idSchoolList) {
        if (request != null) {
            queryStr.append("and id_school in (:idSchoolList) ");
            mapParams.put("idSchoolList", idSchoolList);
            if (request.getIdSchool() != null) {
                queryStr.append("and id_school =:idSchool ");
                mapParams.put("idSchool", request.getIdSchool());
            }
            if (request.getActivated() != null) {
                queryStr.append("and exists (Select*from ma_user as model1 where model.id_ma_user=model1.id and model1.activated=:activated) ");
                mapParams.put("activated", request.getActivated());
            }
            String nameOrPhone = request.getNameOrPhone();
            if (StringUtils.isNotBlank(nameOrPhone)) {
                nameOrPhone = nameOrPhone.trim();
                queryStr.append("and (lower(full_name) like lower(:fullName) ");
                mapParams.put("fullName", "%" + nameOrPhone + "%");
                queryStr.append("or phone like :phone) ");
                mapParams.put("phone", "%" + nameOrPhone + "%");
            }
            queryStr.append("order by id_school ASC ");
        }
    }

}
