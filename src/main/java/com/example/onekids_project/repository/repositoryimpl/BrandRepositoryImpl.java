package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.repository.repositorycustom.BrandRepositoryCustom;
import com.example.onekids_project.request.brand.SearchBrandConfigRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrandRepositoryImpl extends BaseRepositoryimpl<Brand> implements BrandRepositoryCustom {


    @Override
    public List<Brand> searchBrandconfig(SearchBrandConfigRequest searchBrandConfigRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchBrandConfigRequest != null) {

            if (StringUtils.isNotBlank(searchBrandConfigRequest.getName())) {
                queryStr.append("and brand_name like :brandName ");
                mapParams.put("brandName", "%" + searchBrandConfigRequest.getName() + "%");
            }
            queryStr.append("order by brand_name ASC ");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<Brand> findByIdBrand( Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Brand> brandList = findAllNoPaging(queryStr.toString(), mapParams);
        if (brandList.size() > 0) {
            return Optional.ofNullable(brandList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Brand> findAllBrand() {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
//        if (idSchool != null) {
//            queryStr.append("and id_school=:idSchool ");
//            mapParams.put("idSchool", idSchool);
//        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
