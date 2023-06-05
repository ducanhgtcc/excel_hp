package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.entity.sample.WishesSample;
import com.example.onekids_project.repository.repositorycustom.WishesSampleRepositoryCustom;
import com.example.onekids_project.request.birthdaymanagement.SearchWishSampleRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishesSampleRepositoryImpl extends BaseRepositoryimpl<WishesSample> implements WishesSampleRepositoryCustom {
    @Override
    public List<WishesSample> findAllWishSample(Long idSchool, Long idSystem) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idSystem != null) {
            queryStr.append("or id_school=:idSystem ");
            mapParams.put("idSystem", idSystem);
        }
        queryStr.append("order by id_school desc, id desc");
        List<WishesSample> wishesSampleList = findAllNoPaging(queryStr.toString(), mapParams);
        return wishesSampleList;
    }

    @Override
    public List<WishesSample> searchWishesSampleKid(Long idSchool, SearchWishSampleRequest searchWishSampleRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

//        if (searchWishSampleRequest != null) {
//            if (idSchool != null) {
//                queryStr.append("and id_school=:idSchool ");
//                mapParams.put("idSchool", idSchool);
//            }
//        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<WishesSample> searchWishesSampleTeacher(Long idSchoolLogin) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

            if (idSchoolLogin != null) {
                queryStr.append("and id_school=:idSchool ");
                mapParams.put("idSchool", idSchoolLogin);
            }
        queryStr.append("and receiver_type=:type ");
        mapParams.put("type", SampleConstant.KIDS);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
