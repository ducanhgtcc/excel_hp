package com.example.onekids_project.mobile.plus.response.fees;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-07 14:49
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsFeesResponse extends IdResponse {
    private String fullName;

    private String className;

    private String avatar;

    private boolean orderShow;

    private Long idOrder;
}
