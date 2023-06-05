package com.example.onekids_project.controller.brand;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.SupplierDTO;
import com.example.onekids_project.request.brand.CreateSupplierRequest;
import com.example.onekids_project.request.brand.SearchSmsConfigRequest;
import com.example.onekids_project.request.brand.UpdateSupplierRequest;
import com.example.onekids_project.response.brandManagement.ListSmsLinkConfigResponse;
import com.example.onekids_project.response.brandManagement.ListSupplierResponse;
import com.example.onekids_project.response.brandManagement.SupplierResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.service.servicecustom.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController


@RequestMapping("/web/brand")
public class SupplierController {
    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);
    @Autowired
    SupplierService supplierService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll() {
        ListSupplierResponse listSupplierResponse = supplierService.findAllsupplie();
        return NewDataResponse.setDataSearch(listSupplierResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity search(SearchSmsConfigRequest searchSmsConfigRequest) {
        ListSmsLinkConfigResponse listSmsLinkConfigResponse = supplierService.searchSmsLinkConfig(searchSmsConfigRequest);
        return NewDataResponse.setDataSearch(listSmsLinkConfigResponse);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById( @PathVariable("id") Long id) {
        Optional<SupplierDTO> supplierDTOOptional = supplierService.findByIdSupplier(id);
        return NewDataResponse.setDataSearch(supplierDTOOptional);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create( @Valid @RequestBody CreateSupplierRequest createSupplierRequest) {
        SupplierResponse supplierResponse = supplierService.createSupplier1(createSupplierRequest);
        return NewDataResponse.setDataCreate(supplierResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppsend(@PathVariable(name = "id") Long id) {
        boolean checkDelete = supplierService.deleteSupplier(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @Valid @RequestBody UpdateSupplierRequest updateSupplierRequest) {
        //  kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateSupplierRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSupplierRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        SupplierResponse supplierResponse = supplierService.updateSupplier(updateSupplierRequest);
        return NewDataResponse.setDataUpdate(supplierResponse);
    }

}
