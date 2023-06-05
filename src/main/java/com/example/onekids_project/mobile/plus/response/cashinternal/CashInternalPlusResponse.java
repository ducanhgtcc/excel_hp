package com.example.onekids_project.mobile.plus.response.cashinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-06-18 13:41
 *
 * @author lavanviet
 */
@Getter
@Setter
public class CashInternalPlusResponse extends IdResponse {
    private String code;

    private double money;

    private LocalDate date;

    private String content;

    private boolean payment;

    private boolean approved;
}
