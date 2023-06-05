package com.example.onekids_project.request.schedule;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class ApproveStatus {
    private Long idClass;
    private boolean isApprove;
    private LocalDate isMonday;
    private int typeApprove;
}
