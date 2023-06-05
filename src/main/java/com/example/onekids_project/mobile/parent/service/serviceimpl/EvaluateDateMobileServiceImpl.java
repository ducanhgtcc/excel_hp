package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EvaluateConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.entity.kids.EvaluatePeriodic;
import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.evaluate.DateOfMonthMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateDateMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateStatisticalMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.EvaluateDateMobileService;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.ReplyTypeMobileObject;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluateDateMobileServiceImpl implements EvaluateDateMobileService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private EvaluatePeriodicRepository evaluatePeriodicRepository;

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
    public EvaluateStatisticalMobileResponse evaluateStatisticalUnread(UserPrincipal principal) {
        EvaluateStatisticalMobileResponse model = new EvaluateStatisticalMobileResponse();
        LocalDate nowDate = LocalDate.now();
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        Integer year = nowDate.getYear();
        int date = getTatalParentUnreadOfMonth(principal, nowDate);
        int week = getTotalEvaluateWeekUnread(idSchool, idKid);
        int month = getTotalEvaluateMonthUnread(idSchool, idKid);
        int periodic = getTotalEvaluatePeriodicUnread(idSchool, idKid);
        model.setDate(date);
        model.setWeek(week);
        model.setMonth(month);
        model.setPeriodic(periodic);
        return model;
    }


    @Override
    public EvaluateDateMobileResponse findEvaluateByIdKids(UserPrincipal principal, LocalDate localDate) {
        Long idSchool = principal.getIdSchoolLogin();
        Optional<EvaluateDate> evaluateDateOptional = Optional.empty();
        if (localDate == null) {
            evaluateDateOptional = evaluateDateRepository.findEvaluateDateKidHas(principal.getIdSchoolLogin(), principal.getIdKidLogin());
        }
        if (evaluateDateOptional.isEmpty()) {
            localDate = localDate == null ? LocalDate.now() : localDate;
            evaluateDateOptional = evaluateDateRepository.findEvaluateDateKidDateMobile(principal.getIdSchoolLogin(), principal.getIdKidLogin(), localDate);
        }
        if (evaluateDateOptional.isEmpty()) {
            return null;
        }
        EvaluateDate evaluateDate = evaluateDateOptional.get();
        if (StringUtils.isBlank(evaluateDate.getLearnContent()) && StringUtils.isBlank(evaluateDate.getEatContent()) && StringUtils.isBlank(evaluateDate.getSleepContent()) && StringUtils.isBlank(evaluateDate.getSanitaryContent()) && StringUtils.isBlank(evaluateDate.getHealtContent()) && StringUtils.isBlank(evaluateDate.getCommonContent()) && CollectionUtils.isEmpty(evaluateDate.getEvaluateAttachFileList())) {
            return null;
        }
        EvaluateDateMobileResponse model = new EvaluateDateMobileResponse();
        model.setId(evaluateDate.getId());
        model.setDate(evaluateDate.getDate());
        model.setLearnContent(evaluateDate.getLearnContent());
        model.setEatContent(evaluateDate.getEatContent());
        model.setSleepContent(evaluateDate.getSleepContent());
        model.setSanitaryContent(evaluateDate.getSanitaryContent());
        model.setHealtContent(evaluateDate.getHealtContent());
        model.setCommonContent(evaluateDate.getCommonContent());
        if (CollectionUtils.isNotEmpty(evaluateDate.getEvaluateAttachFileList())) {
            model.setFileList(listMapper.mapList(evaluateDate.getEvaluateAttachFileList(), AttachFileMobileResponse.class));
        }
        this.setEvaluateDateReply(idSchool, evaluateDate, model);
        this.updateParentRead(evaluateDate);
        return model;
    }

    @Override
    public int getTatalParentUnreadOfMonth(UserPrincipal principal, LocalDate localDate) {
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.totalEvaluateDateMonthMobile(principal.getIdSchoolLogin(), principal.getIdKidLogin(), localDate.getMonthValue(), localDate.getYear());
        int count = (int) evaluateDateList.stream().filter(x -> !x.isParentRead() && (StringUtils.isNotBlank(x.getLearnContent()) || StringUtils.isNotBlank(x.getEatContent()) || StringUtils.isNotBlank(x.getSleepContent()) || StringUtils.isNotBlank(x.getSanitaryContent()) || StringUtils.isNotBlank(x.getHealtContent()) || StringUtils.isNotBlank(x.getCommonContent()) || CollectionUtils.isNotEmpty(x.getEvaluateAttachFileList()))).count();
        return count;
    }

    @Override
    public List<DateOfMonthMobileResponse> totalEvaluateDateOfMonth(UserPrincipal principal, LocalDate localDate) {
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.totalEvaluateDateMonthMobile(principal.getIdSchoolLogin(), principal.getIdKidLogin(), localDate.getMonthValue(), localDate.getYear());
        List<DateOfMonthMobileResponse> dataList = new ArrayList<>();
        evaluateDateList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getLearnContent()) || StringUtils.isNotBlank(x.getEatContent()) || StringUtils.isNotBlank(x.getSleepContent()) || StringUtils.isNotBlank(x.getSanitaryContent()) || StringUtils.isNotBlank(x.getHealtContent()) || StringUtils.isNotBlank(x.getCommonContent()) || CollectionUtils.isNotEmpty(x.getEvaluateAttachFileList())) {
                DateOfMonthMobileResponse object = new DateOfMonthMobileResponse();
                object.setDate(x.getDate().getDayOfMonth());
                object.setStatus(x.isParentRead());
                dataList.add(object);
            }
        });
        return dataList;
    }

    @Transactional
    @Override
    public boolean createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException {
        EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateDate by id in parent"));
        evaluateDate.setParentReplyIdCreated(principal.getId());
        evaluateDate.setParentReplyDatetime(LocalDateTime.now());
        evaluateDate.setParentReplyCreatedBy(principal.getFullName());
        evaluateDate.setParentReplyContent(content);
        evaluateDate.setSchoolReadReply(AppConstant.APP_FALSE);
        evaluateDateRepository.save(evaluateDate);

        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(39L, evaluateDate.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        firebaseFunctionService.sendTeacherByKids(39L, evaluateDate.getKids(), content, FirebaseConstant.CATEGORY_EVALUATE);
        return true;
    }


    @Override
    public boolean updateParentReply(UserPrincipal principal, Long id, String content) {
        EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateDate by id in parent"));
        evaluateDate.setParentReplyIdCreated(principal.getId());
        evaluateDate.setParentReplyDatetime(LocalDateTime.now());
        evaluateDate.setParentReplyCreatedBy(principal.getFullName());
        evaluateDate.setParentReplyContent(content);
        evaluateDate.setParentReplyModified(AppConstant.APP_TRUE);
        evaluateDate.setParentReplyDel(AppConstant.APP_FALSE);
        evaluateDate.setSchoolReadReply(AppConstant.APP_FALSE);
        evaluateDateRepository.save(evaluateDate);
        return true;
    }

    @Override
    public boolean revokeParentReply(UserPrincipal principal, Long id) {
        EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateDate by id in parent"));
        evaluateDate.setParentReplyDel(AppConstant.APP_TRUE);
        evaluateDate.setParentReplyIdCreated(principal.getId());
        evaluateDate.setParentReplyDatetime(LocalDateTime.now());
        evaluateDate.setParentReplyCreatedBy(principal.getFullName());
        evaluateDate.setSchoolReadReply(AppConstant.APP_FALSE);
        evaluateDateRepository.save(evaluateDate);
        return true;
    }

    /**
     * set reply
     *
     * @param evaluateDate
     * @param model
     */
    private void setEvaluateDateReply(Long idSchool, EvaluateDate evaluateDate, EvaluateDateMobileResponse model) {
        List<ReplyTypeMobileObject> replyDataList = new ArrayList<>();
        if (StringUtils.isNotBlank(evaluateDate.getTeacherReplyContent())) {
            ReplyTypeMobileObject reply = new ReplyTypeMobileObject();
            MaUser maUser = maUserRepository.findById(evaluateDate.getTeacherReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id of teacher"));
            reply.setType(AppTypeConstant.TEACHER);
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setFullName(evaluateDate.getTeacherReplyCreatedBy());
            reply.setContent(evaluateDate.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluateDate.getTeacherReplyContent());
            reply.setCreatedDate(evaluateDate.getTeacherReplyDatetime());
            reply.setModifyStatus(evaluateDate.isTeacherReplyModified());
            replyDataList.add(reply);
        }
        if (StringUtils.isNotBlank(evaluateDate.getSchoolReplyContent())) {
            ReplyTypeMobileObject reply = new ReplyTypeMobileObject();
            MaUser maUser = maUserRepository.findById(evaluateDate.getSchoolReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id of school"));
            reply.setType(AppTypeConstant.SCHOOL);
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setFullName(evaluateDate.getSchoolReplyCreatedBy());
            reply.setContent(evaluateDate.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : evaluateDate.getSchoolReplyContent());
            reply.setCreatedDate(evaluateDate.getSchoolReplyDatetime());
            reply.setModifyStatus(evaluateDate.isSchoolReplyModified());
            replyDataList.add(reply);
        }
        replyDataList = replyDataList.stream().sorted(Comparator.comparing(ReplyTypeMobileObject::getCreatedDate)).collect(Collectors.toList());
        if (StringUtils.isNotBlank(evaluateDate.getParentReplyContent())) {
            ReplyTypeMobileObject reply = new ReplyTypeMobileObject();
            MaUser maUser = maUserRepository.findById(evaluateDate.getParentReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id of parent"));
            reply.setType(AppTypeConstant.PARENT);
            reply.setAvatar(AvatarUtils.getAvatarParent(evaluateDate.getParentReplyIdCreated()));
            reply.setFullName(evaluateDate.getParentReplyCreatedBy());
            reply.setContent(evaluateDate.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : evaluateDate.getParentReplyContent());
            reply.setCreatedDate(evaluateDate.getParentReplyDatetime());
            reply.setModifyStatus(evaluateDate.isParentReplyModified());
            reply.setRevoke(evaluateDate.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.getReplyList().addAll(replyDataList);
    }

    /**
     * update parent read
     *
     * @param evaluateDate
     */
    private void updateParentRead(EvaluateDate evaluateDate) {
        if (!evaluateDate.isParentRead()) {
            evaluateDate.setParentRead(AppConstant.APP_TRUE);
            evaluateDateRepository.save(evaluateDate);
        }
    }

    private int getTotalEvaluateWeekUnread(Long idSchool, Long idKid) {
        List<EvaluateWeek> dataList = evaluateWeekRepository.findParentUnreadOfYear(idSchool, idKid);
        return dataList.size();
    }

    private int getTotalEvaluateMonthUnread(Long idSchool, Long idKid) {
        List<EvaluateMonth> dataList = evaluateMonthRepository.findParentUnreadOfYear(idSchool, idKid);
        return dataList.size();
    }

    private int getTotalEvaluatePeriodicUnread(Long idSchool, Long idKid) {
        List<EvaluatePeriodic> dataList = evaluatePeriodicRepository.findParentUnreadOfYear(idSchool, idKid);
        return dataList.size();
    }
}
