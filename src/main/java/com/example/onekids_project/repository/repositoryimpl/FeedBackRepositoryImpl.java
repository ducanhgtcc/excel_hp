package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.FeedBackConstant;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.master.request.SearchFeedBackOneKidsRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackPlusRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.repository.repositorycustom.FeedBackRepositoryCustom;
import com.example.onekids_project.request.feedback.SearchParentFeedbackRequest;
import com.example.onekids_project.request.feedback.SearchTeacherFeedbackRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedBackRepositoryImpl extends BaseRepositoryimpl<FeedBack> implements FeedBackRepositoryCustom {


    @Override
    public List<FeedBack> findAllFeedBack(Long idSchool, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FeedBack> searchTitle(Long idSchool, SearchParentFeedbackRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchParentFeedback(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

//    @Override
//    public List<FeedBack> searchteacher(Long idSchool, SearchTeacherFeedbackRequest searchTeacherFeedbackRequest) {
//        StringBuilder queryStr = new StringBuilder("");
//        Map<String, Object> mapParams = new HashMap<>();
//
//        if (searchTeacherFeedbackRequest != null) {
//            if (idSchool != null) {
//                queryStr.append("and id_school=:idSchool ");
//                mapParams.put("idSchool", idSchool);
//            }
////            if (StringUtils.isNotBlank(searchTeacherFeedbackRequest.getAccountType())) {
////                queryStr.append("and (account_type=:accountType or account_type=:sys) ");
////                mapParams.put("accountType", searchTeacherFeedbackRequest.getAccountType().trim());
////                mapParams.put("sys", "sys");
////            }
//            if (StringUtils.isNotBlank(searchTeacherFeedbackRequest.getFeedbackTitle())) {
//                queryStr.append("and feedback_title like :feedbackTitle ");
//                mapParams.put("feedbackTitle", "%" + searchTeacherFeedbackRequest.getFeedbackTitle() + "%");
//            }
//
//            if (searchTeacherFeedbackRequest.getHiddenStatus() != null) {
//                queryStr.append("and hidden_status =:hiddenStatus ");
//                mapParams.put("hiddenStatus", searchTeacherFeedbackRequest.getHiddenStatus());
//            }
//
//            if (searchTeacherFeedbackRequest.getSchoolUnread() != null) {
//                queryStr.append("and school_unread=:schoolUnread ");
//                mapParams.put("schoolUnread", searchTeacherFeedbackRequest.getSchoolUnread());
//            }
//            if (!CollectionUtils.isEmpty(searchTeacherFeedbackRequest.getDateStartEnd())) {
//                queryStr.append("and created_date >=:dateStart and created_date <=:dateEnd ");
//                mapParams.put("dateStart", searchTeacherFeedbackRequest.getDateStartEnd().get(0));
//                mapParams.put("dateEnd", searchTeacherFeedbackRequest.getDateStartEnd().get(1));
//            }
//            queryStr.append("and account_type like :accountType1  ");
//            mapParams.put("accountType1", FeedBackConstant.TEACHER);
////            or account_type like :accountType2
////            mapParams.put("accountType2", FeedBackConstant.SYSTEM);
//            queryStr.append("order by created_date desc");
//        }
//        return findAllNoPaging(queryStr.toString(), mapParams);
//    }

    @Override
    public List<FeedBack> searchFeedbackAdmin(SearchFeedBackOneKidsRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchFeedbackAdmin(queryStr, mapParams, request, idSchoolList);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), request.isDeleteStatus());
    }

    @Override
    public long countSearchFeedbackAdmin(SearchFeedBackOneKidsRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchFeedbackAdmin(queryStr, mapParams, request, idSchoolList);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, request.isDeleteStatus());
    }

    @Override
    public List<FeedBack> findFeedBackParentList(Long idSchool, Long idKid, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDateTime != null) {
            queryStr.append("and created_date<:createdate ");
            mapParams.put("createdate", localDateTime);
        }
        queryStr.append("and account_type=:parent ");
        mapParams.put("parent", FeedBackConstant.PARENT);
        queryStr.append("order by created_date desc");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public long getCountParent(Long idSchool, Long idKid, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDateTime != null) {
            queryStr.append("and created_date<:createdate ");
            mapParams.put("createdate", localDateTime);
        }
        return countAll(queryStr.toString(), mapParams);
    }

    @Override

    public List<FeedBack> searchTitleTeacher(Long idSchool, SearchTeacherFeedbackRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchTeacherFeedback(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }


    public List<FeedBack> findFeedBackTeacherList(UserPrincipal principal, PageNumberRequest pageNumberRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());
        }
        if (principal.getId() != null) {
            queryStr.append("and id_created=:idLoggin ");
            mapParams.put("idLoggin", principal.getId());
        }
        queryStr.append("order by created_date desc");
        List<FeedBack> dataList = findAllMobilePaging(queryStr.toString(), mapParams, pageNumberRequest.getPageNumber());
        return dataList;
    }

    @Override
    public long countTotalAccount(Long idSchool, SearchParentFeedbackRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchParentFeedback(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public long countTotalAccountTC(Long idSchool, SearchTeacherFeedbackRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchTeacherFeedback(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<FeedBack> findFeedBackPlus(Long idSchool, FeedbackPlusRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (request.getDate() != null) {
            queryStr.append("and date(created_date)=:createdDate ");
            mapParams.put("createdDate", request.getDate());
        }
        if (request.getConfirmStatus() != null) {
            queryStr.append("and school_confirm_status=:confirmStatus ");
            mapParams.put("confirmStatus", request.getConfirmStatus());
        }
        if (StringUtils.isNotBlank(request.getContent())) {
            queryStr.append("and feedback_content like :feedbackContent or feedback_title like :feedbackTitle ");
            mapParams.put("feedbackContent", "%" + request.getContent() + "%");
            mapParams.put("feedbackTitle", "%" + request.getContent() + "%");
        }
        if (StringUtils.isNotBlank(request.getTypeSend()) && request.getTypeSend().equals(FeedBackConstant.PARENT)) {
            queryStr.append("and (account_type =:accountType1 or account_type =:accountType2) ");
            mapParams.put("accountType1", FeedBackConstant.PARENT);
            mapParams.put("accountType2", FeedBackConstant.SYSTEM);
        }
        if (StringUtils.isNotBlank(request.getTypeSend()) && request.getTypeSend().equals(FeedBackConstant.TEACHER)) {
            queryStr.append("and account_type like :accountType ");
            mapParams.put("accountType", FeedBackConstant.TEACHER);
        }

        queryStr.append("order by created_date desc");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    private void setSearchParentFeedback(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchParentFeedbackRequest request) {
        if (StringUtils.isNotBlank(request.getFeedbackTitle())) {
            queryStr.append("and feedback_title like :feedbackTitle ");
            mapParams.put("feedbackTitle", "%" + request.getFeedbackTitle() + "%");
        }
        if (request.getHiddenStatus() != null) {
            queryStr.append("and hidden_status =:hiddenStatus ");
            mapParams.put("hiddenStatus", request.getHiddenStatus());
        }
        if (request.getSchoolUnread() != null) {
            queryStr.append("and school_unread=:schoolUnread ");
            mapParams.put("schoolUnread", request.getSchoolUnread());
        }
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date >=:dateStart and created_date <=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);

        queryStr.append("and (account_type =:accountType1 or account_type =:accountType2) ");
        mapParams.put("accountType1", FeedBackConstant.PARENT);
        mapParams.put("accountType2", FeedBackConstant.SYSTEM);

        queryStr.append("order by created_date desc");
    }

    private void setSearchTeacherFeedback(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchTeacherFeedbackRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(request.getFeedbackTitle())) {
            queryStr.append("and feedback_title like :feedbackTitle ");
            mapParams.put("feedbackTitle", "%" + request.getFeedbackTitle() + "%");
        }
        if (request.getHiddenStatus() != null) {
            queryStr.append("and hidden_status =:hiddenStatus ");
            mapParams.put("hiddenStatus", request.getHiddenStatus());
        }
        if (request.getSchoolUnread() != null) {
            queryStr.append("and school_unread=:schoolUnread ");
            mapParams.put("schoolUnread", request.getSchoolUnread());
        }
        if (!CollectionUtils.isEmpty(request.getDateStartEnd())) {
            queryStr.append("and created_date >=:dateStart and created_date <=:dateEnd ");
            mapParams.put("dateStart", request.getDateStartEnd().get(0));
            mapParams.put("dateEnd", request.getDateStartEnd().get(1).plusDays(1));
        }
        queryStr.append("and account_type like :accountType1 ");
        mapParams.put("accountType1", FeedBackConstant.TEACHER);
        queryStr.append("order by created_date desc");
    }

    private void setSearchFeedbackAdmin(StringBuilder queryStr, Map<String, Object> mapParams, SearchFeedBackOneKidsRequest request, List<Long> idSchoolList) {
        queryStr.append("and id_school in (:idSchoolList) ");
        mapParams.put("idSchoolList", idSchoolList);
        if (request.getIdSchool() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", request.getIdSchool());
        }
        if (StringUtils.isNotBlank(request.getAccountType())) {
            queryStr.append("and account_type=:accountType ");
            mapParams.put("accountType", request.getAccountType());
        }
        queryStr.append(" order by id DESC");
    }


}
