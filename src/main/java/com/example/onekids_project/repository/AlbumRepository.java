package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.repository.repositorycustom.AlbumRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long>, AlbumRepositoryCustom {
   Album  findByAlistPictureListId(Long id);

   Optional<Album> findByIdAndDelActiveTrue(Long id);

   Optional<Album> findByIdAndDelActiveFalse(Long id);

//   List<Album> findByMaClassIdAndCreatedDate_DateAndDelActiveTrue(Long idClass, LocalDate date);

   List<Album> findByMaClassIdAndDelActiveTrue(Long idClass);
   List<Album> findByIdSchoolAndDelActiveTrue(Long idSchool);

//   List<Album> findByMaClassGradeIdAndCreatedDate_DateAndDelActiveTrue(Long idGrade, LocalDate date);

   @Modifying
   @Query(value = "delete from ma_album where id=:id", nativeQuery = true)
   void deleteAlbumById(Long id);

   List<Album> countByIdSchoolAndDelActiveTrue(Long idSchool);

   Optional<Album> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

   List<Album> findAllByIdSchoolAndDelActiveTrue(Long idSchool);

}
