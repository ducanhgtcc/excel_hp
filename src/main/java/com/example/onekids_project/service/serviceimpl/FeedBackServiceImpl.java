package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.dto.FeedbackDTO;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.feedback.FeedBackRequest;
import com.example.onekids_project.request.feedback.SearchParentFeedbackRequest;
import com.example.onekids_project.request.feedback.SearchTeacherFeedbackRequest;
import com.example.onekids_project.request.feedback.UpdateFeedbackRequest;
import com.example.onekids_project.response.feedback.FeedBackResponse;
import com.example.onekids_project.response.feedback.ListFeedBackResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.FeedBackSerVice;
import com.example.onekids_project.util.EmployeeUtil;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FeedBackServiceImpl implements FeedBackSerVice {
    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListFeedBackResponse findAllFeedBack(Long idSchoolLogin, Pageable pageable) {
        List<FeedBack> feedBackList = feedBackRepository.findAllFeedBack(idSchoolLogin, pageable);
        if (CollectionUtils.isEmpty(feedBackList)) {
            return null;
        }
        List<FeedBackResponse> feedBackResponseList = listMapper.mapList(feedBackList, FeedBackResponse.class);
        ListFeedBackResponse listFeedBackResponse = new ListFeedBackResponse();
        listFeedBackResponse.setFeedBackResponses(feedBackResponseList);
        return listFeedBackResponse;
    }

    @Override
    public ListFeedBackResponse searchTitle(UserPrincipal principal, SearchParentFeedbackRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<FeedBack> feedBackList = feedBackRepository.searchTitle(idSchool, request);
        feedBackList = feedBackList.stream().filter(x -> x.getIdSchool().equals(idSchool)).collect(Collectors.toList());
        List<FeedBackResponse> feedBackResponseList = listMapper.mapList(feedBackList, FeedBackResponse.class);
        feedBackResponseList.forEach(x -> {
            if (x.isHiddenStatus() == AppConstant.APP_TRUE) {
                x.setCreatedByUser(AppConstant.HIDDEN);
            } else {
                //set lại className theo thiết kết mới
                Optional<Kids> kidsOptional = kidsRepository.findByIdAndDelActive(x.getIdKid(), true);
                kidsOptional.ifPresent(kids -> x.setCreatedByUser("PH: " + "" + x.getCreatedBy() + " - " + kids.getMaClass().getClassName() + " - HS:" + kids.getFullName()));
            }
            x.setNumberFile(x.getFeedBackFileList().size());
            if (x.getIdSchoolConfirm() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdSchoolConfirm());
                maUserOptional.ifPresent(maUser -> x.setConfirmName(maUser.getFullName()));
            }
            if (x.getIdSchoolReply() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdSchoolReply());
                maUserOptional.ifPresent(maUser -> x.setReplyName(maUser.getFullName()));
            }
        });
        long total = feedBackRepository.countTotalAccount(idSchool, request);
        ListFeedBackResponse listFeedBackResponse = new ListFeedBackResponse();
        listFeedBackResponse.setTotal(total);
        listFeedBackResponse.setFeedBackResponses(feedBackResponseList);
        return listFeedBackResponse;
    }

    @Override
    public FeedbackDTO findByIdFeedback(Long id) {
        FeedBack feedBack = feedBackRepository.findById(id).orElseThrow();
        FeedbackDTO feedbackDTO = modelMapper.map(feedBack, FeedbackDTO.class);
        if (feedBack.getIdSchoolConfirm() != null) {
            MaUser maUser = maUserRepository.findById(feedBack.getIdSchoolConfirm()).orElseThrow();
            feedbackDTO.setConfirmName(maUser.getFullName());
        }
        if (feedBack.getIdSchoolReply() != null) {
            MaUser maUser = maUserRepository.findById(feedBack.getIdSchoolReply()).orElseThrow();
            feedbackDTO.setReplyName(maUser.getFullName());
        }
        feedBack.setSchoolUnread(AppConstant.APP_TRUE);
        feedBackRepository.save(feedBack);
        return feedbackDTO;
    }

    @Transactional
    @Override
    public FeedBackResponse updateFeedback(Long idSchoolLogin, UserPrincipal userPrincipal, UpdateFeedbackRequest feedBackEditRequest) throws FirebaseMessagingException {
        Optional<FeedBack> feedBackOptional = feedBackRepository.findByIdAndDelActiveTrue(feedBackEditRequest.getId());
        if (feedBackOptional.isEmpty()) {
            return null;
        }
        FeedBack oldFeedback = feedBackOptional.get();
        modelMapper.map(feedBackEditRequest, oldFeedback);
        Kids kids = null;
        if (oldFeedback.getIdKid() != null) {
            kids = kidsRepository.findByIdAndDelActiveTrue(oldFeedback.getIdKid()).orElseThrow();
        }
        // click xac nhan
        if ("feedbackConfirm".equals(feedBackEditRequest.getDataType()) && feedBackEditRequest.getIdSchoolReply() == null) {
            oldFeedback.setIdSchoolConfirm(userPrincipal.getId());
            oldFeedback.setSchoolConfirmStatus(AppConstant.APP_TRUE);
            oldFeedback.setConfirmDate(LocalDateTime.now());
            oldFeedback.setConfirmContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
            //loại góp ý của teacher
            if (kids == null) {
                InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromMaUser(oldFeedback.getIdCreated(), idSchoolLogin);
                if (infoEmployeeSchool.getAppType().equals(AppTypeConstant.TEACHER)) {
                    //gửi firebase
                    firebaseFunctionService.sendTeacherByPlusNoContent(58L, infoEmployeeSchool, FirebaseConstant.CATEGORY_FEEDBACK, idSchoolLogin);
                }
            } else {
                //loại góp ý của phụ huynh
                //gửi firebase
                firebaseFunctionService.sendParentByPlusNoContent(55L, kids, FirebaseConstant.CATEGORY_FEEDBACK);
            }
        }
        // click gửi phản hồi, kiểm tra xem đã xác nhận hay chưa
        if ("feedbackSend".equals(feedBackEditRequest.getDataType()) && feedBackEditRequest.getIdSchoolReply() == null) {
            if (oldFeedback.getIdSchoolReply() == null) {
                //loại góp ý của teacher
                if (kids == null) {
                    InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromMaUser(oldFeedback.getIdCreated(), idSchoolLogin);
                    if (infoEmployeeSchool.getAppType().equals(AppTypeConstant.TEACHER)) {
                        //gửi firebase
                        firebaseFunctionService.sendTeacherByPlus(59L, infoEmployeeSchool, feedBackEditRequest.getSchoolReply(), FirebaseConstant.CATEGORY_FEEDBACK, idSchoolLogin);
                    }
                } else {
                    //loại góp ý của phụ huynh
                    //gửi firebase
                    firebaseFunctionService.sendParentByPlus(56L, kids, FirebaseConstant.CATEGORY_FEEDBACK, feedBackEditRequest.getSchoolReply());
                }
            }
            if (oldFeedback.getIdSchoolConfirm() == null) {
                oldFeedback.setIdSchoolConfirm(userPrincipal.getId());
                oldFeedback.setSchoolConfirmStatus(AppConstant.APP_TRUE);
                oldFeedback.setIdSchoolReply(userPrincipal.getId());
                oldFeedback.setSchoolTimeReply(LocalDateTime.now());
                oldFeedback.setConfirmDate(LocalDateTime.now());
                oldFeedback.setSchoolUnread(AppConstant.APP_TRUE);
            } else {
                oldFeedback.setIdSchoolReply(userPrincipal.getId());
                oldFeedback.setSchoolUnread(AppConstant.APP_TRUE);
                oldFeedback.setSchoolTimeReply(LocalDateTime.now());
            }
        } else if ("feedbackSend".equals(feedBackEditRequest.getDataType()) && feedBackEditRequest.getIdSchoolReply() != null) {
            if (oldFeedback.getIdSchoolReply() == null) {
                //loại góp ý của teacher
                if (kids == null) {
                    InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromMaUser(oldFeedback.getIdCreated(), idSchoolLogin);
                    if (infoEmployeeSchool.getAppType().equals(AppTypeConstant.TEACHER)) {
                        //gửi firebase
                        firebaseFunctionService.sendTeacherByPlus(59L, infoEmployeeSchool, feedBackEditRequest.getSchoolReply(), FirebaseConstant.CATEGORY_FEEDBACK, idSchoolLogin);
                    }
                } else {
                    //loại góp ý của phụ huynh
                    //gửi firebase
                    firebaseFunctionService.sendParentByPlus(56L, kids, FirebaseConstant.CATEGORY_FEEDBACK, feedBackEditRequest.getSchoolReply());
                }
            }
            oldFeedback.setIdSchoolReply(userPrincipal.getId());
            oldFeedback.setSchoolTimeReply(LocalDateTime.now());
            oldFeedback.setLastModifieDate(LocalDateTime.now());
            oldFeedback.setSchoolModifyStatus(AppConstant.APP_TRUE);
            //  chuyển trạng thái parent -> chưa đọc
            if (StringUtils.isNotBlank(feedBackEditRequest.getSchoolReply())) {
                oldFeedback.setParentUnread(AppConstant.APP_FALSE);
            }
        }
        // click thu hoi
        if ("feedbackRemove".equals(feedBackEditRequest.getDataType())) {
            oldFeedback.setSchoolReplyDel(AppConstant.APP_TRUE);
            FeedBack newFeedback = feedBackRepository.save(oldFeedback);
            FeedBackResponse feedbackEditResponse = modelMapper.map(newFeedback, FeedBackResponse.class);
            return feedbackEditResponse;
        } else if ("feedbackRemove1".equals(feedBackEditRequest.getDataType())) {
            // click hủy thu hồi
            oldFeedback.setSchoolReplyDel(AppConstant.APP_FALSE);
        }
        oldFeedback.setSchoolUnread(AppConstant.APP_TRUE);
        oldFeedback.setParentUnread(AppConstant.APP_FALSE);
        FeedBack newFeedback = feedBackRepository.save(oldFeedback);
        FeedBackResponse feedbackEditResponse = modelMapper.map(newFeedback, FeedBackResponse.class);
        return feedbackEditResponse;
    }

    @Override
    public FeedBackRequest updateRead(Long id, List<FeedBackRequest> feedBackResponses) {
        feedBackResponses.forEach(x -> {
            Optional<FeedBack> feedBackOptional = feedBackRepository.findByIdAndDelActiveTrue(x.getId());
            if (feedBackOptional.isPresent()) {
                FeedBack feedBack = feedBackOptional.get();
                feedBack.setSchoolUnread(AppConstant.APP_TRUE);
                feedBackRepository.save(feedBack);
            }
        });
        return null;
    }

    @Override
    public ListFeedBackResponse searchTitleTeacher(UserPrincipal principal, SearchTeacherFeedbackRequest request) {
        long idSchool = principal.getIdSchoolLogin();
        List<FeedBack> feedBackList = feedBackRepository.searchTitleTeacher(idSchool, request);
        feedBackList = feedBackList.stream().filter(x -> x.getIdSchool() == idSchool).collect(Collectors.toList());
        List<FeedBackResponse> feedBackResponseList = listMapper.mapList(feedBackList, FeedBackResponse.class);
        feedBackResponseList.forEach(x -> {
            if (x.isHiddenStatus()) {
                x.setCreatedByUser(AppConstant.HIDDEN);
            } else {
                //set lại className theo thiết kết mới
                Optional<MaClass> maClassOptional = maClassRepository.findById(x.getId());
                if (maClassOptional.isPresent()) {
                    x.setCreatedByUser(x.getCreatedBy());
                }
            }
            if (x.getIdSchoolConfirm() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdSchoolConfirm());
                maUserOptional.ifPresent(maUser -> x.setConfirmName(maUser.getFullName()));
            }
            x.setNumberFile(x.getFeedBackFileList().size());
            if (x.getIdSchoolReply() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdSchoolReply());
                maUserOptional.ifPresent(maUser -> x.setReplyName(maUser.getFullName()));
            }
        });
        long total = feedBackRepository.countTotalAccountTC(idSchool, request);
        ListFeedBackResponse listFeedBackResponse = new ListFeedBackResponse();
        listFeedBackResponse.setTotal(total);
        listFeedBackResponse.setFeedBackResponses(feedBackResponseList);
        return listFeedBackResponse;
    }

    @Override
    public ListFeedBackResponse findAllTeacherFeedBack(Long idSchoolLogin, Pageable pageable) {
        List<FeedBack> feedBackList = feedBackRepository.findAllFeedBack(idSchoolLogin, pageable);
        if (CollectionUtils.isEmpty(feedBackList)) {
            return null;
        }
        List<FeedBackResponse> feedBackResponseList = listMapper.mapList(feedBackList, FeedBackResponse.class);
        ListFeedBackResponse listFeedBackResponse = new ListFeedBackResponse();
        listFeedBackResponse.setFeedBackResponses(feedBackResponseList);
        return listFeedBackResponse;
    }

    @Override
    public FeedbackDTO findByIdTeacherFeedback(Long id) {
        FeedBack feedBack = feedBackRepository.findById(id).orElseThrow();
        FeedbackDTO feedbackDTO = modelMapper.map(feedBack, FeedbackDTO.class);
        if (feedBack.getIdSchoolConfirm() != null) {
            MaUser maUser = maUserRepository.findById(feedBack.getIdSchoolConfirm()).orElseThrow();
            feedbackDTO.setConfirmName(maUser.getFullName());
        }
        if (feedBack.getIdSchoolReply() != null) {
            MaUser maUser = maUserRepository.findById(feedBack.getIdSchoolReply()).orElseThrow();
            feedbackDTO.setReplyName(maUser.getFullName());
        }
        return feedbackDTO;
    }


}





