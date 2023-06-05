package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.SmsReiceiveDTO;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SmsReiceiversRepository;
import com.example.onekids_project.service.servicecustom.SmsReiceriversService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;


@Service
public class SmsReiceiversServiceImpl implements SmsReiceriversService {
    @Autowired
    private SmsReiceiversRepository smsReiceiversRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    public boolean deleteSmsReiceivers(Long id) {
        SmsReceivers smsReceivers = smsReiceiversRepository.findById(id).orElseThrow();
        smsReiceiversRepository.deleteById(smsReceivers.getId());
        return true;
    }

    @Override
    public SmsReiceiveDTO findByIdSmsReiceiver(Long idSchoolLogin, Long id) {
        SmsReceivers smsReceivers = smsReiceiversRepository.findById(id).orElseThrow(() -> new NotFoundException("not found feedback by id"));
        SmsReiceiveDTO smsReiceiveDTO = modelMapper.map(smsReceivers, SmsReiceiveDTO.class);
        if (smsReceivers.getIdUserReceiver() != null) {
            MaUser maUser = maUserRepository.findById(smsReceivers.getIdUserReceiver()).orElseThrow(() -> new NotFoundException("not found maUser by id "));
            smsReiceiveDTO.setNameUser(maUser.getFullName());
            smsReiceiveDTO.setTitle("a");
        }
        return smsReiceiveDTO;
    }

    @Override
    public boolean deleteMultiSms(Long idSchoolLogin, Long[] ids) {
        for (Long idSms : ids) {
            Optional<SmsReceivers> smsReceiversOptional = smsReiceiversRepository.findByIdAndDelActive(idSms, true);
            if (smsReceiversOptional.isEmpty()) {
                return false;
            }
            smsReceiversOptional.get().setDelActive(false);
            smsReiceiversRepository.save(smsReceiversOptional.get());
        }
        return true;
    }


    // reiceivers appsend


}





