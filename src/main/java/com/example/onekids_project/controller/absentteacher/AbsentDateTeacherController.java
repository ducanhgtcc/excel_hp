package com.example.onekids_project.controller.absentteacher;

import com.example.onekids_project.response.absentteacher.AbsentDateTeacherResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.absentteacher.AbsentDateTeacherService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * date 2021-05-24 2:59 PM
 *
 * @author nguyễn văn thụ
 */
@RestController

@RequestMapping("/web/absent-date-teacher")
public class AbsentDateTeacherController {

    @Autowired
    private AbsentDateTeacherService absentDateTeacherService;

    /**
     * Hiển thị All absent-date-teacher theo id absent-teacher
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable Long id){
        RequestUtils.getFirstRequest(principal, id);
        //thực hiện tìm kiếm theo id
        List<AbsentDateTeacherResponse> listDate = absentDateTeacherService.findAllByAbsentTeacherId(id);
        return NewDataResponse.setDataSearch(listDate);
    }
}
