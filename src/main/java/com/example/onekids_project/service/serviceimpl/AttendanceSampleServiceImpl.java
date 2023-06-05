package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.sample.AttendanceSample;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AttendanceSampleRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.schoolconfig.AttendanceSampleCreateRequest;
import com.example.onekids_project.request.schoolconfig.AttendanceSampleUpdateRequest;
import com.example.onekids_project.response.schoolconfig.AttendanceSampleConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AttendanceSampleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceSampleServiceImpl implements AttendanceSampleService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private AttendanceSampleRepository attendanceSampleRepository;

    @Override
    public List<AttendanceSampleConfigResponse> findAllAttendanceSample(Long idSchool, UserPrincipal principal) {
        List<AttendanceSample> attendanceSampleList;
        if (principal.getSchoolConfig().isShowAttentendanceSys()) {
            attendanceSampleList = attendanceSampleRepository.findAllAttendanceSample(idSchool, SystemConstant.ID_SYSTEM);
        } else {
            attendanceSampleList = attendanceSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(idSchool);
        }
        if (CollectionUtils.isEmpty(attendanceSampleList)) {
            return null;
        }
        List<AttendanceSampleConfigResponse> attendanceSampleConfigResponseList = listMapper.mapList(attendanceSampleList, AttendanceSampleConfigResponse.class);
        return attendanceSampleConfigResponseList;
    }

    @Override
    public AttendanceSampleConfigResponse createAttendanceSample(Long idSchool, AttendanceSampleCreateRequest attendanceSampleCreateRequest) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActiveTrue(idSchool);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        AttendanceSample attendanceSample = modelMapper.map(attendanceSampleCreateRequest, AttendanceSample.class);
        attendanceSample.setIdSchool(idSchool);
        AttendanceSample evaluateSampleSaved = attendanceSampleRepository.save(attendanceSample);
        AttendanceSampleConfigResponse attendanceSampleConfigResponse = modelMapper.map(evaluateSampleSaved, AttendanceSampleConfigResponse.class);
        return attendanceSampleConfigResponse;
    }

    @Override
    public AttendanceSampleConfigResponse updateAttendanceSample(Long idSchool, AttendanceSampleUpdateRequest attendanceSampleUpdateRequest) {
        Optional<AttendanceSample> attendanceSampleOptional = attendanceSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(attendanceSampleUpdateRequest.getId(), idSchool);
        if (attendanceSampleOptional.isEmpty()) {
            return null;
        }
        AttendanceSample attendanceSample = attendanceSampleOptional.get();
        modelMapper.map(attendanceSampleUpdateRequest, attendanceSample);
        AttendanceSample evaluateSampleSaved = attendanceSampleRepository.save(attendanceSample);
        AttendanceSampleConfigResponse attendanceSampleConfigResponse = modelMapper.map(evaluateSampleSaved, AttendanceSampleConfigResponse.class);
        return attendanceSampleConfigResponse;
    }

    @Override
    public boolean deleteAttendanceSample(Long idSchool, Long id) {
        Optional<AttendanceSample> attendanceSampleOptional = attendanceSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool);
        if (attendanceSampleOptional.isEmpty()) {
            return false;
        }
        AttendanceSample attendanceSample = attendanceSampleOptional.get();
        attendanceSample.setDelActive(AppConstant.APP_FALSE);
        attendanceSampleRepository.save(attendanceSample);
        return true;
    }

    //    ---------------------method of master----------------
    @Override
    public List<AttendanceSampleConfigResponse> findAllAttendanceSampleSytem() {
        List<AttendanceSample> attendanceSampleList = attendanceSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(SystemConstant.ID_SYSTEM);
        List<AttendanceSampleConfigResponse> attendanceSampleConfigResponseList = listMapper.mapList(attendanceSampleList, AttendanceSampleConfigResponse.class);
        return attendanceSampleConfigResponseList;
    }

    @Override
    public AttendanceSampleConfigResponse createAttendanceSampleSystem(AttendanceSampleCreateRequest attendanceSampleCreateRequest) {
        AttendanceSample attendanceSample = modelMapper.map(attendanceSampleCreateRequest, AttendanceSample.class);
        attendanceSample.setIdSchool(SystemConstant.ID_SYSTEM);
        AttendanceSample evaluateSampleSaved = attendanceSampleRepository.save(attendanceSample);
        AttendanceSampleConfigResponse attendanceSampleConfigResponse = modelMapper.map(evaluateSampleSaved, AttendanceSampleConfigResponse.class);
        return attendanceSampleConfigResponse;
    }

    @Override
    public AttendanceSampleConfigResponse updateAttendanceSampleSystem(AttendanceSampleUpdateRequest attendanceSampleUpdateRequest) {
        Optional<AttendanceSample> attendanceSampleOptional = attendanceSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(attendanceSampleUpdateRequest.getId(), SystemConstant.ID_SYSTEM);
        if (attendanceSampleOptional.isEmpty()) {
            return null;
        }
        AttendanceSample attendanceSample = attendanceSampleOptional.get();
        modelMapper.map(attendanceSampleUpdateRequest, attendanceSample);
        AttendanceSample evaluateSampleSaved = attendanceSampleRepository.save(attendanceSample);
        AttendanceSampleConfigResponse attendanceSampleConfigResponse = modelMapper.map(evaluateSampleSaved, AttendanceSampleConfigResponse.class);
        return attendanceSampleConfigResponse;
    }

    @Override
    public boolean deleteAttendanceSampleSystem(Long id) {
        Optional<AttendanceSample> attendanceSampleOptional = attendanceSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(id, SystemConstant.ID_SYSTEM);
        if (attendanceSampleOptional.isEmpty()) {
            return false;
        }
        AttendanceSample attendanceSample = attendanceSampleOptional.get();
        attendanceSampleRepository.deleteById(id);
        return true;
    }
}
