package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.entity.kids.KidsTransfer;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.response.kidstransfer.KidsTransferTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.KidsTransferTeacherService;
import com.example.onekids_project.repository.KidsTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lavanviet
 */
@Service
public class KidsTransferTeacherServiceImpl implements KidsTransferTeacherService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private KidsTransferRepository repository;

    @Override
    public List<KidsTransferTeacherResponse> searchDataArrive(Long idKid) {
        List<KidsTransfer> list = repository.findByKidsIdAndDelActiveTrueOrderByInStatusDescOutStatusDescIdDesc(idKid);
        return listMapper.mapList(list, KidsTransferTeacherResponse.class);
    }

    @Override
    public List<KidsTransferTeacherResponse> searchDataLeave(Long idKid) {
        List<KidsTransfer> list = repository.findByKidsIdAndDelActiveTrueOrderByOutStatusDescInStatusDescIdDesc(idKid);
        return listMapper.mapList(list, KidsTransferTeacherResponse.class);
    }
}
