package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.parent.request.medicine.MedicineMobileRequest;
import com.example.onekids_project.mobile.parent.response.medicine.ListMedicineMobileResponse;
import com.example.onekids_project.mobile.parent.response.medicine.MedicineDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.medicine.MedicineMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.MedicineMobileService;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.MedicineAttachFileRepository;
import com.example.onekids_project.repository.MedicineRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FirebaseUtils;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineMobileServiceImpl implements MedicineMobileService {
    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MedicineAttachFileRepository medicineAttachFileRepository;

    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMedicineMobileResponse findMedicineMoblie(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        List<Medicine> medicineList = medicineRepository.findMedicineMoblie1(idSchool, idKid, pageable, localDateTime);
        ListMedicineMobileResponse listMedicineMobileResponse = new ListMedicineMobileResponse();
        List<MedicineMobileResponse> medicineMobileResponse = new ArrayList<>();
        medicineList.forEach(x -> {
            MedicineMobileResponse model = new MedicineMobileResponse();
            model.setId(x.getId());
            String content = x.getMedicineContent().length() < 50 ? x.getMedicineContent() : x.getMedicineContent().substring(0, 50);
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
            model.setReplyNumber(replyNumber);
            model.setPictureNumber(x.getMedicineAttachFileList().size());
            model.setCreatedDate(x.getCreatedDate());
            model.setDateMedicine(ConvertData.convertDateToString(x.getFromDate(), x.getToDate()));
            model.setDisease_name(x.getDiseaseName());
            model.setParentUnread(x.isParentUnread());
            model.setConfirmStatus(x.isConfirmStatus());
            medicineMobileResponse.add(model);
        });
        Long countAll = medicineRepository.getCountMessage(idSchool, idKid, localDateTime);
        boolean checkLastPage = countAll <= 20;
        listMedicineMobileResponse.setMedicineMobileResponseList(medicineMobileResponse);
        listMedicineMobileResponse.setLastPage(checkLastPage);
        return listMedicineMobileResponse;
    }

    @Override
    public boolean medicineRevoke(Long id) {
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found medicine by id"));
        if (medicine.isConfirmStatus()) {
            return false;
        }
        medicine.setDelActive(AppConstant.APP_FALSE);
        medicine.setParentMedicineDel(AppConstant.APP_TRUE);
        medicineRepository.save(medicine);
        return true;
    }

    @Override
    public MedicineDetailMobileResponse findMedicineDetailMobile(UserPrincipal principal, Long id) {
        Medicine medicine = medicineRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found medicine by id"));
        MedicineDetailMobileResponse model = new MedicineDetailMobileResponse();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(medicine.getIdCreated()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
        model.setFullName(maUser.getFullName());
        model.setConfirmStatus(medicine.isConfirmStatus());
        if (medicine.isSchoolReplyDel() == AppConstant.APP_TRUE && medicine.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(medicine.getCreatedDate());
            model.setContent(medicine.getMedicineContent());
            model.setDisease_name(medicine.getDiseaseName());
            model.setDateMedicine(medicine.getFromDate().isEqual(medicine.getToDate()) ? ConvertData.convertDateToStringOne(medicine.getFromDate()) : ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
            model.setConfirmStatus(medicine.isConfirmStatus());
            model.setSchoolModifyStatus(medicine.isSchoolModifyStatus());
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply1(principal, medicine, model); // nha truong thu hoi, giao vien k thu hoi
        } else if (medicine.isSchoolReplyDel() == AppConstant.APP_TRUE && medicine.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(medicine.getCreatedDate());
            model.setContent(medicine.getMedicineContent());
            model.setDisease_name(medicine.getDiseaseName());
            model.setDateMedicine(medicine.getFromDate().isEqual(medicine.getToDate()) ? ConvertData.convertDateToStringOne(medicine.getFromDate()) : ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
            model.setConfirmStatus(medicine.isConfirmStatus());
            model.setSchoolModifyStatus(medicine.isSchoolModifyStatus());
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply2(principal, medicine, model);
        } else if (medicine.isSchoolReplyDel() == AppConstant.APP_FALSE && medicine.isTeacherReplyDel() == AppConstant.APP_TRUE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(medicine.getCreatedDate());
            model.setContent(medicine.getMedicineContent());
            model.setConfirmStatus(medicine.isConfirmStatus());
            model.setDisease_name(medicine.getDiseaseName());
            model.setDateMedicine(medicine.getFromDate().isEqual(medicine.getToDate()) ? ConvertData.convertDateToStringOne(medicine.getFromDate()) : ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
            model.setSchoolModifyStatus(medicine.isSchoolModifyStatus());
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply3(principal, medicine, model);
        } else if (medicine.isSchoolReplyDel() == AppConstant.APP_FALSE && medicine.isTeacherReplyDel() == AppConstant.APP_FALSE) {
            String avatarCreate = StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
            model.setAvatar(avatarCreate);
            model.setCreatedDate(medicine.getCreatedDate());
            model.setContent(medicine.getMedicineContent());
            model.setConfirmStatus(medicine.isConfirmStatus());
            model.setDisease_name(medicine.getDiseaseName());
            model.setDateMedicine(medicine.getFromDate().isEqual(medicine.getToDate()) ? ConvertData.convertDateToStringOne(medicine.getFromDate()) : ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
            model.setSchoolModifyStatus(medicine.isSchoolModifyStatus());
            model.setPictureList(medicine.getMedicineAttachFileList().stream().map(MedicineAttachFile::getUrl).collect(Collectors.toList()));
            this.setReply(principal, medicine, model);
        }
        medicine.setParentUnread(AppConstant.APP_TRUE);
        medicineRepository.save(medicine);
        return model;
    }

    private void setReply3(UserPrincipal principal, Medicine medicine, MedicineDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && medicine.isConfirmStatus() == AppConstant.APP_TRUE && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                replyMobileObject.setCreatedDate(medicine.getConfirmdate());
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
                replyMobileObject.setCreatedDate(medicine.getConfirmdate());
            }
            replyMobileObject.setContent(medicine.getConfirmContent());
            replyMobileObject.setCreatedDate(medicine.getConfirmdate());
            replyMobileObjectList.add(replyMobileObject);
        }
        //

        //nhà trường phản hồi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdSchoolReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(medicine.getSchoolReply());
            replyMobileObject.setCreatedDate(medicine.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(medicine.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    private void setReply1(UserPrincipal principal, Medicine medicine, MedicineDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(medicine.getConfirmContent());
            model.setDisease_name(medicine.getDiseaseName());
            model.setDateMedicine(ConvertData.convertDateToString(medicine.getFromDate(), medicine.getToDate()));
            replyMobileObject.setCreatedDate(medicine.getConfirmdate());
            replyMobileObjectList.add(replyMobileObject);
        }
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            MaUser maUser = maUserRepository.findById(medicine.getIdSchoolReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(medicine.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(medicine.getTeacherReply());
            replyMobileObject.setCreatedDate(medicine.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    private void setReply2(UserPrincipal principal, Medicine medicine, MedicineDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && medicine.isConfirmStatus() && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType())) {

                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(medicine.getConfirmContent());
            replyMobileObject.setCreatedDate(medicine.getConfirmdate());
            replyMobileObjectList.add(replyMobileObject);
        }
        //nhà trường thu hoi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdSchoolReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(medicine.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên thu hoi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setContent(ParentDairyConstant.CONTENT_DEL);
            replyMobileObject.setCreatedDate(medicine.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);
    }


    private void setReply(UserPrincipal principal, Medicine medicine, MedicineDetailMobileResponse
            model) {
        List<ReplyMobileObject> replyMobileObjectList = new ArrayList<>();
        //khi có xác nhận
        if (medicine.getIdConfirmType() != null && medicine.isConfirmStatus() && StringUtils.isNotBlank(medicine.getConfirmContent())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdConfirmType()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            if (AppTypeConstant.SCHOOL.equals(medicine.getConfirmType()) || AppTypeConstant.SUPPER_SCHOOL.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(AppConstant.SCHOOL);
                replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(medicine.getConfirmType())) {
                replyMobileObject.setFullName(maUser.getFullName());
                replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            }
            replyMobileObject.setContent(medicine.getConfirmContent());
            replyMobileObject.setCreatedDate(medicine.getConfirmdate());
            replyMobileObjectList.add(replyMobileObject);
        }

        //nhà trường phản hồi
        if (medicine.getIdSchoolReply() != null && StringUtils.isNotBlank(medicine.getSchoolReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdSchoolReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
            replyMobileObject.setFullName(AppConstant.SCHOOL);
            replyMobileObject.setContent(medicine.getSchoolReply());
            replyMobileObject.setCreatedDate(medicine.getSchoolTimeReply());
            replyMobileObject.setModifyStatus(medicine.isSchoolModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        //giáo viên phản hồi
        if (medicine.getIdTeacherReply() != null && StringUtils.isNotBlank(medicine.getTeacherReply())) {
            ReplyMobileObject replyMobileObject = new ReplyMobileObject();
            MaUser maUser = maUserRepository.findById(medicine.getIdTeacherReply()).orElseThrow(() -> new NotFoundException("not found maUser by id in mobile"));
            replyMobileObject.setFullName(maUser.getFullName());
            replyMobileObject.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(principal.getIdSchoolLogin(), maUser.getId()));
            replyMobileObject.setContent(medicine.getTeacherReply());
            replyMobileObject.setCreatedDate(medicine.getTeacherTimeReply());
            replyMobileObject.setModifyStatus(medicine.isTeacherModifyStatus());
            replyMobileObjectList.add(replyMobileObject);
        }
        replyMobileObjectList = replyMobileObjectList.stream().sorted(Comparator.comparing(ReplyMobileObject::getCreatedDate)).collect(Collectors.toList());
        model.setReplyList(replyMobileObjectList);

    }

    @Transactional
    @Override
    public boolean createMedicineMob(UserPrincipal principal, MedicineMobileRequest medicineMobileRequest) throws FirebaseMessagingException {
        if (!CollectionUtils.isEmpty(medicineMobileRequest.getMultipartFileList()) && medicineMobileRequest.getMultipartFileList().size() > 3) {
            return false;
        }
        Long idSchool = principal.getIdSchoolLogin();
        Medicine medicine = new Medicine();
        medicine.setMedicineContent(medicineMobileRequest.getContent());
        medicine.setCreatedBy(principal.getFullName());
        medicine.setDiseaseName(medicineMobileRequest.getDisease_name());
        medicine.setToDate(medicineMobileRequest.getToDate());
        medicine.setFromDate(medicineMobileRequest.getFromDate());
        medicine.setIdSchool(idSchool);
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        medicine.setIdGrade(kids.getIdGrade());
        medicine.setIdClass(kids.getMaClass().getId());
        medicine.setKids(kids);
        Medicine saveMedicine = medicineRepository.save(medicine);
        if (!CollectionUtils.isEmpty(medicineMobileRequest.getMultipartFileList())) {
            this.addFile(idSchool, saveMedicine, medicineMobileRequest.getMultipartFileList());
        }
        //gửi firebase cho teacher và plus
        firebaseFunctionService.sendPlusByKids(27L, kids, medicineMobileRequest.getContent(), FirebaseConstant.CATEGORY_MEDICINE);
        firebaseFunctionService.sendTeacherByKids(27L, kids, medicineMobileRequest.getContent(), FirebaseConstant.CATEGORY_MEDICINE);
        return true;

    }

    private void addFile(Long idSchool, Medicine medicine, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.LOI_NHAN);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MedicineAttachFile medicineAttachFile = new MedicineAttachFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.LOI_NHAN) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.LOI_NHAN) + fileName;
                medicineAttachFile.setUrl(urlWeb);
                String nameFileSave = multipartFile.getOriginalFilename();
                medicineAttachFile.setName(nameFileSave);
                medicineAttachFile.setUrlLocal(urlLocal);
                medicineAttachFile.setMedicine(medicine);
                medicineAttachFileRepository.save(medicineAttachFile);
            });
        }
    }
}