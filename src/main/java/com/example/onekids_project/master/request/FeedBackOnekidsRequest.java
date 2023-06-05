package com.example.onekids_project.master.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedBackOnekidsRequest {
    @NotBlank
    private String feedbackTitle;

    @NotBlank
    private String feedbackContent;

    @NotNull
    private Long idSchool;

    private List<MultipartFile> multipartFileList;
}
