package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppSendConstant;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailUserRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.SearchNotifyTeacherRequest;
import com.example.onekids_project.repository.repositorycustom.ReceiversRepositoryCustom;
import com.example.onekids_project.request.parentweb.NotifyParentWebRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.PrincipalUtils;
import com.example.onekids_project.util.SchoolUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiversRepositoryImpl extends BaseRepositoryimpl<Receivers> implements ReceiversRepositoryCustom {

    @Override
    public Receivers findReceiverByIdUserAndIdSend(Long idUser, Long IdSend) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (IdSend != null) {
            queryStr.append("and id_send=:idSend ");
            mapParams.put("idSend", IdSend);
        }
        if (idUser != null) {
            queryStr.append("and id_user_receiver=:idUserReceiver ");
            mapParams.put("idUserReceiver", idUser);
        }
        List<Receivers> receiversList = findAllNoPaging(queryStr.toString(), mapParams);
        if (receiversList.size() > 0) {
            return receiversList.get(0);
        }
        return null;
    }

    public List<Receivers> findNotifiKidsForMobile(Long idUser, Long idSchool, Long idKid, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_user_receiver=:idUser ");
        mapParams.put("idUser", idUser);
        queryStr.append("and case when id_kids is not null then id_kids=:idKid else true end  ");
        mapParams.put("idKid", idKid);
        queryStr.append("and is_approved=:isApproved ");
        mapParams.put("isApproved", AppConstant.APP_TRUE);
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        List<Receivers> receiversList = findAll(queryStr.toString(), mapParams, pageable);
        return receiversList;
    }

    @Override
    public List<Receivers> findNotifyKidsForWeb(NotifyParentWebRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setNotifyKidsForWeb(queryStr, mapParams, request);
        queryStr.append("order by created_date desc");
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public Long countNotifyKidsForWeb(NotifyParentWebRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setNotifyKidsForWeb(queryStr, mapParams, request);
        queryStr.append("order by created_date desc");
        return countAll(queryStr.toString(), mapParams);
    }

    private void setNotifyKidsForWeb(StringBuilder queryStr, Map<String, Object> mapParams, NotifyParentWebRequest request) {
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", SchoolUtils.getIdSchool());
        queryStr.append("and id_user_receiver=:idUser ");
        mapParams.put("idUser", principal.getId());
        queryStr.append("and case when id_kids is not null then id_kids=:idKid else true end  ");
        mapParams.put("idKid", principal.getIdKidLogin());
        queryStr.append("and is_approved=:isApproved ");
        mapParams.put("isApproved", AppConstant.APP_TRUE);
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        if (StringUtils.isNotBlank(request.getTitle())) {
            queryStr.append(" and exists(select * from ma_app_send as model1 where model1.id = model.id_send and model1.send_title like :title) ");
            mapParams.put("title", "%" + request.getTitle().trim() + "%");
        }
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append(" and exists(select * from ma_app_send as model1 where model1.id = model.id_send and model1.send_content like :content) ");
            mapParams.put("content", "%" + request.getContent().trim() + "%");
        }
        if (CollectionUtils.isNotEmpty(request.getDateList())) {
            queryStr.append("and date(created_date)>=:startDate and date(created_date)<=:endDate ");
            mapParams.put("startDate", request.getDateList().get(0));
            mapParams.put("endDate", request.getDateList().get(1));
        }
    }

    @Override
    public List<Receivers> findNotifyTeacherForMobile(Long idSchool, Long idTeacher, SearchNotifyTeacherRequest searchNotifyTeacherRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_user_receiver=:idTeacher ");
        mapParams.put("idTeacher", idTeacher);
        queryStr.append("and is_approved=:isApproved ");
        mapParams.put("isApproved", AppConstant.APP_TRUE);
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, searchNotifyTeacherRequest.getPageNumber());
    }

    @Override
    public long findNotifiKidsForUnReadMobile(Long idSchool, Long idKid) {
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
        queryStr.append("and is_approved=:isApproved ");
        mapParams.put("isApproved", AppConstant.APP_TRUE);
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        queryStr.append("and user_unread=:userUnread ");
        mapParams.put("userUnread", AppConstant.APP_FALSE);
        long count = countAll(queryStr.toString(), mapParams);
        return count;
    }

    @Override
    public Long getCountNotifi(Long idSchool, Long idKid) {
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
        queryStr.append("and is_approved=:isApproved ");
        mapParams.put("isApproved", AppConstant.APP_TRUE);
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public Long getCountNotifiTeacher(Long idSchool, Long idTeacher) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idTeacher != null) {
            queryStr.append("and id_user_receiver=:idTeacher ");
            mapParams.put("idTeacher", idTeacher);
        }
        queryStr.append("and is_approved=:isApproved ");
        mapParams.put("isApproved", AppConstant.APP_TRUE);
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<Receivers> findByIdAppSend(Long id) {
        return null;
    }

    @Override
    public List<Receivers> findBirthdayStatus(Long idSchool, Long idKid) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append(" and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        int year = LocalDate.now().getYear();
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        if (idKid != null) {
            queryStr.append(" and exists(select*from ma_app_send ap where model.id_send = ap.id and ap.send_type=:birtday and model.id_kids =:idKid and year(created_date) =:year ) ");
            mapParams.put("idKid", idKid);
            mapParams.put("birtday", AppSendConstant.TYPE_BIRTHDAY);
            mapParams.put("year", year);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Receivers> findAllByAppSendIdc(Long id, DetailUserRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and send_title like :sendTitle ");
            mapParams.put("sendTitle", "%" + request.getContent() + "%");
        }
        queryStr.append("and id_send=:idSend ");
        mapParams.put("idSend", id);
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }


}
