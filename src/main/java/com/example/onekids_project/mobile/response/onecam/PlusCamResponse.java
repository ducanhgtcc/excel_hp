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
public class PlusCamResponse {
    private String fullName;

    private String schoolName;

    private String avatar;

    private String logo;

    private List<CamOneModel> camOneModelList;

    private List<NewsOneCamMobResponse> linkList;

}
