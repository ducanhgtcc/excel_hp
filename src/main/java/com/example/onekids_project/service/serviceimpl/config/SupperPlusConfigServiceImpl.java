package com.example.onekids_project.service.serviceimpl.config;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.AttendanceConfig;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.entity.system.SysConfig;
import com.example.onekids_project.repository.AttendanceConfigRepository;
import com.example.onekids_project.repository.SchoolConfigRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.repository.SysConfigRepository;
import com.example.onekids_project.request.schoolconfig.SchoolConfigRequest;
import com.example.onekids_project.request.system.AttendaceConfigRequest;
import com.example.onekids_project.response.schoolconfig.SchoolConfigAttendanceResponse;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.response.schoolconfig.SysConfigShowResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.config.SupperPlusConfigService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

/**
 * date 2021-05-24 09:43
 *
 * @author lavanviet
 */
@Service
public class SupperPlusConfigServiceImpl implements SupperPlusConfigService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SysConfigRepository sysConfigRepository;
    @Autowired
    private SchoolConfigRepository schoolConfigRepository;
    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;

    @Override
    public SchoolConfigResponse findSchoolConfigByIdSchool(UserPrincipal principal) {
//        CommonValidate.checkDataSupperPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found school by id in schoolconfig"));
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found schoolconfig by id"));
        SchoolConfigResponse schoolConfigResponse = modelMapper.map(schoolConfig, SchoolConfigResponse.class);
        SysConfig sysConfig = sysConfigRepository.findBySchoolIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found sysconfig by id"));
        SysConfigShowResponse sysConfigShowResponse = modelMapper.map(sysConfig, SysConfigShowResponse.class);
        schoolConfigResponse.setSysConfigShowResponse(sysConfigShowResponse);
        return schoolConfigResponse;
    }

    @Override
    public SchoolConfigAttendanceResponse findSchoolConfigByIdSchoolByAttendance(UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found school by id in schoolconfig"));
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found schoolconfig by id"));
        SchoolConfigAttendanceResponse response = new SchoolConfigAttendanceResponse();
        response.setTimePickupKid(schoolConfig.getTimePickupKid());
        return response;
    }

    @Transactional
    @Override
    public void updateConfigCommon(UserPrincipal principal, SchoolConfigRequest schoolConfigRequest) {
        CommonValidate.checkDataSupperPlus(principal);
        SchoolConfig schoolConfig = schoolConfigRepository.findByIdAndDelActive(schoolConfigRequest.getId(), AppConstant.APP_TRUE);
        modelMapper.map(schoolConfigRequest, schoolConfig);
        schoolConfigRepository.save(schoolConfig);
        this.saveChangeAttendanceConfig(principal.getIdSchoolLogin(), schoolConfigRequest);
    }

    @Override
    public void saveChangeAttendanceConfig(Long idSchool, SchoolConfigRequest schoolConfigRequest) {
        AttendanceConfig attendanceConfigFinal = attendanceConfigRepository.findAttendanceConfigFinal(idSchool).orElseThrow();
        //STT từ 15-27
        if (
                attendanceConfigFinal.isMorningSaturday() != schoolConfigRequest.isMorningSaturday() ||
                        attendanceConfigFinal.isAfternoonSaturday() != schoolConfigRequest.isAfternoonSaturday() ||
                        attendanceConfigFinal.isEveningSaturday() != schoolConfigRequest.isEveningSaturday() ||
                        attendanceConfigFinal.isSunday() != schoolConfigRequest.isSunday() ||
                        attendanceConfigFinal.isMorningAttendanceArrive() != schoolConfigRequest.isMorningAttendanceArrive() ||
                        attendanceConfigFinal.isAfternoonAttendanceArrive() != schoolConfigRequest.isAfternoonAttendanceArrive() ||
                        attendanceConfigFinal.isEveningAttendanceArrive() != schoolConfigRequest.isEveningAttendanceArrive() ||
                        attendanceConfigFinal.isMorningEat() != schoolConfigRequest.isMorningEat() ||
                        attendanceConfigFinal.isSecondMorningEat() != schoolConfigRequest.isSecondMorningEat() ||
                        attendanceConfigFinal.isLunchEat() != schoolConfigRequest.isLunchEat() ||
                        attendanceConfigFinal.isAfternoonEat() != schoolConfigRequest.isAfternoonEat() ||
                        attendanceConfigFinal.isSecondAfternoonEat() != schoolConfigRequest.isSecondAfternoonEat() ||
                        attendanceConfigFinal.isEveningEat() != schoolConfigRequest.isEveningEat()

        ) {
            logger.info("---attendance_config for update or create---");
            AttendaceConfigRequest attendaceConfigRequest = new AttendaceConfigRequest();
            modelMapper.map(schoolConfigRequest, attendaceConfigRequest);
            Optional<AttendanceConfig> attendanceConfigOld = attendanceConfigRepository.findAttendanceConfigInDate(idSchool, LocalDate.now());
            if (attendanceConfigOld.isPresent()) {
                AttendanceConfig attendanceConfigNow = attendanceConfigOld.get();
                modelMapper.map(attendaceConfigRequest, attendanceConfigNow);
                attendanceConfigRepository.save(attendanceConfigNow);
            } else {
                AttendanceConfig attendanceConfig = new AttendanceConfig();
                modelMapper.map(attendaceConfigRequest, attendanceConfig);
                attendanceConfig.setIdSchool(idSchool);
                attendanceConfigRepository.save(attendanceConfig);
            }
        }
    }


}
