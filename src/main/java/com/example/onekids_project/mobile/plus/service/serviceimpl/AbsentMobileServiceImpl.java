package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentDateDataResponse;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.absent.SearchAbsentPlusRequest;
import com.example.onekids_project.mobile.plus.response.absent.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AbsentPlusMobileService;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.repository.AbsentDateRepository;
import com.example.onekids_project.repository.AbsentLetterRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MaUserRepository;
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
public class AbsentMobileServiceImpl implements AbsentPlusMobileService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private AbsentLetterRepository absentLetterRepository;
    @Autowired
    private AbsentDateRepository absentDateRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListAbsentPlusResponse searchAbsentPlus(UserPrincipal principal, SearchAbsentPlusRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<AbsentLetter> absentLetterList = absentLetterRepository.searchAbsentForPlus(idSchool, request);
        ListAbsentPlusResponse listAbsentPlusResponse = new ListAbsentPlusResponse();
        List<AbsentPlusResponse> absentPlusResponseList = new ArrayList<>();
        absentLetterList.forEach(x -> {
            AbsentPlusResponse model = new AbsentPlusResponse();
            model.setFullName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setId(x.getId());
            String content = StringDataUtils.getSubStringLarge(x.getAbsentContent());
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
            model.setDateAbsent(ConvertData.convertDateToString(x.getFromDate(), x.getToDate()));
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setReplyNumber(replyNumber);
            model.setPictureNumber(x.getAbsentLetterAttachFileList().size());
            model.setTeacherUnread(x.isTeacherUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            model.setExpired(x.isExpired());
            absentPlusResponseList.add(model);
        });
        boolean lastPage = absentLetterList.size() < MobileConstant.MAX_PAGE_ITEM;
        listAbsentPlusResponse.setDataList(absentPlusResponseList);
        listAbsentPlusResponse.setLastPage(lastPage);
        return listAbsentPlusResponse;
    }

    @Override
    public AbsentPlusDetailResponse findDeTailPlusAbent(UserPrincipal principal, Long id) {
        long idSchool = principal.getIdSchoolLogin();
        AbsentLetter absentLetter = absentLetterRepository.findById(id).orElseThrow();
        AbsentPlusDetailResponse model = new AbsentPlusDetailResponse();
        MaClass maClass = maClassRepository.findById(absentLetter.getIdClass()).orElseThrow();
        model.setKidName(absentLetter.getKids().getFullName());
        model.setClassName(maClass.getClassName());
        model.setAvatarkid(ConvertData.getAvatarKid(absentLetter.getKids()));
        model.setContent(absentLetter.getAbsentContent());
        model.setParentName(UserInforUtils.getFullName(absentLetter.getIdCreated()));
        model.setAvartarParent(ConvertData.getAvatarUserSchool(UserInforUtils.getMaUser(absentLetter.getIdCreated())));
        model.setConfirmStatus(absentLetter.isConfirmStatus());
        model.setDateAbsent(absentLetter.getFromDate().isEqual(absentLetter.getToDate()) ? ConvertData.convertDateToStringOne(absentLetter.getFromDate()) : ConvertData.convertDateToString(absentLetter.getFromDate(), absentLetter.getToDate()));
        List<AbsentDate> absentDateList = absentDateRepository.findByIdAbsentDate(idSchool, absentLetter.getId());
        List<AbsentDateDataResponse> absentDateDataResponseList = listMapper.mapList(absentDateList, AbsentDateDataResponse.class);
        model.setAbsentDateList(absentDateDataResponseList);
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getCreatedDate()));
        model.setCheckSchoolReply(absentLetter.getIdSchoolReply() != null);
        model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
        absentLetter.setTeacherUnread(AppConstant.APP_TRUE);
        absentLetterRepository.save(absentLetter);
        this.setReply(principal, absentLetter, model);
        return model;
    }

    @Transactional
    @Override
    public AbsentPlusConfirmResponse absentPlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        AbsentPlusConfirmResponse model = new AbsentPlusConfirmResponse();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        absentLetter.setConfirmStatus(AppConstant.APP_TRUE);
        absentLetter.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_SCHOOL);
        absentLetter.setConfirmDate(LocalDateTime.now());
        absentLetter.setIdConfirmType(principal.getId());
        absentLetter.setConfirmType(AppTypeConstant.SCHOOL);
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        this.setReplyConfirm(principal, absentLetter, model);
        //gửi firebase
        firebaseFunctionService.sendParentByPlusNoContent(34L, absentLetter.getKids(), FirebaseConstant.CATEGORY_ABSENT);
        return model;
    }

    @Transactional
    @Override
    public AbsentPlusSendReplyResponse sendPlusReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException {
        AbsentPlusSendReplyResponse model = new AbsentPlusSendReplyResponse();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(updatePlusSendReplyRequest.getId()).orElseThrow();
        if(absentLetter.getIdSchoolReply()==null){
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(36L, absentLetter.getKids(), FirebaseConstant.CATEGORY_ABSENT, updatePlusSendReplyRequest.getSchoolReply());
        }
        if (StringUtils.isBlank(absentLetter.getConfirmContent())) {
            absentLetter.setConfirmStatus(AppConstant.APP_TRUE);
            absentLetter.setConfirmType(AppTypeConstant.SCHOOL);
            absentLetter.setConfirmDate(LocalDateTime.now());
            absentLetter.setIdConfirmType(principal.getId());
        }
        absentLetter.setIdSchoolReply(principal.getId());
        absentLetter.setSchoolTimeReply(LocalDateTime.now());
        absentLetter.setSchoolModifyStatus(absentLetter.getSchoolReply() == null ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
        model.setId(updatePlusSendReplyRequest.getId());
        model.setSchoolReply(updatePlusSendReplyRequest.getSchoolReply());
        absentLetter.setSchoolReply(updatePlusSendReplyRequest.getSchoolReply());
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetter.setTeacherUnread(AppConstant.APP_TRUE);
        absentLetter.setSchoolReplyDel(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        this.setReplySend(principal, absentLetter, model);
        return model;
    }

    @Override
    public AbsentPlusRevokeResponse sendRevoke(Long idSchoolLogin, UserPrincipal principal, UpdatePlusRevokeRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        AbsentPlusRevokeResponse model = new AbsentPlusRevokeResponse();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (request.getKeyType().equals(MobileConstant.TYPE_TEACHER) && StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            absentLetter.setTeacherReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokeTeacher(idSchool, absentLetter, model);
        } else if (request.getKeyType().equals(MobileConstant.TYPE_SCHOOL) && StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            absentLetter.setSchoolReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokePlus(principal, absentLetter, model);
        }
        absentLetter.setDefaultContentDel(ParentDairyConstant.CONTENT_DEL);
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        return model;
    }

    private void setReplyRevokePlus(UserPrincipal principal, AbsentLetter absentLetter, AbsentPlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(absentLetter.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setStatusDel(AppConstant.APP_TRUE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyRevokeTeacher(Long idSchool, AbsentLetter absentLetter, AbsentPlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_TEACHER);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_TRUE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplySend(UserPrincipal principal, AbsentLetter absentLetter, AbsentPlusSendReplyResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(absentLetter.getSchoolReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(absentLetter.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }


    private void setReplyConfirm(UserPrincipal principal, AbsentLetter absentLetter, AbsentPlusConfirmResponse model) {
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setKeyType("");
        replyMobileDateObject.setContent(absentLetter.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, AbsentLetter absentLetter, AbsentPlusDetailResponse
            model) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        //khi có Xác nhận
        if (StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            replyMobileDateObjectList.add(this.setReplyConfirm(idSchool, absentLetter));
        }
        // nhà trường phản hồi
        if (StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            replyMobileDateObjectList.add(this.setReplySchool(principal, absentLetter));
        }
        // giáo viên phản hồi
        if (StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            replyMobileDateObjectList.add(this.setReplyTeacher(idSchool, absentLetter));
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private ReplyMobilePlusObject setReplyConfirm(Long idSchool, AbsentLetter absentLetter) {
        MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setKeyType("");
        replyMobileDateObject.setContent(absentLetter.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
        replyMobileDateObject.setSortDate(absentLetter.getConfirmDate());
        return replyMobileDateObject;
    }

    private ReplyMobilePlusObject setReplySchool(UserPrincipal principal, AbsentLetter absentLetter) {
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(absentLetter.getIdSchoolReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(absentLetter.isSchoolReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : absentLetter.getSchoolReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(absentLetter.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setSortDate(absentLetter.getSchoolTimeReply());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
        return replyMobileDateObject;
    }

    private ReplyMobilePlusObject setReplyTeacher(Long idSchool, AbsentLetter absentLetter) {
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(absentLetter.isTeacherReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : absentLetter.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_TRUE);
        replyMobileDateObject.setSortDate(absentLetter.getTeacherTimeReply());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_TEACHER);
        replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
        return replyMobileDateObject;
    }
}
