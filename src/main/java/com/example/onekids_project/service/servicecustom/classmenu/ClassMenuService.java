package com.example.onekids_project.service.servicecustom.classmenu;

import com.example.onekids_project.request.classmenu.*;
import com.example.onekids_project.request.schedule.ApproveStatus;
import com.example.onekids_project.response.classmenu.TabAllClassMenuInWeekResponse;
import com.example.onekids_project.response.classmenu.TabClassMenuByIdClassInWeek;
import com.example.onekids_project.response.classmenu.TabClassMenuViewDetail;
import com.example.onekids_project.response.classmenu.TabDetailClassMenuAllClassResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.util.List;


public interface ClassMenuService {
    /**
     * Tìm kiếm các thực đơn của các lớp trong 1 tuần
     *
     * @param idSchool
     * @param searchAllClassMenuRequest
     * @return
     */
    List<TabAllClassMenuInWeekResponse> getAllClassMenuMultiClassInWeek(UserPrincipal principal, Long idSchool, SearchAllClassMenuRequest searchAllClassMenuRequest);

    /**
     * Tìm thực đơn theo Id Class
     *
     * @param idSchool
     * @param idClass
     * @param searchAllClassMenuRequest
     * @return
     */
    List<TabClassMenuByIdClassInWeek> getClassMenuByIdClassInWeek(UserPrincipal principal, Long idSchool, Long idClass, SearchAllClassMenuRequest searchAllClassMenuRequest);

    List<ExcelResponse> getClassMenuByIdClassInWeekNew(Long idSchool, SearchAllClassMenuRequest searchAllClassMenuRequest);

    boolean createClassMenuInClassInWeek(Long idSchool, UserPrincipal principal, List<TabClassMenuByIdClassInWeekRequest> tabClassMenuByIdClassInWeekRequestList);

    boolean saveClassMenuMultiClassMultiWeek(Long idSchool, UserPrincipal principal, CreateMultiClassMenu createMultiClassMenu);

    /**
     * Tìm kiếm tất cả các thực đơn màn hình xem chi tiết
     *
     * @return
     */
    List<TabDetailClassMenuAllClassResponse> findAllClassMenuTabDetail(Long idSchool, SearchAllClassMenuRequest searchAllClassMenuRequest);

    List<TabClassMenuViewDetail> findClassMenuDetailByClass(Long idSchool, Long idClass);

    boolean updateApprove(Long idSchool, ApproveStatus approveStatus);

    boolean updateMultiApprove(Long idSchool, List<ApproveStatus> approveStatusList);

    boolean saveMenuFile(Long idSchool, UserPrincipal principal, CreateFileAndPictureMenuRequest createFileAndPictureMenuRequest) throws IOException;

    boolean deletMenuFileById(Long idSchool, Long idUrlMenuFile);

    boolean deleteContentMenu(List<SearchAllClassMenuRequest> searchAllClassMenuRequests);

    boolean createFileAndPictureMultiClass(UserPrincipal principal, CreateFileAndPictureMenuMultiClassRequest fileAndPictureMenuMultiClassRequest) throws IOException;

//    boolean deleteContentClassMenu(List<SearchScheduleInClassRequest> searchScheduleInClassRequestList);
}
