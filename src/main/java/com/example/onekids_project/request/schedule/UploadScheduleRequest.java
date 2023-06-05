package com.example.onekids_project.request.schedule;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UploadScheduleRequest extends BaseRequest {

    private Long idSchool;

    private List<String> weekClassMenu;

    private MultipartFile multipartFile;

    private boolean isApproved;

    private List<Long> listIdClass;

    private String name;
    private String url;


}
