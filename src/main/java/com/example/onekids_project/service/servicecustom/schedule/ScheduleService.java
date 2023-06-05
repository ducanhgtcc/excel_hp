package com.example.onekids_project.service.servicecustom.schedule;

import com.example.onekids_project.request.classmenu.CreateFileAndPictureMenuMultiClassRequest;
import com.example.onekids_project.request.schedule.*;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.schedule.ScheduleInClassWeekResponse;
import com.example.onekids_project.response.schedule.ScheduleResponse;
import com.example.onekids_project.response.schedule.TabDetailScheduleAllClassResponse;
import com.example.onekids_project.response.schedule.TabScheduleViewDetail;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface ScheduleService {
    /**
     * Tìm kiếm tất cả thời khóa biểu trong tuần (Màn hình xem)
     *
     * @param idSchool
     * @param searchScheduleRequest
     * @return
     */
    List<ScheduleResponse> findAllScheduleInWeek(Long idSchool, SearchScheduleRequest searchScheduleRequest);
    //List<ScheduleInClassResponse> findAllScheduleInClassByTimeSchedule(Long idSchool, SearchScheduleInClassRequest searchScheduleInClassRequest);

    /**
     * Tìm kiếm tất cả thời khóa biểu trong tuần của 1 class
     *
     * @param idSchool
     * @param searchScheduleInClassRequest
     * @return
     */
    List<ScheduleInClassWeekResponse> findAllScheduleInClassWeekByTimeSchedule(Long idSchool, SearchScheduleInClassRequest searchScheduleInClassRequest);

    List<ExcelResponse> findAllScheduleInClassWeekByTimeScheduleNew(Long idSchool, SearchScheduleInClassRequest searchScheduleInClassRequest);

    /**
     * Tạo thời khóa biểu trong tuần
     *
     * @param idSchool
     * @param scheduleInClassWeekRequestList
     * @return
     */
    boolean saveScheduleClassWeek(Long idSchool, UserPrincipal principal, List<ScheduleInClassWeekRequest> scheduleInClassWeekRequestList);

    /**
     * Tạo nhiều thời khóa biểu cho nhiều lớp
     *
     * @param idSchool
     * @param createMultiSchedule
     * @return
     */
    boolean saveMultiSchedule(Long idSchool, UserPrincipal principal, CreateMultiSchedule createMultiSchedule);

    /**
     * Tìm kiếm tất cả các thời khóa biểu màn hình xem chi tiết
     *
     * @return
     */
    List<TabDetailScheduleAllClassResponse> findAllScheduleTabDetail(Long idSchool, SearchScheduleRequest searchScheduleRequest);

    boolean updateApprove(Long idSchool, ApproveStatus approveStatus);

    boolean updateMultiApprove(Long idSchool, List<ApproveStatus> approveStatusList);

    List<TabScheduleViewDetail> findScheduleDetailByClass(Long idSchool, Long idClass);

    boolean deleteContentSchedule(List<SearchScheduleInClassRequest> searchScheduleInClassRequestList);

    boolean saveScheduleFile(Long idSchool, UserPrincipal principal, CreateFileAndPictureRequest createFileAndPictureRequest) throws IOException;

    boolean deleteScheduleFileById(Long idSchool, Long idUrlScheduleFile);

    boolean saveScheduleTitleClass(Long idSchoolLogin, CreateTitleClassRequest createTitleClassRequest);

    boolean createFileAndPictureMultiClass(UserPrincipal principal, CreateFileAndPictureMenuMultiClassRequest fileAndPictureMenuMultiClassRequest) throws IOException;

}
