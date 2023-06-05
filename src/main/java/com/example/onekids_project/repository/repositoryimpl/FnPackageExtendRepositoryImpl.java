package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.example.onekids_project.repository.repositorycustom.FnPackageExtendRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-10-01 16:04
 *
 * @author lavanviet
 */
public class FnPackageExtendRepositoryImpl extends BaseRepositoryimpl<FnPackageExtend> implements FnPackageExtendRepositoryCustom {
    @Override
    public List<FnPackageExtend> searchPackageExtend(Long idSchool, String name) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(name)) {
            queryStr.append("and name like :name ");
            mapParams.put("name", name.trim());
        }
        queryStr.append("order by id desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
