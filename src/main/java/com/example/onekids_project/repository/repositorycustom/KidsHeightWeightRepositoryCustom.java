package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KidsHeightWeightRepositoryCustom {

    Optional<Kids> findByIdHeightWeight(Long idSchool, Long id);



}
