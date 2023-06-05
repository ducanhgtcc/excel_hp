package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.MedicineRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.MedicineRequest;
import com.example.onekids_project.request.parentdiary.SearchMedicineRequest;
import com.example.onekids_project.response.parentdiary.ListMedicineResponse;
import com.example.onekids_project.response.parentdiary.MedicineNewResponse;
import com.example.onekids_project.response.parentdiary.MedicineResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MedicineSerVice;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicineServiceImpl implements MedicineSerVice {
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public boolean updateRead(Long id, List<MedicineRequest> medicineResponse) {
        medicineResponse.forEach(x -> {
            Optional<Medicine> medicineOptional = medicineRepository.findByIdAndDelActiveTrue(x.getId());
            if (medicineOptional.isPresent()) {
                Medicine medicine = medicineOptional.get();
                medicine.setTeacherUnread(true);
                medicineRepository.save(medicine);
            }
        });
        return true;
    }

    @Transactional
    @Override
    public boolean updateConfirmMany(Long id, List<MedicineRequest> medicineResponse, UserPrincipal principal) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        for (MedicineRequest x : medicineResponse) {
            Optional<Medicine> medicineOptional = medicineRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(x.getId());
            if (medicineOptional.isPresent()) {
                Medicine medicine = medicineOptional.get();
                medicine.setConfirmStatus(AppConstant.APP_TRUE);
                medicine.setIdConfirmType(principal.getId());
                medicine.setConfirmdate(LocalDateTime.now());
                if (principal.getAppType().equals(AppTypeConstant.SCHOOL)) {
                    medicine.setConfirmType(AppTypeConstant.SCHOOL);
                    medicine.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_SCHOOL);
                } else if (principal.getAppType().equals(AppTypeConstant.TEACHER)) {
                    medicine.setConfirmType(AppTypeConstant.TEACHER);
                    medicine.setConfirmContent(ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
                }
                medicine.setParentUnread(AppConstant.APP_FALSE);
                medicineRepository.save(medicine);
                //gửi firebase
                Long idWebSystem = AppTypeConstant.SCHOOL.equals(principal.getAppType()) ? 29L : 28L;
                firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, medicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE);
            }
        }
        return true;
    }

    //    fireBasse
    private void sendFireBase(Kids kids, String title, String content) throws FirebaseMessagingException {
        title = title.length() < 50 ? title : title.substring(0, 50);
        content = content.length() < 50 ? content : content.substring(0, 50);

        List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
            NotifyRequest notifyRequest = new NotifyRequest();
            notifyRequest.setBody(content);
            notifyRequest.setTitle(title);
            FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.MEDICINE, notifyRequest, kids.getId().toString());
        }
    }


    @Override
    public ListMedicineResponse searchMedicineAbc(UserPrincipal principal, Long idSchool, SearchMedicineRequest request) {
        CommonValidate.checkPlusOrTeacher(principal);
        List<Medicine> medicineList = medicineRepository.searchMedi(idSchool, request);
        List<MedicineResponse> medicineResponseList = listMapper.mapList(medicineList, MedicineResponse.class);
        medicineResponseList.forEach(x -> {
            if (x.getIdTeacherReply() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdTeacherReply());
                maUserOptional.ifPresent(maUser -> x.setTeacherReplyName(maUser.getFullName()));
            }
            if (x.getIdSchoolReply() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdSchoolReply());
                maUserOptional.ifPresent(maUser -> x.setSchoolReplyy(maUser.getFullName()));
            }
            if (x.getIdConfirm() != null) {
                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdConfirm());
                maUserOptional.ifPresent(maUser -> x.setNameConfirm(maUser.getFullName()));
            }
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(x.getIdClass()).orElseThrow();
            x.setClasName(maClass.getClassName());
            x.setNumberFile(x.getMedicineAttachFileList().size());
            x.setKidName(x.getKids().getFullName());
            x.setDateDrink(x.getFromDate() + "\n đến \n " + x.getToDate());
            if (x.getIdSchoolReply() != null && x.getIdTeacherReply() != null) {
                x.setSchoolFeedback("- Nhà trường \n - Giáo viên");
            } else if (x.getIdSchoolReply() != null && x.getIdTeacherReply() == null) {
                x.setSchoolFeedback("- Nhà trường");
            } else if (x.getIdSchoolReply() == null && x.getIdTeacherReply() != null) {
                x.setSchoolFeedback("- Giáo viên");
            }
        });
        long total = medicineRepository.countTotalAccount(idSchool, request);
        ListMedicineResponse listMedicineResponse = new ListMedicineResponse();
        listMedicineResponse.setTotal(total);
        listMedicineResponse.setMedicineResponses(medicineResponseList);
        return listMedicineResponse;
    }

    @Override
    public boolean revokePlus(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        medicine.setSchoolReplyDel(request.getStatus());
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        return true;
    }

    @Transactional
    @Override
    public boolean updateMedicine(Long idSchoolLogin, UserPrincipal principal, ContentRequest request) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        Medicine oldMedicine = medicineRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType())){
            if (oldMedicine.getIdSchoolReply() == null) {
                //gửi firebase
                firebaseFunctionService.sendParentByPlus(31L, oldMedicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE, request.getContent());
            }
            // click lưu. (chưa xác nhận, chưa phản hồi)
            oldMedicine.setSchoolTimeReply(LocalDateTime.now());
            oldMedicine.setSchoolReply(request.getContent());
            oldMedicine.setSchoolModifyStatus(oldMedicine.getIdSchoolReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            oldMedicine.setIdSchoolReply(principal.getId());
        }else if(AppTypeConstant.TEACHER.equals(principal.getAppType())){
            if (oldMedicine.getIdSchoolReply() == null) {
                //gửi firebase
                firebaseFunctionService.sendParentByTeacher(30L, oldMedicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE, request.getContent());
            }
            // click lưu. (chưa xác nhận, chưa phản hồi)
            oldMedicine.setTeacherTimeReply(LocalDateTime.now());
            oldMedicine.setTeacherReply(request.getContent());
            oldMedicine.setTeacherModifyStatus(oldMedicine.getIdTeacherReply() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            oldMedicine.setIdTeacherReply(principal.getId());
        }
        oldMedicine.setParentUnread(AppConstant.APP_FALSE);
        if (oldMedicine.getIdConfirmType() == null) {
            this.setConfirm(principal, oldMedicine, ButtonConstant.BUTTON_SAVE);
        }
        medicineRepository.save(oldMedicine);

        return true;
    }

    @Transactional
    @Override
    public boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);
        Medicine medicine = medicineRepository.findByIdAndConfirmStatusFalseAndDelActiveTrue(id).orElseThrow();
        medicine.setParentUnread(AppConstant.APP_FALSE);
        this.setConfirm(principal, medicine, ButtonConstant.BUTTON_CONFIRM);
        medicineRepository.save(medicine);
        Long idWebSystem = AppTypeConstant.SCHOOL.equals(principal.getAppType()) ? 29L : 28L;
        firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, medicine.getKids(), FirebaseConstant.CATEGORY_MEDICINE);
        return true;
    }

    @Override
    public boolean revokeTeacher(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkPlusOrTeacher(principal);
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        medicine.setTeacherReplyDel(request.getStatus());
        medicine.setParentUnread(AppConstant.APP_FALSE);
        medicineRepository.save(medicine);
        return true;
    }

    @Override
    public MedicineNewResponse findByIdMedicineNew(UserPrincipal principal, Long id) {
        CommonValidate.checkPlusOrTeacher(principal);
        Medicine oldMedicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        oldMedicine.setTeacherUnread(AppConstant.APP_TRUE);
        medicineRepository.save(oldMedicine);
        MedicineNewResponse response = modelMapper.map(oldMedicine, MedicineNewResponse.class);
        return response;
    }

    private void setConfirm(UserPrincipal principal, Medicine medicine, String type) throws FirebaseMessagingException {
        medicine.setConfirmStatus(AppConstant.APP_TRUE);
        medicine.setIdConfirmType(principal.getId());
        medicine.setConfirmdate(LocalDateTime.now());
        medicine.setConfirmType(principal.getAppType());
        if (type.equals(ButtonConstant.BUTTON_CONFIRM)) {
            medicine.setConfirmContent(principal.getAppType().equals(AppTypeConstant.SCHOOL) ? ParentDairyConstant.CONTENT_CONFIRM_SCHOOL : ParentDairyConstant.CONTENT_CONFIRM_TEACHER);
        }
    }
}


