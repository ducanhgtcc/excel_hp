package com.example.onekids_project.security.payload;

import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.entity.user.Role;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;

    private String tokenType = "Bearer";

    private SchoolInforPayload schoolInfor;

    private String avatar = AvatarDefaultConstant.AVATAR_SYSTEM;

    private String currentUser;

    private String appType;

    private Set<String> apiSet;
}
