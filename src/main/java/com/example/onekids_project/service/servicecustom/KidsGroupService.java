package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.studentgroup.CreateKidsGroupRequest;
import com.example.onekids_project.request.studentgroup.SearchStudentGroupRequest;
import com.example.onekids_project.request.studentgroup.TransferKidsGroupRequest;
import com.example.onekids_project.request.studentgroup.UpdateKidsGroupRequest;
import com.example.onekids_project.response.studentgroup.CreateUpdateKidsGroupResponse;
import com.example.onekids_project.response.studentgroup.KidsGroupResponse;
import com.example.onekids_project.response.studentgroup.ListKidsGroupResponse;
import com.example.onekids_project.response.studentgroup.ListKidsStudentGroupResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface KidsGroupService {

    /**
     * tìm kiếm tất cả nhóm học sinh
     *
     * @param idSchool
     * @param pageable
     * @return
     */
    ListKidsGroupResponse findAllKidsGroup(Long idSchool, PageNumberWebRequest request);

    /**
     * tìm kiếm nhóm học sinh theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<KidsGroupResponse> findByIdKidsGroup(Long idSchool, Long id);

    /**
     * tạo nhóm học sinh
     *
     * @param idSchool
     * @param createKidsGroupRequest
     * @return
     */
    CreateUpdateKidsGroupResponse createKidsGroup(Long idSchool, CreateKidsGroupRequest createKidsGroupRequest);

    /**
     * cập nhật nhóm học sinh
     *
     * @param idSchool
     * @param updateKidsGroupRequest
     * @return
     */
    CreateUpdateKidsGroupResponse updateKidsGroup(Long idSchool, UpdateKidsGroupRequest updateKidsGroupRequest);

    /**
     * xóa nhóm học sinh
     *
     * @param idSchool
     * @param id
     * @return
     */
    boolean deleteKidsGroup(Long idSchool, Long id);

    /**
     * chuyển đổi các học sinh trong nhóm
     *
     * @param idSchool
     * @param transferKidsGroupRequest
     */
    boolean insertTransferKidsGroup(Long idSchool, TransferKidsGroupRequest transferKidsGroupRequest);


    ListKidsStudentGroupResponse searchKids(UserPrincipal principal, SearchStudentGroupRequest request);
}
