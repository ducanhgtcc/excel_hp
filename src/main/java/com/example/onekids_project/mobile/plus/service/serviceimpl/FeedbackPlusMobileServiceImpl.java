package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackPlusRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackRevokeRequest;
import com.example.onekids_project.mobile.plus.response.feedbackplus.*;
import com.example.onekids_project.mobile.plus.response.video.ReplyPlusNewObject;
import com.example.onekids_project.mobile.plus.service.servicecustom.FeedbackPlusMobileService;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.repository.FeedBackRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.StringDataUtils;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
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
public class FeedbackPlusMobileServiceImpl implements FeedbackPlusMobileService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private FeedBackRepository feedBackRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListFeedbackPlusResponse searchFeedbackPlus(UserPrincipal principal, FeedbackPlusRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<FeedBack> feedBackList = feedBackRepository.findFeedBackPlus(idSchool, request);
        ListFeedbackPlusResponse listFeedbackPlusResponse = new ListFeedbackPlusResponse();
        List<FeedbackPlusResponse> feedbackPlusResponseList = new ArrayList<>();
        feedBackList.forEach(x -> {
            FeedbackPlusResponse model = new FeedbackPlusResponse();
            model.setId(x.getId());
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setContent(x.getFeedbackContent());
            int replyNumber = 0;
            if (StringUtils.isNotBlank(x.getConfirmContent())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getSchoolReply())) {
                replyNumber++;
            }
            model.setContent(StringDataUtils.getSubStringLarge(x.getFeedbackContent()));
            model.setTitle(StringDataUtils.getSubStringSmall(x.getFeedbackTitle()));
            model.setReplyNumber(replyNumber);
            model.setConfirmStatus(x.isSchoolConfirmStatus());
            model.setPictureNumber(x.getFeedBackFileList().size());
            model.setSchoolUnread(x.isSchoolUnread());
            feedbackPlusResponseList.add(model);
        });
        boolean lastPage = feedBackList.size() < MobileConstant.MAX_PAGE_ITEM;
        listFeedbackPlusResponse.setDataList(feedbackPlusResponseList);
        listFeedbackPlusResponse.setLastPage(lastPage);
        return listFeedbackPlusResponse;
    }

    @Override
    public FeedbackPlusDetailResponse findDetailFeedback(UserPrincipal principal, Long id) {
        Long idSchool = principal.getIdSchoolLogin();
        FeedBack feedBack = feedBackRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow();
        FeedbackPlusDetailResponse model = new FeedbackPlusDetailResponse();
        model.setTitle(feedBack.getFeedbackTitle());
        model.setContent(feedBack.getFeedbackContent());
        model.setConfirmStatus(feedBack.isSchoolConfirmStatus());
        model.setCheckSchoolReply(StringUtils.isNotBlank(feedBack.getSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getCreatedDate()));
        model.setPictureList(feedBack.getFeedBackFileList().stream().map(FeedBackFile::getAttachPicture).collect(Collectors.toList()));
        MaUser maUser = maUserRepository.findById(feedBack.getIdCreated()).orElseThrow();

        if (feedBack.isHiddenStatus()) {
            if (feedBack.getAccountType().equals(AppTypeConstant.SYSTEM) || feedBack.getAccountType().equals(AppTypeConstant.PARENT)) {
                model.setAvatar(AvatarDefaultConstant.AVATAR_PARENT);
            } else {
                model.setAvatar(AvatarDefaultConstant.AVATAR_TEACHER);
            }
            model.setCreatedName(AppConstant.HIDDEN);

        } else {
            model.setAvatar(feedBack.getAccountType().equals(AppTypeConstant.TEACHER) ? AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()) : ConvertData.getAvatarUserSchool(maUser));
            model.setCreatedName(maUser.getFullName());
        }
        model.setSchoolMoidifystatus(principal.getId().equals(feedBack.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        model.setSchoolReplyDel(feedBack.isSchoolReplyDel());
        feedBack.setSchoolUnread(AppConstant.APP_TRUE);
        feedBackRepository.save(feedBack);
        this.setReply(principal, feedBack, model, idSchool);
        return model;
    }

    @Override
    public FeedbackPlusRevokeResponse sendRevoke(UserPrincipal principal, FeedbackRevokeRequest request) {
        CommonValidate.checkDataPlus(principal);
        FeedbackPlusRevokeResponse model = new FeedbackPlusRevokeResponse();
        FeedBack feedBack = feedBackRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        feedBack.setSchoolReplyDel(AppConstant.APP_TRUE);
        feedBack.setParentUnread(AppConstant.APP_FALSE);
        feedBackRepository.save(feedBack);
        if (request.getKeyType().equals(AppConstant.TYPE_VIDEO_SCHOOL)) {
            this.setReplyRevoke(principal, feedBack, model);
        }
        return model;
    }

    @Transactional
    @Override
    public FeedbackPlusConfirmResponse feedbackPlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        FeedbackPlusConfirmResponse model = new FeedbackPlusConfirmResponse();
        FeedBack feedBack = feedBackRepository.findByIdAndSchoolConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        Kids kids = null;
        if (feedBack.getIdKid() != null) {
            kids = kidsRepository.findByIdAndDelActiveTrue(feedBack.getIdKid()).orElseThrow();
        }
        feedBack.setSchoolConfirmStatus(AppConstant.APP_TRUE);
        feedBack.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_SCHOOL);
        feedBack.setConfirmDate(LocalDateTime.now());
        feedBack.setIdSchoolConfirm(principal.getId());
        feedBack.setParentUnread(AppConstant.APP_FALSE);
        feedBackRepository.save(feedBack);
        this.setReplyConfirm(principal, feedBack, model);
        //loại góp ý của teacher
        if (kids == null) {
            InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromMaUser(feedBack.getIdCreated(), idSchoolLogin);
            if (infoEmployeeSchool.getAppType().equals(AppTypeConstant.TEACHER)) {
                //gửi firebase
                firebaseFunctionService.sendTeacherByPlusNoContent(58L, infoEmployeeSchool, FirebaseConstant.CATEGORY_FEEDBACK, idSchoolLogin);
            }
        } else {
            //loại góp ý của phụ huynh
            //gửi firebase
            firebaseFunctionService.sendParentByPlusNoContent(55L, kids, FirebaseConstant.CATEGORY_FEEDBACK);
        }
        return model;
    }

    @Transactional
    @Override
    public FeedbackPlusSendReplyResponse sendFeedbackReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        FeedbackPlusSendReplyResponse model = new FeedbackPlusSendReplyResponse();
        FeedBack feedBack = feedBackRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        boolean checkSendFirebase = feedBack.getIdSchoolReply() == null;
        if (StringUtils.isBlank(feedBack.getConfirmContent())) {
            feedBack.setSchoolConfirmStatus(AppConstant.APP_TRUE);
            feedBack.setConfirmDate(LocalDateTime.now());
            feedBack.setIdSchoolConfirm(principal.getId());
        }
        feedBack.setIdSchoolReply(principal.getId());
        feedBack.setSchoolTimeReply(LocalDateTime.now());
        feedBack.setSchoolReplyDel(AppConstant.APP_FALSE);
        feedBack.setSchoolModifyStatus(feedBack.getSchoolReply() == null ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
        model.setId(request.getId());
        model.setSchoolReply(request.getSchoolReply());
        feedBack.setSchoolReply(request.getSchoolReply());
        feedBack.setParentUnread(AppConstant.APP_FALSE);
        feedBackRepository.save(feedBack);
        this.setReplySend(principal, feedBack, model);
        if (checkSendFirebase) {
            Kids kids = feedBack.getIdKid() == null ? null : kidsRepository.findByIdAndDelActiveTrue(feedBack.getIdKid()).orElseThrow();
            //loại góp ý của teacher
            if (kids == null) {
                InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromMaUser(feedBack.getIdCreated(), idSchoolLogin);
                if (infoEmployeeSchool.getAppType().equals(AppTypeConstant.TEACHER)) {
                    //gửi firebase
                    firebaseFunctionService.sendTeacherByPlus(59L, infoEmployeeSchool, request.getSchoolReply(), FirebaseConstant.CATEGORY_FEEDBACK, idSchoolLogin);
                }
            } else {
                //loại góp ý của phụ huynh
                //gửi firebase
                firebaseFunctionService.sendParentByPlus(56L, kids, FirebaseConstant.CATEGORY_FEEDBACK, request.getSchoolReply());
            }
        }
        return model;
    }

    private void setReplySend(UserPrincipal principal, FeedBack feedBack, FeedbackPlusSendReplyResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(feedBack.getSchoolReply());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(feedBack.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setKeyType(AppConstant.TYPE_VIDEO_SCHOOL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(feedBack.isSchoolModifyStatus());
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyConfirm(UserPrincipal principal, FeedBack feedBack, FeedbackPlusConfirmResponse model) {
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setKeyType(AppConstant.TYPE_CONFIRM);
        replyMobileDateObject.setContent(feedBack.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getConfirmDate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyRevoke(UserPrincipal principal, FeedBack feedBack, FeedbackPlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(feedBack.isSchoolModifyStatus());
        replyMobileDateObject.setStatusDel(feedBack.isSchoolReplyDel());
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_TRUE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, FeedBack feedBack, FeedbackPlusDetailResponse model, Long idSchool) {
        List<ReplyPlusNewObject> replyList = new ArrayList<>();
        //có xác nhận
        if (StringUtils.isNotBlank(feedBack.getConfirmContent())) {
            ReplyPlusNewObject reply = new ReplyPlusNewObject();
            reply.setContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(feedBack.getIdSchoolConfirm()).orElseThrow();
            reply.setFullName(maUser.getFullName());
            reply.setSchoolMoidifystatus(AppConstant.APP_FALSE);
            reply.setStatusDel(feedBack.isSchoolReplyDel());
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            reply.setKeyType(AppConstant.TYPE_CONFIRM);
            reply.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getConfirmDate()));
            replyList.add(reply);
        }
        //có phản hồi
        if (StringUtils.isNotBlank(feedBack.getSchoolReply())) {
            ReplyPlusNewObject reply = new ReplyPlusNewObject();
            reply.setContent(feedBack.isSchoolReplyDel() ? MobileConstant.CONTENT_DEL : feedBack.getSchoolReply());
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(feedBack.getIdSchoolReply()).orElseThrow();
            reply.setFullName(maUser.getFullName());
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            reply.setKeyType(AppConstant.TYPE_VIDEO_SCHOOL);
            reply.setSchoolMoidifystatus(principal.getId().equals(feedBack.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            reply.setStatusDel(feedBack.isSchoolReplyDel());
            reply.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getConfirmDate()));
            reply.setModifyStatus(feedBack.isSchoolModifyStatus());
            replyList.add(reply);
        }
        model.setReplyList(replyList);
    }
}
