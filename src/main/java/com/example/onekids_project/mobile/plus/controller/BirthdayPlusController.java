package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.birthday.BirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.request.birthday.SearchKidsBirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.response.birthday.CoutbirthDayPlusResponse;
import com.example.onekids_project.mobile.plus.response.birthday.ListKidsBirthdayPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsbirthdayPlusMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/mob/plus/birthday")
public class BirthdayPlusController {

    @Autowired
    private KidsbirthdayPlusMobileService kidsbirthdayPlusMobileService;

    /**
     * Danh sách ngày
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/day")
    public ResponseEntity searchKidbirthdayPlus(@CurrentUser UserPrincipal principal, @Valid SearchKidsBirthdayPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListKidsBirthdayPlusResponse listKidsBirthdayPlusResponse = kidsbirthdayPlusMobileService.searchKidsBirthdayPlus(principal, request);
        return NewDataResponse.setDataSearch(listKidsBirthdayPlusResponse);
    }

    /**
     * Danh sách tuần
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity findBirthWeek(@CurrentUser UserPrincipal principal, @Valid SearchKidsBirthdayPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        LocalDateTime datatime = LocalDateTime.now();
        ListKidsBirthdayPlusResponse listKidsBirthdayPlusResponse = kidsbirthdayPlusMobileService.findBirthWeekList(principal, request, datatime);
        return NewDataResponse.setDataSearch(listKidsBirthdayPlusResponse);
    }

    /**
     * Danh sách tháng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchMonthBirthdayPlus(@CurrentUser UserPrincipal principal, @Valid SearchKidsBirthdayPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListKidsBirthdayPlusResponse listKidsBirthdayPlusResponse = kidsbirthdayPlusMobileService.searchMonthBirthdayPlus(principal, request);
        return NewDataResponse.setDataSearch(listKidsBirthdayPlusResponse);
    }

    /**
     * Đếm số sinh nhật
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public ResponseEntity sumBirthday(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        CoutbirthDayPlusResponse sumBirthday = kidsbirthdayPlusMobileService.coutbirthday(principal);
        return NewDataResponse.setDataSearch(sumBirthday);
    }

    /**
     * Gửi lời chúc sinh nhật cho học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createBirthdayKids(@CurrentUser UserPrincipal principal, @RequestBody @Valid BirthdayPlusRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = kidsbirthdayPlusMobileService.sendKidsBirthday(principal, request);
        return NewDataResponse.setMessage(MessageConstant.CREATE_BIRTHDAY);
    }

}
