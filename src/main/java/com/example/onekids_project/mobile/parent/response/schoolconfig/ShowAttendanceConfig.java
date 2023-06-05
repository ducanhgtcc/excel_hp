package com.example.onekids_project.mobile.parent.response.schoolconfig;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ShowAttendanceConfig {
    private boolean showAttendanceDateParent;

    private boolean showAttendanceMonthParent;

    private boolean showAttendanceEatParent;
}
