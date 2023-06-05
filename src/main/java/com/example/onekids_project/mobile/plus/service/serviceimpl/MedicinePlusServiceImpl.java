package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.medicine.SearchMedicinePlusRequest;
import com.example.onekids_project.mobile.plus.response.medicine.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.MedicinePlusService;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.MedicineRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FirebaseUtils;
import com.example.onekids_project.util.StringDataUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicinePlusServiceImpl implements MedicinePlusService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMedicinePlusResponse searchMedicinePlus(UserPrincipal principal, SearchMedicinePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<Medicine> medicinePlusList = medicineRepository.searchMedicineForPlus(idSchool, request);
        ListMedicinePlusResponse listMedicinePlusResponse = new ListMedicinePlusResponse();
        List<MedicinePlusResponse> medicinePlusResponseList = new ArrayList<>();
        medicinePlusList.forEach(x -> {
            MedicinePlusResponse model = new MedicinePlusResponse();
            model.setFullName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setId(x.getId());
            String content = StringDataUtils.getSubStringLarge(x.getMedicineContent());
            model.setContent(content);
            int replyNumber = 0;
            if (StringUtils.isNotBlank(x.getConfirmContent())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getSchoolReply())) {
                replyNumber++;
            }
            if (StringUtils.isNotBlank(x.getTeacherReply())) {
                replyNumber++;
            }
            model.setDateSick(ConvertData.convertDateToString(x.getFromDate(), x.getToDate()));
            model.setDiseaseName(x.getDiseaseName());
            model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
            model.setReplyNumber(replyNumber);
            model.setPictureNumber(x.getMedicineAttachFileList().size());
            model.setTeacherUnread(x.isTeacherUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            medicinePlusResponseList.add(model);
        });
        boolean lastPage = medicinePlusList.size() < MobileConstant.MAX_PAGE_ITEM;
        listMedicinePlusResponse.setDataList(medicinePlusResponseList);
        listMedicinePlusResponse.setLastPage(lastPage);
        return listMedicinePlusResponse;
    }

    @Override
    public MedicinePlusDetailResponse findDetailMedicinePlus(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        MedicinePlusDetailResponse model = new MedicinePlusDetailResponse();
        Kids kids = kidsRepository.findById(medicine.getKids().getId()).orElseThrow();
        MaUser maUser = maUserRepository.findById(medicine.getIdCreated()).orElseThrow();
        MaClass maClass = maClassRepository.findById(medicine.getIdClass()).orElseThrow();
        model.setKidName(kids.getFullName());
        model.setClassName(maClass.getClassName());
        model.setContent(medicine.getMedicineContent());
        model.setParentName(maUser.getFullName());
        model.setDiseaseName(medicine.getDiseaseName());
        model.setDateSick(medicine.getFromDate().isEqual(medicine.getToDate()) ? ConvertData.convertDateToStringOne(medicine.getFromDate()) : ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
        model.setConfirmStatus(medicine.isConfirmStatus());
        model.setCheckSchoolReply(medicine.getIdSchoolReply() != null);
        model.setAvartarParent(ConvertData.getAvatarUserSchool(maUser));
        model.setAvatarkid(ConvertData.getAvatarKid(kids));
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()));
        model.setContent(medicine.getMedicineContent());
        model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
        medicine.setTeacherUnread(AppConstant.APP_TRUE);
        medicineRepository.save(medicine);
        this.setReply(principal, medicine, model);
        return model;
    }

    @Transactional
    @Override
    public MedicinePlusConfirmResponse medicinePlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        MedicinePlusConfirmResponse model = new MedicinePlusConfirmResponse();
        Medicine medicine = medicineRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        medicine.setConfirmStatus(AppConstant.APP_TRUE);
        medicine.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_SCHOOL);
        medicine.setConfirmdate(LocalDateTime.now());
        medicine.setIdConfirmType(principal.getId());
        medicine.setConfirmType(AppTypeConstant.SCHOOL);
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        this.setReplyConfirmA(principal, medicine, model);

        //gửi firebase
        firebaseFunctionService.sendParentByPlusNoContent(29L, medicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE);
        return model;
    }


    @Transactional
    @Override
    public MedicinePlusSendReplyResponse sendPlusReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        MedicinePlusSendReplyResponse model = new MedicinePlusSendReplyResponse();
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(updatePlusSendReplyRequest.getId()).orElseThrow();
        if (medicine.getIdSchoolReply() == null) {
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(31L, medicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE, updatePlusSendReplyRequest.getSchoolReply());
        }
        if (StringUtils.isBlank(medicine.getConfirmContent())) {
            medicine.setConfirmStatus(AppConstant.APP_TRUE);
            medicine.setConfirmType(AppTypeConstant.SCHOOL);
            medicine.setConfirmdate(LocalDateTime.now());
            medicine.setIdConfirmType(principal.getId());
        }
        medicine.setIdSchoolReply(principal.getId());
        medicine.setSchoolTimeReply(LocalDateTime.now());
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicine.setSchoolModifyStatus(medicine.getSchoolReply() == null ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
        model.setId(updatePlusSendReplyRequest.getId());
        model.setSchoolReply(updatePlusSendReplyRequest.getSchoolReply());
        medicine.setSchoolReply(updatePlusSendReplyRequest.getSchoolReply());
        medicine.setTeacherUnread(AppConstant.APP_TRUE);
        medicine.setSchoolReplyDel(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        this.setReplySend(principal, medicine, model);


        return model;
    }

    @Override
    public MedicinePlusRevokeResponse sendRevoke(Long idSchoolLogin, UserPrincipal principal, UpdatePlusRevokeRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        MedicinePlusRevokeResponse model = new MedicinePlusRevokeResponse();
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (request.getKeyType().equals(MobileConstant.TYPE_TEACHER) && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            medicine.setTeacherReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokeTeacher(idSchool, medicine, model);
        } else if (request.getKeyType().equals(MobileConstant.TYPE_SCHOOL) && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            medicine.setSchoolReplyDel(AppConstant.APP_TRUE);
            this.setReplyRevokePlus(principal, medicine, model);
        }
        medicine.setDefaultContentDel(ParentDairyConstant.CONTENT_DEL);
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        return model;
    }

    //endfirebase
    private void setReplyRevokeTeacher(Long idSchool, Medicine medicine, MedicinePlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_TEACHER);
        replyMobileDateObject.setStatusDel(AppConstant.APP_TRUE);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReplyRevokePlus(UserPrincipal principal, Medicine medicine, MedicinePlusRevokeResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(medicine.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_TRUE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, Medicine medicine, MedicinePlusDetailResponse
            model) {
        Long idSchool = principal.getIdSchoolLogin();
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        //khi có Xác nhận
        if (StringUtils.isNotBlank(medicine.getConfirmContent())) {
            replyMobileDateObjectList.add(this.setReplyConfirm(idSchool, medicine));
        }
        // nhà trường phản hồi
        if (StringUtils.isNotBlank(medicine.getSchoolReply())) {
            replyMobileDateObjectList.add(this.setReplySchool(principal, medicine));
        }
        // giáo viên phản hồi
        if (StringUtils.isNotBlank(medicine.getTeacherReply())) {
            replyMobileDateObjectList.add(this.setReplyTeacher(idSchool, medicine));
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private ReplyMobilePlusObject setReplyConfirm(Long idSchool, Medicine medicine) {
        MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setKeyType("");
        replyMobileDateObject.setContent(medicine.getConfirmContent());
        replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
        replyMobileDateObject.setSortDate(medicine.getConfirmdate());
        return replyMobileDateObject;
    }

    private void setReplyConfirmA(UserPrincipal principal, Medicine medicine, MedicinePlusConfirmResponse model) {
        List<ReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setKeyType("Confirm");
        replyMobileDateObject.setContent(medicine.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private ReplyMobilePlusObject setReplySchool(UserPrincipal principal, Medicine medicine) {
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(medicine.getIdSchoolReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(medicine.isSchoolReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : medicine.getSchoolReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(medicine.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObject.setSortDate(medicine.getSchoolTimeReply());
        replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        return replyMobileDateObject;

    }

    private ReplyMobilePlusObject setReplyTeacher(Long idSchool, Medicine medicine) {
        ReplyMobilePlusObject replyMobileDateObject = new ReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(medicine.isTeacherReplyDel() == AppConstant.APP_TRUE ? ParentDairyConstant.CONTENT_DEL : medicine.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
        replyMobileDateObject.setSchoolMoidifystatus(AppConstant.APP_TRUE);
        replyMobileDateObject.setSortDate(medicine.getTeacherTimeReply());
        replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_TEACHER);
        return replyMobileDateObject;

    }

    private void setReplySend(UserPrincipal principal, Medicine medicine, MedicinePlusSendReplyResponse model) {
        List<SendReplyMobilePlusObject> replyMobileDateObjectList = new ArrayList<>();
        SendReplyMobilePlusObject replyMobileDateObject = new SendReplyMobilePlusObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow();
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(maUser.getFullName());
        replyMobileDateObject.setContent(medicine.getSchoolReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getSchoolTimeReply()));
        replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
        replyMobileDateObject.setKeyType(MobileConstant.TYPE_SCHOOL);
        replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
        replyMobileDateObject.setSchoolMoidifystatus(principal.getId().equals(medicine.getIdSchoolReply()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(SendReplyMobilePlusObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }
}
