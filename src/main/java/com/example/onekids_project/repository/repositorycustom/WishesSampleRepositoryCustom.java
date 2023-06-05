package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.sample.WishesSample;
import com.example.onekids_project.request.birthdaymanagement.SearchWishSampleRequest;

import java.util.List;

public interface WishesSampleRepositoryCustom {
    List<WishesSample> findAllWishSample(Long idSchool, Long idSystem);

    List<WishesSample> searchWishesSampleKid(Long idSchoolLogin, SearchWishSampleRequest searchWishSampleRequest);

    List<WishesSample> searchWishesSampleTeacher(Long idSchoolLogin);
}
