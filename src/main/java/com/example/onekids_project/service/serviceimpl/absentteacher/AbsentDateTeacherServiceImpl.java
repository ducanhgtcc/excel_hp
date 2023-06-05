package com.example.onekids_project.service.serviceimpl.absentteacher;

import com.example.onekids_project.entity.employee.AbsentDateTeacher;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AbsentDateTeacherRepository;
import com.example.onekids_project.response.absentteacher.AbsentDateTeacherResponse;
import com.example.onekids_project.service.servicecustom.absentteacher.AbsentDateTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * date 2021-05-24 3:04 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class AbsentDateTeacherServiceImpl implements AbsentDateTeacherService {

    @Autowired
    private AbsentDateTeacherRepository absentDateTeacherRepository;
    @Autowired
    private ListMapper listMapper;

    @Override
    public List<AbsentDateTeacherResponse> findAllByAbsentTeacherId(Long id) {
        List<AbsentDateTeacher> absentDateTeacherList = absentDateTeacherRepository.findAllByAbsentTeacherId(id);
        if (CollectionUtils.isEmpty(absentDateTeacherList)) {
            return null;
        }
        List<AbsentDateTeacherResponse> dateList = listMapper.mapList(absentDateTeacherList, AbsentDateTeacherResponse.class);
        return dateList;
    }
}
