package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.parent.request.FeedBackParentRequest;
import com.example.onekids_project.mobile.parent.response.feedback.FeedBackDetailParentResponse;
import com.example.onekids_project.mobile.parent.response.feedback.FeedBackParentResponse;
import com.example.onekids_project.mobile.parent.response.feedback.ListFeedBackParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.FeedBackParentService;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.repository.FeedBackFileRepository;
import com.example.onekids_project.repository.FeedBackRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FirebaseUtils;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedBackParentServiceImpl implements FeedBackParentService {
    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private FeedBackFileRepository feedBackFileRepository;

    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListFeedBackParentResponse findFeedBackList(UserPrincipal principal, LocalDateTime localDateTime) {
        List<FeedBack> feedBackList = feedBackRepository.findFeedBackParentList(principal.getIdSchoolLogin(), principal.getIdKidLogin(), localDateTime);
        ListFeedBackParentResponse listFeedBackParentResponse = new ListFeedBackParentResponse();
        List<FeedBackParentResponse> dataList = new ArrayList<>();
        feedBackList.forEach(x -> {
            FeedBackParentResponse model = new FeedBackParentResponse();
            model.setId(x.getId());
            model.setTitle(x.getFeedbackTitle().length() < 50 ? x.getFeedbackTitle() : x.getFeedbackTitle().substring(0, 50));
            model.setContent(x.getFeedbackContent().length() < 50 ? x.getFeedbackContent() : x.getFeedbackContent().substring(0, 50));
            int replyNumber = 0;
            if (StringUtils.isNotBlank(x.getConfirmContent())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getSchoolReply())) {
                replyNumber++;
            }
            model.setReplyNumber(replyNumber);
            model.setPictureNumber(x.getFeedBackFileList().size());
            model.setConfirmStatus(x.isSchoolConfirmStatus());
            model.setCreatedDate(x.getCreatedDate());
            model.setRead(x.isParentUnread());
            dataList.add(model);
        });
        long countAll = feedBackRepository.getCountParent(principal.getIdSchoolLogin(), principal.getIdKidLogin(), localDateTime);
        boolean checkLastPage = countAll < MobileConstant.MAX_PAGE_ITEM;
        listFeedBackParentResponse.setFeedbackList(dataList);
        listFeedBackParentResponse.setLastPage(checkLastPage);
        return listFeedBackParentResponse;
    }

    @Override
    public FeedBackDetailParentResponse findFeedBackDetail(UserPrincipal principal, Long id) {
        Long idSchool = principal.getIdSchoolLogin();
        FeedBack feedBack = feedBackRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow();
        FeedBackDetailParentResponse model = new FeedBackDetailParentResponse();
        model.setTitle(feedBack.getFeedbackTitle());
        model.setContent(feedBack.getFeedbackContent());
        model.setConfirmStatus(feedBack.isSchoolConfirmStatus());
        model.setCreatedDate(feedBack.getCreatedDate());
        model.setPictureList(feedBack.getFeedBackFileList().stream().map(FeedBackFile::getAttachPicture).collect(Collectors.toList()));
        this.setUserCreated(feedBack, model);
        this.setReply(feedBack, model, idSchool);
        feedBack.setParentUnread(AppConstant.APP_TRUE);
        feedBackRepository.save(feedBack);
        return model;
    }


    @Transactional
    @Override
    public boolean createFeedBackParent(UserPrincipal principal, FeedBackParentRequest feedBackParentRequest) throws FirebaseMessagingException, IOException {
        FeedBack feedBack = new FeedBack();
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        feedBack.setFeedbackTitle(feedBackParentRequest.getTitle());
        feedBack.setFeedbackContent(feedBackParentRequest.getContent());
        feedBack.setCreatedBy(principal.getFullName());
        feedBack.setAccountType(FeedBackConstant.PARENT);
        feedBack.setHiddenStatus(feedBackParentRequest.getHiddenStatus());
        feedBack.setIdKid(idKid);
        feedBack.setIdSchool(idSchool);
        FeedBack feedBackSaved = feedBackRepository.save(feedBack);
        //lưu ảnh nếu có
        if (CollectionUtils.isNotEmpty(feedBackParentRequest.getPictureList())) {
            for (MultipartFile multipartFile : feedBackParentRequest.getPictureList()) {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.GOP_Y);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                FeedBackFile feedBackFile = new FeedBackFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.GOP_Y) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.GOP_Y) + fileName;
                feedBackFile.setAttachPicture(urlWeb);
                feedBackFile.setName(multipartFile.getOriginalFilename());
                feedBackFile.setUrlLocalPicture(urlLocal);
                feedBackFile.setFeedBack(feedBackSaved);
                feedBackFileRepository.save(feedBackFile);
            }
        }
        // send firebase
        firebaseFunctionService.sendPlusByKids(54L, kids, feedBackParentRequest.getTitle(), FirebaseConstant.CATEGORY_FEEDBACK);
        return true;
    }
    private void setUserCreated(FeedBack feedBack, FeedBackDetailParentResponse model) {
        //ẩn danh
        if (feedBack.isHiddenStatus()) {
            model.setFullName(AppConstant.ANONYMOUS);
            model.setAvatar(AvatarDefaultConstant.AVATAR_PARENT);
        } else {
            MaUser maUser = maUserRepository.findById(feedBack.getIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            model.setFullName(maUser.getFullName());
            model.setAvatar(ConvertData.getAvatarUserSchool(maUser));
        }
    }

    private void setReply(FeedBack feedBack, FeedBackDetailParentResponse model, Long idSchool) {
        List<ReplyMobileObject> replyList = new ArrayList<>();
        //có xác nhận
        if (StringUtils.isNotBlank(feedBack.getConfirmContent())) {
            ReplyMobileObject reply = new ReplyMobileObject();
            reply.setContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
            reply.setFullName(MobileConstant.SCHOOL);
            reply.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            reply.setCreatedDate(feedBack.getConfirmDate());
            replyList.add(reply);
        }
        //có phản hồi
        if (StringUtils.isNotBlank(feedBack.getSchoolReply())) {
            ReplyMobileObject reply = new ReplyMobileObject();
            reply.setContent(feedBack.isSchoolReplyDel() ? MobileConstant.CONTENT_DEL : feedBack.getSchoolReply());
            reply.setFullName(MobileConstant.SCHOOL);
            reply.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            reply.setCreatedDate(feedBack.getConfirmDate());
            reply.setModifyStatus(feedBack.isSchoolModifyStatus());
            replyList.add(reply);
        }
        model.setReplyList(replyList);
    }

}
