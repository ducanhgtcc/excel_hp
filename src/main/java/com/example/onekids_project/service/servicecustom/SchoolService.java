package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.master.request.SchoolSmsRequest;
import com.example.onekids_project.master.request.school.GroupTypeRequest;
import com.example.onekids_project.master.response.SmsSchoolResponse;
import com.example.onekids_project.master.response.school.ConfigNotifyResponse;
import com.example.onekids_project.master.response.school.icon.IconLockRequest;
import com.example.onekids_project.master.response.school.icon.IconLockResponse;
import com.example.onekids_project.request.school.CreateSchoolRequest;
import com.example.onekids_project.request.school.SearchSchoolRequest;
import com.example.onekids_project.request.school.UpdateForSchoolRequest;
import com.example.onekids_project.request.school.UpdateSchoolRequest;
import com.example.onekids_project.request.system.SchoolConfigSeachRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.response.school.SchoolUniqueResponse;
import com.example.onekids_project.response.schoolconfig.SchoolDataResponse;
import com.example.onekids_project.response.system.SchoolSystemConfigIconResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SchoolService {
    List<SchoolOtherResponse> findSchoolInAgent(Long idAgent);

    List<SchoolOtherResponse> getSchoolBriefService();

    List<SchoolUniqueResponse> findSchoolUnique();

    /**
     * tìm kiếm tất cả các trường
     *
     * @param pageable
     * @return
     */
    ListSchoolResponse findAllSchool(Pageable pageable);

    /**
     * tìm kiếm trường theo id
     *
     * @param id
     * @return
     */
    Optional<SchoolResponse> findByIdSchool(Long id);

    /**
     * tìm kiếm trường theo tùy chọn
     *
     * @param searchSchoolRequest
     * @return
     */
    ListSchoolResponse searchSchool(SearchSchoolRequest searchSchoolRequest);
    List<ExcelResponse> searchSchoolExport(List<Long> idList);

    /**
     * tạo mới trường
     *
     * @param createSchoolRequest
     * @return
     */
    boolean createSchool(CreateSchoolRequest createSchoolRequest) throws IOException;

    /**
     * cập nhật trường
     *
     * @param updateSchoolRequest
     * @return
     */
    SchoolResponse updateSchool(UpdateSchoolRequest updateSchoolRequest) throws IOException;

    /**
     * xóa trường
     *
     * @param id
     * @return
     */
    boolean deleteSchool(Long id);

    int saveSchoolSms(SchoolSmsRequest schoolSmsRequest);

    List<SmsSchoolResponse> findSchoolSmsByIdSchool(Long idSchool);

    /**
     * find all for update icon
     *
     * @param schoolConfigSeachRequest
     * @return
     */
    List<SchoolSystemConfigIconResponse> findAllSchoolConfigIcon(SchoolConfigSeachRequest schoolConfigSeachRequest);

    boolean updateMultiActiveSchool(List<Long> ids, Boolean activeOrUnActive);

    ConfigNotifyResponse getConfigNotify(UserPrincipal principal, Long idSchool);

    boolean updateConfigNotify(UserPrincipal principal, ConfigNotifyResponse configNotifyResponse);

    IconLockResponse getIconLockConfig(UserPrincipal principal, Long idSchool);

    boolean updateIconLockConfig(UserPrincipal principal, IconLockRequest request);

    SchoolDataResponse getSchoolData(UserPrincipal principal);

    boolean updateSchoolData(UserPrincipal principal, UpdateForSchoolRequest request) throws IOException;

    void updateGroupTypeService(GroupTypeRequest request);
}
