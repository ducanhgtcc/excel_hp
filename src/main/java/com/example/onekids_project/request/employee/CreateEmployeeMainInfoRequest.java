package com.example.onekids_project.request.employee;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.dto.ExEmployeeClassDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateEmployeeMainInfoRequest {

    private List<String> accountType;

    private List<Long> idAccountTypeList;//id đối tượng

    private String phone;//set phone

    private String firstName;

    private String lastName;

    private String fullName;// set full name

    private String avatar;

    private LocalDate birthday;// set ngày sinh

    private String address;// set địa chỉ hiện tại

    private String gender;//set giới tính

    private String email;

    private String cmnd; // set số định danh

    private LocalDate cmndDate;

    private String permanentAddress;// địa chỉ thường chú

    private String currentAddress;// không dùng

    private String marriedStatus;//set Độc thân, Đã kết hôn

    private Integer numberChildren;// số con

    private String educationLevel;// Trình độ

    private String professional;// chuyên môn: không dùng

    private LocalDate startDate;//ngày vào*

    private LocalDate contractDate;//Ngày ký hợp đồng

    private LocalDate endDate;//Ngày hết hạn

    private String note;

    //set Đang làm
    private String employeeStatus;

    private LocalDate dateRetain; //không cần

    private LocalDate dateLeave; //không cần

    private boolean historyView = AppConstant.APP_TRUE;//không cần

    private Boolean isActivatedTeacher;//không cần

    private Boolean isActivatedPlus;//không cần

    private Boolean smsSend;//không cần

    private Boolean smsReceive;//không cần

//    set teacher
    private String appType;

    private Long idEmployee;//không cần

    private String ethnic;

//    set idSchool
    private Long idSchool;
}
