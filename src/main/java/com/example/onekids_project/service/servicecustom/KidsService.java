package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.KidsBirthdayDTO;
import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.importexport.model.KidModel;
import com.example.onekids_project.importexport.model.KidModelImport;
import com.example.onekids_project.master.request.kids.KidsSearchAdminRequest;
import com.example.onekids_project.master.response.kids.ListStudentAdminResponse;
import com.example.onekids_project.mobile.parent.response.home.KidsInforResponse;
import com.example.onekids_project.request.birthdaymanagement.SearchKidsBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.request.common.StatusCommonRequest;
import com.example.onekids_project.request.createnotifyschool.CreateStudentNotify;
import com.example.onekids_project.request.kids.*;
import com.example.onekids_project.request.kids.transfer.KidsTransferCreateRequest;
import com.example.onekids_project.request.kids.transfer.KidsTransferUpdateRequest;
import com.example.onekids_project.request.kids.transfer.SearchKidsTransferRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyRequest;
import com.example.onekids_project.response.birthdaymanagement.KidBirthdayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListKidsBirthDayResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.finance.kidspackage.KidsInfoDataResponse;
import com.example.onekids_project.response.kids.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


public interface KidsService {
    /**
     * tìm kiếm tất cả học sinh
     *
     * @param idSchool
     * @param pageable
     * @return
     */
    ListKidsResponse findAllKids(Long idSchool, Pageable pageable);


    /**
     * tìm kiếm học sinh theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<KidsDTO> findByIdKid(Long idSchool, Long id);

    List<KidsInGroupResponse> findAllKidForGroup(UserPrincipal principal);

    /**
     * tìm kiếm học tinh theo tùy chọn
     *
     * @param principal
     * @param request
     * @return
     */
    ListStudentResponse searchKidsService(UserPrincipal principal, SearchKidsRequest request);

    ResponseEntity searchKidsTransferService(UserPrincipal principal, SearchKidsTransferRequest request);

    ResponseEntity searchKidsTransferByIdService(UserPrincipal principal, Long idKid);
    void kidsTransferCreateService(UserPrincipal principal, KidsTransferCreateRequest request) throws IOException;
    void kidsTransferUpdateService(UserPrincipal principal, KidsTransferUpdateRequest request) throws IOException;
    void kidsTransferInStatusByIdService(UserPrincipal principal, Long id, boolean status);
    void kidsTransferOutStatusByIdService(UserPrincipal principal, Long id, boolean status);
    void kidsTransferDeleteByIdService(UserPrincipal principal, Long id);
    void kidsTransferDeleteByIdKidListService(UserPrincipal principal, List<Long> idKidList);
    void resetKidsTransferInOutStatus(Long idKid, boolean inStatus, boolean outStatus);
    /**
     * tìm kiếm học sinh theo tùy chọn của ql ra trường
     *
     * @param principal
     * @param request
     * @return
     */
    ListStudentGroupOutResponse searchKidsGroupOut(UserPrincipal principal, SearchKidsGroupOutRequest request);

    ListStudentGroupOutResponse searchKidsGroupOutExcel(UserPrincipal principal, ExcelGroupOutRequest request);

    StudentGroupOutDetailResponse searchKidsGroupOutById(UserPrincipal principal, Long id);

    /**
     * tìm kiếm học tinh theo tùy chọn
     *
     * @param request
     * @return
     */
    ListStudentAdminResponse searchKidsAdmin(KidsSearchAdminRequest request);

    /**
     * thêm mới học sinh và phụ huynh
     *
     * @param principal
     * @param createStudentRequest
     * @return
     */
    boolean createKids(UserPrincipal principal, CreateKidsRequest createStudentRequest);

    /**
     * create avatar
     *
     * @param multipartFile
     * @return
     */
    void createAvatar(UserPrincipal principal, MultipartFile multipartFile) throws IOException;

    /**
     * xóa học sinh
     *
     * @param principal
     * @param id
     * @return
     */
    boolean deleteKids(UserPrincipal principal, Long id);

    boolean deleteKidsAdmin(Long id);

    boolean restoreKidsAdmin(Long id);

    /**
     * tìm kiếm thông tin chi tiết học sinh
     *
     * @param id
     * @return
     */
    KidResponse findIdKidExtra(Long id);

    /**
     * cập nhật thông tin mở rộng học sinh
     *
     * @param updateStudentRequest
     * @return
     */
    boolean updateKids(UserPrincipal principal, Long idUrl, UpdateKidsRequest updateStudentRequest);

    boolean updateKidExtraAdmin(UpdateKidsAdminRequest request);

    /**
     * cập nhật kích hoạt cho một học sinh
     *
     * @param idSchool
     * @param updateActivedOneKids
     * @return
     */
    boolean updateActiveOneKids(Long idSchool, ActivedOneKidsRequest updateActivedOneKids);

    /**
     * cập nhật kích hoạt nhiều học sinh
     *
     * @param kidsActionRequestList
     * @param active
     */
    void actionActiveManyKids(UserPrincipal principal, List<KidsActionRequest> kidsActionRequestList, boolean active);

    /**
     * cập nhật tính năng nhận sms
     *
     * @param activedOneKidsSMSRequest
     * @return
     */
    boolean updateActiveKidsSMS(ActivedOneKidsSMSRequest activedOneKidsSMSRequest);

    /**
     * cập nhật tính năng nhận sms cho nhiều học sinh
     *
     * @param kidsActionRequestList
     * @param activeSMS
     */
    void actionActiveManyKidsSMS(List<KidsActionRequest> kidsActionRequestList, boolean activeSMS);

    /**
     * chuyển lớp cho các học sinh
     *
     * @param principal
     * @param kidsChangeClassRequest
     */
    boolean actionChangeClassKids(UserPrincipal principal, KidsChangeClassRequest kidsChangeClassRequest) throws IOException;

    /**
     * xóa nhiều học sinh
     *
     * @param kidsActionRequestList
     */
    boolean deleteManyKid(UserPrincipal principal,List<KidsActionRequest> kidsActionRequestList);

    /**
     * tìm kiếm học sinh theo các fied
     *
     * @param searchKidsExportRequest
     * @return
     */
    List<KidsExportResponse> searchKidsByGradeClass(Long idSchool, SearchKidsExportRequest searchKidsExportRequest);

    /**
     * Chuyển đổi đối tượng KidsExportResponse thành KidVM để đổ dữ liệu lên excel
     *
     * @param exportResponseList
     * @return
     */


    List<KidModel> getFileAllKidByGrade(List<KidsExportResponse> exportResponseList, String nameSchool);

    List<ExcelNewResponse> getFileAllKidByGradeNew(SearchKidsExportRequest searchKidsExportRequest, UserPrincipal principal);

    KidBirthdayResponse updateApprove(Long idSchoolLogin, UserPrincipal principal, UpdateReiceiversRequest updateReiceiversEditRequest);

    Optional<KidsBirthdayDTO> findByIdKidb(UserPrincipal principal, Long idSchoolLogin, Long id);

    /**
     * tạo thông báo cho học sinh
     *
     * @param principal
     * @param createStudentNotify
     * @return
     */

    boolean createStudentNotify(UserPrincipal principal, CreateStudentNotify createStudentNotify) throws IOException, FirebaseMessagingException;

    boolean createStudentNotifySms(UserPrincipal principal, SmsNotifyRequest smsNotifyRequest) throws ExecutionException, InterruptedException;

    boolean createStudentSmsService(UserPrincipal  principal, SmsStudentRequest request) throws ExecutionException, InterruptedException;

    /**
     * tạo tài khoản cho các chỗ khác gọi đến
     *
     * @param fullName
     * @param phone
     * @param kids
     * @return
     */
    boolean createAccountAndParentForOther(String fullName, String phone, Kids kids);

    List<KidModelImport> convertDataKids(List<KidModelImport> kidModels, UserPrincipal principal);

    boolean sendAccountStudentSms(UserPrincipal principal, List<Long> idStudents) throws ExecutionException, InterruptedException;

    List<KidsSmsResponse> seachKidsByClass(UserPrincipal principal, List<Long> idClassList);

    List<KidsSmsResponse> seachKidsByGrade(UserPrincipal principal, List<Long> idGradeList);

    ListKidsBirthDayResponse searchKidsBirthDayNew(UserPrincipal principal, SearchKidsBirthDayRequest request);

    boolean mergeKidIntoParent(MergeKidsRequest request);

    KidsInforResponse findKidsInfor(UserPrincipal principal);

    List<KidsInfoDataResponse> findKidsByName(UserPrincipal principal, String fullName);

    boolean updateKidsStatus(UserPrincipal principal, StatusCommonRequest request);

    int updateKidsGroupOut(UserPrincipal principal, KidsGroupOutRequest request);

    boolean restoreKidsGroupOut(UserPrincipal principal, Long id, Long idClass) throws IOException;


}
