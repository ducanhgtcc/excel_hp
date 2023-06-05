package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateMonthMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.ListEvaluateMonthMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluateMonthMobileService;
import com.example.onekids_project.mobile.response.ReplyTypeMobileObject;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FirebaseUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluateMonthMobileServiceImpl implements EvaluateMonthMobileService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public EvaluateMonthMobileResponse findEvaluateMonth(UserPrincipal principal, Long id, Pageable pageable) {
        EvaluateMonthMobileResponse data = new EvaluateMonthMobileResponse();
        List<EvaluateMonth> evaluateWeekList = evaluateMonthRepository.findParentdforKid(principal.getIdSchoolLogin(), principal.getIdKidLogin(), id, pageable);
        List<ListEvaluateMonthMobileResponse> dataList = new ArrayList<>();
        evaluateWeekList.forEach(x -> {
            ListEvaluateMonthMobileResponse model = new ListEvaluateMonthMobileResponse();
            this.setModelData(principal.getIdSchoolLogin(), x, model);
            dataList.add(model);
        });
        data.setDataList(dataList);
        long countAll = evaluateMonthRepository.countParentUnread(principal.getIdSchoolLogin(), principal.getIdKidLogin(), id);
        boolean checkLastPage = countAll <= AppConstant.MAX_PAGE_ITEM;
        data.setLastPage(checkLastPage);
        return data;
    }

    @Transactional
    @Override
    public ListEvaluateMonthMobileResponse createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException {
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateMonth by id in parent"));
        evaluateMonth.setParentReplyIdCreated(principal.getId());
        evaluateMonth.setParentReplyDatetime(LocalDateTime.now());
        evaluateMonth.setParentReplyCreatedBy(principal.getFullName());
        evaluateMonth.setParentReplyContent(content);
        evaluateMonth.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluateMonth evaluateMonthSaved = evaluateMonthRepository.save(evaluateMonth);
        ListEvaluateMonthMobileResponse model = new ListEvaluateMonthMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateMonthSaved, model);

        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(47L, evaluateMonth.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        firebaseFunctionService.sendTeacherByKids(47L, evaluateMonth.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        return model;
    }

    //endfirebase

    @Override
    public ListEvaluateMonthMobileResponse updateParentReply(UserPrincipal principal, Long id, String content) {
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found EvaluateMonth by id in parent"));
        evaluateMonth.setParentReplyIdCreated(principal.getId());
        evaluateMonth.setParentReplyDatetime(LocalDateTime.now());
        evaluateMonth.setParentReplyCreatedBy(principal.getFullName());
        evaluateMonth.setParentReplyContent(content);
        evaluateMonth.setParentReplyModified(AppConstant.APP_TRUE);
        evaluateMonth.setParentReplyDel(AppConstant.APP_FALSE);
        evaluateMonth.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluateMonth evaluateWeekSaved = evaluateMonthRepository.save(evaluateMonth);
        ListEvaluateMonthMobileResponse model = new ListEvaluateMonthMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        return model;
    }

    @Override
    public ListEvaluateMonthMobileResponse revokeParentReply(UserPrincipal principal, Long id) {
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateMonth by id in parent"));
        evaluateMonth.setParentReplyDel(AppConstant.APP_TRUE);
        evaluateMonth.setParentReplyIdCreated(principal.getId());
        evaluateMonth.setParentReplyDatetime(LocalDateTime.now());
        evaluateMonth.setParentReplyCreatedBy(principal.getFullName());
        evaluateMonth.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluateMonth evaluateWeekSaved = evaluateMonthRepository.save(evaluateMonth);
        ListEvaluateMonthMobileResponse model = new ListEvaluateMonthMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        return model;
    }

    @Override
    public int parentView(UserPrincipal principal, Long id) {
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateMonth by id in parent"));
        evaluateMonth.setParentRead(AppConstant.APP_TRUE);
        evaluateMonthRepository.save(evaluateMonth);
        List<EvaluateMonth> dataList = evaluateMonthRepository.findParentUnreadOfYear(principal.getIdSchoolLogin(), principal.getIdKidLogin());
        return dataList.size();
    }

    /**
     * set model
     *
     * @param evaluateMonth
     * @param model
     */
    private void setModelData(Long idSchool, EvaluateMonth evaluateMonth, ListEvaluateMonthMobileResponse model) {
        model.setId(evaluateMonth.getId());
        model.setMonth(ConvertData.convertDateToMonth(evaluateMonth.getMonth(), evaluateMonth.getYear()));
        model.setContent(evaluateMonth.getContent());
        if (CollectionUtils.isNotEmpty(evaluateMonth.getEvaluateMonthFileList())) {
            model.setFileList(listMapper.mapList(evaluateMonth.getEvaluateMonthFileList(), AttachFileMobileResponse.class));
        }
        model.setParentRead(evaluateMonth.isParentRead());
        model.setParentReply(StringUtils.isNotBlank(evaluateMonth.getParentReplyContent()));
        model.setTeacherOrSchoolReply(StringUtils.isNotBlank(evaluateMonth.getTeacherReplyContent()) || StringUtils.isNotBlank(evaluateMonth.getSchoolReplyContent()));
        this.setMonthReply(idSchool, evaluateMonth, model);
    }

    /**
     * set reply
     *
     * @param x
     * @param model
     */
    private void setMonthReply(Long idSchool, EvaluateMonth x, ListEvaluateMonthMobileResponse model) {
        List<ReplyTypeMobileObject> replyDataList = new ArrayList<>();
        if (StringUtils.isNotBlank(x.getTeacherReplyContent())) {
            ReplyTypeMobileObject reply = new ReplyTypeMobileObject();
            MaUser maUser = maUserRepository.findById(x.getTeacherReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
//            reply.setAvatar(StringUtils.isNotBlank(maUser.getEmployee().getAvatar()) ? maUser.getEmployee().getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER);
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, x.getTeacherReplyIdCreated()));
            reply.setType(AppTypeConstant.TEACHER);
            reply.setFullName(x.getTeacherReplyCreatedBy());
            reply.setContent(x.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : x.getTeacherReplyContent());
            reply.setCreatedDate(x.getTeacherReplyDatetime());
            reply.setModifyStatus(x.isTeacherReplyModified());
            reply.setRevoke(x.isTeacherReplyDel());
            replyDataList.add(reply);
        }
        if (StringUtils.isNotBlank(x.getSchoolReplyContent())) {
            ReplyTypeMobileObject reply = new ReplyTypeMobileObject();
            MaUser maUser = maUserRepository.findById(x.getSchoolReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
//            reply.setAvatar(StringUtils.isNotBlank(maUser.getEmployee().getAvatar()) ? maUser.getEmployee().getAvatar() : AvatarDefaultConstant.AVATAR_SCHOOL);
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, x.getSchoolReplyIdCreated()));
            reply.setType(AppTypeConstant.SCHOOL);
            reply.setFullName(x.getSchoolReplyCreatedBy());
            reply.setContent(x.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : x.getSchoolReplyContent());
            reply.setCreatedDate(x.getSchoolReplyDatetime());
            reply.setModifyStatus(x.isSchoolReplyModified());
            reply.setRevoke(x.isSchoolReplyDel());
            replyDataList.add(reply);
        }
        replyDataList = replyDataList.stream().sorted(Comparator.comparing(ReplyTypeMobileObject::getCreatedDate)).collect(Collectors.toList());
        if (StringUtils.isNotBlank(x.getParentReplyContent())) {
            ReplyTypeMobileObject reply = new ReplyTypeMobileObject();
            MaUser maUser = maUserRepository.findById(x.getParentReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
//            reply.setAvatar(StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT);
            reply.setAvatar(AvatarUtils.getAvatarParent(x.getParentReplyIdCreated()));
            reply.setType(AppTypeConstant.PARENT);
            reply.setFullName(x.getParentReplyCreatedBy());
            reply.setContent(x.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : x.getParentReplyContent());
            reply.setCreatedDate(x.getParentReplyDatetime());
            reply.setModifyStatus(x.isParentReplyModified());
            reply.setRevoke(x.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.setReplyList(replyDataList);
    }
}
