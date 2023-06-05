package com.example.onekids_project.service.serviceimpl.evaluatekidsimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.kids.EvaluateAttachFile;
import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.sample.EvaluateSample;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.importexport.model.EvaluateDateKidModel;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.evaluatekids.*;
import com.example.onekids_project.response.evaluatekids.*;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.service.servicecustom.evaluatekids.EvaluateDateService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvaluateDateServiceImpl implements EvaluateDateService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private EvaluateSampleRepository evaluateSampleRepository;

    @Autowired
    private EvaluateAttachFileRepository evaluateAttachFileRepository;

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
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private MaClassService maClassService;
    @Autowired
    private KidsRepository kidsRepository;


    @Override
    public List<EvaluateDateResponse> searchEvaluateKidsDate(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.searchEvaluateKidsDate(idSchool, evaluateDateSearchRequest);
        List<EvaluateDateResponse> evaluateDateResponseList = listMapper.mapList(evaluateDateList, EvaluateDateResponse.class);
        return evaluateDateResponseList;
    }

    @Override
    public List<EvaluateDateKidsBriefResponse> searchEvaluateKidsBriefDate(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.searchEvaluateKidsDate(idSchool, evaluateDateSearchRequest);
        List<EvaluateDateKidsBriefResponse> evaluateDateKidsBriefResponseList = listMapper.mapList(evaluateDateList, EvaluateDateKidsBriefResponse.class);
        return evaluateDateKidsBriefResponseList;
    }

    @Override
    public EvaluateDateKidResponse findEvaluateKidsDateById(Long idSchool, Long id, UserPrincipal principal) {
        EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActive(id, idSchool, true).orElseThrow(() -> new NotFoundException("not found evaluate data by id"));
        EvaluateDateKidResponse evaluateDateKidResponse = modelMapper.map(evaluateDate, EvaluateDateKidResponse.class);
        return evaluateDateKidResponse;
    }

    @Transactional
    @Override
    public boolean updateIsApprovedOneKidsDate(Long idSchool, EvaluateDateApprovedRequest evaluateDateApprovedRequest) throws FirebaseMessagingException {
        Optional<EvaluateDate> evaluateDateOptional = evaluateDateRepository.findByIdAndIdSchoolAndDelActive(evaluateDateApprovedRequest.getId(), idSchool, true);
        if (evaluateDateOptional.isEmpty()) {
            return false;
        }
        EvaluateDate evaluateDate = evaluateDateOptional.get();
        evaluateDate.setApproved(evaluateDateApprovedRequest.isApproved());
        evaluateDateRepository.save(evaluateDate);
        return true;
    }

    //    fireBasse
    private void sendFireBaseDate(List<EvaluateDate> evaluateDateList, Long idTitle) throws FirebaseMessagingException {
        Optional<WebSystemTitle> webSystemTitle = webSystemTitleRepository.findById(idTitle);
        evaluateDateList = evaluateDateList.stream().filter(x -> x.getKids().getParent() != null).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(evaluateDateList)) {
            for (EvaluateDate evaluateDate : evaluateDateList) {
                Parent parent = evaluateDate.getKids().getParent();
                String title = webSystemTitle.get().getTitle();
                String content = webSystemTitle.get().getContent().replace("{Kid_Name}", evaluateDate.getKids().getFullName()).replace("{dd/mm/yyyy}", LocalDate.now().toString());
                List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(parent);
                if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                    NotifyRequest notifyRequest = new NotifyRequest();
                    notifyRequest.setBody(content);
                    notifyRequest.setTitle(title);
                    FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.EVALUATE, notifyRequest, evaluateDate.getKids().getId().toString());
                }
            }


        }
    }

    @Transactional
    @Override
    public boolean updateIsApprovedManyKidsDate(Long idSchool, List<EvaluateDateApprovedRequest> evaluateDateApprovedRequestList) throws FirebaseMessagingException {
        List<EvaluateDate> evaluateDateList = new ArrayList<>();
        for (EvaluateDateApprovedRequest x : evaluateDateApprovedRequestList) {
            Optional<EvaluateDate> evaluateDateOptional = evaluateDateRepository.findByIdAndIdSchoolAndDelActive(x.getId(), idSchool, true);
            if (evaluateDateOptional.isEmpty()) {
                return false;
            }
            EvaluateDate evaluateDate = evaluateDateOptional.get();
            evaluateDate.setApproved(x.isApproved());
            evaluateDateRepository.save(evaluateDate);
            evaluateDateList.add(evaluateDate);
        }
        return true;
    }


    @Transactional
    @Override
    public EvaluateDateResponse saveEvaluateOneKidDate(UserPrincipal userPrincipal, EvaluateDateKidRequest evaluateDateKidRequest) throws FirebaseMessagingException {
        CommonValidate.checkExistIdSchoolInPrinciple(userPrincipal);
        EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(evaluateDateKidRequest.getId(), userPrincipal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluatedate by id"));
        this.deleteFile(evaluateDate.getEvaluateAttachFileList(), evaluateDateKidRequest.getFileDeleteList());
        this.addFile(userPrincipal.getIdSchoolLogin(), evaluateDate, evaluateDateKidRequest.getMultipartFileList());
        this.setProperties(evaluateDate, evaluateDateKidRequest, userPrincipal);
        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDate);
        return modelMapper.map(evaluateDateSaved, EvaluateDateResponse.class);
    }

    @Transactional
    @Override
    public EvaluateDateKidsBriefResponse saveEvaluateOneKidDetailDate(UserPrincipal userPrincipal, EvaluateDateKidsBriefRequest evaluateDateKidsBriefRequest) throws FirebaseMessagingException {
        EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(evaluateDateKidsBriefRequest.getId(), userPrincipal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateDate by id"));
        this.deleteFile(evaluateDate.getEvaluateAttachFileList(), evaluateDateKidsBriefRequest.getFileDeleteList());
        this.addFile(userPrincipal.getIdSchoolLogin(), evaluateDate, evaluateDateKidsBriefRequest.getMultipartFileList());

        this.setPropertiesDetailDate(evaluateDate, evaluateDateKidsBriefRequest, userPrincipal);
        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDate);
        return modelMapper.map(evaluateDateSaved, EvaluateDateKidsBriefResponse.class);
    }

    @Transactional
    @Override
    public boolean saveEvaluateManyKidCommon(UserPrincipal userPrincipal, EvaluateDateKidsBriefCommonRequest evaluateDateKidsBriefCommonRequest) throws FirebaseMessagingException {
        for (Long x : evaluateDateKidsBriefCommonRequest.getIdKidList()) {
            EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(x, userPrincipal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateDate by id"));
            List<Long> longList = evaluateDate.getEvaluateAttachFileList().stream().map(y -> y.getId()).collect(Collectors.toList());
            this.deleteFile(evaluateDate.getEvaluateAttachFileList(), longList);
            this.addFile(userPrincipal.getIdSchoolLogin(), evaluateDate, evaluateDateKidsBriefCommonRequest.getMultipartFileList());

            EvaluateDateKidsBriefRequest evaluateDateKidsBriefRequest = modelMapper.map(evaluateDateKidsBriefCommonRequest, EvaluateDateKidsBriefRequest.class);
            this.setPropertiesDetailDate(evaluateDate, evaluateDateKidsBriefRequest, userPrincipal);
            evaluateDateRepository.save(evaluateDate);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean saveEvaluateManyKidsDetailDate(Long idSchool, UserPrincipal userPrincipal, List<EvaluateDateKidsBriefRequest> evaluateDateKidsBriefRequestList) throws FirebaseMessagingException {
        if (CollectionUtils.isEmpty(evaluateDateKidsBriefRequestList)) {
            return false;
        }
        for (EvaluateDateKidsBriefRequest x : evaluateDateKidsBriefRequestList) {
            EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActiveTrue(x.getId(), userPrincipal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateDate by id"));
            this.setPropertiesDetailDate(evaluateDate, x, userPrincipal);
            evaluateDateRepository.save(evaluateDate);
        }
        return true;
    }

    @Override
    public List<EvaluateSampleResponse> findEvaluateSampleByIdSchool(Long idSchool, UserPrincipal principal) {
        List<EvaluateSample> evaluateSampleList;
        if (principal.getSchoolConfig().isShowEvaluateSys()) {
            evaluateSampleList = evaluateSampleRepository.findAllEvaluateSample(idSchool, SystemConstant.ID_SYSTEM);
        } else {
            evaluateSampleList = evaluateSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(idSchool);
        }
        if (CollectionUtils.isEmpty(evaluateSampleList)) {
            return null;
        }
        List<EvaluateSampleResponse> evaluateSampleResponseList = listMapper.mapList(evaluateSampleList, EvaluateSampleResponse.class);
        return evaluateSampleResponseList;
    }

    @Override
    public List<EvaluateDateKidModel> convertEvaluateKidsToVM(List<EvaluateDateKidExcelResponse> evaluateDateResponses) {
        List<EvaluateDateKidModel> evaluateDateKidModelList = new ArrayList<>();
        long i = 1;
        for (EvaluateDateKidExcelResponse response : evaluateDateResponses) {
            i++;
            EvaluateDateKidModel model = new EvaluateDateKidModel();
            model.setId(i);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (response.getKids().getFullName() != null) {
                model.setKidName(response.getKids().getFullName());
            } else {
                model.setKidName("");
            }
            if (response.getDate() != null) {
                String evDate = response.getDate().format(dateTimeFormatter);
                model.setEvaluateDate(evDate);
            } else {
                model.setEvaluateDate("");
            }
            if (response.isApproved()) {
                model.setIsApproved("Đã duyệt");
            } else {
                model.setIsApproved("Chưa duyệt");
            }
            if (response.getLearnContent() != null) {
                model.setContentLearn(response.getLearnContent());
            } else {
                model.setContentLearn("");
            }
            if (response.getSleepContent() != null) {
                model.setContentSleep(response.getSleepContent());
            } else {
                model.setContentSleep("");
            }
            if (response.getHealtContent() != null) {
                model.setContentHealt(response.getHealtContent());
            } else {
                model.setContentHealt("");
            }
            if (response.getSanitaryContent() != null) {
                model.setContentSanitary(response.getSanitaryContent());
            } else {
                model.setContentSanitary("");
            }
            if (response.getCommonContent() != null) {
                model.setContentCommon(response.getCommonContent());
            } else {
                model.setContentCommon("");
            }
            evaluateDateKidModelList.add(model);
        }
        return evaluateDateKidModelList;
    }

    @Override
    public List<ExcelNewResponse> convertEvaluateKidsToVMNew(UserPrincipal principal, List<EvaluateDateKidExcelResponse> evaluateDateResponses) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        ExcelNewResponse response = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
        assert schoolResponse != null;
        List<String> headerStringList = Arrays.asList("BẢNG THỐNG KÊ NHẬN XÉT NGÀY", AppConstant.EXCEL_SCHOOL.concat(schoolResponse.getSchoolName()), AppConstant.EXCEL_DATE.concat(ConvertData.convertLocalDateToString(LocalDate.now())),"");
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = df1.format(LocalDate.now());
        response.setSheetName(date);
        ExcelDataNew headerMulti = this.setHeaderMulti();
        headerList.add(headerMulti);
        response.setHeaderList(headerList);
        List<EvaluateDateKidModel> evaluateDateKidModelList =  this.setEvaluateDateKidModel(evaluateDateResponses);
        for (EvaluateDateKidModel x : evaluateDateKidModelList){
            List<String> bodyStringList = Arrays.asList(String.valueOf(x.getId()), x.getKidName(), x.getIsApproved(), x.getContentLearn(),
                    x.getContentEat(), x.getContentSleep(), x.getContentSanitary(), x.getContentHealt(), x.getContentCommon());
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<EvaluateDateKidExcelResponse> searchEvaluateKidsDateExcel(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.searchEvaluateKidsDate(idSchool, evaluateDateSearchRequest);
        List<EvaluateDateKidExcelResponse> evaluateDateKidExcelResponseList = listMapper.mapList(evaluateDateList, EvaluateDateKidExcelResponse.class);
        return evaluateDateKidExcelResponseList;
    }

    @Override
    public List<ListIdKidDTO> totalEvaluateKidsDetailOfMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        if (evaluateDateSearchRequest.getDate() == null) {
            return null;
        }
        int month = evaluateDateSearchRequest.getDate().getMonthValue();
        int year = evaluateDateSearchRequest.getDate().getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        return evaluateDateRepository.totalEvaluateKidsDetailOfMonth(idSchool, evaluateDateSearchRequest.getIdClass(), dateStart, dateEnd);
        //return null;

    }

    /**
     * tìm kiếm nhận xét tất cả học sinh trong 1 lớp trong một tháng
     *
     * @param idSchool
     * @param evaluateDateSearchRequest
     * @return
     */

    @Override
    public List<EvaluateDateResponse> findEvaluateDateKidsClassOfMonth(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        if (evaluateDateSearchRequest.getDate() == null) {
            return null;
        }
        int month = evaluateDateSearchRequest.getDate().getMonthValue();
        int year = evaluateDateSearchRequest.getDate().getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<EvaluateDate> attendanceKidsList = evaluateDateRepository.searchEvaluateKidsDateOfMonth(idSchool, evaluateDateSearchRequest.getIdClass(), dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        List<EvaluateDateResponse> evaluateDateResponses = listMapper.mapList(attendanceKidsList, EvaluateDateResponse.class);
        return evaluateDateResponses;
    }

    @Override
    public List<EvaluateDateKidExcelResponse> findEvaluateDateKidsClassOfMonthToExcel(Long idSchool, EvaluateDateSearchRequest evaluateDateSearchRequest) {
        if (evaluateDateSearchRequest.getDate() == null) {
            return null;
        }
        int month = evaluateDateSearchRequest.getDate().getMonthValue();
        int year = evaluateDateSearchRequest.getDate().getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<EvaluateDate> attendanceKidsList = evaluateDateRepository.searchEvaluateKidsDateOfMonth(idSchool, evaluateDateSearchRequest.getIdClass(), dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        List<EvaluateDateKidExcelResponse> evaluateDateResponses = listMapper.mapList(attendanceKidsList, EvaluateDateKidExcelResponse.class);
        return evaluateDateResponses;
    }

    @Override
    public Map<Long, List<EvaluateDateKidModel>> detachedListEvaluateKidsClassOfMonth(List<EvaluateDateKidExcelResponse> evaluateDateKidExcelResponses, List<ListIdKidDTO> kidDTOList) {
        if (evaluateDateKidExcelResponses == null || kidDTOList == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Map<Long, List<EvaluateDateKidModel>> map = new HashMap<>();

        for (ListIdKidDTO listIdKidDTO : kidDTOList) {
            List<EvaluateDateKidExcelResponse> responseList = new ArrayList<>();
            for (EvaluateDateKidExcelResponse evaluateDateKidExcelResponse : evaluateDateKidExcelResponses) {
                if (evaluateDateKidExcelResponse.getKids().getId().equals(listIdKidDTO.getId())) {
                    responseList.add(evaluateDateKidExcelResponse);
                }
            }
            List<EvaluateDateKidModel> evaluateDateKidModels = new ArrayList<>();
            long i = 1;
            for (EvaluateDateKidExcelResponse response : responseList) {
                i++;
                EvaluateDateKidModel model = new EvaluateDateKidModel();
                model.setId(i);
                if (response.getKids().getFullName() != null) {
                    model.setKidName(response.getKids().getFullName());
                } else {
                    model.setKidName("");
                }
                if (response.getDate() != null) {
                    String evaluateDate = response.getDate().format(df);
                    model.setEvaluateDate(evaluateDate);
                } else {
                    model.setEvaluateDate("");
                }
                if (response.isApproved()) {
                    model.setIsApproved("Đã duyệt");
                } else {
                    model.setIsApproved("Chưa duyệt");
                }
                if (response.getLearnContent() != null) {
                    model.setContentLearn(response.getLearnContent());
                } else {
                    model.setContentLearn("");
                }
                if (response.getEatContent() != null) {
                    model.setContentEat(response.getEatContent());
                } else {
                    model.setContentEat("");
                }
                if (response.getSleepContent() != null) {
                    model.setContentSleep(response.getSleepContent());
                } else {
                    model.setContentSleep("");
                }
                if (response.getHealtContent() != null) {
                    model.setContentHealt(response.getHealtContent());
                } else {
                    model.setContentHealt("");
                }
                if (response.getSanitaryContent() != null) {
                    model.setContentSanitary(response.getSanitaryContent());
                } else {
                    model.setContentSanitary("");
                }
                if (response.getCommonContent() != null) {
                    model.setContentCommon(response.getCommonContent());
                } else {
                    model.setContentCommon("");
                }
                evaluateDateKidModels.add(model);
            }
            map.put(listIdKidDTO.getId(), evaluateDateKidModels);
        }
        return map;
    }

    @Override
    public List<ExcelNewResponse> detachedListEvaluateKidsClassOfMonthNew(List<EvaluateDateKidExcelResponse> evaluateDateResponses, List<ListIdKidDTO> kidDTOList, Long idSchool, Long idClass, LocalDate date) {
        if (evaluateDateResponses == null || kidDTOList == null) {
            return null;
        }
        List<ExcelNewResponse> responseList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";
        MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
        String className = classDTO != null ? classDTO.getClassName() : "";
        int month = date.getMonthValue();
        int year = date.getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        for (ListIdKidDTO listIdKidDTO : kidDTOList) {
            List<EvaluateDateKidExcelResponse> responseList1 = new ArrayList<>();
            for (EvaluateDateKidExcelResponse evaluateDateKidExcelResponse : evaluateDateResponses) {
                if (evaluateDateKidExcelResponse.getKids().getId().equals(listIdKidDTO.getId())) {
                    responseList1.add(evaluateDateKidExcelResponse);
                }
            }
            ExcelNewResponse response = new ExcelNewResponse();
            List<ExcelDataNew> bodyList = new ArrayList<>();
            Kids kids = kidsRepository.findById(listIdKidDTO.getId()).orElseThrow();
            String kidsName = kids.getFullName();
            List<String> headerStringList = Arrays.asList("BẢNG THỐNG KÊ NHẬN XÉT THÁNG", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(className), AppConstant.EXCEL_KIDS_NAME.concat(kidsName), AppConstant.EXCEL_DATE.concat(dateToStr.concat(" - ").concat(dateToStrSheet)));
            List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
            ExcelDataNew headerMulti = this.setHeaderMulti();
            headerList.add(headerMulti);
            response.setSheetName(kidsName.concat(AppConstant.SPACE_EXPORT_ID.concat(kids.getId().toString())));
            response.setHeaderList(headerList);
            List<EvaluateDateKidModel> evaluateDateKidModelList =  this.setEvaluateDateKidModel(responseList1);
            for (EvaluateDateKidModel x : evaluateDateKidModelList){
                List<String> bodyStringList = Arrays.asList(String.valueOf(x.getId()), x.getEvaluateDate(), x.getIsApproved(), x.getContentLearn(), x.getContentEat(),
                        x.getContentSleep(), x.getContentSanitary(), x.getContentHealt(), x.getContentCommon());
                ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    private List<EvaluateDateKidModel> setEvaluateDateKidModel(List<EvaluateDateKidExcelResponse> evaluateDateResponses){
        List<EvaluateDateKidModel> evaluateDateKidModelList = new ArrayList<>();
        long i = 0;
        for (EvaluateDateKidExcelResponse response : evaluateDateResponses) {
            i++;
            EvaluateDateKidModel model = new EvaluateDateKidModel();
            model.setId(i);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (response.getKids().getFullName() != null) {
                model.setKidName(response.getKids().getFullName());
            } else {
                model.setKidName("");
            }
            if (response.getDate() != null) {
                String evDate = response.getDate().format(dateTimeFormatter);
                model.setEvaluateDate(evDate);
            } else {
                model.setEvaluateDate("");
            }
            if (response.isApproved()) {
                model.setIsApproved("Đã duyệt");
            } else {
                model.setIsApproved("Chưa duyệt");
            }
            if (response.getLearnContent() != null) {
                model.setContentLearn(response.getLearnContent());
            } else {
                model.setContentLearn("");
            }
            if (response.getSleepContent() != null) {
                model.setContentSleep(response.getSleepContent());
            } else {
                model.setContentSleep("");
            }
            if (response.getHealtContent() != null) {
                model.setContentHealt(response.getHealtContent());
            } else {
                model.setContentHealt("");
            }
            if (response.getSanitaryContent() != null) {
                model.setContentSanitary(response.getSanitaryContent());
            } else {
                model.setContentSanitary("");
            }
            if (response.getCommonContent() != null) {
                model.setContentCommon(response.getCommonContent());
            } else {
                model.setContentCommon("");
            }
            evaluateDateKidModelList.add(model);
        }
        return evaluateDateKidModelList;
    }

    private ExcelDataNew setHeaderMulti(){
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("NHẬN XÉT");
        headerMulti.setPro2("");
        headerMulti.setPro3("");
        headerMulti.setPro4("NỘI DUNG");
        headerMulti.setPro5("");
        headerMulti.setPro6("");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("");
        return headerMulti;
    }


    /**
     * add file
     *
     * @param idSchool
     * @param evaluateDate
     * @param multipartFileList
     */
    private void addFile(Long idSchool, EvaluateDate evaluateDate, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EvaluateAttachFile evaluateAttachFile = new EvaluateAttachFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.NHAN_XET) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.NHAN_XET) + fileName;
                evaluateAttachFile.setName(multipartFile.getOriginalFilename());
                evaluateAttachFile.setUrl(urlWeb);
                evaluateAttachFile.setUrlLocal(urlLocal);
                evaluateAttachFile.setEvaluateDate(evaluateDate);
                evaluateAttachFileRepository.save(evaluateAttachFile);
            });
        }
    }

    /**
     * delete file
     *
     * @param evaluateAttachFileList
     * @param fileDeleteList
     */
    private void deleteFile(List<EvaluateAttachFile> evaluateAttachFileList, List<Long> fileDeleteList) {
        if (CollectionUtils.isEmpty(evaluateAttachFileList) || CollectionUtils.isEmpty(fileDeleteList)) {
            return;
        }
        fileDeleteList.forEach(x -> {
            List<EvaluateAttachFile> attachFileList = evaluateAttachFileList.stream().filter(y -> y.getId().equals(x)).collect(Collectors.toList());
            if (attachFileList.size() == 1) {
                EvaluateAttachFile evaluateAttachFile = attachFileList.get(0);
                //delete trong db cần remove đối tượng trong mảng muốn xóa
                evaluateAttachFileList.remove(evaluateAttachFile);
                HandleFileUtils.deleteFileOrPictureInFolder(evaluateAttachFile.getUrlLocal());
                evaluateAttachFileRepository.deleteById(x);
            }
        });
    }

    /**
     * set properties for evlauate date
     *
     * @param evaluateDate
     * @param evaluateDateKidRequest
     * @param principal
     */
    private void setProperties(EvaluateDate evaluateDate, EvaluateDateKidRequest evaluateDateKidRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        boolean checkSave = false;
        boolean checkReply = false;
        if (!evaluateDate.getLearnContent().equals(evaluateDateKidRequest.getLearnContent())) {
            evaluateDate.setLearnContent(evaluateDateKidRequest.getLearnContent());
            evaluateDate.setLearnIdCreated(idCreated);
            evaluateDate.setLearnCreatedBy(fullName);
            evaluateDate.setLearnDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getEatContent().equals(evaluateDateKidRequest.getEatContent())) {
            evaluateDate.setEatContent(evaluateDateKidRequest.getEatContent());
            evaluateDate.setEatIdCreated(idCreated);
            evaluateDate.setEatCreatedBy(fullName);
            evaluateDate.setEatDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getSleepContent().equals(evaluateDateKidRequest.getSleepContent())) {
            evaluateDate.setSleepContent(evaluateDateKidRequest.getSleepContent());
            evaluateDate.setSleepIdCreated(idCreated);
            evaluateDate.setSleepCreatedBy(fullName);
            evaluateDate.setSleepDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getSanitaryContent().equals(evaluateDateKidRequest.getSanitaryContent())) {
            evaluateDate.setSanitaryContent(evaluateDateKidRequest.getSanitaryContent());
            evaluateDate.setSanitaryIdCreated(idCreated);
            evaluateDate.setSanitaryCreatedBy(fullName);
            evaluateDate.setSanitaryDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getHealtContent().equals(evaluateDateKidRequest.getHealtContent())) {
            evaluateDate.setHealtContent(evaluateDateKidRequest.getEatContent());
            evaluateDate.setHealtIdCreated(idCreated);
            evaluateDate.setHealtCreatedBy(fullName);
            evaluateDate.setHealtDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getCommonContent().equals(evaluateDateKidRequest.getCommonContent())) {
            evaluateDate.setCommonContent(evaluateDateKidRequest.getCommonContent());
            evaluateDate.setCommonIdCreated(idCreated);
            evaluateDate.setCommonCreatedBy(fullName);
            evaluateDate.setCommonDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getCommonContent().equals(evaluateDateKidRequest.getCommonContent())) {
            evaluateDate.setCommonContent(evaluateDateKidRequest.getCommonContent());
            evaluateDate.setCommonIdCreated(idCreated);
            evaluateDate.setCommonCreatedBy(fullName);
            evaluateDate.setCommonDatetime(localDateTime);
            checkSave = true;
        }
        if (AppTypeConstant.TEACHER.equals(principal.getAppType()) && !evaluateDate.getTeacherReplyContent().equals(evaluateDateKidRequest.getTeacherReplyContent())) {
            evaluateDate.setTeacherReplyContent(evaluateDateKidRequest.getTeacherReplyContent());
            evaluateDate.setTeacherReplyIdCreated(idCreated);
            evaluateDate.setTeacherReplyCreatedBy(fullName);
            evaluateDate.setTeacherReplyDatetime(localDateTime);
            checkReply = true;
        }
        if (AppTypeConstant.SCHOOL.equals(principal.getAppType()) && !evaluateDate.getSchoolReplyContent().equals(evaluateDateKidRequest.getSchoolReplyContent())) {
            evaluateDate.setSchoolReplyContent(evaluateDateKidRequest.getSchoolReplyContent());
            evaluateDate.setSchoolReplyIdCreated(idCreated);
            evaluateDate.setSchoolReplyCreatedBy(fullName);
            evaluateDate.setSchoolReplyDatetime(localDateTime);
            checkReply = true;
        }
        //check set duyệt cho lần đầu tạo nhận xét
        if (checkSave || !CollectionUtils.isEmpty(evaluateDateKidRequest.getMultipartFileList())) {
            //gửi firebase cho lần đầu nhận xét
            if (evaluateDate.getIdCreated() == null || evaluateDate.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
                evaluateDate.setIdCreated(idCreated);
                evaluateDate.setCreatedDate(localDateTime);
                evaluateDate.setIdModified(idCreated);
                evaluateDate.setLastModifieDate(localDateTime);
                evaluateDate.setApproved(principal.getSchoolConfig().isEvaluate());
                if (evaluateDate.isApproved()) {
                    firebaseFunctionService.sendParentByPlusNoContent(38L, evaluateDate.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
                }
            } else {
                evaluateDate.setIdModified(idCreated);
                evaluateDate.setLastModifieDate(localDateTime);
            }
        }
        if (checkSave || checkReply) {
            evaluateDate.setParentRead(AppConstant.APP_FALSE);
        }
        evaluateDate.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateDate.setTeacherReplyDel(evaluateDateKidRequest.isTeacherReplyDel());
        evaluateDate.setSchoolReplyDel(evaluateDateKidRequest.isSchoolReplyDel());

    }

    /**
     * set properties for evlauate detail date
     *
     * @param evaluateDate
     * @param evaluateDateKidsBriefRequest
     * @param principal
     */
    private void setPropertiesDetailDate(EvaluateDate evaluateDate, EvaluateDateKidsBriefRequest evaluateDateKidsBriefRequest, UserPrincipal principal) throws FirebaseMessagingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long idCreated = principal.getId();
        String fullName = principal.getFullName();
        boolean checkSave = false;
        if (!evaluateDate.getLearnContent().equals(evaluateDateKidsBriefRequest.getLearnContent())) {
            evaluateDate.setLearnContent(evaluateDateKidsBriefRequest.getLearnContent());
            evaluateDate.setLearnIdCreated(idCreated);
            evaluateDate.setLearnCreatedBy(fullName);
            evaluateDate.setLearnDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getEatContent().equals(evaluateDateKidsBriefRequest.getEatContent())) {
            evaluateDate.setEatContent(evaluateDateKidsBriefRequest.getEatContent());
            evaluateDate.setEatIdCreated(idCreated);
            evaluateDate.setEatCreatedBy(fullName);
            evaluateDate.setEatDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getSleepContent().equals(evaluateDateKidsBriefRequest.getSleepContent())) {
            evaluateDate.setSleepContent(evaluateDateKidsBriefRequest.getSleepContent());
            evaluateDate.setSleepIdCreated(idCreated);
            evaluateDate.setSleepCreatedBy(fullName);
            evaluateDate.setSleepDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getSanitaryContent().equals(evaluateDateKidsBriefRequest.getSanitaryContent())) {
            evaluateDate.setSanitaryContent(evaluateDateKidsBriefRequest.getSanitaryContent());
            evaluateDate.setSanitaryIdCreated(idCreated);
            evaluateDate.setSanitaryCreatedBy(fullName);
            evaluateDate.setSanitaryDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getHealtContent().equals(evaluateDateKidsBriefRequest.getHealtContent())) {
            evaluateDate.setHealtContent(evaluateDateKidsBriefRequest.getEatContent());
            evaluateDate.setHealtIdCreated(idCreated);
            evaluateDate.setHealtCreatedBy(fullName);
            evaluateDate.setHealtDatetime(localDateTime);
            checkSave = true;
        }
        if (!evaluateDate.getCommonContent().equals(evaluateDateKidsBriefRequest.getCommonContent())) {
            evaluateDate.setCommonContent(evaluateDateKidsBriefRequest.getCommonContent());
            evaluateDate.setCommonIdCreated(idCreated);
            evaluateDate.setCommonCreatedBy(fullName);
            evaluateDate.setCommonDatetime(localDateTime);
            checkSave = true;
        }
        evaluateDate.setParentRead(AppConstant.APP_FALSE);

        //check set duyệt cho lần đầu tạo nhận xét
        if (checkSave) {
            if (evaluateDate.getIdCreated() == null || evaluateDate.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
                evaluateDate.setIdCreated(idCreated);
                evaluateDate.setCreatedDate(localDateTime);
                evaluateDate.setIdModified(idCreated);
                evaluateDate.setLastModifieDate(localDateTime);
                evaluateDate.setApproved(principal.getSchoolConfig().isEvaluate());
                if (evaluateDate.isApproved()) {
                    firebaseFunctionService.sendParentByPlusNoContent(38L, evaluateDate.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
                }
            } else {
                evaluateDate.setIdModified(idCreated);
                evaluateDate.setLastModifieDate(localDateTime);
            }
        }
    }

}
