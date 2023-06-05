package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.AbsentDate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AbsentDateRepositoryCustom {
    List<AbsentDate> findAllAbsentDate(Long idSchoolLogin, Pageable pageable);

    List<AbsentDate> findByIdAbsentDate(Long idSchoolLogin, Long id);
}


