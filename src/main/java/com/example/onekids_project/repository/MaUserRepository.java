package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.repositorycustom.MaUserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaUserRepository extends JpaRepository<MaUser, Long>, MaUserRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_user_role(id_user, id_role) value(:idUser, :idRole)", nativeQuery = true)
    void insertUserRole(Long idUser, Long idRole);

    @Modifying
    @Query(value = "delete from ex_user_role where id_user=:idUser and id_role in(:idRoleList)", nativeQuery = true)
    void deleteUserRole(Long idUser, List<Long> idRoleList);

    /**
     * check exist username and apppe
     *
     * @param username
     * @param appType
     * @return
     */
    Boolean existsByUsernameAndAppTypeAndDelActiveTrue(String username, String appType);

    /**
     * check exist username
     *
     * @param username
     * @return
     */
    boolean existsByUsernameAndDelActiveTrue(String username);

    /**
     * tìm kiếm tài khoản người dùng theo id
     *
     * @param id
     * @return
     */
    Optional<MaUser> findByIdAndDelActiveTrue(Long id);

    Optional<MaUser> findByIdAndDelActiveFalse(Long id);

    /**
     * tìm tài khoản chưa bị xóa, còn kích hoạt theo id
     *
     * @param id
     * @return
     */
    Optional<MaUser> findByIdAndDelActiveTrueAndActivatedTrue(Long id);

    /**
     * find username
     *
     * @param username
     * @return
     */
    Optional<MaUser> findByUsernameAndActivatedTrueAndDelActiveTrue(String username);

    Optional<MaUser> findByUsernameAndDelActiveTrue(String username);

    List<MaUser> findByAppTypeAndDelActiveTrueOrderByFullName(String appType);

    /**
     * get role of user school
     *
     * @param idSchool
     * @param appType
     * @return
     */
    List<MaUser> findByRoleList_IdSchoolAndAppTypeAndDelActiveTrue(Long idSchool, String appType);

    Optional<MaUser> findByUsername(String username);

    List<MaUser> findByDelActiveTrue();

    List<MaUser> findByStartDateDeleteIsNotNullAndDelActiveTrue();

}
