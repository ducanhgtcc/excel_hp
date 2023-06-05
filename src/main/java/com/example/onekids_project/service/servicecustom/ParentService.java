package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.request.kids.UpdateParentInforRequest;
import com.example.onekids_project.response.kids.ParentInforResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ParentService {

    /**
     * tìm kiếm phụ huynh theo id
     *
     * @param id
     * @return
     */
    Optional<ParentInforResponse> findByIdParent(Long id);

    /**
     * cập nhật thông tin phụ huynh
     *
     * @param updateParentInforRequest
     * @return
     */
    ParentInforResponse updateParent(UpdateParentInforRequest updateParentInforRequest);

    boolean saveAvatar(UserPrincipal principal, MultipartFile multipartFile) throws IOException;

    void createWalletParent(Parent parent, Long idSchool);
}
