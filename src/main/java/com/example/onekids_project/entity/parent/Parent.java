package com.example.onekids_project.entity.parent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_parent")
public class Parent extends BaseEntity<String> {

    @Column(length = 45, nullable = false)
    private String code;

    private String email;

    @Column(length = 500)
    private String avatar;

    @Column(length = 500)
    private String avatarLocal;

    private LocalDate birthday;

    @Column(nullable = false)
    private Long idKidLogin;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Kids> kidsList;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "id_ma_user", nullable = false, unique = true)
    private MaUser maUser;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<WalletParent> walletParentList;
}
