package com.example.onekids_project.service.serviceimpl.evaluatekidsimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.EvaluatePeriodic;
import com.example.onekids_project.entity.kids.EvaluatePeriodicFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.request.kids.SearchKidsClassRequest;
import com.example.onekids_project.response.evaluatekids.EvaluatePeriodicKidResponse;
import com.example.onekids_project.response.evaluatekids.EvaluatePeriodicResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluatePeriodicService;
import com.example.onekids_project.util.HandleFileUtils;
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
public class EvaluatePeriodicServiceImpl implements EvaluatePeriodicService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluatePeriodicRepository evaluatePeriodicRepository;

    @Autowired
    private EvaluatePeriodicFileRepository evaluatePeriodicFileRepository;

    @Autowired
    private KidsRepository kidsRepository;

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
    public List<KidOtherResponse> searchKidsClass(Long idSchool, SearchKidsClassRequest searchKidsClassRequest) {
        List<Kids> kidsList = kidsRepository.searchKidsClass(idSchool, searchKidsClassRequest);
        List<KidOtherResponse> kidOtherResponseList = listMapper.mapList(kidsList, KidOtherResponse.class);
        return kidOtherResponseList;
    }

    @Transactional
    @Override
    public boolean createEvaluatePeriodic(UserPrincipal principal, Long idKid, EvaluatePeriodicCreateRequest evaluatePeriodicCreateRequest) throws FirebaseMessagingException {
        this.checkDataInput(evaluatePeriodicCreateRequest.getContent(), evaluatePeriodicCreateRequest.getMultipartFileList());
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(idKid, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        EvaluatePeriodic evaluatePeriodic;
        Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findByDateAndKidsId(LocalDate.now(), kids.getId());
        if (evaluatePeriodicOptional.isEmpty()) {
            evaluatePeriodic = new EvaluatePeriodic();
            evaluatePeriodic.setContent(evaluatePeriodicCreateRequest.getContent());
            this.setProperties(evaluatePeriodic, kids, principal);
        } else {
            evaluatePeriodic = modelMapper.map(evaluatePeriodicOptional.get(), EvaluatePeriodic.class);
            evaluatePeriodic.setContent(evaluatePeriodicCreateRequest.getContent());
        }
        EvaluatePeriodic evaluatePeriodicSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
        this.addFile(principal.getIdSchoolLogin(), evaluatePeriodicSaved, evaluatePeriodicCreateRequest.getMultipartFileList());
        return true;
    }


    @Transactional
    @Override
    public boolean createEvaluatePeriodicMany(UserPrincipal principal, EvaluatePeriodicCreateManyRequest evaluatePeriodicCreateManyRequest) throws FirebaseMessagingException {
        this.checkDataInput(evaluatePeriodicCreateManyRequest.getContent(), evaluatePeriodicCreateManyRequest.getMultipartFileList());
        for (Long x : evaluatePeriodicCreateManyRequest.getIdKidList()) {
            Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(x, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found kids by id"));
            EvaluatePeriodic evaluatePeriodic;
            Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findByDateAndKidsId(LocalDate.now(), kids.getId());
            if (evaluatePeriodicOptional.isEmpty()) {
                evaluatePeriodic = new EvaluatePeriodic();
                evaluatePeriodic.setContent(evaluatePeriodicCreateManyRequest.getContent());
                this.setProperties(evaluatePeriodic, kids, principal);
            } else {
                evaluatePeriodic = modelMapper.map(evaluatePeriodicOptional.get(), EvaluatePeriodic.class);
                evaluatePeriodic.setContent(evaluatePeriodicCreateManyRequest.getContent());
            }
            EvaluatePeriodic evaluatePeriodicSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
            this.addFile(principal.getIdSchoolLogin(), evaluatePeriodicSaved, evaluatePeriodicCreateManyRequest.getMultipartFileList());
        }
        return false;
    }


    @Override
    public List<EvaluatePeriodicResponse> searchEvaluatePeriodic(Long idSchool, EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest) {
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.searchEvaluatePeriodicLast(idSchool, evaluatePeriodicSearchRequest);
        List<EvaluatePeriodicResponse> evaluatePeriodicResponseList = listMapper.mapList(evaluatePeriodicList, EvaluatePeriodicResponse.class);
        return evaluatePeriodicResponseList;
    }

    @Override
    public EvaluatePeriodicResponse saveEvaluatePeriodicOne(Long idSchool, EvaluatePeriodicRequest evaluatePeriodicRequest, UserPrincipal principal) {
        EvaluatePeriodic evaluatePeriodic = this.convertToEvaluatePeriodic(evaluatePeriodicRequest, principal, idSchool);
        if (evaluatePeriodic == null) {
            return null;
        }
        EvaluatePeriodic evaluatePeriodicSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
        EvaluatePeriodicResponse evaluatePeriodicResponse = modelMapper.map(evaluatePeriodicSaved, EvaluatePeriodicResponse.class);
        return evaluatePeriodicResponse;
    }

    @Transactional
    @Override
    public boolean saveEvaluatePeriodicMany(Long idSchool, List<EvaluatePeriodicRequest> evaluatePeriodicRequestList, UserPrincipal principal) {
        if (CollectionUtils.isEmpty(evaluatePeriodicRequestList)) {
            return false;
        }
        for (EvaluatePeriodicRequest evaluatePeriodicRequest : evaluatePeriodicRequestList) {
            EvaluatePeriodic evaluatePeriodic = this.convertToEvaluatePeriodic(evaluatePeriodicRequest, principal, idSchool);
            if (evaluatePeriodic == null) {
                return false;
            }
            evaluatePeriodicRepository.save(evaluatePeriodic);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedPeriodicOne(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findByIdAndIdSchoolAndDelActiveTrue(evaluateDateApprovedRequest.getId(), idSchool).orElseThrow(() -> new NotFoundException("not found evaluateperiodic by id"));
        modelMapper.map(evaluateDateApprovedRequest, evaluatePeriodic);
        evaluatePeriodicRepository.save(evaluatePeriodic);
        return true;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedPeriodicMany(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        List<EvaluatePeriodic> evaluatePeriodicList = new ArrayList<>();
        evaluateDateApprovedRequestList.forEach(x -> {
            EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findByIdAndIdSchoolAndDelActiveTrue(x.getId(), idSchool).orElseThrow(() -> new NotFoundException("not found evaluateperiodic by id"));
            modelMapper.map(x, evaluatePeriodic);
            evaluatePeriodicRepository.save(evaluatePeriodic);
            evaluatePeriodicList.add(evaluatePeriodic);
        });
        return true;
    }

    @Override
    public List<EvaluatePeriodicKidResponse> searchEvaluatePeriodicDetaial(Long idSchool, Long idKid) {
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findByIdSchoolAndKidsIdAndDelActiveTrue(idSchool, idKid);
        List<EvaluatePeriodicKidResponse> evaluatePeriodicKidResponses = listMapper.mapList(evaluatePeriodicList, EvaluatePeriodicKidResponse.class);
        return evaluatePeriodicKidResponses;
    }

    @Transactional
    @Override
    public EvaluatePeriodicResponse saveEvaluatePeriodicDetailOne(Long idSchool, EvaluatePeriodicDetailRequest evaluatePeriodicDetailRequest, UserPrincipal principal) throws FirebaseMessagingException {
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findByIdAndIdSchoolAndDelActiveTrue(evaluatePeriodicDetailRequest.getId(), idSchool).orElseThrow(() -> new NotFoundException("not found evaluateperiodic by id"));
        this.checkDataInput(evaluatePeriodicDetailRequest.getContent(), evaluatePeriodicDetailRequest.getMultipartFileList());
        this.deleteFile(evaluatePeriodic.getEvaluatePeriodicFileList(), evaluatePeriodicDetailRequest.getFileDeleteList());
        this.addFile(principal.getIdSchoolLogin(), evaluatePeriodic, evaluatePeriodicDetailRequest.getMultipartFileList());

        this.setDetailProperties(evaluatePeriodic, evaluatePeriodicDetailRequest, principal);
        EvaluatePeriodic evaluatePeriodicSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
        EvaluatePeriodicResponse evaluatePeriodicResponse = modelMapper.map(evaluatePeriodicSaved, EvaluatePeriodicResponse.class);


        return evaluatePeriodicResponse;
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

    /**
     * set thông tin người cập nhật
     *
     * @param model
     * @param principal
     */
    private void setUserUpdateEvaluatePeriodic(EvaluatePeriodic model, UserPrincipal principal) {
        if (StringUtils.isEmpty(model.getCreatedBy())) {
            model.setCreatedBy(principal.getFullName());
            model.setLastModifieBy(principal.getFullName());
        } else {
            model.setLastModifieBy(principal.getFullName());
        }
    }

    /**
     * chuyển đổi về entity
     *
     * @param evaluatePeriodicRequest
     * @param principal
     * @return
     */
    private EvaluatePeriodic convertToEvaluatePeriodic(EvaluatePeriodicRequest evaluatePeriodicRequest, UserPrincipal principal, Long idSchool) {
        EvaluatePeriodic evaluatePeriodic = new EvaluatePeriodic();
        if (evaluatePeriodicRequest.getId() == null) {
            modelMapper.map(evaluatePeriodicRequest, evaluatePeriodic);
            this.setPeriodicProperties(evaluatePeriodic);
        } else {
            //đã tồn tại trong DB
            Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findEvaluatePeriodicByIdAndIdSchoolAndDelActive(evaluatePeriodicRequest.getId(), idSchool, AppConstant.APP_TRUE);
            if (evaluatePeriodicOptional.isEmpty()) {
                return null;
            }
            evaluatePeriodic = evaluatePeriodicOptional.get();
            modelMapper.map(evaluatePeriodicRequest, evaluatePeriodic);
        }
        this.setUserUpdateEvaluatePeriodic(evaluatePeriodic, principal);
        return evaluatePeriodic;
    }

    /**
     * set các thuộc tính cần thiết trước khi update cho TH chưa tồn tại trong DB
     *
     * @param model
     */
    private void setPeriodicProperties(EvaluatePeriodic model) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(model.getKids().getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy học sinh theo id"));
        model.setDate(LocalDate.now());
        model.setKids(kids);
        model.setIdSchool(kids.getIdSchool());
        model.setIdGrade(kids.getIdGrade());
        model.setIdClass(kids.getMaClass().getId());
    }

    /**
     * set properties
     *
     * @param evaluatePeriodic
     * @param kids
     */
    private void setProperties(EvaluatePeriodic evaluatePeriodic, Kids kids, UserPrincipal principal) throws FirebaseMessagingException {
        evaluatePeriodic.setDate(LocalDate.now());
        evaluatePeriodic.setKids(kids);
        evaluatePeriodic.setIdSchool(kids.getIdSchool());
        evaluatePeriodic.setIdGrade(kids.getIdGrade());
        evaluatePeriodic.setIdClass(kids.getMaClass().getId());
        evaluatePeriodic.setCreatedBy(principal.getFullName());
        evaluatePeriodic.setLastModifieBy(principal.getFullName());
        evaluatePeriodic.setApproved(principal.getSchoolConfig().isEvaluate());
        if (evaluatePeriodic.isApproved()) {
            firebaseFunctionService.sendParentByPlusNoContent(50L, evaluatePeriodic.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
        }
    }

    /**
     * set properties
     *
     * @param evaluatePeriodic
     * @param evaluatePeriodicDetailRequest
     * @param principal
     */
    private void setDetailProperties(EvaluatePeriodic evaluatePeriodic, EvaluatePeriodicDetailRequest evaluatePeriodicDetailRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        if (!evaluatePeriodic.getContent().equals(evaluatePeriodicDetailRequest.getContent())) {
            evaluatePeriodic.setContent(evaluatePeriodicDetailRequest.getContent());
            evaluatePeriodic.setIdModified(idCreated);
            evaluatePeriodic.setLastModifieBy(fullName);
            evaluatePeriodic.setLastModifieDate(localDateTime);
        }
        if (AppTypeConstant.TEACHER.equals(principal.getAppType()) && !evaluatePeriodic.getTeacherReplyContent().equals(evaluatePeriodicDetailRequest.getTeacherReplyContent())) {
            evaluatePeriodic.setTeacherReplyContent(evaluatePeriodicDetailRequest.getTeacherReplyContent());
            evaluatePeriodic.setTeacherReplyIdCreated(idCreated);
            evaluatePeriodic.setTeacherReplyCreatedBy(fullName);
            evaluatePeriodic.setTeacherReplyDatetime(localDateTime);
//            evaluatePeriodicDetailRequest.setSchoolReadReply(AppConstant.APP_TRUE);
        }
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType()) && !evaluatePeriodic.getSchoolReplyContent().equals(evaluatePeriodicDetailRequest.getSchoolReplyContent())) {
            evaluatePeriodic.setSchoolReplyContent(evaluatePeriodicDetailRequest.getSchoolReplyContent());
            evaluatePeriodic.setSchoolReplyIdCreated(idCreated);
            evaluatePeriodic.setSchoolReplyCreatedBy(fullName);
            evaluatePeriodic.setSchoolReplyDatetime(localDateTime);
//            evaluatePeriodicDetailRequest.setSchoolReadReply(AppConstant.APP_TRUE);
        }
        evaluatePeriodic.setParentRead(AppConstant.APP_FALSE);
        evaluatePeriodic.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluatePeriodic.setTeacherReplyDel(evaluatePeriodicDetailRequest.isTeacherReplyDel());
        evaluatePeriodic.setSchoolReplyDel(evaluatePeriodicDetailRequest.isSchoolReplyDel());

    }

    /**
     * add file
     *
     * @param idSchool
     * @param evaluatePeriodic
     * @param multipartFileList
     */
    private void addFile(Long idSchool, EvaluatePeriodic evaluatePeriodic, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EvaluatePeriodicFile evaluatePeriodicFile = new EvaluatePeriodicFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.NHAN_XET) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET) + fileName;
                evaluatePeriodicFile.setName(multipartFile.getOriginalFilename());
                evaluatePeriodicFile.setUrl(urlWeb);
                evaluatePeriodicFile.setUrlLocal(urlLocal);
                evaluatePeriodicFile.setEvaluatePeriodic(evaluatePeriodic);
                evaluatePeriodicFileRepository.save(evaluatePeriodicFile);
            });
        }
    }

    /**
     * delete file
     *
     * @param evaluatePeriodicFileList
     * @param fileDeleteList
     */
    private void deleteFile(List<EvaluatePeriodicFile> evaluatePeriodicFileList, List<Long> fileDeleteList) {
        if (CollectionUtils.isEmpty(evaluatePeriodicFileList) || CollectionUtils.isEmpty(fileDeleteList)) {
            return;
        }
        fileDeleteList.forEach(x -> {
            List<EvaluatePeriodicFile> periodicFileList = evaluatePeriodicFileList.stream().filter(y -> y.getId() == x).collect(Collectors.toList());
            if (periodicFileList.size() == 1) {
                EvaluatePeriodicFile evaluatePeriodicFile = periodicFileList.get(0);
                evaluatePeriodicFileList.remove(evaluatePeriodicFile);
                HandleFileUtils.deleteFileOrPictureInFolder(evaluatePeriodicFile.getUrlLocal());
                evaluatePeriodicFileRepository.deleteById(x);
            }
        });
    }

    /**
     * check data
     *
     * @param content
     * @param multipartFileList
     */
    private void checkDataInput(String content, List<MultipartFile> multipartFileList) {
        if (StringUtils.isBlank(content) && CollectionUtils.isEmpty(multipartFileList)) {
            throw new NotFoundException("Dữ liệu không được để trống");
        }
    }
}
