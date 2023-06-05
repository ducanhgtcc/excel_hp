package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.repository.repositorycustom.FnBankRepositoryCustom;
import com.example.onekids_project.repository.repositorycustom.PeopleTypeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FnBankRepository extends JpaRepository<FnBank, Long>, FnBankRepositoryCustom {

    Optional<FnBank> findByIdAndDelActiveTrue(Long id);

    Optional<FnBank> findByIdAndSchoolIdAndDelActiveTrue(Long id, Long idSchool);

    List<FnBank> findBySchoolIdAndDelActiveTrue(Long idSchool);

    Optional<FnBank> findBySchoolIdAndCheckedTrueAndDelActiveTrue(Long idSchool);

    List<FnBank> findBySchoolIdAndCheckedTrue(Long idSchool);
}
