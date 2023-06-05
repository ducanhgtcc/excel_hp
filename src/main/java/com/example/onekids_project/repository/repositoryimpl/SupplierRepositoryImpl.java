package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.repository.repositorycustom.SupplierRepositoryCustom;
import com.example.onekids_project.request.brand.SearchSmsConfigRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SupplierRepositoryImpl extends BaseRepositoryimpl<Supplier> implements SupplierRepositoryCustom {

    @Override
    public List<Supplier> searchSmsConfig(SearchSmsConfigRequest searchSmsConfigRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchSmsConfigRequest != null) {
            if (StringUtils.isNotBlank(searchSmsConfigRequest.getName())) {
                queryStr.append("and supplier_name like :supplierName ");
                mapParams.put("supplierName", "%" + searchSmsConfigRequest.getName() + "%");
            }
        }
        queryStr.append("order by supplier_name ASC ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<Supplier> findByIdSupplier( Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Supplier> supplierList = findAllNoPaging(queryStr.toString(), mapParams);
        if (supplierList.size() > 0) {
            return Optional.ofNullable(supplierList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Supplier> findAllsupplier() {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        return findAllNoPaging(queryStr.toString(), mapParams);
    }




}
