package com.example.onekids_project.service.serviceimpl.evaluatekidsimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.entity.kids.EvaluateMonthFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.EvaluateMonthFileRepository;
import com.example.onekids_project.repository.EvaluateMonthRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.response.evaluatekids.EvaluateMonthKidResponse;
import com.example.onekids_project.response.evaluatekids.EvaluateMonthResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluteMonthService;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluteMonthServiceImpl implements EvaluteMonthService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private EvaluateMonthFileRepository evaluateMonthFileRepository;

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
    public List<EvaluateMonthResponse> searchEvaluateMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        int month = 0;
        int year = 0;
        if (evaluateDateSearchRequest.getDate() != null) {
            month = evaluateDateSearchRequest.getDate().getMonthValue();
            year = evaluateDateSearchRequest.getDate().getYear();
        }
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.searchEvaluateMonth(idSchool, evaluateDateSearchRequest, month, year);
        List<EvaluateMonthResponse> evaluateMonthResponseList = listMapper.mapList(evaluateMonthList, EvaluateMonthResponse.class);
        return evaluateMonthResponseList;
    }

    @Override
    public EvaluateMonthKidResponse getEvaluateMonthById(Long idSchool, Long id) {
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow(() -> new NotFoundException("not found evaluateMonth by id"));
        EvaluateMonthKidResponse evaluateMonthKidResponse = modelMapper.map(evaluateMonth, EvaluateMonthKidResponse.class);
        return evaluateMonthKidResponse;
    }

    @Override
    public EvaluateMonthResponse saveEvaluateMonthOne(Long idSchool, EvaluateMonthRequest evaluateMonthRequest, UserPrincipal principal) {
        Optional<EvaluateMonth> evaluateMonthOptional = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActive(evaluateMonthRequest.getId(), idSchool, AppConstant.APP_TRUE);
        if (evaluateMonthOptional.isEmpty()) {
            return null;
        }
        EvaluateMonth evaluateMonth = evaluateMonthOptional.get();
        modelMapper.map(evaluateMonthRequest, evaluateMonth);
        this.setUserUpdateEvaluateMonth(evaluateMonth, principal);
        EvaluateMonth evaluateMonthSaved = evaluateMonthRepository.save(evaluateMonth);
        EvaluateMonthResponse evaluateMonthResponse = modelMapper.map(evaluateMonthSaved, EvaluateMonthResponse.class);
        return evaluateMonthResponse;
    }

    @Override
    public boolean saveEvaluateMonthMany(Long idSchool, List<EvaluateMonthRequest> evaluateMonthRequestList, UserPrincipal principal) {
        if (CollectionUtils.isEmpty(evaluateMonthRequestList)) {
            return false;
        }
        for (EvaluateMonthRequest evaluateMonthRequest : evaluateMonthRequestList) {
            Optional<EvaluateMonth> evaluateMonthOptional = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActive(evaluateMonthRequest.getId(), idSchool, AppConstant.APP_TRUE);
            if (evaluateMonthOptional.isEmpty()) {
                return false;
            }
            EvaluateMonth evaluateMonth = evaluateMonthOptional.get();
            modelMapper.map(evaluateMonthRequest, evaluateMonth);
            this.setUserUpdateEvaluateMonth(evaluateMonth, principal);
            evaluateMonthRepository.save(evaluateMonth);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedMonthOne(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        Optional<EvaluateMonth> evaluateMonthOptional = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActive(evaluateDateApprovedRequest.getId(), idSchool, AppConstant.APP_TRUE);
        if (evaluateMonthOptional.isEmpty()) {
            return false;
        }
        EvaluateMonth evaluateMonth = evaluateMonthOptional.get();
        evaluateMonth.setApproved(evaluateDateApprovedRequest.isApproved());
        evaluateMonthRepository.save(evaluateMonth);
        return true;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedMonthMany(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        if (CollectionUtils.isEmpty(evaluateDateApprovedRequestList)) {
            return false;
        }
        List<EvaluateMonth> evaluateMonthList = new ArrayList<>();
        for (EvaluateDateApprovedRequest evaluateDateApprovedRequest : evaluateDateApprovedRequestList) {
            Optional<EvaluateMonth> evaluateMonthOptional = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActive(evaluateDateApprovedRequest.getId(), idSchool, AppConstant.APP_TRUE);
            if (evaluateMonthOptional.isEmpty()) {
                return false;
            }
            EvaluateMonth evaluateMonth = evaluateMonthOptional.get();
            evaluateMonth.setApproved(evaluateDateApprovedRequest.isApproved());
            evaluateMonthRepository.save(evaluateMonth);
            evaluateMonthList.add(evaluateMonth);
        }
        return true;
    }

    @Override
    public EvaluateMonthKidResponse searchEvaluateMonthKid(Long idSchool, EvaluateWeekSearchKidRequest evaluateWeekSearchKidRequest) {
        int month = 0;
        int year = 0;
        if (evaluateWeekSearchKidRequest.getDate() != null) {
            month = evaluateWeekSearchKidRequest.getDate().getMonthValue();
            year = evaluateWeekSearchKidRequest.getDate().getYear();
        }
        Optional<EvaluateMonth> evaluateMonthOptional = evaluateMonthRepository.findEvaluateMonthByKidsIdAndIdSchoolAndMonthAndYearAndDelActive(evaluateWeekSearchKidRequest.getIdKid(), idSchool, month, year, AppConstant.APP_TRUE);
        EvaluateMonth evaluateMonth;
        if (evaluateMonthOptional.isEmpty()) {
            evaluateMonth = new EvaluateMonth();
        } else {
            evaluateMonth = evaluateMonthOptional.get();
        }
        EvaluateMonthKidResponse evaluateMonthKidResponse = modelMapper.map(evaluateMonth, EvaluateMonthKidResponse.class);
        return evaluateMonthKidResponse;
    }

    @Transactional
    @Override
    public EvaluateMonthResponse saveEvaluateMonthDetailOne(Long idSchool, EvaluateMonthDetailRequest evaluateMonthDetailRequest, UserPrincipal principal) throws FirebaseMessagingException {
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(evaluateMonthDetailRequest.getId(), idSchool).orElseThrow(() -> new NotFoundException("not found evaluateMonth by id"));
        this.deleteFile(evaluateMonth.getEvaluateMonthFileList(), evaluateMonthDetailRequest.getFileDeleteList());
        this.addFile(principal.getIdSchoolLogin(), evaluateMonth, evaluateMonthDetailRequest.getMultipartFileList());

        this.setProperties(evaluateMonth, evaluateMonthDetailRequest, principal);
        EvaluateMonth evaluateMonthSaved = evaluateMonthRepository.save(evaluateMonth);
        EvaluateMonthResponse evaluateWeekResponse = modelMapper.map(evaluateMonthSaved, EvaluateMonthResponse.class);
        return evaluateWeekResponse;
    }

    @Override
    public boolean saveEvaluateMonthCommon(UserPrincipal userPrincipal, EvaluateMonthCommonRequest evaluateMonthCommonRequest) throws FirebaseMessagingException {
        for (Long x : evaluateMonthCommonRequest.getIdKidList()) {
            EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActiveTrue(x, userPrincipal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateMonth by id"));
            List<Long> longList = evaluateMonth.getEvaluateMonthFileList().stream().map(y -> y.getId()).collect(Collectors.toList());
            this.deleteFile(evaluateMonth.getEvaluateMonthFileList(), longList);
            this.addFile(userPrincipal.getIdSchoolLogin(), evaluateMonth, evaluateMonthCommonRequest.getMultipartFileList());

            this.setPropertiesMonthCommon(evaluateMonth, evaluateMonthCommonRequest, userPrincipal);
            evaluateMonthRepository.save(evaluateMonth);
        }
        return true;
    }

    /**
     * set thông tin người cập nhật
     *
     * @param model
     * @param principal
     */
    private void setUserUpdateEvaluateMonth(EvaluateMonth model, UserPrincipal principal) {
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

//    /**
//     * set phản hồi tháng để trước khi lưu vào DB
//     *
//     * @param evaluateMonth
//     * @param userPrincipal
//     */
//    private void setEvaluateMonthReplyBeforeSave(EvaluateMonth evaluateMonth, UserPrincipal userPrincipal) {
//        EvaluateParentReply evaluateParentReply = evaluateMonth.getEvaluateParentReply();
//        EvaluateTeacherReply evaluateTeacherReply = evaluateMonth.getEvaluateTeacherReply();
//        EvaluateSchoolReply evaluateSchoolReply = evaluateMonth.getEvaluateSchoolReply();
//        if (evaluateParentReply != null) {
//            if (evaluateParentReply.getId() == null) {
//                if (StringUtils.isBlank(evaluateParentReply.getContent())) {
//                    evaluateMonth.setEvaluateParentReply(null);
//                } else {
//                    evaluateParentReply.setEvaluateMonth(evaluateMonth);
//                    evaluateParentReply.setCreatedBy(userPrincipal.getFullName());
//                    evaluateParentReply.setLastModifieBy(userPrincipal.getFullName());
//                }
//            } else {
//                evaluateParentReply.setLastModifieBy(userPrincipal.getFullName());
//            }
//        }
//        if (evaluateParentReply != null && evaluateParentReply.getId() != null && !evaluateParentReply.isReplyDel() && StringUtils.isNotBlank(evaluateParentReply.getContent())) {
//            if (evaluateTeacherReply != null) {
//                if (evaluateTeacherReply.getId() == null) {
//                    if (StringUtils.isBlank(evaluateTeacherReply.getContent())) {
//                        evaluateMonth.setEvaluateTeacherReply(null);
//                    }
//                    evaluateTeacherReply.setEvaluateMonth(evaluateMonth);
//                    evaluateTeacherReply.setCreatedBy(userPrincipal.getFullName());
//                    evaluateTeacherReply.setLastModifieBy(userPrincipal.getFullName());
//                } else {
//                    evaluateTeacherReply.setLastModifieBy(userPrincipal.getFullName());
//                }
//            }
//            if (evaluateSchoolReply != null) {
//                if (evaluateSchoolReply.getId() == null) {
//                    if (StringUtils.isBlank(evaluateSchoolReply.getContent())) {
//                        evaluateMonth.setEvaluateSchoolReply(null);
//                    }
//                    evaluateSchoolReply.setEvaluateMonth(evaluateMonth);
//                    evaluateSchoolReply.setCreatedBy(userPrincipal.getFullName());
//                    evaluateSchoolReply.setLastModifieBy(userPrincipal.getFullName());
//                } else {
//                    evaluateSchoolReply.setLastModifieBy(userPrincipal.getFullName());
//                }
//            }
//        } else {
//            evaluateMonth.setEvaluateTeacherReply(null);
//            evaluateMonth.setEvaluateSchoolReply(null);
//        }
//    }

    /**
     * add file
     *
     * @param idSchool
     * @param evaluateMonth
     * @param multipartFileList
     */
    private void addFile(Long idSchool, EvaluateMonth evaluateMonth, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EvaluateMonthFile evaluateMonthFile = new EvaluateMonthFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.NHAN_XET) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET) + fileName;
                evaluateMonthFile.setName(multipartFile.getOriginalFilename());
                evaluateMonthFile.setUrl(urlWeb);
                evaluateMonthFile.setUrlLocal(urlLocal);
                evaluateMonthFile.setEvaluateMonth(evaluateMonth);
                evaluateMonthFileRepository.save(evaluateMonthFile);
            });
        }
    }

    /**
     * delete file
     *
     * @param evaluateMonthFileList
     * @param fileDeleteList
     */
    private void deleteFile(List<EvaluateMonthFile> evaluateMonthFileList, List<Long> fileDeleteList) {
        if (CollectionUtils.isEmpty(evaluateMonthFileList) || CollectionUtils.isEmpty(fileDeleteList)) {
            return;
        }
        fileDeleteList.forEach(x -> {
            List<EvaluateMonthFile> weekFileList = evaluateMonthFileList.stream().filter(y -> y.getId() == x).collect(Collectors.toList());
            if (weekFileList.size() == 1) {
                EvaluateMonthFile evaluateWeekFile = weekFileList.get(0);
                evaluateMonthFileList.remove(evaluateWeekFile);
                HandleFileUtils.deleteFileOrPictureInFolder(evaluateWeekFile.getUrlLocal());
                evaluateMonthFileRepository.deleteById(x);
            }
        });
    }

    /**
     * set properties
     *
     * @param evaluateMonth
     * @param evaluateMonthDetailRequest
     * @param principal
     */
    private void setProperties(EvaluateMonth evaluateMonth, EvaluateMonthDetailRequest evaluateMonthDetailRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        if (!evaluateMonth.getContent().equals(evaluateMonthDetailRequest.getContent())) {
            evaluateMonth.setContent(evaluateMonthDetailRequest.getContent());
            evaluateMonth.setIdModified(idCreated);
            evaluateMonth.setLastModifieBy(fullName);
            evaluateMonth.setLastModifieDate(localDateTime);
        }
        if (AppTypeConstant.TEACHER.equals(principal.getAppType()) && !evaluateMonth.getTeacherReplyContent().equals(evaluateMonthDetailRequest.getTeacherReplyContent())) {
            evaluateMonth.setTeacherReplyContent(evaluateMonthDetailRequest.getTeacherReplyContent());
            evaluateMonth.setTeacherReplyIdCreated(idCreated);
            evaluateMonth.setTeacherReplyCreatedBy(fullName);
            evaluateMonth.setTeacherReplyDatetime(localDateTime);
//            evaluateMonthDetailRequest.setSchoolReadReply(AppConstant.APP_TRUE);
        }
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType()) && !evaluateMonth.getSchoolReplyContent().equals(evaluateMonthDetailRequest.getSchoolReplyContent())) {
            evaluateMonth.setSchoolReplyContent(evaluateMonthDetailRequest.getSchoolReplyContent());
            evaluateMonth.setSchoolReplyIdCreated(idCreated);
            evaluateMonth.setSchoolReplyCreatedBy(fullName);
            evaluateMonth.setSchoolReplyDatetime(localDateTime);
//            evaluateMonthDetailRequest.setSchoolReadReply(AppConstant.APP_TRUE);
        }
        evaluateMonth.setParentRead(AppConstant.APP_FALSE);
        evaluateMonth.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateMonth.setTeacherReplyDel(evaluateMonthDetailRequest.isTeacherReplyDel());
        evaluateMonth.setSchoolReplyDel(evaluateMonthDetailRequest.isSchoolReplyDel());

        //check set duyệt cho lần đầu tạo nhận xét
        if (evaluateMonth.getIdCreated() == null || evaluateMonth.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
            evaluateMonth.setIdCreated(idCreated);
            evaluateMonth.setCreatedDate(localDateTime);
            evaluateMonth.setIdModified(idCreated);
            evaluateMonth.setLastModifieDate(localDateTime);
            evaluateMonth.setApproved(principal.getSchoolConfig().isEvaluate());
            if (evaluateMonth.isApproved()) {
                firebaseFunctionService.sendParentByPlusNoContent(46L, evaluateMonth.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
            }

        } else {
            evaluateMonth.setIdModified(idCreated);
            evaluateMonth.setLastModifieDate(localDateTime);
        }
    }


    /**
     * set properties
     *
     * @param evaluateMonth
     * @param evaluateMonthCommonRequest
     * @param principal
     */
    private void setPropertiesMonthCommon(EvaluateMonth evaluateMonth, EvaluateMonthCommonRequest evaluateMonthCommonRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        if (!evaluateMonth.getContent().equals(evaluateMonthCommonRequest.getContent())) {
            evaluateMonth.setContent(evaluateMonthCommonRequest.getContent());
            evaluateMonth.setIdModified(idCreated);
            evaluateMonth.setLastModifieBy(fullName);
            evaluateMonth.setLastModifieDate(localDateTime);
            evaluateMonth.setParentRead(AppConstant.APP_FALSE);
        }
        //check set duyệt cho lần đầu tạo nhận xét
        if (evaluateMonth.getIdCreated() == null || evaluateMonth.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
            evaluateMonth.setIdCreated(idCreated);
            evaluateMonth.setCreatedDate(localDateTime);
            evaluateMonth.setIdModified(idCreated);
            evaluateMonth.setLastModifieDate(localDateTime);
            evaluateMonth.setApproved(principal.getSchoolConfig().isEvaluate());
            if (evaluateMonth.isApproved()) {
                firebaseFunctionService.sendParentByPlusNoContent(46L, evaluateMonth.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
            }
        } else {
            evaluateMonth.setIdModified(idCreated);
            evaluateMonth.setLastModifieDate(localDateTime);
        }
    }

}
