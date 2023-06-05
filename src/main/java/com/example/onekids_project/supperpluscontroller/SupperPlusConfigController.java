package com.example.onekids_project.supperpluscontroller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleActiveRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleCreateRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleUpdateRequest;
import com.example.onekids_project.request.schoolconfig.*;
import com.example.onekids_project.request.system.BirthdaySampleUpdateRequest;
import com.example.onekids_project.request.user.AppTypeRequest;
import com.example.onekids_project.request.user.SearchRoleRequest;
import com.example.onekids_project.response.celebrate.CelebrateSampleResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.schoolconfig.*;
import com.example.onekids_project.response.user.ApiResponse;
import com.example.onekids_project.response.user.RoleForUserResponse;
import com.example.onekids_project.response.user.UserRoleResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.config.SupperPlusConfigService;
import com.example.onekids_project.service.servicecustom.system.ApiService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-05-24 09:39
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/supperPlus-config")
public class SupperPlusConfigController {

    @Autowired
    private SupperPlusConfigService supperPlusConfigService;
    @Autowired
    private CameraService cameraService;
    @Autowired
    private DvrCameraService dvrCameraService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private BirthdaySampleService birthdaySampleService;
    @Autowired
    private CelebrateSampleService celebrateSampleService;
    @Autowired
    private ApiService apiService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MaUserService maUserService;

    /**
     * tìm kiếm cấu hình nhà trường của người đang đang nhập
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school")
    public ResponseEntity getSchoolConfigById(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        SchoolConfigResponse sysConfigResponse = supperPlusConfigService.findSchoolConfigByIdSchool(principal);
        return NewDataResponse.setDataSearch(sysConfigResponse);
    }


    /**
     * cập nhật cấu hình chung cho trường
     *
     * @param resquest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/school")
    public ResponseEntity updateConfigSchoolCommon(@CurrentUser UserPrincipal principal, @Valid @RequestBody SchoolConfigRequest resquest) {
        RequestUtils.getFirstRequest(principal, resquest);
        CommonValidate.checkDataSupperPlus(principal);
        supperPlusConfigService.updateConfigCommon(principal, resquest);
        return NewDataResponse.setDataUpdate(true);
    }

    /**
     * tìm kiếm tất cả các media
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/media")
    public ResponseEntity findAllMedia(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<MediaConfigResponse> responseList = mediaService.findAllMedia(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật kích hoạt media
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/media-active-one")
    public ResponseEntity updateActiveMedia(@CurrentUser UserPrincipal principal, @Valid @RequestBody MediaActiveRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = mediaService.checkActiveMedia(request.getId(), request.getMediaActive());
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * cập nhật media
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/media")
    public ResponseEntity updateMedia(@CurrentUser UserPrincipal principal, @Valid @RequestBody MediaUpdateRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        MediaConfigResponse response = mediaService.updateMedia(request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * xóa media theo id
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/media/{id}")
    public ResponseEntity deleteMediaOne(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = mediaService.deleteMediaOne(id);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * xóa nhiều media
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/media/delete")
    public ResponseEntity deleteMediaMany(@CurrentUser UserPrincipal principal, @RequestBody List<MediaUpdateRequest> request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = mediaService.deleteMediaMany(request);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * tạo media
     *
     * @param mediaCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/media")
    public ResponseEntity createMedia(@CurrentUser UserPrincipal principal, @Valid @RequestBody MediaCreateRequest mediaCreateRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        MediaConfigResponse response = mediaService.createMedia(principal.getIdSchoolLogin(), mediaCreateRequest);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tìm kiếm media setting cho các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/media-setting")
    public ResponseEntity findAllMediaSetting(@CurrentUser UserPrincipal principal, @Valid MediaSettingSearchRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<MediaSettingResponse> responseList = mediaService.findAllMediaSetting(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm media cho lớp
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/media-class/{id}")
    public ResponseEntity findAllMediaForClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        List<MediaForClassResponse> responseList = mediaService.findMediaForClass(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật media cho một lớp
     *
     * @param principal
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/media-class/{id}")
    public ResponseEntity updateMediaOfClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody List<IdObjectRequest> request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = mediaService.updateMediaForClass(id, request);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * tìm kiếm tất cả dvrcamera
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/dvrcamera")
    public ResponseEntity findAllDvrCamera(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<DvrCameraResponse> responseList = dvrCameraService.findAllDvrCamera(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tạo dvrcamera
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/dvrcamera")
    public ResponseEntity createDvrCamera(@CurrentUser UserPrincipal principal, @Valid @RequestBody DvrCameraCreateRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        DvrCameraResponse response = dvrCameraService.createDvrCamera(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật dvrcamra
     *
     * @param principal
     * @param dvrCameraUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/dvrcamera")
    public ResponseEntity updateDvrCamera(@CurrentUser UserPrincipal principal, @Valid @RequestBody DvrCameraUpdateRequest dvrCameraUpdateRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        DvrCameraResponse response = dvrCameraService.updateDvrCamera(principal.getIdSchoolLogin(), dvrCameraUpdateRequest);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * xóa dvrcamera theo id
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/dvrcamera/{id}")
    public ResponseEntity deleteDvrcameraOne(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = dvrCameraService.deleteDvrcameraOne(id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * xóa nhiều dvrcamera
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/dvrcamera/delete")
    public ResponseEntity deleteDvrcameraMany(@CurrentUser UserPrincipal principal, @RequestBody List<IdObjectRequest> request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = dvrCameraService.deleteMediaMany(request);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * cập nhật kích hoạt dvrcamera
     *
     * @param dvrcameraActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/dvrcamera/active-one")
    public ResponseEntity updateActiveDvrcamera(@CurrentUser UserPrincipal principal, @Valid @RequestBody DvrcameraActiveRequest dvrcameraActiveRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = dvrCameraService.checkActiveDvrcamera(dvrcameraActiveRequest);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * tìm kiếm tất cả camera
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/camera")
    public ResponseEntity findAllCameraList(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<CameraResponse> responseList = cameraService.findAllCamera(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tao camera
     *
     * @param principal
     * @param cameraCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/camera")
    public ResponseEntity createCamera(@CurrentUser UserPrincipal principal, @Valid @RequestBody CameraCreateRequest cameraCreateRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        CameraResponse response = cameraService.createCamera(principal.getIdSchoolLogin(), cameraCreateRequest);
        return NewDataResponse.setDataCreate(response);
    }

    /**
     * cập nhật camera
     *
     * @param principal
     * @param cameraUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/camera")
    public ResponseEntity updateCamera(@CurrentUser UserPrincipal principal, @Valid @RequestBody CameraUpdateRequest cameraUpdateRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        CameraResponse response = cameraService.updateCamera(principal.getIdSchoolLogin(), cameraUpdateRequest);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * xoa camera
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/camera/{id}")
    public ResponseEntity deleteCameraOne(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = cameraService.deleteCameraOne(id);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * xóa nhiều dvrcamera
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/camera/delete")
    public ResponseEntity deleteCameraMany(@CurrentUser UserPrincipal principal, @RequestBody List<IdObjectRequest> request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = cameraService.deleteMediaMany(request);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * cập nhật kích hoạt camera
     *
     * @param cameraActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/camera/active-one")
    public ResponseEntity updateActiveCamera(@CurrentUser UserPrincipal principal, @Valid @RequestBody CameraActiveRequest cameraActiveRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = cameraService.checkActiveCamera(cameraActiveRequest);
        return NewDataResponse.setDataSearch(check);
    }

    /**
     * tìm kiếm camera setting cho các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/camera-setting")
    public ResponseEntity findAllCameraSetting(@CurrentUser UserPrincipal principal, @Valid MediaSettingSearchRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<CameraSettingResponse> responseList = cameraService.findAllCameraSetting(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm camera cho lớp
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/camera-class/{id}")
    public ResponseEntity findAllCameraForClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        List<CameraForClassResponse> responseList = cameraService.findCameraForClass(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật camera cho một lớp
     *
     * @param principal
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/camera-class/{id}")
    public ResponseEntity updateCameraOfClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody List<IdObjectRequest> request) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = cameraService.updateCameraForClass(id, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * tìm kiếm mẫu sinh nhật
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/birthday-sample")
    public ResponseEntity findAllBirthdaySamle(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<BirthdaySampleResponse> responseList = birthdaySampleService.findAllBirthdaySample(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật kích hoạt sinh nhật
     *
     * @param birthdaySampleActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/birthday-sample/active")
    public ResponseEntity updateActiveBirthdaySample(@CurrentUser UserPrincipal principal, @Valid @RequestBody BirthdaySampleActiveRequest birthdaySampleActiveRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        BirthdaySampleResponse response = birthdaySampleService.updateBirthdaySampleActive(birthdaySampleActiveRequest);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * update mẫu sinh nhật
     *
     * @param principal
     * @param birthdaySampleUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/birthday-sample")
    public ResponseEntity updateBirthdaySample(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute BirthdaySampleUpdateRequest birthdaySampleUpdateRequest) throws IOException {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        BirthdaySampleResponse response = birthdaySampleService.updateBirthdaySample(principal.getIdSchoolLogin(), birthdaySampleUpdateRequest);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * find all celebrate
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/celebrate-sample")
    public ResponseEntity findAllCelebrateSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataSupperPlus(principal);
        List<CelebrateSampleResponse> responseList = celebrateSampleService.findAllCelebrateSystem(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật kích hoạt celebrate
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/celebrate-sample/active")
    public ResponseEntity updateActiveCelebrateSample(@CurrentUser UserPrincipal principal, @Valid @RequestBody CelebrateSampleActiveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        CelebrateSampleResponse response = celebrateSampleService.updateCelebrateSampleActive(request);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * tạo mẫu celebrate sample
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/celebrate-sample")
    public ResponseEntity createCelebrateSample(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute CelebrateSampleCreateRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        CelebrateSampleResponse response = celebrateSampleService.createCelebrateSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(response);
    }

    /**
     * tạo mẫu celebrate sample
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/celebrate-sample/update")
    public ResponseEntity updateCelebrateSample(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute CelebrateSampleUpdateRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        CelebrateSampleResponse celebrateSampleResponse = celebrateSampleService.updateCelebrateSample(principal.getIdSchoolLogin(), request);
        return DataResponse.getData(celebrateSampleResponse, HttpStatus.OK);
    }

    /**
     * delete celebrate sample
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/celebrate-sample/{id}")
    public ResponseEntity deleteCelebrateSample(@CurrentUser UserPrincipal principal, @PathVariable() Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        celebrateSampleService.deleteCelebrateSample(id);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    /**
     * tìm kiếm tất cả các nhóm quyền cua truong
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/permission-role")
    public ResponseEntity findAllRoleSchool(@CurrentUser UserPrincipal principal, @Valid SearchRoleRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        List<RoleConfigResponse> roleConfigResponseList = roleService.searchRole(principal, request);
        return NewDataResponse.setDataSearch(roleConfigResponseList);
    }

    /**
     * tìm kiếm quyền theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/permission-role/{id}")
    public ResponseEntity findRoleUser(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        RoleConfigResponse response = roleService.findRoleForUser(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tạo role
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/permission-role")
    public ResponseEntity createRoleSchool(@CurrentUser UserPrincipal principal, @Valid @RequestBody RoleCreateConfigRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = roleService.createRole(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * cap nhat role
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/permission-role")
    public ResponseEntity updateRoleSchool(@CurrentUser UserPrincipal principal, @Valid @RequestBody RoleUpdateConfigRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = roleService.updateRole(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * xóa role
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/permission-role/{id}")
    public ResponseEntity deleteRoleSchool(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataSupperPlus(principal);
        boolean check = roleService.deleteRoleOne(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * tìm kiếm tất cả các nhóm quyền cua truong
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/user-role")
    public ResponseEntity findUserRoleSchool(@CurrentUser UserPrincipal principal, @Valid AppTypeRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        List<UserRoleResponse> responseList = maUserService.findMaUserRoleSchool(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm tất cả các nhóm quyền cua truong
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/role-user/{id}")
    public ResponseEntity findRoleForUser(@CurrentUser UserPrincipal principal, @PathVariable("id") Long idUser, @Valid AppTypeRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idUser, request);
        CommonValidate.checkDataSupperPlus(principal);
        List<RoleForUserResponse> roleForUserResponseList = roleService.findRoleOfUser(principal, idUser, request);
        return DataResponse.getData(roleForUserResponseList, HttpStatus.OK);
    }

    /**
     * update role for user
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/role-user/{id}")
    public ResponseEntity updateRoleForUser(@CurrentUser UserPrincipal principal, @PathVariable("id") Long idUser, @RequestBody List<Long> longList) {
        RequestUtils.getFirstRequestExtend(principal, idUser, longList);
        CommonValidate.checkDataSupperPlus(principal);
        boolean checkUpdate = roleService.updateRoleOfUser(principal, idUser, longList);
        return NewDataResponse.setDataSave(checkUpdate);
    }

    /**
     * find all api for school
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api")
    public ResponseEntity findAllApi(@CurrentUser UserPrincipal principal, @Valid AppTypeRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataSupperPlus(principal);
        List<ApiResponse> responseList = apiService.searchApi(principal, request.getType());
        return NewDataResponse.setDataSearch(responseList);
    }

}
