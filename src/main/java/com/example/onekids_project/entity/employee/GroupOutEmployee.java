package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-07-12 10:10 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Entity
@Table(name = "group_out_employee")
public class GroupOutEmployee extends BaseEntity<String> {

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String note;

    //Nhóm tạo tự động: không sửa tên, xóa được
    private boolean defaultStatus;

    @Column(nullable = false)
    private Long idSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "groupOutEmployee", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<InfoEmployeeSchool> infoEmployeeSchoolList;


}
