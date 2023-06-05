package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-06-01 13:54
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_package_group")
public class FnPackageGroup extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;

    private String note;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @OneToMany(mappedBy = "fnPackageGroup", fetch = FetchType.LAZY)
    List<FnPackage> fnPackageList;

}
