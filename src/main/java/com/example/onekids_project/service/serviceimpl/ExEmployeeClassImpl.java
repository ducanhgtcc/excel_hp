package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.Subject;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.ExEmployeeClassRepository;
import com.example.onekids_project.repository.SubjectRepository;
import com.example.onekids_project.request.classes.ExEmployeeClassRequest;
import com.example.onekids_project.response.classes.ExEmployeeClassResponse;
import com.example.onekids_project.service.servicecustom.ExEmployeeClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExEmployeeClassImpl implements ExEmployeeClassService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ExEmployeeClassRepository exEmployeeClassRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<ExEmployeeClassResponse> findByIdExEmployeeClass(Long idSchool, Long idClass) {
        List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByIdExEmployeeClass(idClass);
        if (CollectionUtils.isEmpty(exEmployeeClassList)) {
            return null;
        }

        List<ExEmployeeClassResponse> exEmployeeClassResponseList = listMapper.mapList(exEmployeeClassList, ExEmployeeClassResponse.class);
        exEmployeeClassResponseList = exEmployeeClassResponseList.stream().filter(x -> StringUtils.isNotBlank(x.getListIdSubject()) || x.isMaster()).collect(Collectors.toList());
        exEmployeeClassResponseList.forEach(x -> {
            String idsStringList = x.getListIdSubject();
            if (StringUtils.isNotBlank(idsStringList)) {
                List<String> idsString = Arrays.asList(idsStringList.split(","));
                List<Long> idsLongList = idsString.stream().map(Long::valueOf).collect(Collectors.toList());
                List<Subject> subjectList = subjectRepository.findByIdsSubject(idSchool, idsLongList);
                List<String> subjectNameList = subjectList.stream().map(Subject::getSubjectName).collect(Collectors.toList());
                x.setSubjectNameList(StringUtils.join(subjectNameList, ", "));
            }
        });

        return exEmployeeClassResponseList;
    }

    @Override
    public boolean updateExEmployeeClass(Long idSchool, ListExEmployeeClassRequest listExEmployeeClassRequest) {
        Long idClass = listExEmployeeClassRequest.getId();
        List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByIdExEmployeeClass(idClass);
        if (CollectionUtils.isEmpty(exEmployeeClassList)) {
            return false;
        }
        List<ExEmployeeClassRequest> exEmployeeClassRequestList = listExEmployeeClassRequest.getExEmployeeClassRequestList();
        exEmployeeClassRequestList.forEach(x -> {
            ExEmployeeClass exEmployeeClass = exEmployeeClassRepository.findByIdClassAndIdEmployee(idClass, x.getIdInfoEmployeeSchool());
            if (exEmployeeClass != null) {
                exEmployeeClass.setMaster(x.getIsMaster());
                List<Long> idSubjectList = x.getListIdSubject();
                if (!CollectionUtils.isEmpty(idSubjectList)) {
                    String idSubjectStringList = StringUtils.join(idSubjectList, ",");
//                    exEmployeeClass.setListIdSubject(idSubjectStringList);
                }
                exEmployeeClassRepository.save(exEmployeeClass);
            }
        });
        return true;
    }
}
