package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.FeedBackOnekidsRequest;
import com.example.onekids_project.master.request.SearchFeedBackOneKidsRequest;
import com.example.onekids_project.master.response.FeedBackOnekidsResponse;
import com.example.onekids_project.master.response.feedback.FeedbackAdminResponse;
import com.example.onekids_project.master.response.feedback.FeedbackDetailAdminResponse;
import com.example.onekids_project.master.response.feedback.ListFeedbackAdminResponse;
import com.example.onekids_project.master.service.FeedBackOnekidsService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedBackOnekidsServiceImpl implements FeedBackOnekidsService {
    private static final Logger logger = LoggerFactory.getLogger(FeedBackOnekidsService.class);

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private FeedBackFileRepository feedBackFileRepository;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ListMapper listMapper;

    @Override
    public ListFeedbackAdminResponse searchFeedbackAdmin(SearchFeedBackOneKidsRequest request) {
        ListFeedbackAdminResponse response = new ListFeedbackAdminResponse();
        List<Long> idSchoolList = ConvertData.getIdSchoolListInAgent(schoolService.findSchoolInAgent(request.getIdAgent()));
        List<FeedBack> feedBackList = feedBackRepository.searchFeedbackAdmin(request, idSchoolList);
        long count = feedBackRepository.countSearchFeedbackAdmin(request, idSchoolList);
        List<FeedbackAdminResponse> dataList = new ArrayList<>();
        feedBackList.forEach(x -> {
            FeedbackAdminResponse model = modelMapper.map(x, FeedbackAdminResponse.class);
            if (x.getIdSchoolReply() != null) {
                MaUser maUser = maUserRepository.findById(x.getIdSchoolReply()).orElseThrow();
                model.setReplyName(maUser.getFullName());
            }
            model.setFileNumber(x.getFeedBackFileList().size());
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setTotal(count);
        return response;
    }

    @Override
    public FeedbackDetailAdminResponse viewFeedbackDetail(Long id) {
        FeedBack feedBack = feedBackRepository.findById(id).orElseThrow();
        FeedbackDetailAdminResponse response = modelMapper.map(feedBack, FeedbackDetailAdminResponse.class);

        if (!CollectionUtils.isEmpty(feedBack.getFeedBackFileList())) {
            List<FileResponse> fileList = new ArrayList<>();
            feedBack.getFeedBackFileList().forEach(x -> {
                FileResponse fileResponse = new FileResponse();
                fileResponse.setName(x.getName());
                fileResponse.setUrl(x.getAttachPicture());
                fileList.add(fileResponse);
            });
            response.setFileList(fileList);
        }
        if (feedBack.getIdSchoolConfirm() != null) {
            MaUser maUser = maUserRepository.findById(feedBack.getIdSchoolConfirm()).orElseThrow();
            response.setConfirmName(maUser.getFullName());
        }
        if (feedBack.getIdSchoolReply() != null) {
            MaUser maUser = maUserRepository.findById(feedBack.getIdSchoolReply()).orElseThrow();
            response.setReplyName(maUser.getFullName());
        }
        if (feedBack.getAccountType().equals(AppTypeConstant.SYSTEM)) {
            response.setType(FeedBackConstant.SYSTEM_NAME);
        } else if (feedBack.getAccountType().equals(AppTypeConstant.TEACHER)) {
            response.setType(FeedBackConstant.TEACHER_NAME);
        } else if (feedBack.getAccountType().equals(AppTypeConstant.PARENT)) {
            response.setType(FeedBackConstant.PARENT_NAME);
        }
        if (feedBack.isHiddenStatus()) {
            response.setHidden(AppConstant.HIDDEN);
        } else {
            response.setHidden(AppConstant.NO_HIDDEN);
        }
        return response;
    }

    @Override
    public FeedBackOnekidsResponse findFeedBackOnekidsById(Long id) {
        Optional<FeedBack> feedBackOptional = feedBackRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE);
        if (feedBackOptional.isEmpty()) {
            return null;
        }
        FeedBack feedBack = feedBackOptional.get();
        FeedBackOnekidsResponse feedBackOnekidsResponse = modelMapper.map(feedBack, FeedBackOnekidsResponse.class);
        return feedBackOnekidsResponse;
    }

    @Override
    public boolean deleteFeedBackById(Long id) {
        Optional<FeedBack> feedBackOptional = feedBackRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE);
        if (feedBackOptional.isEmpty()) {
            return false;
        }
        feedBackRepository.delete(feedBackOptional.get());
        return true;
    }

    @Override
    @Transactional
    public boolean createFeedbackHidden(UserPrincipal principal, FeedBackOnekidsRequest feedBackOnekidsRequest) {
        FeedBack feedBack = modelMapper.map(feedBackOnekidsRequest, FeedBack.class);
        feedBack.setAccountType(FeedBackConstant.SYSTEM);
        feedBack.setHiddenStatus(AppConstant.APP_TRUE);
        feedBack.setCreatedBy(principal.getFullName());
        FeedBack feedBackSaved = feedBackRepository.save(feedBack);
        List<MultipartFile> multipartFileList = feedBackOnekidsRequest.getMultipartFileList();
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(x -> {
                FeedBackFile feedBackFile = new FeedBackFile();
                HandleFileResponse handleFileResponse = null;
                try {
                    handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(x, SystemConstant.ID_SYSTEM, UploadDownloadConstant.GOP_Y);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                feedBackFile.setName(handleFileResponse.getName());
                feedBackFile.setAttachPicture(handleFileResponse.getUrlWeb());
                feedBackFile.setUrlLocalPicture(handleFileResponse.getUrlLocal());
                feedBackFile.setFeedBack(feedBackSaved);
                feedBackFileRepository.save(feedBackFile);
            });

        }

        return true;
    }

    @Override
    public boolean deleteMultiFeedBackById(List<Long> idList) {
        for (Long id : idList) {
            Optional<FeedBack> feedBackOptional = feedBackRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE);
            feedBackRepository.delete(feedBackOptional.get());
        }
        return true;
    }


}
