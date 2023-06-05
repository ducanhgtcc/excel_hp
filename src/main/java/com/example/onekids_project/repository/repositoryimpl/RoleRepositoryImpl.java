package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.user.Role;
import com.example.onekids_project.repository.repositorycustom.RoleRepositoryCustom;
import com.example.onekids_project.request.user.SearchRoleRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleRepositoryImpl extends BaseRepositoryimpl<Role> implements RoleRepositoryCustom {
    @Override
    public List<Role> searchRole(Long idSchool, SearchRoleRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getType())) {
            queryStr.append("and type=:type ");
            mapParams.put("type", request.getType());
        }
        if (StringUtils.isNotBlank(request.getRoleName())) {
            queryStr.append("and role_name=:roleName ");
            mapParams.put("roleName", request.getRoleName().trim());
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

}
