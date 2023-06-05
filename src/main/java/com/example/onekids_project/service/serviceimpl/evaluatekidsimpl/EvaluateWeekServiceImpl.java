package com.example.onekids_project.service.serviceimpl.evaluatekidsimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.entity.kids.EvaluateWeekFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.enums.DateEnum;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.EvaluateWeekFileRepository;
import com.example.onekids_project.repository.EvaluateWeekRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.response.evaluatekids.EvaluateWeekKidResponse;
import com.example.onekids_project.response.evaluatekids.EvaluateWeekResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluateWeekService;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluateWeekServiceImpl implements EvaluateWeekService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateWeekFileRepository evaluateWeekFileRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private WebSystemTitleService webSystemTitleService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;


    @Override
    public List<EvaluateWeekResponse> searchEvaluateWeek(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        //chuyển đổi ngày
        evaluateDateSearchRequest.setDate(this.convertToMonday(evaluateDateSearchRequest.getDate()));
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.searchEvaluateWeek(idSchool, evaluateDateSearchRequest);
        List<EvaluateWeekResponse> evaluateWeekResponseList = listMapper.mapList(evaluateWeekList, EvaluateWeekResponse.class);
        return evaluateWeekResponseList;
    }

    @Override
    public EvaluateWeekKidResponse getEvaluateWeekById(Long idSchool, Long id) {
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow(() -> new NotFoundException("not found evaluateWeek by id"));
        EvaluateWeekKidResponse evaluateWeekKidResponse = modelMapper.map(evaluateWeek, EvaluateWeekKidResponse.class);
        return evaluateWeekKidResponse;
    }

    @Override
    public EvaluateWeekResponse saveEvaluateWeekOne(Long idSchool, EvaluateWeekRequest evaluateWeekRequest, UserPrincipal principal) {
        Optional<EvaluateWeek> evaluateWeekOptional = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActive(evaluateWeekRequest.getId(), idSchool, AppConstant.APP_TRUE);
        if (evaluateWeekOptional.isEmpty()) {
            return null;
        }
        EvaluateWeek evaluateWeek = evaluateWeekOptional.get();
        modelMapper.map(evaluateWeekRequest, evaluateWeek);
        this.setUserUpdateEvaluateWeek(evaluateWeek, principal);
        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
        EvaluateWeekResponse evaluateWeekResponse = modelMapper.map(evaluateWeekSaved, EvaluateWeekResponse.class);
        return evaluateWeekResponse;
    }

    @Transactional
    @Override
    public boolean saveEvaluateWeekCommon(UserPrincipal userPrincipal, EvaluateWeekCommonRequest evaluateWeekCommonRequest) throws FirebaseMessagingException {
        for (Long x : evaluateWeekCommonRequest.getIdKidList()) {
            EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(x, userPrincipal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateweek by id"));
            List<Long> longList = evaluateWeek.getEvaluateWeekFileList().stream().map(y -> y.getId()).collect(Collectors.toList());
            this.deleteFile(evaluateWeek.getEvaluateWeekFileList(), longList);
            this.addFile(userPrincipal.getIdSchoolLogin(), evaluateWeek, evaluateWeekCommonRequest.getMultipartFileList());

            this.setPropertiesWeekCommon(evaluateWeek, evaluateWeekCommonRequest, userPrincipal);
            evaluateWeekRepository.save(evaluateWeek);
        }
        return true;
    }


    @Override
    public boolean saveEvaluateWeekMany(Long idSchool, List<EvaluateWeekRequest> evaluateWeekRequestList, UserPrincipal principal) {
        if (CollectionUtils.isEmpty(evaluateWeekRequestList)) {
            return false;
        }
        for (EvaluateWeekRequest evaluateWeekRequest : evaluateWeekRequestList) {
            Optional<EvaluateWeek> evaluateWeekOptional = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActive(evaluateWeekRequest.getId(), idSchool, AppConstant.APP_TRUE);
            if (evaluateWeekOptional.isEmpty()) {
                return false;
            }
            EvaluateWeek evaluateWeek = evaluateWeekOptional.get();
            modelMapper.map(evaluateWeekRequest, evaluateWeek);
            this.setUserUpdateEvaluateWeek(evaluateWeek, principal);
            evaluateWeekRepository.save(evaluateWeek);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedWeekOne(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        Optional<EvaluateWeek> evaluateWeekOptional = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActive(evaluateDateApprovedRequest.getId(), idSchool, AppConstant.APP_TRUE);
        if (evaluateWeekOptional.isEmpty()) {
            return false;
        }
        EvaluateWeek evaluateDate = evaluateWeekOptional.get();
        evaluateDate.setApproved(evaluateDateApprovedRequest.isApproved());
        evaluateWeekRepository.save(evaluateDate);

        return true;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedWeekMany(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        if (CollectionUtils.isEmpty(evaluateDateApprovedRequestList)) {
            return false;
        }
        List<EvaluateWeek> evaluateWeekList = new ArrayList<>();
        for (EvaluateDateApprovedRequest evaluateDateApprovedRequest : evaluateDateApprovedRequestList) {
            Optional<EvaluateWeek> evaluateWeekOptional = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActive(evaluateDateApprovedRequest.getId(), idSchool, AppConstant.APP_TRUE);
            if (evaluateWeekOptional.isEmpty()) {
                return false;
            }
            EvaluateWeek evaluateDate = evaluateWeekOptional.get();
            evaluateDate.setApproved(evaluateDateApprovedRequest.isApproved());
            evaluateWeekRepository.save(evaluateDate);
            evaluateWeekList.add(evaluateDate);
        }
        return true;
    }

    @Transactional
    @Override
    public EvaluateWeekResponse saveEvaluateWeekDetailOne(EvaluateWeekDetailRequest evaluateWeekDetailRequest, UserPrincipal principal) throws FirebaseMessagingException {
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActiveTrue(evaluateWeekDetailRequest.getId(), idSchool).orElseThrow(() -> new NotFoundException("not found evaluateWeek by id"));
        this.deleteFile(evaluateWeek.getEvaluateWeekFileList(), evaluateWeekDetailRequest.getFileDeleteList());
        this.addFile(principal.getIdSchoolLogin(), evaluateWeek, evaluateWeekDetailRequest.getMultipartFileList());

        this.setProperties(evaluateWeek, evaluateWeekDetailRequest, principal);
        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
        EvaluateWeekResponse evaluateWeekResponse = modelMapper.map(evaluateWeekSaved, EvaluateWeekResponse.class);
        return evaluateWeekResponse;
    }

    private void sendFirebaseReply(Kids kids, Long idTitle, String contentRq) throws FirebaseMessagingException {
        Optional<WebSystemTitle> webSystemTitle = webSystemTitleService.findById(idTitle);

        String title = webSystemTitle.get().getTitle().replace("{dd/mm/yyyy}", LocalDate.now().toString());
        String content = contentRq.length() < 50 ? contentRq : contentRq.substring(0, 50);
        List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
        if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
            NotifyRequest notifyRequest = new NotifyRequest();
            notifyRequest.setBody(content);
            notifyRequest.setTitle(title);
            FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.EVALUATE, notifyRequest, kids.getId().toString());
        }
    }

    private void setProperties(EvaluateWeek evaluateWeek, EvaluateWeekDetailRequest evaluateWeekDetailRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        if (!evaluateWeek.getContent().equals(evaluateWeekDetailRequest.getContent())) {
            evaluateWeek.setContent(evaluateWeekDetailRequest.getContent());
            evaluateWeek.setIdModified(idCreated);
            evaluateWeek.setLastModifieBy(fullName);
            evaluateWeek.setLastModifieDate(localDateTime);
        }
        if (AppTypeConstant.TEACHER.equals(principal.getAppType()) && !evaluateWeek.getTeacherReplyContent().equals(evaluateWeekDetailRequest.getTeacherReplyContent())) {
            evaluateWeek.setTeacherReplyContent(evaluateWeekDetailRequest.getTeacherReplyContent());
            evaluateWeek.setTeacherReplyIdCreated(idCreated);
            evaluateWeek.setTeacherReplyCreatedBy(fullName);
            evaluateWeek.setTeacherReplyDatetime(localDateTime);
//            evaluateWeekDetailRequest.setSchoolReadReply(AppConstant.APP_TRUE);
        }
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType()) && !evaluateWeek.getSchoolReplyContent().equals(evaluateWeekDetailRequest.getSchoolReplyContent())) {
            evaluateWeek.setSchoolReplyContent(evaluateWeekDetailRequest.getSchoolReplyContent());
            evaluateWeek.setSchoolReplyIdCreated(idCreated);
            evaluateWeek.setSchoolReplyCreatedBy(fullName);
            evaluateWeek.setSchoolReplyDatetime(localDateTime);
//            evaluateWeekDetailRequest.setSchoolReadReply(AppConstant.APP_TRUE);
        }
        evaluateWeek.setParentRead(AppConstant.APP_FALSE);
        evaluateWeek.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateWeek.setTeacherReplyDel(evaluateWeekDetailRequest.isTeacherReplyDel());
        evaluateWeek.setSchoolReplyDel(evaluateWeekDetailRequest.isSchoolReplyDel());

        //check set duyệt cho lần đầu tạo nhận xét
        if (evaluateWeek.getIdCreated() == null || evaluateWeek.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
            evaluateWeek.setIdCreated(idCreated);
            evaluateWeek.setCreatedDate(localDateTime);
            evaluateWeek.setIdModified(idCreated);
            evaluateWeek.setLastModifieDate(localDateTime);
            evaluateWeek.setApproved(principal.getSchoolConfig().isEvaluate());
            if (evaluateWeek.isApproved()) {
                firebaseFunctionService.sendParentByPlusNoContent(42L, evaluateWeek.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
            }
        } else {
            evaluateWeek.setIdModified(idCreated);
            evaluateWeek.setLastModifieDate(localDateTime);
        }
    }

    @Override
    public EvaluateWeekKidResponse searchEvaluateWeekKid(Long idSchool, EvaluateWeekSearchKidRequest evaluateWeekSearchKidRequest) {
        evaluateWeekSearchKidRequest.setDate(this.convertToMonday(evaluateWeekSearchKidRequest.getDate()));
        Optional<EvaluateWeek> evaluateWeekOptional = evaluateWeekRepository.findEvaluateWeekByKidsIdAndDateAndIdSchoolAndDelActive(evaluateWeekSearchKidRequest.getIdKid(), evaluateWeekSearchKidRequest.getDate(), idSchool, AppConstant.APP_TRUE);
        EvaluateWeek evaluateWeek;
        if (evaluateWeekOptional.isEmpty()) {
            evaluateWeek = new EvaluateWeek();
        } else {
            evaluateWeek = evaluateWeekOptional.get();
        }
        EvaluateWeekKidResponse evaluateWeekKidResponse = modelMapper.map(evaluateWeek, EvaluateWeekKidResponse.class);
        return evaluateWeekKidResponse;
    }


    /**
     * trả về thứ 2 nếu là thứ 2 hoặc 3
     * trả về null nếu là các thứ còn lại
     *
     * @param date
     * @return
     */
    private LocalDate convertToMonday(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().toString();
        if (DateEnum.MONDAY.toString().equals(dayOfWeek)) {
            return date;
        } else if (DateEnum.TUESDAY.toString().equals(dayOfWeek)) {
            return date.minusDays(1);
        }
        return null;
    }

    /**
     * set thông tin người cập nhật
     *
     * @param model
     * @param principal
     */
    private void setUserUpdateEvaluateWeek(EvaluateWeek model, UserPrincipal principal) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (StringUtils.isEmpty(model.getCreatedBy())) {
            model.setIdCreated(principal.getId());
            model.setCreatedDate(localDateTime);
            model.setCreatedBy(principal.getFullName());
            model.setIdModified(principal.getId());
            model.setLastModifieDate(localDateTime);
            model.setLastModifieBy(principal.getFullName());
        } else {
            model.setIdModified(principal.getId());
            model.setLastModifieDate(localDateTime);
            model.setLastModifieBy(principal.getFullName());
        }

        //check set duyệt cho lần đầu tạo nhận xét
        if (model.getIdCreated() == null || model.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
            model.setIdCreated(principal.getId());
            model.setCreatedDate(localDateTime);
            model.setIdModified(principal.getId());
            model.setLastModifieDate(localDateTime);
            model.setApproved(principal.getSchoolConfig().isEvaluate());
        } else {
            model.setIdModified(principal.getId());
            model.setLastModifieDate(localDateTime);
        }
    }

    /**
     * add file
     *
     * @param idSchool
     * @param evaluateWeek
     * @param multipartFileList
     */
    private void addFile(Long idSchool, EvaluateWeek evaluateWeek, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EvaluateWeekFile evaluateWeekFile = new EvaluateWeekFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.NHAN_XET) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET) + fileName;
                evaluateWeekFile.setName(multipartFile.getOriginalFilename());
                evaluateWeekFile.setUrl(urlWeb);
                evaluateWeekFile.setUrlLocal(urlLocal);
                evaluateWeekFile.setEvaluateWeek(evaluateWeek);
                evaluateWeekFileRepository.save(evaluateWeekFile);
            });
        }
    }

    /**
     * delete file
     *
     * @param evaluateWeekFileList
     * @param fileDeleteList
     */
    private void deleteFile(List<EvaluateWeekFile> evaluateWeekFileList, List<Long> fileDeleteList) {
        if (CollectionUtils.isEmpty(evaluateWeekFileList) || CollectionUtils.isEmpty(fileDeleteList)) {
            return;
        }
        fileDeleteList.forEach(x -> {
            List<EvaluateWeekFile> weekFileList = evaluateWeekFileList.stream().filter(y -> y.getId() == x).collect(Collectors.toList());
            if (weekFileList.size() == 1) {
                EvaluateWeekFile evaluateWeekFile = weekFileList.get(0);
                evaluateWeekFileList.remove(evaluateWeekFile);
                HandleFileUtils.deleteFileOrPictureInFolder(evaluateWeekFile.getUrlLocal());
                evaluateWeekFileRepository.deleteById(x);
            }
        });
    }

    private void setPropertiesWeekCommon(EvaluateWeek evaluateWeek, EvaluateWeekCommonRequest evaluateWeekCommonRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        if (!evaluateWeek.getContent().equals(evaluateWeekCommonRequest.getContent())) {
            evaluateWeek.setContent(evaluateWeekCommonRequest.getContent());
            evaluateWeek.setIdModified(idCreated);
            evaluateWeek.setLastModifieBy(fullName);
            evaluateWeek.setLastModifieDate(localDateTime);
            evaluateWeek.setParentRead(AppConstant.APP_FALSE);
        }
        if (evaluateWeek.getIdCreated() == null || evaluateWeek.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
            evaluateWeek.setIdCreated(idCreated);
            evaluateWeek.setCreatedDate(localDateTime);
            evaluateWeek.setIdModified(idCreated);
            evaluateWeek.setLastModifieDate(localDateTime);
            evaluateWeek.setApproved(principal.getSchoolConfig().isEvaluate());
            if (evaluateWeek.isApproved()) {
                firebaseFunctionService.sendParentByPlusNoContent(42L, evaluateWeek.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
            }
        } else {
            evaluateWeek.setIdModified(idCreated);
            evaluateWeek.setLastModifieDate(localDateTime);
        }
    }
}
