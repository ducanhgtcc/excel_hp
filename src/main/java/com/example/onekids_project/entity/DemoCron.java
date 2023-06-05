package com.example.onekids_project.entity;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "demo_cron")
public class DemoCron extends BaseEntity<String> {
    private String name;

    private String cronjob;
}
