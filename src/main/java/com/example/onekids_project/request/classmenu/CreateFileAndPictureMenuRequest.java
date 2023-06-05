package com.example.onekids_project.request.classmenu;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateFileAndPictureMenuRequest {
    private Long idSchool;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromFileTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toFileTime;

    private List<MultipartFile> multipartFileList;

    private boolean Approved;

    private Long idClass;

    private String name;
    private String url;

}

