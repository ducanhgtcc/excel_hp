package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.entity.kids.KidsTransfer;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.response.kidstransfer.KidsTransferPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsTransferPlusService;
import com.example.onekids_project.repository.KidsTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lavanviet
 */
@Service
public class KidsTransferPlusServiceImpl implements KidsTransferPlusService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private KidsTransferRepository repository;

    @Override
    public List<KidsTransferPlusResponse> searchDataArrive(Long idKid) {
        List<KidsTransfer> list = repository.findByKidsIdAndDelActiveTrueOrderByInStatusDescOutStatusDescIdDesc(idKid);
        return listMapper.mapList(list, KidsTransferPlusResponse.class);
    }

    @Override
    public List<KidsTransferPlusResponse> searchDataLeave(Long idKid) {
        List<KidsTransfer> list = repository.findByKidsIdAndDelActiveTrueOrderByOutStatusDescInStatusDescIdDesc(idKid);
        return listMapper.mapList(list, KidsTransferPlusResponse.class);
    }
}
