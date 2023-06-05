package com.example.onekids_project.entity.finance.fees;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-03-02 13:08
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "ex_order_history_kids_package")
public class ExOrderHistoryKidsPackage extends BaseEntity<String> {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_history", nullable = false)
    private OrderKidsHistory orderKidsHistory;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids_package", nullable = false)
    private FnKidsPackage fnKidsPackage;

    private double money;
}
