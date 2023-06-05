package com.example.onekids_project.master.response;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MaUserOtherRequest extends IdRequest {
    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String passwordShow;

}
