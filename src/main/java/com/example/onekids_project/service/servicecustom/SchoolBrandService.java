package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.brand.SearchBrandClickSchoolConfigRequest;
import com.example.onekids_project.request.brand.SearchSchoolBrandRequest;
import com.example.onekids_project.request.brand.UpdateSchoolBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandNewResponse;
import com.example.onekids_project.response.brandManagement.ListSchoolNewResponse;
import com.example.onekids_project.response.school.ListSchoolOtherResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface SchoolBrandService {

    ListSchoolResponse searchSchoolBrand(SearchSchoolBrandRequest searchSchoolBrandRequest);

    /**
     * udpate brand for school
     *
     * @param idSchool
     * @param updateSchoolBrandRequest
     * @return
     */
    SchoolResponse updateSchoolBrand(Long idSchool, UpdateSchoolBrandRequest updateSchoolBrandRequest);

    boolean deleteSchoolBrand(Long id);

    ListSchoolOtherResponse searchBrandClickSchool(SearchBrandClickSchoolConfigRequest searchBrandClickSchoolConfigRequest);

    List<BrandNewResponse> searchBrand(Long idSchool);

    ListSchoolNewResponse searchSchool(UserPrincipal principal);
}
