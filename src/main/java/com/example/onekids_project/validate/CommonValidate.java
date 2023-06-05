package com.example.onekids_project.validate;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * validate những cái chung của trường, lớp
 */
@Component
public class CommonValidate {

    public static void checkDataParent(UserPrincipal principal) {
        checkAppTypeParent(principal);
        checkIdSchool(principal);
        checkIdKid(principal);
    }

    public static void checkDataTeacher(UserPrincipal principal) {
        checkAppTypeTeacher(principal);
        checkIdSchool(principal);
        checkIdClass(principal);
    }

    public static void checkDataTeacherNoClass(UserPrincipal principal) {
        checkAppTypeTeacher(principal);
        checkIdSchool(principal);
    }

    public static void checkDataPlus(UserPrincipal principal) {
        checkAppTypePlus(principal);
        checkIdSchool(principal);
    }

    public static void checkDataSupperPlus(UserPrincipal principal) {
        checkAppTypeSupperPlus(principal);
        checkIdSchool(principal);
    }

    public static void checkPlusOrTeacher(UserPrincipal principal) {
        checkIdSchool(principal);
        checkAppTypePlusOrTeacher(principal);
    }

    public static void checkDataAdmin(UserPrincipal principal) {
        String appType = principal.getAppType();
        if (appType.equals(AppTypeConstant.PARENT) || appType.equals(AppTypeConstant.TEACHER) || appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.SUPPER_SCHOOL)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPTYPE_INVALID);
        }
    }

    public static void checkDataSystem(UserPrincipal principal) {
        if (!principal.getId().equals(1L)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPTYPE_INVALID);
        }
    }


    /**
     * check data no class for teacher
     *
     * @param principal
     */
    public static void checkDataNoClassTeacher(UserPrincipal principal) {
        checkAppTypeTeacher(principal);
        checkIdSchool(principal);
    }

    public static void checkExistIdSchoolInPrinciple(UserPrincipal principal) {
        if (principal.getIdSchoolLogin() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản không thuộc trường nào");
        }
    }

    public static void checkExistSchoolInAgent(Long idSchool) {
        if (idSchool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_SCHOOL_IN_AGENT);
        }
    }

    public static void checkMatchIdUrlWithBody(Long idUrl, Long idBody) {
        if (!idUrl.equals(idBody)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id Url không khớp");
        }
    }

    public static void checkAppType(String appType) {
        if (!AppTypeConstant.PARENT.equals(appType) && !AppTypeConstant.TEACHER.equals(appType) && !AppTypeConstant.SCHOOL.equals(appType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPTYPE_INVALID);
        }
    }

    private static void checkAppTypeParent(UserPrincipal principal) {
        if (!AppTypeConstant.PARENT.equals(principal.getAppType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức năng chỉ dành cho phụ huynh");
        }
    }

    private static void checkAppTypeTeacher(UserPrincipal principal) {
        if (!AppTypeConstant.TEACHER.equals(principal.getAppType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức năng chỉ dành cho giáo viên");
        }
    }

    private static void checkAppTypePlus(UserPrincipal principal) {
        if (!AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức năng chỉ dành cho nhân sự nhà trường");
        }
    }

    private static void checkAppTypeSupperPlus(UserPrincipal principal) {
        if (!AppTypeConstant.SUPPER_SCHOOL.equals(principal.getAppType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức năng chỉ dành cho supper plus");
        }
    }

    private static void checkAppTypePlusOrTeacher(UserPrincipal principal) {
        if (!AppTypeConstant.SCHOOL.equals(principal.getAppType()) && !AppTypeConstant.TEACHER.equals(principal.getAppType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức năng chỉ dành cho nhà trường hoặc giáo viên");
        }
    }

    private static void checkIdSchool(UserPrincipal principal) {
        if (principal.getIdSchoolLogin() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không thuộc trường nào");
        }
    }

    private static void checkIdClass(UserPrincipal principal) {
        if (principal.getIdClassLogin() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không thuộc lớp nào");
        }
    }

    private static void checkIdKid(UserPrincipal principal) {
        if (principal.getIdKidLogin() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng chưa có học sinh nào");
        }
    }
}
