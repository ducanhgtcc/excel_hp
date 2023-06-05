package com.example.onekids_project.response.kids;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.KidsGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class KidMainExportRespone {

    private Long idGrade;

    private Long idClass;

    private Long idGroup;

    private String kidStatus;

    private String kidCode;

    private String fullName;

    private LocalDate birthDay;

    private String gender;

    private String nickName;

    private String address;

    private String permanentAddress;

    private String ethnic;

    private LocalDate dateStart;

    private String note;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    private List<KidsGroup> kidsGroupList;

    private MaClass maClass;
}
