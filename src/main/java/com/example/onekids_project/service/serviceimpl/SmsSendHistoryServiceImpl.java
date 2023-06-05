package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.dto.SmsSendDTO;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SmsSendHistoryRepository;
import com.example.onekids_project.request.notifihistory.SearchSmsSendHistoryRequest;
import com.example.onekids_project.response.notifihistory.ListSmsSendHistoryResponse;
import com.example.onekids_project.response.notifihistory.SmsSendHistoryResponse;
import com.example.onekids_project.service.servicecustom.SmsSendHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SmsSendHistoryServiceImpl implements SmsSendHistoryService {
    @Autowired
    private SmsSendHistoryRepository smsSendHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ListMapper listMapper;

    @Override
    public ListSmsSendHistoryResponse searchSmsSendHistory(Long idSchoolLogin, SearchSmsSendHistoryRequest searchSmsSendHistoryRequest) {
        List<SmsSend> smsSendList = smsSendHistoryRepository.searchSmsSendCustom(searchSmsSendHistoryRequest);
        if (CollectionUtils.isEmpty(smsSendList)) {
            return null;
        }
        smsSendList = filterNotifySys(smsSendList);
        List<SmsSendHistoryResponse> smsSendHistoryResponseList = listMapper.mapList(smsSendList, SmsSendHistoryResponse.class);
        smsSendHistoryResponseList.forEach(x -> {
            x.setCoutUserSent(x.getSmsReceiversList().size());
            Optional<MaUser> maUser = maUserRepository.findByIdAndDelActiveTrue(x.getIdUserSend());
            x.setNameUserSend(maUser.get().getFullName());

        });

        ListSmsSendHistoryResponse listSmsSendHistoryResponse = new ListSmsSendHistoryResponse();
        listSmsSendHistoryResponse.setSmsSendHistoryResponses(smsSendHistoryResponseList);
        return listSmsSendHistoryResponse;
    }

    @Override
    public Optional<SmsSendDTO> findByIdSmsSendHistory(Long idSchoolLogin, Long id) {
        Optional<SmsSend> optionalSmsSend = smsSendHistoryRepository.findById(id);
        if (optionalSmsSend.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(modelMapper.map(optionalSmsSend.get(), SmsSendDTO.class));
    }

    private List<SmsSend> filterNotifySys(List<SmsSend> smsSendList) {
        return smsSendList.stream().filter(x -> x.isSent() && !x.getAppType().equals(AppTypeConstant.SYSTEM)).collect(Collectors.toList());
    }
}





