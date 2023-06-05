package com.example.onekids_project.security.service.servicecustom;

import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.master.response.ProfileMobileResponse;
import com.example.onekids_project.mobile.request.ProfileMobielRequest;
import com.example.onekids_project.request.user.AppTypeRequest;
import com.example.onekids_project.response.mauser.*;
import com.example.onekids_project.request.mauser.*;
import com.example.onekids_project.response.user.UserRoleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MaUserService {

    /**
     * kiểm tra thông tin đăng nhập của người dùng
     * @param maUser
     */
    void checkUserInMaUser(MaUser maUser);

    boolean checkUserInSchool(MaUser maUser);
    /**
     * cập nhật mật khẩu
     *
     * @param principal
     * @param changePasswordRequest
     * @return
     */
    boolean updatePassword(UserPrincipal principal, ChangePasswordRequest changePasswordRequest);

    void updatePasswordMany(List<MaUser> maUserList, String newPassword);

    /**
     * create user cho TH gọi api đăng ký
     * @param signUpRequest
     * @return
     */
//    MaUserResponse createAccount(SignUpRequest signUpRequest);

    /**
     * create role default for user
     * @param idUser
     * @param idSchool
     */
//    void createRoleDefault(Long idUser, Long idSchool, String appType);

    /**
     * createAccount cho các TH không gọi từ api đăng ký
     * @param accountCreateData
     * @return
     */
    MaUser createAccountOther(AccountCreateData accountCreateData);

    /**
     * create user for supper admin
     * @param adminDataRequest
     * @return
     */
    MaUser createAccountSupperAdmin(AdminDataRequest adminDataRequest);


    /**
     * active account
     * @param maUserActiveRequest
     * @return
     */
    boolean checkActiveUser(MaUserActiveRequest maUserActiveRequest);

    /**
     * active account
     * @param maUserActiveRequestList
     * @return
     */
    boolean checkActiveManyUser(List<MaUserActiveRequest> maUserActiveRequestList);

    /**
     * check username and apptype exist
     * @param username
     * @param appType
     */
    void checkExistUsernameAndAppType(String username, String appType);

    /**
     * find by user of school
     * @param principal
     * @param request
     * @return
     */
    List<UserRoleResponse> findMaUserRoleSchool(UserPrincipal principal, AppTypeRequest request);

    /**
     * lấy thông tin trang cá nhân
     * @param principal
     * @return
     */
    ProfileResponse findProfile(UserPrincipal principal);

    ProfileMobileResponse findProfileMobile(UserPrincipal principal);

    boolean updateProfile(UserPrincipal principal, ProfileRequest profileRequest) throws IOException;

    boolean updateProfileMobile(UserPrincipal principal, ProfileMobielRequest request);

    boolean updateAvatarMobile(UserPrincipal principal, MultipartFile multipartFile) throws IOException;

    ListAccountResponse findAccount(SearchAccountRequest request);

    boolean updateAccount(AccountRequest request);

    boolean deleteAccount(Long id);

    /**
     *  cập nhật lại username tài khoản cũ bằng cách thêm số vào đăng sau
     *  chuyển del_active=false
     * @param id
     * @param typeDelete
     */
    void deleteCompleteAccount(Long id, String typeDelete);

    boolean restoreAccount(Long id);

    void checkExistUsername(String username);

    void checkUsernameAndAppType(String usernameNoExtend, String appType);

    void checkUsername(String username, String appType);

    void checkAccountHandle(UsernameRequest request);

    boolean createAccountWithNewPhone(HandleNewPhoneRequest request);

    boolean verifycation(UserPrincipal principal, VerificationAccountRequest request);

    boolean mergeAccount(MergeAccountRequest request);

    List<AccountEmployeeHandleResponse> getAccountEmployeeDuplicate(UserPrincipal principal, AccountInforRequest request);

    List<AccountKidsHandleResponse> getAccountKidsDuplicate(UserPrincipal principal, String phone);

    MaUserParentResponse getMauserByUsername(String username, String appType);

}
