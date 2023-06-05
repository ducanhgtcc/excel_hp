package com.example.onekids_project.mobile.teacher.request.identifykid;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IdentityKidRequest {

    @NotNull
    private Long idKid;

    private MultipartFile newFileJson;

    private List<MultipartFile> multipartFileImageList;

    private List<IdentityModel> delIdentityModelList;


}
