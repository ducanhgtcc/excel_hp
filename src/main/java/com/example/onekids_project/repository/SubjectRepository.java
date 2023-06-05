package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.Subject;
import com.example.onekids_project.repository.repositorycustom.SubjectRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long>, SubjectRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_class_subject(id_class, id_subject) value(:idClass, :idSubject)", nativeQuery = true)
    void insertSubjectForClass(Long idClass, Long idSubject);

    @Modifying
    @Query(value = "delete from ex_class_subject where id_class=:idClass", nativeQuery = true)
    void deleteSubjectForClass(Long idClass);

    Optional<Subject> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * tìm kiếm môn học theo id
     *
     * @param id
     * @return
     */
    Optional<Subject> findByIdAndDelActiveTrue(Long id);

    List<Subject> findByMaClassListIdAndMaClassListDelActiveTrueAndDelActiveTrueAndIdSchool(Long idClass, Long idSchool);

//    List<Subject> findByDelActiveTrue();

    /**
     * tìm kiếm môn học theo trường và sắp xếp theo subjectName
     *
     * @param idSchool
     * @return
     */
    List<Subject> findByIdSchoolAndDelActiveTrueOrderBySubjectNameAsc(Long idSchool);

    /**
     * tìm kiếm danh sách các môn học trong một lớp
     *
     * @param idClass
     * @return
     */
    List<Subject> findByMaClassList_Id(Long idClass);


}
