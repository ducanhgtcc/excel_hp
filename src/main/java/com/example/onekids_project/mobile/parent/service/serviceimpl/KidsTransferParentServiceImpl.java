package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.entity.kids.KidsTransfer;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.request.kidstransfer.KidsTransferParentCreateRequest;
import com.example.onekids_project.mobile.parent.request.kidstransfer.KidsTransferParentUpdateRequest;
import com.example.onekids_project.mobile.parent.response.kidstransfer.KidsTransferParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsTransferParentService;
import com.example.onekids_project.repository.KidsTransferRepository;
import com.example.onekids_project.request.kids.transfer.KidsTransferCreateRequest;
import com.example.onekids_project.request.kids.transfer.KidsTransferUpdateRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.util.PrincipalUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author lavanviet
 */
@Service
public class KidsTransferParentServiceImpl implements KidsTransferParentService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KidsTransferRepository repository;
    @Autowired
    private KidsService kidsService;

    @Override
    public List<KidsTransferParentResponse> kidsTransferSearch() {
        Long idKid = PrincipalUtils.getUserPrincipal().getIdKidLogin();
        List<KidsTransfer> kidsTransferList = repository.findByKidsIdAndDelActiveTrueOrderByInStatusDescOutStatusDescIdDesc(idKid);
        return listMapper.mapList(kidsTransferList, KidsTransferParentResponse.class);
    }

    @Override
    public void kidsTransferUpdateService(UserPrincipal principal, KidsTransferParentUpdateRequest request) throws IOException {
        KidsTransferUpdateRequest kidsTransferUpdateRequest = modelMapper.map(request, KidsTransferUpdateRequest.class);
        kidsService.kidsTransferUpdateService(principal, kidsTransferUpdateRequest);
    }

    @Override
    public void kidsTransferCreateService(UserPrincipal principal, KidsTransferParentCreateRequest request) throws IOException {
        KidsTransferCreateRequest kidsTransferCreateRequest = modelMapper.map(request, KidsTransferCreateRequest.class);
        kidsTransferCreateRequest.setIdKid(principal.getIdKidLogin());
        kidsService.kidsTransferCreateService(principal, kidsTransferCreateRequest);
    }
}
