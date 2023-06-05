package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.EvaluatePeriodic;
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
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluatePeriodicMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.ListEvaluatePeriodicMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluatePeriodicMobileService;
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
public class EvaluatePeriodicMobileServiceImpl implements EvaluatePeriodicMobileService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluatePeriodicRepository evaluatePeriodicRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public EvaluatePeriodicMobileResponse findEvaluatePeriodic(UserPrincipal principal, Long id, Pageable pageable) {
        EvaluatePeriodicMobileResponse data = new EvaluatePeriodicMobileResponse();
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findParentdforKid(principal.getIdSchoolLogin(), principal.getIdKidLogin(), id, pageable);
        List<ListEvaluatePeriodicMobileResponse> dataList = new ArrayList<>();
        evaluatePeriodicList.forEach(x -> {
            ListEvaluatePeriodicMobileResponse model = new ListEvaluatePeriodicMobileResponse();
            this.setModelData(principal.getIdSchoolLogin(), x, model);
            dataList.add(model);
        });
        data.setDataList(dataList);
        long countAll = evaluatePeriodicRepository.countParentUnread(principal.getIdSchoolLogin(), principal.getIdKidLogin(), id);
        boolean checkLastPage = countAll <= AppConstant.MAX_PAGE_ITEM;
        data.setLastPage(checkLastPage);
        return data;
    }

    @Transactional
    @Override
    public ListEvaluatePeriodicMobileResponse createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException {
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findEvaluatePeriodicByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluatePeriodic by id in parent"));
        evaluatePeriodic.setParentReplyIdCreated(principal.getId());
        evaluatePeriodic.setParentReplyDatetime(LocalDateTime.now());
        evaluatePeriodic.setParentReplyCreatedBy(principal.getFullName());
        evaluatePeriodic.setParentReplyContent(content);
        evaluatePeriodic.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluatePeriodic evaluateWeekSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
        ListEvaluatePeriodicMobileResponse model = new ListEvaluatePeriodicMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);

        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(51L, evaluatePeriodic.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        firebaseFunctionService.sendTeacherByKids(51L, evaluatePeriodic.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        return model;
    }

    @Override
    public ListEvaluatePeriodicMobileResponse updateParentReply(UserPrincipal principal, Long id, String content) {
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findEvaluatePeriodicByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found EvaluatePeriodic by id in parent"));
        evaluatePeriodic.setParentReplyIdCreated(principal.getId());
        evaluatePeriodic.setParentReplyDatetime(LocalDateTime.now());
        evaluatePeriodic.setParentReplyCreatedBy(principal.getFullName());
        evaluatePeriodic.setParentReplyContent(content);
        evaluatePeriodic.setParentReplyModified(AppConstant.APP_TRUE);
        evaluatePeriodic.setParentReplyDel(AppConstant.APP_FALSE);
        evaluatePeriodic.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluatePeriodic evaluateWeekSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
        ListEvaluatePeriodicMobileResponse model = new ListEvaluatePeriodicMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        return model;
    }

    @Override
    public ListEvaluatePeriodicMobileResponse revokeParentReply(UserPrincipal principal, Long id) {
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findEvaluatePeriodicByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluatePeriodic by id in parent"));
        evaluatePeriodic.setParentReplyDel(AppConstant.APP_TRUE);
        evaluatePeriodic.setParentReplyIdCreated(principal.getId());
        evaluatePeriodic.setParentReplyDatetime(LocalDateTime.now());
        evaluatePeriodic.setParentReplyCreatedBy(principal.getFullName());
        evaluatePeriodic.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluatePeriodic evaluateWeekSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
        ListEvaluatePeriodicMobileResponse model = new ListEvaluatePeriodicMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        return model;
    }

    @Override
    public int parentView(UserPrincipal principal, Long id) {
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findEvaluatePeriodicByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluatePeriodic by id in parent"));
        evaluatePeriodic.setParentRead(AppConstant.APP_TRUE);
        evaluatePeriodicRepository.save(evaluatePeriodic);
        List<EvaluatePeriodic> dataList = evaluatePeriodicRepository.findParentUnreadOfYear(principal.getIdSchoolLogin(), principal.getIdKidLogin());
        return dataList.size();
    }

    /**
     * set model
     *
     * @param evaluatePeriodic
     * @param model
     */
    private void setModelData(Long idSchool, EvaluatePeriodic evaluatePeriodic, ListEvaluatePeriodicMobileResponse model) {
        model.setId(evaluatePeriodic.getId());
        model.setPeriodic(ConvertData.convertDateToPeriodic(evaluatePeriodic.getDate()));
        model.setContent(evaluatePeriodic.getContent());
        if (CollectionUtils.isNotEmpty(evaluatePeriodic.getEvaluatePeriodicFileList())) {
            model.setFileList(listMapper.mapList(evaluatePeriodic.getEvaluatePeriodicFileList(), AttachFileMobileResponse.class));
        }
        model.setParentRead(evaluatePeriodic.isParentRead());
        model.setParentReply(StringUtils.isNotBlank(evaluatePeriodic.getParentReplyContent()));
        model.setTeacherOrSchoolReply(StringUtils.isNotBlank(evaluatePeriodic.getTeacherReplyContent()) || StringUtils.isNotBlank(evaluatePeriodic.getSchoolReplyContent()));
        this.setPeriodicReply(idSchool, evaluatePeriodic, model);
    }

    /**
     * set reply
     *
     * @param x
     * @param model
     */
    private void setPeriodicReply(Long idSchool, EvaluatePeriodic x, ListEvaluatePeriodicMobileResponse model) {
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
