package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.BrandDTO;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AgentRepository;
import com.example.onekids_project.repository.BrandRepository;
import com.example.onekids_project.repository.SupplierRepository;
import com.example.onekids_project.request.brand.CreateBrandRequest;
import com.example.onekids_project.request.brand.SearchBrandConfigRequest;
import com.example.onekids_project.request.brand.UpdateBrandRequest;
import com.example.onekids_project.response.brandManagement.BrandResponse;
import com.example.onekids_project.response.brandManagement.ListBrandResponse;
import com.example.onekids_project.service.servicecustom.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;


@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListBrandResponse searchBrandConfig(SearchBrandConfigRequest searchBrandConfigRequest) {
        List<Brand> brandList = brandRepository.searchBrandconfig(searchBrandConfigRequest);
        if (CollectionUtils.isEmpty(brandList)) {
            return null;
        }
        List<BrandResponse> brandResponseList = listMapper.mapList(brandList, BrandResponse.class);
        brandResponseList.forEach(x -> {
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
    public Optional<BrandDTO> findByIdBrand(Long id) {
        Optional<Brand> optionalBrand = brandRepository.findByIdBrand(id);
        if (optionalBrand.isEmpty()) {
            return Optional.empty();
        }
        Optional<BrandDTO> optionalBrandDTO = Optional.ofNullable(modelMapper.map(optionalBrand.get(), BrandDTO.class));
        return optionalBrandDTO;
    }

    @Override
    public boolean deleteBrand(Long id) {
        Optional<Brand> brandOptional = brandRepository.findByIdAndDelActive(id, true);
        if (brandOptional.isEmpty()) {
            return false;
        }
        Brand deleteBrand = brandOptional.get();
        long countNumber = deleteBrand.getSchoolList().stream().filter(x -> x.isDelActive()).count();
        if (countNumber > 0) {
            return false;
        }
//        deleteBrand.setDelActive(false);
        brandRepository.deleteById(id);
//        brandRepository.save(deleteBrand);
        return true;
    }


    @Override
    public BrandResponse updateBrand(Long id, UpdateBrandRequest updateBrandRequest) {
        Optional<Brand> brandOptional = brandRepository.findByIdBrand(updateBrandRequest.getId());
        if (brandOptional.isEmpty()) {
            return null;
        }
        Brand oldBrand = brandOptional.get();
        modelMapper.map(updateBrandRequest, oldBrand);
        Brand newBrand = brandRepository.save(oldBrand);
        BrandResponse brandResponse = modelMapper.map(newBrand, BrandResponse.class);
        return brandResponse;
    }

    @Override
    public ListBrandResponse findAllBrand() {
        List<Brand> brandList = brandRepository.findAllBrand();
        if (CollectionUtils.isEmpty(brandList)) {
            return null;
        }
        List<BrandResponse> brandResponseList = listMapper.mapList(brandList, BrandResponse.class);
        ListBrandResponse listBrandResponse = new ListBrandResponse();
        listBrandResponse.setBrandResponses(brandResponseList);
        return listBrandResponse;
    }

    @Override
    public BrandResponse createBrand(CreateBrandRequest createBrandRequest) {
        Brand newBrand = modelMapper.map(createBrandRequest, Brand.class);
        Supplier supplier = supplierRepository.findByIdAndDelActive(createBrandRequest.getIdSupplier(), AppConstant.APP_TRUE).orElseThrow(() -> new RuntimeException("Fail! -> Không tìm thấy supplier có id=" + createBrandRequest.getIdSupplier()));
        newBrand.setSupplier(supplier);
        Brand brandSave = brandRepository.save(newBrand);
        BrandResponse brandResponse = modelMapper.map(brandSave, BrandResponse.class);
        return brandResponse;
    }

}





