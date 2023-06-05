package com.example.onekids_project.entity.school;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-02-23 13:35
 * 
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_bank")
public class FnBank extends BaseEntity<String> {
    @Column(nullable = false)
    private String  fullName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String bankName;

    private String branch;

    @Column(columnDefinition = "TEXT")
    private String description;

    //chọn làm tài khoản chính, mỗi trường chỉ có 1 tài khoản chính
    private boolean checked;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false)
    private School school;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnBank", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<WalletParentHistory> walletParentHistoryList;
}
