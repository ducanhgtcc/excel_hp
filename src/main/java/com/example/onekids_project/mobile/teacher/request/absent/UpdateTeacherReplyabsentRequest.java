package com.example.onekids_project.mobile.teacher.request.absent;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateTeacherReplyabsentRequest extends IdRequest {

    @NotBlank
    private String teacherReply;

}
