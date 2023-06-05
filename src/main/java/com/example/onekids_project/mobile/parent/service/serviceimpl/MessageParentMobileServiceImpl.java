package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.entity.parent.MessageParentAttachFile;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseDataService;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.parent.request.messageparent.MessageParentMobileRequest;
import com.example.onekids_project.mobile.parent.response.message.ListMessageParentMobileResponse;
import com.example.onekids_project.mobile.parent.response.message.MessageParentDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.message.MessageParentMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.MessageParentMobileService;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.model.firebase.ContentFirebaseModel;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.MessageParentAttachFileRepository;
import com.example.onekids_project.repository.MessageParentRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.FirebaseUtils;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageParentMobileServiceImpl implements MessageParentMobileService {

    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MessageParentAttachFileRepository messageParentAttachFileRepository;

    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMessageParentMobileResponse findMessageParentMobile(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        List<MessageParent> messageParentList = messageParentRepository.findMessageParentMobile(idSchool, idKid, pageable, localDateTime);
        ListMessageParentMobileResponse listMessageParentResponse = new ListMessageParentMobileResponse();
        List<MessageParentMobileResponse> messageParentMobileResponse = new ArrayList<>();
        messageParentList.forEach(x -> {
            MessageParentMobileResponse model = new MessageParentMobileResponse();
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
            model.setReplyNumber(replyNumber);
            model.setPictureNumber(x.getMessageParentAttachFileList().size());
            model.setCreatedDate(x.getCreatedDate());
            model.setParentUnread(x.isParentUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            messageParentMobileResponse.add(model);
        });
        long countAll = messageParentRepository.getCountMessage(idSchool, idKid, localDateTime);
        boolean checkLastPage = countAll <= 20;

        listMessageParentResponse.setMessageList(messageParentMobileResponse);
        listMessageParentResponse.setLastPage(checkLastPage);
        return listMessageParentResponse;
    }

    @Override
    public boolean messageParentRevoke(Long id) {
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        if (messageParent.isConfirmStatus()) {
            return false;
        }
        messageParent.setParentMessageDel(AppConstant.APP_TRUE);
        messageParentRepository.save(messageParent);
        return true;
    }

    @Override
    public MessageParentDetailMobileResponse findMessParentDetailMobile(UserPrincipal principal, Long id) {
        MessageParent messageParent = messageParentRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        MessageParentDetailMobileResponse model = new MessageParentDetailMobileResponse();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(messageParent.getIdCreated()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        model.setFullName(maUser.getFullName());
        model.setContent(messageParent.getMessageContent());
        model.setConfirmStatus(messageParent.isConfirmStatus());
        if (messageParent.isSchoolReplyDel() == AppConstant.APP_TRUE && messageParent.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(messageParent.getCreatedDate());
            model.setContent(messageParent.getMessageContent());
            model.setConfirmStatus(messageParent.isConfirmStatus());
            model.setSchoolModifyStatus(messageParent.isSchoolModifyStatus());
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply1(principal, messageParent, model); // nha truong thu hoi, giao vien k thu hoi
        } else if (messageParent.isSchoolReplyDel() == AppConstant.APP_TRUE && messageParent.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(messageParent.getCreatedDate());
            model.setContent(messageParent.getMessageContent());
            model.setConfirmStatus(messageParent.isConfirmStatus());
            model.setSchoolModifyStatus(messageParent.isSchoolModifyStatus());
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply2(principal, messageParent, model); // ca 2 thu hoi
        } else if (messageParent.isSchoolReplyDel() == AppConstant.APP_FALSE && messageParent.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(messageParent.getCreatedDate());
            model.setConfirmStatus(messageParent.isConfirmStatus());
            model.setSchoolModifyStatus(messageParent.isSchoolModifyStatus());
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply3(principal, messageParent, model); // giao vien thu hoi
        } else if (messageParent.isSchoolReplyDel() == AppConstant.APP_FALSE && messageParent.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(messageParent.getCreatedDate());
            model.setConfirmStatus(messageParent.isConfirmStatus());
            model.setSchoolModifyStatus(messageParent.isSchoolModifyStatus());
            model.setPictureList(messageParent.getMessageParentAttachFileList().stream().map(MessageParentAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply(principal, messageParent, model);
        }
        messageParent.setParentUnread(AppConstant.APP_TRUE);
        messageParentRepository.save(messageParent);
        return model;
    }

    private void setReply3(UserPrincipal principal, MessageParent messageParent, MessageParentDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (messageParent.getIdConfirm() != null && messageParent.isConfirmStatus() == AppConstant.APP_TRUE && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType()) && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow();
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(messageParent.getConfirmContent());
            replyMobileObject.setCreatedDate(messageParent.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }
        //
        //nhà trường phản hồi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply()) && !messageParent.isSchoolReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(messageParent.getSchoolReply());
            replyMobileObject.setCreatedDate(messageParent.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên thu hồi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow();
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(messageParent.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    private void setReply1(UserPrincipal principal, MessageParent messageParent, MessageParentDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (messageParent.getIdConfirm() != null && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            String typeConfirm = messageParent.getConfirmType();
            MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow();
            if (AppTypeConstant.SCHOOL.equals(typeConfirm)) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(typeConfirm) && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(messageParent.getConfirmContent());
            replyMobileObject.setCreatedDate(messageParent.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }
        if (messageParent.getIdSchoolReply() != null) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(messageParent.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (messageParent.getIdTeacherReply() != null) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow();
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(messageParent.getTeacherReply());
            replyMobileObject.setCreatedDate(messageParent.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    private void setReply2(UserPrincipal principal, MessageParent messageParent, MessageParentDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (messageParent.getIdConfirm() != null && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType()) && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(messageParent.getConfirmContent());
            replyMobileObject.setCreatedDate(messageParent.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }
        //nhà trường thu hoi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply()) && messageParent.isSchoolReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(messageParent.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên thu hoi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply()) && messageParent.isTeacherReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(messageParent.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);
    }

    @Transactional
    @Override
    public boolean createMessageParent(UserPrincipal principal, MessageParentMobileRequest messageParentMobileRequest) throws FirebaseMessagingException, IOException {
        if (!CollectionUtils.isEmpty(messageParentMobileRequest.getMultipartFileList()) && messageParentMobileRequest.getMultipartFileList().size() > 3) {
            return false;
        }
        Long idSchool = principal.getIdSchoolLogin();
        MessageParent messageParent = new MessageParent();
        messageParent.setMessageContent(messageParentMobileRequest.getContent());
        messageParent.setCreatedBy(principal.getFullName());
        messageParent.setIdSchool(idSchool);
        messageParent.setTeacherUnread(AppConstant.APP_FALSE);
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        messageParent.setIdGrade(kids.getIdGrade());
        messageParent.setIdClass(kids.getMaClass().getId());
        messageParent.setKids(kids);
        MessageParent messageParentSaved = messageParentRepository.save(messageParent);
        if (!CollectionUtils.isEmpty(messageParentMobileRequest.getMultipartFileList())) {
            this.addFile(idSchool, messageParentSaved, messageParentMobileRequest.getMultipartFileList());
        }
        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(22L, kids, messageParentMobileRequest.getContent(), FirebaseConstant.CATEGORY_MESSAGE);
        firebaseFunctionService.sendTeacherByKids(22L, kids, messageParentMobileRequest.getContent(), FirebaseConstant.CATEGORY_MESSAGE);
        return true;
    }

    private void setReply(UserPrincipal principal, MessageParent messageParent, MessageParentDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (messageParent.getIdConfirm() != null && messageParent.isConfirmStatus() && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            if (AppTypeConstant.SCHOOL.equals(messageParent.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(messageParent.getConfirmType()) && StringUtils.isNotBlank(messageParent.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(messageParent.getIdConfirm()).orElseThrow();
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(messageParent.getConfirmContent());
            replyMobileObject.setCreatedDate(messageParent.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }

        //nhà trường phản hồi
        if (messageParent.getIdSchoolReply() != null && StringUtils.isNotBlank(messageParent.getSchoolReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdSchoolReply()).orElseThrow();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(messageParent.getSchoolReply());
            replyMobileObject.setCreatedDate(messageParent.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (messageParent.getIdTeacherReply() != null && StringUtils.isNotBlank(messageParent.getTeacherReply()) && !messageParent.isTeacherReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(messageParent.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(messageParent.getTeacherReply());
            replyMobileObject.setCreatedDate(messageParent.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(messageParent.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);
    }

    private void addFile(Long idSchool, MessageParent messageParent, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.LOI_NHAN);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MessageParentAttachFile messageParentAttachFile = new MessageParentAttachFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.LOI_NHAN) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.LOI_NHAN) + fileName;
                messageParentAttachFile.setUrl(urlWeb);
                messageParentAttachFile.setName(multipartFile.getOriginalFilename());
                messageParentAttachFile.setUrlLocal(urlLocal);
                messageParentAttachFile.setMessageParent(messageParent);
                messageParentAttachFileRepository.save(messageParentAttachFile);
            });
        }
    }

}
