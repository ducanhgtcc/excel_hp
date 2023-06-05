package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.Role;
import com.example.onekids_project.request.user.SearchRoleRequest;

import java.util.List;

public interface RoleRepositoryCustom {
    List<Role> searchRole(Long idSchool, SearchRoleRequest request);

}
