package com.example.onekids_project.entity.onecam;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
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
@Table
public class OneCamConfig extends BaseEntity<String> {
    private boolean plusStatus;

    private int plusNumber;

    private boolean teacherStatus;

    private int teacherNumber;

    private boolean parentStatus;

    private int parentNumber;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;
}
