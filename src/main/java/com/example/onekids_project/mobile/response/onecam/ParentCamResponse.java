package com.example.onekids_project.mobile.response.onecam;

import com.example.onekids_project.mobile.response.NewsOneCamMobResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ParentCamResponse {
    private Long id;

    private String schoolName;

    private String avatar;

    private String fullName;

    private String className;

    private String logo;

    private boolean viewLimitStatus;

    private int viewLimitNumber;

    private String viewLimitText;

    private boolean noViewCameraStatus;

    private String noViewCameraText;

    private List<CamOneModel> camOneModelList;

    private List<NewsOneCamMobResponse> linkList;

}
