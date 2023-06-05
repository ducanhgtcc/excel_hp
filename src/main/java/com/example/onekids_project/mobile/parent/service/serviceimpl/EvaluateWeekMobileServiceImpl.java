package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateWeekMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.ListEvaluateWeekMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluateWeekMobileService;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
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
public class EvaluateWeekMobileServiceImpl implements EvaluateWeekMobileService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

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
    public EvaluateWeekMobileResponse findEvaluateWeek(UserPrincipal principal, Long id, Pageable pageable) {
        EvaluateWeekMobileResponse data = new EvaluateWeekMobileResponse();
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findParentdforKid(principal.getIdSchoolLogin(), principal.getIdKidLogin(), id, pageable);
        List<ListEvaluateWeekMobileResponse> dataList = new ArrayList<>();
        evaluateWeekList.forEach(x -> {
            ListEvaluateWeekMobileResponse model = new ListEvaluateWeekMobileResponse();
            this.setModelData(principal.getIdSchoolLogin(), x, model);
            dataList.add(model);
        });
        data.setDataList(dataList);
        long countAll = evaluateWeekRepository.countParentUnread(principal.getIdSchoolLogin(), principal.getIdKidLogin(), id);
        boolean checkLastPage = countAll <= AppConstant.MAX_PAGE_ITEM;
        data.setLastPage(checkLastPage);
        return data;
    }

    @Transactional
    @Override
    public ListEvaluateWeekMobileResponse createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException {
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateWeek by id in parent"));
        evaluateWeek.setParentReplyIdCreated(principal.getId());
        evaluateWeek.setParentReplyDatetime(LocalDateTime.now());
        evaluateWeek.setParentReplyCreatedBy(principal.getFullName());
        evaluateWeek.setParentReplyContent(content);
        evaluateWeek.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
        ListEvaluateWeekMobileResponse model = new ListEvaluateWeekMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(43L, evaluateWeek.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        firebaseFunctionService.sendTeacherByKids(43L, evaluateWeek.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        return model;
    }


    @Override
    public ListEvaluateWeekMobileResponse updateParentReply(UserPrincipal principal, Long id, String content) {
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found EvaluateWeek by id in parent"));
        evaluateWeek.setParentReplyIdCreated(principal.getId());
        evaluateWeek.setParentReplyDatetime(LocalDateTime.now());
        evaluateWeek.setParentReplyCreatedBy(principal.getFullName());
        evaluateWeek.setParentReplyContent(content);
        evaluateWeek.setParentReplyModified(AppConstant.APP_TRUE);
        evaluateWeek.setParentReplyDel(AppConstant.APP_FALSE);
        evaluateWeek.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
        ListEvaluateWeekMobileResponse model = new ListEvaluateWeekMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        return model;
    }

    @Override
    public ListEvaluateWeekMobileResponse revokeParentReply(UserPrincipal principal, Long id) {
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateWeek by id in parent"));
        evaluateWeek.setParentReplyDel(AppConstant.APP_TRUE);
        evaluateWeek.setParentReplyIdCreated(principal.getId());
        evaluateWeek.setParentReplyDatetime(LocalDateTime.now());
        evaluateWeek.setParentReplyCreatedBy(principal.getFullName());
        evaluateWeek.setSchoolReadReply(AppConstant.APP_FALSE);
        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
        ListEvaluateWeekMobileResponse model = new ListEvaluateWeekMobileResponse();
        this.setModelData(principal.getIdSchoolLogin(), evaluateWeekSaved, model);
        return model;
    }

    @Override
    public int parentView(UserPrincipal principal, Long id) {
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateWeek by id in parent"));
        evaluateWeek.setParentRead(AppConstant.APP_TRUE);
        evaluateWeekRepository.save(evaluateWeek);
        List<EvaluateWeek> dataList = evaluateWeekRepository.findParentUnreadOfYear(principal.getIdSchoolLogin(), principal.getIdKidLogin());
        return dataList.size();
    }

    /**
     * set model
     *
     * @param evaluateWeek
     * @param model
     */
    private void setModelData(Long idSchool, EvaluateWeek evaluateWeek, ListEvaluateWeekMobileResponse model) {
        model.setId(evaluateWeek.getId());
        model.setWeek(ConvertData.convertDateToWeek(evaluateWeek.getWeek(), evaluateWeek.getDate()));
        model.setContent(evaluateWeek.getContent());
        if (CollectionUtils.isNotEmpty(evaluateWeek.getEvaluateWeekFileList())) {
            model.setFileList(listMapper.mapList(evaluateWeek.getEvaluateWeekFileList(), AttachFileMobileResponse.class));
        }
        model.setParentRead(evaluateWeek.isParentRead());
        model.setParentReply(StringUtils.isNotBlank(evaluateWeek.getParentReplyContent()));
        model.setTeacherOrSchoolReply(StringUtils.isNotBlank(evaluateWeek.getTeacherReplyContent()) || StringUtils.isNotBlank(evaluateWeek.getSchoolReplyContent()));
        this.setReply(idSchool, evaluateWeek, model);
    }

    /**
     * set reply
     *
     * @param x
     * @param model
     */
    private void setReply(Long idSchool, EvaluateWeek x, ListEvaluateWeekMobileResponse model) {
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
