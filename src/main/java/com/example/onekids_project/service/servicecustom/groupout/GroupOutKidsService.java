package com.example.onekids_project.service.servicecustom.groupout;

import com.example.onekids_project.request.groupout.GroupOutRequest;
import com.example.onekids_project.response.groupout.GroupOutNameResponse;
import com.example.onekids_project.response.groupout.ListGroupOutResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-07-12 2:24 PM
 *
 * @author nguyễn văn thụ
 */
public interface GroupOutKidsService {

    ListGroupOutResponse searchGroupOutKids(UserPrincipal principal);

    List<GroupOutNameResponse> findAllGroupName(UserPrincipal principal);

    boolean createGroupOutKids(UserPrincipal principal, GroupOutRequest request);

    boolean updateGroupOutKids(UserPrincipal principal, Long id, GroupOutRequest request);

    boolean deleteGroupOutKids(UserPrincipal principal, Long id);
}
