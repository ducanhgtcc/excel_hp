package com.example.onekids_project.response.cashbook;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * date 2021-03-09 16:31
 *
 * @author lavanviet
 */
@Getter
@Setter
public class CashBookHistoryResponse extends IdResponse {
    private String category;

    private String type;

    private double money;

    private LocalDate date;

    private LocalDateTime createdDate;

    //mã hóa đơn học sinh, hóa đơn nhân sự, hóa đơn phiếu thu chi
    private String code;

    private String origin;
}
