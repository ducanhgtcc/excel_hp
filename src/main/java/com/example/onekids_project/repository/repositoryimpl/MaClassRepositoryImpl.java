package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.repository.repositorycustom.MaClassRepositoryCustom;
import com.example.onekids_project.request.classes.ClassSearchCommonRequest;
import com.example.onekids_project.request.classes.ClassSearchNewRequest;
import com.example.onekids_project.request.classes.SearchMaClassRequest;
import com.example.onekids_project.request.schoolconfig.ClassConfigSearchRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MaClassRepositoryImpl extends BaseRepositoryimpl<MaClass> implements MaClassRepositoryCustom {

    @Override
    public List<MaClass> findClassInGrade(Long idSchool, Long idGrade) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (idGrade != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", idGrade);
        }
        queryStr.append("order by class_name collate utf8_vietnamese_ci");
        List<MaClass> maClassList = findAllNoPaging(queryStr.toString(), mapParams);
        return maClassList;
    }

    @Override
    public List<MaClass> findClassCommon(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by id_grade, class_name collate utf8_vietnamese_ci");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> findClassTeacher(Long idSchool, List<Long> idClassList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id in(:idClassList) ");
        mapParams.put("idClassList", idClassList);
        queryStr.append("order by class_name collate utf8_vietnamese_ci");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> findClassInGradeWithDate(Long idSchool, Long idGrade, LocalDate localDate) {
        LocalDate date = localDate.plusDays(1);
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (idGrade != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", idGrade);
        }
        queryStr.append("and created_date <:date ");
        mapParams.put("date", date);
        queryStr.append("order by class_name collate utf8_vietnamese_ci");
        List<MaClass> maClassList = findAllNoPaging(queryStr.toString(), mapParams);
        return maClassList;
    }

    @Override
    public List<MaClass> findClassByInfoEmployee(Long idInfo, Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null){
            queryStr.append("and model.id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        queryStr.append(" and exists(select eec.id from ex_employee_class as eec where model.id = eec.id_class and eec.id_info_employee =:idInfo) ");
        mapParams.put("idInfo", idInfo);
        queryStr.append("order by id_grade, class_name collate utf8_vietnamese_ci");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> searchClassCommon(Long idSchool, ClassSearchCommonRequest classSearchCommonRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (classSearchCommonRequest.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", classSearchCommonRequest.getIdGrade());
        }
        if (classSearchCommonRequest.getIdClass() != null) {
            queryStr.append("and id=:idClass ");
            mapParams.put("idClass", classSearchCommonRequest.getIdClass());
        }
        String CodeOrName = classSearchCommonRequest.getClassName();
        if (StringUtils.isNotBlank(CodeOrName)) {
            CodeOrName = CodeOrName.trim();
            queryStr.append("and lower(class_name) like lower(:className) ");
            mapParams.put("className", "%" + CodeOrName + "%");
        }
        queryStr.append("order by class_name asc");

        List<MaClass> maClassList = findAllNoPaging(queryStr.toString(), mapParams);
        return maClassList;
    }

    @Override
    public List<MaClass> searchClassNew(Long idSchool, ClassSearchNewRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        this.setSearchClassNew(idSchool, queryStr, mapParams, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countSearchClassNew(Long idSchool, ClassSearchNewRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        this.setSearchClassNew(idSchool, queryStr, mapParams, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> findAllMaClass(Long idSchool, Pageable pageable) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            queryStr.append("order by id_grade asc, class_name asc");
            mapParams.put("idSchool", idSchool);
        }
        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public Optional<MaClass> findByIdMaClass(Long idSchool, Long id) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<MaClass> maClassList = findAllNoPaging(queryStr.toString(), mapParams);
        if (!CollectionUtils.isEmpty(maClassList)) {
            return Optional.ofNullable(maClassList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<MaClass> searchMaClassWithSchool(Long idSchool, Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id=:idClass");
            mapParams.put("idClass", idClass);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> searchMaClass(Long idSchool, Pageable pageable, SearchMaClassRequest searchMaClassRequest) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchMaClassRequest != null) {

            if (idSchool != null) {
                queryStr.append("and id_school=:idSchool ");
                mapParams.put("idSchool", idSchool);
            }

            String CodeOrName = searchMaClassRequest.getClassName();
            if (StringUtils.isNotBlank(CodeOrName)) {
                CodeOrName = CodeOrName.trim();
                queryStr.append("and lower(class_name) like lower(:className) ");
                mapParams.put("className", "%" + CodeOrName + "%");
            }
        }

        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public List<MaClass> searchMaClassByIdGrade(Long idSchool, Long idGrade, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idGrade != null) {
            queryStr.append("and id_grade=:idGrade");
            mapParams.put("idGrade", idGrade);
        }
        if (id != null) {
            queryStr.append(" and id=:id");
            mapParams.put("id", id);
        }
        return findAll(queryStr.toString(), mapParams, null);
    }

    @Override
    public List<MaClass> searchMaClassConfig(Long idSchool, ClassConfigSearchRequest classConfigSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (classConfigSearchRequest.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", classConfigSearchRequest.getIdGrade());
        }
        if (classConfigSearchRequest.getIdClass() != null) {
            queryStr.append("and id=:idClass ");
            mapParams.put("idClass", classConfigSearchRequest.getIdClass());
        }
        String CodeOrName = classConfigSearchRequest.getClassName();
        if (StringUtils.isNotBlank(CodeOrName)) {
            CodeOrName = CodeOrName.trim();
            queryStr.append("and lower(class_name) like lower(:className) ");
            mapParams.put("className", "%" + CodeOrName + "%");
        }
        List<MaClass> maClassList = findAllNoPaging(queryStr.toString(), mapParams);
        return maClassList;
    }

    @Override
    public List<MaClass> searchMaclassForMediaSetting(Long idSchool, MediaSettingSearchRequest mediaSettingSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (mediaSettingSearchRequest.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", mediaSettingSearchRequest.getIdGrade());
        }
        if (mediaSettingSearchRequest.getIdClass() != null) {
            queryStr.append("and id=:idClass ");
            mapParams.put("idClass", mediaSettingSearchRequest.getIdClass());
        }
        String CodeOrName = mediaSettingSearchRequest.getClassName();
        if (StringUtils.isNotBlank(CodeOrName)) {
            CodeOrName = CodeOrName.trim();
            queryStr.append("and lower(class_name) like lower(:className) ");
            mapParams.put("className", "%" + CodeOrName + "%");
        }
        queryStr.append("order by class_name asc");

        List<MaClass> maClassList = findAllNoPaging(queryStr.toString(), mapParams);
        return maClassList;
    }

    @Override
    public List<MaClass> findAllMaClassAlbum(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            queryStr.append("order by id_grade asc, class_name asc");
            mapParams.put("idSchool", idSchool);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> findAllKidsFromClassOld(Long idSchool, Long idClass, Long idAlbum) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            queryStr.append("order by id_grade asc, class_name asc");
            mapParams.put("idSchool", idSchool);
        }
        if (idAlbum != null) {
            queryStr.append("and id_class=:idClass");
            mapParams.put("idClass", idClass);
        }
        if (idAlbum != null) {
            queryStr.append("and id_album=:idAlbum");
            mapParams.put("idAlbum", idAlbum);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> findAllMaClassTeacherAlbum(Long idClass) {
        return null;
    }

    @Override
    public MaClass searchByIdClass(Long idClass) {
        return null;
    }

    @Override
    public List<MaClass> findClassBySchool(Long idSchool, Long idGrade) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);

            if (idGrade != null) {
                queryStr.append("and id_grade=:idGrade ");
                mapParams.put("idGrade", idGrade);
            }
        }


        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaClass> searchClassWithName(Long idSchool, String className) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(className)) {
            queryStr.append("and lower(class_name) like lower(:className) ");
            mapParams.put("className", "%" + className.trim() + "%");
        }
        queryStr.append("order by class_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void setSearchClassNew(Long idSchool, StringBuilder queryStr, Map<String, Object> mapParams, ClassSearchNewRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getClassName())) {
            queryStr.append("and lower(class_name) like lower(:className) ");
            mapParams.put("className", "%" + request.getClassName() + "%");
        }
        queryStr.append("order by class_name collate utf8_vietnamese_ci ");
    }


}
