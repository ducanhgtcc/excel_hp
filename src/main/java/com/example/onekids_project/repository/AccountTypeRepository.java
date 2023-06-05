package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.AccountType;
import com.example.onekids_project.entity.user.SmsSendCustom;
import com.example.onekids_project.repository.repositorycustom.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    List<AccountType> findByIdSchool(Long idSchool);

    AccountType findAllByDelActiveTrueAndNameAndAndIdSchool(String name, Long id);
}
