package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "kids_change_temp")
public class KidsChangeTemp extends BaseEntityId<String> {

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    private Integer appMessage;

    @Column(nullable = false)
    private Integer dateCount;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kid;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idClass;
}
