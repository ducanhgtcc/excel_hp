package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.repository.repositorycustom.CameraRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long>, CameraRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_class_cam(id_class, id_cam) value(:idClass, :idCam)", nativeQuery = true)
    void insertCameraForClass(Long idClass, Long idCam);

    @Modifying
    @Query(value = "delete from ex_class_cam where id_class=:idClass", nativeQuery = true)
    void deleteCameraForClass(Long idClass);
    /**
     * tìm kếm camera của trường
     *
     * @param idSchool
     * @return
     */
    List<Camera> findByIdSchoolAndDelActiveTrueOrderByCamNameAsc(Long idSchool);

    /**
     * tìm kiếm theo id
     *
     * @param id
     * @return
     */
    Optional<Camera> findByIdAndDelActiveTrue(Long id);

    /**
     * tìm kiếm danh sách camera của một lớp
     *
     * @param idClass
     * @return
     */
    List<Camera> findByMaClassList_Id(Long idClass);

    List<Camera> findByIdSchoolAndDelActiveTrueAndMaClassList_Id(Long idSchool, Long idClass);

    List<Camera> findAllByIdSchoolAndDelActiveTrue(Long idSchool);
    List<Camera> findAllByIdSchoolAndCamActiveTrueAndDelActiveTrue(Long idSchool);
}
