package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.entity.parent.MessageParentAttachFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.MessagePlusMobileService;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.MessageParentRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagePlusMobileServiceImpl implements MessagePlusMobileService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private MessageParentRepository messageParentRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMessagePlusResponse searchMessagePlus(UserPrincipal principal, SearchMessagePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<MessageParent> messageTeacherList = messageParentRepository.findMessageforPlus(idSchool, request);
        ListMessagePlusResponse listMessagePlusResponse = new ListMessagePlusResponse();
        List<MessagePlusResponse> messageTeacherResponseList = new ArrayList<>();
        messageTeacherList.forEach(x -> {
            MessagePlusResponse model = new MessagePlusResponse();
            model.setKidName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setId(x.getId());
            model.setContent(StringDataUtils.getSubStringLarge(x.getMessageContent()));
            int replyNumber = 0;
            if (StringUtils.isNotBlank(x.getConfirmContent())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getSchoolReply())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getTeacherReply())) {
                replyNumber++;
            }
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setReplyNumber(replyNumber);
            model.setPictureNumber(x.getMessageParentAttachFileList().size());
            model.setSchoolUnread(x.isTeacherUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            messageTeacherResponseList.add(model);
        });
        boolean lastPage = messageTeacherList.size() < MobileConstant.MAX_PAGE_ITEM;
        listMessagePlusResponse.setDataList(messageTeacherResponseList);
        listMessagePlusResponse.setLastPage(lastPage);
        return listMessagePlusResponse;
    }

    @Override
    public MessagePlusDetailResponse findDeTailPlusMessage(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        MessageParent messageParent = messageParentRepository.findById(id).orElseThrow();
        MessagePlusDetailResponse model = new MessagePlusDetailResponse();
        MaClass maClass = maClassRepository.findById(messageParent.getIdClass()).orElseThrow();
        model.setKidName(messageParent.getKids().getFullName());
        model.setClassName(maClass.getClassName());
        model.setAvatarkid(ConvertData.getAvatarKid(messageParent.getKids()));
        model.setContent(messageParent.getMessageContent());
        model.setParentName(UserInforUtils.getFullName(messageParent.getIdCreated()));
        model.setAvartarParent(ConvertData.getAvatarUserSchool(UserInforUtils.getMaUser(messageParent.getIdCreated())));
        model.setConfirmStatus(messageParent.isConfirmStatus());
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getCreatedDate()));
        model.setCheckSchoolReply(messageParent.getIdSchoolReply() != null);
        model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
        messageParent.setTeacherUnread(AppConstant.APP_TRUE);
        messageParentRepository.save(messageParent);
        this.setReply(principal, messageParent, model);
        return model;
    }

    // findDetail
    @Transactional
    @Override
    public MessagePlusConfirmResponse messagePlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        MessagePlusConfirmResponse model = new MessagePlusConfirmResponse();
        MessageParent messageParent = messageParentRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        messageParent.setConfirmStatus(AppConstant.APP_TRUE);
        messageParent.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_SCHOOL);
        messageParent.setConfirmDate(LocalDateTime.now());
        messageParent.setIdConfirm(principal.getId());
        messageParent.setConfirmType(AppTypeConstant.SCHOOL);
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParentRepository.save(messageParent);
        this.setReplyConfirmA(principal, messageParent, model);
        //gửi firebase
        firebaseFunctionService.sendParentByPlusNoContent(24L, messageParent.getKids(), FirebaseConstant.CATEGORY_MESSAGE);
        return model;
    }


    @Transactional
    @Override
    public MessagePlusSendReplyResponse sendPlusReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        MessagePlusSendReplyResponse model = new MessagePlusSendReplyResponse();
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(updatePlusSendReplyRequest.getId()).orElseThrow();
        if (messageParent.getIdSchoolReply() == null) {
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(26L, messageParent.getKids(), FirebaseConstant.CATEGORY_MESSAGE, updatePlusSendReplyRequest.getSchoolReply());
        }
        if (StringUtils.isBlank(messageParent.getConfirmContent())) {
            messageParent.setConfirmStatus(AppConstant.APP_TRUE);
            messageParent.setConfirmType(AppTypeConstant.SCHOOL);
            messageParent.setConfirmDate(LocalDateTime.now());
            messageParent.setIdConfirm(principal.getId());
        }
        messageParent.setIdSchoolReply(principal.getId());
        messageParent.setSchoolTimeReply(LocalDateTime.now());
        messageParent.setSchoolModifyStatus(messageParent.getSchoolReply() == null ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
        messageParent.setSchoolReplyDel(AppConstant.APP_FALSE);
        model.setId(updatePlusSendReplyRequest.getId());
        model.setSchoolReply(updatePlusSendReplyRequest.getSchoolReply());
        messageParent.setSchoolReply(updatePlusSendReplyRequest.getSchoolReply());
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParent.setTeacherUnread(AppConstant.APP_TRUE);
        messageParentRepository.save(messageParent);
        this.setReplySend(principal, messageParent, model);
        return model;
    }


    @Override
    public MessagePlusRevokeResponse sendRevoke(Long idSchoolLogin, UserPrincipal principal, UpdatePlusRevokeRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        MessagePlusRevokeResponse model = new MessagePlusRevokeResponse();
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (request.getKeyType().equals(MobileConstant.TYPE_TEACHER) && StringUtils.isNotBlank(messageParent.getTeacherReply())) {
            messageParent.setTeacherReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokeTeacher(idSchool, messageParent, model);
        } else if (request.getKeyType().equals(MobileConstant.TYPE_SCHOOL) && StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            messageParent.setSchoolReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokePlus(principal, messageParent, model);
        }
        messageParent.setDefaultContentDel(ParentDairyConstant.CONTENT_DEL);
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParentRepository.save(messageParent);
        return model;
    }

    private void setReplyRevokeTeacher(Long idSchool, MessageParent messageParent, MessagePlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_TEACHER);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_TRUE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyRevokePlus(UserPrincipal principal, MessageParent messageParent, MessagePlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(messageParent.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_TRUE);
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplySend(UserPrincipal principal, MessageParent messageParent, MessagePlusSendReplyResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(messageParent.getSchoolReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(messageParent.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyConfirmA(UserPrincipal principal, MessageParent messageParent, MessagePlusConfirmResponse model) {
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setKeyType("Confirm");
        replyMobileDateObject.setContent(messageParent.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, MessageParent messageParent, MessagePlusDetailResponse
            model) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        //khi có Xác nhận
        if (StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            replyMobileDateObjectList.add(this.setReplyConfirm(idSchool, messageParent));
        }
        // nhà trường phản hồi
        if (StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            replyMobileDateObjectList.add(this.setReplySchool(principal, messageParent));
        }
        // giáo viên phản hồi
        if (StringUtils.isNotBlank(messageParent.getTeacherReply())) {
            replyMobileDateObjectList.add(this.setReplyTeacher(idSchool, messageParent));
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private ReplyMobilePlusObject setReplyConfirm(Long idSchool, MessageParent messageParent) {
        MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setKeyType("");
        replyMobileDateObject.setContent(messageParent.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
        replyMobileDateObject.setSortDate(messageParent.getConfirmDate());
        return replyMobileDateObject;
    }

    private ReplyMobilePlusObject setReplySchool(UserPrincipal principal, MessageParent messageParent) {
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(messageParent.getIdSchoolReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(messageParent.isSchoolReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : messageParent.getSchoolReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(messageParent.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setSortDate(messageParent.getSchoolTimeReply());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
        return replyMobileDateObject;

    }

    private ReplyMobilePlusObject setReplyTeacher(Long idSchool, MessageParent messageParent) {
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(messageParent.isTeacherReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : messageParent.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_TRUE);
        replyMobileDateObject.setSortDate(messageParent.getTeacherTimeReply());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_TEACHER);
        replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
        return replyMobileDateObject;

    }

}
