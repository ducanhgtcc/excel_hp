package com.example.onekids_project.entity.appversion;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "app_version")
public class AppVersion extends BaseEntity<String> {

    @Column(nullable = false)
    private String version;

    //0 là ko thông báo cho người dùng có phiên bản mới, 1 là thông báo
    private boolean versionUpdate;

    //0 là không bắt buộc cập nhật lên phiên bản mới, 1 là bắt buộc
    private boolean compulsory;

    //áp dụng cho teacher, parent, plus
    @Column(nullable = false)
    private String appType;

    //nền tảng android hoặc ios
    @Column(nullable = false)
    private String appOs;

    @Column(nullable = false)
    private String apiUrl;

    @Column(nullable = false)
    private String updateContent="Đã có phiên bản mới. Bạn hãy cập nhật để sử dụng các tính năng mới nhất.";
}
