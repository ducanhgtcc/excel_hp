package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.repository.repositorycustom.MessageParentRepositoryCustom;
import com.example.onekids_project.request.parentdiary.SearchMessageParentRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MessageParentRepositoryImpl extends BaseRepositoryimpl<MessageParent> implements MessageParentRepositoryCustom {

    @Override
    public List<MessageParent> findAllMessageParent(Long idSchoolLogin, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MessageParent> searchMessageParent(Long idSchool, SearchMessageParentRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMessageParent(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countSearchMessageParent(Long idSchool, SearchMessageParentRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMessageParent(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }


    @Override
    public Optional<MessageParent> findByIdMessage(Long idSchool, Long id) {
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
        List<MessageParent> messageParentList = findAllNoPaging(queryStr.toString(), mapParams);
        if (messageParentList.size() > 0) {
            return Optional.ofNullable(messageParentList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<MessageParent> findMessageParentMobile(Long idSchool, Long idKid, Pageable pageable, LocalDateTime localDateTime) {
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
        queryStr.append("and parent_message_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        List<MessageParent> messageParentList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return messageParentList;
    }

    @Override
    public long getCountMessage(Long idSchool, Long idKid, LocalDateTime localDateTime) {
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
        queryStr.append("and parent_message_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        return countAll(queryStr.toString(), mapParams);
    }


    @Override
    public List<MessageParent> findMessageforTeacherMobile(Long idSchool, Long idClass, SearchMessageTeacherRequest searchMessageTeacherRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchMessageTeacherRequest != null) {
            if (StringUtils.isNotBlank(searchMessageTeacherRequest.getDate())) {
                queryStr.append("and created_date like :createdDate ");
                mapParams.put("createdDate", "%" + searchMessageTeacherRequest.getDate() + "%");
            }
            if (searchMessageTeacherRequest.getConfirmStatus() != null) {
                queryStr.append("and confirm_status=:confirmStatus ");
                mapParams.put("confirmStatus", searchMessageTeacherRequest.getConfirmStatus());
            }
            if (StringUtils.isNotBlank(searchMessageTeacherRequest.getKeyWord())) {
                queryStr.append("and message_content like :messageContent ");
                mapParams.put("messageContent", "%" + searchMessageTeacherRequest.getKeyWord() + "%");
            }
            queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
            queryStr.append("and parent_message_del=:parentMessageDel ");
            mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
            queryStr.append("order by created_date desc");
        }
        return findAllMobilePaging(queryStr.toString(), mapParams, searchMessageTeacherRequest.getPageNumber());
    }

    @Override
    public List<MessageParent> findMessageforPlus(Long idSchool, SearchMessagePlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and parent_message_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        if (request.getDate() != null) {
            queryStr.append("and date(created_date)=:createdDate ");
            mapParams.put("createdDate", request.getDate());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(request.getKidName())) {
            request.setKidName(request.getKidName().trim());
            queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and full_name like :fullName) ");
            mapParams.put("fullName", "%" + request.getKidName() + "%");
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<MessageParent> findMessageParentWithDate(Long idSchool, List<Long> idClassList, LocalDate date) {
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
            queryStr.append("and date(created_date)=:date ");
            mapParams.put("date", date);
        }
        queryStr.append("and parent_message_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void setSearchMessageParent(Long idSchool, SearchMessageParentRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getDate() != null) {
            queryStr.append("and date(created_date)=:createdDate ");
            mapParams.put("createdDate", request.getDate());
        }
        if (request.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", request.getIdGrade());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and message_content like :messageContent ");
            mapParams.put("messageContent", "%" + request.getName() + "%");
        }
        queryStr.append("and EXISTS (SELECT * FROM ma_kids as model1 WHERE model.id_kids = model1.id and del_active=true) ");
        queryStr.append("and parent_message_del=:parentMessageDel ");
        mapParams.put("parentMessageDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
    }

}
