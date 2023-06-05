package com.example.onekids_project.entity.system;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * date 2021-10-15 14:42
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "menu_support")
public class MenuSupport extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String link;

    private boolean plusStatus;

    private boolean teacherStatus;

    private boolean parentStatus;

}
