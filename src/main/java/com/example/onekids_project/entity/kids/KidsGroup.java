package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_kids_group")
public class KidsGroup extends BaseEntity<String> {

    @Column(nullable = false)
    private String groupName;

    private String description;

    @Column(nullable = false)
    private Long idSchool;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)

    @JoinTable(name = "ex_kids_group",
            joinColumns = @JoinColumn(name = "id_kids_group"),
            inverseJoinColumns = @JoinColumn(name = "id_kids")
    )
    @JsonManagedReference
    private List<Kids> kidsList;
}
