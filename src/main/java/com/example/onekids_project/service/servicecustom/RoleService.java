package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.schoolconfig.RoleCreateConfigRequest;
import com.example.onekids_project.request.schoolconfig.RoleUpdateConfigRequest;
import com.example.onekids_project.request.user.AppTypeRequest;
import com.example.onekids_project.request.user.SearchRoleRequest;
import com.example.onekids_project.response.schoolconfig.RoleConfigResponse;
import com.example.onekids_project.response.user.RoleForUserResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface RoleService {
//    void createRoleDefaultForSchool(Long idSchool);

    /**
     * tìm kiếm tất cả các quyền
     *
     * @param principal
     * @return
     */
    List<RoleConfigResponse> searchRole(UserPrincipal principal, SearchRoleRequest request);

    RoleConfigResponse findRoleForUser(UserPrincipal principal, Long id);

    /**
     * tạo nhóm quyền
     *
     * @param principal
     * @param request
     * @return
     */
    boolean createRole(UserPrincipal principal, RoleCreateConfigRequest request);

    /**
     * cập nhật nhóm quyền
     *
     * @param principal
     * @param request
     * @return
     */
    boolean updateRole(UserPrincipal principal, RoleUpdateConfigRequest request);

    /**
     * xóa 1 role
     *
     * @param principal
     * @param id
     * @return
     */
    boolean deleteRoleOne(UserPrincipal principal, Long id);

    /**
     * file roleList for User
     *
     * @param principal
     * @param idUser
     * @return
     */
    List<RoleForUserResponse> findRoleOfUser(UserPrincipal principal, Long idUser, AppTypeRequest request);

    /**
     * update role for user
     *
     * @param idUser
     * @param longList
     * @return
     */
    boolean updateRoleOfUser(UserPrincipal principal, Long idUser, List<Long> longList);

    /**
     * tạo các vai trò mặc định khi tạo trường
     * @param school
     */
    void createRoleForSchool(School school);

    void addApiForRoleDefault(School school);


}
