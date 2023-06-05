package com.example.onekids_project.request.classmenu;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateFileExcelRequest {

    private Long idSchool;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate fromFileTime;
//
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate toFileTime;

    private List<String> weekClassMenu;

    private MultipartFile multipartFile;

    private boolean isApproved;

    private List<Long> listIdClass;

    private String name;
    private String url;
}
