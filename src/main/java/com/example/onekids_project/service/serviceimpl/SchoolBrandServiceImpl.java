package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AgentRepository;
import com.example.onekids_project.repository.BrandRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.brand.SearchBrandClickSchoolConfigRequest;
import com.example.onekids_project.request.brand.SearchSchoolBrandRequest;
import com.example.onekids_project.request.brand.UpdateSchoolBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandNewResponse;
import com.example.onekids_project.response.brandManagement.ListSchoolNewResponse;
import com.example.onekids_project.response.brandManagement.SchoolNewResponse;
import com.example.onekids_project.response.school.ListSchoolOtherResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.response.school.SchoolBrandOtherResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolBrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SchoolBrandServiceImpl implements SchoolBrandService {
//    @Autowired
//    private BrandRepository brandRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ListSchoolResponse searchSchoolBrand(SearchSchoolBrandRequest searchSchoolBrandRequest) {
        List<School> schoolList = schoolRepository.searchSchoolBrand(searchSchoolBrandRequest);
        if (CollectionUtils.isEmpty(schoolList)) {
            return null;
        }
        List<SchoolDTO> schoolDTOList = listMapper.mapList(schoolList, SchoolDTO.class);
        ListSchoolResponse listSchoolResponse = new ListSchoolResponse();

        listSchoolResponse.setSchoolList(schoolDTOList);
        return listSchoolResponse;
    }

    @Override
    public SchoolResponse updateSchoolBrand(Long idSchool, UpdateSchoolBrandRequest updateSchoolBrandRequest) {
        School school = schoolRepository.findByIdAndDelActive(idSchool, true).orElseThrow(() -> new NotFoundException("not found school by id"));
        Brand brand = brandRepository.findByIdAndDelActiveTrue(updateSchoolBrandRequest.getIdBrand()).orElseThrow();
        school.setBrand(brand);
        School newSchool = schoolRepository.save(school);
        SchoolResponse schoolResponse = modelMapper.map(newSchool, SchoolResponse.class);
        return schoolResponse;
    }

    @Override
    public boolean deleteSchoolBrand(Long id) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(id, true);
        if (schoolOptional.isEmpty()) {
            return false;
        }
        School deleteSchool = schoolOptional.get();
        deleteSchool.setDelActive(true);
        deleteSchool.setBrand(null);
        schoolRepository.save(deleteSchool);
        return true;
    }

    @Override
    public ListSchoolOtherResponse searchBrandClickSchool(SearchBrandClickSchoolConfigRequest searchBrandClickSchoolConfigRequest) {
        List<School> schoolList = schoolRepository.searchSchoolBrand1(searchBrandClickSchoolConfigRequest);
        if (CollectionUtils.isEmpty(schoolList)) {
            return null;
        }
        List<SchoolBrandOtherResponse> schoolBrandOtherResponseList = listMapper.mapList(schoolList, SchoolBrandOtherResponse.class);
        ListSchoolOtherResponse listSchoolOtherResponse = new ListSchoolOtherResponse();
        listSchoolOtherResponse.setSchoolBrandOtherResponses(schoolBrandOtherResponseList);
        return listSchoolOtherResponse;
    }

    @Override
    public List<BrandNewResponse> searchBrand(Long idSchool) {
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        Long idAgent = school.getAgent().getId();
        List<Long> brandList = agentRepository.findBrandByIdAgent(idAgent);
        List<BrandNewResponse> responseList = new ArrayList<>();
        brandList.forEach(x -> {
            Brand brand = brandRepository.findByIdAndDelActive(x, true).orElseThrow();
            BrandNewResponse model = new BrandNewResponse();
            model.setId(brand.getId());
            model.setBrandName(brand.getBrandName());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public ListSchoolNewResponse searchSchool(UserPrincipal principal) {
        List<School> schoolList = schoolRepository.findAllByDelActiveTrue();
        List<SchoolNewResponse> schoolNewResponseList = listMapper.mapList(schoolList, SchoolNewResponse.class);
        ListSchoolNewResponse listSchoolNewResponse = new ListSchoolNewResponse();
        listSchoolNewResponse.setResponseList(schoolNewResponseList);
        return listSchoolNewResponse;
    }
}





