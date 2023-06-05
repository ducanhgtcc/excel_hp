package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.ExAlbumKids;
import com.example.onekids_project.repository.repositorycustom.ExAlbumKidsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExAlbumKidsRepository extends JpaRepository<ExAlbumKids, Long>, ExAlbumKidsRepositoryCustom {

    Optional<ExAlbumKids> findByKidsIdAndAlbumId(Long idKid, Long idAlbum);

    @Query(value = "select * from ex_album_kids where id_album=:idAlbum and id_kids=:idKid ", nativeQuery = true)
    Optional<ExAlbumKids>  findExAlbumByIdAlbum(Long idAlbum, Long idKid);

    Optional<ExAlbumKids> findByAlbum_Id(Long idAlbum);

    @Modifying
    @Query(value = "delete from ex_album_kids where id_album=:id", nativeQuery = true)
    void deleteExAlbumByIdAlbum(Long id);

    List<ExAlbumKids> findByKidsIdAndStatusUnreadFalse(Long idKid);
}
