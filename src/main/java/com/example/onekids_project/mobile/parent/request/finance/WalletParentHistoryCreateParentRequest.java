package com.example.onekids_project.mobile.parent.request.finance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-03-17 16:23
 *
 * @author lavanviet
 */
@Data
public class WalletParentHistoryCreateParentRequest {
    @NotNull
    @Min(1)
    private Long money;

    //ko truyền thì lấy thời gian hệ thống
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    //ko có thì lấy thông tin mặt định hệ thống trong financeMobileConstant
    private String description;

    private Long idBank;

    private MultipartFile multipartFile;

}
