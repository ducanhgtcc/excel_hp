package com.example.onekids_project.controller.brand;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.BrandDTO;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.brand.CreateBrandRequest;
import com.example.onekids_project.request.brand.SearchBrandConfigRequest;
import com.example.onekids_project.request.brand.UpdateBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandResponse;
import com.example.onekids_project.response.brandManagement.ListBrandResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.BrandService;
import com.example.onekids_project.service.servicecustom.SchoolBrandService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController


@RequestMapping("/web/brandconfig")
public class
BrandController {
    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);
    @Autowired
    BrandService brandService;

    @Autowired
    SchoolBrandService schoolBrandService;

    @Autowired
    SchoolService schoolService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll( BaseRequest baseRequest) {
        ListBrandResponse listBrandResponse = brandService.findAllBrand();
        return NewDataResponse.setDataSearch(listBrandResponse);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity search(SearchBrandConfigRequest searchBrandConfigRequest) {
        ListBrandResponse listBrandResponse = brandService.searchBrandConfig(searchBrandConfigRequest);
        listBrandResponse.getBrandResponses().forEach(school -> {
            school.setAgent(school.getAgent());
        });
        return NewDataResponse.setDataSearch(listBrandResponse);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create( @Valid @RequestBody CreateBrandRequest createBrandRequest) {
        BrandResponse brandResponse = brandService.createBrand(createBrandRequest);
        return NewDataResponse.setDataCreate(brandResponse);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Optional<BrandDTO> brandDTOOptional = brandService.findByIdBrand(id);
        return NewDataResponse.setDataSearch(brandDTOOptional);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppSend(@PathVariable(name = "id") Long id) {
        boolean checkDelete = brandService.deleteBrand(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id,  @Valid @RequestBody UpdateBrandRequest updateBrandRequest) {
        if (!updateBrandRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateBrandRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        BrandResponse brandResponse = brandService.updateBrand(id, updateBrandRequest);
        return NewDataResponse.setDataUpdate(brandResponse);
    }

}
