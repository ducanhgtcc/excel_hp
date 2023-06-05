package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "kids_transfer")
public class KidsTransfer extends BaseEntity<String> {
    //đưa học sinh
    private boolean inStatus;
    //đón học sinh
    private boolean outStatus;
    @Column(nullable = false)
    private String fullName;
    private String phone;
    private String identify;
    private String relationship;
    @Column(length = 1000)
    private String note;


    //thông tin ảnh
    private String name;
    @Column(length = 1000)
    private String url;
    @Column(length = 1000)
    private String urlLocal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_kid", nullable = false)
    private Kids kids;
}
