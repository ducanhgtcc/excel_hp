package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.master.request.admin.MaAdminSearchRequest;
import com.example.onekids_project.repository.repositorycustom.MaAdminRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaAdminRepositoryImpl extends BaseRepositoryimpl<MaAdmin> implements MaAdminRepositoryCustom {
    @Override
    public List<MaAdmin> searchMaAdmin(MaAdminSearchRequest maAdminSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isNotBlank(maAdminSearchRequest.getStatus())) {
            queryStr.append("and model.admin_status=:adminStatus ");
            mapParams.put("adminStatus", maAdminSearchRequest.getStatus());
        }
        if (maAdminSearchRequest.getActivated() != null) {
            queryStr.append("and exists (Select * from ma_user where ma_user.id=model.id_ma_user and ma_user.activated=:activated) ");
            mapParams.put("activated", maAdminSearchRequest.getActivated());
        }
        String NameOrPhone = maAdminSearchRequest.getNameOrPhone();
        if (StringUtils.isNotBlank(NameOrPhone)) {
            NameOrPhone = NameOrPhone.trim();
            queryStr.append("and (phone like :phone ");
            mapParams.put("phone", "%" + NameOrPhone + "%");

            queryStr.append("or lower(full_name) like lower(:fullName)) ");
            mapParams.put("fullName", "%" + NameOrPhone + "%");
        }
        queryStr.append("order by id desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);

    }
}
