package com.example.onekids_project.entity.employee;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalaryDefault;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.school.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "info_employee_school")
public class InfoEmployeeSchool extends BaseEntity<String> {

    @Column(nullable = false, length = 45)
    private String code;

    //không cho phép null, check lại các field không cho phép null theo giao diện
    @Column(nullable = false, length = 15)
    private String phone;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 500)
    private String avatar;

    @Column(length = 500)
    private String urlLocalAvatar;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(length = 1000)
    private String address;

    @Column(length = 45, nullable = false)
    private String gender;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String cmnd;

    private LocalDate cmndDate;

    private String permanentAddress;//set địa chỉ thường chú

    private String currentAddress;// không dùng

    //dân tộc
    private String ethnic;

    private String marriedStatus;

    private Integer numberChildren;

    private String educationLevel;

    private String professional;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate contractDate;

    private LocalDate endDate;

    private String note;

    //Đang làm, Tạm nghỉ, Nghỉ làm: in class EmployeeConstant
    private String employeeStatus;

    private LocalDate dateRetain;

    private LocalDate dateLeave;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean historyView = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean activated = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean smsSend;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean smsReceive = AppConstant.APP_TRUE;

    //plus or teacher
    @Column(nullable = false)
    private String appType;

    //mã nhận từ sms hoặc khi tạo ban đầu
    @Column(nullable = false, length = 6)
    private String verifyCodeSchool;

    //mã từ hệ thống trong sysInfo
    @Column(nullable = false, length = 6)
    private String verifyCodeAdmin;

    private LocalDate outDate;

    //không cho phép null
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_school", nullable = false)
    private School school;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AbsentTeacher> absentTeacherList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExEmployeeClass> exEmployeeClassList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AttendanceTeacher> attendanceTeacherList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExDepartmentEmployee> departmentEmployeeList;

    @JsonManagedReference
    @OneToOne(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AppIconPlusAdd appIconPlusAdd;

    @JsonManagedReference
    @OneToOne(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private EmployeeNotify employeeNotify;

    @JsonManagedReference
    @OneToOne(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AppIconTeacherAdd appIconTeacherAdd;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_employee_account_type",
            joinColumns = @JoinColumn(name = "id_info_employee"),
            inverseJoinColumns = @JoinColumn(name = "id_account_type")
    )
    @JsonManagedReference
    private List<AccountType> accountTypeList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FnEmployeeSalaryDefault> fnEmployeeSalaryDefaultList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FnEmployeeSalary> fnEmployeeSalaryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FnOrderEmployee> fnOrderEmployeeList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<EmployeeStatusTimeline> employeeStatusTimelineList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AttendanceEmployee> attendanceEmployeeList;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ConfigAttendanceEmployee> configAttendanceEmployeeList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group_out_employee")
    private GroupOutEmployee groupOutEmployee;

    @JsonManagedReference
    @OneToMany(mappedBy = "infoEmployeeSchool", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<InternalNotificationPlus> internalNotificationPlusList;

}
