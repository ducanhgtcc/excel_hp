package com.example.onekids_project.request.schedule;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateFileAndPictureRequest extends BaseRequest {

    private Long idSchool;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromFileTsime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toFileTsime;


    private List<MultipartFile> multipartFileList;

    private boolean Approved;

    private Long idClass;

    private String name;
    private String url;

    @Override
    public String toString() {
        return "CreateFileAndPictureRequest{" +
                "idSchool=" + idSchool +
                ", fromFileTsime=" + fromFileTsime +
                ", toFileTsime=" + toFileTsime +
                ", multipartFileList=" + multipartFileList +
                ", Approved=" + Approved +
                ", idClass=" + idClass +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                "} " + super.toString();
    }
}
