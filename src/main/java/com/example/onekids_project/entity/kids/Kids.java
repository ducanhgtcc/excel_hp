package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExKidsSubject;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.entity.school.AppIconParentAdd;
import com.example.onekids_project.entity.school.KidsSchoolDate;
import com.example.onekids_project.entity.tuitionexcel.*;
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
@Table(name = "ma_kids")
public class Kids extends BaseEntity<String> {

    @Column(length = 45, unique = true, nullable = false)
    private String kidCode;

    @Column(nullable = false)
    private String representation;

    @Column(length = 500)
    private String avatarKid;

    @Column(length = 500)
    private String avatarKidLocal;

    @Column(length = 45)
    private String firstName;

    @Column(length = 45)
    private String lastName;

    @Column(length = 60, nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column(length = 45)
    private String nickName;

    @Column(length = 45, nullable = false)
    private String gender;

    @Column(length = 1000)
    private String address;

    //địa chỉ thường trú
    private String permanentAddress;

    //dân tộc
    private String ethnic;

    @Column(length = 1000)
    private String note;

    @Column(length = 45)
    private String kidStatus;

    @Column(nullable = false)
    private LocalDate dateStart;

    //ngày bảo lưu
    private LocalDate dateRetain;

    //    ngày nghỉ học
    private LocalDate dateLeave;

    @Column(columnDefinition = "bit default true", nullable = false)
    private boolean isActivated = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean historyView = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean smsSend = AppConstant.APP_FALSE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean smsReceive = AppConstant.APP_TRUE;

    private String listIdCam;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int schoolCount;

    //start infor of parents
    private String fatherName;

    private LocalDate fatherBirthday;

    @Column(length = 15)
    private String fatherPhone;

    @Column(length = 50)
    private String fatherEmail;

    private String fatherJob;

    private String motherName;

    private LocalDate motherBirthday;

    @Column(length = 15)
    private String motherPhone;

    @Column(length = 50)
    private String motherEmail;

    private String motherJob;

    @Column(length = 500)
    private String picJsonUrlLocal;

    @Column(length = 500)
    private String picJsonUrl;

    //mã nhận từ sms hoặc khi tạo ban đầu
    @Column(nullable = false, length = 6)
    private String verifyCodeSchool;

    //mã từ hệ thống trong sysInfo
    @Column(nullable = false, length = 6)
    private String verifyCodeAdmin;

    /**
     * nếu outDate khác null thì có 2 TH
     * - nếu groupOutKids!=null: học sinh được chọn cho ra trường
     * - nếu groupOutKids==null: học sinh thực hiện thao tác xóa bằng tay: khi xóa ghi nhận groupOutKids=bằng ngày thực hiện thao tác xóa
     */
    private LocalDate outDate;

    //số định danh cá nhân
    private String identificationNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MaKidPics> maKidPicsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsHistory> kidsHistoryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsChangeTemp> kidsChangeTempList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsLog> kidsLogList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsVaccinate> kidsVaccinateList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExKidsSubject> exKidsSubjectList;

    @JsonManagedReference
    @OneToOne(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private KidsExtraInfo kidsExtraInfo;

    // tuan todo
    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MessageParent> messageParentList;

    // 1 - n kisd-medicine
    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Medicine> medicineList;

    // 1 - n abentLetter
    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AbsentLetter> absentLetterList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AttendanceKids> attendanceKidsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsSchoolDate> kidsSchoolDateList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsClassDate> kidsClassDateList;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Parent parent;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateDate> evaluateDateList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateWeek> evaluateWeekList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateMonth> evaluateMonthList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluatePeriodic> evaluatePeriodicList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AlbumKidsHisotry> albumKidsHisotryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExAlbumKids> exAlbumKidsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsNotifyLeave> kidsNotifyLeaveList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<KidsHeight> kidsHeightList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<KidsWeight> kidsWeightList;

    @JsonBackReference
    @ManyToMany(mappedBy = "kidsList", fetch = FetchType.LAZY)
    private List<KidsGroup> kidsGroupList;

    @JsonManagedReference
    @OneToOne(mappedBy = "kid", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private AppIconParentAdd appIconParentAdd;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<KidsStatusTimeline> kidsStatusTimelineList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FnKidsPackageDefault> fnKidsPackageDefaultList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FnKidsPackage> fnKidsPackageList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FnOrderKids> fnOrderKidsList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group_out_kids")
    private GroupOutKids groupOutKids;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids")
    private List<WalletParentHistory> walletParentHistoryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids")
    private List<KidsTransfer> kidsTransferList;


//    PHẦN THU HỌC PHÍ MỚI

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<TotalKidsArrive> totalKidsArriveList;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT01> orderKidsExcelT01List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT02> orderKidsExcelT02List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT03> orderKidsExcelT03List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT04> orderKidsExcelT04List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT05> orderKidsExcelT05List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT06> orderKidsExcelT06List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT07> orderKidsExcelT07List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT08> orderKidsExcelT08List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT09> orderKidsExcelT09List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT10> orderKidsExcelT10List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT11> orderKidsExcelT11List;

    @JsonManagedReference
    @OneToMany(mappedBy = "kids", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<OrderKidsExcelT12> orderKidsExcelT12List;

}
