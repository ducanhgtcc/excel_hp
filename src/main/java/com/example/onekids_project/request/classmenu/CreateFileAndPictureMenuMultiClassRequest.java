package com.example.onekids_project.request.classmenu;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class CreateFileAndPictureMenuMultiClassRequest {
    @NotEmpty
    private List<String> weekClassMenu;
    @NotEmpty
    private List<MultipartFile> multipartFile;
    @NotEmpty
    private List<Long> listIdClass;

}
