package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.employee.EmployeeNotify;
import com.example.onekids_project.repository.repositorycustom.NotifyPlusSchoolRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NotifyPlusSchoolRepositoryImpl extends BaseRepositoryimpl<EmployeeNotify> implements NotifyPlusSchoolRepositoryCustom {
    @Override
    public Optional<EmployeeNotify> findAppIconNotifyPlus(Long id) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (id != null) {
            queryStr.append("and id_info_employee=:id ");
            mapParams.put("id", id);
        }
        List<EmployeeNotify> employeeNotifies = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(employeeNotifies)) {
            return null;
        }

        if (employeeNotifies.size() > 0) {
            return Optional.ofNullable(employeeNotifies.get(0));
        }
        return Optional.empty();
    }

}
