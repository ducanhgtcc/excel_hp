package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.entity.employee.ConfigAttendanceEmployee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.mobile.plus.request.attendanceTeacher.*;
import com.example.onekids_project.mobile.plus.response.attendanceTeacher.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AttendanceTeacherPlusService;
import com.example.onekids_project.mobile.response.AttendanceStatusDayResponse;
import com.example.onekids_project.repository.AttendanceEmployeeRepository;
import com.example.onekids_project.repository.ConfigAttendanceEmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.request.attendanceemployee.AttendanceEmployeeConfigResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * date 2021-06-01 3:19 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class AttendanceTeacherPlusServiceImpl  implements AttendanceTeacherPlusService {

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private AttendanceEmployeeRepository attendanceEmployeeRepository;

    @Autowired
    private ConfigAttendanceEmployeeRepository configAttendanceEmployeeRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * Lấy dữ liệu thống kê
     * @param principal
     * @param localDate
     * @return
     */
    @Override
    public AttendanceDayTeacherPlusResponse searchAttendanceTeacherArrive(UserPrincipal principal, LocalDate localDate) {
        AttendanceDayTeacherPlusResponse response = new AttendanceDayTeacherPlusResponse();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), localDate);
        List<AttendanceEmployee> attendance = new ArrayList<>();
        List<AttendanceEmployee> attendanceOff = new ArrayList<>();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList){
            Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), localDate);
            if (this.checkAttendance(attendanceEmployee)){
                attendance.add(attendanceEmployee.get());
                if (!attendanceEmployee.get().isMorning() && !attendanceEmployee.get().isAfternoon() && !attendanceEmployee.get().isEvening()){
                    attendanceOff.add(attendanceEmployee.get());
                }
            }
        }
        response.setSumTeacher(infoEmployeeSchoolList.size());
        response.setSumTeacherAttendance(attendance.size());
        response.setSumTeacherOff(attendanceOff.size());
        response.setSumTeacherNoAttendance(infoEmployeeSchoolList.size() - attendance.size());
        return response;
    }

    /***
     * Lấy dữ liệu điểm danh đến
     * @param principal
     * @param date
     * @return
     */
    @Override
    public List<AttendanceTeacherArriveResponse> searchAttendanceArriveDetailDay(UserPrincipal principal, LocalDate date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        String checkNull = "";
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), date);
        List<AttendanceTeacherArriveResponse> responseList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x->{
            Optional<AttendanceEmployee> attendanceDetail = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            AttendanceTeacherArriveResponse model = new AttendanceTeacherArriveResponse();
            model.setId(x.getId());
            model.setTeacherName(x.getFullName());
            String avatar = StringUtils.isNotBlank(x.getAvatar()) ? x.getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER;
            model.setAvatar(avatar);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            if (attendanceEmployeeConfigResponse.isMorning()||attendanceEmployeeConfigResponse.isAfternoon()||attendanceEmployeeConfigResponse.isEvening()){
                if (attendanceDetail.isPresent()){
                    AttendanceEmployee attendanceEmployee = attendanceDetail.get();
                    model.setStatus(this.checkArriveHas(attendanceEmployee));
                    model.setPicture(attendanceEmployee.getArrivePicture() != null? attendanceEmployee.getArrivePicture(): checkNull);
                    model.setContent(attendanceEmployee.getArriveContent() != null? attendanceEmployee.getArriveContent(): checkNull);
                    model.setTime(attendanceEmployee.getArriveTime() != null? attendanceEmployee.getArriveTime().format(df): checkNull);
                    this.setAttendanceStatusDay(model, attendanceEmployeeConfigResponse, attendanceEmployee);
                }else {
                    model.setStatus(AppConstant.APP_FALSE);
                    model.setPicture(checkNull);
                    model.setContent(checkNull);
                    model.setTime(checkNull);
                    this.setAttendanceStatusDay(model, attendanceEmployeeConfigResponse, null);
                }
            }
            responseList.add(model);

        });
        return responseList;
    }

    /**
     * check status điểm danh của ngày hiện tại
     * @param principal
     * @return
     */
    @Override
    public StatusAttendanceTeacherDay checkAttendanceStatusTeacherArriveDay(UserPrincipal principal) {
        LocalDate date = LocalDate.now();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), date);
        StatusAttendanceTeacherDay response = new StatusAttendanceTeacherDay();
        int arriveFull = 0;
        int arriveEmpty = 0;
        int leaveFull = 0;
        int leaveEmpty = 0;
        int eatFull = 0;
        int eatEmpty = 0;
        for (InfoEmployeeSchool x : infoEmployeeSchoolList){
            Optional<AttendanceEmployee> attendanceDetail = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()){
                boolean checkArrive = this.checkAttendance(attendanceDetail);
                if (checkArrive){
                    arriveFull++;
                }else {
                    arriveEmpty++;
                }
                if (attendanceDetail.isPresent() && attendanceDetail.get().getLeaveTime() != null){
                    leaveFull++;
                }else {
                    leaveEmpty++;
                }
            }else {
                arriveEmpty++;
                leaveEmpty++;
            }
            if (attendanceEmployeeConfigResponse.isBreakfast() || attendanceEmployeeConfigResponse.isLunch() || attendanceEmployeeConfigResponse.isDinner()){
                boolean checkEat = this.checkEat(attendanceDetail);
                if (checkEat){
                    eatFull++;
                }else {
                    eatEmpty++;
                }
            }else {
                eatEmpty++;
            }
        }
        this.setCompareAttendance(response, infoEmployeeSchoolList, arriveFull, arriveEmpty, leaveFull, leaveEmpty, eatFull, eatEmpty);
        return response;
    }

    /**
     * Tạo điểm danh đến
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @Transactional
    @Override
    public List<AttendanceTeacherArriveResponse> createAttendanceTeacherArrive(UserPrincipal principal, AttendanceTeacherArriveRequest request) throws FirebaseMessagingException, IOException {
        LocalDate date = request.getDate();
        List<AttendanceTeacherArriveResponse> responseList = new ArrayList<>();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendance(date, principal.getSchoolConfig().getAgainAttendance());
        }
        for (Long idInfo : request.getIdList()){
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(idInfo, date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(idInfo, date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            AttendanceTeacherArriveResponse response = new AttendanceTeacherArriveResponse();
            response.setId(idInfo);
            if (attendanceEmployeeOptional.isPresent()){
                AttendanceEmployee attendanceEmployeeOld = attendanceEmployeeOptional.get();
                this.setAttendanceEmployeeArrive(response, attendanceEmployeeOld, request, principal, attendanceEmployeeConfigResponse);
            }else {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
                AttendanceEmployee attendanceEmployeeNew = new AttendanceEmployee();
                attendanceEmployeeNew.setDate(date);
                attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
                this.setAttendanceEmployeeArrive(response, attendanceEmployeeNew, request, principal, attendanceEmployeeConfigResponse);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * Tạo điểm danh đến many
     * @param principal
     * @param requestList
     * @return
     * @throws FirebaseMessagingException
     */
    @Transactional
    @Override
    public List<AttendanceTeacherArriveResponse> createAttendanceTeacherArriveMany(UserPrincipal principal, ArriveManyRequest requestList) throws FirebaseMessagingException, IOException {

        LocalDate date = requestList.getDataList().get(0).getDate();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendance(date, principal.getSchoolConfig().getAgainAttendance());
        }
        List<AttendanceTeacherArriveResponse> responseList = new ArrayList<>();
        for (AttendanceTeacherArriveManyRequest request : requestList.getDataList()){
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(request.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(request.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            AttendanceTeacherArriveResponse response = new AttendanceTeacherArriveResponse();
            response.setId(request.getId());
            if (attendanceEmployeeOptional.isPresent()){
                AttendanceEmployee attendanceEmployeeOld = attendanceEmployeeOptional.get();
                AttendanceTeacherArriveRequest arriveRequest = modelMapper.map(request, AttendanceTeacherArriveRequest.class);
                this.setAttendanceEmployeeArrive(response, attendanceEmployeeOld, arriveRequest, principal, attendanceEmployeeConfigResponse);
            }else {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
                AttendanceEmployee attendanceEmployeeNew = new AttendanceEmployee();
                attendanceEmployeeNew.setDate(date);
                attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
                AttendanceTeacherArriveRequest arriveRequest = modelMapper.map(request, AttendanceTeacherArriveRequest.class);
                this.setAttendanceEmployeeArrive(response, attendanceEmployeeNew, arriveRequest, principal, attendanceEmployeeConfigResponse);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * Thống kê điểm danh về
     * @param principal
     * @param localDate
     * @return
     */
    @Override
    public AttendanceDayTeacherPlusResponse searchAttendanceTeacherLeave(UserPrincipal principal, LocalDate localDate) {
        AttendanceDayTeacherPlusResponse response = new AttendanceDayTeacherPlusResponse();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), localDate);
        List<AttendanceEmployee> attendance = new ArrayList<>();
        List<AttendanceEmployee> attendanceOff = new ArrayList<>();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList){
            Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), localDate);
            if (this.checkAttendance(attendanceEmployee)){
                if (attendanceEmployee.get().getLeaveTime() != null){
                    attendance.add(attendanceEmployee.get());
                }
                if (!attendanceEmployee.get().isMorning() && !attendanceEmployee.get().isAfternoon() && !attendanceEmployee.get().isEvening()){
                    attendanceOff.add(attendanceEmployee.get());
                }
            }
        }
        response.setSumTeacher(infoEmployeeSchoolList.size());
        response.setSumTeacherAttendance(attendance.size());
        response.setSumTeacherOff(attendanceOff.size());
        response.setSumTeacherNoAttendance(infoEmployeeSchoolList.size() - attendance.size());
        return response;
    }

    /**
     * Lấy DL Chấm công về, thay đổi ngày được chọn
     * @param principal
     * @param date
     * @return
     */
    @Override
    public List<AttendanceTeacherLeaveResponse> searchAttendanceLeaveDetailDay(UserPrincipal principal, LocalDate date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        String checkNull = "";
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), date);
        List<AttendanceTeacherLeaveResponse> responseList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x->{
            Optional<AttendanceEmployee> attendanceDetail = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            AttendanceTeacherLeaveResponse model = new AttendanceTeacherLeaveResponse();
            model.setId(x.getId());
            model.setTeacherName(x.getFullName());
            String avatar = StringUtils.isNotBlank(x.getAvatar()) ? x.getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER;
            model.setAvatar(avatar);
            if (attendanceDetail.isPresent()){
                AttendanceEmployee attendanceEmployee = attendanceDetail.get();
                model.setStatus(this.checkLeaveHas(attendanceEmployee));
                model.setPicture(attendanceEmployee.getLeavePicture() != null? attendanceEmployee.getLeavePicture(): checkNull);
                model.setContent(attendanceEmployee.getLeaveContent() != null? attendanceEmployee.getLeaveContent(): checkNull);
                model.setTime(attendanceEmployee.getLeaveTime() != null? attendanceEmployee.getLeaveTime().format(df): checkNull);
                if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()){
                    if (attendanceEmployee.isMorning() || attendanceEmployee.isAfternoon() || attendanceEmployee.isEvening()){
                        model.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                    }else {
                        model.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                    }
                }else {
                    model.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                }
            }else {
                model.setStatus(AppConstant.APP_FALSE);
                model.setPicture(checkNull);
                model.setContent(checkNull);
                model.setTime(checkNull);
                model.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
            }
            responseList.add(model);
        });
        return responseList;
    }

    /**
     * Tạo điểm danh về
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    @Transactional
    @Override
    public List<AttendanceTeacherLeaveResponse> createAttendanceTeacherLeave(UserPrincipal principal, AttendanceTeacherLeaveRequest request) throws FirebaseMessagingException, IOException {
        LocalDate date = request.getDate();
        List<AttendanceTeacherLeaveResponse> responseList = new ArrayList<>();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendance(date, principal.getSchoolConfig().getAgainAttendance());
        }
        for (Long idInfo : request.getIdList()){
            AttendanceTeacherLeaveResponse response = new AttendanceTeacherLeaveResponse();
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(idInfo, date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(idInfo, date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            response.setId(idInfo);
            if (attendanceEmployeeOptional.isPresent()){
                AttendanceEmployee attendanceEmployeeNew = attendanceEmployeeOptional.get();
                this.setAttendanceEmployeeLeave(request, principal, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }else {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
                AttendanceEmployee attendanceEmployeeNew = new AttendanceEmployee();
                attendanceEmployeeNew.setDate(date);
                attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
                this.setAttendanceEmployeeLeave(request, principal, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * Tạo điểm danh về many
     * @param principal
     * @param requestList
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    @Transactional
    @Override
    public List<AttendanceTeacherLeaveResponse> createAttendanceTeacherLeaveMany(UserPrincipal principal, LeaveManyRequest requestList) throws FirebaseMessagingException, IOException {
        LocalDate date = requestList.getDataList().get(0).getDate();
        List<AttendanceTeacherLeaveResponse> responseList = new ArrayList<>();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendance(date, principal.getSchoolConfig().getAgainAttendance());
        }
        for (AttendanceTeacherLeaveManyRequest request : requestList.getDataList()){
            AttendanceTeacherLeaveResponse response = new AttendanceTeacherLeaveResponse();
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(request.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(request.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            response.setId(request.getId());
            if (attendanceEmployeeOptional.isPresent()){
                AttendanceEmployee attendanceEmployeeNew = attendanceEmployeeOptional.get();
                AttendanceTeacherLeaveRequest leaveRequest = modelMapper.map(request, AttendanceTeacherLeaveRequest.class);
                this.setAttendanceEmployeeLeave(leaveRequest, principal, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }else {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
                AttendanceEmployee attendanceEmployeeNew = new AttendanceEmployee();
                attendanceEmployeeNew.setDate(date);
                attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
                AttendanceTeacherLeaveRequest leaveRequest = modelMapper.map(request, AttendanceTeacherLeaveRequest.class);
                this.setAttendanceEmployeeLeave(leaveRequest, principal, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * thống kê điểm danh ăn
     * @param principal
     * @param localDate
     * @return
     */
    @Override
    public AttendanceDayTeacherPlusResponse searchAttendanceTeacherEat(UserPrincipal principal, LocalDate localDate) {
        AttendanceDayTeacherPlusResponse response = new AttendanceDayTeacherPlusResponse();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), localDate);
        List<AttendanceEmployee> attendance = new ArrayList<>();
        List<AttendanceEmployee> attendanceOff = new ArrayList<>();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList){
            Optional<AttendanceEmployee> attendanceEmployee = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), localDate);
            if (attendanceEmployee.isPresent()){
                if (attendanceEmployee.get().isBreakfast() || attendanceEmployee.get().isLunch() || attendanceEmployee.get().isDinner()){
                    attendance.add(attendanceEmployee.get());
                }
                if (!attendanceEmployee.get().isMorning() && !attendanceEmployee.get().isAfternoon() && !attendanceEmployee.get().isEvening()){
                    attendanceOff.add(attendanceEmployee.get());
                }
            }
        }
        response.setSumTeacher(infoEmployeeSchoolList.size());
        response.setSumTeacherAttendance(attendance.size());
        response.setSumTeacherOff(attendanceOff.size());
        response.setSumTeacherNoAttendance(infoEmployeeSchoolList.size() - attendance.size());
        return response;
    }

    @Override
    public List<AttendanceTeacherEatResponse> searchAttendanceEatDetailDay(UserPrincipal principal, LocalDate date) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeTimeLineWithDate(principal.getIdSchoolLogin(), date);
        List<AttendanceTeacherEatResponse> responseList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x->{
            Optional<AttendanceEmployee> attendanceDetail = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(x.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(x.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            if (attendanceEmployeeConfigResponse.isBreakfast()||attendanceEmployeeConfigResponse.isLunch()||attendanceEmployeeConfigResponse.isDinner()){
                AttendanceTeacherEatResponse model = new AttendanceTeacherEatResponse();
                model.setId(x.getId());
                model.setTeacherName(x.getFullName());
                String avatar = StringUtils.isNotBlank(x.getAvatar()) ? x.getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER;
                model.setAvatar(avatar);
                if (attendanceDetail.isPresent()){
                    AttendanceEmployee attendanceEmployee = attendanceDetail.get();
                    model.setStatus(this.checkEatHas(attendanceEmployee));
                    if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()){
                        if (attendanceEmployee.isMorning() || attendanceEmployee.isAfternoon() || attendanceEmployee.isEvening()){
                            model.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                        }else if (attendanceEmployee.isMorningYes() || attendanceEmployee.isMorningNo() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isAfternoonNo() || attendanceEmployee.isEveningYes() || attendanceEmployee.isEveningNo()){
                            model.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                        } else {
                            model.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                        }
                    }else {
                        model.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                    }
                    model.setBreakfast(attendanceEmployeeConfigResponse.isBreakfast() ? attendanceEmployee.isBreakfast() : null);
                    model.setLunch(attendanceEmployeeConfigResponse.isLunch()? attendanceEmployee.isLunch(): null);
                    model.setDinner(attendanceEmployeeConfigResponse.isDinner()? attendanceEmployee.isDinner(): null);
                }else {
                    model.setStatus(AppConstant.APP_FALSE);
                    model.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                    model.setBreakfast(attendanceEmployeeConfigResponse.isBreakfast()? AppConstant.APP_FALSE: null);
                    model.setLunch(attendanceEmployeeConfigResponse.isLunch()? AppConstant.APP_FALSE: null);
                    model.setDinner(attendanceEmployeeConfigResponse.isDinner()? AppConstant.APP_FALSE: null);
                }
                responseList.add(model);
            }
        });
        return responseList;
    }

    @Override
    public List<AttendanceTeacherEatResponse> createAttendanceTeacherEat(UserPrincipal principal, AttendanceTeacherEatRequest request) throws FirebaseMessagingException {
        LocalDate date = request.getDate();
        // ngày điểm danh phải lớn hơn ngày config
        List<AttendanceTeacherEatResponse> responseList = new ArrayList<>();
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendance(date, principal.getSchoolConfig().getAgainAttendance());
        }
        for (Long idInfo : request.getIdList()){
            AttendanceTeacherEatResponse response = new AttendanceTeacherEatResponse();
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(idInfo, date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(idInfo, date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            response.setId(idInfo);
            if (attendanceEmployeeOptional.isPresent()){
                AttendanceEmployee attendanceEmployeeNew = attendanceEmployeeOptional.get();
                this.setAttendanceEmployeeEat(request, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }else {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(idInfo).orElseThrow();
                AttendanceEmployee attendanceEmployeeNew = new AttendanceEmployee();
                attendanceEmployeeNew.setDate(date);
                attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
                this.setAttendanceEmployeeEat(request, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * Gửi điểm danh ăn many
     * @param principal
     * @param requestList
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    @Override
    public List<AttendanceTeacherEatResponse> createAttendanceTeacherEatMany(UserPrincipal principal, EatManyRequest requestList) throws FirebaseMessagingException {
        LocalDate date = requestList.getDataList().get(0).getDate();
        // ngày điểm danh phải lớn hơn ngày config
        List<AttendanceTeacherEatResponse> responseList = new ArrayList<>();
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendance(date, principal.getSchoolConfig().getAgainAttendance());
        }
        for (AttendanceTeacherEatManyRequest request : requestList.getDataList()){
            AttendanceTeacherEatResponse response = new AttendanceTeacherEatResponse();
            Optional<AttendanceEmployee> attendanceEmployeeOptional = attendanceEmployeeRepository.findByInfoEmployeeSchool_IdAndDate(request.getId(), date);
            ConfigAttendanceEmployee configAttendanceEmployee = configAttendanceEmployeeRepository.findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(request.getId(), date).orElseThrow();
            AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse = ConvertData.convertAttendanceEmployeeConfig(configAttendanceEmployee, date);
            response.setId(request.getId());
            if (attendanceEmployeeOptional.isPresent()){
                AttendanceEmployee attendanceEmployeeNew = attendanceEmployeeOptional.get();
                AttendanceTeacherEatRequest eatRequest = modelMapper.map(request, AttendanceTeacherEatRequest.class);
                this.setAttendanceEmployeeEat(eatRequest, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }else {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
                AttendanceEmployee attendanceEmployeeNew = new AttendanceEmployee();
                attendanceEmployeeNew.setDate(date);
                attendanceEmployeeNew.setInfoEmployeeSchool(infoEmployeeSchool);
                AttendanceTeacherEatRequest eatRequest = modelMapper.map(request, AttendanceTeacherEatRequest.class);
                this.setAttendanceEmployeeEat(eatRequest, attendanceEmployeeNew, response, attendanceEmployeeConfigResponse);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * Set Arrive cho mobile và save
     * @param response
     * @param attendanceEmployeeOld
     * @param request
     * @param principal
     * @param attendanceEmployeeConfigResponse
     */
    private void setAttendanceEmployeeArrive(AttendanceTeacherArriveResponse response, AttendanceEmployee attendanceEmployeeOld, AttendanceTeacherArriveRequest request, UserPrincipal principal, AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse) throws IOException {
        String checkNull = "";
        if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()){
            attendanceEmployeeOld.setArriveContent(request.getContent());
            if (request.getPicture() != null){
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                String urlWeb = handleFileResponse.getUrlWeb();
                String urlLocal = handleFileResponse.getUrlLocal();
                attendanceEmployeeOld.setArrivePicture(urlWeb);
                attendanceEmployeeOld.setArriveLocalPicture(urlLocal);
                response.setPicture(urlWeb);
            }else {
                if (request.isDeletePicture()) {
                    attendanceEmployeeOld.setArrivePicture(null);
                }
            }
            if (attendanceEmployeeConfigResponse.isMorning() && request.getAttendanceMorning() != null){
                attendanceEmployeeOld.setMorning(request.getAttendanceMorning().isAbsentLetter());
                attendanceEmployeeOld.setMorningYes(request.getAttendanceMorning().isAbsentLetterYes());
                attendanceEmployeeOld.setMorningNo(request.getAttendanceMorning().isAbsentLetterNo());
            }
            if (attendanceEmployeeConfigResponse.isAfternoon() && request.getAttendanceMorning() != null){
                attendanceEmployeeOld.setAfternoon(request.getAttendanceAfternoon().isAbsentLetter());
                attendanceEmployeeOld.setAfternoonYes(request.getAttendanceAfternoon().isAbsentLetterYes());
                attendanceEmployeeOld.setAfternoonNo(request.getAttendanceAfternoon().isAbsentLetterNo());
            }
            if (attendanceEmployeeConfigResponse.isEvening() && request.getAttendanceEvening() != null){
                attendanceEmployeeOld.setEvening(request.getAttendanceEvening().isAbsentLetter());
                attendanceEmployeeOld.setEveningYes(request.getAttendanceEvening().isAbsentLetterYes());
                attendanceEmployeeOld.setEveningNo(request.getAttendanceEvening().isAbsentLetterNo());
            }
            if (attendanceEmployeeOld.isMorning() || attendanceEmployeeOld.isAfternoon() || attendanceEmployeeOld.isEvening()){
                if (StringUtils.isNotEmpty(request.getTime())){
                    LocalTime time = LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                    attendanceEmployeeOld.setArriveTime(time);
                    attendanceEmployeeOld.setMinuteArriveLate((int)this.getMinuteArriveLate(time, principal));
                }else {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                    String timeNew = df.format(LocalTime.now());
                    LocalTime time = LocalTime.parse(timeNew);
                    if (attendanceEmployeeOld.getArriveTime() == null) {
                        attendanceEmployeeOld.setArriveTime(time);
                        attendanceEmployeeOld.setMinuteArriveLate((int)this.getMinuteArriveLate(time, principal));
                    }
                }
            }else {
                attendanceEmployeeOld.setArriveTime(null);
            }
            AttendanceEmployee attendanceEmployee = attendanceEmployeeRepository.save(attendanceEmployeeOld);
            //Send Firebase
            //du liệu trả về
            this.setAttendanceStatusDay(response, attendanceEmployeeConfigResponse, attendanceEmployee);
            response.setTime(attendanceEmployee.getArriveTime() != null? attendanceEmployee.getArriveTime().toString(): checkNull);
            response.setPicture(attendanceEmployee.getArrivePicture() != null? attendanceEmployee.getArrivePicture(): checkNull);
            String avatar = StringUtils.isNotBlank(attendanceEmployee.getInfoEmployeeSchool().getAvatar()) ? attendanceEmployee.getInfoEmployeeSchool().getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER;
            response.setAvatar(avatar);
            response.setStatus(this.checkArriveHas(attendanceEmployee));
            response.setTeacherName(attendanceEmployee.getInfoEmployeeSchool().getFullName());
            response.setContent(attendanceEmployee.getArriveContent() != null? attendanceEmployee.getArriveContent(): checkNull);
        }
    }

    /**
     * Set Leave cho mobile và save
     * @param request
     * @param principal
     * @param attendanceEmployeeNew
     * @param response
     * @param attendanceEmployeeConfigResponse
     * @throws IOException
     */
    private void setAttendanceEmployeeLeave(AttendanceTeacherLeaveRequest request, UserPrincipal principal, AttendanceEmployee attendanceEmployeeNew, AttendanceTeacherLeaveResponse response,AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse) throws IOException {
        String checkNull = "";
        if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()) {
            attendanceEmployeeNew.setLeaveContent(request.getContent());
            if (request.getPicture() != null) {
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                String urlWeb = handleFileResponse.getUrlWeb();
                String urlLocal = handleFileResponse.getUrlLocal();
                attendanceEmployeeNew.setLeavePicture(urlWeb);
                attendanceEmployeeNew.setLeaveLocalPicture(urlLocal);
                response.setPicture(urlWeb);
            } else {
                if (request.isDeletePicture()) {
                    attendanceEmployeeNew.setLeavePicture(null);
                }
            }
            if (attendanceEmployeeNew.isMorning() || attendanceEmployeeNew.isAfternoon() || attendanceEmployeeNew.isEvening()) {
                if (StringUtils.isNotEmpty(request.getTime())) {
                    LocalTime time = LocalTime.parse(request.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                    attendanceEmployeeNew.setLeaveTime(time);
                    attendanceEmployeeNew.setMinuteLeaveSoon((int) this.getMinuteLeaveSoon(time, principal));
                } else {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                    String timeNew = df.format(LocalTime.now());
                    LocalTime time = LocalTime.parse(timeNew);
                    if (attendanceEmployeeNew.getLeaveTime() == null) {
                        attendanceEmployeeNew.setLeaveTime(time);
                        attendanceEmployeeNew.setMinuteLeaveSoon((int) this.getMinuteLeaveSoon(time, principal));
                    }
                }
            } else {
                attendanceEmployeeNew.setArriveTime(null);
            }
            AttendanceEmployee attendanceEmployee = attendanceEmployeeRepository.save(attendanceEmployeeNew);
            //Send Firebase
            //du liệu trả về
            response.setTime(attendanceEmployee.getLeaveTime() != null? attendanceEmployee.getLeaveTime().toString(): checkNull);
            response.setPicture(attendanceEmployee.getLeavePicture() != null? attendanceEmployee.getLeavePicture(): checkNull);
            if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()){
                if (attendanceEmployee.isMorning() || attendanceEmployee.isAfternoon() || attendanceEmployee.isEvening()) {
                    response.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                }else if(attendanceEmployee.isMorningYes() || attendanceEmployee.isMorningNo() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isAfternoonNo() || attendanceEmployee.isEveningYes() || attendanceEmployee.isEveningNo()) {
                    response.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                }else {
                    response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                }
            }else {
                response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
            }
            String avatar = StringUtils.isNotBlank(attendanceEmployee.getInfoEmployeeSchool().getAvatar()) ? attendanceEmployee.getInfoEmployeeSchool().getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER;
            response.setAvatar(avatar);
            response.setStatus(this.checkLeaveHas(attendanceEmployee));
            response.setTeacherName(attendanceEmployee.getInfoEmployeeSchool().getFullName());
            response.setContent(attendanceEmployee.getLeaveContent() != null? attendanceEmployee.getLeaveContent(): checkNull);
        }else {
            response.setStatus(AppConstant.APP_FALSE);
            response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
        }
    }

    /**
     * Set eat cho mobile và save
     * @param request
     * @param attendanceEmployeeNew
     * @param response
     * @param attendanceEmployeeConfigResponse
     * @throws IOException
     */
    private void setAttendanceEmployeeEat(AttendanceTeacherEatRequest request, AttendanceEmployee attendanceEmployeeNew, AttendanceTeacherEatResponse response,AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse) {
        if (attendanceEmployeeConfigResponse.isBreakfast() || attendanceEmployeeConfigResponse.isLunch() || attendanceEmployeeConfigResponse.isDinner()) {
            attendanceEmployeeNew.setBreakfast(attendanceEmployeeConfigResponse.isBreakfast()? request.isBreakfast(): AppConstant.APP_FALSE);
            attendanceEmployeeNew.setLunch(attendanceEmployeeConfigResponse.isLunch()? request.isLunch(): AppConstant.APP_FALSE);
            attendanceEmployeeNew.setDinner(attendanceEmployeeConfigResponse.isDinner()? request.isDinner(): AppConstant.APP_FALSE);
            AttendanceEmployee attendanceEmployee = attendanceEmployeeRepository.save(attendanceEmployeeNew);
            //Send Firebase
            //du liệu trả về
            if (attendanceEmployeeConfigResponse.isMorning() || attendanceEmployeeConfigResponse.isAfternoon() || attendanceEmployeeConfigResponse.isEvening()){
                if (attendanceEmployee.isMorning() || attendanceEmployee.isAfternoon() || attendanceEmployee.isEvening()) {
                    response.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                }else if(attendanceEmployee.isMorningYes() || attendanceEmployee.isMorningNo() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isAfternoonNo() || attendanceEmployee.isEveningYes() || attendanceEmployee.isEveningNo()){
                    response.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                }else {
                    response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                }
            }else {
                response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
            }
            String avatar = StringUtils.isNotBlank(attendanceEmployee.getInfoEmployeeSchool().getAvatar()) ? attendanceEmployee.getInfoEmployeeSchool().getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER;
            response.setAvatar(avatar);
            response.setStatus(this.checkEatHas(attendanceEmployee));
            response.setTeacherName(attendanceEmployee.getInfoEmployeeSchool().getFullName());
            response.setBreakfast(attendanceEmployeeConfigResponse.isBreakfast()? attendanceEmployee.isBreakfast(): null);
            response.setLunch(attendanceEmployeeConfigResponse.isLunch()? attendanceEmployee.isLunch(): null);
            response.setDinner(attendanceEmployeeConfigResponse.isDinner()? attendanceEmployee.isDinner(): null);
        }else {
            response.setStatus(AppConstant.APP_FALSE);
        }
    }

    private long getMinuteArriveLate(LocalTime time, UserPrincipal principal){
        long timeCv = ConvertData.convertLocalTimeToLong(time);
        long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimeAttendanceArrive());
        return (timeCv > timeCf)? (timeCv - timeCf) : 0;
    }

    private long getMinuteLeaveSoon(LocalTime time, UserPrincipal principal){
        long timeCv = ConvertData.convertLocalTimeToLong(time);
        long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimeAttendanceLeave());
        return (timeCv > timeCf)? (timeCv - timeCf) : 0;
    }


    private void checkAgainAttendance(LocalDate date, int day) {
        // ngày điểm danh phải lớn hơn ngày config
        LocalDate dateCheck = LocalDate.now().minusDays(day);
        if (date.isBefore(dateCheck)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không được điểm danh muộn quá " + day + " ngày");
        }
    }

    /**
     * Check đã điểm danh buổi nao hay chưa
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkArriveHas(AttendanceEmployee attendanceEmployee) {
        return attendanceEmployee.isMorning() || attendanceEmployee.isMorningYes() || attendanceEmployee.isMorningNo() ||
                attendanceEmployee.isAfternoon() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isAfternoonNo() ||
                attendanceEmployee.isEvening() || attendanceEmployee.isEveningYes() || attendanceEmployee.isEveningNo();
    }

    /**
     * Check đã điểm danh về chưa
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkLeaveHas(AttendanceEmployee attendanceEmployee) {
        return attendanceEmployee.getLeaveTime() != null;
    }

    /**
     * Check đã điểm danh ăn chưa
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkEatHas(AttendanceEmployee attendanceEmployee) {
        return attendanceEmployee.isBreakfast() || attendanceEmployee.isLunch() || attendanceEmployee.isDinner();
    }

    private void setCompareAttendance(StatusAttendanceTeacherDay response,List<InfoEmployeeSchool> infoEmployeeSchoolList, int arriveFull, int arriveEmpty, int leaveFull, int leaveEmpty, int eatFull, int eatEmpty){
        if (infoEmployeeSchoolList.size() == arriveFull){
            response.setArrive(AttendanceConstant.FULL);
        }else if(infoEmployeeSchoolList.size() == arriveEmpty){
            response.setArrive(AttendanceConstant.EMPTY);
        }else {
            response.setArrive(AttendanceConstant.PART);
        }
        if (infoEmployeeSchoolList.size() == leaveFull) {
            response.setLeave(AttendanceConstant.FULL);
        } else if (infoEmployeeSchoolList.size() == leaveEmpty) {
            response.setLeave(AttendanceConstant.EMPTY);
        } else {
            response.setLeave(AttendanceConstant.PART);
        }
        if (infoEmployeeSchoolList.size() == eatFull) {
            response.setEat(AttendanceConstant.FULL);
        } else if (infoEmployeeSchoolList.size() == eatEmpty) {
            response.setEat(AttendanceConstant.EMPTY);
        } else {
            response.setEat(AttendanceConstant.PART);
        }
    }

    /**
     * check đã điểm danh hay chưa( đi làm or nghỉ)
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkAttendance(Optional<AttendanceEmployee> attendanceEmployee) {
        if (attendanceEmployee.isEmpty()) {
            return false;
        } else
            return attendanceEmployee.isPresent() && (attendanceEmployee.get().isAfternoon() || attendanceEmployee.get().isMorning() || attendanceEmployee.get().isEvening() || attendanceEmployee.get().isEveningNo() || attendanceEmployee.get().isEveningYes() || attendanceEmployee.get().isMorningNo() || attendanceEmployee.get().isMorningYes() || attendanceEmployee.get().isAfternoonNo() || attendanceEmployee.get().isAfternoonYes());
    }

    /**
     * check ăn
     *
     * @param attendanceEmployee
     * @return
     */
    private boolean checkEat(Optional<AttendanceEmployee> attendanceEmployee) {
        if (attendanceEmployee.isEmpty()) {
            return false;
        } else
            return attendanceEmployee.isPresent() && (attendanceEmployee.get().isBreakfast() || attendanceEmployee.get().isLunch() || attendanceEmployee.get().isDinner());
    }

    private void setAttendanceStatusDay(AttendanceTeacherArriveResponse model, AttendanceEmployeeConfigResponse attendanceEmployeeConfigResponse, AttendanceEmployee attendanceEmployee){

        if (attendanceEmployee != null){
            if (attendanceEmployeeConfigResponse.isMorning()){
                AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                attendanceStatusDayResponse.setAbsentLetter(attendanceEmployee.isMorning());
                attendanceStatusDayResponse.setAbsentLetterNo(attendanceEmployee.isMorningNo());
                attendanceStatusDayResponse.setAbsentLetterYes(attendanceEmployee.isMorningYes());
                model.setAttendanceMorning(attendanceStatusDayResponse);
            }
            if (attendanceEmployeeConfigResponse.isAfternoon()){
                AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                attendanceStatusDayResponse.setAbsentLetter(attendanceEmployee.isAfternoon());
                attendanceStatusDayResponse.setAbsentLetterNo(attendanceEmployee.isAfternoonNo());
                attendanceStatusDayResponse.setAbsentLetterYes(attendanceEmployee.isAfternoonYes());
                model.setAttendanceAfternoon(attendanceStatusDayResponse);
            }
            if (attendanceEmployeeConfigResponse.isEvening()){
                AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                attendanceStatusDayResponse.setAbsentLetter(attendanceEmployee.isEvening());
                attendanceStatusDayResponse.setAbsentLetterNo(attendanceEmployee.isEveningNo());
                attendanceStatusDayResponse.setAbsentLetterYes(attendanceEmployee.isEveningYes());
                model.setAttendanceEvening(attendanceStatusDayResponse);
            }
        }else {
            if (attendanceEmployeeConfigResponse.isMorning()){
                AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                attendanceStatusDayResponse.setAbsentLetter(AppConstant.APP_FALSE);
                attendanceStatusDayResponse.setAbsentLetterNo(AppConstant.APP_FALSE);
                attendanceStatusDayResponse.setAbsentLetterYes(AppConstant.APP_FALSE);
                model.setAttendanceMorning(attendanceStatusDayResponse);
            }
            if (attendanceEmployeeConfigResponse.isAfternoon()){
                AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                attendanceStatusDayResponse.setAbsentLetter(AppConstant.APP_FALSE);
                attendanceStatusDayResponse.setAbsentLetterNo(AppConstant.APP_FALSE);
                attendanceStatusDayResponse.setAbsentLetterYes(AppConstant.APP_FALSE);
                model.setAttendanceAfternoon(attendanceStatusDayResponse);
            }
            if (attendanceEmployeeConfigResponse.isEvening()){
                AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                attendanceStatusDayResponse.setAbsentLetter(AppConstant.APP_FALSE);
                attendanceStatusDayResponse.setAbsentLetterNo(AppConstant.APP_FALSE);
                attendanceStatusDayResponse.setAbsentLetterYes(AppConstant.APP_FALSE);
                model.setAttendanceEvening(attendanceStatusDayResponse);
            }
        }
    }

}
