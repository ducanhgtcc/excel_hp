package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentDateDataResponse;
import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.teacher.request.absent.SearchAbsentTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.absent.UpdateTeacherReplyabsentRequest;
import com.example.onekids_project.mobile.teacher.response.absentletter.*;
import com.example.onekids_project.mobile.teacher.response.message.MessageTeacherSendReplyResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AbsentLetterTeacherMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
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
public class AbsentLetterTeacherMobileServiceImpl implements AbsentLetterTeacherMobileService {

    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Autowired
    private AbsentDateRepository absentDateRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private AbsentTeacherRepository absentTeacherRepository;
    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;
    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;

    private void setReplyUpdate(UserPrincipal principal, AbsentLetter absentLetter, AbsentTeacherSendReplyResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(absentLetter.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(absentLetter.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
        if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    @Override
    public ListAbsentTeacherResponse searchAbsentTeacher(UserPrincipal principal, SearchAbsentTeacherRequest searchAbsentTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        List<AbsentLetter> absentLetterList = absentLetterRepository.findAbsentForTeacher(idSchool, idClass, searchAbsentTeacherRequest);
        ListAbsentTeacherResponse listAbsentTeacherResponse = new ListAbsentTeacherResponse();
        List<AbsentTeacherResponse> absentTeacherResponseList = new ArrayList<>();
        absentLetterList.forEach(x -> {
            AbsentTeacherResponse model = new AbsentTeacherResponse();
            model.setFullName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setId(x.getId());
            String content = x.getAbsentContent().length() < 100 ? x.getAbsentContent() : x.getAbsentContent().substring(0, 100);
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
            absentTeacherResponseList.add(model);
        });
        boolean lastPage = absentLetterList.size() < MobileConstant.MAX_PAGE_ITEM;
        listAbsentTeacherResponse.setDataList(absentTeacherResponseList);
        listAbsentTeacherResponse.setLastPage(lastPage);
        return listAbsentTeacherResponse;
    }

    @Override
    public AbsentTeacherDetailResponse findAbsentLetterDetail(UserPrincipal principal, Long id) {
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        AbsentTeacherDetailResponse model = new AbsentTeacherDetailResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(absentLetter.getKids().getId()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(absentLetter.getIdCreated()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(absentLetter.getIdClass()).orElseThrow(() -> new NotFoundException("not found id class"));
        model.setFullName(kids.getFullName());
        Long idSchool = principal.getIdSchoolLogin();
        List<AbsentDate> absentDateList = absentDateRepository.findByIdAbsentDate(idSchool, absentLetter.getId());
        List<AbsentDateDataResponse> absentDateDataResponseList = listMapper.mapList(absentDateList, AbsentDateDataResponse.class);
        model.setAbsentDateList(absentDateDataResponseList);
        model.setClassName(maClass.getClassName());
        model.setContent(absentLetter.getAbsentContent());
        model.setParentName(maUser.getFullName());
        model.setConfirmStatus(absentLetter.isConfirmStatus());
        model.setDateAbsent(absentLetter.getFromDate().isEqual(absentLetter.getToDate()) ? ConvertData.convertDateToStringOne(absentLetter.getFromDate()) : ConvertData.convertDateToString(absentLetter.getFromDate(), absentLetter.getToDate()));
        model.setCheckTeacherReply(absentLetter.getIdTeacherReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        if (absentLetter.isSchoolReplyDel() == AppConstant.APP_TRUE && absentLetter.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getCreatedDate()));
            model.setContent(absentLetter.getAbsentContent());
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply(principal, absentLetter, model);
        } else if (absentLetter.isSchoolReplyDel() == AppConstant.APP_TRUE && absentLetter.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getCreatedDate()));
            model.setContent(absentLetter.getAbsentContent());
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply2(principal, absentLetter, model);
        } else if (absentLetter.isSchoolReplyDel() == AppConstant.APP_FALSE && absentLetter.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getCreatedDate()));
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply3(principal, absentLetter, model);
        } else if (absentLetter.isSchoolReplyDel() == AppConstant.APP_FALSE && absentLetter.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getCreatedDate()));
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply1(principal, absentLetter, model);
        }
        absentLetter.setTeacherUnread(AppConstant.APP_TRUE);
        absentLetterRepository.save(absentLetter);
        return model;
    }

    @Override
    public AbsentTeacherRevokeResponse absentTeacherRevoke(UserPrincipal principal, Long id) {
        AbsentTeacherRevokeResponse model = new AbsentTeacherRevokeResponse();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        absentLetter.setTeacherReplyDel(AppConstant.APP_TRUE);
        absentLetter.setDefaultContentDel(ParentDairyConstant.CONTENT_DEL);
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        this.setReplyRevoke(principal, absentLetter, model);
        return model;
    }

    private void setReplyRevoke(UserPrincipal principal, AbsentLetter absentLetter, AbsentTeacherRevokeResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(absentLetter.getDefaultContentDel());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getLastModifieDate()));
        replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
        if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    @Transactional
    @Override
    public AbsentTeacheConfirmResponse absentTeacherConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        AbsentTeacheConfirmResponse model = new AbsentTeacheConfirmResponse();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        absentLetter.setConfirmStatus(AppConstant.APP_TRUE);
        absentLetter.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
        absentLetter.setConfirmDate(LocalDateTime.now());
        absentLetter.setIdConfirmType(principal.getId());
        absentLetter.setConfirmType(AppTypeConstant.TEACHER);
        absentLetter.setParentUnread(AppConstant.APP_FALSE);
        absentLetterRepository.save(absentLetter);
        attendanceKidsCronjobs.createAttendanceByAbsent(absentLetter, principal.getId());
        this.setReplyConfirm(principal, absentLetter, model);
        //    firebase
        firebaseFunctionService.sendParentByTeacherNoContent(33L, absentLetter.getKids(), FirebaseConstant.CATEGORY_ABSENT, UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal).getFullName());
        return model;
    }

    @Transactional
    @Override
    public AbsentTeacherSendReplyResponse sendTeacherReply(Long idSchoolLogin, UserPrincipal
            principal, UpdateTeacherReplyabsentRequest updateTeacherReplyabsentRequest) throws
            FirebaseMessagingException {
        AbsentTeacherSendReplyResponse model = new AbsentTeacherSendReplyResponse();
        Optional<AbsentLetter> absentLetterOptional = absentLetterRepository.findByIdAndDelActiveTrue(updateTeacherReplyabsentRequest.getId());
        if (absentLetterOptional.isEmpty()) {
            return null;
        }
        Long id = updateTeacherReplyabsentRequest.getId();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        AbsentLetter oldMessage = absentLetterOptional.get();
        modelMapper.map(updateTeacherReplyabsentRequest, oldMessage);
        if (oldMessage.getIdConfirmType() == null) {
            oldMessage.setTeacherTimeReply(LocalDateTime.now());
            oldMessage.setIdTeacherReply(principal.getId());
            oldMessage.setConfirmStatus(AppConstant.APP_TRUE);
            oldMessage.setConfirmType(AppTypeConstant.TEACHER);
            oldMessage.setConfirmDate(LocalDateTime.now());
            oldMessage.setIdConfirmType(principal.getId());
            oldMessage.setTeacherReply(updateTeacherReplyabsentRequest.getTeacherReply());
            oldMessage.setTeacherTimeReply(LocalDateTime.now());
            oldMessage.setIdTeacherReply(principal.getId());
            oldMessage.setTeacherModifyStatus(AppConstant.APP_FALSE);
            oldMessage.setParentUnread(AppConstant.APP_FALSE);
            attendanceKidsCronjobs.createAttendanceByAbsent(absentLetter, principal.getId());
        } else {
            oldMessage.setTeacherTimeReply(LocalDateTime.now());
            oldMessage.setIdTeacherReply(principal.getId());
            oldMessage.setTeacherModifyStatus(AppConstant.APP_FALSE);
            oldMessage.setParentUnread(AppConstant.APP_FALSE);
        }
        model.setId(updateTeacherReplyabsentRequest.getId());
        model.setTeacherReply(updateTeacherReplyabsentRequest.getTeacherReply());
        absentLetterRepository.save(oldMessage);
        this.setReplySend(principal, absentLetter, model);

        //gửi firebase
        firebaseFunctionService.sendParentByTeacher(35L, oldMessage.getKids(), FirebaseConstant.CATEGORY_ABSENT, updateTeacherReplyabsentRequest.getTeacherReply());

        return model;
    }

    @Transactional
    @Override
    public AbsentTeacherSendReplyResponse updateTeacherReply(Long idSchoolLogin, UserPrincipal
            principal, UpdateTeacherReplyabsentRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException {
        AbsentTeacherSendReplyResponse model = new AbsentTeacherSendReplyResponse();
        Optional<AbsentLetter> absentLetterOptional = absentLetterRepository.findByIdAbsent(idSchoolLogin, updateTeacherSendReplyRequest.getId());
        if (absentLetterOptional.isEmpty()) {
            return null;
        }
        AbsentLetter oldMessage = absentLetterOptional.get();
        Long id = updateTeacherSendReplyRequest.getId();
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        oldMessage.setTeacherTimeReply(LocalDateTime.now());
        oldMessage.setTeacherReplyDel(AppConstant.APP_FALSE);
        oldMessage.setTeacherModifyStatus(AppConstant.APP_TRUE);
        oldMessage.setParentUnread(AppConstant.APP_FALSE);
        modelMapper.map(updateTeacherSendReplyRequest, oldMessage);
        model.setId(updateTeacherSendReplyRequest.getId());
        model.setTeacherReply(updateTeacherSendReplyRequest.getTeacherReply());
        AbsentLetter newMessage = absentLetterRepository.save(oldMessage);
//        this.setReplyUpdate(principal, absentLetter, model);
        this.setReplyUpdate(principal, newMessage, model);
        MessageTeacherSendReplyResponse messageTeacherSendReplyResponse = modelMapper.map(newMessage, MessageTeacherSendReplyResponse.class);
        return model;
    }

    private void setReplySend(UserPrincipal principal, AbsentLetter
            absentLetter, AbsentTeacherSendReplyResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(absentLetter.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(absentLetter.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
        if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyConfirm(UserPrincipal principal, AbsentLetter
            absentLetter, AbsentTeacheConfirmResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(absentLetter.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
        replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }


    private void setReply3(UserPrincipal principal, AbsentLetter absentLetter, AbsentTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }

        }
        //nhà trường phản hồi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(absentLetter.getSchoolReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(absentLetter.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
            if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(absentLetter.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }

    private void setReply1(UserPrincipal principal, AbsentLetter absentLetter, AbsentTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường phản hồi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(absentLetter.getSchoolReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(absentLetter.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setContent(absentLetter.getTeacherReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
            if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(absentLetter.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }

        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }

    private void setReply2(UserPrincipal principal, AbsentLetter absentLetter, AbsentTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && absentLetter.isConfirmStatus() && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
//                MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường phản hồi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
//            MaUser maUser = maUserRepository.findById(absentLetter.getIdSchoolReply()).orElseThrow(() -> new NotFoundException("not found id"));
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(absentLetter.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setFullName(absentLetter.getTeacherReply());
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(AppConstant.TEACHER);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
            if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(absentLetter.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, AbsentLetter absentLetter, AbsentTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found id"));
//                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setContent(absentLetter.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getConfirmDate()));
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường thu hoi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isSchoolReplyDel());
            replyMobileDateObject.setSortDate(absentLetter.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setContent(absentLetter.getTeacherReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentLetter.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(absentLetter.isTeacherReplyDel());
            if (absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!absentLetter.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(absentLetter.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }
}
