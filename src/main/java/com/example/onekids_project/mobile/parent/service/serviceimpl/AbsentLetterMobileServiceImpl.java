package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.entity.kids.AttendanceConfig;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.enums.DateEnum;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.request.absent.AbsentMobileRequest;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentDateDataResponse;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentLetterDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentLetterMobileResponse;
import com.example.onekids_project.mobile.parent.response.absentletter.ListAbsentLetterMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AbsentLetterMobileService;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbsentLetterMobileServiceImpl implements AbsentLetterMobileService {
    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private AbsentLetterAttachFileRepository absentLetterAttachFileRepository;

    @Autowired
    private AbsentDateRepository absentDateRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListAbsentLetterMobileResponse findAbsentMoblie(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        List<AbsentLetter> absentLetterList = absentLetterRepository.findAbsentMobile(idSchool, idKid, pageable, localDateTime);
        ListAbsentLetterMobileResponse listAbsentLetterMobileResponse = new ListAbsentLetterMobileResponse();
        List<AbsentLetterMobileResponse> absentLetterMobileResponses = new ArrayList<>();
        absentLetterList.forEach(x -> {
            AbsentLetterMobileResponse model = new AbsentLetterMobileResponse();
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
            model.setNumberFile(x.getAbsentLetterAttachFileList().size());
            model.setReplyNumber(replyNumber);
            model.setDateAbsent(ConvertData.convertDateToString(x.getFromDate(), x.getToDate()));
            model.setPictureNumber(x.getAbsentLetterAttachFileList().size());
            model.setCreatedDate(x.getCreatedDate());
            model.setParentUnread(x.isParentUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            absentLetterMobileResponses.add(model);
        });
        Long countAll = absentLetterRepository.getCountMessage(idSchool, idKid, localDateTime);
        boolean checkLastPage = countAll <= 20;
        listAbsentLetterMobileResponse.setAbsentLetterMobileResponseList(absentLetterMobileResponses);
        listAbsentLetterMobileResponse.setLastPage(checkLastPage);
        return listAbsentLetterMobileResponse;
    }

    @Override
    public boolean absentRevoke(Long id) {
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        if (absentLetter.isConfirmStatus()) {
            return false;
        }
        absentLetter.setDelActive(AppConstant.APP_FALSE);
        absentLetter.setParentAbsentDel(AppConstant.APP_TRUE);
        absentLetterRepository.save(absentLetter);
        return true;
    }

    @Override
    public AbsentLetterDetailMobileResponse findAbsentDetailMobile(UserPrincipal principal, Long id) {
        AbsentLetter absentLetter = absentLetterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentLetter by id"));
        AbsentLetterDetailMobileResponse model = new AbsentLetterDetailMobileResponse();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(absentLetter.getIdCreated()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        model.setFullName(maUser.getFullName());
        model.setConfirmStatus(absentLetter.isConfirmStatus());
        Long idSchool = principal.getIdSchoolLogin();
        List<AbsentDate> absentDateList = absentDateRepository.findByIdAbsentDate(idSchool, absentLetter.getId());
        List<AbsentDateDataResponse> absentDateDataResponseList = listMapper.mapList(absentDateList, AbsentDateDataResponse.class);
        model.setAbsentDateList(absentDateDataResponseList);
        if (absentLetter.isSchoolReplyDel() == AppConstant.APP_TRUE && absentLetter.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(absentLetter.getCreatedDate());
            model.setContent(absentLetter.getAbsentContent());
            model.setDateAbsent(absentLetter.getFromDate().isEqual(absentLetter.getToDate()) ? ConvertData.convertDateToStringOne(absentLetter.getFromDate()) : ConvertData.convertDateToString(absentLetter.getFromDate(), absentLetter.getToDate()));
            model.setConfirmStatus(absentLetter.isConfirmStatus());
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply1(principal, absentLetter, model); // nha truong thu hoi, giao vien k thu hoi
        } else if (absentLetter.isSchoolReplyDel() == AppConstant.APP_TRUE && absentLetter.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(absentLetter.getCreatedDate());
            model.setContent(absentLetter.getAbsentContent());
            model.setDateAbsent(absentLetter.getFromDate().isEqual(absentLetter.getToDate()) ? ConvertData.convertDateToStringOne(absentLetter.getFromDate()) : ConvertData.convertDateToString(absentLetter.getFromDate(), absentLetter.getToDate()));
            model.setConfirmStatus(absentLetter.isConfirmStatus());
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply2(principal, absentLetter, model);
        } else if (absentLetter.isSchoolReplyDel() == AppConstant.APP_FALSE && absentLetter.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(absentLetter.getCreatedDate());
            model.setContent(absentLetter.getAbsentContent());
            model.setDateAbsent(absentLetter.getFromDate().isEqual(absentLetter.getToDate()) ? ConvertData.convertDateToStringOne(absentLetter.getFromDate()) : ConvertData.convertDateToString(absentLetter.getFromDate(), absentLetter.getToDate()));
            model.setConfirmStatus(absentLetter.isConfirmStatus());
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply3(principal, absentLetter, model);
        } else if (absentLetter.isSchoolReplyDel() == AppConstant.APP_FALSE && absentLetter.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(absentLetter.getCreatedDate());
            model.setContent(absentLetter.getAbsentContent());
            model.setDateAbsent(absentLetter.getFromDate().isEqual(absentLetter.getToDate()) ? ConvertData.convertDateToStringOne(absentLetter.getFromDate()) : ConvertData.convertDateToString(absentLetter.getFromDate(), absentLetter.getToDate()));
            model.setConfirmStatus(absentLetter.isConfirmStatus());
            model.setPictureList(absentLetter.getAbsentLetterAttachFileList().stream().map(AbsentLetterAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply(principal, absentLetter, model);
        }
        absentLetter.setParentUnread(AppConstant.APP_TRUE);
        absentLetterRepository.save(absentLetter);
        return model;
    }


    private void setReply3(UserPrincipal principal, AbsentLetter absentLetter, AbsentLetterDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && absentLetter.isConfirmStatus() == AppConstant.APP_TRUE && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(absentLetter.getConfirmContent());
            replyMobileObject.setCreatedDate(absentLetter.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }
        //
        //nhà trường phản hồi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply()) && !absentLetter.isSchoolReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdSchoolReply()).orElseThrow();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(absentLetter.getSchoolReply());
            replyMobileObject.setCreatedDate(absentLetter.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow();
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(absentLetter.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    private void setReply1(UserPrincipal principal, AbsentLetter absentLetter, AbsentLetterDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow();
            String typeConfirm = absentLetter.getConfirmType();
            if (AppTypeConstant.SCHOOL.equals(typeConfirm)) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(typeConfirm) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(ConvertData.getAvatarUserSchool(maUser));
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(absentLetter.getConfirmContent());
            replyMobileObject.setCreatedDate(absentLetter.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }
        if (absentLetter.getIdSchoolReply() != null) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdSchoolReply()).orElseThrow();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(absentLetter.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow();
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(absentLetter.getTeacherReply());
            replyMobileObject.setCreatedDate(absentLetter.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    private void setReply2(UserPrincipal principal, AbsentLetter absentLetter, AbsentLetterDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow();
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(absentLetter.getConfirmContent());
            replyMobileObject.setCreatedDate(absentLetter.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }
        //nhà trường phản hồi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply()) && absentLetter.isSchoolReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdSchoolReply()).orElseThrow();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(absentLetter.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply()) && absentLetter.isTeacherReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow();
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(absentLetter.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);
    }

    private void setReply(UserPrincipal principal, AbsentLetter absentLetter, AbsentLetterDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (absentLetter.getIdConfirmType() != null && absentLetter.isConfirmStatus() && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdConfirmType()).orElseThrow();
            if (AppTypeConstant.SCHOOL.equals(absentLetter.getConfirmType()) || AppTypeConstant.SUPPER_SCHOOL.equals(absentLetter.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(absentLetter.getConfirmType()) && StringUtils.isNotBlank(absentLetter.getConfirmContent())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(absentLetter.getConfirmContent());
            replyMobileObject.setCreatedDate(absentLetter.getConfirmDate());
            replyMobileObjectList.add(replyMobileObject);
        }

        //nhà trường phản hồi
        if (absentLetter.getIdSchoolReply() != null && StringUtils.isNotBlank(absentLetter.getSchoolReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdSchoolReply()).orElseThrow();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(absentLetter.getSchoolReply());
            replyMobileObject.setCreatedDate(absentLetter.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (absentLetter.getIdTeacherReply() != null && StringUtils.isNotBlank(absentLetter.getTeacherReply()) && !absentLetter.isTeacherReplyDel()) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(absentLetter.getIdTeacherReply()).orElseThrow();
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(absentLetter.getTeacherReply());
            replyMobileObject.setCreatedDate(absentLetter.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(absentLetter.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);
    }

    @Transactional
    @Override
    public boolean createAbsent(UserPrincipal principal, AbsentMobileRequest absentMobileRequest) throws FirebaseMessagingException {
        AbsentLetter absentLetter = new AbsentLetter();
        this.checkData(absentMobileRequest, principal.getSchoolConfig(), principal.getIdKidLogin(), absentLetter);
        Long idSchool = principal.getIdSchoolLogin();
        absentLetter.setAbsentContent(absentMobileRequest.getContent());
        absentLetter.setCreatedBy(principal.getFullName());
        absentLetter.setToDate(absentMobileRequest.getToDate());
        absentLetter.setFromDate(absentMobileRequest.getFromDate());
        absentLetter.setIdSchool(idSchool);

        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow();
        absentLetter.setIdGrade(kids.getIdGrade());
        absentLetter.setIdClass(kids.getMaClass().getId());
        absentLetter.setKids(kids);

        List<AbsentDate> absentDateList = listMapper.mapList(absentMobileRequest.getAbsentDateList(), AbsentDate.class);

        AbsentLetter saveAbsent = absentLetterRepository.save(absentLetter);
        if (!CollectionUtils.isEmpty(absentMobileRequest.getMultipartFileList())) {
            this.addFile(idSchool, saveAbsent, absentMobileRequest.getMultipartFileList());
        }
        List<AbsentDate> absentDateSavedList = this.setAbsentDateForConfig(idSchool, saveAbsent, absentDateList, absentMobileRequest.getFromDate(), absentMobileRequest.getToDate());

        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(32L, kids, absentMobileRequest.getContent(), FirebaseConstant.CATEGORY_ABSENT);
        firebaseFunctionService.sendTeacherByKids(32L, kids, absentMobileRequest.getContent(), FirebaseConstant.CATEGORY_ABSENT);

        return true;
    }


    @Override
    public AbsentLetterDetailMobileResponse findDetailMessageTeacher(UserPrincipal principal, Long id) {
        return null;
    }

    /**
     * check dữ liệu truyền vào
     *
     * @param absentMobileRequest
     * @param schoolConfig
     * @param idKid
     * @return
     */
    private void checkData(AbsentMobileRequest absentMobileRequest, SchoolConfigResponse schoolConfig, Long idKid, AbsentLetter absentLetter) {
        LocalDate fromDate = absentMobileRequest.getFromDate();
        LocalDate toDate = absentMobileRequest.getToDate();
        int dateNumber = schoolConfig.getDateAbsent();
        LocalDate checkDate = LocalDate.now().plusDays(dateNumber);
        LocalTime checkTime = schoolConfig.getTimeAbsent();
        int rangeFromToValid = 30;
        long rangeFromToDate = Math.abs(ChronoUnit.DAYS.between(fromDate, toDate));
        if (rangeFromToDate > rangeFromToValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể tạo đơn có khoảng ngày lớn hơn " + rangeFromToValid);
        }
        int rangeDateValid = 30;
        long diffDate = Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), fromDate));
        if (diffDate > rangeDateValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể tạo đơn ngoài khoảng " + rangeDateValid + " ngày tính từ ngày bắt đầu");
        }
        //check ngày và giờ tạo đơn xin nghỉ so với config
        if (fromDate.isBefore(checkDate)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phải gửi xin nghỉ trước " + dateNumber + " ngày");
            absentLetter.setExpired(AppConstant.APP_TRUE);
        } else if (fromDate.isEqual(checkDate) && LocalDate.now().isEqual(fromDate)) {
            if (LocalTime.now().isAfter(checkTime)) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phải gửi xin nghỉ trước " + checkTime);
                absentLetter.setExpired(AppConstant.APP_TRUE);
            }
        }
        //check 3 buổi false
        long count = absentMobileRequest.getAbsentDateList().stream().filter(x -> !x.isAbsentMorning() && !x.isAbsentAfternoon() && !x.isAbsentEvening()).count();
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phải có ít nhất một buổi nghỉ");
        }
        //check số file
        if (!CollectionUtils.isEmpty(absentMobileRequest.getMultipartFileList()) && absentMobileRequest.getMultipartFileList().size() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tối đa 3 file");
        }
        //check khoảng thời gian so với các đơn xin nghỉ khác
        List<AbsentLetter> absentLetterList = absentLetterRepository.findFromDate(idKid, absentMobileRequest.getFromDate());
        for (AbsentLetter x : absentLetterList) {
            if (fromDate.isAfter(x.getToDate()) || toDate.isBefore(x.getFromDate())) {
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoảng thời gian đơn xin nghỉ đã tồn tại");
            }
        }
    }

    /**
     * create absentDate
     *
     * @param absentLetter
     * @param absentDateList
     * @param from
     * @param to
     * @return
     */
    private List<AbsentDate> setAbsentDateForConfig(Long idSchool, AbsentLetter absentLetter, List<AbsentDate> absentDateList, LocalDate from, LocalDate to) {
        AttendanceConfig attendanceConfig = attendanceConfigRepository.findAttendanceConfigFinal(idSchool).orElseThrow(() -> new NotFoundException("not foud attendanceDateConfig by id in cronjob"));
        List<AbsentDate> absentDateSavedList = new ArrayList<>();
        int number = ConvertData.convertDateToDay(from, to);
        for (int i = 0; i <= number; i++) {
            boolean morning;
            boolean afternoon;
            boolean evening;
            int finalI = i;
            List<AbsentDate> dateList = absentDateList.stream().filter(y -> y.getAbsentDate().isEqual(from.plusDays(finalI))).collect(Collectors.toList());
            AbsentDate absentDate;
            //ngày nghỉ mà bên mobile không truyền data vào
            if (CollectionUtils.isEmpty(dateList)) {
                absentDate = new AbsentDate();
            } else {
                //ngày học của trường
                absentDate = dateList.get(0);
                String dayOfWeek = absentDate.getAbsentDate().getDayOfWeek().toString();
                if (DateEnum.SATURDAY.toString().equals(dayOfWeek)) {
                    morning = attendanceConfig.isMorningSaturday();
                    afternoon = attendanceConfig.isAfternoonSaturday();
                    evening = attendanceConfig.isEveningSaturday();
                } else if (DateEnum.SUNDAY.toString().equals(dayOfWeek)) {
                    if (attendanceConfig.isSunday()) {
                        morning = attendanceConfig.isMorningAttendanceArrive();
                        afternoon = attendanceConfig.isAfternoonAttendanceArrive();
                        evening = attendanceConfig.isEveningAttendanceArrive();
                    } else {
                        morning = AppConstant.APP_FALSE;
                        afternoon = AppConstant.APP_FALSE;
                        evening = AppConstant.APP_FALSE;
                    }
                } else {
                    morning = attendanceConfig.isMorningAttendanceArrive();
                    afternoon = attendanceConfig.isAfternoonAttendanceArrive();
                    evening = attendanceConfig.isEveningAttendanceArrive();
                }
                //set các buổi đi học theo config
                if (morning) {
                    absentDate.setAbsentMorning(absentDate.isAbsentMorning());
                } else {
                    absentDate.setAbsentMorning(AppConstant.APP_FALSE);
                }
                if (afternoon) {
                    absentDate.setAbsentAfternoon(absentDate.isAbsentAfternoon());
                } else {
                    absentDate.setAbsentAfternoon(AppConstant.APP_FALSE);
                }
                if (evening) {
                    absentDate.setAbsentEvening(absentDate.isAbsentEvening());
                } else {
                    absentDate.setAbsentEvening(AppConstant.APP_FALSE);
                }
            }
            absentDate.setAbsentDate(from.plusDays(finalI));
            absentDate.setAbsentLetter(absentLetter);
            AbsentDate absentDateSaved = absentDateRepository.save(absentDate);
            absentDateSavedList.add(absentDateSaved);
        }
        return absentDateSavedList;
    }


    private void addFile(Long idSchool, AbsentLetter absentLetter, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.XIN_NGHI);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AbsentLetterAttachFile absentLetterAttachFile = new AbsentLetterAttachFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.XIN_NGHI) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.XIN_NGHI) + fileName;
                absentLetterAttachFile.setUrl(urlWeb);
                String nameFileSave = multipartFile.getOriginalFilename();
                absentLetterAttachFile.setName(nameFileSave);
                absentLetterAttachFile.setUrlLocal(urlLocal);
                absentLetterAttachFile.setAbsentLetter(absentLetter);
                absentLetterAttachFileRepository.save(absentLetterAttachFile);
            });
        }
    }


}
