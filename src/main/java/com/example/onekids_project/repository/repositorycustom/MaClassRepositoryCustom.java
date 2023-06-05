package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.request.classes.ClassSearchCommonRequest;
import com.example.onekids_project.request.classes.ClassSearchNewRequest;
import com.example.onekids_project.request.classes.SearchMaClassRequest;
import com.example.onekids_project.request.schoolconfig.ClassConfigSearchRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaClassRepositoryCustom {
    /**
     * lấy danh sách lớp trong khối
     *
     * @param idGrade
     * @return
     */
    List<MaClass> findClassInGrade(Long idSchool, Long idGrade);

    List<MaClass> findClassCommon(Long idSchool);
    List<MaClass> findClassTeacher(Long idSchool, List<Long> idClassList);

    List<MaClass> findClassInGradeWithDate(Long idSchool, Long idGrade, LocalDate date);

    List<MaClass> findClassByInfoEmployee(Long idInfo, Long idSchool);

    /**
     * tìm kiếm lớp học chung cho nhiều chức năng theo idGrade, idClass và className
     *
     * @param idSchool
     * @param classSearchCommonRequest
     * @return
     */
    List<MaClass> searchClassCommon(Long idSchool, ClassSearchCommonRequest classSearchCommonRequest);

    List<MaClass> searchClassNew(Long idSchool, ClassSearchNewRequest request);

    long countSearchClassNew(Long idSchool, ClassSearchNewRequest request);

    /**
     * tìm kiếm tất cả các lớp học
     *
     * @param idSchool
     * @param pageable
     * @return
     */
    List<MaClass> findAllMaClass(Long idSchool, Pageable pageable);

    /**
     * tìm kiếm lớp học theo id
     *
     * @param idSchool
     * @param id
     */
    Optional<MaClass> findByIdMaClass(Long idSchool, Long id);


    /**
     * tìm kiếm các lớp theo tùy chọn
     *
     * @param idSchool
     * @param pageable
     * @param searchMaClassRequest
     * @return
     */
    List<MaClass> searchMaClass(Long idSchool, Pageable pageable, SearchMaClassRequest searchMaClassRequest);

    /**
     * Tìm kiếm các lớp theo khối
     *
     * @param idSchool
     * @return
     */
    List<MaClass> searchMaClassByIdGrade(Long idSchool, Long idGrade, Long id);

    /**
     * tìm kiếm cấu hình cho các lớp
     *
     * @param idSchool
     * @param classConfigSearchRequest
     * @return
     */
    List<MaClass> searchMaClassConfig(Long idSchool, ClassConfigSearchRequest classConfigSearchRequest);

    /**
     * tìm kiếm media setting
     *
     * @param idSchool
     * @param mediaSettingSearchRequest
     * @return
     */
    List<MaClass> searchMaclassForMediaSetting(Long idSchool, MediaSettingSearchRequest mediaSettingSearchRequest);

    List<MaClass> findAllMaClassAlbum(Long idSchool);

    List<MaClass> findAllKidsFromClassOld(Long idSchool, Long idClass, Long idAlbum);

    List<MaClass> findAllMaClassTeacherAlbum(Long idClass);

    MaClass searchByIdClass(Long idClass);

    List<MaClass> findClassBySchool(Long idSchool, Long idGrade);

    List<MaClass> searchClassWithName(Long idSchool, String className);

    List<MaClass> searchMaClassWithSchool(Long idSchool, Long idClass);
}
