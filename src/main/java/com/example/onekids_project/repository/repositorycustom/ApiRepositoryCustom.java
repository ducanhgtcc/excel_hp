package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.Api;

import java.util.List;

public interface ApiRepositoryCustom {
    List<Api> getApiList();
    List<Api> getApiFromTo(int from, int to);
}
