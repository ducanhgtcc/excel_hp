package com.example.onekids_project.entity.parent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Medicine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "medicine_attach_file")
public class MedicineAttachFile extends BaseEntity<String> {
    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 500)
    private String urlLocal;

    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_medicine", nullable = false)
    private Medicine medicine;
}
