package com.example.onekids_project.entity.system;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "sys_infor")
public class SysInfor extends BaseEntity<String> {

    @Column(nullable = false)
    private String namePhone1 = "Hỗ trợ Onekids";

    @Column(nullable = false)
    private String phone1 = "01234567689";

    private String namePhone2;

    private String phone2;

    private String namePhone3;

    private String phone3;

    @Column(nullable = false)
    private String urlApiBackend = "https://api.onekids.edu.vn/";

    private int widthAlbum = 236;

    private int widthOther = 512;

    private int mobileSizePage = 20;

    @Column(nullable = false)
    private String localUrl = "C:\\\\xampp\\\\htdocs\\\\upload\\\\";

    @Column(nullable = false)
    private String webUrl = "https://upload.onekids.edu.vn/";

    @Column(nullable = false, length = 500)
    private String urlPictureSystem = "https://upload.onekids.edu.vn/sysfiles/avatar/system_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureSchool = "https://upload.onekids.edu.vn/sysfiles/avatar/school_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureEmployee = "https://upload.onekids.edu.vn/sysfiles/avatar/teacher_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureParent = "https://upload.onekids.edu.vn/sysfiles/avatar/parents_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureKids = "https://upload.onekids.edu.vn/sysfiles/avatar/kids_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureSystemLocal = "C:\\xampp\\htdocs\\upload\\sysfiles\\avatar\\system_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureSchoolLocal = "C:\\xampp\\htdocs\\upload\\sysfiles\\avatar\\school_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureEmployeeLocal = "C:\\xampp\\htdocs\\upload\\sysfiles\\avatar\\teacher_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureParentLocal = "C:\\xampp\\htdocs\\upload\\sysfiles\\avatar\\parents_avatar.png";

    @Column(nullable = false, length = 500)
    private String urlPictureKidsLocal = "C:\\xampp\\htdocs\\upload\\sysfiles\\avatar\\kids_avatar.png";

    //Link thông tin điều khoản
    @Column(nullable = false, length = 500)
    private String policyLink = "Nhập Link thông tin điều khoản";

    //link hướng dẫn sử dụng dành cho parent
    @Column(nullable = false, length = 500)
    private String guideLink = "Nhập link hướng dẫn sử dụng dành cho phụ huynh";

    //link hướng dẫn sử dụng dành cho teacher
    @Column(nullable = false, length = 500)
    private String guideTeacherLink = "Nhập link hướng dẫn sử dụng dành cho giáo viên";

    //link hướng dẫn sử dụng dành cho plus
    @Column(nullable = false, length = 500)
    private String guidePlusLink = "Nhập link hướng dẫn sử dụng dành cho plus";

    //link hỗ trợ trực tuyến
    @Column(nullable = false, length = 500)
    private String supportLink = "Nhập link hỗ trợ trực tuyến";

    //link đăng ký dịch vụ
    @Column(nullable = false, length = 500)
    private String registerService = "Nhập link đăng ký dịch vụ";

    @Column(nullable = false, length = 500)
    private String videoLink = "Nhập Link kênh Video OneKids";

    @Column(nullable = false, columnDefinition = "time default '08:00:00'")
    private LocalTime timeSend = LocalTime.of(8, 0, 0);

    //mã xác thực cho các tài khoản khi thao tác xử lý trùng tài khoản, chỉ bao gồm các ký tự A-Z
    //độ dài là 6 ký tự
    @Column(length = 6, nullable = false)
    private String verificationCode;

    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean schoolTrialStatus = true;

    @Column(nullable = false, columnDefinition = "int default 10")
    private int schoolTrailNumber = 10;

    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean accountTrialStatus = true;

    @Column(nullable = false, columnDefinition = "int default 10")
    private int accountTrailNumber = 10;

    @Column(nullable = false, columnDefinition = "int default 2")
    private int limitDevicePlusNumber = 2;

    @Column(nullable = false, columnDefinition = "int default 2")
    private int limitDeviceTeacherNumber = 2;

    @Column(nullable = false, columnDefinition = "int default 2")
    private int limitDeviceParentNumber = 2;

    private String oneCamePicture;

}
