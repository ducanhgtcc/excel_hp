package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * thông tin bảng này chỉ cập nhật khi có sự thay đổi trong schoolConfig
 */
@Getter
@Setter
@Entity
@Table(name = "attendance_config")
public class AttendanceConfig extends BaseEntity<String> {
    @Column(nullable = false)
    private Long idSchool;

    private boolean morningSaturday;

    private boolean afternoonSaturday;

    private boolean eveningSaturday;

    private boolean sunday;

    //true là học sáng
    private boolean morningAttendanceArrive;

    //true là học chiều
    private boolean afternoonAttendanceArrive;

    //true là học tối
    private boolean eveningAttendanceArrive;

    //true là ăn sáng
    private boolean morningEat;

    //true là ăn phụ sáng
    private boolean secondMorningEat;

    //true là ăn trưa
    private boolean lunchEat;

    //true là ăn chiều
    private boolean afternoonEat;

    //true là ăn phụ chiều
    private boolean secondAfternoonEat;

    //true là ăn tối
    private boolean eveningEat;

    @JsonManagedReference
    @OneToMany(mappedBy = "attendanceConfig", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AttendanceKids> attendanceKidsList;
}
