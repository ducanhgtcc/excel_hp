package com.example.onekids_project.entity.parent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * date 2021-02-22 09:47
 * 
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "wallet_parent")
public class WalletParent extends BaseEntity<String> {
    //mã trường-6 chữ số
    @Column(nullable = false, unique = true)
    private String code;

    private double moneyIn;

    private double moneyOut;

    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_parent", nullable = false)
    private Parent parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "walletParent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<WalletParentHistory> walletParentHistoryList;

}
