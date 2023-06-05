package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.entity.parent.MessageParentAttachFile;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.teacher.request.UpdateTeacherReplyRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.UpdateTeacherSendReplyRequest;
import com.example.onekids_project.mobile.teacher.response.message.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.MessageTeacherMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageTeacherMobileServiceImpl implements MessageTeacherMobileService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private MessageParentRepository messageParentRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMessageTeacherResponse searchMessageTeache(UserPrincipal principal, SearchMessageTeacherRequest searchMessageTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        Long idClass = principal.getIdClassLogin();
        List<MessageParent> messageTeacherList = messageParentRepository.findMessageforTeacherMobile(idSchool, idClass, searchMessageTeacherRequest);
        ListMessageTeacherResponse listMessageTeacherResponse = new ListMessageTeacherResponse();
        List<MessageTeacherResponse> messageTeacherResponseList = new ArrayList<>();
        messageTeacherList.forEach(x -> {
            MessageTeacherResponse model = new MessageTeacherResponse();
            model.setFullName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setId(x.getId());
            String content = x.getMessageContent().length() < 100 ? x.getMessageContent() : x.getMessageContent().substring(0, 100);
            model.setContent(content);
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
            model.setTeacherUnread(x.isTeacherUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            messageTeacherResponseList.add(model);
        });
        boolean lastPage = messageTeacherList.size() < MobileConstant.MAX_PAGE_ITEM;
        listMessageTeacherResponse.setDataList(messageTeacherResponseList);
        listMessageTeacherResponse.setLastPage(lastPage);
        return listMessageTeacherResponse;
    }

    @Override
    public MessageTeacherDetailResponse findDetailMessageTeacher(UserPrincipal principal, Long id) {
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        MessageTeacherDetailResponse model = new MessageTeacherDetailResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(messageParent.getKids().getId()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(messageParent.getIdCreated()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(messageParent.getIdClass()).orElseThrow(() -> new NotFoundException("not found id class"));
        model.setFullName(kids.getFullName());
        model.setClassName(maClass.getClassName());
        model.setContent(messageParent.getMessageContent());
        model.setParentName(maUser.getFullName());
        model.setConfirmStatus(messageParent.isConfirmStatus());
        model.setCheckTeacherReply(messageParent.getIdTeacherReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        if (messageParent.isSchoolReplyDel() == AppConstant.APP_TRUE && messageParent.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getCreatedDate()));
            model.setContent(messageParent.getMessageContent());
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply(principal, messageParent, model);
        } else if (messageParent.isSchoolReplyDel() == AppConstant.APP_TRUE && messageParent.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getCreatedDate()));
            model.setContent(messageParent.getMessageContent());
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply2(principal, messageParent, model);
        } else if (messageParent.isSchoolReplyDel() == AppConstant.APP_FALSE && messageParent.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getCreatedDate()));
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply3(principal, messageParent, model);
        } else if (messageParent.isSchoolReplyDel() == AppConstant.APP_FALSE && messageParent.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getCreatedDate()));
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply1(principal, messageParent, model);
        }
        messageParent.setTeacherUnread(AppConstant.APP_TRUE);
        messageParentRepository.save(messageParent);
        return model;
    }

    @Override
    public MessageTeacherRevokeResponse messageTeacherRevoke(UserPrincipal principal, Long id) {
        MessageTeacherRevokeResponse model = new MessageTeacherRevokeResponse();
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        messageParent.setTeacherReplyDel(AppConstant.APP_TRUE);
        messageParent.setDefaultContentDel(ParentDairyConstant.CONTENT_DEL);
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParentRepository.save(messageParent);
        this.setReplyRevoke(principal, messageParent, model);
        return model;
    }

    private void setReplyRevoke(UserPrincipal principal, MessageParent messageParent, MessageTeacherRevokeResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(messageParent.getDefaultContentDel());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(LocalDateTime.now()));
        replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
        if (messageParent.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    @Transactional
    @Override
    public MessageTeacheConfirmResponse messageTeacherConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        MessageTeacheConfirmResponse model = new MessageTeacheConfirmResponse();
        MessageParent messageParent = messageParentRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        messageParent.setConfirmStatus(AppConstant.APP_TRUE);
        messageParent.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
        messageParent.setConfirmDate(LocalDateTime.now());
        messageParent.setIdConfirm(principal.getId());
        messageParent.setConfirmType(AppTypeConstant.TEACHER);
        messageParent.setParentUnread(AppConstant.APP_FALSE);
        messageParentRepository.save(messageParent);
        this.setReplyConfirm(principal, messageParent, model);
        //gửi firebase
        firebaseFunctionService.sendParentByTeacherNoContent(23L, messageParent.getKids(), FirebaseConstant.CATEGORY_MESSAGE, UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal).getFullName());
        return model;
    }

    //    fireBasse
    private void sendFireBase(Kids kids, String title, String content) throws FirebaseMessagingException {
        List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
        if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
            NotifyRequest notifyRequest = new NotifyRequest();
            notifyRequest.setBody(content);
            notifyRequest.setTitle(title);
            FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.MESSAGE, notifyRequest, kids.getId().toString());
        }
    }


    private MessageTeacheConfirmResponse setReplyConfirm(UserPrincipal principal, MessageParent messageParent, MessageTeacheConfirmResponse model) throws FirebaseMessagingException {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(messageParent.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
        replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

        return model;
    }

    @Transactional
    @Override
    public MessageTeacherSendReplyResponse updateTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyRequest updateTeacherReplyRequest) throws FirebaseMessagingException {
        MessageTeacherSendReplyResponse model = new MessageTeacherSendReplyResponse();
        Optional<MessageParent> messageParentOptional = messageParentRepository.findById(updateTeacherReplyRequest.getId());
        if (messageParentOptional.isEmpty()) {
            return null;
        }
        Long id = updateTeacherReplyRequest.getId();
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        MessageParent oldMessage = messageParentOptional.get();
        oldMessage.setTeacherReplyDel(AppConstant.APP_FALSE);
        oldMessage.setTeacherModifyStatus(AppConstant.APP_TRUE);
        oldMessage.setParentUnread(AppConstant.APP_FALSE);
        oldMessage.setTeacherTimeReply(LocalDateTime.now());
        modelMapper.map(updateTeacherReplyRequest, oldMessage);
        model.setId(updateTeacherReplyRequest.getId());
        model.setTeacherReply(updateTeacherReplyRequest.getTeacherReply());
        MessageParent newMessage = messageParentRepository.save(oldMessage);
//        this.setReplyUpdate(principal, messageParent, model);
        this.setReplyUpdate(principal, newMessage, model);
        MessageTeacherSendReplyResponse messageTeacherSendReplyResponse = modelMapper.map(newMessage, MessageTeacherSendReplyResponse.class);
        return model;
    }

    private void setReplyUpdate(UserPrincipal principal, MessageParent messageParent, MessageTeacherSendReplyResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(messageParent.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
        if (messageParent.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    @Transactional
    @Override
    public MessageTeacherSendReplyResponse sendTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherSendReplyRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException {
        MessageTeacherSendReplyResponse model = new MessageTeacherSendReplyResponse();
        Optional<MessageParent> messageParentOptional = messageParentRepository.findById(updateTeacherSendReplyRequest.getId());
        if (messageParentOptional.isEmpty()) {
            return null;
        }
        Long id = updateTeacherSendReplyRequest.getId();
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        MessageParent oldMessage = messageParentOptional.get();
        modelMapper.map(updateTeacherSendReplyRequest, oldMessage);
        if (oldMessage.getIdConfirm() == null) {
            oldMessage.setConfirmStatus(AppConstant.APP_TRUE);
            oldMessage.setConfirmType(AppTypeConstant.TEACHER);
            oldMessage.setConfirmDate(LocalDateTime.now());
            oldMessage.setIdConfirm(principal.getId());
            oldMessage.setTeacherReply(updateTeacherSendReplyRequest.getTeacherReply());
            oldMessage.setTeacherTimeReply(LocalDateTime.now());
            oldMessage.setIdTeacherReply(principal.getId());
            oldMessage.setParentUnread(AppConstant.APP_FALSE);
        } else {
            oldMessage.setTeacherTimeReply(LocalDateTime.now());
            oldMessage.setIdTeacherReply(principal.getId());
            oldMessage.setTeacherModifyStatus(AppConstant.APP_FALSE);
            oldMessage.setParentUnread(AppConstant.APP_FALSE);
        }
        model.setId(updateTeacherSendReplyRequest.getId());
        model.setTeacherReply(updateTeacherSendReplyRequest.getTeacherReply());
        messageParentRepository.save(oldMessage);
        this.setReplySend(principal, messageParent, model);
        //gửi firebase
        firebaseFunctionService.sendParentByTeacher(25L, messageParent.getKids(), FirebaseConstant.CATEGORY_MESSAGE, updateTeacherSendReplyRequest.getTeacherReply());
        return model;
    }

    private void setReplySend(UserPrincipal principal, MessageParent messageParent, MessageTeacherSendReplyResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(messageParent.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
        if (messageParent.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply3(UserPrincipal principal, MessageParent messageParent, MessageTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (messageParent.getIdConfirm() != null && messageParent.isConfirmStatus() == AppConstant.APP_TRUE && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }

        }
        //nhà trường phản hồi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(messageParent.getSchoolReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(messageParent.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
            if (messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(messageParent.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }

    private void setReply1(UserPrincipal principal, MessageParent messageParent, MessageTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        //khi có xác nhận
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        if (messageParent.getIdConfirm() != null && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường phản hồi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(messageParent.getSchoolReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(messageParent.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(messageParent.getTeacherReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
            if (messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(messageParent.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply2(UserPrincipal principal, MessageParent messageParent, MessageTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (messageParent.getIdConfirm() != null && messageParent.isConfirmStatus() && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường thu hoi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(messageParent.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply()) && messageParent.isTeacherReplyDel()) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
            if (messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(messageParent.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, MessageParent messageParent, MessageTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        //khi có xác nhận
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        if (messageParent.getIdConfirm() != null && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(messageParent.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường thu hoi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isSchoolReplyDel());
            replyMobileDateObject.setSortDate(messageParent.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(messageParent.getTeacherReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(messageParent.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(messageParent.isTeacherReplyDel());
            if (messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!messageParent.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(messageParent.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }

}
