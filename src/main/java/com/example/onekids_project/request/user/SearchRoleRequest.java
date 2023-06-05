package com.example.onekids_project.request.user;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

/**
 * date 2021-04-22 10:17
 *
 * @author lavanviet
 */
@Data
public class SearchRoleRequest {
    private String roleName;

    @StringInList(values = {AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT})
    private String type;
}
