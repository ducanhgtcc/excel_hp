package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.KidsWeight;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface KidsWeightRepositoryCustom {

    List<KidsWeight> findKidsWeight(Long idKid, LocalDate localDate, Pageable pageable);
    List<KidsWeight> findKidsWeightfClass(Long idKid);

}
