package com.example.onekids_project.controller.brand;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.brand.SearchBrandClickSchoolConfigRequest;
import com.example.onekids_project.request.brand.SearchSchoolBrandRequest;
import com.example.onekids_project.request.brand.UpdateSchoolBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandNewResponse;
import com.example.onekids_project.response.brandManagement.ListSchoolNewResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.school.ListSchoolOtherResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolBrandService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/schoolbrand")
public class SchoolBrandController {
    private static final Logger logger = LoggerFactory.getLogger(SchoolBrandController.class);
    @Autowired
    SchoolBrandService schoolBrandService;

    @Autowired
    SchoolService schoolService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity search(@CurrentUser UserPrincipal principal, SearchSchoolBrandRequest searchSchoolBrandRequest) {
        ListSchoolResponse listSchoolResponse = schoolBrandService.searchSchoolBrand(searchSchoolBrandRequest);
        //set an agent for a school
        listSchoolResponse.getSchoolList().forEach(school -> {
            school.setBrand(school.getBrand());
        });
        return NewDataResponse.setDataSearch(listSchoolResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchBrand")
    public ResponseEntity searchBrand(SearchBrandClickSchoolConfigRequest searchBrandClickSchoolConfigRequest) {
        ListSchoolOtherResponse listSchoolResponse = schoolBrandService.searchBrandClickSchool(searchBrandClickSchoolConfigRequest);
        return NewDataResponse.setDataSearch(listSchoolResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Optional<SchoolResponse> schoolResponse = schoolService.findByIdSchool(id);
        return NewDataResponse.setDataSearch(schoolResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchAgent/{idSchool}")
    public ResponseEntity searchMessageTeacher(@CurrentUser UserPrincipal principal, @PathVariable Long idSchool) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), idSchool);
        List<BrandNewResponse> response = schoolBrandService.searchBrand(idSchool);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchSchool")
    public ResponseEntity searchSchool(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        ListSchoolNewResponse response = schoolBrandService.searchSchool(principal);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @Valid @RequestBody UpdateSchoolBrandRequest updateSchoolBrandRequest) {
        if (!updateSchoolBrandRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSchoolBrandRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        SchoolResponse schoolResponse = schoolBrandService.updateSchoolBrand(id, updateSchoolBrandRequest);
        return NewDataResponse.setDataUpdate(schoolResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppSend(@PathVariable(name = "id") Long id) {
        boolean checkDelete = schoolBrandService.deleteSchoolBrand(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

}
