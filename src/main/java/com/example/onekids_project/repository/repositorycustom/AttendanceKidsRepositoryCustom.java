package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.request.attendancekids.AttendanceKidsSearchRequest;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceKidsRepositoryCustom {
    Optional<AttendanceKids> findByIdAttendance(Long idSchool, Long id);

    /**
     * tìm kiếm điểm danh cho các học sinh theo ngày
     *
     * @param idSchool
     * @param attendanceKidsSearchRequest
     * @return
     */
    List<AttendanceKids> searchAllAttendanceKidsDate(Long idSchool, String type, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * tìm kiếm điểm danh cho học sinh trong một tháng
     *
     * @param idSchool
     * @param idKid
     * @param dateStart
     * @param dateEnd
     * @return
     */
    List<AttendanceKids> findAttendanceKidsDetailOfMonth(Long idSchool, Long idKid, LocalDate dateStart, LocalDate dateEnd);


    /**
     * tìm kiếm điểm danh tất cả học sinh 1 lớp trong một tháng
     *
     * @param idSchool
     * @param idClass
     * @return
     */
    List<AttendanceKids> findAttendanceKidsClassOfMonth(Long idSchool, Long idClass, LocalDate dateStart, LocalDate dateEnd);

    /**
     * @param idSchool
     * @param idKid
     * @param localDate
     * @param pageNumber
     * @return
     */
    List<AttendanceKids> findAttendanceKidsForMobile(Long idSchool, Long idKid, LocalDate localDate, Integer pageNumber);

    /**
     * @param idSchool
     * @param idKid
     * @param localDate
     * @return
     */
    long getCountAttendance(Long idSchool, Long idKid, LocalDate localDate, Integer pageNumber);

    /**
     * tìm kiếm điểm danh tháng
     *
     * @param idSchool
     * @param idKid
     * @param dateStart
     * @param dateEnd
     * @return
     */
    List<AttendanceKids> findAttendanceMonth(Long idSchool, Long idKid, LocalDate dateStart, LocalDate dateEnd);

    /**
     * tìm kiếm điểm ăn danh tháng
     *
     * @param idSchool
     * @param idKid
     * @param dateStart
     * @param dateEnd
     * @return
     */
    List<AttendanceKids> findAttendanceEatMonth(Long idSchool, Long idKid, LocalDate dateStart, LocalDate dateEnd);


    List<AttendanceKids> findAllMonthAttendanceKidsForMobile(Long idSchool, Long idKid, LocalDate localDate, LocalDate dateStart, LocalDate dateEnd);

    /**
     * find các ngày chưa duyệt của đơn xin nghỉ
     *
     * @param idKid
     * @param startDate
     * @param endDate
     * @return
     */
//    List<AttendanceKids> findAttendanceKidStartEndDate(Long idKid, LocalDate startDate, LocalDate endDate);


    /**
     * find attendaceArrive for appTeacher
     *
     * @param principal
     * @param localDate
     * @return
     */
    List<AttendanceKids> findAttendanceKidDay(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKids> findAttendanceKidsAfterDate(Long idKid, LocalDate date);
    List<AttendanceKids> findAttendanceKidsDateList(List<Long> idKidList, LocalDate date);

    List<AttendanceKids> findAttendanceKidsMonthYear(Long idKid, int month, int year);

}
