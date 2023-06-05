package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.EmployeeNotify;

import java.util.Optional;

public interface NotifyPlusSchoolRepositoryCustom {
    Optional<EmployeeNotify> findAppIconNotifyPlus(Long id);
}
