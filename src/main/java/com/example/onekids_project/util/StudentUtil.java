package com.example.onekids_project.util;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.repository.KidsClassDateRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.response.kids.KidStatusResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentUtil {
    private static KidsClassDateRepository kidsClassDateRepository;
    private static MaUserRepository maUserRepository;

    @Autowired
    public StudentUtil(KidsClassDateRepository kidsClassDateRepository, MaUserRepository maUserRepository) {
        StudentUtil.kidsClassDateRepository = kidsClassDateRepository;
        StudentUtil.maUserRepository = maUserRepository;
    }

    public static KidsClassDateRepository getKidsClassDateRepository() {
        return kidsClassDateRepository;
    }

    public static List<Long> getKidsClassList(LocalDate date, Long idClass) {
        return getKidsClassDateRepository().findByDateAndClass(date, idClass);
    }

    /**
     * lấy các trạng thái của học sinh
     *
     * @return
     */
    public static List<KidStatusResponse> getKidStatus() {
        List<StudentStatusEnum> studentEnumList = Arrays.asList(StudentStatusEnum.values());
        List<KidStatusResponse> kidStatusResponseList = new ArrayList<>();
        studentEnumList.forEach(e -> {
            KidStatusResponse kidStatusResponse = new KidStatusResponse();
            kidStatusResponse.setKey(e);
            kidStatusResponse.setValue(e.getValue());
            kidStatusResponseList.add(kidStatusResponse);
        });
        return kidStatusResponseList;
    }

    public static String getConvertKidStatus(String status) {
        if (status.equals(KidsStatusConstant.STUDYING)) {
            return KidsStatusConstant.STUDYING_NAME;
        }
        if (status.equals(KidsStatusConstant.STUDY_WAIT)) {
            return KidsStatusConstant.STUDY_WAIT_NAME;
        }
        if (status.equals(KidsStatusConstant.RESERVE)) {
            return KidsStatusConstant.RESERVE_NAME;
        }
        if (status.equals(KidsStatusConstant.LEAVE_SCHOOL)) {
            return KidsStatusConstant.LEAVE_SCHOOL_NAME;
        }
        return null;
    }

    public static String setLoginStatus(Kids kids) {
        String loginStatus = AppConstant.LOGIN_YET;
        if (kids.getParent() != null) {
            MaUser maUser = kids.getParent().getMaUser();
            List<Device> deviceList = maUser.getDeviceList();
            if (CollectionUtils.isEmpty(deviceList)) {
                loginStatus = AppConstant.LOGIN_YET;
            } else {
                long count = maUser.getDeviceList().stream().filter(Device::isLogin).count();
                loginStatus = count > 0 ? AppConstant.LOGIN_YES : AppConstant.LOGIN_NO;
            }
        }
        return loginStatus;
    }

    public static List<Kids> getKidInClassWithStudying(MaClass maClass) {
        return maClass.getKidsList().stream().filter(a -> a.isDelActive() && a.getKidStatus().equals(KidsStatusConstant.STUDYING)).collect(Collectors.toList());
    }

    public static List<Kids> getKidList() {
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrueAndActivatedTrue(principal.getId()).orElseThrow();
        List<Kids> kidsList = maUser.getParent().getKidsList().stream().filter(x -> x.isActivated() && x.isDelActive() && x.getKidStatus().equals(KidsStatusConstant.STUDYING)).collect(Collectors.toList());
        return kidsList;
    }
}
