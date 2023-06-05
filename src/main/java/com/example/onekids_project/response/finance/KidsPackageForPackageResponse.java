package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsPackageForPackageResponse extends IdResponse {
    private int month;

    private int year;

    private boolean locked;

    private boolean approved;

    private double paid;

    private String kidName;

    private String className;

    private Long idClass;
}
