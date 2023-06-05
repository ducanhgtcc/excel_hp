package com.example.onekids_project.controller;

import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.teacher.ClassTeacherResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * date 2021-06-15 13:51
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/teacher")
public class TeacherController {

    @Autowired
    private MaClassService maClassService;

    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity searchNewClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataTeacherNoClass(principal);
        List<ClassTeacherResponse> responseList = maClassService.findClassTeacher(principal);
        return NewDataResponse.setDataSearch(responseList);
    }
}
