package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.repositorycustom.SchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, SchoolRepositoryCustom {

    /**
     * tìm kiếm trường theo id
     *
     * @param idSchool
     * @return
     */
    Optional<School> findByIdAndDelActive(Long idSchool, boolean delActive);

    /**
     * tìm kiếm trường theo id và chưa xóa
     *
     * @param idSchool
     * @return
     */
    Optional<School> findByIdAndDelActiveTrue(Long idSchool);
    List<School> findByIdInAndDelActiveTrue(List<Long> idList);


    /**
     * tìm kiếm tất cả các trường
     *
     * @param delActive
     * @return
     */
    List<School> findALlByDelActive(boolean delActive);

    Optional<School> findBySchoolCode(String schoolCode);

    /**
     * find all school
     * @return
     */
    List<School> findAllByDelActiveTrue();
    List<School> findAllBySchoolActiveTrueAndDelActiveTrue();
    List<School> findAllByGroupTypeAndSchoolActiveTrueAndDelActiveTrue(String groupType);

    /**
     * find school for one admin
     * @param idAdmin
     * @return
     */
    List<School> findByMaAdminList_Id(Long idAdmin);

    @Modifying
    @Transactional
    @Query("UPDATE School c SET  c.smsUsed= (c.smsUsed + ?1) WHERE c.id = ?2")
    void updateUsed(long used,long schoolId);

    List<School> findByAgentIdAndDelActiveTrueOrderBySchoolName(Long idAgent);


}

