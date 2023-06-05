package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lavanviet
 */
@Data
public class KidsTransferResponse extends IdResponse {
    private boolean inStatus;
    private boolean outStatus;
    @NotBlank
    private String fullName;
    private String phone;
    private String identify;
    private String relationship;
    private String note;

    //thông tin ảnh
    private String url;
}
