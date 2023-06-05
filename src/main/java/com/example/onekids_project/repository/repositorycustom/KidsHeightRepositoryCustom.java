package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.KidsHeight;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface KidsHeightRepositoryCustom {
    List<KidsHeight> findKidsHeight(Long idKid, LocalDate localDate,  Pageable pageable);
    List<KidsHeight> findKidsHeightOfClass(Long idKid);

}
