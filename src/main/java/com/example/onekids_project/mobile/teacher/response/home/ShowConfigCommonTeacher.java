package com.example.onekids_project.mobile.teacher.response.home;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ShowConfigCommonTeacher {

    private boolean showAttendanceArriveTeacherPlus;

    private boolean showAttendanceLeaveTeacherPlus;

    private boolean showAttendanceEatTeacherPlus;

    private boolean showEvaluateLearnTeacherPlus;

    private boolean showEvaluateEatTeacherPlus;

    private boolean showEvaluateSleepTeacherPlus;

    private boolean showEvaluateSanitaryTeacherPlus;

    private boolean showEvaluateHealthTeacherPlus;

    private boolean showEvaluateCommonTeacherPlus;
}
