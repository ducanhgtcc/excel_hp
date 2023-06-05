package com.example.onekids_project.mobile.response.wallet;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-22 10:01
 *
 * @author lavanviet
 */
@Getter
@Setter
public class WalletHistoryPlusResponse extends IdResponse {
    private String date;

    private String category;

    private long money;

    //phụ huynh chưa xác nhận
    private boolean parentUnConfirm;

    //nhà trường chưa xác nhận
    private boolean schoolUnConfirm;

    private String description;

    private String picture;
}
