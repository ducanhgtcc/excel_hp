package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.dto.MaUserDTO;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.security.payload.LoginMobileRequest;
import com.example.onekids_project.security.payload.LoginRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaUserRepositoryCustom{
    /**
     * find all user login
     * @param maUser
     * @param pageable
     * @return
     */
    List<MaUser> findAllMaUser(MaUserDTO maUser, Pageable pageable);

    /**
     * find user long by username
     * @param username
     * @return
     */
    List<MaUser> getMaUsersUsername(String username);

    /**
     * find user login by properties
     * @param loginRequest
     * @return
     */
    List<MaUser> findMaUsersLogin(LoginRequest loginRequest);

    /**
     * find user login by properties
     * @param loginMobileRequest
     * @return
     */
    List<MaUser> findMaUsersLoginMobile(LoginMobileRequest loginMobileRequest);


    List<MaUser> searchTeacherBirthday(Long idSchoolLogin, SearchTeacherBirthDayRequest searchTeacherBirthDayRequest);

    List<MaUser> searchParentBirthday(Long idSchool, SearchParentBirthDayRequest searchParentBirthDayRequest);

    List<MaUser> findAccount(SearchAccountRequest request);

    Long countTotalAccount(SearchAccountRequest request);

    List<MaUser> findAccountHasExtendUsername(String username);

    List<MaUser> getUserExpired(int number);

    List<MaUser> getUserExpiredHandle();


}
