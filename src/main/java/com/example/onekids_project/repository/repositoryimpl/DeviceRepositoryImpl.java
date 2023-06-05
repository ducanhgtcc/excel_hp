package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.DeviceTypeConstant;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.repository.repositorycustom.DeviceRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DeviceRepositoryImpl extends BaseRepositoryimpl<Device> implements DeviceRepositoryCustom {
    @Override
    public List<Device> findTokenFireBaseByIdEmployee(Long idUser) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idUser != null) {
                queryStr.append("and id_user =:idUser ");
                mapParams.put("idUser", idUser);

                    queryStr.append("and type =:type ");
                    mapParams.put("type", DeviceTypeConstant.ANDROID);
                    mapParams.put("type", DeviceTypeConstant.IOS);
        }

        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
