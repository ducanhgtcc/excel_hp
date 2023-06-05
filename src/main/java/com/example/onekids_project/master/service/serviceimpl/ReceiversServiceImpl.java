package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.FirebaseRouterConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.master.response.ReceiversResponse;
import com.example.onekids_project.master.service.ReceiversService;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.ParentRepository;
import com.example.onekids_project.repository.ReceiversRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceiversServiceImpl implements ReceiversService {

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ReceiversResponse> findAllReceivers(Long idAppSend) {
        List<Receivers> receiversList = receiversRepository.findByAppSendIdAndDelActiveTrue(idAppSend);
        List<ReceiversResponse> receiversResponseList = new ArrayList<>();
        receiversList.forEach(receivers -> {
            ReceiversResponse receiversResponse = new ReceiversResponse();
            if (receivers.getIdUserReceiver() != null) {
                MaUser maUser = maUserRepository.findById(receivers.getIdUserReceiver()).orElseThrow();
                String fullName = maUser.getFullName();
                receiversResponse.setFullName(fullName);
                String phone = maUser.getPhone();
                receiversResponse.setPhone(phone);
                receiversResponse.setAppType(maUser.getAppType());
            }
            if (receivers.isUserUnread()) {
                receiversResponse.setUserUnread("Đã đọc");
                receiversResponse.setTimeRead(receivers.getTimeRead());

            } else if (!receivers.isUserUnread()) {
                receiversResponse.setUserUnread("Chưa đọc");
            }
            receiversResponse.setUnRead(receivers.isUserUnread());
            if (receivers.getIdKids() != null) {
                Kids kids = kidsRepository.findById(receivers.getIdKids()).orElseThrow();
                receiversResponse.setClassName(kids.getFullName() + " - " + kids.getMaClass().getClassName());
            } else {

            }
            receiversResponse.setId(receivers.getId());
            receiversResponse.setSendDel(receivers.isSendDel());
            receiversResponse.setIsApproved(receivers.isApproved());
            receiversResponseList.add(receiversResponse);
        });
        return receiversResponseList;
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<Receivers> receiversOptional = receiversRepository.findByIdAndDelActiveTrue(id);
        if (receiversOptional.isEmpty()) {
            return false;
        }
        receiversRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteByMultiId(List<Long> idList) {
        idList.forEach(id -> {
            this.deleteById(id);
        });

        return true;
    }

    @Override
    public boolean revokeReceiversNotify(Long idReceivers) {
        Optional<Receivers> receiversOptional = receiversRepository.findByIdAndDelActiveTrue(idReceivers);
        if (receiversOptional.isEmpty()) {
            return false;
        }
        Receivers receivers = receiversOptional.get();
        receivers.setSendDel(!receivers.isSendDel());
        receiversRepository.save(receivers);
        return true;
    }

    @Override
    public boolean revokeMultiReceiversNotify(List<Long> idList) {
        idList.forEach(id -> {
            Optional<Receivers> receiversOptional = receiversRepository.findByIdAndDelActiveTrue(id);

            Receivers receivers = receiversOptional.get();
            receivers.setSendDel(AppConstant.APP_TRUE);
            receiversRepository.save(receivers);

        });
        return true;
    }

    @Override
    public boolean revokeMultiReceiversNotifyShow(List<Long> idList) {
        idList.forEach(id -> {
            Optional<Receivers> receiversOptional = receiversRepository.findByIdAndDelActiveTrue(id);
            Receivers receivers = receiversOptional.get();
            receivers.setSendDel(AppConstant.APP_FALSE);
            receiversRepository.save(receivers);

        });
        return true;
    }

    @Transactional
    @Override
    public boolean approvedReceiversNotify(Long idReceivers) throws FirebaseMessagingException {
        Optional<Receivers> receiversOptional = receiversRepository.findByIdAndDelActiveTrue(idReceivers);
        if (receiversOptional.isEmpty()) {
            return false;
        }
        Receivers receivers = receiversOptional.get();
        receivers.setApproved(AppConstant.APP_TRUE);
        receiversRepository.save(receivers);

//            firebase
        this.sendFireBase(receivers);
        return true;
    }

    //        fireBasse
    private void sendFireBase(Receivers receivers) throws FirebaseMessagingException {

        if (receivers.getAppSend().getAppType().equalsIgnoreCase(AppTypeConstant.TEACHER)) {
            Optional<Parent> parent = parentRepository.findByIdKidLoginAndDelActiveTrue(receivers.getIdKids());
            String title = receivers.getAppSend().getSendTitle().length() < 50 ? receivers.getAppSend().getSendTitle() : receivers.getAppSend().getSendTitle().substring(0, 50);
            String content = receivers.getAppSend().getSendContent().length() < 50 ? receivers.getAppSend().getSendContent() : receivers.getAppSend().getSendContent().substring(0, 50);
            List<TokenFirebaseObject> tokenFirebaseObjectList = null;
            if (parent.isPresent()) {
                tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(parent.get());
            }
            if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                NotifyRequest notifyRequest = new NotifyRequest();
                notifyRequest.setBody(content);
                notifyRequest.setTitle(title);
                FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.NOTIFY_PARENT, notifyRequest, receivers.getIdKids().toString());
            }
        } else {
            String title = receivers.getAppSend().getSendTitle().length() < 50 ? receivers.getAppSend().getSendTitle() : receivers.getAppSend().getSendTitle().substring(0, 50);
            String content = receivers.getAppSend().getSendContent().length() < 50 ? receivers.getAppSend().getSendContent() : receivers.getAppSend().getSendContent().substring(0, 50);
            Optional<MaUser> maUser = maUserRepository.findByIdAndDelActiveTrue(receivers.getIdUserReceiver());
            if (maUser.isPresent() && maUser.get().getEmployee() != null) {
                List<InfoEmployeeSchool> infoEmployeeSchool = maUser.get().getEmployee().getInfoEmployeeSchoolList();
                infoEmployeeSchool = infoEmployeeSchool.stream().filter(x -> x.getSchool().getId().equals(receivers.getIdSchool())).collect(Collectors.toList());
                List<TokenFirebaseObject> tokenFirebaseObjectList = null;
                if (!infoEmployeeSchool.isEmpty()) {
                    tokenFirebaseObjectList = firebaseService.getEmployeeTokenFirebases(infoEmployeeSchool);
                }
                if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                    NotifyRequest notifyRequest = new NotifyRequest();
                    notifyRequest.setBody(content);
                    notifyRequest.setTitle(title);
                    FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsTeacher(tokenFirebaseObjectList, FirebaseRouterConstant.NOTIFY_TEACHER, notifyRequest);
                }
            }
        }
    }
}
