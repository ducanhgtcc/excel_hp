package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsGroup;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.KidsGroupRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.studentgroup.CreateKidsGroupRequest;
import com.example.onekids_project.request.studentgroup.SearchStudentGroupRequest;
import com.example.onekids_project.request.studentgroup.TransferKidsGroupRequest;
import com.example.onekids_project.request.studentgroup.UpdateKidsGroupRequest;
import com.example.onekids_project.response.studentgroup.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.KidsGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KidsGroupServiceImpl implements KidsGroupService {

    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private KidsGroupRepository kidsGroupRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public ListKidsGroupResponse findAllKidsGroup(Long idSchool, PageNumberWebRequest request) {
        List<KidsGroup> kidsGroupList = kidsGroupRepository.findAllKidsGroup(idSchool, request);
        if (CollectionUtils.isEmpty(kidsGroupList)) {
            return null;
        }
        List<KidsGroupResponse> kidsGroupResponseList = listMapper.mapList(kidsGroupList, KidsGroupResponse.class);
        ListKidsGroupResponse listKidsGroupResponse = new ListKidsGroupResponse();
        listKidsGroupResponse.setKidsGroupResponseList(kidsGroupResponseList);
        long total = kidsGroupRepository.countKidsGroup(idSchool);
        listKidsGroupResponse.setTotal(total);
        kidsGroupResponseList.forEach(x -> {
            x.setKidsList(x.getKidsList().stream().filter(KidsDTO::isDelActive).collect(Collectors.toList()));
            x.setKidsNumber(x.getKidsList().size());
        });

        return listKidsGroupResponse;
    }

    @Override
    public Optional<KidsGroupResponse> findByIdKidsGroup(Long idSchool, Long id) {
        Optional<KidsGroup> kidsGroupOptional = kidsGroupRepository.findByIdKidsGroup(idSchool, id);
        if (kidsGroupOptional.isEmpty()) {
            return Optional.empty();
        }

        Optional<KidsGroupResponse> kidsGroupResponse = Optional.ofNullable(modelMapper.map(kidsGroupOptional.get(), KidsGroupResponse.class));
        if (kidsGroupResponse.isPresent()) {
            KidsGroupResponse groupResponse = kidsGroupResponse.get();
            groupResponse.setKidsNumber(kidsGroupResponse.get().getKidsList().size());
            List<Long> idKidsList = groupResponse.getKidsList().stream().filter(KidsDTO::isDelActive).map(KidsDTO::getId).collect(Collectors.toList());
            groupResponse.setIdKidsList(idKidsList);
        }
        return kidsGroupResponse;
    }

    @Override
    public CreateUpdateKidsGroupResponse createKidsGroup(Long idSchool, CreateKidsGroupRequest createKidsGroupRequest) {
        KidsGroup newKidsGroup = modelMapper.map(createKidsGroupRequest, KidsGroup.class);
        newKidsGroup.setIdSchool(idSchool);
        KidsGroup savedKidsGroup = kidsGroupRepository.save(newKidsGroup);
        return modelMapper.map(savedKidsGroup, CreateUpdateKidsGroupResponse.class);
    }

    @Override
    public CreateUpdateKidsGroupResponse updateKidsGroup(Long idSchool, UpdateKidsGroupRequest updateKidsGroupRequest) {
        KidsGroup kidsGroup = kidsGroupRepository.findByIdKidsGroup(idSchool, updateKidsGroupRequest.getId()).orElseThrow();
        modelMapper.map(updateKidsGroupRequest, kidsGroup);
        KidsGroup newKidsGroup = kidsGroupRepository.save(kidsGroup);
        return modelMapper.map(newKidsGroup, CreateUpdateKidsGroupResponse.class);
    }

    @Override
    public boolean deleteKidsGroup(Long idSchool, Long id) {
        KidsGroup kidsGroup = kidsGroupRepository.findByIdKidsGroup(idSchool, id).orElseThrow();
        long count = kidsGroup.getKidsList().stream().filter(BaseEntity::isDelActive).count();
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Có " + count + " học sinh trong nhóm");
        }
        kidsGroup.setDelActive(AppConstant.APP_TRUE);
        kidsGroupRepository.save(kidsGroup);
        return true;
    }

    @Override
    @Transactional
    public boolean insertTransferKidsGroup(Long idSchool, TransferKidsGroupRequest transferKidsGroupRequest) {
        KidsGroup kidsGroup = kidsGroupRepository.findByIdKidsGroup(idSchool, transferKidsGroupRequest.getId()).orElseThrow();
        Long idGroup = kidsGroup.getId();
        kidsGroupRepository.deleteTransferKidsGroup(idGroup);
        List<Long> idKidsList = transferKidsGroupRequest.getIdKidsGroupList();
        idKidsList.forEach(idKid -> kidsGroupRepository.insertTransferKidsGroup(idGroup, idKid));
        return true;
    }

    @Override
    public ListKidsStudentGroupResponse searchKids(UserPrincipal principal, SearchStudentGroupRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.findAllKidGroup(idSchool, request);
        ListKidsStudentGroupResponse listKidsStudentGroupResponse = new ListKidsStudentGroupResponse();
        List<KidsGroupResponseOther> kidsGroupResponseaList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsGroupResponseOther model = new KidsGroupResponseOther();
            model.setFullName(x.getFullName());
            model.setId(x.getId());
            kidsGroupResponseaList.add(model);
        });
        listKidsStudentGroupResponse.setResponseList(kidsGroupResponseaList);
        return listKidsStudentGroupResponse;
    }
}
