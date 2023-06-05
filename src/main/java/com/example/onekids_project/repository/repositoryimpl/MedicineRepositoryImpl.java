package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.mobile.plus.request.medicine.SearchMedicinePlusRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.SearchMedicineTeacherRequest;
import com.example.onekids_project.repository.repositorycustom.MedicineRepositoryCustom;
import com.example.onekids_project.request.parentdiary.SearchMedicineRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MedicineRepositoryImpl extends BaseRepositoryimpl<Medicine> implements MedicineRepositoryCustom {

    @Override
    public List<Medicine> findAllMedicine(Long idSchool, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<Medicine> findByIdMedicine(Long idSchool, Long id) {
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
        List<Medicine> medicineList = findAllNoPaging(queryStr.toString(), mapParams);
        if (medicineList.size() > 0) {
            return Optional.ofNullable(medicineList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Medicine> searchMedi(Long idSchool, SearchMedicineRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMedicine(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public Long getCountMessage(Long idSchool, Long idKid, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDateTime != null) {
            queryStr.append("and created_date<:createDate ");
            mapParams.put("createDate", localDateTime);
        }
        queryStr.append("and parent_medicine_del=:parentMedicineDel ");
        mapParams.put("parentMedicineDel", AppConstant.APP_FALSE);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<Medicine> findMedicineMoblie1(Long idSchool, Long idKid, Pageable pageable, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDateTime != null) {
            queryStr.append("and created_date<:createDate ");
            mapParams.put("createDate", localDateTime);
        }
        queryStr.append("and parent_medicine_del=:parentMedicineDel ");
        mapParams.put("parentMedicineDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        List<Medicine> medicineList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return medicineList;
    }

    @Override
    public List<Medicine> searchMedicineForTeacher(Long idSchool, Long idClass, SearchMedicineTeacherRequest searchMedicineTeacherRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchMedicineTeacherRequest.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", searchMedicineTeacherRequest.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(searchMedicineTeacherRequest.getDateSick())) {
            queryStr.append("and to_date >=:dateSick and from_date <=:dateSick ");
            mapParams.put("dateSick", LocalDate.now());
        }
        if (StringUtils.isNotBlank(searchMedicineTeacherRequest.getDateDetail())) {
            queryStr.append("and to_date >=:dateDetail and from_date <=:dateDetail ");
            mapParams.put("dateDetail", searchMedicineTeacherRequest.getDateDetail());
        }
        if (StringUtils.isNotBlank(searchMedicineTeacherRequest.getKeyWord())) {
            queryStr.append("and medicine_content like :medicineContent ");
            mapParams.put("medicineContent", "%" + searchMedicineTeacherRequest.getKeyWord() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and parent_medicine_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, searchMedicineTeacherRequest.getPageNumber());
    }

    @Override
    public long countTotalAccount(Long idSchool, SearchMedicineRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMedicine(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<Medicine> findMedicineDate(Long idSchool, List<Long> idClassList, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idClassList)) {
            queryStr.append("and id_class in(:idClassList) ");
            mapParams.put("idClassList", idClassList);
        }
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (date != null) {
            queryStr.append("and from_date<=:date and to_date>=:date ");
            mapParams.put("date", date);
        }
        queryStr.append("and parent_medicine_del=:parentMedicineDel ");
        mapParams.put("parentMedicineDel", AppConstant.APP_FALSE);
        List<Medicine> medicineList = findAllNoPaging(queryStr.toString(), mapParams);
        return medicineList;
    }

    @Override
    public List<Medicine> searchMedicineForPlus(Long idSchool, SearchMedicinePlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (StringUtils.isNotBlank(request.getDateSick())) {
            queryStr.append("and to_date >=:dateSick and from_date <=:dateSick ");
            mapParams.put("dateSick", LocalDate.now());
        }
        if (request.getDateDetail() != null) {
            queryStr.append("and to_date >=:dateDetail and from_date <=:dateDetail ");
            mapParams.put("dateDetail", request.getDateDetail());
        }
        if (StringUtils.isNotBlank(request.getKidName())) {
            request.setKidName(request.getKidName().trim());
            queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and full_name like :fullName) ");
            mapParams.put("fullName", "%" + request.getKidName() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and parent_medicine_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<Medicine> searchMedicineDate(Long idKid, Long idSchool, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kids=:idKid and id_school=:idSchool and from_date<=:date and to_date>=:date and parent_medicine_del=false ");
        mapParams.put("idKid", idKid);
        mapParams.put("idSchool", idSchool);
        mapParams.put("date", date);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void setSearchMedicine(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchMedicineRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", request.getIdGrade());
        }
        if (request.getDate() != null) {
            queryStr.append("and date(created_date) = :createdDate ");
            mapParams.put("createdDate", request.getDate());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(request.getDateSick())) {
            queryStr.append("and to_date >=:dateSick and from_date <=:dateSick ");
            mapParams.put("dateSick", LocalDate.now());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and medicine_content like :medicineContent ");
            mapParams.put("medicineContent", "%" + request.getName() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("order by created_date desc");
    }

}
