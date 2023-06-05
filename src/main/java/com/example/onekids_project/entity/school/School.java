package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnSalary;
import com.example.onekids_project.entity.onecam.OneCamConfig;
import com.example.onekids_project.entity.system.SysConfig;
import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.entity.usermaster.SchoolMaster;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_school")
public class School extends BaseEntity<String> {

    @Column(nullable = false, unique = true)
    private String schoolCode;

    private String schoolAvatar;

    private String schoolLocalAvatar;

    @Column(nullable = false)
    private String schoolName;

    @Column(columnDefinition = "TEXT")
    private String schoolDescription;

    @Column(nullable = false)
    private String schoolAddress;

    @Column(length = 15, nullable = false)
    private String schoolPhone;

    @Column(length = 50)
    private String schoolEmail;

    private String schoolWebsite;

    private String contactName1;

    private String contactDescription1;

    @Column(length = 15)
    private String contactPhone1;

    @Column(length = 50)
    private String contactEmail1;

    private String contactName2;

    private String contactDescription2;

    @Column(length = 15)
    private String contactPhone2;

    @Column(length = 50)
    private String contactEmail2;

    private String contactName3;

    private String contactDescription3;

    @Column(length = 15)
    private String contactPhone3;

    @Column(length = 50)
    private String contactEmail3;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean schoolActive = AppConstant.APP_TRUE;

    private long smsBudget;

    private LocalDateTime smsBudgetDate;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean smsActiveMore;

    private long smsUsed;

    private long smsTotal;

    private String idsmsBrand;

    private String namePhone1;

    private String namePhone2;

    private String namePhone3;

    private LocalDate demoStart;

    private LocalDate demoEnd;

    private LocalDate dateContractStart;

    private LocalDate dateContractEnd;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private boolean limitTime;

    private boolean limitDevice;

    private int numberDevice;

    //true->tài khoản đang dùng thử
    private boolean trialStatus;

    @Column(columnDefinition = "varchar(255) default 'D'", nullable = false)
    private String groupType = "D";

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agent", nullable = false)
    private Agent agent;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_brand", nullable = true)
    private Brand brand;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Grade> gradeList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Department> departmentList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<DvrCamera> dvrCameraList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SchoolSms> schoolSmsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AppIconTeacher> appIconTeacherList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AppIconParent> appIconParentList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AppIconPlus> appIconPlusList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsSchoolDate> kidsSchoolDateList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<InfoEmployeeSchool> infoEmployeeSchoolList;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private SchoolConfig schoolConfig;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private SysConfig sysConfig;

    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SchoolMaster> schoolMasterList;

    @JsonBackReference
    @ManyToMany(mappedBy = "schoolList", fetch = FetchType.LAZY)
    private List<MaAdmin> maAdminList;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private ConfigPlus configPlus;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FnSalary> fnSalaryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FnBank> fnBankList;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FnCashBook> fnCashBookList;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private SchoolInfo schoolInfo;

    @JsonManagedReference
    @OneToMany(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ConfigAttendanceEmployeeSchool> configAttendanceEmployeeSchoolList;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private ConfigNotifyPlus configNotifyPlus;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private ConfigNotifyTeacher configNotifyTeacher;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private ConfigNotifyParent configNotifyParent;


    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private OneCamConfig oneCamConfig;

    @JsonManagedReference
    @OneToOne(mappedBy = "school", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private CycleMoney cycleMoney;
}
