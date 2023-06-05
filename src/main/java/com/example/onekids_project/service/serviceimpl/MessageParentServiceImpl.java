package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MessageParentRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.MessageParentRequest;
import com.example.onekids_project.request.parentdiary.SearchMessageParentRequest;
import com.example.onekids_project.response.parentdiary.ListMessageParentResponse;
import com.example.onekids_project.response.parentdiary.MessageNewResponse;
import com.example.onekids_project.response.parentdiary.MessageParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MessageParentSerVice;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageParentServiceImpl implements MessageParentSerVice {
    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMessageParentResponse searchMessageParent(UserPrincipal principal, SearchMessageParentRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListMessageParentResponse response = new ListMessageParentResponse();
        List<MessageParent> messageParentList = messageParentRepository.searchMessageParent(idSchool, request);
        long total = messageParentRepository.countSearchMessageParent(idSchool, request);
        List<MessageParentResponse> messageParentResponseList = listMapper.mapList(messageParentList, MessageParentResponse.class);
        messageParentResponseList.forEach(x -> {
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(x.getIdClass()).orElseThrow();
            x.setClassName(maClass.getClassName());
        });
        response.setTotal(total);
        response.setResponseList(messageParentResponseList);
        return response;
    }

    @Override
    public MessageParentRequest updateRead(Long id, List<MessageParentRequest> messageParentResponse) {
        messageParentResponse.forEach(x -> {
            Optional<MessageParent> messageParentOptional = messageParentRepository.findById(x.getId());
            if (messageParentOptional.isPresent()) {
                MessageParent messageParent = messageParentOptional.get();
                messageParent.setTeacherUnread(AppConstant.APP_TRUE);
                messageParentRepository.save(messageParent);
            }
        });
        return null;
    }

    @Transactional
    @Override
    public boolean updateConfirmMany(Long id, UserPrincipal userPrincipal, List<MessageParentRequest> messageParentResponse) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(userPrincipal);
        for (MessageParentRequest x : messageParentResponse) {
            Optional<MessageParent> byIdMessage = messageParentRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(x.getId());
            if (byIdMessage.isPresent()) {
                MessageParent messageParent = byIdMessage.get();
                messageParent.setConfirmStatus(AppConstant.APP_TRUE);
                messageParent.setIdConfirm(userPrincipal.getId());
                messageParent.setParentUnread(AppConstant.APP_FALSE);
                messageParentRepository.save(messageParent);
                Long idWebSystem = AppTypeConstant.SCHOOL.equals(userPrincipal.getAppType()) ? 24L : 23L;
                firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, messageParent.getKids(), FirebaseConstant.CATEGORY_MESSAGE);
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateMessage(Long idSchoolLogin, UserPrincipal principal, ContentRequest request) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        MessageParent oldMessage = messageParentRepository.findById(request.getId()).orElseThrow();
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
            if (oldMessage.getIdSchoolReply() == null) {
                //gửi firebase
                firebaseFunctionService.sendParentByPlus(26L, oldMessage.getKids(), FirebaseConstant.CATEGORY_MESSAGE, request.getContent());
            }
            oldMessage.setSchoolTimeReply(LocalDateTime.now());
            oldMessage.setSchoolReply(request.getContent());
            oldMessage.setSchoolModifyStatus(oldMessage.getIdSchoolReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            oldMessage.setIdSchoolReply(principal.getId());
        } else if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
            if (oldMessage.getIdTeacherReply() == null) {
                //gửi firebase
                firebaseFunctionService.sendParentByTeacher(25L, oldMessage.getKids(), FirebaseConstant.CATEGORY_MESSAGE, request.getContent());
            }
            oldMessage.setTeacherTimeReply(LocalDateTime.now());
            oldMessage.setTeacherReply(request.getContent());
            oldMessage.setTeacherModifyStatus(oldMessage.getIdTeacherReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            oldMessage.setIdTeacherReply(principal.getId());
        }
        oldMessage.setParentUnread(AppConstant.APP_FALSE);
        if (oldMessage.getIdConfirm() == null) {
            this.setConfirm(principal, oldMessage, ButtonConstant.BUTTON_SAVE);
        }
        messageParentRepository.save(oldMessage);
        return true;
    }

    @Transactional
    @Override
    public boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        MessageParent messageParent = messageParentRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        this.setConfirm(principal, messageParent, ButtonConstant.BUTTON_CONFIRM);
        messageParentRepository.save(messageParent);
        Long idWebSystem = AppTypeConstant.SCHOOL.equals(principal.getAppType()) ? 24L : 23L;
        firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, messageParent.getKids(), FirebaseConstant.CATEGORY_MESSAGE);
        return true;
    }

    @Override
    public boolean revokeTeacher(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkPlusOrTeacher(principal);
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        messageParent.setTeacherReplyDel(request.getStatus());
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParentRepository.save(messageParent);
        return true;
    }

    @Override
    public boolean revokePlus(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkDataPlus(principal);
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        messageParent.setSchoolReplyDel(request.getStatus());
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParentRepository.save(messageParent);
        return true;
    }

    @Override
    public MessageNewResponse findByIdMessageNew(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        MessageParent oldMessage = messageParentRepository.findById(id).orElseThrow();
        oldMessage.setTeacherUnread(AppConstant.APP_TRUE);
        messageParentRepository.save(oldMessage);
        return modelMapper.map(oldMessage, MessageNewResponse.class);
    }


    private void setConfirm(UserPrincipal principal, MessageParent messageParent, String type) throws FirebaseMessagingException {
        messageParent.setConfirmStatus(AppConstant.APP_TRUE);
        messageParent.setIdConfirm(principal.getId());
        messageParent.setConfirmDate(LocalDateTime.now());
        messageParent.setConfirmType(principal.getAppType());
        if (type.equals(ButtonConstant.BUTTON_CONFIRM)) {
            messageParent.setConfirmContent(principal.getAppType().equals(AppTypeConstant.SCHOOL) ? ParentDairyConstant.CONTENT_CONFIRM_SCHOOL : ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
        }
    }

}


