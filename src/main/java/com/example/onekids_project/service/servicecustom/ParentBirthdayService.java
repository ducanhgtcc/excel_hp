package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchWishSampleRequest;
import com.example.onekids_project.response.birthdaymanagement.ListParentBirthDayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListParentResponse;
import com.example.onekids_project.response.birthdaymanagement.ListWishesSampleResponse;
import com.example.onekids_project.response.birthdaymanagement.ParentsBirthdayResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParentBirthdayService {

    ListParentResponse findAllParentBirthday(Long idSchoolLogin, PageNumberWebRequest request);

    ListWishesSampleResponse searchBirthdayKid1(UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest);

    ListWishesSampleResponse searchBirthdayParent(UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest);

    ListWishesSampleResponse searchBirthdayTeacher(UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest);

    List<ParentsBirthdayResponse> searchParentBirthDayNew(UserPrincipal principal, SearchParentBirthDayRequest searchParentBirthDayRequest);

    ListParentBirthDayResponse searchParentBirthDayNewa(UserPrincipal principal, SearchParentBirthDayRequest request);
}
