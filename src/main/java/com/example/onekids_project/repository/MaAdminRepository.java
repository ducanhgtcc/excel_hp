package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.repository.repositorycustom.MaAdminRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MaAdminRepository extends CrudRepository<MaAdmin, Long>, MaAdminRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_admin_school(id_admin, id_school) value(:idAdmin, :idSchool)", nativeQuery = true)
    void insertSchoolForAdmin(Long idAdmin, Long idSchool);

    @Modifying
    @Query(value = "delete from ex_admin_school where id_admin=:idAdmin", nativeQuery = true)
    void deleteSchoolForAdmin(Long idAdmin);
    /**
     * find all admin
     * @return
     */
    List<MaAdmin> findByDelActiveTrue();

    /**
     * find by id
     * @param id
     * @return
     */
    Optional<MaAdmin> findByIdAndDelActiveTrue(Long id);
}
