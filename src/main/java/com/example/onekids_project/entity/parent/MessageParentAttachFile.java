package com.example.onekids_project.entity.parent;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "message_parent_attach_file")
public class MessageParentAttachFile extends BaseEntity<String> {
    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 500)
    private String urlLocal;

    private String name;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_message_parent", nullable = false)
    private MessageParent messageParent;
}
