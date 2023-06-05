package com.example.onekids_project.entity.parent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.FnBank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * date 2021-02-22 13:34
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "wallet_parent_history")
public class WalletParentHistory extends BaseEntity<String> {
    //in,out
    @Column(nullable = false)
    private String category;

    //school, parent
    private String type;

    private double money;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    //phụ huynh: nạp tiền: cần được nhà trường xác nhận
    //nhà trường: - nạp tiền: true, rút tiền: chờ phụ huynh xác nhận
    private boolean confirm;

    private Long idConfirm;

    private LocalDateTime timeConfirm;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000)
    private String picture;

    @Column(length = 1000)
    private String pictureLocal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_kid")
    private Kids kids;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_bank")
    private FnBank fnBank;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_wallet_parent", nullable = false)
    private WalletParent walletParent;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_order_history")
    private OrderKidsHistory orderKidsHistory;

}
