package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.home.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HomeWebService;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeWebServiceImpl implements HomeWebService {
    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Autowired
    private MaClassRepository maClassRepository;


    @Override
    public StatisticalHomeResponse findStatisticalTotalHome(UserPrincipal principal) {
        CommonValidate.checkPlusOrTeacher(principal);
        StatisticalHomeResponse response = new StatisticalHomeResponse();
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate nowDate = LocalDate.now();

        this.setTotalKids(idSchool, response);
        this.setProperties(principal, idSchool, nowDate, response);
        return response;
    }

    @Override
    public List<StatisticalClassHomeResponse> findStatisticalClassHome(UserPrincipal principal) {
        CommonValidate.checkPlusOrTeacher(principal);
        List<StatisticalClassHomeResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate nowDate = LocalDate.now();
        List<MaClass> maClassList = new ArrayList<>();
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
            maClassList = maClassRepository.findClassCommon(idSchool);
        } else if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
            maClassList = UserPrincipleToUserUtils.getClassListTeacher(principal);
        }
        maClassList.forEach(x -> {
            List<Kids> kidsList = kidsRepository.findByIdSchoolAndMaClassIdAndDelActiveTrue(idSchool, x.getId());
            List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
            List<AttendanceKids> classDataList = attendanceKidsRepository.findByKidsIdInAndAttendanceDate(idKidList, nowDate);
            //số học sinh trong một lớp trong bảng điểm danh
            StatisticalClassHomeResponse model = new StatisticalClassHomeResponse();
            model.setClassName(x.getClassName());
            model.setGradeName(x.getGrade().getGradeName());
            if (CollectionUtils.isNotEmpty(classDataList)) {
                int kidsTotal = classDataList.size();
                //số học sinh đã được điểm danh
                List<AttendanceKids> attendanceYesList = this.getAttendanceYesList(classDataList);
                int attendanceNo = kidsTotal - attendanceYesList.size();
                //số học sinh đã được điểm danh đi học
                List<AttendanceKids> goSchoolList = this.getAttendanceGoSchool(classDataList);
                model.setKidsTotalNumber(kidsTotal);
                model.setAttendanceYes(attendanceYesList.size());
                model.setAttendanceNo(attendanceNo);
                model.setAbsentNumber(this.getKidsAbsentNumber(attendanceYesList));
                model.setGoSchoolNumber(goSchoolList.size());
                model.setLeaveYes(this.getAttendanceLeaveSchoolYes(goSchoolList));
                model.setLeaveNo(this.getAttendanceLeaveSchoolNo(goSchoolList));
            }
            responseList.add(model);
        });
        return responseList;
    }

    /**
     * set tổng số học sinh đang học và bảo lưu
     *
     * @param idSchool
     */
    private void setTotalKids(Long idSchool, StatisticalHomeResponse response) {
        KidsTotalHome model = new KidsTotalHome();
        List<Kids> studyList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(idSchool, KidsStatusConstant.STUDYING);
        List<Kids> reserveList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(idSchool, KidsStatusConstant.RESERVE);
        List<Kids> waitList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(idSchool, KidsStatusConstant.STUDY_WAIT);
        model.setStudy(studyList.size());
        model.setReserve(reserveList.size());
        model.setWait(waitList.size());
        model.setTotal(studyList.size() + reserveList.size() + waitList.size());
        response.setKidsTotal(model);

    }

    /**
     * set đã điểm danh, chưa điểm danh
     *
     * @param idSchool
     * @param nowDate
     * @param response
     */
    private void setProperties(UserPrincipal principal, Long idSchool, LocalDate nowDate, StatisticalHomeResponse response) {
        AttendanceTotalHome attendanceTotalHome = new AttendanceTotalHome();
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsIdInAndAttendanceDate(idKidList, nowDate);
        /**
         * set học sinh đã điểm danh đến, chưa điểm danh đến, đã điểm danh ăn, chưa điểm danh ăn
         */
        //số học sinh được tạo điểm danh cho ngày đó
        //số học sinh đã được điểm danh đến
        List<AttendanceKids> attendaceYesList = this.getAttendanceYesList(attendanceKidsList);
        //số học sinh chưa được điểm danh đến
        int attendaceNoNumber = attendanceKidsList.size() - attendaceYesList.size();
        //số học sinh đã được điểm danh ăn
        int countEatYes = (int) attendanceKidsList.stream().filter(x -> x.getAttendanceEatKids().isBreakfast() || x.getAttendanceEatKids().isSecondBreakfast() || x.getAttendanceEatKids().isLunch() || x.getAttendanceEatKids().isAfternoon() || x.getAttendanceEatKids().isSecondAfternoon() || x.getAttendanceEatKids().isDinner()).count();
        //số học sinh chưa được điểm danh ăn
        int countEatNo = attendanceKidsList.size() - countEatYes;

        attendanceTotalHome.setAttendaceYes(attendaceYesList.size());
        attendanceTotalHome.setAttendaceNo(attendaceNoNumber);
        attendanceTotalHome.setAttendanceEatYes(countEatYes);
        attendanceTotalHome.setAttendanceEatNo(countEatNo);

        //truyền vào số học sinh đã được điểm danh đến
        LeaveTotalHome leaveTotalHome = this.getLeaveResponse(attendaceYesList);
        OtherTotalHome otherTotalHome = this.getOtherResponse(principal, idSchool, nowDate, attendanceKidsList);

        response.setAttendanceTotal(attendanceTotalHome);
        response.setLeaveTotal(leaveTotalHome);
        response.setOtherTotal(otherTotalHome);
    }

    /**
     * @param attendaceYesList số học sinh đã được điểm danh đến: đi học, nghỉ có phép, nghỉ không phép các buổi sáng, chiều, tối
     * @return
     */
    private LeaveTotalHome getLeaveResponse(List<AttendanceKids> attendaceYesList) {
        LeaveTotalHome leaveTotalHome = new LeaveTotalHome();
        //số học sinh đi học
        List<AttendanceKids> goSchoolList = this.getAttendanceGoSchool(attendaceYesList);
        //số học sinh đã về
        int leaveSchoolYesNumber = this.getAttendanceLeaveSchoolYes(goSchoolList);
        //số học sinh chưa về
        int leaveSchoolNoNumber = this.getAttendanceLeaveSchoolNo(goSchoolList);
        //số học sinh đón muộn
        int pickupLateNumber = this.getAttendancePickupLate(goSchoolList);

        leaveTotalHome.setGoSchool(goSchoolList.size());
        leaveTotalHome.setLeaveYes(leaveSchoolYesNumber);
        leaveTotalHome.setLeaveNo(leaveSchoolNoNumber);
        leaveTotalHome.setPickupLater(pickupLateNumber);
        return leaveTotalHome;
    }

    /**
     * @param idSchool
     * @param nowDate
     * @param attendanceKidsList số học sinh được tạo điểm danh trong toàn trường
     * @return
     */
    private OtherTotalHome getOtherResponse(UserPrincipal principal, Long idSchool, LocalDate nowDate, List<AttendanceKids> attendanceKidsList) {
        OtherTotalHome otherTotalHome = new OtherTotalHome();
        List<Long> idClassList = UserPrincipleToUserUtils.getIdClassListTeacher(principal);
        List<MessageParent> messageParentList = messageParentRepository.findMessageParentWithDate(idSchool, AppTypeConstant.TEACHER.equals(principal.getAppType()) ? idClassList : null, nowDate);
        List<Medicine> medicineList = medicineRepository.findMedicineDate(idSchool, AppTypeConstant.TEACHER.equals(principal.getAppType()) ? idClassList : null, nowDate);
        int countAbsentLetter = AppTypeConstant.SCHOOL.equals(principal.getAppType()) ? absentLetterRepository.countByIdSchoolAndConfirmStatusFalseAndParentAbsentDelFalseAndDelActiveTrue(idSchool) : absentLetterRepository.countByIdSchoolAndIdClassInAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrue(idSchool, idClassList);
        int countAbsent = this.getKidsAbsentNumber(attendanceKidsList);
        otherTotalHome.setMessage(messageParentList.size());
        otherTotalHome.setMedicine(medicineList.size());
        otherTotalHome.setAbsent(countAbsent);
        otherTotalHome.setAbsentLetterNoConfirm(countAbsentLetter);
        return otherTotalHome;
    }

    /**
     * lấy số học sinh được điểm danh vào checkbox đi học trong số các học sinh tạo tự động
     *
     * @param attendanceKidsList
     * @return
     */
    private List<AttendanceKids> getAttendanceGoSchool(List<AttendanceKids> attendanceKidsList) {
        return attendanceKidsList.stream().filter(x -> x.getAttendanceArriveKids().isMorning() || x.getAttendanceArriveKids().isAfternoon() || x.getAttendanceArriveKids().isEvening()).collect(Collectors.toList());
    }

    /**
     * lấy số học sinh nghỉ học trong số các học sinh tạo tự động
     *
     * @param attendanceKidsList
     * @return
     */
    private int getKidsAbsentNumber(List<AttendanceKids> attendanceKidsList) {
        return (int) attendanceKidsList.stream().filter(x -> x.getAttendanceArriveKids().isMorningYes() || x.getAttendanceArriveKids().isMorningNo() || x.getAttendanceArriveKids().isAfternoonYes() || x.getAttendanceArriveKids().isAfternoonNo() || x.getAttendanceArriveKids().isEveningYes() || x.getAttendanceArriveKids().isEveningNo()).count();
    }

    /**
     * lấy số học sinh đã được điểm danh(check 3 ô đi học, có phép, không phép) trong số các học sinh tạo tự động
     *
     * @param attendanceKidsList
     * @return
     */
    private List<AttendanceKids> getAttendanceYesList(List<AttendanceKids> attendanceKidsList) {
        return attendanceKidsList.stream().filter(b -> b.isAttendanceArrive()).collect(Collectors.toList());
    }

    /**
     * lấy số học sinh đã được điểm danh về trong số học sinh đã được điểm danh đến
     *
     * @param attendanceKidsList
     * @return
     */
    private int getAttendanceLeaveSchoolYes(List<AttendanceKids> attendanceKidsList) {
        return (int) attendanceKidsList.stream().filter(x -> x.isAttendanceLeave()).count();
    }

    /**
     * lấy số học sinh chưa được điểm danh về trong số học sinh đã được điểm danh đến
     *
     * @param attendanceKidsList
     * @return
     */
    private int getAttendanceLeaveSchoolNo(List<AttendanceKids> attendanceKidsList) {
        return (int) attendanceKidsList.stream().filter(x -> !x.isAttendanceLeave()).count();
    }

    /**
     * lấy số học sinh đã được điểm danh về trong số học sinh đã được điểm danh đến
     *
     * @param attendanceKidsList
     * @return
     */
    private int getAttendancePickupLate(List<AttendanceKids> attendanceKidsList) {
        return (int) attendanceKidsList.stream().filter(x -> x.getAttendanceLeaveKids().getMinutePickupLate() > 0).count();
    }
}
