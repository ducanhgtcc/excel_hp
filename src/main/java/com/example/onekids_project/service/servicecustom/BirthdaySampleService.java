package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.schoolconfig.BirthdaySampleActiveRequest;
import com.example.onekids_project.request.system.BirthdaySampleUpdateRequest;
import com.example.onekids_project.response.schoolconfig.BirthdaySampleResponse;

import java.io.IOException;
import java.util.List;

public interface BirthdaySampleService {

    /**
     * create birthday sample for system
     */
    void createBirthSampleForSystem();

    /**
     * create birthday sample for idschool
     */
    void createBirthSampleForSchool(School school);

    /**
     * tìm kiếm mẫu lời chúc sinh nhật
     *
     * @param idSchool
     * @return
     */
    List<BirthdaySampleResponse> findAllBirthdaySample(Long idSchool);

    /**
     * cập nhật các loại kích hoạt
     *
     * @param birthdaySampleActiveRequest
     * @return
     */
    BirthdaySampleResponse updateBirthdaySampleActive(BirthdaySampleActiveRequest birthdaySampleActiveRequest);

    /**
     * update birthday sample
     * @param birthdaySampleUpdateRequest
     * @return
     */
//    BirthdaySampleResponse updateBirthdaySample(BirthdaySampleUpdateRequest birthdaySampleUpdateRequest) throws IOException;

    /**
     * update birthday sample
     *
     * @param birthdaySampleUpdateRequest
     * @return
     */
    BirthdaySampleResponse updateBirthdaySample(Long idSchool, BirthdaySampleUpdateRequest birthdaySampleUpdateRequest) throws IOException;
}
