package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.response.finance.order.OrderKidsParentResponse;
import com.example.onekids_project.mobile.plus.response.salary.NumberSalaryPlusResponse;
import com.example.onekids_project.mobile.teacher.response.absentletter.NumberSalaryTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.salary.ListAttendanceTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-04-20 09:07
 *
 * @author lavanviet
 */
public interface SalaryTeacherService {
    ListAttendanceTeacherResponse getAttendanceTeacher(UserPrincipal principal, LocalDate date, Long idInfo);

    List<OrderKidsParentResponse>  searchOrderTeacher(UserPrincipal principal, OrderKidsParentRequest request, Long idInfo);

    /**
     * Hiện thị số thông báo mục công lương app teacher
     * @param principal
     * @return
     */
    NumberSalaryTeacherResponse showNumberSalary(UserPrincipal principal);

    /**
     * Hiện thị số thông báo mục công lương app plus
     * @param principal
     * @return
     */
    NumberSalaryPlusResponse showNumberPlus(UserPrincipal principal);
}
