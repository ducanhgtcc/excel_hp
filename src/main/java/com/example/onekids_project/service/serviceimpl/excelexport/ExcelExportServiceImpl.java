package com.example.onekids_project.service.serviceimpl.excelexport;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.GroupOutEmployee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.entity.kids.GroupOutKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.kids.ExcelGroupOutRequest;
import com.example.onekids_project.request.parentdiary.ExportMedicineRequest;
import com.example.onekids_project.response.employee.EmployeeGroupOutResponse;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.excelexport.ExcelExportService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-05-13 08:42
 *
 * @author lavanviet
 */
@Service
public class ExcelExportServiceImpl implements ExcelExportService {
    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private MedicineRepository messageMedicineRepository;

    @Autowired
    private AbsentLetterRepository messageAbsentLetterRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private GroupOutKidsRepository groupOutKidsRepository;
    @Autowired
    private GroupOutEmployeeRepository groupOutEmployeeRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Override
    public List<ExcelResponse> getExportMessageParent(UserPrincipal principal, IdListRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ExcelResponse> responseList = new ArrayList<>();
        request.getIdList().forEach(x -> {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<MessageParent> messageParentList = messageParentRepository.findByKidsIdAndIdSchoolAndParentMessageDelFalseAndDelActiveTrue(x, idSchool);
            ExcelResponse response = new ExcelResponse();
            response.setSheetName(kids.getFullName().concat(AppConstant.SPACE_EXPORT_ID).concat(kids.getId().toString()));

            List<ExcelData> headerList = new ArrayList<>();
            List<ExcelData> bodyList = new ArrayList<>();
            //lấy header
            headerList.add(this.getHeaderOne(kids.getFullName().concat(AppConstant.SPACE_EXPORT_ID).concat(kids.getMaClass().getClassName())));
            headerList.add(this.getHeaderOne("Danh sách lời nhắn"));

            //lấy body
            int i = 0;
            for (MessageParent a : messageParentList) {
                i++;
                List<String> bodyStringList = Arrays.asList(String.valueOf(i), ConvertData.convertDatettotimeDDMMYY(a.getCreatedDate()), a.getMessageContent(), String.valueOf(a.getMessageParentAttachFileList().size()),
                        a.isConfirmStatus() ? AppConstant.CONFIRM_YES : AppConstant.CONFIRM_NO, a.getTeacherReply(), a.getSchoolReply());
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setHeaderList(headerList);
            response.setBodyList(bodyList);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExportMedicine(UserPrincipal principal, IdListRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ExcelResponse> responseList = new ArrayList<>();
        request.getIdList().forEach(x -> {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<Medicine> medicineList = messageMedicineRepository.findByKidsIdAndIdSchoolAndParentMedicineDelFalseAndDelActiveTrue(x, idSchool);
            ExcelResponse response = new ExcelResponse();
            response.setSheetName(kids.getFullName().concat(AppConstant.SPACE_EXPORT_ID).concat(kids.getId().toString()));

            List<ExcelData> headerList = new ArrayList<>();
            List<ExcelData> bodyList = new ArrayList<>();

            //lấy header
            headerList.add(this.getHeaderOne(kids.getFullName().concat(AppConstant.SPACE_EXPORT_ID).concat(kids.getMaClass().getClassName())));
            headerList.add(this.getHeaderOne("Danh sách dặn thuốc"));

            //lấy body
            int i = 0;
            for (Medicine medicine : medicineList) {
                i++;
                List<String> bodyStringList = Arrays.asList(String.valueOf(i), ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()), medicine.getMedicineContent(), ConvertData.convertLocalDateToString(medicine.getFromDate()).concat(" - ").concat(ConvertData.convertLocalDateToString(medicine.getToDate())),
                        String.valueOf(medicine.getMedicineAttachFileList().size()), medicine.isConfirmStatus() ? AppConstant.CONFIRM_YES : AppConstant.CONFIRM_NO, medicine.getTeacherReply(), medicine.getSchoolReply());
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setHeaderList(headerList);
            response.setBodyList(bodyList);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExportMedicineDate(UserPrincipal principal, ExportMedicineRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> headerList = new ArrayList<>();
        List<ExcelData> bodyList = new ArrayList<>();
        MaClass maClass = maClassRepository.findById(request.getIdClass()).orElseThrow();
        List<String> headerStringList = Arrays.asList("Trường mầm non: ".concat(principal.getSchool().getSchoolName()), "Lớp: ".concat(maClass.getClassName()), "Danh sách dặn thuốc");
        headerList.addAll(ExportExcelUtils.setHeaderExcel(headerStringList));
        List<Kids> kidsList = kidsRepository.findKidsByIdList(request.getIdList());
        request.getIdList().forEach(x -> {
            List<Medicine> medicineList = messageMedicineRepository.searchMedicineDate(x, idSchool, request.getDate());
            int i = 0;
            for (Medicine medicine : medicineList) {
                i++;
                List<String> bodyStringList = Arrays.asList(String.valueOf(i), medicine.getKids().getFullName(), ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()), medicine.getMedicineContent(), ConvertData.convertLocalDateToString(medicine.getFromDate()).concat(" - ").concat(ConvertData.convertLocalDateToString(medicine.getToDate())),
                        String.valueOf(medicine.getMedicineAttachFileList().size()), medicine.isConfirmStatus() ? AppConstant.CONFIRM_YES : AppConstant.CONFIRM_NO, medicine.getTeacherReply(), medicine.getSchoolReply());
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
        });
        response.setSheetName("Dặn thuốc ngày");
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExportAbsentLetter(UserPrincipal principal, IdListRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ExcelResponse> responseList = new ArrayList<>();
        request.getIdList().forEach(x -> {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<AbsentLetter> absentLetterList = messageAbsentLetterRepository.findByKidsIdAndIdSchoolAndParentAbsentDelFalseAndDelActiveTrue(x, idSchool);
            ExcelResponse response = new ExcelResponse();
            response.setSheetName(kids.getFullName().concat(AppConstant.SPACE_EXPORT_ID).concat(kids.getId().toString()));

            List<ExcelData> headerList = new ArrayList<>();
            List<ExcelData> bodyList = new ArrayList<>();

            //lấy header
            headerList.add(this.getHeaderOne(kids.getFullName().concat(AppConstant.SPACE_EXPORT_ID).concat(kids.getMaClass().getClassName())));
            headerList.add(this.getHeaderOne("Danh sách xin nghỉ"));

            //lấy body
            int i = 0;
            for (AbsentLetter a : absentLetterList) {
                i++;
                List<String> bodyStringList = Arrays.asList(String.valueOf(i), ConvertData.convertDatettotimeDDMMYY(a.getCreatedDate()), a.getAbsentContent(), ConvertData.convertLocalDateToString(a.getFromDate()).concat(" - ").concat(ConvertData.convertLocalDateToString(a.getToDate())),
                        String.valueOf(a.getAbsentLetterAttachFileList().size()), a.isConfirmStatus() ? AppConstant.CONFIRM_YES : AppConstant.CONFIRM_NO, a.getTeacherReply(), a.getSchoolReply());
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setHeaderList(headerList);
            response.setBodyList(bodyList);
            responseList.add(response);
        });
        return responseList;
    }

    /**
     * Xuất excel SMS
     *
     * @param request
     * @return
     */
    @Override
    public List<ExcelResponse> getExportExcelSMS(Long idSchool, IdListRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        response.setSheetName("DS học sinh");
        int i = 0;
        for (Long id : request.getIdList()) {
            Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow();
            if (kids.getParent() != null) {
                i++;
                List<String> bodyStringList = Arrays.asList(String.valueOf(i), kids.getKidCode(), kids.getFullName(), kids.getMaClass().getClassName(), "");
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
                response.setBodyList(bodyList);
            }
        }
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExcelGroupOutKids(UserPrincipal principal, IdListRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        List<ExcelData> headerList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";

        response.setSheetName("DS học sinh");
        int i = 0;
        for (Long id : request.getIdList()) {
            Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveFalse(id, principal.getIdSchoolLogin()).orElseThrow();
            String className = kids.getMaClass().getClassName() != null ? kids.getMaClass().getClassName() : "";
            List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH ĐÃ RA TRƯỜNG", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(className), AppConstant.EXCEL_DATE.concat(ConvertData.convertDateString(LocalDate.now())));
            headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            i++;
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), kids.getKidCode(), kids.getFullName(), kids.getMaClass().getClassName(),
                    ConvertData.convertLocalDateToString(kids.getBirthDay()),
                    kids.getGender(), kids.getAddress(),
                    ConvertData.convertLocalDateToString(kids.getDateStart()),
                    ConvertData.convertLocalDateToString(kids.getOutDate()));
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExcelGroupOutKidsProviso(UserPrincipal principal, ExcelGroupOutRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.searchKidsGroupOutExcel(idSchool, request);
        List<GroupOutKids> groupOutKidsList = groupOutKidsRepository.findAllByIdSchoolAndDelActiveTrue(idSchool);
        List<ExcelResponse> responseList = new ArrayList<>();
        List<ExcelData> headerList;
        SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";
        List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH ĐÃ RA TRƯỜNG", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_DATE.concat(ConvertData.convertDateString(LocalDate.now())), "");
        headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        for (GroupOutKids groupOutKids : groupOutKidsList) {
            ExcelResponse response = new ExcelResponse();
            List<ExcelData> bodyList = new ArrayList<>();
            response.setSheetName(groupOutKids.getName().concat(AppConstant.SPACE_EXPORT_ID).concat(String.valueOf(groupOutKids.getId())));
            int i = 1;
            for (Kids kids : kidsList.stream().filter(a -> a.getGroupOutKids().getId().equals(groupOutKids.getId())).collect(Collectors.toList())) {
                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), kids.getKidCode(), kids.getFullName(), kids.getMaClass().getClassName(),
                        ConvertData.convertLocalDateToString(kids.getBirthDay()),
                        kids.getGender(), kids.getAddress(),
                        ConvertData.convertLocalDateToString(kids.getDateStart()),
                        ConvertData.convertLocalDateToString(kids.getOutDate()));
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setHeaderList(headerList);
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExcelGroupOutEmployee(UserPrincipal principal, IdListRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
        assert schoolResponse != null;
        List<String> headerStringList = Arrays.asList("DANH SÁCH NHÂN SỰ RA TRƯỜNG", AppConstant.EXCEL_SCHOOL.concat(schoolResponse.getSchoolName()), AppConstant.EXCEL_DATE.concat(ConvertData.convertDateString(LocalDate.now())), "");
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        response.setSheetName("DS nhân sự");
        response.setHeaderList(headerList);
        int i = 1;
        for (Long id : request.getIdList()) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveFalse(id, principal.getIdSchoolLogin()).orElseThrow();
            EmployeeGroupOutResponse model = modelMapper.map(infoEmployeeSchool, EmployeeGroupOutResponse.class);
            List<String> departmentList = new ArrayList<>();
            model.getDepartmentEmployeeList().forEach(x -> departmentList.add(x.getDepartment().getDepartmentName()));
            String code = departmentList.toString().replace("[", "");
            String dataCode = code.replace("]", "");
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), model.getCode(), model.getFullName(),
                    dataCode, ConvertData.convertLocalDateToString(model.getBirthday()),
                    model.getGender(), model.getPhone(), ConvertData.convertLocalDateToString(model.getStartDate()), ConvertData.convertLocalDateToString(model.getOutDate()));
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
            response.setBodyList(bodyList);
        }
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExcelGroupOutEmployeeProviso(UserPrincipal principal, ExcelGroupOutRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchGroupOutExcel(idSchool, request);
        List<GroupOutEmployee> groupOutEmployeeList = groupOutEmployeeRepository.findAllByIdSchoolAndDelActiveTrue(idSchool);
        List<ExcelResponse> responseList = new ArrayList<>();
        List<ExcelData> headerList;
        SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";
        List<String> headerStringList = Arrays.asList("DANH SÁCH NHÂN SỰ ĐÃ RA TRƯỜNG", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_DATE.concat(ConvertData.convertDateString(LocalDate.now())), "");
        headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        for (GroupOutEmployee groupOutEmployee : groupOutEmployeeList) {
            ExcelResponse response = new ExcelResponse();
            List<ExcelData> bodyList = new ArrayList<>();
            response.setSheetName(groupOutEmployee.getName().concat(AppConstant.SPACE_EXPORT_ID).concat(String.valueOf(groupOutEmployee.getId())));
            int i = 1;
            for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList.stream().filter(a -> a.getGroupOutEmployee().getId().equals(groupOutEmployee.getId())).collect(Collectors.toList())) {
                EmployeeGroupOutResponse model = modelMapper.map(infoEmployeeSchool, EmployeeGroupOutResponse.class);
                List<String> departmentList = new ArrayList<>();
                model.getDepartmentEmployeeList().forEach(x -> departmentList.add(x.getDepartment().getDepartmentName()));
                String code = departmentList.toString().replace("[", "");
                String dataCode = code.replace("]", "");
                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), model.getCode(), model.getFullName(),
                        dataCode, ConvertData.convertLocalDateToString(model.getBirthday()),
                        model.getGender(), model.getPhone(), ConvertData.convertLocalDateToString(model.getStartDate()), ConvertData.convertLocalDateToString(model.getOutDate()));
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setHeaderList(headerList);
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    private ExcelData getHeaderOne(String pro1) {
        ExcelData model = new ExcelData();
        model.setPro1(pro1);
        return model;
    }

}
