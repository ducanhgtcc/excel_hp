package com.example.onekids_project.service.serviceimpl.absentteacher;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.employee.AbsentTeacher;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AbsentTeacherRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.absentteacher.SearchAbsentTeacherRequest;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.response.absentteacher.AbsentTeacherDetailResponse;
import com.example.onekids_project.response.absentteacher.AbsentTeacherResponse;
import com.example.onekids_project.response.absentteacher.ListAbsentTeacherResponse;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.absentteacher.AbsentTeacherService;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * date 2021-05-21 2:30 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class AbsentTeacherServiceImpl implements AbsentTeacherService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AbsentTeacherRepository absentTeacherRepository;

    @Autowired
    private AttendanceEmployeeService attendanceEmployeeService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Override
    public ListAbsentTeacherResponse searchAbsentTeacher(UserPrincipal principal, SearchAbsentTeacherRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<AbsentTeacher> absentTeacherList = absentTeacherRepository.searchAbsentTeacher(request, idSchool);
        ListAbsentTeacherResponse response = new ListAbsentTeacherResponse();
        List<AbsentTeacherResponse> absentTeacherResponseList = new ArrayList<>();
        absentTeacherList.forEach(x -> {
            AbsentTeacherResponse absentTeacherResponse = modelMapper.map(x, AbsentTeacherResponse.class);
            absentTeacherResponse.setNumberFile(x.getAbsentTeacherAttackFileList().size());
            absentTeacherResponse.setCreatedBy(x.getInfoEmployeeSchool().getFullName());
            absentTeacherResponseList.add(absentTeacherResponse);
        });
        response.setAbsentList(absentTeacherResponseList);
        long total = absentTeacherRepository.countTotalAccount(request, idSchool);
        response.setTotal(total);
        return response;
    }

    @Override
    public AbsentTeacherDetailResponse findByIdAbsentTeacher(UserPrincipal principal, Long id) {
        AbsentTeacher oldAbsentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        oldAbsentTeacher.setSchoolRead(AppConstant.APP_TRUE);
        absentTeacherRepository.save(oldAbsentTeacher);
        AbsentTeacherDetailResponse response = modelMapper.map(oldAbsentTeacher, AbsentTeacherDetailResponse.class);
        response.setCreatedBy(oldAbsentTeacher.getInfoEmployeeSchool().getFullName());
        List<FileResponse> fileResponseList = listMapper.mapList(oldAbsentTeacher.getAbsentTeacherAttackFileList(), FileResponse.class);
        response.setFileList(fileResponseList);
        return response;
    }

    @Transactional
    @Override
    public boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        this.setConfirm(absentTeacher, principal.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean updateConfirmMany(List<Long> request, UserPrincipal principal) {
        request.forEach(x -> {
            AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(x).orElseThrow();
            absentTeacher.setSchoolRead(AppConstant.APP_TRUE);
            try {
                this.setConfirm(absentTeacher, principal.getId());
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @Transactional
    @Override
    public boolean updateRead(List<Long> requestList) {
        requestList.forEach(x -> {
            AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndSchoolReadFalseAndDelActiveTrue(x).orElseThrow();
            absentTeacher.setSchoolRead(AppConstant.APP_TRUE);
            absentTeacherRepository.save(absentTeacher);
        });
        return true;
    }


    @Transactional
    @Override
    public boolean updateAbsentTeacher(UserPrincipal principal, ContentRequest request) throws FirebaseMessagingException {
        AbsentTeacher oldAbsentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        boolean checkSendFirebase = oldAbsentTeacher.getIdSchoolReply() == null;
        //check nếu chưa xác nhận thì xác nhận
        if (oldAbsentTeacher.getIdSchoolConfirm() == null) {
            oldAbsentTeacher.setConfirmStatus(AppConstant.APP_TRUE);
            oldAbsentTeacher.setConfirmContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
            oldAbsentTeacher.setConfirmDate(LocalDateTime.now());
            oldAbsentTeacher.setIdSchoolConfirm(principal.getId());
        }
        oldAbsentTeacher.setTeacherRead(AppConstant.APP_FALSE);
        oldAbsentTeacher.setSchoolModifyStatus(oldAbsentTeacher.getIdSchoolReply() != null);
        oldAbsentTeacher.setSchoolTimeReply(LocalDateTime.now());
        oldAbsentTeacher.setSchoolReply(request.getContent());
        oldAbsentTeacher.setIdSchoolReply(principal.getId());
        absentTeacherRepository.save(oldAbsentTeacher);
        if (checkSendFirebase) {
            //send firebase
            this.sendFirebaseByReply(oldAbsentTeacher.getIdSchool(), oldAbsentTeacher.getInfoEmployeeSchool().getId());
        }
        return true;
    }

    @Transactional
    @Override
    public boolean revokePlus(UserPrincipal principal, StatusRequest request) {
        AbsentTeacher absentTeacher = absentTeacherRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        absentTeacher.setSchoolReplyDel(request.getStatus());
        absentTeacherRepository.save(absentTeacher);
        return true;
    }

    private void setConfirm(AbsentTeacher absentTeacher, Long id) throws FirebaseMessagingException {
        absentTeacher.setConfirmStatus(AppConstant.APP_TRUE);
        absentTeacher.setTeacherRead(AppConstant.APP_FALSE);
        absentTeacher.setConfirmContent(MobileConstant.CONTENT_CONFIRM_SCHOOL);
        absentTeacher.setConfirmDate(LocalDateTime.now());
        absentTeacher.setIdSchoolConfirm(id);
        absentTeacherRepository.save(absentTeacher);
        //send fire base
        this.sendFirebaseByConfirm(absentTeacher.getIdSchool(), absentTeacher.getInfoEmployeeSchool().getId());
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

}
