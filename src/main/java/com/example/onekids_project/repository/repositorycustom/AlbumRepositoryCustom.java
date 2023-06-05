package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.mobile.plus.request.album.SearchAlbumPlusRequest;
import com.example.onekids_project.mobile.teacher.request.album.AlbumTeacherRequest;
import com.example.onekids_project.request.album.SearchAlbumNewRequest;
import com.example.onekids_project.request.album.SearchAlbumRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AlbumRepositoryCustom {

    Long countAllAlbum(Long idSchool,SearchAlbumRequest searchAlbumRequest);

    long getCountMessage(Long idSchool, LocalDateTime localDateTime);

    Long getCountMessageforClass(Long idClass, LocalDateTime localDateTime);

    List<Album> findAllbumClassForTeacher(Long idSchool, Long idClass, AlbumTeacherRequest albumTeacherRequest);

    List<Album> findAllAlbumForTeacherx(Long idSchool,Long idClass, AlbumTeacherRequest albumTeacherRequest);

    List<Album> findAllbumSchoolForTeachers(Long idSchool, Long idClass, AlbumTeacherRequest albumTeacherRequest);

    List<Album> findAllAlbumForPlus(Long idSchool, SearchAlbumPlusRequest searchAlbumPlusRequest);

    List<Album> findalBumNew(Long idSchool, SearchAlbumNewRequest request);

    long countSearchAlbum(Long idSchool, SearchAlbumNewRequest request);

    List<Album> findAllAlbumClassChart(Long idClass, String albumType, LocalDate date);

    List<Album> findAllAlbumDateInChart(Long idSchool, Long idClass, Long idGrade, String albumType, List<LocalDate> dates);

    List<Album> findAllAlbumGradeChart(Long idGrade, String albumType, LocalDate date);
}
