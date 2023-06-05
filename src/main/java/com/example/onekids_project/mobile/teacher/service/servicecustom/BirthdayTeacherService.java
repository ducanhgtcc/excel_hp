package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.request.BirthdayTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.birthday.BirthdaySampleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.birthday.ListBirthdayTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.birthday.SumBirthdayTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface BirthdayTeacherService {
    /**
     * tìm kiếm danh sách học sinh có sinh nhaatjt rong ngày, tuần, tháng
     *
     * @param principal
     * @param
     * @return
     */
    ListBirthdayTeacherResponse findBirthdayList(UserPrincipal principal, LocalDateTime localDate);

    ListBirthdayTeacherResponse findBirthWeekList(UserPrincipal principal, LocalDateTime localDate);

    ListBirthdayTeacherResponse findBirthMonthList(UserPrincipal principal, LocalDateTime localDate);

    /**
     * tạo sinh nhật
     *
     * @param principal
     * @param birthdayTeacherRequest
     * @return
     */
    boolean createBirthday(UserPrincipal principal, BirthdayTeacherRequest birthdayTeacherRequest) throws FirebaseMessagingException;

    BirthdaySampleTeacherResponse findWishTeacher(UserPrincipal principal);

    SumBirthdayTeacherResponse sumBirthday(UserPrincipal principal);
}
