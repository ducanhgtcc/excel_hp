package com.example.onekids_project.entity.finance.CashInternal;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * bảng chứa các đối tượng
 */
@Getter
@Setter
@Entity
@Table(name = "fn_people_type")
public class FnPeopleType extends BaseEntity<String> {

    @Column(nullable = false)
    private String name;

    private String description;

    private String gender;

    private String birthday;

    private String indentify;

    private String email;

    private String phone;

    private String address;

    // internal - nhà trường, external - khác (lấy ở financeConstant)
    @Column(nullable = false)
    private String type;

    private boolean defaultStatus;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @OneToMany(mappedBy = "fnPeopleTypeInternal", fetch = FetchType.LAZY)
    private List<FnCashInternal> fnCashInternalList;

    @JsonBackReference
    @OneToMany(mappedBy = "fnPeopleTypeOther", fetch = FetchType.LAZY)
    private List<FnCashInternal> fnCashOtherList;

}
