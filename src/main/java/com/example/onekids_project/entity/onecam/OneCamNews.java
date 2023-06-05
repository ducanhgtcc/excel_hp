package com.example.onekids_project.entity.onecam;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table
public class OneCamNews extends BaseEntity<String> {
    private int oneCamNumber = 9;

    private int oneKidsNumber = 0;

    private int schoolNumber = 0;

    private boolean extendLinkStatus = true;

    private String extendLink;

    @Column(nullable = false)
    private Long idSchool;
}
