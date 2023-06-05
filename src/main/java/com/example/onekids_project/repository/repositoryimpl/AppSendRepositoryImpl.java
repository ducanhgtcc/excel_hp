package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppSendConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.master.request.SearchAppSendRequest;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.NotifiSysRequest;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.SearchHistoryNotifiPlusRequest;
import com.example.onekids_project.mobile.teacher.request.historynotifi.SearchHistoryNotifiTeacherRequest;
import com.example.onekids_project.repository.repositorycustom.AppSendRepositoryCustom;
import com.example.onekids_project.request.AppSend.SearchContentRequest;
import com.example.onekids_project.request.notifihistory.SearchSmsAppRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class AppSendRepositoryImpl extends BaseRepositoryimpl<AppSend> implements AppSendRepositoryCustom {

    @Override
    public List<AppSend> findAllNotif(Long idSchool, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<AppSend> findByIdNotifi(Long idSchool, Long id) {
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
        List<AppSend> appSendList = findAllNoPaging(queryStr.toString(), mapParams);
        if (appSendList.size() > 0) {
            return Optional.ofNullable(appSendList.get(0));
        }
        return Optional.empty();
    }

    public List<AppSend> searchNotifi(Long idSchool, Long idUserReceiver, SearchContentRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAbsentLetter(queryStr, mapParams, idSchool, idUserReceiver, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    private void setSearchAbsentLetter(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, Long idUserReceiver, SearchContentRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getSendContent())) {
            queryStr.append("and send_content like :sendContent ");
            mapParams.put("sendContent", "%" + request.getSendContent() + "%");
        }
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        queryStr.append("and send_del=:sendDel ");
        mapParams.put("sendDel", AppConstant.APP_FALSE);
        queryStr.append(" and exists(select * from ma_receivers as model1 where model1.id_send =  model.id and model1.id_user_receiver =:idUserReceiver) ");
        mapParams.put("idUserReceiver", idUserReceiver);
        queryStr.append("order by created_date desc");
    }

    @Override
    public List<AppSend> searchNotifyAdmin(SearchAppSendRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchNotifyAdmin(queryStr, mapParams, request, idSchoolList);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countSearchNotifyAdmin(SearchAppSendRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchNotifyAdmin(queryStr, mapParams, request, idSchoolList);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<AppSend> findAppSendListByIdClass(Long idClass, SearchHistoryNotifiTeacherRequest searchHistoryNotifiTeacherRequest, UserPrincipal principal) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isNotBlank(searchHistoryNotifiTeacherRequest.getKeyWord())) {
            queryStr.append("and send_content like :sendContent ");
            mapParams.put("sendContent", "%" + searchHistoryNotifiTeacherRequest.getKeyWord() + "%");
        }
        queryStr.append("and app_type=:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        queryStr.append("and id_created=:idCreated ");
        mapParams.put("idCreated", principal.getId());
        queryStr.append("order by created_date desc ");
        return findAllMobilePaging(queryStr.toString(), mapParams, searchHistoryNotifiTeacherRequest.getPageNumber());

    }


    @Override
    public long countTotalAccount(Long idSchool, Long idUserReceiver, SearchContentRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAbsentLetter(queryStr, mapParams, idSchool, idUserReceiver, request);
        return countAll(queryStr.toString(), mapParams);
    }


    @Override
    public List<AppSend> searchSmsAppnew(Long idSchool, Long idUser, SearchSmsAppRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMessageParent(idSchool, idUser, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    private void setSearchMessageParent(Long idSchool, Long idUser, SearchSmsAppRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        if (StringUtils.isNotBlank(request.getDateSick()) && request.getDateSick().equals("finance")) {
            queryStr.append("and send_type like :sendType ");
            mapParams.put("sendType", "%" + request.getDateSick() + "%");
            queryStr.append("and id_created=:idCreated ");
            mapParams.put("idCreated", idUser);
        }
        if (StringUtils.isNotBlank(request.getDateSick()) && !request.getDateSick().equals("finance")) {
            queryStr.append("and send_type like :sendType ");
            mapParams.put("sendType", "%" + request.getDateSick() + "%");
        }
        if (StringUtils.isNotBlank(request.getReceiverType())) {
            queryStr.append("and type_reicever like :typeReicever ");
            mapParams.put("typeReicever", "%" + request.getReceiverType() + "%");

        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            queryStr.append("and send_title like :sendTitle ");
            mapParams.put("sendTitle", "%" + request.getTitle() + "%");
        }

        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and app_type=:appType ");
        mapParams.put("appType", AppTypeConstant.SCHOOL);
        queryStr.append("order by created_date desc");
    }

    @Override
    public long coutSearchSmsAppnew(Long idSchool, Long idUser, SearchSmsAppRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchMessageParent(idSchool, idUser, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<AppSend> searchSmsAppTeachernew(Long idSchool, SearchSmsAppRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSmsTeacherNew(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    private void setSearchSmsTeacherNew(Long idSchool, SearchSmsAppRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        if (StringUtils.isNotBlank(request.getTitle())) {
            queryStr.append("and send_title like :sendTitle ");
            mapParams.put("sendTitle", "%" + request.getTitle() + "%");
        }
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date>=:dateStart and created_date<=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        if (StringUtils.isNotBlank(request.getDateSick())) {
            queryStr.append("and send_type like :sendType ");
            mapParams.put("sendType", "%" + request.getDateSick() + "%");
        }
        queryStr.append("and app_type=:appType ");
        mapParams.put("appType", AppTypeConstant.TEACHER);
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by created_date desc");
    }

    @Override
    public long coutSearchSmsAppTeachernew(Long idSchool, SearchSmsAppRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSmsTeacherNew(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public long countByIdSchool(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);

        queryStr.append("and app_type in (:appTypeList) ");
        mapParams.put("appTypeList", Arrays.asList(AppTypeConstant.TEACHER, AppTypeConstant.SCHOOL, AppTypeConstant.SYSTEM));
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<AppSend> searchHistoryNotifi(Long idSchool, SearchHistoryNotifiPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and send_type=:sendType ");
        mapParams.put("sendType", AppConstant.SEND_TYPE_COMMON);
        queryStr.append("and app_type!=:appType ");
        mapParams.put("appType", AppTypeConstant.SYSTEM);
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and send_title like :sendTitle ");
            mapParams.put("sendTitle", "%" + request.getContent() + "%");
        }
        if (StringUtils.isNotBlank(request.getAppType()) && request.getAppType().equals(AppConstant.TYPE_VIDEO_SCHOOL)) {
            queryStr.append("and app_type =:appType1 ");
            mapParams.put("appType1", AppTypeConstant.SCHOOL);
        }
        else if (StringUtils.isNotBlank(request.getAppType()) && request.getAppType().equals(AppConstant.TYPE_TEACHER)) {
            queryStr.append("and app_type =:appType1 ");
            mapParams.put("appType1", AppTypeConstant.TEACHER);
        }
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<AppSend> findNotifiSys(Long idSchool, Long idReceiver, NotifiSysRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append(" and exists(select * from ma_receivers as model1 where model1.id_send =  model.id and model1.id_user_receiver =:idReceiver and send_del=false) ");
        mapParams.put("idReceiver", idReceiver);
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and send_content like :sendContent ");
            mapParams.put("sendContent", "%" + request.getContent() + "%");
        }
        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    private void setSearchNotifyAdmin(StringBuilder queryStr, Map<String, Object> mapParams, SearchAppSendRequest request, List<Long> idSchoolList) {
        queryStr.append("and id_school in (:idSchoolList) ");
        mapParams.put("idSchoolList", idSchoolList);
        if (request.getIdSchool() != null) {
            queryStr.append("and id_school =:idSchool ");
            mapParams.put("idSchool", request.getIdSchool());
        }
        if (StringUtils.isNotBlank(request.getSendType())) {
            queryStr.append("and send_type =:sendType ");
            mapParams.put("sendType", request.getSendType());
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            queryStr.append("and send_title like :sendTitle ");
            mapParams.put("sendTitle", "%" + request.getTitle() + "%");
        }
        List<String> sendTypeList = Arrays.asList(AppSendConstant.TYPE_SYS, AppSendConstant.TYPE_COMMON, AppSendConstant.TYPE_BIRTHDAY, AppSendConstant.TYPE_CELEBRATE);
        queryStr.append("and send_type in(:sendTypeList) ");
        mapParams.put("sendTypeList", sendTypeList);
        queryStr.append("and app_type=:appType ");
        mapParams.put("appType", AppTypeConstant.SYSTEM);
        queryStr.append(" order by id desc");
    }


}
