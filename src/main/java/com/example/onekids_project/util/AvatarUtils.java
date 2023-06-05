package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.MaUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Component
public class AvatarUtils {
    private static MaUserRepository maUserRepository;

    @Autowired
    public AvatarUtils(MaUserRepository maUserRepository) {
        AvatarUtils.maUserRepository = maUserRepository;
    }

    /**
     * lấy avatar nhân sự
     *
     * @param idSchool
     * @param idUser
     * @return
     */
    public static String getAvatarEmployeeWithSchool(Long idSchool, Long idUser) {
        MaUser maUser = maUserRepository.findById(idUser).orElseThrow(() -> new NoSuchElementException("not found MaUser by id"));
        if (!maUser.getAppType().equals(AppTypeConstant.SCHOOL) && !maUser.getAppType().equals(AppTypeConstant.TEACHER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_AVATAR_EMPLOYEE);
        }
        String avatar;
        InfoEmployeeSchool infoEmployeeSchool = EmployeeUtil.convertEmployeeToInfoEmployeeSchoolDeleteAble(idSchool, maUser.getEmployee());
        avatar = infoEmployeeSchool.getAvatar();
        if (StringUtils.isBlank(avatar)) {
            avatar = maUser.getAppType().equals(AppTypeConstant.SCHOOL) ? AvatarDefaultConstant.AVATAR_SCHOOL : AvatarDefaultConstant.AVATAR_TEACHER;
        }
        return avatar;
    }

    /**
     * lấy avatar phụ huynh
     *
     * @param idUser
     * @return
     */
    public static String getAvatarParent(Long idUser) {
        MaUser maUser = maUserRepository.findById(idUser).orElseThrow(() -> new NoSuchElementException("not found MaUser by id"));
        if (!maUser.getAppType().equals(AppTypeConstant.PARENT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_AVATAR_PARENT);
        }
        return StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
    }

    /**
     * lấy avatar phụ huynh
     *
     * @param parent
     * @return
     */
    public static String getAvatarParentFromObject(Parent parent) {
        return StringUtils.isNotBlank(parent.getAvatar()) ? parent.getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
    }

    /**
     * lấy avatar của infoEmployeeSchool
     *
     * @param infoEmployeeSchool
     * @return
     */
    public static String getAvatarInfoEmployee(InfoEmployeeSchool infoEmployeeSchool) {
        String avatar = infoEmployeeSchool.getAvatar();
        if (StringUtils.isBlank(avatar)) {
            if (AppTypeConstant.SCHOOL.equals(infoEmployeeSchool.getAppType())) {
                avatar = AvatarDefaultConstant.AVATAR_SCHOOL;
            } else if (AppTypeConstant.TEACHER.equals(infoEmployeeSchool.getAppType())) {
                avatar = AvatarDefaultConstant.AVATAR_TEACHER;
            }
        }
        return avatar;
    }

    public static String getAvatarKids(Kids kids) {
        return StringUtils.isNotBlank(kids.getAvatarKid()) ? kids.getAvatarKid() : AvatarDefaultConstant.AVATAR_KIDS;
    }

    public static String getAvatarSchool(School school) {
        return StringUtils.isNotBlank(school.getSchoolAvatar()) ? school.getSchoolAvatar() : AvatarDefaultConstant.AVATAR_SCHOOL_DEFAULT;
    }

}
