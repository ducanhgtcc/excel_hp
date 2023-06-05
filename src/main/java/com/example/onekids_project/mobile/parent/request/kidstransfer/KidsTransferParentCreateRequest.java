package com.example.onekids_project.mobile.parent.request.kidstransfer;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lavanviet
 */
@Data
public class KidsTransferParentCreateRequest {
    private boolean inStatus;
    private boolean outStatus;
    @NotBlank
    private String fullName;
    private String phone;
    private String identify;
    private String relationship;
    private String note;

    private MultipartFile multipartFile;
}
