package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.repository.repositorycustom.MediaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long>, MediaRepositoryCustom {

    @Modifying
    @Query(value = "insert into ex_class_media(id_class, id_media) value(:idClass, :idMedia)", nativeQuery = true)
    void insertMediaForClass(Long idClass, Long idMedia);

    @Modifying
    @Query(value = "delete from ex_class_media where id_class=:idClass", nativeQuery = true)
    void deleteMediaForClass(Long idClass);

    /**
     * tìm kiếm tất cả các media
     *
     * @param idSchool
     * @return
     */
    List<Media> findByIdSchoolAndDelActiveTrueOrderByMediaNameAsc(Long idSchool);

    Optional<Media> findFirstByIdSchoolAndScopeTypeAndMediaTypeAndMediaActiveTrueAndDelActiveTrue(Long idSchool, String scopeType, String mediaType);

    /**
     * tìm kiếm media theo di
     *
     * @param id
     * @return
     */
    Optional<Media> findByIdAndDelActiveTrue(Long id);

    /**
     * tìm kiếm danh sách media của một lớp
     *
     * @param idClass
     * @return
     */
    List<Media> findByMaClassList_Id(Long idClass);

    List<Media> findByMediaActiveTrueAndDelActiveTrueAndMediaTypeAndMaClassList_Id(String type, Long id);

    @Query(value = "select * from ex_class_media where id_class=:id", nativeQuery = true)
    List<Long> findIdMediaByIdClass(Long id);

    @Query(value = "select id_cam from ex_class_cam where id_class=:id", nativeQuery = true)
    List<Long> findIdCamByIdClass(Long id);

}
