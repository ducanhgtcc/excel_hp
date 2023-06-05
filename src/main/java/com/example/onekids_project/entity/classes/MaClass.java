package com.example.onekids_project.entity.classes;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.Subject;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.onecam.OneCamSetting;
import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.entity.school.Grade;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ma_class")
public class MaClass extends BaseEntity<String> {

    @Column(nullable = false, unique = true)
    private String classCode;

    @Column(nullable = false)
    private String className;

    @Column(length = 3000)
    private String classDescription;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean morningSaturday=AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean afternoonSaturday=AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean eveningSaturday=AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 0")
    private boolean sunday;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_grade", nullable = false)
    private Grade grade;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExEmployeeClass> exEmployeeClassList;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_class_cam",
            joinColumns = @JoinColumn(name = "id_class"),
            inverseJoinColumns = @JoinColumn(name = "id_cam")
    )
    @JsonManagedReference
    private List<Camera> cameraList;

    // n-n tuan todo
    @JsonManagedReference
    @ManyToMany(mappedBy = "maClassList", fetch = FetchType.LAZY)
    List<Media> mediaList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Album> albumList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Kids> kidsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ClassMenu> classMenuList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ManuFile> manuFileList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AttendanceKids> attendanceKidsList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ClassSchedule> classScheduleList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ScheduleFile> scheduleFileList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<KidsClassDate> kidsClassDateList;

    @JsonManagedReference
    @OneToMany(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<DayOffClass> dayOffClassList;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_class_subject",
            joinColumns = @JoinColumn(name = "id_class"),
            inverseJoinColumns = @JoinColumn(name = "id_subject")
    )
    @JsonManagedReference
    private List<Subject> subjectList;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_class_package",
            joinColumns = @JoinColumn(name = "id_class"),
            inverseJoinColumns = @JoinColumn(name = "id_package")
    )
    @JsonManagedReference
    private Set<FnPackage> fnPackageSet;

    @JsonManagedReference
    @OneToOne(mappedBy = "maClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private OneCamSetting oneCamSetting;
}
