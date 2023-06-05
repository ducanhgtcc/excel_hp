package com.example.onekids_project.controller.brand;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.BrandDTO;
import com.example.onekids_project.request.brand.CreateAgentBrandRequest;
import com.example.onekids_project.request.brand.SearchAgentBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandResponse;
import com.example.onekids_project.response.brandManagement.ListBrandResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AgentBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/web/agentbrand")
public class AgentBrandController {
    private static final Logger logger = LoggerFactory.getLogger(AgentBrandController.class);
    @Autowired
    AgentBrandService agentBrandService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity search(@CurrentUser UserPrincipal principal, SearchAgentBrandRequest searchAgentBrandRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListBrandResponse listBrandResponse = agentBrandService.searchAgentBrand(idSchoolLogin, searchAgentBrandRequest);
        return NewDataResponse.setDataSearch(listBrandResponse);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateAgentBrandRequest createAgentBrandRequest) {
        BrandResponse brandAddResponse = agentBrandService.createAgentBrand(principal.getIdSchoolLogin(), createAgentBrandRequest);
        return NewDataResponse.setDataCreate(brandAddResponse);
    }

    //
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<BrandDTO> brandDTOOptional = agentBrandService.findByIdBrand(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(brandDTOOptional);
    }


}
