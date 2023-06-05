package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-04 09:40
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ListOrderKidsDetailResponse extends IdResponse {
    private String code;

    private String description;

    private List<KidsPackageForPaymentDetailResponse> dataList;

}
