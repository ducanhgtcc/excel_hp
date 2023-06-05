package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AbsentLetterRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.AbsentLetterRequest;
import com.example.onekids_project.request.parentdiary.SearchAbsentLetterRequest;
import com.example.onekids_project.response.parentdiary.AbsentLetterResponse;
import com.example.onekids_project.response.parentdiary.AbsentNewResponse;
import com.example.onekids_project.response.parentdiary.ListAbsentLetterResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AbsentLetterSerVice;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AbsentServiceImpl implements AbsentLetterSerVice {
    private static final Logger logger = LoggerFactory.getLogger(AbsentLetterSerVice.class);
    @Autowired
    private AbsentLetterRepository absentLetterRepository;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListAbsentLetterResponse searchAbsent(UserPrincipal principal, SearchAbsentLetterRequest request) {
        long idSchool = principal.getIdSchoolLogin();
        List<AbsentLetter> absentLetterList = absentLetterRepository.searchAbsent(idSchool, request);
        List<AbsentLetterResponse> absentLetterResponseList = listMapper.mapList(absentLetterList, AbsentLetterResponse.class);
        absentLetterResponseList.forEach(x -> {
            if (x.getIdTeacherReply() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdTeacherReply());
                maUserOptional.ifPresent(maUser -> x.setTeacherReplyName(maUser.getFullName()));
            }
            if (x.getIdSchoolReply() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdSchoolReply());
                maUserOptional.ifPresent(maUser -> x.setSchoolReplyy(maUser.getFullName()));
            }
            x.setKidName(x.getKids().getFullName());
            x.setStatusfirst(x.isConfirmStatus() ? MessageWebConstant.CONFIRM : MessageWebConstant.NOT_CONFIRM);
            MaClass maClass = maClassRepository.findById(x.getIdClass()).orElseThrow();
            x.setClassName(maClass.getClassName());
            x.setNumberFile(x.getAbsentLetterAttachFileList().size());
            x.setDateLeave(x.getFromDate() + "\n đến \n " + x.getToDate());
            if (x.getIdSchoolReply() != null && x.getIdTeacherReply() != null) {
                x.setSchoolFeedback("- Nhà trường \n - Giáo viên");
            } else if (x.getIdSchoolReply() != null && x.getIdTeacherReply() == null) {
                x.setSchoolFeedback("- Nhà trường");
            } else if (x.getIdSchoolReply() == null && x.getIdTeacherReply() != null) {
                x.setSchoolFeedback("- Giáo viên");
            }
        });
        long total = absentLetterRepository.countTotalAccount(idSchool, request);
        ListAbsentLetterResponse listAbsentLetterResponse = new ListAbsentLetterResponse();
        listAbsentLetterResponse.setTotal(total);
        listAbsentLetterResponse.setAbsentLetterResponses(absentLetterResponseList);
        return listAbsentLetterResponse;
    }

    @Transactional
    @Override
    public boolean updateAbsent(Long idSchoolLogin, UserPrincipal principal, ContentRequest request) throws FirebaseMessagingException {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AbsentLetter oldAbsent = absentLetterRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType())){
            if (oldAbsent.getIdSchoolReply() == null) {
                //gửi firebase
                firebaseFunctionService.sendParentByPlus(36L, oldAbsent.getKids(), FirebaseConstant.CATEGORY_ABSENT, request.getContent());
            }
            // click lưu. (chưa xác nhận, chưa phản hồi)
            oldAbsent.setSchoolTimeReply(LocalDateTime.now());
            oldAbsent.setSchoolReply(request.getContent());
            oldAbsent.setSchoolModifyStatus(oldAbsent.getIdSchoolReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            oldAbsent.setIdSchoolReply(principal.getId());
        }else if(AppTypeConstant.TEACHER.equals(principal.getAppType())){
            if (oldAbsent.getIdTeacherReply() == null) {
                //gửi firebase
                firebaseFunctionService.sendParentByTeacher(35L, oldAbsent.getKids(), FirebaseConstant.CATEGORY_ABSENT, request.getContent());
            }
            // click lưu. (chưa xác nhận, chưa phản hồi)
            oldAbsent.setTeacherTimeReply(LocalDateTime.now());
            oldAbsent.setTeacherReply(request.getContent());
            oldAbsent.setTeacherModifyStatus(oldAbsent.getIdTeacherReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            oldAbsent.setIdTeacherReply(principal.getId());
        }
        oldAbsent.setParentUnread(AppConstant.APP_FALSE);
        if (oldAbsent.getIdConfirmType() == null) {
            this.setConfirm(principal, oldAbsent, ButtonConstant.BUTTON_SAVE);
        }
        absentLetterRepository.save(oldAbsent);
        return true;
    }

    @Transactional
    @Override
    public boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        this.setConfirm(principal, absentLetter, ButtonConstant.BUTTON_CONFIRM);
        absentLetterRepository.save(absentLetter);
        //gửi firebase
        Long idWebSystem = AppTypeConstant.SCHOOL.equals(principal.getAppType()) ? 34L : 33L;
        firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, absentLetter.getKids(), FirebaseConstant.CATEGORY_ABSENT);
        return true;
    }

    @Override
    public boolean revokeTeacher(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        absentLetter.setTeacherReplyDel(request.getStatus());
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        return true;
    }

    @Override
    public boolean revokePlus(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        absentLetter.setSchoolReplyDel(request.getStatus());
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        return true;
    }

    //    fireBasse
    private void sendFireBase(Kids kids, String title, String content) throws FirebaseMessagingException {
        if (kids.getParent() != null) {
            List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
            if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                NotifyRequest notifyRequest = new NotifyRequest();
                notifyRequest.setBody(content);
                notifyRequest.setTitle(title);
                FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.ABSENT, notifyRequest, kids.getId().toString());
            }
        }
    }

    @Override
    public AbsentNewResponse findByIdAbsent(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        AbsentLetter oldAbsent = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        oldAbsent.setTeacherUnread(AppConstant.APP_TRUE);
        absentLetterRepository.save(oldAbsent);
        AbsentNewResponse response = modelMapper.map(oldAbsent, AbsentNewResponse.class);
        return response;
    }

    @Override
    public boolean updateRead(Long id, List<AbsentLetterRequest> absentLetterResponse) {
        absentLetterResponse.forEach(x -> {
            Optional<AbsentLetter> absentLetterOptional = absentLetterRepository.findByIdAndDelActiveTrue(x.getId());
            if (absentLetterOptional.isPresent()) {
                AbsentLetter absentLetter = absentLetterOptional.get();
                absentLetter.setTeacherUnread(AppConstant.APP_TRUE);
                absentLetterRepository.save(absentLetter);
            }
        });
        return true;
    }

    @Transactional
    @Override
    public boolean updateConfirmMany(Long id, List<AbsentLetterRequest> absentletterResponse, UserPrincipal principal) throws FirebaseMessagingException {
        for (AbsentLetterRequest x : absentletterResponse) {
            AbsentLetter absentLetter = absentLetterRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(x.getId()).orElseThrow();
            this.setConfirm(principal, absentLetter, ButtonConstant.BUTTON_CONFIRM);
            absentLetter.setParentUnread(AppConstant.APP_FALSE);
            absentLetterRepository.save(absentLetter);
            //gửi firebase
            Long idWebSystem = AppTypeConstant.SCHOOL.equals(principal.getAppType()) ? 36L : 35L;
            firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, absentLetter.getKids(), FirebaseConstant.CATEGORY_ABSENT);
        }
        return true;
    }

    /**
     * xác nhận
     *
     * @param principal
     * @param absentLetter
     * @param type
     */
    private void setConfirm(UserPrincipal principal, AbsentLetter absentLetter, String type) {
        absentLetter.setConfirmStatus(AppConstant.APP_TRUE);
        absentLetter.setIdConfirmType(principal.getId());
        absentLetter.setConfirmDate(LocalDateTime.now());
        absentLetter.setConfirmType(principal.getAppType());
        if (type.equals(ButtonConstant.BUTTON_CONFIRM)) {
            absentLetter.setConfirmContent(principal.getAppType().equals(AppTypeConstant.SCHOOL) ? ParentDairyConstant.CONTENT_CONFIRM_SCHOOL : ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
        }
        attendanceKidsCronjobs.createAttendanceByAbsent(absentLetter, principal.getId());
    }
}


