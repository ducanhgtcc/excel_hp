package com.example.onekids_project.repository;

import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.importexport.model.HeightWeightModel;
import com.example.onekids_project.response.kidsheightweight.KidsHeightWeightResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KidsWeightHeightRepository extends JpaRepository<KidsWeight, Long> {

    Optional<KidsHeight> findByIdAndDelActive(Long id, boolean delActive);
}
