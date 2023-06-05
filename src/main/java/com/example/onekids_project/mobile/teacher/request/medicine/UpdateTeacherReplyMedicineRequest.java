package com.example.onekids_project.mobile.teacher.request.medicine;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherReplyMedicineRequest extends IdRequest {

    private String teacherReply;

    private boolean teacherReplyDel;
}
