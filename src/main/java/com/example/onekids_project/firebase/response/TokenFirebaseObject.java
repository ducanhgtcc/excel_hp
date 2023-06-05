package com.example.onekids_project.firebase.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenFirebaseObject extends IdResponse {
    private String fullName;

    private String tokenFirebase;
}
