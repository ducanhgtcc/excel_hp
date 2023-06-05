package com.example.onekids_project.entity.user;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.onecam.DeviceCam;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.tuitionexcel.*;
import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.entity.usermaster.SchoolMaster;
import com.example.onekids_project.entity.usermaster.SupperAdmin;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ma_user")
@NoArgsConstructor
public class MaUser extends BaseEntity<String> {
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String gender;

    //plus *NT, teacher *GV. parent *PH, trùng chưa xử lý đuôi A1,2,3. trùng đã xử lý đuôi B1,2,3
    @Column(nullable = false, unique = true)
    @Size(min = 6)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Size(min = 6)
    private String passwordShow;

    @Column(nullable = false, length = 15)
    private String phone;

    //lấy ở class ApptypeConstant
    @Column(nullable = false)
    private String appType;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean activated = AppConstant.APP_TRUE;

    private String activatedKey;

    private String resetPasswordKey;

    private LocalDateTime activatedDate;

    private LocalDateTime resetPasswordDate;

    private Integer deviceNumber;

    private Integer deviceLimit;

    private Integer deviceLoginedNumber;

    private LocalDate fromDate;

    private LocalDate toDate;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean unlimitTime = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean demoStatus = AppConstant.APP_TRUE;

    private LocalDate fromDemoDate;

    private LocalDate toDemoDate;

    /**
     * - với tài khoản có del_active=true
     * nếu ==null thì tài khoản còn tồn tại các thực thể con(phụ huynh->kids, employee->inforEmployeeSchool)
     * nếu !=null: tài khoản đã ko còn tồn tại thực thể con nào từ ngày ghi nhận
     * - khi khôi phục tài khoản thì set giá trị này về null
     */
    private LocalDate startDateDelete;

    /**
     * có 3 loại bị xóa hoàn toàn:
     * - manual: xóa bằng tay
     * - auto: xóa tự động
     * - handle: xóa khi xử lý trùng
     */
    private String typeDelete;

    //thời gian xóa tài khoản(del_active=false)
    private LocalDateTime timeDelete;

    //thời gian xóa hoàn toàn tài khoản(del_active=false and username+1)
    private LocalDateTime timeDeleteComplete;

    //true->tài khoản đang dùng thử
    private boolean trialStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Device> deviceList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<RemindNotify> remindNotifyList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AppSendHistory> appSendHistoryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ReceiversHistory> receiversHistoryList;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    @JsonManagedReference
    private List<Role> roleList;

    @JsonBackReference
    @OneToOne(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Employee employee;

    @JsonBackReference
    @OneToOne(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Parent parent;

    @JsonBackReference
    @OneToOne(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private MaAdmin maAdmin;

    @JsonBackReference
    @OneToOne(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private SchoolMaster schoolMaster;

    @JsonBackReference
    @OneToOne(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AgentMaster agentMaster;

    @JsonBackReference
    @OneToOne(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private SupperAdmin supperAdmin;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<DeviceCam> deviceCamList;


//    PHẦN THU HỌC PHÍ MỚI

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<HistoryOrderKids> historyOrderKidsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<TotalKidsArrive> totalKidsArriveList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT01> orderKidsExcelT01List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT02> orderKidsExcelT02List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT03> orderKidsExcelT03List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT04> orderKidsExcelT04List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT05> orderKidsExcelT05List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT06> orderKidsExcelT06List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT07> orderKidsExcelT07List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT08> orderKidsExcelT08List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT09> orderKidsExcelT09List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT10> orderKidsExcelT10List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT11> orderKidsExcelT11List;

    @JsonManagedReference
    @OneToMany(mappedBy = "maUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT12> orderKidsExcelT12List;

}
