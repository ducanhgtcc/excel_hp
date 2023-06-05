package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.mobile.plus.request.album.SearchAlbumPlusRequest;
import com.example.onekids_project.mobile.teacher.request.album.AlbumTeacherRequest;
import com.example.onekids_project.repository.repositorycustom.AlbumRepositoryCustom;
import com.example.onekids_project.request.album.SearchAlbumNewRequest;
import com.example.onekids_project.request.album.SearchAlbumRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumRepositoryImpl extends BaseRepositoryimpl<Album> implements AlbumRepositoryCustom {

    @Override
    public Long countAllAlbum(Long idSchool, SearchAlbumRequest searchAlbumRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchAlbumRequest != null) {
            if (StringUtils.isNotBlank(searchAlbumRequest.getTimeAlbumSearch())) {
                queryStr.append("and created_date like :createdDate");
                mapParams.put("createdDate", "%" + searchAlbumRequest.getTimeAlbumSearch() + "%");
            }
            if (StringUtils.isNotBlank(searchAlbumRequest.getAlbumType())) {
                queryStr.append("and album_type=:albumType");
                mapParams.put("albumType", searchAlbumRequest.getAlbumType());
            }
            if (idSchool != null) {
                queryStr.append(" and id_school =:idSchool");
                mapParams.put("idSchool", idSchool);
            }
            if (searchAlbumRequest.getIdClass() != null) {
                queryStr.append(" and id_class =:idClass");
                mapParams.put("idClass", searchAlbumRequest.getIdClass());
            }
            if (searchAlbumRequest.getIdGrade() != null) {
                queryStr.append(" and maClass.grade.id =:idGrade");
                mapParams.put("idClass", searchAlbumRequest.getIdGrade());
            }

        }
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public long getCountMessage(Long idSchool, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (localDateTime != null) {
            queryStr.append("and created_date<:createDate ");
            mapParams.put("createDate", localDateTime);
        }
        return countAll(queryStr.toString(), mapParams);
    }

//    @Override
//    public Long getCountMessage(Long idSchool) {
//        StringBuilder queryStr = new StringBuilder("");
//        Map<String, Object> mapParams = new HashMap<>();
//        if (idSchool != null) {
//            queryStr.append("and id_school=:idSchool ");
//            mapParams.put("idSchool", idSchool);
//        }
////        if (idKid != null) {
////            queryStr.append("and id_kids=:idKid ");
////            mapParams.put("idKid", idKid);
////        }
////        queryStr.append("and parent_medicine_del=:parentMedicineDel ");
////        mapParams.put("parentMedicineDel", AppConstant.APP_FALSE);
//        return countAll(queryStr.toString(), mapParams);
//    }


    @Override
    public Long getCountMessageforClass(Long idClass, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (localDateTime != null) {
            queryStr.append("and created_date<:createDate ");
            mapParams.put("createDate", localDateTime);
        }
        return countAll(queryStr.toString(), mapParams);
    }


    @Override
    public List<Album> findAllbumClassForTeacher(Long idSchool, Long idClass, AlbumTeacherRequest albumTeacherRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append(" order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, albumTeacherRequest.getPageNumber());
    }

    @Override
    public List<Album> findAllAlbumForTeacherx(Long idSchool, Long idClass, AlbumTeacherRequest albumTeacherRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append(" order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, albumTeacherRequest.getPageNumber());
    }

    @Override
    public List<Album> findAllbumSchoolForTeachers(Long idSchool, Long idClass, AlbumTeacherRequest albumTeacherRequest) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append(" and id_class IS NULL ");
        queryStr.append(" order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, albumTeacherRequest.getPageNumber());
    }

    @Override
    public List<Album> findAllAlbumForPlus(Long idSchool, SearchAlbumPlusRequest searchAlbumPlusRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchAlbumPlusRequest.getDate() != null) {
            queryStr.append("and date(created_date)=:createdDate ");
            mapParams.put("createdDate", searchAlbumPlusRequest.getDate());
        }
        if (searchAlbumPlusRequest.getIdClass() != null && searchAlbumPlusRequest.getIdClass() > 0) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", searchAlbumPlusRequest.getIdClass());
        }
        if (searchAlbumPlusRequest.getIdClass() != null && searchAlbumPlusRequest.getIdClass() == 0) {
            queryStr.append("and album_type=:albumType ");
            mapParams.put("albumType", "Trường");
        }
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, searchAlbumPlusRequest.getPageNumber());
    }

    @Override
    public List<Album> findalBumNew(Long idSchool, SearchAlbumNewRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAlbum(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countSearchAlbum(Long idSchool, SearchAlbumNewRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAlbum(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<Album> findAllAlbumClassChart(Long idClass, String albumType, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and album_type=:albumType ");
        mapParams.put("albumType", albumType);
        if (idClass != null){
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (date != null){
            queryStr.append("and date(created_date)=:date ");
            mapParams.put("date", date);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Album> findAllAlbumDateInChart(Long idSchool, Long idClass, Long idGrade, String albumType, List<LocalDate> dates) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and album_type=:albumType ");
        mapParams.put("albumType", albumType);
        if (CollectionUtils.isNotEmpty(dates)){
            LocalDate startDate = dates.get(0);
            LocalDate endDate = dates.get(1);
            queryStr.append("and date(created_date)>=:startDate and date(created_date)<=:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        if (idClass != null){
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (idGrade != null){
            queryStr.append("and exists(select*from ma_class as model1 where model1.id=model.id_class and model1.id_grade=:idGrade) ");
            mapParams.put("idGrade", idGrade);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Album> findAllAlbumGradeChart(Long idGrade, String albumType, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and album_type=:albumType ");
        mapParams.put("albumType", albumType);
        if (date != null){
            queryStr.append("and date(created_date)=:date ");
            mapParams.put("date", date);
        }
        if (idGrade != null){
            queryStr.append("and exists(select*from ma_class as model1 where model1.id=model.id_class and model1.id_grade=:idGrade) ");
            mapParams.put("idGrade", idGrade);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void setSearchAlbum(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchAlbumNewRequest request) {
        if (StringUtils.isNotBlank(request.getTimeAlbumSearch()) && !request.getTimeAlbumSearch().equalsIgnoreCase("null")) {
            queryStr.append("and date(created_date)  =:createdDateFrom ");
            mapParams.put("createdDateFrom", request.getTimeAlbumSearch());
        }
        if (StringUtils.isNotBlank(request.getAlbumType())) {
            queryStr.append(" and album_type=:albumType ");
            mapParams.put("albumType", request.getAlbumType());
        }
        if (request.getIdClass() != null) {
            queryStr.append(" and id_class =:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        queryStr.append(" and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by created_date desc");
    }

}
