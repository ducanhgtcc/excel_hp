package com.example.onekids_project.master.controller;

import com.example.onekids_project.master.request.SchoolMasterSearchRequest;
import com.example.onekids_project.master.request.school.SchoolMasterCreateRequest;
import com.example.onekids_project.master.request.school.SchoolMasterUpdateRequest;
import com.example.onekids_project.master.response.school.ListSchoolMasterResponse;
import com.example.onekids_project.master.response.school.SchoolMasterForSchoolResponse;
import com.example.onekids_project.master.response.school.SchoolMasterResponse;
import com.example.onekids_project.master.service.AccountSchoolService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.MaUserActiveRequest;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/account-school")
public class SchoolMasterController {

    private static final Logger logger = LoggerFactory.getLogger(SchoolMasterController.class);

    @Autowired
    private AccountSchoolService accountSchoolService;

    @Autowired
    private MaUserService maUserService;


    /**
     * search account school master
     *
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity getAllAcountSchool(@CurrentUser UserPrincipal principal, @Valid SchoolMasterSearchRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListSchoolMasterResponse dataList = accountSchoolService.getAllAccountSchool(request);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * create account schoolmaster
     *
     * @param schoolMasterCreateRequest
     * @return
     */
    @PostMapping
    public ResponseEntity createAccountSchool(@Valid @RequestBody SchoolMasterCreateRequest schoolMasterCreateRequest) {
        try {
            boolean checkCreate = accountSchoolService.createAccountSchool(schoolMasterCreateRequest);
            if (!checkCreate) {
                logger.error("Lỗi thêm mới tài khoản master cho Trường");
                return ErrorResponse.errorData("Lỗi thêm mới tài khoản master cho Trường", "Lỗi thêm mới tài khoản master cho Trường", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("Thêm tài khoản master cho Trường thành công");
            return DataResponse.getData(checkCreate, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi thêm mới tài khoản master cho Trường: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi thêm mới tài khoản master cho Trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update school master
     *
     * @param schoolMasterUpdateRequest
     * @return
     */
    @PutMapping()
    public ResponseEntity update(@Valid @RequestBody SchoolMasterUpdateRequest schoolMasterUpdateRequest) {
        try {
            SchoolMasterResponse schoolMasterResponse = accountSchoolService.updateAccountSchool(schoolMasterUpdateRequest);
            if (schoolMasterResponse == null) {
                logger.error("Lỗi cập nhật tài khoản Trường");
                return ErrorResponse.errorData("Không thể cập nhật tài khoản Trường", "Không thể cập nhật tài khoản Trường", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("cập nhật Trường thành công");
            return DataResponse.getData(schoolMasterResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception lỗi cập nhật Trường: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật Trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * delete school master
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        try {
            boolean checkDelete = accountSchoolService.deleteSchoolMaster(id);
            if (!checkDelete) {
                logger.error("lỗi xóa tài khoản trường");
                return ErrorResponse.errorData("lỗi xóa tài khoản trường", "lỗi xóa tài khoản trường", HttpStatus.BAD_REQUEST);
            }
            logger.info("xóa tài khoản trường thành công");
            return DataResponse.getData("Xóa tài khoản trường thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception lỗi xóa tài khoản trường" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "lỗi xóa tài khoản trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update active account agent
     *
     * @param maUserActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/active")
    public ResponseEntity updateSchoolActive(@Valid @RequestBody MaUserActiveRequest maUserActiveRequest) {
        try {
            boolean checkActive = maUserService.checkActiveUser(maUserActiveRequest);
            if (!checkActive) {
                logger.error("Lỗi cập nhật kích hoạt tài khoản trường");
                return DataResponse.getData("Lỗi cập nhật kích hoạt tài khoản trường", HttpStatus.NOT_FOUND);
            }
            logger.info("Cập nhật kích hoạt tài khoản trường thành công");
            return DataResponse.getData("Cập nhật kích hoạt tài khoản trường thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật kích hoạt tài khoản trường" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật kích hoạt tài khoản trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update active mamy account school
     *
     * @param maUserActiveRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/active-many")
    public ResponseEntity updateAgentManyActive(@Valid @RequestBody List<MaUserActiveRequest> maUserActiveRequestList) {
        try {
            boolean checkActive = maUserService.checkActiveManyUser(maUserActiveRequestList);
            if (!checkActive) {
                logger.error("Lỗi cập nhật kích hoạt tài khoản trường");
                return DataResponse.getData("Lỗi cập nhật kích hoạt tài khoản trường", HttpStatus.NOT_FOUND);
            }
            logger.info("Cập nhật kích hoạt tài khoản trường thành công");
            return DataResponse.getData("Cập nhật kích hoạt tài khoản trường thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi cập nhật kích hoạt tài khoản trường" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi cập nhật kích hoạt tài khoản trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lấy tài khoản Trường theo ID School
     *
     * @param idSchool
     * @return
     */
    @GetMapping(value = "/id-school/{idSchool}")
    public ResponseEntity getByIdSchool(@PathVariable("idSchool") Long idSchool) {
        try {
            List<SchoolMasterForSchoolResponse> schoolMasterForSchoolResponseList = accountSchoolService.getAccountSchoolByIdSchool(idSchool);
            if (CollectionUtils.isEmpty(schoolMasterForSchoolResponseList)) {
                logger.warn("Không có tài khoản Trường theo id=" + idSchool);
                return DataResponse.getData("không tìm thấy Trường nào", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm tài khoản Trường theo id thành công");
            return DataResponse.getData(schoolMasterForSchoolResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Không tìm thấy tài khoản Trường theo id: " + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Không tìm thấy tài khoản Trường theo id", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
