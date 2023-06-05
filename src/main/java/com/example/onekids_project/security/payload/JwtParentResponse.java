package com.example.onekids_project.security.payload;

import com.example.onekids_project.mobile.parent.response.schoolconfig.SchoolConfigParent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class JwtParentResponse {

    private LocalDate nowDate = LocalDate.now();

    private String quality;

    private String width;

    private SchoolConfigParent schoolConfig;

    private List<JwtDataObject> weekList;

    private List<JwtDataObject> monthList;

    public JwtParentResponse() {
        this.schoolConfig = new SchoolConfigParent();
    }
}
