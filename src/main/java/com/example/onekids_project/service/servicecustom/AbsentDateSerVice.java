package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.AbsentDateDTO;
import com.example.onekids_project.response.parentdiary.ListAbsentDateResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AbsentDateSerVice {

    ListAbsentDateResponse findAllAbsentDate(Long idSchoolLogin, Pageable pageable);

    List<AbsentDateDTO> findByIdAbsentDate(Long idSchoolLogin, Long id);
}
