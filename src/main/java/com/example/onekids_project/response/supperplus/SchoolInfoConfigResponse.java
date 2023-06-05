package com.example.onekids_project.response.supperplus;

import lombok.Data;

/**
 * @author lavanviet
 */
@Data
public class SchoolInfoConfigResponse {

    private boolean showAttendanceDateParent;

    private boolean showAttendanceMonthParent;

    private boolean showAttendanceEatParent;

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
