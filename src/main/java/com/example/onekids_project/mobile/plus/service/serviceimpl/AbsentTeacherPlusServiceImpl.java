package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.AbsentTeacher;
import com.example.onekids_project.entity.employee.AbsentTeacherAttackFile;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.absentteacher.SearchAbsentTeacherPlusRequest;
import com.example.onekids_project.mobile.plus.response.absentteacher.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AbsentTeacherPlusService;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.mobile.teacher.response.absentteacher.AbsentTeacherDateMobileResponse;
import com.example.onekids_project.repository.AbsentTeacherRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.StringDataUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-05-31 8:53 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class AbsentTeacherPlusServiceImpl implements AbsentTeacherPlusService {

    @Autowired
    private AbsentTeacherRepository absentTeacherRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private AttendanceEmployeeService attendanceEmployeeService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    /**
     * Tìm kiếm
     * @param principal
     * @param request
     * @return response
     */
    @Override
    public ListAbsentTeacherPlusResponse searchAbsentTeacherPlus(UserPrincipal principal, SearchAbsentTeacherPlusRequest request) {
        Long idSchool = principal.getSchool().getId();
        List<AbsentTeacher> absentTeacherList = absentTeacherRepository.searchAbsentTeacherPlus(request, idSchool);
        ListAbsentTeacherPlusResponse response = new ListAbsentTeacherPlusResponse();
        List<AbsentTeacherPlusResponse> absentTeacherPlusResponseList = new ArrayList<>();
        absentTeacherList.forEach(x ->{
            AbsentTeacherPlusResponse model = new AbsentTeacherPlusResponse();
            model.setId(x.getId());
            model.setFullName(x.getInfoEmployeeSchool().getFullName());
            model.setContent(StringDataUtils.getSubStringLarge(x.getContent()));
            String avatar = AvatarUtils.getAvatarInfoEmployee(x.getInfoEmployeeSchool());
            model.setAvatar(avatar);
            int replyNumber = 0;
            if (StringUtils.isNotBlank(x.getConfirmContent())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getSchoolReply())) {
                replyNumber++;
            }
            model.setReplyNumber(replyNumber);
            model.setAbsentDate(ConvertData.convertDateToString(x.getFromDate(), x.getToDate()));
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setPictureNumber(x.getAbsentTeacherAttackFileList().size());
            model.setConfirmStatus(x.isConfirmStatus());
            model.setTeacherRead(x.isSchoolRead());
            absentTeacherPlusResponseList.add(model);
        });
        boolean lastPage = absentTeacherList.size() < MobileConstant.MAX_PAGE_ITEM;
        response.setDataList(absentTeacherPlusResponseList);
        response.setLastPage(lastPage);
        return response;
    }

    /**
     * Chi tiết
     * @param principal
     * @param id
     * @return response
     */
    @Override
    public AbsentTeacherPlusDetailResponse findDeTailPlusAbsentTeacher(UserPrincipal principal, Long id) {
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        AbsentTeacherPlusDetailResponse response = new AbsentTeacherPlusDetailResponse();
        response.setFullName(absentTeacher.getInfoEmployeeSchool().getFullName());
        response.setAvatar(AvatarUtils.getAvatarInfoEmployee(absentTeacher.getInfoEmployeeSchool()));
        response.setContent(absentTeacher.getContent());
        response.setPictureList(absentTeacher.getAbsentTeacherAttackFileList().stream().map(AbsentTeacherAttackFile::getUrl).collect(Collectors.toList()));
        response.setDateAbsent(absentTeacher.getFromDate().isEqual(absentTeacher.getToDate()) ? ConvertData.convertDateToStringOne(absentTeacher.getFromDate()) : ConvertData.convertDateToString(absentTeacher.getFromDate(), absentTeacher.getToDate()));
        response.setConfirmStatus(absentTeacher.isConfirmStatus());
        response.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentTeacher.getCreatedDate()));
        response.setCheckPlusReply(absentTeacher.getIdSchoolReply() != null);

        List<AbsentTeacherDateMobileResponse> dateList = listMapper.mapList(absentTeacher.getAbsentDateTeacherList(), AbsentTeacherDateMobileResponse.class);
        response.setAbsentDateList(dateList);

        response.setPictureList(absentTeacher.getAbsentTeacherAttackFileList().stream().map(AbsentTeacherAttackFile::getUrl).collect(Collectors.toList()));
        absentTeacher.setSchoolRead(AppConstant.APP_TRUE);
        absentTeacherRepository.save(absentTeacher);
        this.setReply(principal, absentTeacher, response);
        return response;
    }

    /**
     * Thu hồi phản hồi
     * @param principal
     * @param id
     * @return response
     */
    @Transactional
    @Override
    public AbsentTeacherPlusRevokeResponse sendRevoke(UserPrincipal principal, Long id) {
        AbsentTeacherPlusRevokeResponse response = new AbsentTeacherPlusRevokeResponse();
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if(StringUtils.isNotBlank(absentTeacher.getSchoolReply())){
            absentTeacher.setSchoolReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokePlus(principal, absentTeacher, response);
        }
        absentTeacher.setTeacherRead(AppConstant.APP_FALSE);
        absentTeacherRepository.save(absentTeacher);
        return response;
    }

    /**
     * Xác nhận xin nghỉ
     * @param principal
     * @param id
     * @return response
     */
    @Transactional
    @Override
    public AbsentTeacherPlusConfirmResponse absentTeacherPlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        AbsentTeacherPlusConfirmResponse response = new AbsentTeacherPlusConfirmResponse();
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        this.setConfirm(absentTeacher, principal.getId(), absentTeacher.getInfoEmployeeSchool().getId());
        this.setReplyConfirm(principal, absentTeacher, response);
        return response;
    }

    /**
     * Gửi phản hồi
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @Transactional
    @Override
    public AbsentTeacherPlusSendReplyResponse sendPlusReply(UserPrincipal principal, UpdatePlusSendReplyRequest request) throws FirebaseMessagingException {
        AbsentTeacherPlusSendReplyResponse response = new AbsentTeacherPlusSendReplyResponse();
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        boolean checkSendFirebase = absentTeacher.getIdSchoolReply() == null;
        response.setSchoolReply(request.getSchoolReply());
        response.setId(request.getId());
        //check nếu chưa xác nhận thì xác nhận
        if (StringUtils.isBlank(absentTeacher.getConfirmContent())) {
            absentTeacher.setConfirmStatus(AppConstant.APP_TRUE);
            absentTeacher.setConfirmDate(LocalDateTime.now());
            absentTeacher.setIdSchoolConfirm(principal.getId());
        }
        absentTeacher.setTeacherRead(AppConstant.APP_FALSE);
        absentTeacher.setSchoolModifyStatus(absentTeacher.getIdSchoolReply() != null);
        absentTeacher.setSchoolTimeReply(LocalDateTime.now());
        absentTeacher.setSchoolReply(request.getSchoolReply());
        absentTeacher.setIdSchoolReply(principal.getId());
        absentTeacher.setSchoolReplyDel(AppConstant.APP_FALSE);
        absentTeacherRepository.save(absentTeacher);
        if (checkSendFirebase) {
            //send firebase
            this.sendFirebaseByReply(absentTeacher.getIdSchool(), absentTeacher.getInfoEmployeeSchool().getId());
        }
        this.setReplySend(principal, absentTeacher, response);
        return response;
    }

    private void setConfirm(AbsentTeacher absentTeacher, Long id, Long idInfo) throws FirebaseMessagingException {
        absentTeacher.setConfirmStatus(AppConstant.APP_TRUE);
        absentTeacher.setTeacherRead(AppConstant.APP_FALSE);
        absentTeacher.setConfirmContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
        absentTeacher.setConfirmDate(LocalDateTime.now());
        absentTeacher.setIdSchoolConfirm(id);
        absentTeacherRepository.save(absentTeacher);
        //send fire base
        this.sendFirebaseByConfirm(absentTeacher.getIdSchool(), idInfo);
        //tạo dữ liệu bảng xin nghỉ
        attendanceEmployeeService.createAttendanceEmployeeFromConfirm(absentTeacher);
    }

    private void sendFirebaseByConfirm(Long idSchool, Long idInfo) throws FirebaseMessagingException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(74L).orElseThrow();
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
        firebaseFunctionService.sendTeacherCommon(Collections.singletonList(infoEmployeeSchool), webSystemTitle.getTitle(), "", idSchool, FirebaseConstant.CATEGORY_ABSENT_TEACHER);
    }

    private void sendFirebaseByReply(Long idSchool, Long idInfo) throws FirebaseMessagingException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(75L).orElseThrow();
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
        firebaseFunctionService.sendTeacherCommon(Collections.singletonList(infoEmployeeSchool), webSystemTitle.getTitle(), "", idSchool, FirebaseConstant.CATEGORY_ABSENT_TEACHER);
    }

    private void setReplyConfirm(UserPrincipal principal, AbsentTeacher absentTeacher, AbsentTeacherPlusConfirmResponse response) {
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(absentTeacher.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentTeacher.getConfirmDate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);

        response.setReplyList(replyMobileDateObjectList);
    }


    private void setReplyRevokePlus(UserPrincipal principal, AbsentTeacher absentTeacher, AbsentTeacherPlusRevokeResponse response) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentTeacher.getSchoolTimeReply()));
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(absentTeacher.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setModifyStatus(absentTeacher.isSchoolModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setStatusDel(absentTeacher.isSchoolReplyDel());
        replyMobileDateObjectList.add(replyMobileDateObject);
        response.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, AbsentTeacher absentTeacher, AbsentTeacherPlusDetailResponse response) {
        List<ReplyMobilePlusObject> replyList = new ArrayList<>();
        //Khi có xác nhận
        if (StringUtils.isNotBlank(absentTeacher.getConfirmContent())){
            ReplyMobilePlusObject replyMobilePlusObject = new ReplyMobilePlusObject();
            MaUser maUser = maUserRepository.findById(absentTeacher.getIdSchoolConfirm()).orElseThrow();
            replyMobilePlusObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobilePlusObject.setFullName(maUser.getFullName());
            replyMobilePlusObject.setContent(absentTeacher.getConfirmContent());
            replyMobilePlusObject.setKeyType(MobileConstant.TYPE_SCHOOL);
            replyMobilePlusObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentTeacher.getConfirmDate()));
            replyMobilePlusObject.setSortDate(absentTeacher.getConfirmDate());
            replyList.add(replyMobilePlusObject);
        }
        //Khi nhà trường phản hồi
        if(StringUtils.isNotBlank(absentTeacher.getSchoolReply())){
            ReplyMobilePlusObject replyMobilePlusObject = new ReplyMobilePlusObject();
            MaUser maUser = maUserRepository.findById(absentTeacher.getIdSchoolReply()).orElseThrow();
            replyMobilePlusObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobilePlusObject.setFullName(maUser.getFullName());
            replyMobilePlusObject.setKeyType(MobileConstant.TYPE_SCHOOL);
            replyMobilePlusObject.setContent(absentTeacher.isSchoolReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : absentTeacher.getSchoolReply());
            replyMobilePlusObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentTeacher.getSchoolTimeReply()));
            replyMobilePlusObject.setModifyStatus(absentTeacher.isSchoolModifyStatus());
            replyMobilePlusObject.setSchoolMoidifystatus(principal.getId().equals(absentTeacher.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            replyMobilePlusObject.setSortDate(absentTeacher.getSchoolTimeReply());
            replyMobilePlusObject.setStatusDel(absentTeacher.isSchoolReplyDel());
            replyList.add(replyMobilePlusObject);
        }
        response.setReplyList(replyList);
    }

    private void setReplySend(UserPrincipal principal, AbsentTeacher absentTeacher, AbsentTeacherPlusSendReplyResponse response) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(absentTeacher.getSchoolReply());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(absentTeacher.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(absentTeacher.isSchoolModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(absentTeacher.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(absentTeacher.isSchoolReplyDel());
        replyMobileDateObjectList.add(replyMobileDateObject);
        response.setReplyList(replyMobileDateObjectList);
    }
}
