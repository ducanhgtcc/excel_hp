package com.example.onekids_project.request.kids.transfer;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * @author lavanviet
 */
@Data
public class KidsTransferUpdateRequest extends IdRequest {
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
