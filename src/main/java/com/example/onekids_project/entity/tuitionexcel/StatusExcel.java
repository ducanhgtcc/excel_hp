package com.example.onekids_project.entity.tuitionexcel;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status_excel")
public class StatusExcel extends BaseEntity<String> {
    private String idGuid;
    private String status;
    private String fileName;

}
