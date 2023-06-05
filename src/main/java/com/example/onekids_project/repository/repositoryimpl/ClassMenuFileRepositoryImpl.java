package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.repository.repositorycustom.ClassMenuFileRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMenuFileRepositoryImpl extends BaseRepositoryimpl<ManuFile> implements ClassMenuFileRepositoryCustom {
    @Override
    public ManuFile searchMenuImageWeek(Long idSchool, Long idClass, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        ManuFile manuFile = new ManuFile();
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {     // đặt tên theo idSchool truyền vào
            queryStr.append("and id_school=:idSchool ");    // đặt tên theo id_school giống tên trường trong database,
            mapParams.put("idSchool", idSchool); // đặt tên theo idSchool truyền vào
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (localDate != null) {
            queryStr.append("and from_file_time =:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("and is_approved =true ");
        List<ManuFile> manuFileList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(manuFileList)) {
            return manuFile;
        }
        return manuFileList.get(0);
    }

    @Override
    public List<ManuFile> searchMenuFile(Long idSchool, Long idClass, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {     // đặt tên theo idSchool truyền vào
            queryStr.append("and id_school=:idSchool ");    // đặt tên theo id_school giống tên trường trong database,
            mapParams.put("idSchool", idSchool); // đặt tên theo idSchool truyền vào
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (localDate != null) {
            queryStr.append("and from_file_time <=:fromFileTime ");
            mapParams.put("fromFileTime", localDate);
        }
        queryStr.append("and is_approved = true ");
        queryStr.append("order by from_file_time desc");
        List<ManuFile> manuFileList = findAllNoPaging(queryStr.toString(), mapParams);
        return manuFileList;
    }

    @Override
    public List<ManuFile> searchMenuFilePageNumber(Long idSchool, Long idClass, Integer pageNumber) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("and is_approved = true ");
        queryStr.append("order by from_file_time desc");
        List<ManuFile> manuFileList = findAllMobilePaging(queryStr.toString(), mapParams, pageNumber);
        return manuFileList;
    }

    @Override
    public long countMenuFile(Long idSchool, Long idClass, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {     // đặt tên theo idSchool truyền vào
            queryStr.append("and id_school=:idSchool ");    // đặt tên theo id_school giống tên trường trong database,
            mapParams.put("idSchool", idSchool); // đặt tên theo idSchool truyền vào
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (localDate != null) {
            queryStr.append("and from_file_time <:fromFileTime ");
            mapParams.put("fromFileTime", localDate);
        }
        queryStr.append("and is_approved =true ");
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public ManuFile findMenuFile(Long idSchool, Long idClass, LocalDate monday) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (monday != null) {

            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", idSchool);

            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);

            queryStr.append("and from_file_time =:date ");
            mapParams.put("date", monday);

        }
        List<ManuFile> manuFiles = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(manuFiles)) {
            return null;
        } else {
            return manuFiles.get(0);
        }
    }

    @Override
    public List<ManuFile> findManuFile(Long idSchool, Long idClass, LocalDate monday) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (monday != null) {

            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", idSchool);

            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);

            queryStr.append("and from_file_time =:date ");
            mapParams.put("date", monday);

        }

        return findAllNoPaging(queryStr.toString(), mapParams);

    }
}
