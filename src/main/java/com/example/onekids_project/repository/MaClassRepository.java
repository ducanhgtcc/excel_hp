package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.repository.repositorycustom.MaClassRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaClassRepository extends JpaRepository<MaClass, Long>, MaClassRepositoryCustom {

    List<MaClass> findByIdSchoolAndDelActiveTrue(Long idSchool);

    Optional<MaClass> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * tìm kiếm lớp theo id
     *
     * @param id
     * @return
     */
    Optional<MaClass> findByIdAndDelActiveTrue(Long id);

    Optional<MaClass> findByClassNameAndDelActiveTrueAndIdSchool(String className, Long id);

    @Query(value = "select id from ma_class where del_active=1 and id_school=:idSchool ", nativeQuery = true)
    List<Long> findIdClassInSchool(Long idSchool);
}
