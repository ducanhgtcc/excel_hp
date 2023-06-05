package com.example.onekids_project.response.finance.wallet;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-25 15:43
 *
 * @author lavanviet
 */
@Getter
@Setter
public class WalletParentHistoryResponse extends IdResponse {
    private String category;

    private String type;

    private double money;

    private String name;

    private LocalDate date;

    private boolean confirm;

    private String description;

    private String picture;

    private BankCustom1 fnBank;
}

@Getter
@Setter
class BankCustom1 {
    private String fullName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String bankName;
}
