package com.example.onekids_project.request.kids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MultipartFileRequest {
    private MultipartFile multipartFile;
}
