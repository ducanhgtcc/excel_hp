package com.example.onekids_project.mobile.response.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class WalletHistoryDetailResponse extends IdResponse {
    private String description;

    private long money;

    private String date;

    private String picture;

    private boolean parentUnConfirm;

    private boolean schoolUnConfirm;

}
