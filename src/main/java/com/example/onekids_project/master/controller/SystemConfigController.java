package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.master.request.api.ApiUpdateRequest;
import com.example.onekids_project.master.response.api.ApiMasterResponse;
import com.example.onekids_project.master.response.school.ConfigNotifyResponse;
import com.example.onekids_project.master.response.school.icon.IconLockRequest;
import com.example.onekids_project.master.response.school.icon.IconLockResponse;
import com.example.onekids_project.request.celebrate.CelebrateSampleActiveRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleCreateRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleUpdateRequest;
import com.example.onekids_project.request.finance.ChangeSortRequest;
import com.example.onekids_project.request.onecam.OneCamConfigRequest;
import com.example.onekids_project.request.schoolconfig.*;
import com.example.onekids_project.request.system.*;
import com.example.onekids_project.request.user.AppTypeRequest;
import com.example.onekids_project.response.celebrate.CelebrateSampleResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.onecam.OneCamConfigResponse;
import com.example.onekids_project.response.schoolconfig.AttendanceSampleConfigResponse;
import com.example.onekids_project.response.schoolconfig.BirthdaySampleResponse;
import com.example.onekids_project.response.schoolconfig.EvaluateSampleConfigResponse;
import com.example.onekids_project.response.system.*;
import com.example.onekids_project.response.wishes.WishesSampleResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.onecam.OneCamConfigService;
import com.example.onekids_project.service.servicecustom.system.ApiService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/master/system-config")
public class SystemConfigController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttendanceSampleService attendanceSampleService;

    @Autowired
    private EvaluateSampleService evaluateSampleService;

    @Autowired
    private WebSystemTitleService webSystemTitleService;

    @Autowired
    private AppVersionConfigService appVersionConfigService;

    @Autowired
    private SysInforService sysInforService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AppIconParentService appIconParentService;

    @Autowired
    private AppIconTeacherService appIconTeacherService;

    @Autowired
    private AppIconPlusService appIconPlusService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private BirthdaySampleService birthdaySampleService;

    @Autowired
    private WishesSampleService wishesSampleService;


    @Autowired
    private CelebrateSampleService celebrateSampleService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private OneCamConfigService oneCamConfigService;

    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-sample")
    public ResponseEntity findAllAttendanceSample() {
        List<AttendanceSampleConfigResponse> attendanceSampleConfigResponseList = attendanceSampleService.findAllAttendanceSampleSytem();
        return NewDataResponse.setDataSearch(attendanceSampleConfigResponseList);
    }

    /**
     * tạo mẫu điểm danh mặc định
     *
     * @param attendanceSampleCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/attendance-sample")
    public ResponseEntity createAttendanceSample(@Valid @RequestBody AttendanceSampleCreateRequest attendanceSampleCreateRequest) {
        AttendanceSampleConfigResponse attendanceSampleConfigResponse = attendanceSampleService.createAttendanceSampleSystem(attendanceSampleCreateRequest);
        return NewDataResponse.setDataCreate(attendanceSampleConfigResponse);
    }

    /**
     * cập nhật mẫu điểm danh mặt định
     *
     * @param principal
     * @param attendanceSampleUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/attendance-sample")
    public ResponseEntity updateAttendanceSample(@CurrentUser UserPrincipal principal, @Valid @RequestBody AttendanceSampleUpdateRequest attendanceSampleUpdateRequest) {
        AttendanceSampleConfigResponse attendanceSampleConfigResponse = attendanceSampleService.updateAttendanceSampleSystem(attendanceSampleUpdateRequest);
        return NewDataResponse.setDataUpdate(attendanceSampleConfigResponse);
    }

    /**
     * xóa mẫu điểm danh mặc định
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/attendance-sample/{id}")
    public ResponseEntity deleteAttendanceSample(@PathVariable("id") Long id) {
        boolean checkDelete = attendanceSampleService.deleteAttendanceSampleSystem(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wishes-sample")
    public ResponseEntity findAllWishesSample() {
        List<WishesSampleResponse> wishesSampleResponseList = wishesSampleService.findAllSystemConfig();
        return NewDataResponse.setDataSearch(wishesSampleResponseList);
    }

    /**
     * tạo mẫu wishes sample
     *
     * @param wishesSampleCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/wishes-sample")
    public ResponseEntity createWishesSample(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute WishesSampleCreateRequest wishesSampleCreateRequest) throws IOException {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), wishesSampleCreateRequest);
        WishesSampleResponse wishesSampleResponse = wishesSampleService.createWishesSample(SystemConstant.ID_SYSTEM, wishesSampleCreateRequest);
        return NewDataResponse.setDataCreate(wishesSampleResponse);
    }

    /**
     * tạo mẫu wishes sample
     *
     * @param wishesSampleUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/wishes-sample/update")
    public ResponseEntity updateWishesSample(@Valid @ModelAttribute WishesSampleUpdateRequest wishesSampleUpdateRequest) throws IOException {
        WishesSampleResponse wishesSampleResponse = wishesSampleService.updateWishesSample(SystemConstant.ID_SYSTEM, wishesSampleUpdateRequest);
        return NewDataResponse.setDataUpdate(wishesSampleResponse);
    }

    /**
     * delete wishes sample
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/wishes-sample/{id}")
    public ResponseEntity deleteWishesSample(@PathVariable() Long id) {
        boolean checkDelete = wishesSampleService.deleteWishesSample(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    /**
     * tìm kiếm mẫu nhận xét mặc định
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/evaluate-sample")
    public ResponseEntity findAllEvaluateSample() {
        List<EvaluateSampleConfigResponse> evaluateSampleConfigResponseList = evaluateSampleService.findAllEvaluateSampleSystem();
        return NewDataResponse.setDataSearch(evaluateSampleConfigResponseList);
    }

    /**
     * tạo mẫu nhận xét mặc định
     *
     * @param evaluateSampleCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/evaluate-sample")
    public ResponseEntity createEvaluateSample(@Valid @RequestBody EvaluateSampleCreateRequest evaluateSampleCreateRequest) {
        EvaluateSampleConfigResponse evaluateSampleConfigResponse = evaluateSampleService.createEvaluateSampleSystem(evaluateSampleCreateRequest);
        return NewDataResponse.setDataCreate(evaluateSampleConfigResponse);
    }

    /**
     * cập nhật mẫu nhận xét mặt định
     *
     * @param evaluateSampleUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/evaluate-sample")
    public ResponseEntity updateEvaluateSample(@Valid @RequestBody EvaluateSampleUpdateRequest evaluateSampleUpdateRequest) {
        EvaluateSampleConfigResponse evaluateSampleConfigResponse = evaluateSampleService.updateEvaluateSampleSystem(evaluateSampleUpdateRequest);
        return NewDataResponse.setDataUpdate(evaluateSampleConfigResponse);
    }

    /**
     * xóa mẫu nhận xét mặc định
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/evaluate-sample/{id}")
    public ResponseEntity deleteEvaluateSample(@PathVariable("id") Long id) {
        boolean checkDelete = evaluateSampleService.deleteEvaluateSampleSystem(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    /**
     * tìm kiếm tiêu đề, nội dung
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/web-system-title")
    public ResponseEntity findAllWebSystemTitle() {
        try {
            List<WebSystemTitleConfigResponse> webSystemTitleConfigResponseList = webSystemTitleService.findAllWebSystemTitle();
            if (CollectionUtils.isEmpty(webSystemTitleConfigResponseList)) {
                logger.warn("Lỗi tìm kiếm thiết lập tiêu đề, nội dung");
                return DataResponse.getData("Lỗi tìm kiếm thiết lập tiêu đề, nội dung", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm thiết lập tiêu đề, nội dung thành công");
            return DataResponse.getData(webSystemTitleConfigResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm thiết lập tiêu đề, nội dung" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm thiết lập tiêu đề, nội dung", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * cập nhật thông tin
     *
     * @param webSystemTitleConfigRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/web-system-title")
    public ResponseEntity updateWebSystemTitle(@RequestBody List<WebSystemTitleConfigRequest> webSystemTitleConfigRequestList) {
        try {
            boolean checkUpate = webSystemTitleService.updateWebSystemTitle(webSystemTitleConfigRequestList);
            if (!checkUpate) {
                logger.warn("Lỗi cập nhật thiết lập tiêu đề, nội dung");
                return DataResponse.getData("Lỗi cập nhật thiết lập tiêu đề, nội dung", HttpStatus.NOT_FOUND);
            }
            logger.info("cập nhật thiết lập tiêu đề, nội dung thành công");
            return DataResponse.getData("cập nhật thiết lập tiêu đề, nội dung thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật thiết lập tiêu đề, nội dung" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật thiết lập tiêu đề, nội dung", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tìm kiếm app version
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/system/app-version")
    public ResponseEntity findAllAppVersion() {
        try {
            List<AppVersionConfigResponse> appVersionConfigResponseList = appVersionConfigService.findAllAppVersion();
            if (CollectionUtils.isEmpty(appVersionConfigResponseList)) {
                logger.warn("Lỗi tìm kiếm app version");
                return DataResponse.getData("Lỗi tìm kiếm app version", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm app version thành công");
            return DataResponse.getData(appVersionConfigResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm app version" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm app version", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update app version
     *
     * @param appVersionConfigRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/system/app-version")
    public ResponseEntity updateAppVersion(@Valid @RequestBody AppVersionConfigRequest appVersionConfigRequest) {
        try {
            AppVersionConfigResponse appVersionConfigResponse = appVersionConfigService.updateAppVersion(appVersionConfigRequest);
            if (appVersionConfigResponse == null) {
                logger.warn("Lỗi update app version");
                return DataResponse.getData("Lỗi update app version", HttpStatus.NOT_FOUND);
            }
            logger.info("update app version thành công");
            return DataResponse.getData(appVersionConfigResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi update app version" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi update app version", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find sysinfor
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/system/infor")
    public ResponseEntity findSysInfor(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        SysInforResponse response = sysInforService.getFirstSupportOnekids();
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * update sysinfor
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/system/infor")
    public ResponseEntity updateSysInfor(@CurrentUser UserPrincipal principal, @Valid @RequestBody SysInforRequest sysInforRequest) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), sysInforRequest);
        SysInforResponse sysInforResponse = sysInforService.updateSystemInfor(sysInforRequest);
        return NewDataResponse.setDataUpdate(sysInforResponse);
    }

    /**
     * find all system config icon
     *
     * @param schoolConfigSeachRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school")
    public ResponseEntity findSchoolSystemConfigIcon(SchoolConfigSeachRequest schoolConfigSeachRequest) {
        try {
            List<SchoolSystemConfigIconResponse> schoolSystemConfigIconResponseList = schoolService.findAllSchoolConfigIcon(schoolConfigSeachRequest);
            if (schoolSystemConfigIconResponseList == null) {
                logger.warn("Lỗi tìm kiếm tất cả trường trong systemconfig");
                return DataResponse.getData("Lỗi tìm kiếm tất cả trường trong systemconfig", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm tất cả trường trong systemconfig thành công");
            return DataResponse.getData(schoolSystemConfigIconResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm tất cả trường trong systemconfig" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm tất cả trường trong systemconfig", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find icon parent for school
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school/icon-parent/{id}")
    public ResponseEntity findSchoolIconParent(@PathVariable Long id) {
        IconParentConfigResponse iconParentConfigResponse = appIconParentService.findIconByIdSchoolConfig(id);
        return DataResponse.getData(iconParentConfigResponse, HttpStatus.OK);
    }

    /**
     * update icon parent
     *
     * @param id
     * @param iconParentConfigRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/school/icon-parent/{id}")
    public ResponseEntity updateSchoolIconParent(@PathVariable Long id, @Valid @RequestBody IconParentConfigRequest iconParentConfigRequest) {
        IconParentConfigResponse iconParentConfigResponse = appIconParentService.updateIconParentShool(id, iconParentConfigRequest);
        return DataResponse.getData(iconParentConfigResponse, HttpStatus.OK);
    }

    /**
     * find icon teacher for school
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school/icon-teacher/{id}")
    public ResponseEntity findSchoolIconTeacher(@PathVariable Long id) {
        try {
            IconTeacherConfigResponse iconTeacherConfigResponse = appIconTeacherService.findIconByIdSchoolConfig(id);
            logger.info("Tìm kiếm icon teacher thành công");
            return DataResponse.getData(iconTeacherConfigResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm icon teacher" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm icon teacher", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update icon teacher
     *
     * @param id
     * @param iconTeacherConfigRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/school/icon-teacher/{id}")
    public ResponseEntity updateSchoolIconTeacher(@PathVariable Long id, @Valid @RequestBody IconTeacherConfigRequest iconTeacherConfigRequest) {
        try {
            IconTeacherConfigResponse iconTeacherConfigResponse = appIconTeacherService.updateIconSchool(id, iconTeacherConfigRequest);
            logger.info("Update icon teacher thành công");
            return DataResponse.getData(iconTeacherConfigResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi icon teacher" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi icon teacher", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find icon plus for school
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school/icon-plus/{id}")
    public ResponseEntity findSchoolIconPlus(@PathVariable Long id) {
        IconPlusConfigResponse iconPlusConfigResponse = appIconPlusService.findIconByIdSchoolConfig(id);
        return DataResponse.getData(iconPlusConfigResponse, HttpStatus.OK);
    }

    /**
     * update icon plus
     *
     * @param id
     * @param iconPlusConfigRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/school/icon-plus/{id}")
    public ResponseEntity updateSchoolIconPlus(@PathVariable Long id, @Valid @RequestBody IconPlusConfigRequest iconPlusConfigRequest) {
        IconPlusConfigResponse iconPlusConfigResponse = appIconPlusService.updateIconSchool(id, iconPlusConfigRequest);
        return DataResponse.getData(iconPlusConfigResponse, HttpStatus.OK);
    }

    /**
     * find system config for school
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school/config/{id}")
    public ResponseEntity findSystemCongifForSchool(@PathVariable Long id) {
        SystemConfigSchoolTotalResponse systemConfigSchoolTotalResponse = sysConfigService.findSystemConfigSchool(id);
        return NewDataResponse.setDataSearch(systemConfigSchoolTotalResponse);
    }

    /**
     * udpate systemconfif and schoolconfig
     *
     * @param resquest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/school/config")
    public ResponseEntity updateSystemconfigForSchool(@CurrentUser UserPrincipal principal, @Valid @RequestBody SystemConfigSchoolTotalRequest resquest) {
        RequestUtils.getFirstRequest(principal, resquest);
        CommonValidate.checkDataAdmin(principal);
        sysConfigService.updateSystemConfigSchool(resquest);
        return NewDataResponse.setDataUpdate(AppConstant.APP_TRUE);
    }

    /**
     * tìm kiếm mẫu sinh nhật
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/birthday-sample")
    public ResponseEntity findAllBirthdaySamle() {
        try {
            List<BirthdaySampleResponse> birthdaySampleResponseList = birthdaySampleService.findAllBirthdaySample(SystemConstant.ID_SYSTEM);
            if (CollectionUtils.isEmpty(birthdaySampleResponseList)) {
                logger.warn("Lỗi tìm kiếm mẫu lời chúc sinh nhật của hệ thống");
                return DataResponse.getData("Lỗi tìm kiếm mẫu lời chúc sinh nhật của hệ thống", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm mẫu lời chúc sinh nhật của hệ thống thành công");
            return DataResponse.getData(birthdaySampleResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm mẫu lời chúc sinh nhật của hệ thống" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm mẫu lời chúc sinh nhật của hệ thống", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * cập nhật kích hoạt sinh nhật
     *
     * @param birthdaySampleActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/birthday-sample/active")
    public ResponseEntity updateActiveBirthdaySample(@Valid @RequestBody BirthdaySampleActiveRequest birthdaySampleActiveRequest) {
        try {
            BirthdaySampleResponse birthdaySampleResponse = birthdaySampleService.updateBirthdaySampleActive(birthdaySampleActiveRequest);
            if (birthdaySampleResponse == null) {
                logger.warn("Lỗi cập nhật các loại kích hoạt mẫu sinh nhật của hệ thống");
                return DataResponse.getData("Lỗi cập nhật các loại kích hoạt mẫu sinh nhật của hệ thống", HttpStatus.NOT_FOUND);
            }
            logger.info("Cập nhật các loại kích hoạt mẫu sinh nhật của hệ thống thành công");
            return DataResponse.getData(birthdaySampleResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật các loại kích hoạt mẫu sinh nhật của hệ thống" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật các loại kích hoạt mẫu sinh nhật của hệ thống", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update birthday sample
     *
     * @param birthdaySampleUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/birthday-sample/update")
    public ResponseEntity updateBirthdaySample(@Valid @ModelAttribute BirthdaySampleUpdateRequest birthdaySampleUpdateRequest) {
        try {
            BirthdaySampleResponse birthdaySampleResponse = birthdaySampleService.updateBirthdaySample(SystemConstant.ID_SYSTEM, birthdaySampleUpdateRequest);
            if (birthdaySampleResponse == null) {
                logger.error("Lỗi cập nhật mẫu sinh nhật");
                return DataResponse.getData("Lỗi tạo mẫu sinh nhật", HttpStatus.NOT_FOUND);
            }
            logger.info("cập nhật mẫu sinh nhật thành công");
            return DataResponse.getData(birthdaySampleResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật mẫu sinh nhật" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật mẫu sinh nhật", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find all celebrate
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/celebrate-sample")
    public ResponseEntity findAllCelebrateSample() {
        List<CelebrateSampleResponse> celebrateSampleResponseList = celebrateSampleService.findAllCelebrateSystem(SystemConstant.ID_SYSTEM);
        return NewDataResponse.setDataSearch(celebrateSampleResponseList);
    }

    /**
     * cập nhật kích hoạt celebrate
     *
     * @param celebrateSampleActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/celebrate-sample/active")
    public ResponseEntity updateActiveCelebrateSample(@Valid @RequestBody CelebrateSampleActiveRequest celebrateSampleActiveRequest) {
        try {
            CelebrateSampleResponse celebrateSampleResponse = celebrateSampleService.updateCelebrateSampleActive(celebrateSampleActiveRequest);
            logger.info("Cập nhật các loại kích hoạt ngày lễ của hệ thống thành công");
            return DataResponse.getData(celebrateSampleResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật các loại kích hoạt mẫu ngày lễ của hệ thống" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật các loại kích hoạt mẫu ngày lễ của hệ thống", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tạo mẫu celebrate sample
     *
     * @param celebrateSampleCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/celebrate-sample")
    public ResponseEntity createCelebrateSample(@Valid @ModelAttribute CelebrateSampleCreateRequest celebrateSampleCreateRequest) {
        try {
            CelebrateSampleResponse celebrateSampleResponse = celebrateSampleService.createCelebrateSample(SystemConstant.ID_SYSTEM, celebrateSampleCreateRequest);
            logger.info("Tạo mẫu ngày lễ thành công");
            return DataResponse.getData(celebrateSampleResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tạo mẫu ngày lễ" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tạo mẫu ngày lễ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tạo mẫu celebrate sample
     *
     * @param celebrateSampleUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/celebrate-sample/update")
    public ResponseEntity updateCelebrateSample(@Valid @ModelAttribute CelebrateSampleUpdateRequest celebrateSampleUpdateRequest) {
        try {
            CelebrateSampleResponse celebrateSampleResponse = celebrateSampleService.updateCelebrateSample(SystemConstant.ID_SYSTEM, celebrateSampleUpdateRequest);
            logger.info("Cập nhật mẫu ngày lễ thành công");
            return DataResponse.getData(celebrateSampleResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật mẫu ngày lễ" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật mẫu ngày lễ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * delete celebrate sample
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/celebrate-sample/{id}")
    public ResponseEntity deleteCelebrateSample(@PathVariable() Long id) {
        try {
            celebrateSampleService.deleteCelebrateSample(id);
            logger.info("Xóa mẫu ngày lễ thành công");
            return DataResponse.getData("Xóa mẫu ngày lễ thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi xóa mẫu ngày lễ" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi xóa mẫu ngày lễ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * lấy config notify
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/config-notify/{idSchool}")
    public ResponseEntity getNotifyConfigSchool(@CurrentUser UserPrincipal principal, @PathVariable Long idSchool) {
        RequestUtils.getFirstRequest(principal, idSchool);
        ConfigNotifyResponse response = schoolService.getConfigNotify(principal, idSchool);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * update config notify
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/config-notify")
    public ResponseEntity updateNotifyConfigSchool(@CurrentUser UserPrincipal principal, @Valid @RequestBody ConfigNotifyResponse request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = schoolService.updateConfigNotify(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/config-icon-lock/{idSchool}")
    public ResponseEntity getConfigIconLock(@CurrentUser UserPrincipal principal, @PathVariable Long idSchool) {
        RequestUtils.getFirstRequest(principal, idSchool);
        IconLockResponse response = schoolService.getIconLockConfig(principal, idSchool);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/config-icon-lock")
    public ResponseEntity updateConfigIconLock(@CurrentUser UserPrincipal principal, @Valid @RequestBody IconLockRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = schoolService.updateIconLockConfig(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api")
    public ResponseEntity getApi(@CurrentUser UserPrincipal principal, @Valid AppTypeRequest request) {
        RequestUtils.getFirstRequest(principal);
        List<ApiMasterResponse> responseList = apiService.searchApiMaster(principal, request.getType());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api")
    public ResponseEntity updateApi(@CurrentUser UserPrincipal principal, @Valid @RequestBody ApiUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = apiService.updateApi(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api/ranks")
    public ResponseEntity updateApiRank(@CurrentUser UserPrincipal principal, @Valid @RequestBody ChangeSortRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = apiService.updateApiRanks(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/onecam/config")
    public ResponseEntity searchOneCamConfigController(@RequestParam Long idSchool) {
        OneCamConfigResponse response = oneCamConfigService.getOneCamConfigService(idSchool);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/onecam/config")
    public ResponseEntity updateOneCamSetting(@Valid @RequestBody OneCamConfigRequest request) {
        oneCamConfigService.getOneCamConfigService(request);
        return NewDataResponse.setDataUpdate(true);
    }
}
