package com.example.onekids_project.service.servicecustom.attendancekids;

import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.importexport.model.AttendanceKidsModel;
import com.example.onekids_project.request.attendancekids.*;
import com.example.onekids_project.response.attendancekids.*;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceKidsService {

    List<Long> checkAbsentHas(UserPrincipal principal, AbsentCheckSearchRequest request);

    /**
     * tìm kiếm điểm danh các học sinh trong một ngày
     *
     * @param attendanceKidsSearchRequest
     * @return
     */
    ListAttendanceKidsDetailDateResponse searchAllAttendanceKidsDetailDate(UserPrincipal principal, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * tìm kiếm điểm danh đến cho các học sinh trong một ngày
     *
     * @param idSchool
     * @param attendanceKidsSearchRequest
     * @return
     */
    ListAttendanceArriveKidsDateResponse searchAttendanceArriveKidsDate(UserPrincipal principal, Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * cập nhật điểm danh đến cho một học sinh trong một ngày
     *
     * @param idSchool
     * @param attendanceArriveKidsDateRequest
     * @return
     */
    AttendanceArriveKidsDateResponse saveAttendanceArriveOneKidsDate(Long idSchool, UserPrincipal principal, AttendanceArriveKidsDateRequest attendanceArriveKidsDateRequest) throws FirebaseMessagingException;

    /**
     * update arrive content
     *
     * @param id
     * @param attendanceArriveUpdateContentRequest
     * @return
     */
    boolean updateContentArrive(Long idSchool, UserPrincipal principal, Long id, AttendanceArriveUpdateContentRequest attendanceArriveUpdateContentRequest) throws IOException;


    /**
     * cập nhật điểm danh đến cho nhiều học sinh trong ngày
     *
     * @param idSchool
     * @param principal
     * @param attendanceArriveKidsDateRequestList
     * @return
     */
    boolean saveAttendanceArriveManyKidsDate(Long idSchool, UserPrincipal principal, List<AttendanceArriveKidsDateRequest> attendanceArriveKidsDateRequestList) throws FirebaseMessagingException;

    /**
     * tìm kiếm thông tin điểm danh đến cho một học sinh trong một tháng
     *
     * @param idSchool
     * @param idKid
     * @param month
     * @param year
     * @return
     */
    List<AttendanceArriveKidsDateResponse> findAttendanceArriveKidsDetailOfMonth(Long idSchool, Long idKid, Integer month, Integer year);

    /**
     * tìm kiếm điểm danh về cho các học sinh trong một ngày
     *
     * @param idSchool
     * @param attendanceKidsSearchRequest
     * @return
     */
    List<AttendanceLeaveKidsDateResponse> searchAttendanceLeaveKidsDate(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * cập nhật điểm danh về cho một học sinh trong một ngày
     *
     * @param idSchool
     * @param principal
     * @param attendanceLeaveKidsDateRequest
     * @return
     */
    AttendanceLeaveKidsDateResponse saveAttendanceLeaveOneKidsDate(Long idSchool, UserPrincipal principal, AttendanceLeaveKidsDateRequest attendanceLeaveKidsDateRequest) throws FirebaseMessagingException;

    /**
     * update leave content
     *
     * @param id
     * @param attendanceLeaveUpdateContentRequest
     * @return
     */
    boolean updateContentLeave(Long idSchool, UserPrincipal principal, Long id, AttendanceLeaveUpdateContentRequest attendanceLeaveUpdateContentRequest) throws IOException;

    /**
     * cập nhật điểm danh về cho nhiều học sinh trong một ngày
     *
     * @param idSchool
     * @param principal
     * @param attendanceLeaveKidsDateRequestList
     * @return
     */
    boolean saveAttendanceLeaveManyKidsDate(Long idSchool, UserPrincipal principal, List<AttendanceLeaveKidsDateRequest> attendanceLeaveKidsDateRequestList) throws FirebaseMessagingException;

    /**
     * tìm kiếm thông tin điểm danh về cho một học sinh trong một tháng
     *
     * @param idSchool
     * @param idKid
     * @param month
     * @param year
     * @return
     */
    List<AttendanceLeaveKidsDateResponse> findAttendanceLeaveKidsDetailOfMonth(Long idSchool, Long idKid, Integer month, Integer year);

    /**
     * tìm kiếm điểm danh ăn cho nhiều học sinh trong một ngày
     *
     * @param idSchool
     * @param attendanceKidsSearchRequest
     * @return
     */
    ListAttendanceEatKidsDateResponse searchAttendanceEatKidsDate(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * cập nhật điểm danh ăn cho một học sinh trong một ngày
     *
     * @param idSchool
     * @param principal
     * @param attendanceEatKidsDateRequest
     * @return
     */
    AttendanceEatKidsDateResponse saveAttendanceEatOneKidsDate(Long idSchool, UserPrincipal principal, AttendanceEatKidsDateRequest attendanceEatKidsDateRequest);

    /**
     * cập nhật điểm danh ăn cho nhiều học sinh trong một ngày
     *
     * @param idSchool
     * @param principal
     * @param attendanceEatKidsDateRequestList
     * @return
     */
    boolean saveAttendanceEatManyKidsDate(Long idSchool, UserPrincipal principal, List<AttendanceEatKidsDateRequest> attendanceEatKidsDateRequestList);

    /**
     * tìm kiếm thông tin điểm danh ăn cho một học sinh trong một tháng
     *
     * @param idSchool
     * @param idKid
     * @param month
     * @param year
     * @return
     */
    List<AttendanceEatKidsDateResponse> findAttendanceEatKidsDetailOfMonth(Long idSchool, Long idKid, Integer month, Integer year);

    /**
     * chuyển đổi học sinh sang view excel
     *
     * @param attendanceKidsDetailDateRespons
     */
    List<AttendanceKidsModel> getFileAttendanceKids(ListAttendanceKidsDetailDateResponse attendanceKidsDetailDateRespons);

    List<ExcelNewResponse> getFileAttendanceKidsNew(ListAttendanceKidsDetailDateResponse attendanceKidsDetailDateRespons, Long idSchool, Long idClass, LocalDate date);

    /**
     * danh sách id từng học sinh trong lớp theo tháng
     *
     * @param attendanceKidsSearchRequest
     */

    List<ListIdKidDTO> listIdAttendanceKidsDetailOfMonth(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * tìm kiếm thông tin điểm danh đến cho một học sinh trong một tháng
     *
     * @param idSchool
     * @param attendanceKidsSearchRequest
     * @return
     */
    List<AttendanceKidsDetailDateResponse> findAttendanceKidsClassOfMonth(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * tách list cho học sinh trong lớp trong một tháng
     *
     * @param listAttendanceKidsDetailDateResponse
     * @return
     */
    Map<Long, List<AttendanceKidsModel>> detachedListAttendanceKidsClassOfMonth(List<AttendanceKidsDetailDateResponse> listAttendanceKidsDetailDateResponse, List<ListIdKidDTO> kidDTOList);

    List<ExcelNewResponse> detachedListAttendanceKidsClassOfMonthNew(List<AttendanceKidsDetailDateResponse> listAttendanceKidsDetailDateResponse, List<ListIdKidDTO> kidDTOList,Long idSchool, Long idClass, LocalDate date);

    List<AttendanceKidsDetailDateResponse> findAttendanceKidsClassCustom(Long idSchool, AttendanceKidsSearchCustomRequest request);

    List<ListIdKidDTO> listIdAttendanceKidsDetailCustom(Long idSchool, AttendanceKidsSearchCustomRequest request);

    void saveAttendanceEatAuto(AttendanceKids attendanceKids);


}
