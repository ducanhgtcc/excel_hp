package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListPictureRepository extends JpaRepository<ListPicture, Long> {
    void deleteByAlbum(Album album);

    List<ListPicture> findByAlbum_IdAndDelActiveTrue(Long idAlbum);

    @Query(value = "select id_album from list_picture where id=:id", nativeQuery = true)
    long findIdAlbum(Long id);

    @Modifying
    @Query(value = "delete from list_picture where id_album=:id", nativeQuery = true)
    void deleteListPictureById(Long id);

    @Modifying
    @Query(value = "delete from list_picture where id=:id", nativeQuery = true)
    void deletePictureById(Long id);

    List<ListPicture> findByAlbumIdAndIsApprovedTrueAndDelActiveTrue(Long idAlbum);



}
