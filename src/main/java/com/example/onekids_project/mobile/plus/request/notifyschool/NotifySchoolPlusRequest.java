package com.example.onekids_project.mobile.plus.request.notifyschool;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * date 2021-10-22 11:29 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class NotifySchoolPlusRequest {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String link;

    private List<MultipartFile> multipartFileList;

    private List<Long> fileDeleteList;

}
