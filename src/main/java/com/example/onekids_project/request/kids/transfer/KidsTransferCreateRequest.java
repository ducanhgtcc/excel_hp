package com.example.onekids_project.request.kids.transfer;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lavanviet
 */
@Data
public class KidsTransferCreateRequest {
    @NotNull
    private Long idKid;
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
