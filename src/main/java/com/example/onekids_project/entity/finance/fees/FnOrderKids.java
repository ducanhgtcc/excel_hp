package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * date 2021-02-22 14:04
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "fn_order_kids")
public class FnOrderKids extends BaseEntity<String> {
    //mã hóa đơn: mã K032021-1
    @Column(nullable = false, unique = true)
    private String code;

    private int year;

    private int month;

    private boolean locked;

    private Long idLocked;

    private LocalDateTime timeLock;

    @Column(columnDefinition = "TEXT")
    private String description;

    //true: cho phụ huynh xem hóa đơn, false là không cho xem
    private boolean view;

    private Long idView;

    private LocalDateTime timeView;

    //true là phụ huynh đã đọc
    private boolean parentRead;

    //thời gian thanh toán
    @Column(nullable = false)
    private LocalDateTime timePayment = LocalDateTime.now();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;

    @JsonManagedReference
    @OneToMany(mappedBy = "fnOrderKids", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderKidsHistory> orderKidsHistoryList;

}
