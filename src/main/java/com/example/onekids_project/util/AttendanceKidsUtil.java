package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.model.attendance.AttendanceNumber;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceKidsUtil {
    /**
     * check đã được điểm danh hay chưa
     * true đã được điểm danh đến, về hoặc ăn
     * false là chưa được điểm danh cái nào
     *
     * @param attendanceKids
     * @return
     */
    public static boolean checkHasAttendanceKids(AttendanceKids attendanceKids) {
        if (attendanceKids.isAttendanceArrive() || attendanceKids.isAttendanceLeave() || attendanceKids.isAttendanceEat()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check trường có học ngày đó hay không
     * true là có học ít nhất 1 buổi
     * false là không học buổi nào
     *
     * @param attendanceConfigResponse
     * @return
     */
    public static boolean checkStudyInSchool(AttendanceConfigResponse attendanceConfigResponse) {
        if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isEveningAttendanceArrive()) {
            return true;
        }
        return false;
    }


    /**
     * Check đã điểm danh buổi nao hay chưa
     *
     * @param arriveKids
     * @return
     */
    public static boolean checkArriveHas(AttendanceArriveKids arriveKids) {
        if (arriveKids.isMorning() || arriveKids.isMorningYes() || arriveKids.isMorningNo() ||
                arriveKids.isAfternoon() || arriveKids.isAfternoonYes() || arriveKids.isAfternoonNo() ||
                arriveKids.isEvening() || arriveKids.isEveningYes() || arriveKids.isEveningNo()
        ) {
            return true;
        }
        return false;
    }

    /**
     * set auditting cho điểm danh và điểm danh đến, về hoặc ăn
     *
     * @param attendanceType
     * @param principal
     * @param attendanceKids
     */
    public static boolean setAuditingAttendance(String attendanceType, UserPrincipal principal, AttendanceKids attendanceKids) {
        boolean sendFirebase = false;
        if (attendanceKids == null) {
            return sendFirebase;
        }
        /**
         * set cho ma_attendance_kids
         */
        LocalDateTime nowDate = LocalDateTime.now();
        if (attendanceKids.getIdCreated() == null) {
            attendanceKids.setIdCreated(principal.getId());
            attendanceKids.setCreatedBy(principal.getFullName());
            attendanceKids.setCreatedDate(nowDate);
            attendanceKids.setIdModified(principal.getId());
            attendanceKids.setLastModifieBy(principal.getFullName());
            attendanceKids.setLastModifieDate(nowDate);
        } else {
            attendanceKids.setIdModified(principal.getId());
            attendanceKids.setLastModifieBy(principal.getFullName());
            attendanceKids.setLastModifieDate(nowDate);
        }
        switch (attendanceType) {
            case AttendanceConstant.ATTENDANCE_ARRIVE: {
                /**
                 * set cho điểm danh đến
                 */
                if (attendanceKids.getAttendanceArriveKids().getIdCreated() == null) {
                    attendanceKids.getAttendanceArriveKids().setIdCreated(principal.getId());
                    attendanceKids.getAttendanceArriveKids().setCreatedBy(principal.getFullName());
                    attendanceKids.getAttendanceArriveKids().setCreatedDate(nowDate);
                    attendanceKids.getAttendanceArriveKids().setIdModified(principal.getId());
                    attendanceKids.getAttendanceArriveKids().setLastModifieBy(principal.getFullName());
                    attendanceKids.getAttendanceArriveKids().setLastModifieDate(nowDate);
                    sendFirebase = true;
                } else {
                    attendanceKids.getAttendanceArriveKids().setIdModified(principal.getId());
                    attendanceKids.getAttendanceArriveKids().setLastModifieBy(principal.getFullName());
                    attendanceKids.getAttendanceArriveKids().setLastModifieDate(nowDate);
                }
                break;
            }
            case AttendanceConstant.ATTENDANCE_LEAVE: {
                if (attendanceKids.getAttendanceLeaveKids().getIdCreated() == null) {
                    attendanceKids.getAttendanceLeaveKids().setIdCreated(principal.getId());
                    attendanceKids.getAttendanceLeaveKids().setCreatedBy(principal.getFullName());
                    attendanceKids.getAttendanceLeaveKids().setCreatedDate(nowDate);
                    attendanceKids.getAttendanceLeaveKids().setIdModified(principal.getId());
                    attendanceKids.getAttendanceLeaveKids().setLastModifieBy(principal.getFullName());
                    attendanceKids.getAttendanceLeaveKids().setLastModifieDate(nowDate);
                    sendFirebase = true;
                } else {
                    attendanceKids.getAttendanceLeaveKids().setIdModified(principal.getId());
                    attendanceKids.getAttendanceLeaveKids().setLastModifieBy(principal.getFullName());
                    attendanceKids.getAttendanceLeaveKids().setLastModifieDate(nowDate);
                }
                break;
            }
            case AttendanceConstant.ATTENDANCE_EAT: {
                if (attendanceKids.getAttendanceEatKids().getIdCreated() == null) {
                    attendanceKids.getAttendanceEatKids().setIdCreated(principal.getId());
                    attendanceKids.getAttendanceEatKids().setCreatedBy(principal.getFullName());
                    attendanceKids.getAttendanceEatKids().setCreatedDate(nowDate);
                    attendanceKids.getAttendanceEatKids().setIdModified(principal.getId());
                    attendanceKids.getAttendanceEatKids().setLastModifieBy(principal.getFullName());
                    attendanceKids.getAttendanceEatKids().setLastModifieDate(nowDate);
                } else {
                    attendanceKids.getAttendanceEatKids().setIdModified(principal.getId());
                    attendanceKids.getAttendanceEatKids().setLastModifieBy(principal.getFullName());
                    attendanceKids.getAttendanceEatKids().setLastModifieDate(nowDate);
                }
                break;
            }
        }
        return sendFirebase;
    }


    public static String checkStatusKidArriveDay(AttendanceConfigResponse attendanceConfigResponse, AttendanceKids attendanceKidNew) {
        List<AttendanceKids> appsendArrive = new ArrayList<>();
        List<AttendanceKids> appsendArriveYes = new ArrayList<>();
        List<AttendanceKids> appsendArriveNo = new ArrayList<>();
        AttendanceArriveKids attendanceArriveKids = attendanceKidNew.getAttendanceArriveKids();
        if (attendanceConfigResponse.isMorningAttendanceArrive()) {
            if (attendanceArriveKids.isMorning()) {
                appsendArrive.add(attendanceKidNew);
            }
            if (attendanceArriveKids.isMorningYes()) {
                appsendArriveYes.add(attendanceKidNew);
            }
            if (attendanceArriveKids.isMorningNo()) {
                appsendArriveNo.add(attendanceKidNew);
            }
        } else if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
            if (attendanceArriveKids.isAfternoon()) {
                appsendArrive.add(attendanceKidNew);
            }
            if (attendanceArriveKids.isAfternoonYes()) {
                appsendArriveYes.add(attendanceKidNew);
            }
            if (attendanceArriveKids.isAfternoonNo()) {
                appsendArriveNo.add(attendanceKidNew);
            }
        } else if (attendanceConfigResponse.isEveningAttendanceArrive()) {
            if (attendanceArriveKids.isEvening()) {
                appsendArrive.add(attendanceKidNew);
            }
            if (attendanceArriveKids.isEveningNo()) {
                appsendArriveYes.add(attendanceKidNew);
            }
            if (attendanceArriveKids.isEveningYes()) {
                appsendArriveNo.add(attendanceKidNew);
            }
        }
        if (!CollectionUtils.isEmpty(appsendArriveNo)) {
            return AttendanceConstant.TYPE_ABSENT_NO;
        } else if (!CollectionUtils.isEmpty(appsendArriveYes)) {
            return AttendanceConstant.TYPE_ABSENT_YES;
        }
        return AttendanceConstant.TYPE_GO_SCHOOL;
    }

    /**
     * lấy idWebSystemTitle cho điểm danh: check điểm danh ngày hiện tại và đúng theo giờ config thì gửi điểm danh
     * bằng 0: ko gửi điểm danh
     * khác 0: gửi điểm danh
     *
     * @param principal
     * @param attendanceKids
     * @param category       AttendanceConstant.ATTENDANCE_ARRIVE, AttendanceConstant.ATTENDANCE_LEAVE
     * @param appType        teacher->cho app teacher, plus->cho app plus va web
     * @return
     */
    public static long sendFirebaseConditions(UserPrincipal principal, AttendanceKids attendanceKids, String category, String appType) {
        long number = 0;
        LocalDate nowDate = LocalDate.now();
        LocalDate attendanceDate = attendanceKids.getAttendanceDate();
        SchoolConfigResponse schoolConfig = principal.getSchoolConfig();
        LocalTime nowTime = LocalTime.now();
        if (nowDate.isEqual(attendanceDate)) {
            if (category.equals(AttendanceConstant.ATTENDANCE_ARRIVE)) {
                if (nowTime.isBefore(schoolConfig.getTimeAttendanceArrive())) {
                    AttendanceArriveKids arrive = attendanceKids.getAttendanceArriveKids();
                    if (arrive.isMorning() || arrive.isAfternoon() || arrive.isEvening()) {
                        if (appType.equals(AppTypeConstant.SCHOOL)) {
                            return 5L;
                        } else if (appType.equals(AppTypeConstant.TEACHER)) {
                            return 11L;
                        }
                    } else if (arrive.isMorningYes() || arrive.isAfternoonYes() || arrive.isEveningYes()) {
                        if (appType.equals(AppTypeConstant.SCHOOL)) {
                            return 6L;
                        } else if (appType.equals(AppTypeConstant.TEACHER)) {
                            return 12L;
                        }
                    } else if (arrive.isMorningNo() || arrive.isAfternoonNo() || arrive.isEveningNo()) {
                        if (appType.equals(AppTypeConstant.SCHOOL)) {
                            return 7L;
                        } else if (appType.equals(AppTypeConstant.TEACHER)) {
                            return 13L;
                        }
                    }
                }
            } else if (category.equals(AttendanceConstant.ATTENDANCE_LEAVE)) {
                if (nowTime.isBefore(schoolConfig.getTimeAttendanceLeave())) {
                    AttendanceLeaveKids leave = attendanceKids.getAttendanceLeaveKids();
                    if (leave.isStatusLeave()) {
                        if (appType.equals(AppTypeConstant.SCHOOL)) {
                            return 8L;
                        } else if (appType.equals(AppTypeConstant.TEACHER)) {
                            return 14L;
                        }
                    }
                }
            }
        }
        return number;
    }

    /**
     * lấy trạng thái điểm danh của một ngày
     *
     * @param attendanceKidsList
     * @return
     */
    public static AttendanceNumber getAttendanceKidsNumber(List<AttendanceKids> attendanceKidsList) {
        AttendanceNumber attendanceNumber = new AttendanceNumber();
        int goSchool = 0;
        int absentYes = 0;
        int absentNo = 0;
        int noAttendance = 0;
        List<AttendanceArriveKids> arriveKidsList = attendanceKidsList.stream().map(AttendanceKids::getAttendanceArriveKids).collect(Collectors.toList());
        for (AttendanceArriveKids arriveKids : arriveKidsList) {
            if (arriveKids.isMorning() || arriveKids.isAfternoon() || arriveKids.isEvening()) {
                goSchool++;
            } else if (arriveKids.isMorningYes() || arriveKids.isAfternoonYes() || arriveKids.isEveningYes()) {
                absentYes++;
            } else if (arriveKids.isMorningNo() || arriveKids.isAfternoonNo() || arriveKids.isEveningNo()) {
                absentNo++;
            } else {
                noAttendance++;
            }
        }
        attendanceNumber.setGoSchool(goSchool);
        attendanceNumber.setAbsentYes(absentYes);
        attendanceNumber.setAbsentNo(absentNo);
        attendanceNumber.setNoAttendance(noAttendance);
        return attendanceNumber;
    }

    /**
     * số buổi điểm danh trong tháng
     *
     * @param kids
     * @param maClass
     * @param month
     * @param year
     * @return
     */
    public static AttendanceNumber getAttendanceKidsMonth(Kids kids, MaClass maClass, int month, int year) {
        List<LocalDate> absentDateList = maClass.getDayOffClassList().stream().filter(x -> x.isDelActive() && x.getDate().getMonthValue() == month && x.getDate().getYear() == year).map(DayOffClass::getDate).collect(Collectors.toList());
        List<AttendanceKids> attendanceKidsList = kids.getAttendanceKidsList().stream().filter(x -> x.getAttendanceDate().getMonthValue() == month && x.getAttendanceDate().getYear() == year).collect(Collectors.toList());
        attendanceKidsList = attendanceKidsList.stream().filter(x -> absentDateList.stream().noneMatch(y -> y.isEqual(x.getAttendanceDate()))).collect(Collectors.toList());
        return getAttendanceKidsNumber(attendanceKidsList);
    }

    public static boolean checkArrive(AttendanceArriveKids arriveKids) {
        return arriveKids.isMorning() || arriveKids.isAfternoon() || arriveKids.isEvening();
    }

    public static boolean checkArriveYes(AttendanceArriveKids arriveKids) {
        return arriveKids.isMorningYes() || arriveKids.isAfternoonYes() || arriveKids.isEveningYes();
    }

    public static boolean checkArriveNo(AttendanceArriveKids arriveKids) {
        return arriveKids.isMorningNo() || arriveKids.isAfternoonNo() || arriveKids.isEveningNo();
    }

    public static boolean checkEat(AttendanceEatKids eatKids) {
        return eatKids.isBreakfast() || eatKids.isSecondBreakfast() || eatKids.isLunch() || eatKids.isAfternoon() || eatKids.isSecondAfternoon() || eatKids.isDinner();
    }
}
