package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsExtraInfo;
import com.example.onekids_project.repository.KidsExtraInfoRepository;
import com.example.onekids_project.request.kids.KidsExtraInfoRequest;
import com.example.onekids_project.response.kids.KidsExtraInfoResponse;
import com.example.onekids_project.service.servicecustom.KidsExtraInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KidsExtraInfoServiceImpl implements KidsExtraInfoService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KidsExtraInfoRepository kidsExtraInfoRepository;

    @Override
    public Optional<KidsExtraInfoResponse> findByIdKidsExtraInfo(Long idSchool, Long id) {
        Optional<KidsExtraInfo> kidsExtraInfoOptional = kidsExtraInfoRepository.findByIdKidsExtraInfo(idSchool, id);
        if (kidsExtraInfoOptional.isEmpty()) {
            return Optional.empty();
        }
        KidsExtraInfoResponse kidsExtraInfoResponse = modelMapper.map(kidsExtraInfoOptional.get(), KidsExtraInfoResponse.class);
        return Optional.ofNullable(kidsExtraInfoResponse);
    }

    @Override
    public KidsExtraInfoResponse createKidsExtraInfo(Long idSchool, Kids kid, KidsExtraInfoRequest kidsExtraInfoRequest) {
        if (kidsExtraInfoRequest == null) {
            return null;
        }
        KidsExtraInfo kidsExtraInfo = modelMapper.map(kidsExtraInfoRequest, KidsExtraInfo.class);

        kidsExtraInfo.setIdSchool(idSchool);
        kidsExtraInfo.setKid(kid);
        KidsExtraInfo kidsExtraInforCreated = kidsExtraInfoRepository.save(kidsExtraInfo);
        KidsExtraInfoResponse kidsExtraInfoResponse = modelMapper.map(kidsExtraInforCreated, KidsExtraInfoResponse.class);
        return kidsExtraInfoResponse;
    }

    @Override
    public KidsExtraInfoResponse updateKidsExtraInfo(Long idSchool, Kids kid, KidsExtraInfoRequest kidsExtraInfoRequest) {
        Optional<KidsExtraInfo> kidsExtraInfoOptional = kidsExtraInfoRepository.findByIdKidsExtraInfo(idSchool, kid.getId());
        if (kidsExtraInfoOptional.isEmpty()) {
            return null;
        }
        KidsExtraInfo kidsExtraInfo = kidsExtraInfoOptional.get();
        modelMapper.map(kidsExtraInfoRequest, kidsExtraInfo);
        KidsExtraInfo kidsExtraInforUpdate = kidsExtraInfoRepository.save(kidsExtraInfo);
        KidsExtraInfoResponse kidsExtraInfoResponse = modelMapper.map(kidsExtraInforUpdate, KidsExtraInfoResponse.class);
        return kidsExtraInfoResponse;
    }
}
