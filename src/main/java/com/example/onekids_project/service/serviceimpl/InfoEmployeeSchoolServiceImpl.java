package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.enums.EmployeeStatusEnum;
import com.example.onekids_project.importexport.model.EmployeeModel;
import com.example.onekids_project.master.request.employee.EmployeeSearchAdminRequest;
import com.example.onekids_project.master.response.employee.EmployeeAdminResponse;
import com.example.onekids_project.master.response.employee.ListEmployeeAdminResponse;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.response.employee.*;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DepartmentService;
import com.example.onekids_project.service.servicecustom.InfoEmployeeSchoolService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoEmployeeSchoolServiceImpl implements InfoEmployeeSchoolService {

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolService schoolService;


    @Override
    public ListEmployeeNewResponse searchEmployee(UserPrincipal principal, EmployeeSearchNew search) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        ListEmployeeNewResponse response = new ListEmployeeNewResponse();
        List<EmployeeNewResponse> dataList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.search(principal.getIdSchoolLogin(), search);
        for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList) {
            EmployeeNewResponse model = modelMapper.map(infoEmployeeSchool, EmployeeNewResponse.class);
            model.setLogin(this.getLogin(infoEmployeeSchool));
            model.setUsername(this.getUsername(infoEmployeeSchool));
            model.setPassword(this.getPassword(infoEmployeeSchool));
            model.setPhoneSMS(this.getPhone(infoEmployeeSchool));
            dataList.add(model);
        }
        long total = infoEmployeeSchoolRepository.countEmployee(principal.getIdSchoolLogin(), search);
        if (search.getLoginStatus() != null) {
            dataList = dataList.stream().filter(x -> x.isLogin() == search.getLoginStatus()).collect(Collectors.toList());
        }
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public ListEmployeePlusNewResponse searchEmployeePlus(UserPrincipal principal, EmployeePlusSearchNew search) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        ListEmployeePlusNewResponse response = new ListEmployeePlusNewResponse();
        List<EmployeePlusNewResponse> dataList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchPlus(principal.getIdSchoolLogin(), search);
        long total = infoEmployeeSchoolRepository.countEmployeePlus(principal.getIdSchoolLogin(), search);
        infoEmployeeSchoolList.forEach(x -> {
            EmployeePlusNewResponse model = modelMapper.map(x, EmployeePlusNewResponse.class);
            model.setLogin(this.getLogin(x));
            model.setUsername(this.getUsername(x));
            model.setPassword(this.getPassword(x));
            model.setPhoneSMS(this.getPhone(x));
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public ListEmployeeAdminResponse searchEmployeeAdmin(EmployeeSearchAdminRequest search) {
        ListEmployeeAdminResponse response = new ListEmployeeAdminResponse();
        List<EmployeeAdminResponse> dataList = new ArrayList<>();
        List<Long> idSchoolList = ConvertData.getIdSchoolListInAgent(schoolService.findSchoolInAgent(search.getIdAgent()));
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchEmployeeAdmin(search, idSchoolList);
        long total = infoEmployeeSchoolRepository.countEmployeeAdmin(search, idSchoolList);
        infoEmployeeSchoolList.forEach(x -> {
            EmployeeAdminResponse model = modelMapper.map(x, EmployeeAdminResponse.class);
            model.setLogin(this.getLogin(x));
            model.setUsername(this.getUsername(x));
            model.setPhoneSMS(this.getPhone(x));
            model.setPassword(this.getPassword(x));
            dataList.add(model);
        });

        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public boolean updateActiveOne(UserPrincipal principal, EmployeeUpdateActiveOneRequest request) {
        this.saveActive(request.getId(), request.isActivated());
        return true;
    }

    @Override
    public boolean updateActiveSMSOne(UserPrincipal principal, EmployeeUpdateActiveSMSOneRequest request) {
        this.saveActiveSMS(request.getId(), request.isSmsReceive());
        return true;
    }

    @Override
    public boolean updateActiveSMSSendOne(UserPrincipal principal, EmployeeUpdateActiveSMSSendOneRequest request) {
        this.saveActiveSendSMS(request.getId(), request.isSmsSend());
        return true;
    }

    @Transactional
    @Override
    public boolean updateActiveMany(UserPrincipal principal, EmployeeUpdateActiveManyRequest request) {
        request.getIdList().forEach(x -> {
            this.saveActive(x, request.isStatus());
        });
        return true;
    }

    @Override
    public boolean updateActiveSMSMany(UserPrincipal principal, EmployeeUpdateActiveManyRequest request) {
        request.getIdList().forEach(x -> {
            this.saveActiveSMS(x, request.isStatus());
        });
        return true;
    }

    @Override
    public ListInfoEmployeeSchoolResponse findAllInfoEmployeeSchool(SearchInfoEmployeeRequest searchInfoEmployeeRequest, Pageable pageable, Long idSchool) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findAllInfoEmployeeSchool(searchInfoEmployeeRequest, pageable, idSchool);
        List<InfoEmployeeSchoolDTO> infoEmployeeSchoolDTOList = new ArrayList<>();
        for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList) {
            InfoEmployeeSchoolDTO infoEmployeeSchoolDTO;
            infoEmployeeSchoolDTO = modelMapper.map(infoEmployeeSchool, InfoEmployeeSchoolDTO.class);
            if (infoEmployeeSchool.getEmployee() == null) {
                infoEmployeeSchoolDTO.setPhone("Xử lý");
            } else {
                infoEmployeeSchoolDTO.setPhone(infoEmployeeSchool.getEmployee().getMaUser().getPhone());
            }
//            if (infoEmployeeSchool.getAccountType() != null) {
//                infoEmployeeSchoolDTO.setAccountType(infoEmployeeSchool.getAccountType().split(","));
//            }
            if (infoEmployeeSchool.getDepartmentEmployeeList() != null) {
                StringBuilder derpartmentNameStr = new StringBuilder();
                for (ExDepartmentEmployee exDepartmentEmployee : infoEmployeeSchool.getDepartmentEmployeeList()) {
                    if (derpartmentNameStr.length() > 0) {
                        derpartmentNameStr.append("\n-" + exDepartmentEmployee.getDepartment().getDepartmentName());
                    } else {
                        derpartmentNameStr.append("-" + exDepartmentEmployee.getDepartment().getDepartmentName());
                    }
                }
                infoEmployeeSchoolDTO.setDepartmentName(derpartmentNameStr.toString());
            }
            if (infoEmployeeSchool.getExEmployeeClassList() != null) {
                StringBuilder classNameStr = new StringBuilder();
                for (ExEmployeeClass exEmployeeClass : infoEmployeeSchool.getExEmployeeClassList()) {
                    if (classNameStr.length() > 0) {
                        classNameStr.append("\n-" + exEmployeeClass.getMaClass().getClassName());
                    } else {
                        classNameStr.append("-" + exEmployeeClass.getMaClass().getClassName());
                    }
                }
                infoEmployeeSchoolDTO.setClassName(classNameStr.toString());
            }
            Employee employee = infoEmployeeSchool.getEmployee();
            if (employee != null) {
                MaUser maUser = employee.getMaUser();
                if (maUser != null) {
                    infoEmployeeSchoolDTO.setPasswordUser(employee.getMaUser().getPasswordShow());
                }
            }
            infoEmployeeSchoolDTOList.add(infoEmployeeSchoolDTO);
        }
        //List<InfoEmployeeSchoolDTO> infoEmployeeSchoolDTOList = infoEmployeeSchoolList.stream().map(item->modelMapper.map(item,InfoEmployeeSchoolDTO.class)).collect(Collectors.toList());
        ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse = new ListInfoEmployeeSchoolResponse(infoEmployeeSchoolDTOList);
        return listInfoEmployeeSchoolResponse;
    }

    /**
     * Tìm kiếm  tất cả các InfoEmployee (có thể lọc theo từng field)
     *
     * @param searchExportEmployeeRequest
     * @return
     */

    @Override
    public ListInfoEmployeeSchoolResponse findAllInfoEmployee(Long idSchool, SearchExportEmployeeRequest searchExportEmployeeRequest) {

        List<InfoEmployeeSchool> infoEmployeeSchoolList = new ArrayList<>();
        if (searchExportEmployeeRequest.getList() != null) {
            for (Long id : searchExportEmployeeRequest.getList()) {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.getOne(id);
                infoEmployeeSchoolList.add(infoEmployeeSchool);

            }
        } else {
            infoEmployeeSchoolList = infoEmployeeSchoolRepository.findAllInfoEmployeeSchoolByDepartment(idSchool, searchExportEmployeeRequest);
        }
        List<InfoEmployeeSchoolDTO> infoEmployeeSchoolDTOList = new ArrayList<>();
        for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList) {
            InfoEmployeeSchoolDTO infoEmployeeSchoolDTO = new InfoEmployeeSchoolDTO();
            infoEmployeeSchoolDTO = modelMapper.map(infoEmployeeSchool, InfoEmployeeSchoolDTO.class);
            if (infoEmployeeSchool.getDepartmentEmployeeList() != null) {
                StringBuilder derpartmentNameStr = new StringBuilder();
                for (ExDepartmentEmployee exDepartmentEmployee : infoEmployeeSchool.getDepartmentEmployeeList()) {
                    if (derpartmentNameStr.length() > 0) {
                        derpartmentNameStr.append(" , " + exDepartmentEmployee.getDepartment().getDepartmentName());
                    } else {
                        derpartmentNameStr.append(exDepartmentEmployee.getDepartment().getDepartmentName());
                    }
                }
                infoEmployeeSchoolDTO.setDepartmentName(derpartmentNameStr.toString());
            }
            if (infoEmployeeSchool.getExEmployeeClassList() != null) {
                StringBuilder classNameStr = new StringBuilder();
                for (ExEmployeeClass exEmployeeClass : infoEmployeeSchool.getExEmployeeClassList()) {
                    if (classNameStr.length() > 0) {
                        classNameStr.append("\n-" + exEmployeeClass.getMaClass().getClassName());
                    } else {
                        classNameStr.append("-" + exEmployeeClass.getMaClass().getClassName());
                    }
                }
                infoEmployeeSchoolDTO.setClassName(classNameStr.toString());
            }

//            if (infoEmployeeSchool.getAccountType() != null) {
//                infoEmployeeSchoolDTO.setAccountType(infoEmployeeSchool.getAccountType().split(","));
//            }

            infoEmployeeSchoolDTOList.add(infoEmployeeSchoolDTO);
        }
        //List<InfoEmployeeSchoolDTO> infoEmployeeSchoolDTOList = infoEmployeeSchoolList.stream().map(item->modelMapper.map(item,InfoEmployeeSchoolDTO.class)).collect(Collectors.toList());
        ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse = new ListInfoEmployeeSchoolResponse(infoEmployeeSchoolDTOList);
        return listInfoEmployeeSchoolResponse;

    }

    /**
     * Chuyển đổi response InfoEmployee  thành đối  tượng  EmployeeVM hiển thị lên excel
     *
     * @param listInfoEmployeeSchoolResponse
     * @return
     */


    @Override
    public List<EmployeeModel> getFileAllEmployeeByDepartment(ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse, String nameSchool) {

        List<EmployeeModel> employeeModels = new ArrayList<>();

//        long i = 1;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        for (InfoEmployeeSchoolDTO infoEmployeeSchoolDTO : listInfoEmployeeSchoolResponse.getInfoEmployeeSchoolDTOList()) {
//            EmployeeModel employeeModel = new EmployeeModel();
//            employeeModel.setStt(i++);
//            String checkStatus = "";
//            if (infoEmployeeSchoolDTO.getEmployeeStatus() != null) {
//                checkStatus = infoEmployeeSchoolDTO.getEmployeeStatus();
//            }
//
//            switch (checkStatus) {
//                case "EMPLOYEE_STATUS_WORKING":
//                    employeeModel.setEmployeeStatus(EmployeeStatusEnum.EMPLOYEE_STATUS_WORKING.getValue());
//                    break;
//                case "EMPLOYEE_STATUS_RETAIN":
//                    employeeModel.setEmployeeStatus(EmployeeStatusEnum.EMPLOYEE_STATUS_RETAIN.getValue());
//                    break;
//                case "EMPLOYEE_STATUS_LEAVE":
//                    employeeModel.setEmployeeStatus(EmployeeStatusEnum.EMPLOYEE_STATUS_LEAVE.getValue());
//                    break;
//                default:
//                    employeeModel.setEmployeeStatus(infoEmployeeSchoolDTO.getEmployeeStatus());
//
//            }
//            if (infoEmployeeSchoolDTO.getFullName() != null) {
//                employeeModel.setFullName(infoEmployeeSchoolDTO.getFullName());
//            } else {
//                employeeModel.setFullName("");
//            }
//
//            if (infoEmployeeSchoolDTO.getBirthday() != null) {
//                String birthday = infoEmployeeSchoolDTO.getBirthday().format(formatter);
//                employeeModel.setBirthday(birthday);
//            } else {
//                employeeModel.setBirthday("");
//            }
//            if (infoEmployeeSchoolDTO.getGender() != null) {
//                employeeModel.setGender(infoEmployeeSchoolDTO.getGender());
//            } else {
//                employeeModel.setGender("");
//            }
//
//            if (infoEmployeeSchoolDTO.getPhone() != null) {
//                employeeModel.setPhone(infoEmployeeSchoolDTO.getPhone());
//            } else {
//                employeeModel.setPhone("");
//            }
//
//            if (infoEmployeeSchoolDTO.getStartDate() != null) {
//                String startDate = infoEmployeeSchoolDTO.getStartDate().format(formatter);
//                employeeModel.setStartDate(startDate);
//            } else {
//                employeeModel.setStartDate("");
//            }
//            if (infoEmployeeSchoolDTO.getContractDate() != null) {
//                String contractDate = infoEmployeeSchoolDTO.getContractDate().format(formatter);
//                employeeModel.setContractDate(contractDate);
//            } else {
//                employeeModel.setContractDate("");
//            }
//            if (infoEmployeeSchoolDTO.getEndDate() != null) {
//                String endDate = infoEmployeeSchoolDTO.getEndDate().format(formatter);
//                employeeModel.setEndDate(endDate);
//            } else {
//                employeeModel.setEndDate("");
//            }
//            if (infoEmployeeSchoolDTO.getEmail() != null) {
//                employeeModel.setEmail(infoEmployeeSchoolDTO.getEmail());
//            } else {
//                employeeModel.setEmail("");
//            }
//            if (infoEmployeeSchoolDTO.getCmnd() != null) {
//                employeeModel.setCmnd(infoEmployeeSchoolDTO.getCmnd());
//            } else {
//                employeeModel.setCmnd("");
//            }
//
//            if (infoEmployeeSchoolDTO.getCmndDate() != null) {
//                String cmndDate = infoEmployeeSchoolDTO.getCmndDate().format(formatter);
//                employeeModel.setCmndDate(cmndDate);
//            } else {
//                employeeModel.setCmndDate("");
//            }
//            if (infoEmployeeSchoolDTO.getAddress() != null) {
//                employeeModel.setAddress(infoEmployeeSchoolDTO.getAddress());
//            } else {
//                employeeModel.setAddress("");
//            }
//            if (infoEmployeeSchoolDTO.getPermanentAddress() != null) {
//                employeeModel.setPermanentAddress(infoEmployeeSchoolDTO.getPermanentAddress());
//            } else {
//                employeeModel.setPermanentAddress("");
//            }
//            if (infoEmployeeSchoolDTO.getMarriedStatus() != null) {
//                employeeModel.setMarriedStatus(infoEmployeeSchoolDTO.getMarriedStatus());
//            } else {
//                employeeModel.setMarriedStatus("");
//            }
//            if (infoEmployeeSchoolDTO.getNumberChildren() != null) {
//                employeeModel.setNumberChildren(infoEmployeeSchoolDTO.getNumberChildren());
//            } else {
//                employeeModel.setNumberChildren(0);
//            }
//            if (infoEmployeeSchoolDTO.getEducationLevel() != null) {
//                employeeModel.setEducationLevel(infoEmployeeSchoolDTO.getEducationLevel());
//            } else {
//                employeeModel.setEducationLevel("");
//            }
//            if (infoEmployeeSchoolDTO.getProfessional() != null) {
//                employeeModel.setProfessional(infoEmployeeSchoolDTO.getProfessional());
//            } else {
//                employeeModel.setProfessional("");
//            }
//            if (infoEmployeeSchoolDTO.getDepartmentName() != null) {
//                employeeModel.setNameDepartment(infoEmployeeSchoolDTO.getDepartmentName());
//            } else {
//                employeeModel.setNameDepartment("");
//            }
//            if (infoEmployeeSchoolDTO.getAccountType() != null) {
//                String[] array = infoEmployeeSchoolDTO.getAccountType();
//                employeeModel.setObjectEmployee(Arrays.toString(array));
//            } else {
//                employeeModel.setObjectEmployee("");
//            }
//            if (infoEmployeeSchoolDTO.getNote() != null) {
//                employeeModel.setNote(infoEmployeeSchoolDTO.getNote());
//            } else {
//                employeeModel.setNote("");
//            }
//
//            employeeModels.add(employeeModel);
//        }
        return employeeModels;
    }

    @Override
    public List<ExcelNewResponse> getFileAllEmployeeByDepartmentNew(ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse, UserPrincipal principal) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        ExcelNewResponse response = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
        assert schoolResponse != null;
        List<String> headerStringList = Arrays.asList("DANH SÁCH NHÂN SỰ", AppConstant.EXCEL_SCHOOL.concat(schoolResponse.getSchoolName()), AppConstant.EXCEL_DATE.concat(ConvertData.convertLocalDateToString(LocalDate.now())), "");
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        response.setSheetName("Danh_sach_nhan_su");
        response.setHeaderList(headerList);
        List<EmployeeModel> employeeModels = this.setEmployeeModel(listInfoEmployeeSchoolResponse);
        for (EmployeeModel x : employeeModels) {
            List<String> bodyStringList = Arrays.asList(String.valueOf(x.getStt()), x.getEmployeeStatus(), x.getFullName(), x.getBirthday(), x.getGender(), x.getPhone(), x.getStartDate(), x.getContractDate(),
                    x.getEndDate(), x.getEmail(), x.getCmnd(), x.getCmndDate(), x.getAddress(), x.getPermanentAddress(), x.getMarriedStatus(), String.valueOf(x.getNumberChildren()), x.getEducationLevel(),
                    x.getProfessional(), x.getNameDepartment(), x.getObjectEmployee(), x.getNote());
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    private List<EmployeeModel> setEmployeeModel(ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse) {
        List<EmployeeModel> employeeModels = new ArrayList<>();

        long i = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (InfoEmployeeSchoolDTO infoEmployeeSchoolDTO : listInfoEmployeeSchoolResponse.getInfoEmployeeSchoolDTOList()) {
            EmployeeModel employeeModel = new EmployeeModel();
            employeeModel.setStt(i++);
            String checkStatus = "";
            if (infoEmployeeSchoolDTO.getEmployeeStatus() != null) {
                checkStatus = infoEmployeeSchoolDTO.getEmployeeStatus();
            }

            switch (checkStatus) {
                case "EMPLOYEE_STATUS_WORKING":
                    employeeModel.setEmployeeStatus(EmployeeStatusEnum.EMPLOYEE_STATUS_WORKING.getValue());
                    break;
                case "EMPLOYEE_STATUS_RETAIN":
                    employeeModel.setEmployeeStatus(EmployeeStatusEnum.EMPLOYEE_STATUS_RETAIN.getValue());
                    break;
                case "EMPLOYEE_STATUS_LEAVE":
                    employeeModel.setEmployeeStatus(EmployeeStatusEnum.EMPLOYEE_STATUS_LEAVE.getValue());
                    break;
                default:
                    employeeModel.setEmployeeStatus(infoEmployeeSchoolDTO.getEmployeeStatus());

            }
            if (infoEmployeeSchoolDTO.getFullName() != null) {
                employeeModel.setFullName(infoEmployeeSchoolDTO.getFullName());
            } else {
                employeeModel.setFullName("");
            }

            if (infoEmployeeSchoolDTO.getBirthday() != null) {
                String birthday = infoEmployeeSchoolDTO.getBirthday().format(formatter);
                employeeModel.setBirthday(birthday);
            } else {
                employeeModel.setBirthday("");
            }
            if (infoEmployeeSchoolDTO.getGender() != null) {
                employeeModel.setGender(infoEmployeeSchoolDTO.getGender());
            } else {
                employeeModel.setGender("");
            }

            if (infoEmployeeSchoolDTO.getPhone() != null) {
                employeeModel.setPhone(infoEmployeeSchoolDTO.getPhone());
            } else {
                employeeModel.setPhone("");
            }

            if (infoEmployeeSchoolDTO.getStartDate() != null) {
                String startDate = infoEmployeeSchoolDTO.getStartDate().format(formatter);
                employeeModel.setStartDate(startDate);
            } else {
                employeeModel.setStartDate("");
            }
            if (infoEmployeeSchoolDTO.getContractDate() != null) {
                String contractDate = infoEmployeeSchoolDTO.getContractDate().format(formatter);
                employeeModel.setContractDate(contractDate);
            } else {
                employeeModel.setContractDate("");
            }
            if (infoEmployeeSchoolDTO.getEndDate() != null) {
                String endDate = infoEmployeeSchoolDTO.getEndDate().format(formatter);
                employeeModel.setEndDate(endDate);
            } else {
                employeeModel.setEndDate("");
            }
            if (infoEmployeeSchoolDTO.getEmail() != null) {
                employeeModel.setEmail(infoEmployeeSchoolDTO.getEmail());
            } else {
                employeeModel.setEmail("");
            }
            if (infoEmployeeSchoolDTO.getCmnd() != null) {
                employeeModel.setCmnd(infoEmployeeSchoolDTO.getCmnd());
            } else {
                employeeModel.setCmnd("");
            }

            if (infoEmployeeSchoolDTO.getCmndDate() != null) {
                String cmndDate = infoEmployeeSchoolDTO.getCmndDate().format(formatter);
                employeeModel.setCmndDate(cmndDate);
            } else {
                employeeModel.setCmndDate("");
            }
            if (infoEmployeeSchoolDTO.getAddress() != null) {
                employeeModel.setAddress(infoEmployeeSchoolDTO.getAddress());
            } else {
                employeeModel.setAddress("");
            }
            if (infoEmployeeSchoolDTO.getPermanentAddress() != null) {
                employeeModel.setPermanentAddress(infoEmployeeSchoolDTO.getPermanentAddress());
            } else {
                employeeModel.setPermanentAddress("");
            }
            if (infoEmployeeSchoolDTO.getMarriedStatus() != null) {
                employeeModel.setMarriedStatus(infoEmployeeSchoolDTO.getMarriedStatus());
            } else {
                employeeModel.setMarriedStatus("");
            }
            if (infoEmployeeSchoolDTO.getNumberChildren() != null) {
                employeeModel.setNumberChildren(infoEmployeeSchoolDTO.getNumberChildren());
            } else {
                employeeModel.setNumberChildren(0);
            }
            if (infoEmployeeSchoolDTO.getEducationLevel() != null) {
                employeeModel.setEducationLevel(infoEmployeeSchoolDTO.getEducationLevel());
            } else {
                employeeModel.setEducationLevel("");
            }
            if (infoEmployeeSchoolDTO.getProfessional() != null) {
                employeeModel.setProfessional(infoEmployeeSchoolDTO.getProfessional());
            } else {
                employeeModel.setProfessional("");
            }
            if (infoEmployeeSchoolDTO.getDepartmentName() != null) {
                employeeModel.setNameDepartment(infoEmployeeSchoolDTO.getDepartmentName());
            } else {
                employeeModel.setNameDepartment("");
            }
            if (infoEmployeeSchoolDTO.getAccountType() != null) {
                String[] array = infoEmployeeSchoolDTO.getAccountType();
                employeeModel.setObjectEmployee(Arrays.toString(array));
            } else {
                employeeModel.setObjectEmployee("");
            }
            if (infoEmployeeSchoolDTO.getNote() != null) {
                employeeModel.setNote(infoEmployeeSchoolDTO.getNote());
            } else {
                employeeModel.setNote("");
            }

            employeeModels.add(employeeModel);
        }
        return employeeModels;
    }

    private boolean getLogin(InfoEmployeeSchool infoEmployeeSchool) {
        boolean checkLogin = false;
        if (infoEmployeeSchool.getEmployee() != null) {
            List<Device> deviceList = infoEmployeeSchool.getEmployee().getMaUser().getDeviceList();
            long count = deviceList.stream().filter(x -> x.isLogin()).count();
            checkLogin = count > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE;
        }
        return checkLogin;
    }

    private String getUsername(InfoEmployeeSchool infoEmployeeSchool) {
        String username = "";
        if (infoEmployeeSchool.getEmployee() != null) {
            username = ConvertData.getUsernameNoExtend(infoEmployeeSchool.getEmployee().getMaUser().getUsername());
        }
        return username;
    }

    private String getPhone(InfoEmployeeSchool infoEmployeeSchool) {
        return infoEmployeeSchool.getEmployee() != null ? infoEmployeeSchool.getEmployee().getMaUser().getPhone() : "";
    }

    private String getPassword(InfoEmployeeSchool infoEmployeeSchool) {
        return infoEmployeeSchool.getEmployee() == null ? "" : infoEmployeeSchool.getEmployee().getMaUser().getPasswordShow();
    }

    private void saveActive(Long id, boolean status) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        infoEmployeeSchool.setActivated(status);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }

    private void saveActiveSMS(Long id, boolean status) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        infoEmployeeSchool.setSmsReceive(status);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }

    private void saveActiveSendSMS(Long id, boolean status) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        infoEmployeeSchool.setSmsSend(status);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }


}
