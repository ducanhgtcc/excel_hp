package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.onecam.DeviceCam;
import com.example.onekids_project.entity.onecam.OneCamConfig;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.service.servicecustom.DeviceCamService;
import com.example.onekids_project.repository.DeviceCamRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.OneCamConfigRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.PrincipalUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author lavanviet
 */
@Service
public class DeviceCamServiceImpl implements DeviceCamService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private DeviceCamRepository deviceCamRepository;
    @Autowired
    private OneCamConfigRepository oneCamConfigRepository;

    @Override
    public void saveDeviceCame(Long idUser, String idDevice, String deviceType) {
        if (StringUtils.isBlank(idDevice)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có IdDevice");
        }
        Optional<DeviceCam> deviceCamOptional = deviceCamRepository.findByIdDeviceAndMaUserIdAndDelActiveTrue(idDevice, idUser);
        DeviceCam deviceCam = new DeviceCam();
        if (deviceCamOptional.isEmpty()) {
            MaUser maUser = maUserRepository.findById(idUser).orElseThrow();
            deviceCam.setMaUser(maUser);
            deviceCam.setIdDevice(idDevice);
        } else {
            deviceCam = deviceCamOptional.get();
            deviceCam.setDateLogout(null);
            deviceCam.setForceLogout(false);
        }
        deviceCam.setType(deviceType);
        deviceCam.setLogin(true);
        deviceCam.setDateLogin(LocalDateTime.now());
        deviceCamRepository.save(deviceCam);
    }

    @Override
    public void checkLogoutDeviceCame(String idDevice) {
        Long idUser = PrincipalUtils.getUserPrincipal().getId();
        DeviceCam deviceCam = deviceCamRepository.findByIdDeviceAndMaUserIdAndDelActiveTrue(idDevice, idUser).orElseThrow(() -> new NoSuchElementException("not found idDevice oncame: " + idDevice));
        if (deviceCam.isForceLogout()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu đăng xuất thiết bị");
        }
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        Long idSchool = principal.getSchool().getId();
        OneCamConfig oneCamConfig = oneCamConfigRepository.findBySchoolId(idSchool).orElseThrow();
        String appType = principal.getAppType();
        boolean checkStatus = false;
        if (StringUtils.equals(appType, AppTypeConstant.PARENT)) {
            checkStatus = oneCamConfig.isParentStatus();
        } else if (StringUtils.equals(appType, AppTypeConstant.TEACHER)) {
            checkStatus = oneCamConfig.isTeacherStatus();
        } else if (StringUtils.equals(appType, AppTypeConstant.SCHOOL)) {
            checkStatus = oneCamConfig.isPlusStatus();
        }
        if (!checkStatus) {
            logger.warn("---App OneCam force logout by status=false");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bắt buộc đăng xuất");
        }
    }

    @Override
    public void forceLogoutCame(Long id) {
        DeviceCam deviceCam = deviceCamRepository.findById(id).orElseThrow();
        deviceCam.setForceLogout(true);
        deviceCamRepository.save(deviceCam);
    }

    @Override
    public void checkDeviceCamLimit(UserPrincipal principal) {
        String appType = principal.getAppType();
        Long idSchool = principal.getSchool().getId();
        OneCamConfig oneCamConfig = oneCamConfigRepository.findBySchoolId(idSchool).orElseThrow();
        int numberLogin = 0;
        boolean checkStatus = false;
        if (StringUtils.equals(appType, AppTypeConstant.PARENT)) {
            numberLogin = oneCamConfig.getParentNumber();
            checkStatus = oneCamConfig.isParentStatus();
        } else if (StringUtils.equals(appType, AppTypeConstant.TEACHER)) {
            numberLogin = oneCamConfig.getTeacherNumber();
            checkStatus = oneCamConfig.isTeacherStatus();
        } else if (StringUtils.equals(appType, AppTypeConstant.SCHOOL)) {
            numberLogin = oneCamConfig.getPlusNumber();
            checkStatus = oneCamConfig.isPlusStatus();
        }
        if (!checkStatus) {
            logger.warn("---App OneCam not login by status=false");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không cho phép đăng nhập");
        }
        if (numberLogin > 0) {
            int deviceNumber = deviceCamRepository.findByMaUserIdAndLoginTrueAndForceLogoutFalse(principal.getId()).size();
            if (deviceNumber >= numberLogin) {
                logger.warn("---App OneCam {}, numberLogin={}, idUser= {}, idSchool={}: ", appType, deviceNumber, principal.getId(), idSchool);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vượt quá số lượng thiết bị được phép đăng nhập ở OneCam");
            }
        }
    }

    @Override
    public void logoutCamService(String idDevice) {
        Optional<DeviceCam> deviceCamOptional = deviceCamRepository.findByIdDeviceAndMaUserIdAndLoginTrueAndDelActiveTrue(idDevice, PrincipalUtils.getUserPrincipal().getId());
        if (deviceCamOptional.isPresent()) {
            DeviceCam deviceCam = deviceCamOptional.get();
            deviceCam.setLogin(false);
            deviceCam.setForceLogout(false);
            deviceCam.setDateLogout(LocalDateTime.now());
            deviceCamRepository.save(deviceCam);
        }
    }
}
