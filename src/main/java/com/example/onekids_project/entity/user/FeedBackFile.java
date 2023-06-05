package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "feedback_file")
public class FeedBackFile extends BaseEntity<String> {
    @Column(length = 1500)
    private String attachPicture;

    @Column(length = 1500)
    private String urlLocalPicture;

    @Column(length = 1500)
    private String name;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_feedback", nullable = false)
    private FeedBack feedBack;
}
