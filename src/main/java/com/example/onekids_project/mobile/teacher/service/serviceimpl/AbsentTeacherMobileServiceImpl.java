package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.*;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.mobile.teacher.request.absentteacher.AbsentDateRequest;
import com.example.onekids_project.mobile.teacher.request.absentteacher.AbsentTeacherMobileRequest;
import com.example.onekids_project.mobile.teacher.response.absentteacher.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AbsentTeacherMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.attendanceemployee.AttendanceEmployeeConfigResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.example.onekids_project.validate.RequestValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * date 2021-05-21 5:17 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class AbsentTeacherMobileServiceImpl implements AbsentTeacherMobileService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AbsentTeacherRepository absentTeacherRepository;

    @Autowired
    private AbsentDateTeacherRepository absentTeacherDateRepository;

    @Autowired
    private AbsentTeacherAttachFileRepository absentTeacherAttachFileRepository;

    @Autowired
    private ConfigAttendanceEmployeeRepository configAttendanceEmployeeRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    /**
     * Tạo absent teacher
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    @Transactional
    @Override
    public boolean createAbsentTeacher(UserPrincipal principal, AbsentTeacherMobileRequest request) throws FirebaseMessagingException, IOException {
        Long idSchool = principal.getIdSchoolLogin();
        InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal);
        this.checkBeforeCreate(request, infoEmployeeSchool.getId());
        AbsentTeacher absentTeacher = modelMapper.map(request, AbsentTeacher.class);
        absentTeacher.setInfoEmployeeSchool(infoEmployeeSchool);
        absentTeacher.setIdSchool(idSchool);
        AbsentTeacher absentTeacherSaved = absentTeacherRepository.save(absentTeacher);
        if (CollectionUtils.isNotEmpty(request.getMultipartFileList())) {
            for (MultipartFile x : request.getMultipartFileList()) {
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(x, idSchool, UploadDownloadConstant.XIN_NGHI);
                AbsentTeacherAttackFile absentTeacherAttackFile = modelMapper.map(handleFileResponse, AbsentTeacherAttackFile.class);
                absentTeacherAttackFile.setUrl(handleFileResponse.getUrlWeb());
                absentTeacherAttackFile.setAbsentTeacher(absentTeacherSaved);
                absentTeacherAttachFileRepository.save(absentTeacherAttackFile);
            }
        }
        this.saveAbsentTeacherDate(absentTeacherSaved, request.getDateList(), infoEmployeeSchool.getId());
        this.sendFirebaseByCreate(idSchool, infoEmployeeSchool.getFullName());
        return true;
    }

    /**
     * Tìm kiếm đơn xin nghỉ
     *
     * @param principal
     * @param request
     * @return
     */
    @Override
    public ListAbsentTeacherMobileResponse searchAbsentTeacher(UserPrincipal principal, PageNumberRequest request) {
        InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal);
        Long idSchool = principal.getSchool().getId();
        List<AbsentTeacher> absentTeacherList = absentTeacherRepository.searchAbsentTeacherMobile(request, idSchool, infoEmployeeSchool.getId());
        ListAbsentTeacherMobileResponse response = new ListAbsentTeacherMobileResponse();
        List<AbsentTeacherMobileResponse> absentTeacherResponseList = new ArrayList<>();
        absentTeacherList.forEach(x -> {
            AbsentTeacherMobileResponse model = new AbsentTeacherMobileResponse();
            model.setId(x.getId());
            model.setContent(x.getContent().length() < 100 ? x.getContent() : x.getContent().substring(0, 100));
            int replyNumber = 0;
            if (StringUtils.isNotBlank(x.getConfirmContent())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getSchoolReply())) {
                replyNumber++;
            }
            model.setReplyNumber(replyNumber);
            model.setDateAbsent(ConvertData.convertDateToString(x.getFromDate(), x.getToDate()));
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setPictureNumber(x.getAbsentTeacherAttackFileList().size());
            model.setConfirmStatus(x.isConfirmStatus());
            model.setTeacherRead(x.isTeacherRead());
            absentTeacherResponseList.add(model);
        });
        boolean lastPage = absentTeacherList.size() < MobileConstant.MAX_PAGE_ITEM;
        response.setDataList(absentTeacherResponseList);
        response.setLastPage(lastPage);
        return response;
    }

    /**
     * Thu hồi đơn
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public boolean absentTeacherRevoke(Long id) {
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentTeacher by id"));
        if (absentTeacher.isConfirmStatus()) {
            return false;
        }
        absentTeacher.setDelActive(AppConstant.APP_FALSE);
        absentTeacherRepository.save(absentTeacher);
        return true;
    }

    /**
     * Chi tiết đơn xin nghỉ
     *
     * @param principal
     * @param id
     * @return
     */
    @Override
    public AbsentTeacherDetailMobileResponse findAbsentTeacherDetail(UserPrincipal principal, Long id) {
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found absentTeacher by id"));
        AbsentTeacherDetailMobileResponse response = new AbsentTeacherDetailMobileResponse();
        response.setFullName(absentTeacher.getInfoEmployeeSchool().getFullName());
        response.setAvatar(AvatarUtils.getAvatarInfoEmployee(absentTeacher.getInfoEmployeeSchool()));
        response.setContent(absentTeacher.getContent());
        response.setPictureList(absentTeacher.getAbsentTeacherAttackFileList().stream().map(AbsentTeacherAttackFile::getUrl).collect(Collectors.toList()));
        response.setDateAbsent(absentTeacher.getFromDate().isEqual(absentTeacher.getToDate()) ? ConvertData.convertDateToStringOne(absentTeacher.getFromDate()) : ConvertData.convertDateToString(absentTeacher.getFromDate(), absentTeacher.getToDate()));
        response.setConfirmStatus(absentTeacher.isConfirmStatus());
        response.setCreatedDate(absentTeacher.getCreatedDate());

        List<AbsentTeacherDateMobileResponse> dateList = new ArrayList<>();
        absentTeacher.getAbsentDateTeacherList().forEach(x -> {
            AbsentTeacherDateMobileResponse absentTeacherDateResponse = new AbsentTeacherDateMobileResponse();
            absentTeacherDateResponse.setDate(x.getDate());
            absentTeacherDateResponse.setMorning(x.isMorning());
            absentTeacherDateResponse.setAfternoon(x.isAfternoon());
            absentTeacherDateResponse.setEvening(x.isEvening());
            dateList.add(absentTeacherDateResponse);
        });
        response.setDateList(dateList);
        this.setReply(absentTeacher, response);
        absentTeacher.setTeacherRead(AppConstant.APP_TRUE);
        absentTeacherRepository.save(absentTeacher);
        return response;
    }

    private void setReply(AbsentTeacher absentTeacher, AbsentTeacherDetailMobileResponse response) {
        List<ReplyMobileObject> replyList = new ArrayList<>();
        //Khi có xác nhận
        if (absentTeacher.isConfirmStatus() && StringUtils.isNotBlank(absentTeacher.getConfirmContent())) {
            ReplyMobileObject replyObjectResponse = new ReplyMobileObject();
            replyObjectResponse.setFullName(MobileConstant.SCHOOL);
            replyObjectResponse.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyObjectResponse.setContent(absentTeacher.getConfirmContent());
            replyObjectResponse.setCreatedDate(absentTeacher.getConfirmDate());
            replyList.add(replyObjectResponse);
        }
        //Khi nhà trường phản hồi
        if (absentTeacher.getIdSchoolReply() != null && StringUtils.isNotBlank(absentTeacher.getSchoolReply())) {
            ReplyMobileObject replyObjectResponse = new ReplyMobileObject();
            replyObjectResponse.setFullName(MobileConstant.SCHOOL);
            replyObjectResponse.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyObjectResponse.setContent(absentTeacher.getSchoolReply());
            replyObjectResponse.setCreatedDate(absentTeacher.getSchoolTimeReply());
            replyObjectResponse.setModifyStatus(absentTeacher.isSchoolModifyStatus());
            replyList.add(replyObjectResponse);
        }
        response.setReplyList(replyList);
    }

    /**
     * create absentTeacherDate
     *
     * @param absentTeacher
     * @param dateList
     * @param idInfoEmployee
     * @return
     */
    private void saveAbsentTeacherDate(AbsentTeacher absentTeacher, List<AbsentDateRequest> dateList, Long idInfoEmployee) {
        dateList.forEach(x -> {
            ConfigAttendanceEmployee attendanceConfig = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(idInfoEmployee, x.getDate()).orElseThrow(() -> new NoSuchElementException("not found config attendance employee"));
            AttendanceEmployeeConfigResponse config = ConvertData.convertAttendanceEmployeeConfig(attendanceConfig, x.getDate());
            if (config.isMorning() || config.isAfternoon() || config.isEvening()) {
                if (!x.isMorning() && !x.isAfternoon() && !x.isEvening()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phải có ít nhất một buổi nghỉ");
                }
            }
            x.setMorning(config.isMorning() ? x.isMorning() : AppConstant.APP_FALSE);
            x.setAfternoon(config.isAfternoon() ? x.isAfternoon() : AppConstant.APP_FALSE);
            x.setEvening(config.isEvening() ? x.isEvening() : AppConstant.APP_FALSE);
            AbsentDateTeacher absentDateTeacher = modelMapper.map(x, AbsentDateTeacher.class);
            absentDateTeacher.setAbsentTeacher(absentTeacher);
            absentTeacherDateRepository.save(absentDateTeacher);

        });
    }

    /**
     * check dữ liệu truyền vào
     *
     * @param request
     * @param idInfoEmployee
     * @return
     */

    private void checkBeforeCreate(AbsentTeacherMobileRequest request, Long idInfoEmployee) {
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();
        RequestValidate.checkMaxfileInput(request.getMultipartFileList(), UploadDownloadConstant.MAX_FILE);
        if (CollectionUtils.isEmpty(request.getDateList())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể tạo xin nghỉ cho ngày nghỉ");
        }
        //check khoảng thời gian so với các đơn xin nghỉ khác
        List<AbsentTeacher> absentTeacherList = absentTeacherRepository.findFromDate(idInfoEmployee, fromDate, toDate);
        for (AbsentTeacher x : absentTeacherList) {
            if (fromDate.isAfter(x.getToDate()) || toDate.isBefore(fromDate)) {
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoảng thời gian đơn xin nghỉ đã tồn tại");
            }
        }

    }

    private void sendFirebaseByCreate(Long idSchool, String teacherName) throws FirebaseMessagingException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(73L).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndAppTypeAndEmployeeStatusAndDelActiveTrue(idSchool, AppTypeConstant.SCHOOL, EmployeeConstant.STATUS_WORKING);
        firebaseFunctionService.sendPlusCommon(infoEmployeeSchoolList, webSystemTitle.getTitle().replace("{full_name}", teacherName), "", idSchool, FirebaseConstant.CATEGORY_ABSENT_TEACHER);
    }

}
