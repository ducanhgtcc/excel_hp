package com.example.onekids_project.entity.finance.feesextend;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-09-21 14:00
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_package_kids_extend")
public class FnPackageKidsExtend extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String note;

    //true: tạo tự động kèm theo cho khoản mặc định
    private boolean active;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @OneToMany(mappedBy = "fnPackageKidsExtend", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<FnMoneyKidsExtend> fnMoneyKidsExtendList;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "id_package_kids", nullable = false)
    private FnKidsPackage fnKidsPackage;

}
