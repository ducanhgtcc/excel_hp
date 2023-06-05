package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Component
public class SchoolUtils {
    private static SchoolRepository schoolRepository;

    @Autowired
    public SchoolUtils(SchoolRepository schoolRepository) {
        SchoolUtils.schoolRepository = schoolRepository;
    }

    public static SchoolRepository getBeanSchoolRepository() {
        return schoolRepository;
    }

    public static String getSchoolName(Long id) {
        School school = getBeanSchoolRepository().findByIdAndDelActiveTrue(id).orElseThrow(() -> new NoSuchElementException("not found school by id"));
        return school.getSchoolName();
    }

    public static Long getIdSchool() {
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        boolean check = StringUtils.equalsAny(principal.getAppType(), AppTypeConstant.SUPPER_SCHOOL, AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT);
        if (check) {
            return principal.getIdSchoolLogin();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.SCHOOL_NOT_FOUND);
        }
    }
}
