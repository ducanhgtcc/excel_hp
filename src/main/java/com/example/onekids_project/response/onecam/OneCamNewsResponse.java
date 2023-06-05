package com.example.onekids_project.response.onecam;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class OneCamNewsResponse extends IdResponse {
    private int oneCamNumber;

    private int oneKidsNumber;

    private int schoolNumber;

    private boolean extendLinkStatus;

    private String extendLink;
}
