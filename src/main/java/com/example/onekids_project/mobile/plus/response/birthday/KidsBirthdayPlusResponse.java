package com.example.onekids_project.mobile.plus.response.birthday;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsBirthdayPlusResponse extends IdResponse {

    private String nameKid;

    private String birthDay;

    private String avatar;

    boolean status;

}
