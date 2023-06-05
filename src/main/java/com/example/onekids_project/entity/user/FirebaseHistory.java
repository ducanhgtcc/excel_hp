package com.example.onekids_project.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "firebase_history_fail")
public class FirebaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageId;

    @Column(nullable = false)
    private LocalDateTime dateTime = LocalDateTime.now();

    // số lần gửi, tối đa 15 lần, vượt mức xóa trong DB
    private int numberSend = 1;

    @Column(nullable = false, length = 1000)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

//    AppTypeConstant
    @Column(nullable = false)
    private String AppType;

    @Column(nullable = false)
    private String router;

    @Column(length = 1000, nullable = false)
    private String tokenFirebase;

}
