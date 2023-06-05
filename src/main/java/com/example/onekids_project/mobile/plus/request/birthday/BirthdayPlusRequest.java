package com.example.onekids_project.mobile.plus.request.birthday;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BirthdayPlusRequest {

    @NotBlank
    private String sendContent;

    private String urlPicture;

    @NotNull
    private List<Long> idKidList;

}
