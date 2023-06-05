package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.request.brand.SearchBrandClickSchoolConfigRequest;
import com.example.onekids_project.request.brand.SearchSchoolBrandRequest;
import com.example.onekids_project.request.school.SearchSchoolRequest;
import com.example.onekids_project.request.system.SchoolConfigSeachRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchoolRepositoryCustom {

    /**
     * tìm kiếm tất cả các trường
     * @param pageable
     * @return
     */
    List<School> findAllSchool(Pageable pageable);

    /**
     * tìm kiếm trường theo tùy chọn
     * @param searchSchoolRequest
     * @return
     */
    List<School> searchSchool(SearchSchoolRequest searchSchoolRequest);

    long countsearchSchool(SearchSchoolRequest searchSchoolRequest);

    /**
     * find school for system config icon
     * @param schoolConfigSeachRequest
     * @return
     */
    List<School> searchSchoolForConfigIcon(SchoolConfigSeachRequest schoolConfigSeachRequest);


    List<School> searchSchoolBrand(SearchSchoolBrandRequest searchSchoolBrandRequest);

    List<School> searchSchoolBrand1(SearchBrandClickSchoolConfigRequest searchBrandClickSchoolConfigRequest);

    List<School> getSchoolExpired(int number);
    List<School> getSchoolExpiredHandle();

}
