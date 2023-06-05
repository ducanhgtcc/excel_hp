package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.response.ReplyTeacherObject;
import com.example.onekids_project.mobile.teacher.request.FeedBackTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.feedback.FeedBackDetailTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.feedback.FeedBackTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.feedback.ListFeedBackTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.FeedBackTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FirebaseUtils;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedBackTeacherServiceImpl implements FeedBackTeacherService {

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private FeedBackFileRepository feedBackFileRepository;

    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListFeedBackTeacherResponse findFeedBackList(UserPrincipal principal, PageNumberRequest pageNumberRequest) {
        CommonValidate.checkDataNoClassTeacher(principal);
        List<FeedBack> feedBackList = feedBackRepository.findFeedBackTeacherList(principal, pageNumberRequest);
        ListFeedBackTeacherResponse listFeedBackTeacherResponse = new ListFeedBackTeacherResponse();
        List<FeedBackTeacherResponse> dataList = new ArrayList<>();
        feedBackList.forEach(x -> {
            FeedBackTeacherResponse model = new FeedBackTeacherResponse();
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
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setRead(x.isParentUnread());
            dataList.add(model);

        });
        boolean lastPage = dataList.size() < MobileConstant.MAX_PAGE_ITEM;
        listFeedBackTeacherResponse.setDataList(dataList);
        listFeedBackTeacherResponse.setLastPage(lastPage);
        return listFeedBackTeacherResponse;
    }

    @Override
    public FeedBackDetailTeacherResponse findFeedBackDetail(UserPrincipal principal, Long id) {
        Long idSchool = principal.getIdSchoolLogin();
        FeedBack feedBack = feedBackRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow(() -> new NotFoundException("not found feedBack by id in teacher"));
        FeedBackDetailTeacherResponse model = new FeedBackDetailTeacherResponse();
        model.setTitle(feedBack.getFeedbackTitle());
        model.setContent(feedBack.getFeedbackContent());
        model.setConfirmStatus(feedBack.isSchoolConfirmStatus());
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getCreatedDate()));
        model.setPictureList(feedBack.getFeedBackFileList().stream().map(FeedBackFile::getAttachPicture).collect(Collectors.toList()));
        this.setUserCreated(feedBack, model);
        this.setReply(feedBack, model, idSchool);
        feedBack.setParentUnread(AppConstant.APP_TRUE);
        feedBackRepository.save(feedBack);
        return model;
    }

    @Transactional
    @Override
    public boolean createFeedBackTeacher(UserPrincipal principal, FeedBackTeacherRequest feedBackTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        FeedBack feedBack = new FeedBack();
        Long idSchool = principal.getIdSchoolLogin();
        feedBack.setFeedbackTitle(feedBackTeacherRequest.getTitle());
        feedBack.setFeedbackContent(feedBackTeacherRequest.getContent());
        feedBack.setCreatedBy(principal.getFullName());
        feedBack.setAccountType(FeedBackConstant.TEACHER);
        feedBack.setHiddenStatus(feedBackTeacherRequest.getHiddenStatus());
        feedBack.setIdSchool(idSchool);
        feedBack.setIdClass(principal.getIdClassLogin());
        FeedBack feedBackSaved = feedBackRepository.save(feedBack);
        //lưu ảnh nếu có
        if (CollectionUtils.isNotEmpty(feedBackTeacherRequest.getPictureList())) {
            feedBackTeacherRequest.getPictureList().forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.GOP_Y);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FeedBackFile feedBackFile = new FeedBackFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.GOP_Y) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.GOP_Y) + fileName;
                feedBackFile.setAttachPicture(urlWeb);
                feedBackFile.setName(multipartFile.getOriginalFilename());
                feedBackFile.setUrlLocalPicture(urlLocal);
                feedBackFile.setFeedBack(feedBackSaved);
                feedBackFileRepository.save(feedBackFile);
            });
        }
        // send firebase
        firebaseFunctionService.sendPlusByTeacher(57L, feedBackTeacherRequest.getTitle(), FirebaseConstant.FEEDBACK, idSchool);
        return true;
    }

    private void setUserCreated(FeedBack feedBack, FeedBackDetailTeacherResponse model) {
        //ẩn danh
        if (feedBack.isHiddenStatus()) {
            model.setFullName(AppConstant.ANONYMOUS);
            model.setAvatar(AvatarDefaultConstant.AVATAR_TEACHER);
        } else {
            MaUser maUser = maUserRepository.findById(feedBack.getIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            model.setFullName(maUser.getFullName());
            if (maUser.getEmployee().getInfoEmployeeSchoolList().get(0).getAvatar() != null) {
                model.setAvatar(maUser.getEmployee().getInfoEmployeeSchoolList().get(0).getAvatar());
            } else {
                model.setAvatar(AvatarDefaultConstant.AVATAR_TEACHER);
            }
        }
    }

    private void setReply(FeedBack feedBack, FeedBackDetailTeacherResponse model, Long idSchool) {
        List<ReplyTeacherObject> replyList = new ArrayList<>();
        //có xác nhận
        if (StringUtils.isNotBlank(feedBack.getConfirmContent())) {
            ReplyTeacherObject reply = new ReplyTeacherObject();
            reply.setContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
            reply.setFullName(MobileConstant.SCHOOL);
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(feedBack.getIdSchoolConfirm()).orElseThrow();
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getConfirmDate()));
            replyList.add(reply);
        }
        //có phản hồi
        if (StringUtils.isNotBlank(feedBack.getSchoolReply())) {
            ReplyTeacherObject reply = new ReplyTeacherObject();
            reply.setContent(feedBack.isSchoolReplyDel() ? MobileConstant.CONTENT_DEL : feedBack.getSchoolReply());
            reply.setFullName(MobileConstant.SCHOOL);
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(feedBack.getIdSchoolReply()).orElseThrow();
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(feedBack.getConfirmDate()));
            reply.setModifyStatus(feedBack.isSchoolModifyStatus());
            replyList.add(reply);
        }
        model.setReplyList(replyList);
    }
}
