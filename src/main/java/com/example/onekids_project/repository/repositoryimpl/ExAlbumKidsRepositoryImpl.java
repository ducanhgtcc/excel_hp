package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import com.example.onekids_project.repository.repositorycustom.ExAlbumKidsRepositoryCustom;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExAlbumKidsRepositoryImpl extends BaseRepositoryimpl<ExAlbumKids> implements ExAlbumKidsRepositoryCustom {


    @Override
    public List<ExAlbumKids> findAlbumMoblieforClass(Long idKids, Long idClass) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKids != null) {
            quertStr.append(" and id_kids =:idKid");
            mapParams.put("idKid", idKids);
        }
        quertStr.append(" and exists(select*from ma_album where model.id_album=ma_album.id and album_type='lớp') ");
        quertStr.append("order by created_date desc");
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<ExAlbumKids> findAlbumMoblieforSchool1(Long idKids, Long idClass) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKids != null) {
            quertStr.append(" and id_kids =:idKid");
            mapParams.put("idKid", idKids);
        }
        quertStr.append(" and exists(select*from ma_album where model.id_album=ma_album.id and album_type='trường') ");
        quertStr.append("order by created_date desc");
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<ExAlbumKids> findAlbumMoblie(Long idKids, LocalDateTime localDateTime, Pageable pageable) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setAlbumMobile(idKids, localDateTime, quertStr, mapParams);
        return findAllMobile(quertStr.toString(), mapParams, pageable);
    }

    @Override
    public long countAlbumMobile(Long idKids, LocalDateTime localDateTime) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setAlbumMobile(idKids, localDateTime, quertStr, mapParams);
        return countAll(quertStr.toString(), mapParams);
    }

    private void setAlbumMobile(Long idKids, LocalDateTime localDateTime, StringBuilder quertStr, Map<String, Object> mapParams) {
        quertStr.append(" and id_kids =:idKid");
        mapParams.put("idKid", idKids);
        if (localDateTime != null) {
            quertStr.append(" and created_date<:createDate");
            mapParams.put("createDate", localDateTime);
        }
        quertStr.append(" and del_active =:delActive");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        quertStr.append(" and exists(select*from ma_album where model.id_album=ma_album.id)");
        quertStr.append(" order by created_date desc");
    }

    @Override
    public List<ExAlbumKids> findByIdAlbumClass(Long idAlbum) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idAlbum != null) {
            quertStr.append(" and id_album =:idAlbum");
            mapParams.put("idAlbum", idAlbum);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<ExAlbumKids> findByidAlbum(int idAlbum) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        quertStr.append(" and id_album =:idAlbum");
        mapParams.put("idAlbum", idAlbum);
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<ExAlbumKids> findAlbumMoblieForteacher(Long idClass) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            quertStr.append(" and id_class =:idClass");
            mapParams.put("idClass", idClass);
        }
        quertStr.append(" and del_active =:delActive");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        quertStr.append(" and exists(select*from ma_album where model.id_album=ma_album.id) ");
        quertStr.append("order by created_date desc");
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

}
