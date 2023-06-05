package com.example.onekids_project.mobile.parent.controller;


import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.parent.response.home.*;
import com.example.onekids_project.mobile.parent.service.servicecustom.HomeParentService;
import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.MobileMaUserResponse;
import com.example.onekids_project.mobile.service.servicecustom.MobileMaUserService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.JwtParentResponse;
import com.example.onekids_project.service.servicecustom.DeviceService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.ParentService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/mob/parent")
public class HomeParentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HomeParentService homeParentService;

    @Autowired
    private MobileMaUserService mobileMaUserService;

    @Autowired
    private ParentService parentService;

    @Autowired
    private KidsService kidsService;
    @Autowired
    private DeviceService deviceService;


    /**
     * lấy dữ liệu trước khi vào màn home
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/home-first")
    public ResponseEntity searchHomeFirstParent(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            JwtParentResponse jwtParentResponse = homeParentService.findHomeFirstParent(principal);
            if (jwtParentResponse == null) {
                return NewDataResponse.setDataCustom(jwtParentResponse, "Không có thông tin chung");
            }
            return NewDataResponse.setDataSearch(jwtParentResponse);
        } catch (Exception e) {
            logger.error("Exception Không có thông tin chung" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Không có thông tin chung", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tìm kiếm thông tin chung của màn home
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/home")
    public ResponseEntity searchHomeParent(@CurrentUser UserPrincipal principal, DeviceWebRequest deviceWebRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            HomeParentResponse homeParentResponse = homeParentService.findHomeParent(principal);
            deviceService.forceLogoutDevice(deviceWebRequest.getIdDevice(), principal.getId());
            if (homeParentResponse == null) {
                return NewDataResponse.setDataCustom(homeParentResponse, "Không có thông tin phụ huynh");
            }
            return NewDataResponse.setDataSearch(homeParentResponse);
        } catch (Exception e) {
            logger.error("Exception tìm kiếm thông tin phụ huynh thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi tìm kiếm thông tin phụ huynh thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * thay đổi học sinh
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/change-kid")
    public ResponseEntity changeKidsParent(@CurrentUser UserPrincipal principal, @RequestParam Long idKid) {
        CommonValidate.checkDataParent(principal);
        ChangeTokenResponse changeTokenResponse = homeParentService.changeKids(principal, idKid);
        return NewDataResponse.setDataUpdate(changeTokenResponse);
    }

    /**
     * tìm kiếm danh bạ của phụ huynh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/phone-book")
    public ResponseEntity searchPhoneOfParent(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            List<PhoneBookOfParentResponse> phoneBookOfParentResponseList = homeParentService.findPhoneOfParent(principal);
            if (CollectionUtils.isEmpty(phoneBookOfParentResponseList)) {
                return NewDataResponse.setDataCustom(phoneBookOfParentResponseList, "Không có danh bạ nào của phụ huynh");
            }
            return NewDataResponse.setDataSearch(phoneBookOfParentResponseList);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm danh bạ nào của phụ huynh" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm danh bạ nào của phụ huynh", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tìm kiếm thông báo
     *
     * @param principal
     * @param pageNumber
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/notifi")
    public ResponseEntity searchNotifiOfParent(@CurrentUser UserPrincipal principal, String pageNumber) {
        CommonValidate.checkDataParent(principal);
        int page = ConvertData.getPageNumber(pageNumber);
        if (page == -1) {
            logger.error(AppConstant.INVALID_PAGE_NUMBER);
            return DataResponse.getData(AppConstant.INVALID_PAGE_NUMBER, HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, AppConstant.MAX_PAGE_ITEM);
        ListMobileNotifiParentResponse ListMobileNotifiParentResponse = homeParentService.findNotifiKidsForMobile(principal, pageable);
        return NewDataResponse.setDataSearch(ListMobileNotifiParentResponse);
    }

    /**
     * xem chi tiết thông báo của phụ huynh
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/notifi-detail/{id}")
    public ResponseEntity showNotifiDetailOfParent(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        CommonValidate.checkDataParent(principal);
        try {
            MobileNotifiDetailParentResponse mobileNotifiDetailParentResponse = homeParentService.findNotifiByIdForMobile(id);
            return NewDataResponse.setDataSearch(mobileNotifiDetailParentResponse);
        } catch (Exception e) {
            logger.error("Exception Lỗi xem chi tiết thông báo của phụ huynh" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi xem chi tiết thông báo của phụ huynh", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tìm kiếm số thông báo chưa đọc
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/notifi-unread")
    public ResponseEntity showNotifiDetailOfParent(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            long countUnread = homeParentService.findNotifiUnRead(principal);
            return NewDataResponse.setDataSearch(countUnread);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm kiếm số thông báo chưa đọc" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm số thông báo chưa đọc", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tìm kiếm thông tin tài khoản
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/account")
    public ResponseEntity getProfileOfFather(@CurrentUser UserPrincipal principal) {
        try {
            CommonValidate.checkDataParent(principal);
            MobileMaUserResponse mobileMaUserResponse = mobileMaUserService.getMaUserByIdOfFather(principal);
            return NewDataResponse.setDataSearch(mobileMaUserResponse);
        } catch (Exception e) {
            logger.error("Exception Không có thông tin tài khoản" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Không có thông tin tài khoản", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * save avatar
     *
     * @param principal
     * @param multipartFile
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/avatar")
    public ResponseEntity saveAvatar(@CurrentUser UserPrincipal principal, @ModelAttribute MultipartFile multipartFile) {
        try {
            CommonValidate.checkDataParent(principal);
            boolean checkSave = parentService.saveAvatar(principal, multipartFile);
            return NewDataResponse.setDataCreate(checkSave);
        } catch (Exception e) {
            logger.error("Exception Lỗi lưu avatar: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi lưu avatar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public ResponseEntity getKidsInfor(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        KidsInforResponse response = kidsService.findKidsInfor(principal);
        return NewDataResponse.setDataSearch(response);
    }

}
