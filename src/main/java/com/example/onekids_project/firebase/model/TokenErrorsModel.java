package com.example.onekids_project.firebase.model;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-31 15:16
 *
 * @author lavanviet
 */
@Getter
@Setter
public class TokenErrorsModel {
    private String messageId;

    private String title;

    private String body;

    private String AppType;

    private String router;

    private String tokenFirebase;
}
