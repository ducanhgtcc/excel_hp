package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.BrandDTO;
import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AgentBrandRepository;
import com.example.onekids_project.repository.AgentRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.repository.SupplierRepository;
import com.example.onekids_project.request.brand.CreateAgentBrandRequest;
import com.example.onekids_project.request.brand.SearchAgentBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandResponse;
import com.example.onekids_project.response.brandManagement.ListBrandResponse;
import com.example.onekids_project.service.servicecustom.AgentBrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AgentBrandServiceImpl implements AgentBrandService {

    @Autowired
    private AgentBrandRepository agentBrandRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private AgentRepository agentRepository;


    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListBrandResponse searchAgentBrand(Long idSchoolLogin, SearchAgentBrandRequest searchAgentBrandRequest) {
        List<Brand> brandList = agentBrandRepository.searchAgentBrand(idSchoolLogin, searchAgentBrandRequest);
        if (CollectionUtils.isEmpty(brandList)) {
            return null;
        }
        List<BrandResponse> brandResponseList = listMapper.mapList(brandList, BrandResponse.class);
        brandResponseList.forEach(x -> {
            if (x.getIdSchool() != null) {
                Optional<School> schoolOptional = schoolRepository.findById(x.getIdSchool());
                if (schoolOptional.isPresent()) {
                    x.setSchoolNameShow(schoolOptional.get().getSchoolName());
                }
            }
            if (x.getIdAgent() != null) {
                Optional<Agent> agentOptional = agentRepository.findByIdAndDelActive(idSchoolLogin, true);
                if (agentOptional.isPresent()) {
                    x.setAgentNameshow(agentOptional.get().getAgentName());
                }
            }
            x.setSupplierNameShow(x.getSupplier().getSupplierName());
            if (x.isBrandTypeAds() == true && x.isBrandTypeCskh() == true) {
                x.setBrandType("CSKH , Quảng cáo");
            } else if (x.isBrandTypeCskh() == true && x.isBrandTypeAds() == false) {
                x.setBrandType("CSKH");
            } else if (x.isBrandTypeCskh() == false && x.isBrandTypeAds() == true) {
                x.setBrandType("Quảng cáo");
            }

        });
        ListBrandResponse listBrandResponse = new ListBrandResponse();
        listBrandResponse.setBrandResponses(brandResponseList);
        return listBrandResponse;
    }

    @Override
    public Optional<BrandDTO> findByIdBrand(Long idSchoolLogin, Long id) {
        Optional<Brand> optionalBrand = agentBrandRepository.findByIdBrand(idSchoolLogin, id);
        if (optionalBrand.isEmpty()) {
            return Optional.empty();
        }
        Optional<BrandDTO> optionalBrandDTO = Optional.ofNullable(modelMapper.map(optionalBrand.get(), BrandDTO.class));
        return optionalBrandDTO;
    }

    @Override
    public BrandResponse createAgentBrand(Long idSchool, CreateAgentBrandRequest createAgentBrandRequest) {
        Brand newBrand = modelMapper.map(createAgentBrandRequest, Brand.class);
        School school = schoolRepository.findById(idSchool).orElseThrow(() -> new RuntimeException("Fail! -> Không tìm thấy school có id=" + idSchool));
//        newBrand.setSchool(school);
        Brand brandSave = agentBrandRepository.save(newBrand);
        BrandResponse brandResponse = modelMapper.map(brandSave, BrandResponse.class);
        return brandResponse;
    }


}