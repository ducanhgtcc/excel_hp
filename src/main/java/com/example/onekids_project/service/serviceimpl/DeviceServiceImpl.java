package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.DeviceTypeConstant;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.request.DeviceLoginMobileRequest;
import com.example.onekids_project.repository.DeviceRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private SysInforRepository sysInforRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean saveDeviceLogin(Long idUser, DeviceWebRequest deviceWebRequest) {
        List<Device> deviceList = deviceRepository.findByIdDeviceAndDelActiveTrue(deviceWebRequest.getIdDevice());
        this.setLoginStatus(deviceList);
        List<Device> deviceIdUserList = deviceList.stream().filter(x -> x.getMaUser().getId().equals(idUser)).collect(Collectors.toList());
        Device device;
        if (CollectionUtils.isEmpty(deviceIdUserList)) {
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow(() -> new NotFoundException("not found mauser by id"));
            device = new Device();
            device.setMaUser(maUser);
            device.setIdDevice(deviceWebRequest.getIdDevice());
            device.setType(DeviceTypeConstant.WEB);
        } else {
            device = deviceIdUserList.get(0);
        }
        device.setLogin(AppConstant.APP_TRUE);
        device.setDateLogin(LocalDateTime.now());
        device.setDateLogout(null);
        deviceRepository.save(device);
        return true;
    }

    @Override
    public boolean saveDeviceLogout(Long idUser, DeviceWebRequest deviceWebRequest) {
        Optional<Device> deviceOptional = deviceRepository.findByMaUserIdAndIdDevice(idUser, deviceWebRequest.getIdDevice());
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            this.logoutDevice(device);
            return true;
        }
        return false;
    }

    @Override
    public void saveDeviceLogoutAdmin(Long idDevice) {
        Device device = deviceRepository.findById(idDevice).orElseThrow();
        this.logoutDevice(device);
    }

    @Override
    public boolean saveDeviceLoginMobile(Long idUser, DeviceLoginMobileRequest deviceLoginMobileRequest) {
        MaUser maUserDB = maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow();
        List<Device> deviceList = deviceRepository.findByIdDeviceAndMaUserAppTypeAndDelActiveTrue(deviceLoginMobileRequest.getIdDevice(), maUserDB.getAppType());
        this.setLoginStatus(deviceList);
        List<Device> deviceIdUserList = deviceList.stream().filter(x -> x.getMaUser().getId().equals(idUser)).collect(Collectors.toList());
        Device device;
        if (CollectionUtils.isEmpty(deviceIdUserList)) {
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow(() -> new NotFoundException("not found mauser by id"));
            device = new Device();
            device.setMaUser(maUser);
            device.setIdDevice(deviceLoginMobileRequest.getIdDevice());
        } else {
            device = deviceIdUserList.get(0);
        }
        device.setTokenFirebase(deviceLoginMobileRequest.getTokenFirebase());
        device.setType(deviceLoginMobileRequest.getType());
        device.setLogin(AppConstant.APP_TRUE);
        device.setDateLogin(LocalDateTime.now());
        device.setDateLogout(null);
        deviceRepository.save(device);
        return true;
    }

    /**
     * check số lượng thiết bị đăng nhập
     *
     * @param principal
     */
    @Override
    public void checkDeviceLimit(UserPrincipal principal, String idDevice) {
        String appType = principal.getAppType();
        Long idUser = principal.getId();
        int numberLogin = principal.getSchool().getNumberDevice();
        if (principal.getSchool().isLimitDevice() && numberLogin > 0) {
            List<String> typeList = Arrays.asList(DeviceTypeConstant.ANDROID, DeviceTypeConstant.IOS);
            List<Device> deviceList = deviceRepository.findByMaUserIdAndTypeInAndLoginTrueAndDelActiveTrueOrderByCreatedDateDesc(idUser, typeList);
            if (deviceList.size() >= numberLogin) {
                logger.warn("---App {} numberLogin={}: idUser= {}: ", appType, deviceList.size(), principal.getId());
                if (principal.getSchoolConfig().isApprovedLogoutStatus()) {
                    List<Device> filterDeviceList = deviceList.stream().filter(a -> !a.getIdDevice().equals(idDevice)).collect(Collectors.toList());
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(filterDeviceList)) {
                        this.logoutDevice(deviceList.get(0));
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vượt quá số lượng thiết bị được phép đăng nhập");
                }
            }
        }
    }

    @Override
    public void forceLogoutDevice(String idDevice, Long idUser) {
        if (StringUtils.isNotBlank(idDevice)) {
            Device device = deviceRepository.findByMaUserIdAndIdDevice(idUser, idDevice).orElseThrow(() -> new NoSuchElementException("not found idDevice={" + idDevice + "} and idUser={" + idUser + "}"));
            if (!device.isLogin()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản bị buộc đăng xuất");
            }
        }
    }

    /**
     * chuyển hết các tài khoản trạng thái login về false
     *
     * @param deviceList
     */
    private void setLoginStatus(List<Device> deviceList) {
        deviceList.forEach(x -> {
            x.setLogin(AppConstant.APP_FALSE);
            if (x.getDateLogout() == null) {
                x.setDateLogout(LocalDateTime.now());
            }
            deviceRepository.save(x);
        });
    }

    private void logoutDevice(Device device) {
        device.setLogin(AppConstant.APP_FALSE);
        device.setDateLogout(LocalDateTime.now());
        deviceRepository.save(device);
    }
}
