package com.example.onekids_project.request.smsNotify;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * date 2021-04-03 15:20
 *
 * @author Phạm Ngọc Thắng
 */
@Data
public class SmsNotifyDataRequest {
    private String sendTitle;
    @NotBlank
    private String sendContent;

    //thời gian hẹn gửi
    //    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd - HH:mm")
    private LocalDateTime dateTime;

    //hẹn giờ gửi hay không, true là có
    private boolean timer;
}
