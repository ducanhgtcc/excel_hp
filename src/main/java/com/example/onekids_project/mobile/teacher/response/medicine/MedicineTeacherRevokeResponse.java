package com.example.onekids_project.mobile.teacher.response.medicine;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicineTeacherRevokeResponse {

    private List<ReplyMobileDateObject> replyList;
}
