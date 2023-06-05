package com.example.onekids_project.entity.user;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_role")
public class Role extends BaseEntity<String> {

    @Column(nullable = false)
    private String roleName;

    @Column(length = 3000)
    private String description;

    //AppTypeConstant, dùng để tạo các loại role cho từng người dùng
    @Column(nullable = false)
    private String type;

    //true là vai trò mặc định, true là ko cho phép xóa, false là được xóa
    private boolean defaultStatus;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToMany(mappedBy = "roleList", fetch = FetchType.LAZY)
    private List<MaUser> maUsersList;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "ex_role_api",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_api")
    )
    @JsonManagedReference
    private List<Api> apiList;
}
