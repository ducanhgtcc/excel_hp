package com.example.onekids_project.firebase.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-31 15:25
 * id: idKid, idClass, idSchool
 * @author lavanviet
 */

@Getter
@Setter
public class TokenModel {
    private Long id;

    private List<String> tokenList;
}
