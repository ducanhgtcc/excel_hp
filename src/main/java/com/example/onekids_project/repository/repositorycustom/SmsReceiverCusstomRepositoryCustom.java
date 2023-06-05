package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.mobile.plus.request.album.SearchAlbumPlusRequest;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailSmsRequest;
import com.example.onekids_project.mobile.teacher.request.album.AlbumTeacherRequest;
import com.example.onekids_project.request.album.SearchAlbumNewRequest;
import com.example.onekids_project.request.album.SearchAlbumRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsReceiverCusstomRepositoryCustom {

    List<SmsReceiversCustom> searchSmsCustomnew(Long idSchool, Long idSmsSendCustom, DetailSmsRequest request);
}
