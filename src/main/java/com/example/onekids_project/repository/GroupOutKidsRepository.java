package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.GroupOutKids;
import com.example.onekids_project.repository.repositorycustom.GroupOutKidsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-07-12 10:39 AM
 *
 * @author nguyễn văn thụ
 */
public interface GroupOutKidsRepository extends JpaRepository<GroupOutKids, Long>, GroupOutKidsRepositoryCustom {

    List<GroupOutKids> findAllByIdSchoolAndDelActiveTrue(Long idSchool);
    Optional<GroupOutKids> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);
}
