package com.example.onekids_project.util;

import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.NotificationConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.entity.school.InternalNotificationPlus;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * date 2021-08-10 11:44 AM
 *
 * @author nguyễn văn thụ
 */
@Component
@Slf4j
public class NotificationUtils {

    private static AttendanceKidsRepository attendanceKidsRepository;
    private static DayOffClassRepository dayOffClassRepository;
    private static MessageParentRepository messageParentRepository;
    private static MedicineRepository medicineRepository;
    private static AbsentLetterRepository absentLetterRepository;
    private static ExEmployeeClassRepository exEmployeeClassRepository;
    private static AppSendService appSendService;
    private static FirebaseFunctionService firebaseFunctionService;
    private static ExAlbumKidsRepository exAlbumKidsRepository;
    private static AlbumRepository albumRepository;
    private static InternalNotificationPlusRepository internalNotificationPlusRepository;

    @Autowired
    public NotificationUtils(InternalNotificationPlusRepository internalNotificationPlusRepository, AlbumRepository albumRepository,ExAlbumKidsRepository exAlbumKidsRepository,FirebaseFunctionService firebaseFunctionService,AppSendService appSendService,ExEmployeeClassRepository exEmployeeClassRepository,AbsentLetterRepository absentLetterRepository,MedicineRepository medicineRepository,AttendanceKidsRepository attendanceKidsRepository,DayOffClassRepository dayOffClassRepository,MessageParentRepository messageParentRepository){
        NotificationUtils.attendanceKidsRepository = attendanceKidsRepository;
        NotificationUtils.dayOffClassRepository = dayOffClassRepository;
        NotificationUtils.messageParentRepository = messageParentRepository;
        NotificationUtils.medicineRepository = medicineRepository;
        NotificationUtils.absentLetterRepository = absentLetterRepository;
        NotificationUtils.exEmployeeClassRepository = exEmployeeClassRepository;
        NotificationUtils.appSendService = appSendService;
        NotificationUtils.firebaseFunctionService = firebaseFunctionService;
        NotificationUtils.exAlbumKidsRepository = exAlbumKidsRepository;
        NotificationUtils.albumRepository = albumRepository;
        NotificationUtils.internalNotificationPlusRepository = internalNotificationPlusRepository;
    }

    public static List<Kids> getKidsList(Long idSchool, String type) {
        LocalDate date = LocalDate.now();
        List<Kids> kidsList = new ArrayList<>();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByIdSchoolAndAttendanceDateAndKidsKidStatusAndKidsDelActiveTrue(idSchool, date, KidsStatusConstant.STUDYING);
        for (AttendanceKids x : attendanceKidsList) {//check ngay ngay nghi
            boolean checkAbsent = dayOffClassRepository.existsByDateAndMaClassIdAndDelActiveTrue(date, x.getMaClass().getId());
            if (!checkAbsent) {
                switch (type) {
                    // DS học sinh chưa điểm danh đến
                    case NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE:
                        AttendanceArriveKids arriveKids = x.getAttendanceArriveKids();
                        //check diem danh hay chua
                        if (!arriveKids.isMorningYes() && !arriveKids.isMorningNo() && !arriveKids.isMorning() && !arriveKids.isAfternoonYes() && !arriveKids.isAfternoonNo() && !arriveKids.isAfternoon() && !arriveKids.isEveningYes() && !arriveKids.isEveningNo() && !arriveKids.isEvening()) {
                            kidsList.add(x.getKids());
                        }
                        break;
                    //DS học sinh chưa điểm danh về
                    case NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE:
                        AttendanceLeaveKids leaveKids = x.getAttendanceLeaveKids();
                        AttendanceArriveKids arriveKidsLeave = x.getAttendanceArriveKids();
                        //check diem danh hay chua
                        if (!leaveKids.isStatusLeave() && (arriveKidsLeave.isMorning() || arriveKidsLeave.isAfternoon() || arriveKidsLeave.isEvening())) {
                            kidsList.add(x.getKids());
                        }
                        break;
                    //DS học sinh chưa điểm danh ăn
                    case NotificationConstant.NOTIFICATION_ATTENDANCE_EAT:
                        AttendanceEatKids eatKids = x.getAttendanceEatKids();
                        //check diem danh hay chua
                        if (!eatKids.isBreakfast() && !eatKids.isSecondBreakfast() && !eatKids.isLunch() && !eatKids.isAfternoon() && !eatKids.isSecondAfternoon() && !eatKids.isDinner()) {
                            kidsList.add(x.getKids());
                        }
                        break;
                    //lời nhắn chưa xác nhận trong trường
                    case NotificationConstant.NOTIFICATION_MESSAGE:
                        List<MessageParent> messageParentList = messageParentRepository.findAllByIdSchoolAndConfirmStatusFalseAndDelActiveTrue(idSchool);
                        kidsList = messageParentList.stream().map(MessageParent::getKids).collect(Collectors.toList());
                        break;
                    //dặn thuốc chưa xác nhận trong trường
                    case NotificationConstant.NOTIFICATION_MEDICINE:
                        List<Medicine> medicineList = medicineRepository.findAllByIdSchoolAndConfirmStatusFalseAndDelActiveTrue(idSchool);
                        kidsList = medicineList.stream().map(Medicine::getKids).collect(Collectors.toList());
                        break;
                    //xin nghỉ chưa xác nhận trong trường
                    case NotificationConstant.NOTIFICATION_ABSENT:
                        List<AbsentLetter> absentLetterList = absentLetterRepository.findAllByIdSchoolAndConfirmStatusFalseAndDelActiveTrue(idSchool);
                        kidsList = absentLetterList.stream().map(AbsentLetter::getKids).collect(Collectors.toList());
                        break;
                    //Sinh nhật của học sinh hôm nay
                    case NotificationConstant.NOTIFICATION_BIRTHDAY:
                        //Home
                    case NotificationConstant.NOTIFICATION_HOME:
                        //Hóa đơn chưa hoàn thành của học sinh
                    case NotificationConstant.NOTIFICATION_FEES:
                        kidsList.add(x.getKids());
                        break;
                    default:
                        log.warn("Chưa có loại thông báo hợp lệ");
                }
            }
        }
        kidsList = kidsList.stream().distinct().collect(Collectors.toList());
        return kidsList;
    }

    public static List<InfoEmployeeSchool> getInfoEmployeeList(List<Kids> kidsList) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = new ArrayList<>();
        Set<Long> idClassList = kidsList.stream().map(x->x.getMaClass().getId()).collect(Collectors.toSet());
        //ham gui firebase
        for (Long idClass : idClassList) {//guiw cho phu huynh
            List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByMaClassIdAndDelActiveTrue(idClass);
            List<InfoEmployeeSchool> employeeSchoolList = exEmployeeClassList.stream().map(ExEmployeeClass::getInfoEmployeeSchool).filter(a -> a.isDelActive() && a.getEmployeeStatus().equals(EmployeeConstant.STATUS_WORKING)).collect(Collectors.toList());
            infoEmployeeSchoolList.addAll(employeeSchoolList);
        }
        return infoEmployeeSchoolList.stream().filter(x->x.getEmployee() != null).filter(FilterDataUtils.distinctBy(BaseEntity::getId)).collect(Collectors.toList());
    }

    //Album mới
    public static int getAlbumNewNumber(Long idKid) {
        LocalDateTime date = LocalDateTime.now();
        int newNumber = 0;
        List<ExAlbumKids> exAlbumKidsList = exAlbumKidsRepository.findByKidsIdAndStatusUnreadFalse(idKid);
        if (CollectionUtils.isEmpty(exAlbumKidsList)) {
            return newNumber;
        }
        for (ExAlbumKids x : exAlbumKidsList) {
            if (!x.isStatusUnread()) {
                Album album = albumRepository.findById(x.getAlbum().getId()).orElseThrow(() -> new NotFoundException("not found album by id in home"));
                long countPicutreApproved = album.getAlistPictureList().stream().filter(a -> a.getIsApproved() &&  date.getDayOfMonth() == a.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == a.getCreatedDate().getMonth().getValue() && date.getYear() == a.getCreatedDate().getYear()).count();
                if (countPicutreApproved > 0) {
                    newNumber++;
                }
            }
        }
        return newNumber;
    }

    public static void sendFirebaseParent(Kids kids, String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, content, idSchool, category);
        appSendService.saveToAppSendParentForAuto(idSchool, kids, title, content, FirebaseConstant.CATEGORY_NOTIFY);
    }

    public static void sendFirebaseTeacher(InfoEmployeeSchool infoEmployeeSchool, String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        firebaseFunctionService.sendTeacherCommon(Collections.singletonList(infoEmployeeSchool), title, content, idSchool, category);
        appSendService.saveToAppSendEmployeeForAuto(idSchool, infoEmployeeSchool, title, content, FirebaseConstant.CATEGORY_NOTIFY);
    }

    public static void sendFirebasePlus(InfoEmployeeSchool infoEmployeeSchool, String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        firebaseFunctionService.sendPlusCommon(Collections.singletonList(infoEmployeeSchool), title, content, idSchool, category);
        InternalNotificationPlus model = new InternalNotificationPlus();
        model.setIdCreated(idSchool);
        model.setTitle(title);
        model.setContent(content);
        model.setInfoEmployeeSchool(infoEmployeeSchool);
        internalNotificationPlusRepository.save(model);
    }

}
