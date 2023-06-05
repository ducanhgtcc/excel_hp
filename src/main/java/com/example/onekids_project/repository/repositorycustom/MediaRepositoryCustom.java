package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.mobile.plus.request.video.SearchVideoPlusRequest;

import java.util.List;

public interface MediaRepositoryCustom {

    List<Media> findVideoSchool(Long idSchool);
}
