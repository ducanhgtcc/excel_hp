package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.request.accounttype.AccountTypeCreateRequest;
import com.example.onekids_project.request.accounttype.AccountTypeUpdateRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.cashbook.CashBookLockedRequest;
import com.example.onekids_project.request.cashbook.CashBookUpdateMoneyStartRequest;
import com.example.onekids_project.request.classes.*;
import com.example.onekids_project.request.school.ConfigAttendanceEmployeeSchoolRequest;
import com.example.onekids_project.request.school.ConfigTimeAttendanceEmployeeRequest;
import com.example.onekids_project.request.school.UpdateForSchoolRequest;
import com.example.onekids_project.request.schoolconfig.*;
import com.example.onekids_project.request.system.WishesSampleCreateRequest;
import com.example.onekids_project.request.system.WishesSampleUpdateRequest;
import com.example.onekids_project.response.accounttype.AccountTypeDetailReponse;
import com.example.onekids_project.response.accounttype.AccountTypeResponse;
import com.example.onekids_project.response.cashbook.CashBookResponse;
import com.example.onekids_project.response.caskinternal.SchoolInfoBankResponses;
import com.example.onekids_project.response.classes.DayOffClassResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.school.ConfigAttendanceEmployeeSchoolResponse;
import com.example.onekids_project.response.school.ConfigTimeAttendanceEmployeeSchoolResponse;
import com.example.onekids_project.response.schoolconfig.*;
import com.example.onekids_project.response.subject.SubjectSearchRequest;
import com.example.onekids_project.response.wishes.WishesSampleResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.cashbook.FnCashBookService;
import com.example.onekids_project.service.servicecustom.classes.DayOffClassService;
import com.example.onekids_project.service.servicecustom.config.SchoolConfigService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/school-config")
public class SchoolConfigController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SchoolConfigService schoolConfigService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private EvaluateSampleService evaluateSampleService;
    @Autowired
    private AttendanceSampleService attendanceSampleService;
    @Autowired
    private WishesSampleService wishesSampleService;
    @Autowired
    private AccountTypeService accountTypeService;
    @Autowired
    private DayOffClassService dayOffClassService;
    @Autowired
    private SchoolInfoService schoolInfoService;
    @Autowired
    private FnCashBookService fnCashBookService;

    /**
     * tìm kiếm trường theo id
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/infor")
    public ResponseEntity getSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        SchoolDataResponse response = schoolService.getSchoolData(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật trường cho một trường
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/infor")
    public ResponseEntity updateForSchool(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute UpdateForSchoolRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean check = schoolService.updateSchoolData(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }


    /**
     * tìm kiếm các lớp trong cấu hình
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class/absent")
    public ResponseEntity searchClassConfig(@CurrentUser UserPrincipal principal, ClassConfigSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<ClassConfigResponse> responseList = schoolConfigService.searchMaclassConfig(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật cấu hình nghỉ học cho các lớp
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/class/absent")
    public ResponseEntity updateConfigClassAbsent(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<ClassConfigRequest> request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = schoolConfigService.updateConfigClassAbsent(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * tạo ngày nghỉ cho lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/class/dayOff")
    public ResponseEntity createDayOffClass(@CurrentUser UserPrincipal principal, @Valid @RequestBody DayOffClassRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        dayOffClassService.createDayOffClass(request);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    /**
     * tạo ngày nghỉ cho nhiều lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/class/dayOff/many")
    public ResponseEntity createDayOffClassMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody DayOffClassManyRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        dayOffClassService.createDayOffClassMany(request);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    /**
     * lấy danh sách ngày nghỉ theo năm
     *
     * @param principal
     * @param idClass
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class/dayOff/{id}")
    public ResponseEntity getDayOffClassYear(@CurrentUser UserPrincipal principal, @PathVariable("id") Long idClass, @Valid SearchDayOffClassRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idClass, request);
        CommonValidate.checkDataPlus(principal);
        List<DayOffClassResponse> responseList = dayOffClassService.getDayOffClassYear(idClass, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Cập nhật nội dung ngày nghỉ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/class/dayOff")
    public ResponseEntity updateDayOffClassYear(@CurrentUser UserPrincipal principal, @Valid @RequestBody DayOffClassUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        dayOffClassService.updateDayOffClassYear(principal, request);
        return NewDataResponse.setDataUpdate(AppConstant.APP_TRUE);
    }

    /**
     * xóa ngày nghỉ
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/class/dayOff/{id}")
    public ResponseEntity deleteDayOffClassYear(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        dayOffClassService.deleteDayOffClassYear(principal, id);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/class/dayOff")
    public ResponseEntity deleteDayOffClassYear(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        CommonValidate.checkDataPlus(principal);
        dayOffClassService.deleteDayOffClassYearList(principal, idList);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    /**
     * xem thông tin ngày nghỉ theo tháng
     *
     * @param principal
     * @param idClass
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class/dayOff/view/{idClass}")
    public ResponseEntity getDayOffClassYear(@CurrentUser UserPrincipal principal, @PathVariable Long idClass) {
        RequestUtils.getFirstRequest(principal, idClass);
        CommonValidate.checkDataPlus(principal);
        List<DayOffClassResponse> responseList = dayOffClassService.getDayOffClassView(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }


    /**
     * tìm kiếm các môn học cho các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/subject/manage")
    public ResponseEntity findAllSubjectManage(@CurrentUser UserPrincipal principal, @Valid ClassSearchCommonRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<SubjectForClassConfigResponse> responseList = subjectService.findAllSubjectManegeConfig(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(responseList);
    }

    /**
     * tìm kiếm môn học cho lớp
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/subject/class/{id}")
    public ResponseEntity findAllSubjectForClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<SubjectForClassResponse> responseList = subjectService.findSubjectForClass(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataUpdate(responseList);
    }

    /**
     * cập nhật subject cho một lớp
     *
     * @param principal
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/subject/class/{id}")
    public ResponseEntity updateSubjectOfClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @RequestBody List<IdObjectRequest> request) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean response = subjectService.updateSubjectForClass(id, request);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * tìm kiếm danh sách môn học
     *
     * @param principal
     * @param subjectSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/subject")
    public ResponseEntity searchSubject(@CurrentUser UserPrincipal principal, SubjectSearchRequest subjectSearchRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<SubjectConfigResponse> responseList = subjectService.searchSubjectConfig(principal.getIdSchoolLogin(), subjectSearchRequest);
        return NewDataResponse.setDataUpdate(responseList);
    }

    /**
     * tạo môn học
     *
     * @param principal
     * @param subjectCreateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/subject")
    public ResponseEntity createSubject(@CurrentUser UserPrincipal principal, @Valid @RequestBody SubjectCreateRequest subjectCreateRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        SubjectConfigResponse response = subjectService.createSubject(principal.getIdSchoolLogin(), subjectCreateRequest);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * tạo môn học
     *
     * @param subjectUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/subject")
    public ResponseEntity createSubject(@CurrentUser UserPrincipal principal, @Valid @RequestBody SubjectUpdateRequest subjectUpdateRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        SubjectConfigResponse response = subjectService.updateSubject(subjectUpdateRequest);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * xóa một môn học
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/subject/{id}")
    public ResponseEntity deleteSubjectOne(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean response = subjectService.deleteSubjectOne(id);
        return NewDataResponse.setDataDelete(response);
    }

    /**
     * xóa nhiều môn học
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/subject/delete")
    public ResponseEntity deleteSubjectMany(@CurrentUser UserPrincipal principal, @RequestBody List<IdObjectRequest> request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        int number = subjectService.deleteSubjectMany(request);
        String message = number == 0 ? "Các môn học đã chọn đã được áp dụng cho lớp" : "Xóa thành công " + number + " môn học";
        return NewDataResponse.setDataCustom(number, message);
    }

    /**
     * tìm kiếm mẫu nhận xét mặc định
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/evaluate-sample")
    public ResponseEntity findAllEvaluateSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<EvaluateSampleConfigResponse> responseList = evaluateSampleService.findAllEvaluateSample(principal.getIdSchoolLogin(), principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tạo mẫu nhận xét mặc định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/evaluate-sample")
    public ResponseEntity createEvaluateSample(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateSampleCreateRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        EvaluateSampleConfigResponse response = evaluateSampleService.createEvaluateSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(response);
    }

    /**
     * cập nhật mẫu nhận xét mặt định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/evaluate-sample")
    public ResponseEntity updateEvaluateSample(@CurrentUser UserPrincipal principal, @Valid @RequestBody EvaluateSampleUpdateRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        EvaluateSampleConfigResponse response = evaluateSampleService.updateEvaluateSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * xóa mẫu nhận xét mặc định
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/evaluate-sample/{id}")
    public ResponseEntity deleteEvaluateSample(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean response = evaluateSampleService.deleteEvaluateSample(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-sample")
    public ResponseEntity findAllAttendanceSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceSampleConfigResponse> responseList = attendanceSampleService.findAllAttendanceSample(principal.getIdSchoolLogin(), principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tạo mẫu điểm danh mặc định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/attendance-sample")
    public ResponseEntity createAttendanceSample(@CurrentUser UserPrincipal principal, @Valid @RequestBody AttendanceSampleCreateRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        AttendanceSampleConfigResponse response = attendanceSampleService.createAttendanceSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(response);
    }

    /**
     * cập nhật mẫu điểm danh mặt định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/attendance-sample")
    public ResponseEntity updateAttendanceSample(@CurrentUser UserPrincipal principal, @Valid @RequestBody AttendanceSampleUpdateRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        AttendanceSampleConfigResponse response = attendanceSampleService.updateAttendanceSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * xóa mẫu điểm danh mặc định
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/attendance-sample/{id}")
    public ResponseEntity deleteAttendanceSample(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean response = attendanceSampleService.deleteAttendanceSample(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataDelete(response);
    }

    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wishes-sample")
    public ResponseEntity findAllWishesSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<WishesSampleResponse> responseList = wishesSampleService.findAllSystemConfigSchool(principal.getIdSchoolLogin(), principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tạo mẫu wishes sample
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/wishes-sample")
    public ResponseEntity createWishesSample(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute WishesSampleCreateRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        WishesSampleResponse response = wishesSampleService.createWishesSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(response);
    }

    /**
     * tạo mẫu wishes sample
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/wishes-sample/update")
    public ResponseEntity updateWishesSample(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute WishesSampleUpdateRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        WishesSampleResponse response = wishesSampleService.updateWishesSample(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(response);
    }

    /**
     * delete wishes sample
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/wishes-sample/{id}")
    public ResponseEntity deleteWishesSample(@CurrentUser UserPrincipal principal, @PathVariable() Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean response = wishesSampleService.deleteWishesSample(id);
        return NewDataResponse.setDataDelete(response);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/account-type")
    public ResponseEntity getAccountType(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<AccountTypeResponse> responseList = accountTypeService.findAccountType(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/account-type/{id}")
    public ResponseEntity getAccountTypeById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        AccountTypeDetailReponse reponse = accountTypeService.findAccountTypeById(principal, id);
        return NewDataResponse.setDataUpdate(reponse);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/account-type")
    public ResponseEntity createAccountType(@CurrentUser UserPrincipal principal, @Valid @RequestBody AccountTypeCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = accountTypeService.createAccountType(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/account-type")
    public ResponseEntity updateAccountType(@CurrentUser UserPrincipal
                                                    principal, @Valid @RequestBody AccountTypeUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = accountTypeService.updateAccountType(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/account-type/{id}")
    public ResponseEntity deleteAccountTypeById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = accountTypeService.deleteAccountTypeById(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * lấy cấu hình điểm danh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-employee")
    public ResponseEntity getConfigAttendanceEmployeeSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        ConfigAttendanceEmployeeSchoolResponse response = schoolConfigService.getConfigAttendanceEmployeeSchool(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lấy cấu hình thời gian
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-employee/time")
    public ResponseEntity getConfigTimeAttendanceEmployeeSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        ConfigTimeAttendanceEmployeeSchoolResponse response = schoolConfigService.getConfigTimeAttendanceEmployeeSchool(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật cấu hình điểm danh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/attendance-employee")
    public ResponseEntity updateConfigAttendanceEmployeeSchool(@CurrentUser UserPrincipal principal, @RequestBody ConfigAttendanceEmployeeSchoolRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = schoolConfigService.updateConfigAttendanceEmployeeSchool(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * cập nhật cấu hình thời gian
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/attendance-employee/time")
    public ResponseEntity updateConfigTimeAttendanceEmployeeSchool(@CurrentUser UserPrincipal principal, @RequestBody ConfigTimeAttendanceEmployeeRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = schoolConfigService.updateConfigTimeAttendanceEmployeeSchool(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * tìm kiếm cashbook
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/finance/cashbook")
    public ResponseEntity findCashBook(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<CashBookResponse> responseList = fnCashBookService.findCashBook(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * cập nhật số dư đầu kì
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/finance/cashbook/money")
    public ResponseEntity updateMoneyCashBook(@CurrentUser UserPrincipal
                                                      principal, @Valid @RequestBody CashBookUpdateMoneyStartRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnCashBookService.updateMoneyStart(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * cập nhật trạng thái khóa
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/finance/cashbook/locked")
    public ResponseEntity saveCashBookLocked(@CurrentUser UserPrincipal
                                                     principal, @Valid @RequestBody CashBookLockedRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = fnCashBookService.updateLocked(principal, request);
        String message = request.getLocked() ? MessageWebConstant.SUCCESS_LOCKED : MessageWebConstant.SUCCESS_UNLOCKED;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * Cập nhật thông tin bổ sung ngân hàng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/finance/bank-info")
    public ResponseEntity createBankInfo(@CurrentUser UserPrincipal
                                                 principal, @RequestBody @Valid UpdateBankInfoRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = schoolInfoService.updateBankInfo(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_BANK);
    }

    /**
     * Xem chi tiết thông tin ngân hàng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/finance/bank-info")
    public ResponseEntity findDetailSchoolInfoBank(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkDataPlus(principal);
        SchoolInfoBankResponses response = schoolInfoService.findDetailSchoolInfoBank(principal);
        return NewDataResponse.setDataSearch(response);
    }


}
