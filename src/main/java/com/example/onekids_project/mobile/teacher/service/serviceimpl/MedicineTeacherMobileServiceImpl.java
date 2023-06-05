package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.teacher.request.medicine.SearchMedicineTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.UpdateTeacherReplyMedcRequest;
import com.example.onekids_project.mobile.teacher.response.medicine.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.MedicineTeacherMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineTeacherMobileServiceImpl implements MedicineTeacherMobileService {

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
    private ModelMapper modelMapper;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMedicineTeacherResponse searchMedicine(UserPrincipal principal, SearchMedicineTeacherRequest searchMedicineTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        List<Medicine> medicineTeacherList = medicineRepository.searchMedicineForTeacher(idSchool, idClass, searchMedicineTeacherRequest);
        ListMedicineTeacherResponse listMedicineTeacherResponse = new ListMedicineTeacherResponse();
        List<MedicineTeacherResponse> medicineTeacherResponseList = new ArrayList<>();
        medicineTeacherList.forEach(x -> {
            MedicineTeacherResponse model = new MedicineTeacherResponse();
            model.setFullName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setId(x.getId());
            String content = x.getMedicineContent().length() < 100 ? x.getMedicineContent() : x.getMedicineContent().substring(0, 100);
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
            medicineTeacherResponseList.add(model);
        });
        boolean lastPage = medicineTeacherList.size() < MobileConstant.MAX_PAGE_ITEM;
        listMedicineTeacherResponse.setDataList(medicineTeacherResponseList);
        listMedicineTeacherResponse.setLastPage(lastPage);
        return listMedicineTeacherResponse;
    }

    @Override
    public MedicineTeacherDetailResponse findDetailMedicineTeacher(UserPrincipal principal, Long id) {
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found medicine by id"));
        MedicineTeacherDetailResponse model = new MedicineTeacherDetailResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(medicine.getKids().getId()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(medicine.getIdCreated()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(medicine.getIdClass()).orElseThrow(() -> new NotFoundException("not found id class"));
        model.setFullName(kids.getFullName());
        model.setClassName(maClass.getClassName());
        model.setContent(medicine.getMedicineContent());
        model.setParentName(maUser.getFullName());
        model.setDiseaseName(medicine.getDiseaseName());
        model.setDateSick(medicine.getFromDate().isEqual(medicine.getToDate()) ? ConvertData.convertDateToStringOne(medicine.getFromDate()) : ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
        model.setConfirmStatus(medicine.isConfirmStatus());
        model.setCheckTeacherReply(medicine.getIdTeacherReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        if (medicine.isSchoolReplyDel() == AppConstant.APP_TRUE && medicine.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()));
            model.setContent(medicine.getMedicineContent());
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply(principal, medicine, model); // nha truong  thu hoi
        } else if (medicine.isSchoolReplyDel() == AppConstant.APP_TRUE && medicine.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()));
            model.setContent(medicine.getMedicineContent());
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply2(principal, medicine, model);
        } else if (medicine.isSchoolReplyDel() == AppConstant.APP_FALSE && medicine.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()));
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply3(principal, medicine, model);
        } else if (medicine.isSchoolReplyDel() == AppConstant.APP_FALSE && medicine.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvartarParent(avatarCreate);
            model.setAvatarkid(ConvertData.getAvatarKid(kids));
            model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getCreatedDate()));
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply1(principal, medicine, model);
        }
        medicine.setTeacherUnread(AppConstant.APP_TRUE);
        medicineRepository.save(medicine);
        return model;
    }

    @Override
    public MedicineTeacherRevokeResponse medicineTeacherRevoke(UserPrincipal principal, Long id) {
        MedicineTeacherRevokeResponse model = new MedicineTeacherRevokeResponse();
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        medicine.setTeacherReplyDel(AppConstant.APP_TRUE);
        medicine.setDefaultContentDel(ParentDairyConstant.CONTENT_DEL);
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        this.setReplyRevoke(principal, medicine, model);
        return model;
    }

    private void setReplyRevoke(UserPrincipal principal, Medicine medicine, MedicineTeacherRevokeResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(medicine.getDefaultContentDel());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getLastModifieDate()));
        replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
        if (medicine.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    @Transactional
    @Override
    public MedicineTeacherSendReplyResponse sendTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyMedcRequest updateTeacherReplyMedcRequest) throws FirebaseMessagingException {
        MedicineTeacherSendReplyResponse model = new MedicineTeacherSendReplyResponse();
        Optional<Medicine> medicineOptional = medicineRepository.findByIdAndDelActiveTrue(updateTeacherReplyMedcRequest.getId());
        if (medicineOptional.isEmpty()) {
            return null;
        }
        Long id = updateTeacherReplyMedcRequest.getId();
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        Medicine oldMedicine = medicineOptional.get();
        modelMapper.map(updateTeacherReplyMedcRequest, oldMedicine);
        if (oldMedicine.getIdConfirmType() == null) {
            oldMedicine.setConfirmStatus(AppConstant.APP_TRUE);
            oldMedicine.setConfirmType(AppTypeConstant.TEACHER);
            oldMedicine.setConfirmdate(LocalDateTime.now());
            oldMedicine.setIdConfirmType(principal.getId());
            oldMedicine.setTeacherReply(updateTeacherReplyMedcRequest.getTeacherReply());
            oldMedicine.setTeacherTimeReply(LocalDateTime.now());
            oldMedicine.setIdTeacherReply(principal.getId());
            oldMedicine.setParentUnread(AppConstant.APP_FALSE);
        } else {
            oldMedicine.setTeacherTimeReply(LocalDateTime.now());
            oldMedicine.setIdTeacherReply(principal.getId());
            oldMedicine.setTeacherModifyStatus(AppConstant.APP_FALSE);
            oldMedicine.setParentUnread(AppConstant.APP_FALSE);
        }
        model.setId(updateTeacherReplyMedcRequest.getId());
        model.setTeacherReply(updateTeacherReplyMedcRequest.getTeacherReply());
        Medicine newMedicine = medicineRepository.save(oldMedicine);
        this.setReplySend(principal, medicine, model);
        MedicineTeacherSendReplyResponse medicineTeacherSendReplyResponse = modelMapper.map(newMedicine, MedicineTeacherSendReplyResponse.class);
        //gửi firebase
        firebaseFunctionService.sendParentByTeacher(30L, medicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE, updateTeacherReplyMedcRequest.getTeacherReply());
        return model;
    }

    private void setReplySend(UserPrincipal principal, Medicine medicine, MedicineTeacherSendReplyResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(medicine.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
        replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
        if (medicine.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    @Transactional
    @Override
    public MedicineTeacherConfirmResponse medicineTeacherConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        MedicineTeacherConfirmResponse model = new MedicineTeacherConfirmResponse();
        Medicine medicine = medicineRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        medicine.setConfirmStatus(AppConstant.APP_TRUE);
        medicine.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
        medicine.setConfirmdate(LocalDateTime.now());
        medicine.setIdConfirmType(principal.getId());
        medicine.setConfirmType(AppTypeConstant.TEACHER);
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        this.setReplyConfirm(principal, medicine, model);
        //    firebase
        firebaseFunctionService.sendParentByTeacherNoContent(28L, medicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE, UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal).getFullName());
        return model;
    }

    //    fireBasse
    private void sendFireBase(Kids kids, String title, String content, UserPrincipal principal) throws FirebaseMessagingException {
        if (kids.getParent() != null) {
            List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
            if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                NotifyRequest notifyRequest = new NotifyRequest();
                notifyRequest.setBody(content);
                notifyRequest.setTitle(title);
                FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.MEDICINE, notifyRequest, kids.getId().toString());
            }
        }
    }

    private void setReplyConfirm(UserPrincipal principal, Medicine medicine, MedicineTeacherConfirmResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(medicine.getConfirmContent());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
        replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }


    @Override
    public MedicineTeacherSendReplyResponse updateTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyMedcRequest updateTeacherReplyMedcRequest) throws FirebaseMessagingException {
        MedicineTeacherSendReplyResponse model = new MedicineTeacherSendReplyResponse();
        Optional<Medicine> medicineOptional = medicineRepository.findByIdAndDelActiveTrue(updateTeacherReplyMedcRequest.getId());
        if (medicineOptional.isEmpty()) {
            return null;
        }
        Long id = updateTeacherReplyMedcRequest.getId();
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        Medicine oldMedicine = medicineOptional.get();
        oldMedicine.setTeacherTimeReply(LocalDateTime.now());
        oldMedicine.setTeacherReplyDel(AppConstant.APP_FALSE);
        oldMedicine.setTeacherModifyStatus(AppConstant.APP_TRUE);
        oldMedicine.setParentUnread(AppConstant.APP_FALSE);
        model.setId(updateTeacherReplyMedcRequest.getId());
        model.setTeacherReply(updateTeacherReplyMedcRequest.getTeacherReply());
        modelMapper.map(updateTeacherReplyMedcRequest, oldMedicine);
        Medicine newMedicine = medicineRepository.save(oldMedicine);
//        this.setReplyUpdate(principal, medicine, model);
        this.setReplyUpdate(principal, newMedicine, model);
        MedicineTeacherSendReplyResponse medicineTeacherSendReplyResponse = modelMapper.map(newMedicine, MedicineTeacherSendReplyResponse.class);
        return model;
    }

    private void setReplyUpdate(UserPrincipal principal, Medicine medicine, MedicineTeacherSendReplyResponse model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
        MaUser maUser = maUserRepository.findById(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
        replyMobileDateObject.setFullName(principal.getFullName());
        replyMobileDateObject.setContent(medicine.getTeacherReply());
        replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
        replyMobileDateObject.setModifyStatus(AppConstant.APP_TRUE);
        replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
        if (medicine.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
        } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
        }
        replyMobileDateObjectList.add(replyMobileDateObject);
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply3(UserPrincipal principal, Medicine medicine, MedicineTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
                replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType()) && StringUtils.isNotBlank(medicine.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
                replyMobileDateObject.setStatusDel(AppConstant.APP_FALSE);
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }

        }
        //nhà trường phản hồi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(medicine.getSchoolReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(medicine.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
            if (medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(medicine.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }

    private void setReply1(UserPrincipal principal, Medicine medicine, MedicineTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && medicine.isConfirmStatus() && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
                replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType())) {
                MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
                replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }
        //nhà trường phản hồi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(medicine.getSchoolReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getSchoolTimeReply()));
            replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(medicine.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setContent(medicine.getTeacherReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
            if (medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(medicine.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);

    }

    private void setReply2(UserPrincipal principal, Medicine medicine, MedicineTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && medicine.isConfirmStatus() && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
                replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType()) && StringUtils.isNotBlank(medicine.getConfirmContent())) {
                replyMobileDateObject.setFullName(AppConstant.TEACHER);
                MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
                replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }

        }
        //nhà trường phản hồi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getSchoolTimeReply()));
            replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
            replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileDateObject.setSortDate(medicine.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getLastModifieDate()));
            replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
            if (medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(medicine.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }

    private void setReply(UserPrincipal principal, Medicine medicine, MedicineTeacherDetailResponse
            model) {
        List<ReplyMobileDateObject> replyMobileDateObjectList = new ArrayList<>();
        LocalDateTime dateconfirm = LocalDateTime.of(1970, Month.JANUARY, 25, 12, 30);
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && medicine.isConfirmStatus() && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileDateObject.setFullName(AppConstant.SCHOOL);
                replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
                replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType()) && StringUtils.isNotBlank(medicine.getConfirmContent())) {
                MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found id"));
                replyMobileDateObject.setFullName(maUser.getFullName());
                replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileDateObject.setContent(medicine.getConfirmContent());
                replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getConfirmdate()));
                replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
                replyMobileDateObject.setModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
                replyMobileDateObject.setSortDate(dateconfirm);
                replyMobileDateObjectList.add(replyMobileDateObject);
            }
        }

        //nhà trường phản hồi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            replyMobileDateObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileDateObject.setFullName(AppConstant.SCHOOL);
            replyMobileDateObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getLastModifieDate()));
            replyMobileDateObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isSchoolReplyDel());
            replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            replyMobileDateObject.setSortDate(medicine.getSchoolTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileDateObject replyMobileDateObject = new ReplyMobileDateObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileDateObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileDateObject.setFullName(maUser.getFullName());
            replyMobileDateObject.setContent(medicine.getTeacherReply());
            replyMobileDateObject.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(medicine.getTeacherTimeReply()));
            replyMobileDateObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileDateObject.setStatusDel(medicine.isTeacherReplyDel());
            if (medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_TRUE);
            } else if (!medicine.getIdTeacherReply().equals(principal.getId())) {
                replyMobileDateObject.setTeacherModifyStatus(AppConstant.APP_FALSE);
            }
            replyMobileDateObject.setSortDate(medicine.getTeacherTimeReply());
            replyMobileDateObjectList.add(replyMobileDateObject);
        }
        replyMobileDateObjectList = replyMobileDateObjectList.stream().sorted(Comparator.comparing(ReplyMobileDateObject::getSortDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileDateObjectList);
    }
}
