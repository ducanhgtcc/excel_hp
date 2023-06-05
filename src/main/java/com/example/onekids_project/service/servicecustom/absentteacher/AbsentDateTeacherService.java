package com.example.onekids_project.service.servicecustom.absentteacher;

import com.example.onekids_project.response.absentteacher.AbsentDateTeacherResponse;

import java.util.List;

/**
 * date 2021-05-24 3:03 PM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentDateTeacherService {

    List<AbsentDateTeacherResponse> findAllByAbsentTeacherId(Long id);
}
