package com.example.onekids_project.request.finance.wallet;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-02-25 08:58
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class WalletParentHistoryCreateRequest {

    @NotNull
    private Long idKid;

    @NotNull
    private Long idWalletParent;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT})
    private String category;

    @Min(1)
    private double money;

    @NotBlank
    private String name;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    private String description;

    private Long idBank;

    private MultipartFile multipartFile;

}
