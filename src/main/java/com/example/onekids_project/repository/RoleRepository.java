package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.Role;
import com.example.onekids_project.repository.repositorycustom.RoleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_role_api(id_role, id_api) value(:idRole, :idApi)", nativeQuery = true)
    void insertRoleAPi(Long idRole, Long idApi);

    @Modifying
    @Query(value = "delete from ex_role_api where id_role=:idRole", nativeQuery = true)
    void deleteRoleAPi(Long idRole);

    /**
     * find id root role of school
     * @param idSchool
     * @return
     */
    Optional<Role> findByIdSchoolAndDelActiveTrue(Long idSchool);
    Optional<Role> findByIdSchoolAndDelActiveTrueAndRoleName(Long idSchool, String name);

    List<Role> findByIdSchoolAndTypeAndDelActiveTrue(Long idSchool, String type);

    Optional<Role> findByRoleName(String roleName);

    /**
     * tìm kiếm tất cả các role theo trường
     * @param idSchool
     * @return
     */
    List<Role> findByIdSchoolAndDelActiveTrueOrderByRoleNameAsc(Long idSchool);

    /**
     * tìm kiếm theo id
     * @param idSchool
     * @param id
     * @return
     */
    Optional<Role> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * tìm kiếm danh sách rolle của một user
     * @param idSchool
     * @param idUser
     * @return
     */
    List<Role> findByIdSchoolAndMaUsersListIdAndDelActiveTrue(Long idSchool, Long idUser);

    Optional<Role> findByIdSchoolAndRoleNameAndDelActiveTrue(Long idSchool, String roleName);

}
