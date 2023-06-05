package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.SupplierDTO;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.SupplierRepository;
import com.example.onekids_project.request.brand.CreateSupplierRequest;
import com.example.onekids_project.request.brand.SearchSmsConfigRequest;
import com.example.onekids_project.request.brand.UpdateSupplierRequest;
import com.example.onekids_project.response.brandManagement.CreatesupplierResponse;
import com.example.onekids_project.response.brandManagement.ListSmsLinkConfigResponse;
import com.example.onekids_project.response.brandManagement.ListSupplierResponse;
import com.example.onekids_project.response.brandManagement.SupplierResponse;
import com.example.onekids_project.service.servicecustom.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;


@Service
public class BrandManagementServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListSmsLinkConfigResponse searchSmsLinkConfig(SearchSmsConfigRequest searchSmsConfigRequest) {
        List<Supplier> supplierList = supplierRepository.searchSmsConfig(searchSmsConfigRequest);
        if (CollectionUtils.isEmpty(supplierList)) {
            return null;
        }
        List<CreatesupplierResponse> smsLinkConfigResponseList = listMapper.mapList(supplierList, CreatesupplierResponse.class);

        ListSmsLinkConfigResponse listSmsLinkConfigResponse = new ListSmsLinkConfigResponse();
        listSmsLinkConfigResponse.setSmsLinkConfigResponses(smsLinkConfigResponseList);
        return listSmsLinkConfigResponse;
    }

    @Override
    public Optional<SupplierDTO> findByIdSupplier(Long id) {
        Optional<Supplier> optionalSupplier = supplierRepository.findByIdSupplier(id);
        if (optionalSupplier.isEmpty()) {
            return Optional.empty();
        }
        Optional<SupplierDTO> optionalSupplierDTO = Optional.ofNullable(modelMapper.map(optionalSupplier.get(), SupplierDTO.class));
        return optionalSupplierDTO;
    }

//    @Override
//    public SupplierResponse createSupplier(UserPrincipal principal, CreateSupplierRequest createSupplierRequest) {
//        Supplier supplier = modelMapper.map(createSupplierRequest, Supplier.class);
//        Supplier supplierSave = supplierRepository.save(supplier);
//        SupplierResponse supplierResponse = modelMapper.map(supplierSave, SupplierResponse.class);
//        return supplierResponse;
//    }

    @Override
    public boolean deleteSupplier(Long id) {
        Optional<Supplier> supplierOptional = supplierRepository.findByIdAndDelActive(id, true);
        if (supplierOptional.isEmpty()) {
            return false;
        }
        Supplier deleteSupplier = supplierOptional.get();
        long countNumber = deleteSupplier.getBrandList().stream().filter(BaseEntity::isDelActive).count();
        if (countNumber > 0) {
            return false;
        }
        deleteSupplier.setDelActive(false);
        supplierRepository.save(deleteSupplier);
        return true;
    }

    @Override
    public SupplierResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest) {
        Optional<Supplier> supplierOptional = supplierRepository.findByIdSupplier(updateSupplierRequest.getId());
        if (supplierOptional.isEmpty()) {
            return null;
        }
        Supplier oldSupplier = supplierOptional.get();
        modelMapper.map(updateSupplierRequest, oldSupplier);
        Supplier newSupplier = supplierRepository.save(oldSupplier);
        SupplierResponse supplierResponse = modelMapper.map(newSupplier, SupplierResponse.class);
        return supplierResponse;
    }


    @Override
    public ListSupplierResponse findAllsupplie() {
        List<Supplier> supplierList = supplierRepository.findAllsupplier();
        if (CollectionUtils.isEmpty(supplierList)) {
            return null;
        }
        List<SupplierResponse> supplierResponseList = listMapper.mapList(supplierList, SupplierResponse.class);
        ListSupplierResponse listSupplierResponse = new ListSupplierResponse();
        listSupplierResponse.setSupplierResponses(supplierResponseList);
        return listSupplierResponse;
    }

    @Override
    public SupplierResponse createSupplier1(CreateSupplierRequest createSupplierRequest) {
        Supplier supplier = modelMapper.map(createSupplierRequest, Supplier.class);
        Supplier supplierSave = supplierRepository.save(supplier);
        SupplierResponse supplierResponse = modelMapper.map(supplierSave, SupplierResponse.class);
        return supplierResponse;
    }


}





