package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.NotifiSysRequest;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.ListNotifiSysResponse;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.NotifiSysDetailResponse;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.NotifiSysResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.NotifsysPlusService;
import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.repository.AppSendRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.ReceiversRepository;
import com.example.onekids_project.repository.UrlFileAppSendRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotifisysPlusServiceImpl implements NotifsysPlusService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @Override
    public ListNotifiSysResponse searchNitifiSys(UserPrincipal principal, NotifiSysRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idReceiver = principal.getId();
        Long idSchool = principal.getIdSchoolLogin();
        List<AppSend> appSendList = appSendRepository.findNotifiSys(idSchool, idReceiver, request);
        ListNotifiSysResponse listNotifiSysResponse = new ListNotifiSysResponse();
        List<NotifiSysResponse> responseList = new ArrayList<>();
        appSendList.forEach(x -> {
            NotifiSysResponse model = new NotifiSysResponse();
            model.setId(x.getId());
            model.setContent(x.getSendContent());
            model.setTitle(x.getSendTitle());
            model.setAvatar(this.getAvatarNotifyPlus(x.getIdCreated()));
            int pictureNumber = (int) x.getUrlFileAppSendList().stream().filter(y -> StringUtils.isNotBlank(y.getAttachPicture())).count();
            int fileNumber = (int) x.getUrlFileAppSendList().stream().filter(y -> StringUtils.isNotBlank(y.getAttachFile())).count();
            model.setNumberPicture(pictureNumber);
            model.setNumberFile(fileNumber);
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            Receivers receivers = receiversRepository.findReceiverByIdUserAndIdSend(idReceiver, x.getId());
            model.setUserUnread(receivers.isUserUnread());
            responseList.add(model);
        });
        boolean lastPage = appSendList.size() < MobileConstant.MAX_PAGE_ITEM;
        listNotifiSysResponse.setDataList(responseList);
        listNotifiSysResponse.setLastPage(lastPage);
        return listNotifiSysResponse;
    }

    @Override
    public NotifiSysDetailResponse findDetailNotifiSys(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        AppSend appSend = appSendRepository.findById(id).orElseThrow();
        NotifiSysDetailResponse model = new NotifiSysDetailResponse();
        model.setContent(appSend.getSendContent());
        model.setTitle(appSend.getSendTitle());
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(appSend.getCreatedDate()));
        List<UrlFileAppSend> urlFileAppSendList = urlFileAppSendRepository.findUrlFileAppSendByThanh(id);
        List<ListFileNotifi> fileNotifis = new ArrayList<>();
        model.setPictureList(appSend.getUrlFileAppSendList().stream().filter(x -> x.getAttachPicture() != null).map(UrlFileAppSend::getAttachPicture).collect(Collectors.toList()));
        List<UrlFileAppSend> urlFileAppSendList1 = urlFileAppSendList.stream().filter(x -> x.getAttachFile() != null).collect(Collectors.toList());
        urlFileAppSendList1.forEach(x -> {
            ListFileNotifi model2 = new ListFileNotifi();
            model2.setName(x.getName());
            model2.setId(x.getId());
            if (x.getAttachFile() == null) {
                model2.setUrl("");
            } else {
                model2.setUrl(x.getAttachFile());
            }
            fileNotifis.add(model2);
        });
        Receivers receivers = receiversRepository.findReceiverByIdUserAndIdSend(principal.getId(), id);
        receivers.setUserUnread(AppConstant.APP_TRUE);
        receiversRepository.save(receivers);
        model.setFileList(fileNotifis);

        return model;
    }

    private String getAvatarNotifyPlus(Long idCreated) {
        if (idCreated == 0) {
            return AvatarDefaultConstant.AVATAR_SYSTEM;
        }
        MaUser maUser = maUserRepository.findById(idCreated).orElseThrow();
        if (maUser.getAppType().equals(AppTypeConstant.SUPPER_SCHOOL)) {
            return AvatarDefaultConstant.AVATAR_SCHOOL;
        }
        return AvatarDefaultConstant.AVATAR_SYSTEM;
    }
}
