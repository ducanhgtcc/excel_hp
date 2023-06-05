package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.BirthdayTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.birthday.BirthdaySampleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.birthday.ListBirthdayTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.birthday.SumBirthdayTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.BirthdayTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/mob/teacher/birthday")
public class BirthdayTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    BirthdayTeacherService birthdayTeacherService;

    /**
     * findBirthday
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findBirthday(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        LocalDateTime datatime = LocalDateTime.now();
        ListBirthdayTeacherResponse listBirthdayTeacherResponse = birthdayTeacherService.findBirthdayList(principal, datatime);
        return NewDataResponse.setDataSearch(listBirthdayTeacherResponse);

    }

    /**
     * findBirthday Week
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity findBirthWeek(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        LocalDateTime datatime = LocalDateTime.now();
        ListBirthdayTeacherResponse listBirthdayTeacherResponse = birthdayTeacherService.findBirthWeekList(principal, datatime);
        return NewDataResponse.setDataSearch(listBirthdayTeacherResponse);
    }

    /**
     * findBirthday Month
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity findBirthMonth(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        LocalDateTime datatime = LocalDateTime.now();
        ListBirthdayTeacherResponse listBirthdayTeacherResponse = birthdayTeacherService.findBirthMonthList(principal, datatime);
        return NewDataResponse.setDataSearch(listBirthdayTeacherResponse);

    }

    /**
     * create birdayteacher for kid
     *
     * @param principal
     * @param birthdayTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createBirthdayTeacher(@CurrentUser UserPrincipal principal, @RequestBody @Valid BirthdayTeacherRequest birthdayTeacherRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, birthdayTeacherRequest);
        boolean checkCreate = birthdayTeacherService.createBirthday(principal, birthdayTeacherRequest);
        return NewDataResponse.setMessage(MessageConstant.CREATE_BIRTHDAY);
    }

    /**
     * danh sách lời chúc, ảnh thiệp
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wishes-sample-teacher")
    public ResponseEntity findWishList(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        BirthdaySampleTeacherResponse birthdaySampleTeacherResponse = birthdayTeacherService.findWishTeacher(principal);
        return NewDataResponse.setDataSearch(birthdaySampleTeacherResponse);
    }

    /**
     * tổng số sinh nhật trong ngày +5, tuần , tháng
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sum-birthday")
    public ResponseEntity sumBirthday(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        SumBirthdayTeacherResponse sumBirthday = birthdayTeacherService.sumBirthday(principal);
        return NewDataResponse.setDataSearch(sumBirthday);
    }


}
