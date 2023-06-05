package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.kids.AttendanceConfig;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.repository.AttendanceConfigRepository;
import com.example.onekids_project.service.servicecustom.AttendanceConfigService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceConfigServiceImpl implements AttendanceConfigService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;

    @Override
    public void createFirstAttendanceDateConfig(Long idSchool, SchoolConfig schoolConfig) {
        AttendanceConfig model = modelMapper.map(schoolConfig, AttendanceConfig.class);
        model.setIdSchool(idSchool);
        attendanceConfigRepository.save(model);
    }
}
