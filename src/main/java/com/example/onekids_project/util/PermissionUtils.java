package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * date 2021-04-23 15:29
 *
 * @author lavanviet
 */
public class PermissionUtils {
    public static Set<String> getApiOfUser(MaUser maUser, Long idSchool) {
        List<Role> roleList = idSchool != null ? maUser.getRoleList().stream().filter(x -> x.isDelActive() && x.getType().equals(maUser.getAppType()) && x.getIdSchool().equals(idSchool)).distinct().collect(Collectors.toList()) : maUser.getRoleList().stream().filter(x -> x.isDelActive() && x.getType().equals(maUser.getAppType())).distinct().collect(Collectors.toList());
        Set<String> apiSet = new HashSet<>();
        String type = AppTypeConstant.TEACHER.equals(maUser.getAppType()) ? AppTypeConstant.SCHOOL : maUser.getAppType();
        roleList.forEach(x -> x.getApiList().stream().filter(a -> a.isDelActive() && a.getType().equals(type)).forEach(y -> apiSet.add(y.getApiUrl())));
        return apiSet;
    }
}
