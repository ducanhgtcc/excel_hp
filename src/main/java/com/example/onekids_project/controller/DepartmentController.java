package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.DepartmentDTO;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.department.CreateDepartmentRequest;
import com.example.onekids_project.request.department.UpdateDepartmentRequest;
import com.example.onekids_project.request.employee.SearchInfoEmployeeRequest;
import com.example.onekids_project.request.employee.TransferEmployeeDepartmentRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.department.CreateDepartmentResponse;
import com.example.onekids_project.response.department.DepartmentResponse;
import com.example.onekids_project.response.employee.ListInfoEmployeeSchoolResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DepartmentService;
import com.example.onekids_project.service.servicecustom.InfoEmployeeSchoolService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private InfoEmployeeSchoolService infoEmployeeSchoolService;

    /**
     * tìm kiếm phòng ban theo id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<DepartmentDTO> optionalDepartmentDTO = departmentService.findByIdDepartment(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(optionalDepartmentDTO);

    }

    /**
     * tìm kiếm phòng ban
     *
     * @param principal
     * @param
     * @return
     */
    @GetMapping
    public ResponseEntity search(@CurrentUser UserPrincipal principal, @Valid PageNumberWebRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<DepartmentResponse> responseList = departmentService.findAllDepartment(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Thêm mới Phòng ban
     *
     * @param principal
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateDepartmentRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        CreateDepartmentResponse createDepartmentResponse = departmentService.saveDeparment(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(createDepartmentResponse);

    }

    /**
     * Sửa phòng ban
     *
     * @param id
     * @param principal
     * @param updateDepartmentRequest
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateDepartmentRequest updateDepartmentRequest) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = departmentService.updateDeparment(principal.getIdSchoolLogin(), updateDepartmentRequest);
        return NewDataResponse.setDataUpdate(check);

    }

    /**
     * xóa Phòng theo id
     *
     * @param id
     * @param principal
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = departmentService.deleteDepartment(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataDelete(check);
    }

    @PutMapping("/transfer-department")
    public ResponseEntity updateTransferDepartment(@CurrentUser UserPrincipal principal, @Valid @RequestBody TransferEmployeeDepartmentRequest transferEmployeeDepartmentRequest) {
        RequestUtils.getFirstRequest(principal, transferEmployeeDepartmentRequest);
        CommonValidate.checkDataPlus(principal);
        boolean check = departmentService.insertTransferEmployeeInDepartment(principal.getIdSchoolLogin(), transferEmployeeDepartmentRequest);
        return NewDataResponse.setDataCustom(check, "Chuyển đổi nhóm học sinh thành công");

    }

//    /**
//     * Tìm kiếm nhân viên
//     *
//     * @param principal
//     * @return
//     */
//    @GetMapping("/infoEmployee")
//    public ResponseEntity search(@CurrentUser UserPrincipal principal, SearchInfoEmployeeRequest searchInfoEmployeeRequest) {
//        RequestUtils.getFirstRequestPlus(principal, searchInfoEmployeeRequest);
//        //check id_school_login có tồn tại trong tài khoản của người dùng đang đăng nhập hay không
//        Long idSchoolLogin = principal.getIdSchoolLogin();
//        if (idSchoolLogin == null || idSchoolLogin <= 0) {
//            logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//            return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//        }
//        //lấy số trang
//        int pageNumber = ConvertData.getPageNumber(searchInfoEmployeeRequest.getPageNumber());
//        if (pageNumber == -1) {
//            logger.error("Số trang không hợp lệ");
//            return DataResponse.getData("Số trang không hợp lệ", HttpStatus.NOT_FOUND);
//        }
//        Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);
//        ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse = infoEmployeeSchoolService.findAllInfoEmployeeSchool(searchInfoEmployeeRequest, null, idSchoolLogin);
//
//        if (listInfoEmployeeSchoolResponse == null) {
//            logger.warn("Không tìm thấy nhân viên nào");
//            return DataResponse.getData("Không tìm thấy nhân viên nào", HttpStatus.NOT_FOUND);
//        }
//
//        //trả ra thông tin khối học theo id
//        logger.info("Tìm kiếm nhân viên thành công");
//        return DataResponse.getData(listInfoEmployeeSchoolResponse, HttpStatus.OK);
//
//    }
}
