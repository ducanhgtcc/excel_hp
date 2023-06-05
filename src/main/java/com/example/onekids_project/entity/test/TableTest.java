package com.example.onekids_project.entity.test;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * date 2021-06-09 08:53
 *
 * @author lavanviet
 */
@Getter
@Setter
@Entity
@Table(name = "table_test")
public class TableTest extends BaseEntity<String> {
    @Column(nullable = false)
    private String fullName;

    private int age;

    private String address;

    private String phone;

    private LocalDate birthday;
}
