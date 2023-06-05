package com.example.onekids_project.service.serviceimpl.classes;

import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.DayOffClassRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.request.classes.DayOffClassManyRequest;
import com.example.onekids_project.request.classes.DayOffClassRequest;
import com.example.onekids_project.request.classes.DayOffClassUpdateRequest;
import com.example.onekids_project.request.classes.SearchDayOffClassRequest;
import com.example.onekids_project.response.classes.DayOffClassResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.classes.DayOffClassService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-05-05 14:03
 *
 * @author lavanviet
 */
@Service
public class DayOffClassServiceImpl implements DayOffClassService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private DayOffClassRepository dayOffClassRepository;
    @Autowired
    private MaClassRepository maClassRepository;

    @Transactional
    @Override
    public void createDayOffClass(DayOffClassRequest request) {
        Long idClass = request.getIdClass();
        String note = request.getNote();
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow();
        request.getDateList().forEach(x -> {
            Optional<DayOffClass> dayOffClassOptional = dayOffClassRepository.findByMaClassIdAndDateAndDelActiveTrue(idClass, x);
            if (dayOffClassOptional.isEmpty()) {
                DayOffClass dayOffClass = new DayOffClass();
                dayOffClass.setMaClass(maClass);
                dayOffClass.setDate(x);
                dayOffClass.setNote(note);
                dayOffClassRepository.save(dayOffClass);
            } else {
                DayOffClass dayOffClass = dayOffClassOptional.get();
                dayOffClass.setNote(note);
            }
        });
    }

    @Override
    public void createDayOffClassMany(DayOffClassManyRequest request) {
        request.getIdClassList().forEach(x -> {
            DayOffClassRequest dayOffClassRequest = new DayOffClassRequest();
            dayOffClassRequest.setIdClass(x);
            dayOffClassRequest.setDateList(request.getDateList());
            dayOffClassRequest.setNote(request.getNote());
            this.createDayOffClass(dayOffClassRequest);
        });
    }

    @Override
    public List<DayOffClassResponse> getDayOffClassYear(Long idClass, SearchDayOffClassRequest request) {
        List<DayOffClass> dayOffClassList = dayOffClassRepository.getDayOffClassYear(idClass, request);
        return listMapper.mapList(dayOffClassList, DayOffClassResponse.class);
    }

    @Override
    public void updateDayOffClassYear(UserPrincipal principal, DayOffClassUpdateRequest request) {
        DayOffClass dayOffClass = dayOffClassRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        dayOffClass.setNote(request.getNote());
        dayOffClassRepository.save(dayOffClass);
    }

    @Override
    public void deleteDayOffClassYear(UserPrincipal principal, Long id) {
        DayOffClass dayOffClass = dayOffClassRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        dayOffClassRepository.delete(dayOffClass);
    }

    @Transactional
    @Override
    public void deleteDayOffClassYearList(UserPrincipal principal, List<Long> idList) {
        dayOffClassRepository.deleteByIdIn(idList);
    }

    @Override
    public List<DayOffClassResponse> getDayOffClassView(UserPrincipal principal, Long idClass) {
        List<DayOffClass> dayOffClassList = dayOffClassRepository.findByMaClassIdAndDelActiveTrueOrderByDateDesc(idClass);
        return listMapper.mapList(dayOffClassList, DayOffClassResponse.class);
    }
}
