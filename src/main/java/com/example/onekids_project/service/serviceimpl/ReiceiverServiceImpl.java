package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.ReiceiversDTO;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.repository.ReceiversRepository;
import com.example.onekids_project.request.AppSend.ReceiversRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppReiceiverAprovedRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppReiceiverRevokeRequest;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ReiceiverService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReiceiverServiceImpl implements ReiceiverService {
    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Optional<ReiceiversDTO> findByidReiceivers(Long idSchoolLogin, Long id) {
        Optional<Receivers> optionalReceivers = receiversRepository.findByIdAndDelActiveTrue(id);
        if (optionalReceivers.isEmpty()) {
            return Optional.empty();
        }
        Optional<ReiceiversDTO> optionalReiceiversDTO = Optional.ofNullable(modelMapper.map(optionalReceivers.get(), ReiceiversDTO.class));
        return optionalReiceiversDTO;
    }

    // update approved
    @Override
    public ReceiversResponse updateApprove(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppReiceiverAprovedRequest updateSmsAppReiceiverAprovedRequest) {
        Optional<Receivers> receiversOptional = receiversRepository.findById(updateSmsAppReiceiverAprovedRequest.getId());
        if (receiversOptional.isEmpty()) {
            return null;
        }
        Receivers oldReiceivers = receiversOptional.get();
        modelMapper.map(updateSmsAppReiceiverAprovedRequest, oldReiceivers);
        oldReiceivers.setApproved(true);
        Receivers newReiceivers = receiversRepository.save(oldReiceivers);
        ReceiversResponse receiversResponse = modelMapper.map(newReiceivers, ReceiversResponse.class);
        return receiversResponse;
    }

    @Override
    public boolean deleteReiceivers(Long idSchoolLogin, Long id) {
        Optional<Receivers> receiversOptional = receiversRepository.findById(id);
        if (receiversOptional.isEmpty()) {
            return false;
        }
        Receivers deleteReiceivers = receiversOptional.get();
        deleteReiceivers.setDelActive(false);
        receiversRepository.delete(deleteReiceivers);
        return true;
    }

    @Override
    public ReceiversResponse updateRevoke2(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppReiceiverRevokeRequest updateSmsAppReiceiverRevokeRequest) {
        Optional<Receivers> receiversOptional = receiversRepository.findById(updateSmsAppReiceiverRevokeRequest.getId());
        if (receiversOptional.isEmpty()) {
            return null;
        }
        Receivers oldReiceivers = receiversOptional.get();
        modelMapper.map(updateSmsAppReiceiverRevokeRequest, oldReiceivers);
        oldReiceivers.setSendDel(false);
        Receivers newReiceivers = receiversRepository.save(oldReiceivers);
        ReceiversResponse receiversResponse = modelMapper.map(newReiceivers, ReceiversResponse.class);
        return receiversResponse;
    }

    // update many approved
    @Override
    public ReceiversRequest updateManyApprove(Long id, List<ReceiversRequest> receiversRequests) {
        receiversRequests.forEach(x -> {
            Optional<Receivers> receiversOptional = receiversRepository.findById(id);
            if (receiversOptional.isPresent()) {
                Receivers receivers = receiversOptional.get();
                receivers.setApproved(true);
                receiversRepository.save(receivers);
            }
        });
        return null;
    }

    // update senddel
    @Override
    public ReceiversResponse updateRevoke1(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppReiceiverRevokeRequest updateSmsAppReiceiverRevokeRequest) {
        CommonValidate.checkDataPlus(principal);
        Optional<Receivers> receiversOptional = receiversRepository.findById(updateSmsAppReiceiverRevokeRequest.getId());
        if (receiversOptional.isEmpty()) {
            return null;
        }
        Receivers oldReiceivers = receiversOptional.get();
        modelMapper.map(updateSmsAppReiceiverRevokeRequest, oldReiceivers);
        oldReiceivers.setSendDel(true);
        Receivers newReiceivers = receiversRepository.save(oldReiceivers);
        ReceiversResponse receiversResponse = modelMapper.map(newReiceivers, ReceiversResponse.class);
        return receiversResponse;
    }

}


