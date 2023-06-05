package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.ExAlbumKids;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ExAlbumKidsRepositoryCustom {

    List<ExAlbumKids> findAlbumMoblieforClass(Long idKids, Long idClass);

    List<ExAlbumKids> findAlbumMoblieforSchool1(Long idKids, Long idClass);

    List<ExAlbumKids> findAlbumMoblie(Long idKids, LocalDateTime localDateTime, Pageable pageable);

    long countAlbumMobile(Long idKids, LocalDateTime localDateTime);

    List<ExAlbumKids> findByIdAlbumClass(Long idAlbum);

    List<ExAlbumKids> findByidAlbum(int idAlbum);

    List<ExAlbumKids> findAlbumMoblieForteacher(Long idClass);
}


