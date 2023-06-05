package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.repository.repositorycustom.PeopleTypeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeopleTypeRepository extends JpaRepository<FnPeopleType, Long>, PeopleTypeRepositoryCustom {

    Optional<FnPeopleType> findByIdAndDelActiveTrue(Long id);

    Optional<FnPeopleType> findByIdSchoolAndDefaultStatusTrue(Long id);
}
