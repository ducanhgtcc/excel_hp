package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.classes.ScheduleFile;
import com.example.onekids_project.repository.repositorycustom.ScheduleFileRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleFileRepositoryImpl extends BaseRepositoryimpl<ScheduleFile> implements ScheduleFileRepositoryCustom {
    @Override
    public ScheduleFile searchScheduleImageWeek(Long idSchool, Long idClass, LocalDate localDate) {
        ScheduleFile scheduleFile = new ScheduleFile();
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
            queryStr.append("and from_file_tsime =:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("and is_approved =true ");
        List<ScheduleFile> scheduleFileList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(scheduleFileList)) {
            return scheduleFile;
        }
        return scheduleFileList.get(0);
    }

    @Override
    public List<ScheduleFile> searchScheduleFile(Long idSchool, Long idClass, LocalDate localDate) {
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
            queryStr.append("and from_file_tsime <=:fromFileTsime ");
            mapParams.put("fromFileTsime", localDate);
        }
        queryStr.append("and is_approved =true ");
        queryStr.append("order by from_file_tsime desc");
        List<ScheduleFile> scheduleFiles = findAllNoPaging(queryStr.toString(), mapParams);
        return scheduleFiles;
    }

    @Override
    public long countScheduleFile(Long idSchool, Long idClass, LocalDate localDate) {
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
            queryStr.append("and from_file_tsime <:fromFileTsime ");
            mapParams.put("fromFileTsime", localDate);
        }
        queryStr.append("and is_approved =true ");
        return countAll(queryStr.toString(), mapParams);
    }

    public List<ScheduleFile> findScheduleFile(Long idSchool, Long idClass, LocalDate monday) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (monday != null) {

            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", idSchool);

            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);

            queryStr.append("and from_file_tsime =:date ");
            mapParams.put("date", monday);

        }

        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<ScheduleFile> searchScheduleFilePageNumber(Long idSchool, Long idClass, Integer pageNumber) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("and is_approved = true ");
        queryStr.append("order by from_file_tsime desc");
        List<ScheduleFile> scheduleFileList = findAllMobilePaging(queryStr.toString(), mapParams, pageNumber);
        return scheduleFileList;
    }
}
