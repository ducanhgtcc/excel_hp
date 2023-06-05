package com.example.onekids_project.mobile.teacher.response.qualitykid;

import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.mobile.parent.response.kids.KidsHeightWeightParentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KidsExtraQualityResponse {
    private String name;

    private String nameClass;

    private String avatar;

//    private String nickName;

    private String bloodType;

    private String swim;

    // dị ứng
    private String allery;

    // awn kieeng
    private String diet;

    //tai
    private String ear;
    // mui
    private String nose;
    // hong
    private String throat;

    private String eyes;

    private String skin;

    private String heart;

    private String fat;

    private List<KidsHeightWeightTeacherResponse> dataList;


}

