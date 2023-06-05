package com.example.onekids_project.response.teacher;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-15 13:53
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ClassTeacherResponse extends IdResponse {
    private String className;

    private boolean checked;

}
