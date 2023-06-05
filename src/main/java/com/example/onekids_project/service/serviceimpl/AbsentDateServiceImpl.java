package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.AbsentDateDTO;
import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AbsentDateRepository;
import com.example.onekids_project.response.parentdiary.AbsentDateResponse;
import com.example.onekids_project.response.parentdiary.ListAbsentDateResponse;
import com.example.onekids_project.service.servicecustom.AbsentDateSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class AbsentDateServiceImpl implements AbsentDateSerVice {
    @Autowired
    private AbsentDateRepository absentDateRepository;
    @Autowired
    private ListMapper listMapper;

    @Override
    public ListAbsentDateResponse findAllAbsentDate(Long idSchoolLogin, Pageable pageable) {
        List<AbsentDate> absentDateList = absentDateRepository.findAllAbsentDate(idSchoolLogin, pageable);
        if (CollectionUtils.isEmpty(absentDateList)) {
            return null;
        }
        List<AbsentDateResponse> absentDateResponseList = listMapper.mapList(absentDateList, AbsentDateResponse.class);
        ListAbsentDateResponse listAbsentDateResponse = new ListAbsentDateResponse();
        listAbsentDateResponse.setAbsentDateResponseList(absentDateResponseList);
        return listAbsentDateResponse;
    }

    @Override
    public List<AbsentDateDTO> findByIdAbsentDate(Long idSchoolLogin, Long id) {
        List<AbsentDate> absentDateList = absentDateRepository.findByIdAbsentDate(idSchoolLogin, id);
        if (CollectionUtils.isEmpty(absentDateList)) {
            return null;
        }
        List<AbsentDateDTO> absentDateDTOList = listMapper.mapList(absentDateList, AbsentDateDTO.class);
        return absentDateDTOList;
    }
}


