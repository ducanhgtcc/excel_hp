package com.example.onekids_project.mobile.plus.response.KidsReco;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KidsRecoRespone {

    private String className;

    private int numberStudent;

    private Long idClass;

    //tên giáo viên chủ nhiệm
    private List<String> masterNameList;

}
