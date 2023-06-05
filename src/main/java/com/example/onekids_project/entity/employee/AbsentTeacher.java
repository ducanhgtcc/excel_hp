package com.example.onekids_project.entity.employee;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
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
@Table(name = "ma_absent_teacher")
public class AbsentTeacher extends BaseEntity<String> {

    //ngày bắt đầu
    @Column(nullable = false)
    private LocalDate fromDate;

    //ngày kết thúc
    @Column(nullable = false)
    private LocalDate toDate;

    //nội dung xin nghỉ
    @Column(length = 5000, nullable = false)
    private String content;

    //giáo viên đã đọc phản hồi hay chưa
    //true: đã đọc
    private boolean teacherRead = AppConstant.APP_TRUE;

    //trạng thái xác nhận
    //true là đã xác nhận
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean confirmStatus;

    //người xác nhận
    private Long idSchoolConfirm;

    //ngày xác nhận, lấy từ hệ thống
    private LocalDateTime confirmDate;

    //khi nhà trường click xác nhận thì mới lưu nội dung này
    //CONTENT_CONFIRM_SCHOOL
    private String confirmContent;

    //nhà trường đọc, true là nhà trường đã đọc
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolRead;

//    @Column(nullable = false, columnDefinition = "varchar(255) default 'Nhà trường đã xác nhận đơn xin nghỉ. Trân trọng!'")
//    private String defaultContentSchool;

    //nội dung nhà trường phải hồi
    private String schoolReply;

    //id người phản hồi
    private Long idSchoolReply;

    //thời gian nhà trường phản hồi, lấy từ hệ thống
    private LocalDateTime schoolTimeReply;

    //true: nhà trường đã thu hồi phản hồi
    @Column(columnDefinition = "bit default 0")
    private boolean schoolReplyDel;

    //true là nhà trường đã sửa phản hồi, false là chưa sửa
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolModifyStatus;

    @Column(nullable = false)
    private Long idSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "absentTeacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AbsentTeacherAttackFile> absentTeacherAttackFileList;

    @JsonManagedReference
    @OneToMany(mappedBy = "absentTeacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AbsentDateTeacher> absentDateTeacherList;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;


}
