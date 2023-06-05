package com.example.onekids_project.request.notifyschool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * date 2021-10-21 9:19 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotifySchoolRequest{

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String link;

    private List<MultipartFile> multipartFileList;

    private List<Long> fileDeleteList;
}
