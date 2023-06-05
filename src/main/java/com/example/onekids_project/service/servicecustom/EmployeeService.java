package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.importexport.model.EmployeeModelImport;
import com.example.onekids_project.master.request.employee.UpdateEmployeeAdminRequest;
import com.example.onekids_project.request.createnotifyschool.CreateEmployeeNotify;
import com.example.onekids_project.request.employee.CreateEmployeeRequest;
import com.example.onekids_project.request.employee.EmployeeGroupOutRequest;
import com.example.onekids_project.request.employee.SearchEmployeeGroupOutRequest;
import com.example.onekids_project.request.employee.UpdateEmployeeRequest;
import com.example.onekids_project.request.common.StatusCommonRequest;
import com.example.onekids_project.request.kids.SmsStudentRequest;
import com.example.onekids_project.request.smsNotify.SMSRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyRequest;
import com.example.onekids_project.response.accounttype.AccountTypeOtherResponse;
import com.example.onekids_project.response.department.TabDepartmentResponse;
import com.example.onekids_project.response.employee.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmployeeService {

    boolean createAccountAndEmployeeForOther(InfoEmployeeSchool infoEmployeeSchool);

    /**
     * Tìm kiếm nhân viên theo Id
     *
     * @param id
     * @return
     */
    InfoEmployeeSchoolDTO findByIdEmployee(Long id);

    /**
     * Tạo nhân viên
     *
     * @param createEmployeeRequest
     * @return
     */
    boolean createEmployee(CreateEmployeeRequest createEmployeeRequest, UserPrincipal principal);

    /**
     * Sửa nhân viên
     *
     * @param updateEmployeeRequest
     * @return
     */
    boolean updateEmployee(UserPrincipal principal, Long idUrl, UpdateEmployeeRequest updateEmployeeRequest);

    boolean updateEmployeeAdmin(UpdateEmployeeAdminRequest updateEmployeeRequest);

    /**
     * Tạo mới hoặc Sửa nhân viên
     *
     * @param id
     * @param employeeDTO
     * @return
     */
    /*public Optional<EmployeeDTO> saveOrUpdate(Long id, EmployeeDTO employeeDTO);*/

    /**
     * Xóa nhân viên(set delActive chuyển về false)
     *
     * @param id
     */
    boolean deleteEmployee(UserPrincipal principal, Long id);

    boolean deleteEmployeeAdmin(Long id);

    boolean restoreEmployeeAdmin(Long id);

    boolean deleteMultiEmployee(UserPrincipal principal, Long[] ids);

    ListExEmployeeClassResponse findByIdEmployeeClass(Long idEmployee);

    /**
     * Xóa nhân viên ở những phòng ban
     *
     * @param idEmployee
     * @return
     */
    boolean deleteEmployeeDepartment(Long idEmployee);

    /**
     * Tìm kiếm tất cả các đối tượng
     *
     * @return
     */
    List<AccountTypeOtherResponse> findAllAccountTypeByIdSchool(Long idSchool);

//    /**
//     * Update tất cả các trạng thái Activated
//     *
//     * @param updateEmployeeMainInfoRequestList
//     * @param idSchool
//     * @return
//     */
//    public List<UpdateEmployeeMainInfoResponse> updateMultiEmployee(List<UpdateEmployeeMainInfoRequest> updateEmployeeMainInfoRequestList, Long idSchool);

    /***
     * Lấy thông tin Tab phòng ban trong nhân viên
     * @return
     */
    List<TabDepartmentResponse> getTabDepartmentInEmployee(Long idSchool, Long idEmployee);

    /**
     * Lấy thông tin Tab chuyên môn trong employee
     *
     * @param id
     * @param idEmployee
     * @return
     */
    List<TabProfessionalResponse> getTabProfessionalInEmployee(Long id, Long idEmployee);

    /**
     * Lấy các môn học ở trong lớp
     *
     * @return
     */
    List<SubjectInClassResponse> getSubjectInClass(Long idClass, Long id);

    /**
     * Tìm kiếm thông tin nhân viên
     *
     * @return
     */

    void uploadAvatarEmployee(Long idSchool, MultipartFile multipartFile) throws IOException;

    void uploadAvatarEditEmployee(Long idSchool, MultipartFile multipartFile, Long idEmployee, String fileName) throws IOException;

    /**
     * Create thông báo cho employee
     *
     * @param principal
     * @param createEmployeeNotify
     * @return
     */
    boolean createEmployeeNotify(UserPrincipal principal, CreateEmployeeNotify createEmployeeNotify) throws IOException, FirebaseMessagingException;

    boolean createEmployeeNotifySms(UserPrincipal principal, SmsNotifyRequest smsNotifyRequest) throws ExecutionException, InterruptedException;
    boolean sendSmsInfoEmployee(UserPrincipal principal, SMSRequest request) throws ExecutionException, InterruptedException;
    boolean createEmployeeSms(UserPrincipal  principal, SmsStudentRequest request) throws ExecutionException, InterruptedException;

    List<EmployeeModelImport> saveDataEmployeeExcel(List<EmployeeModelImport> employeeModels, UserPrincipal principal);

    boolean sendAccountEmployeeSms(UserPrincipal principal, List<Long> idEmployee) throws ExecutionException, InterruptedException;

    boolean updateStatusForEmployee(UserPrincipal principal, StatusCommonRequest request);

    boolean updateEmployeeGroupOut(UserPrincipal principal, EmployeeGroupOutRequest request);

    ListEmployeeGroupOutResponse searchEmployeeGroupOut(UserPrincipal principal, SearchEmployeeGroupOutRequest request);

    EmployeeDetailGroupOutResponse findByIdEmployeeGroupOut(UserPrincipal principal, Long id);

    boolean restoreEmployeeGroupOut(UserPrincipal principal, Long id);
}
