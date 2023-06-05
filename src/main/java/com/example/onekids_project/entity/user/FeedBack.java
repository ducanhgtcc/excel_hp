package com.example.onekids_project.entity.user;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_feedback")
public class FeedBack extends BaseEntity<String> {

    @Column(nullable = false)
    private String feedbackTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedbackContent;

    //loại góp ý: trong FeedBackConstant
    @Column(nullable = false)
    private String accountType;

    //true là ẩn danh, false là không ẩn danh
    @Column(nullable = false, columnDefinition = "bit default false")
    private boolean hiddenStatus;

    //false là người tạo góp ý(phụ huynh, giáo viên) chưa đọc
    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean parentUnread = AppConstant.APP_TRUE;

    //false là nhà trường chưa đọc
    @Column(nullable = false, columnDefinition = "bit default false")
    private boolean schoolUnread;

    //false là nhà trường chưa xác nhận
    @Column(nullable = false, columnDefinition = "bit default false")
    private boolean schoolConfirmStatus;

    private LocalDateTime confirmDate;

    private Long idSchoolConfirm;

    //khi click vào xác nhận thì sẽ lưu nội dung này, click thì không lưu.
    private String confirmContent;

    private Long idSchoolReply;

    //nội dung nhà trường phản hồi
    @Column(length = 3000)
    private String schoolReply;

    private LocalDateTime schoolTimeReply;

    private boolean schoolReplyDel;

    @Column(nullable = false, columnDefinition = "bit default false")
    private boolean schoolModifyStatus;

    //phụ huynh tạo thì có idKid
    private Long idKid;

    //giáo viên tạo thì có idClass
    private Long idClass;

    @Column(nullable = false)
    private Long idSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "feedBack", cascade = {CascadeType.PERSIST, CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<FeedBackFile> feedBackFileList;

}
