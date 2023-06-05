package com.example.onekids_project.response.parentweb;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ChangeKidsResponse extends IdResponse {

    private String fullName;

    private String gender;

    private String birthDay;

    private boolean status;

    private MaClassOtherResponse maClass;

}
