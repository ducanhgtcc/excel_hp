package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.dto.KidsHeightWeightDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.entity.sample.HeightSample;
import com.example.onekids_project.entity.sample.WeightSample;
import com.example.onekids_project.importexport.model.HeightWeightModel;
import com.example.onekids_project.importexport.model.KidsHeightModel;
import com.example.onekids_project.importexport.model.KidsWeightModel;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.kidsheightweight.*;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HeightWeightService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeightWeightServiceImpl implements HeightWeightService {

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HeightSampleRepository heightSampleRepository;

    @Autowired
    private KidsWeightRepository kidsWeightRepository;

    @Autowired
    private KidsHeightRepository kidsHeightRepository;

    @Autowired
    private WeightSampleRepository weightSampleRepository;

    @Autowired
    private KidsService kidsService;

    @Autowired
    private MaClassService maClassService;

    @Autowired
    private SchoolService schoolService;

    @Override
    public ListKidsHeightWeightResponse searchAddHeightWeight(UserPrincipal principal, SearchKidsHeightWeightRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.searchKidsHeightWeight(idSchool, request);
        String appType = principal.getAppType();
        List<KidsHeightWeightResponse> kidsHeightWeightResponseList = listMapper.mapList(kidsList, KidsHeightWeightResponse.class);
        String localDate = request.getDate();
        //search lich su
        if (StringUtils.isBlank(localDate)) {
            kidsHeightWeightResponseList.forEach(x -> {
                //lấy ngày đo mới nhất
                if (!CollectionUtils.isEmpty(x.getKidsHeightList())) {
                    KidsHeightResponse kidsHeightResponse = x.getKidsHeightList().stream().filter(y -> y.getTimeHeight() != null).max(Comparator.comparing(KidsHeightResponse::getTimeHeight)).orElseThrow(NoSuchElementException::new);
                    if (AppTypeConstant.SCHOOL.equals(appType) || AppTypeConstant.TEACHER.equals(appType)) {
                        if (AppTypeConstant.SCHOOL.equals(kidsHeightResponse.getAppType()) || AppTypeConstant.TEACHER.equals(kidsHeightResponse.getAppType())) {
                            x.setHeight(kidsHeightResponse.getHeight());
                            x.setTimeHeight(kidsHeightResponse.getTimeHeight());
                        }
                    } else if (AppTypeConstant.PARENT.equals(appType)) {
                        x.setHeight(kidsHeightResponse.getHeight());
                        x.setTimeHeight(kidsHeightResponse.getTimeHeight());
                    }
                }
                //lấy ngày cân mới nhất
                if (!CollectionUtils.isEmpty(x.getKidsWeightList())) {
                    KidsWeightResponse kidsWeightResponse = x.getKidsWeightList().stream().filter(y -> y.getTimeWeight() != null).max(Comparator.comparing(KidsWeightResponse::getTimeWeight)).orElseThrow(NoSuchElementException::new);
                    if (AppTypeConstant.SCHOOL.equals(appType) || AppTypeConstant.TEACHER.equals(appType)) {
                        if (AppTypeConstant.SCHOOL.equals(kidsWeightResponse.getAppType()) || AppTypeConstant.TEACHER.equals(kidsWeightResponse.getAppType())) {
                            x.setWeight(kidsWeightResponse.getWeight());
                            x.setTimeWeight(kidsWeightResponse.getTimeWeight());
                        }
                    } else if (AppTypeConstant.PARENT.equals(appType)) {
                        x.setWeight(kidsWeightResponse.getWeight());
                        x.setTimeWeight(kidsWeightResponse.getTimeWeight());
                    }
                }
            });
        } else {
            //search thêm cân đo
            LocalDate date = LocalDate.parse(localDate);
            kidsHeightWeightResponseList.forEach(x -> {
                List<KidsHeightResponse> kidsHeightResponseList = x.getKidsHeightList();
                if (!CollectionUtils.isEmpty(kidsHeightResponseList)) {
                    List<KidsHeightResponse> heightResponseList = kidsHeightResponseList.stream().filter(y -> y.isDelActive() && !y.getAppType().equals(AppTypeConstant.PARENT) && y.getTimeHeight() != null && date.isEqual(y.getTimeHeight())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(heightResponseList)) {
                        KidsHeightResponse kidsHeightResponse = heightResponseList.get(heightResponseList.size() - 1);
                        x.setHeight(kidsHeightResponse.getHeight());
                        x.setTimeHeight(kidsHeightResponse.getTimeHeight());
                    }
                }
                List<KidsWeightResponse> kidsWeightResponseList = x.getKidsWeightList();
                if (!CollectionUtils.isEmpty(kidsWeightResponseList)) {
                    List<KidsWeightResponse> weightResponseList = kidsWeightResponseList.stream().filter(y -> y.isDelActive() && !y.getAppType().equals(AppTypeConstant.PARENT) && y.getTimeWeight() != null && date.isEqual(y.getTimeWeight())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(weightResponseList)) {
                        KidsWeightResponse kidsWeightResponse = weightResponseList.get(weightResponseList.size() - 1);
                        x.setWeight(kidsWeightResponse.getWeight());
                        x.setTimeWeight(kidsWeightResponse.getTimeWeight());
                    }
                }
            });
        }
        ListKidsHeightWeightResponse listKidsHeightWeightResponse = new ListKidsHeightWeightResponse();
        listKidsHeightWeightResponse.setKidsHeightWeightResponse(kidsHeightWeightResponseList);
        return listKidsHeightWeightResponse;
    }

    @Override
    public Optional<KidsHeightWeightDTO> findByIdHeightWeight(Long idSchool, Long id) {
        Optional<Kids> optionalKids = kidsRepository.findByIdAndDelActiveTrue(id);
        if (optionalKids.isEmpty()) {
            return Optional.empty();
        }
        Optional<KidsHeightWeightDTO> optionalKidsHeightWeightDTO = Optional.ofNullable(modelMapper.map(optionalKids.get(), KidsHeightWeightDTO.class));
        if (optionalKidsHeightWeightDTO.isEmpty()) {
            return Optional.empty();
        }
        return optionalKidsHeightWeightDTO;
    }

    @Override
    public KidsHeightWeightResponse createKidsWeightHeight(Long idSchool, CreateKidsHeightWeightRequest createKidsHeightWeightRequest) {
        Optional<Kids> kidsOptional = kidsRepository.findByIdKid(idSchool, createKidsHeightWeightRequest.getId());
        if (kidsOptional.isEmpty()) {
            return null;
        }
        Kids kid = kidsOptional.get();
        KidsHeight newHeight = modelMapper.map(createKidsHeightWeightRequest, KidsHeight.class);
        return null;
    }
    /* List danh sách cân nặng chiều cao tất cả các học sinh theo tùy chọn
     * @Param searchStudentRequest
     */

    @Override
    public ListKidsHeightWeightResponse searchMenuHeightWeight(Long idSchool, SearchKidsHeightWeightRequest searchKidsHeightWeightRequest) {

//        List<Kids> kidsList = kidsRepository.searchMenuKidsHeightWeight(idSchool, searchKidsHeightWeightRequest);
//        if (CollectionUtils.isEmpty(kidsList)) {
//            return null;
//        }
        List<Kids> kidsList = new ArrayList<>();
        if (searchKidsHeightWeightRequest.getIdKidsList() != null) {
            for (Long id : searchKidsHeightWeightRequest.getIdKidsList()) {
                Kids kidsHeightWeight = kidsRepository.getOne(id);
                kidsList.add(kidsHeightWeight);
            }
        }

        List<KidsHeightWeightResponse> kidsHeightWeightResponses = listMapper.mapList(kidsList, KidsHeightWeightResponse.class);
        String localDate = searchKidsHeightWeightRequest.getDate();
        //search lich su
        if (StringUtils.isBlank(localDate)) {
            kidsHeightWeightResponses.forEach(x -> {
                //lấy ngày đo mới nhất
                if (!CollectionUtils.isEmpty(x.getKidsHeightList())) {
                    KidsHeightResponse kidsHeightResponse = x.getKidsHeightList().stream().filter(y -> y.getTimeHeight() != null).max(Comparator.comparing(KidsHeightResponse::getTimeHeight)).orElseThrow(NoSuchElementException::new);
                    x.setHeight(kidsHeightResponse.getHeight());
                    x.setTimeHeight(kidsHeightResponse.getTimeHeight());
                }
                //lấy ngày cân mới nhất
                if (!CollectionUtils.isEmpty(x.getKidsWeightList())) {
                    KidsWeightResponse kidsWeightResponse = x.getKidsWeightList().stream().filter(y -> y.getTimeWeight() != null).max(Comparator.comparing(KidsWeightResponse::getTimeWeight)).orElseThrow(NoSuchElementException::new);
                    x.setWeight(kidsWeightResponse.getWeight());
                    x.setTimeWeight(kidsWeightResponse.getTimeWeight());
                }
//                String a = x.get
            });
        }
        ListKidsHeightWeightResponse listKidsHeightWeightResponse = new ListKidsHeightWeightResponse();
        listKidsHeightWeightResponse.setKidsHeightWeightResponse(kidsHeightWeightResponses);
        return listKidsHeightWeightResponse;
    }

    @Override
    public List<KidsHeightWeightResponse> findKidsHeightWeightToExcel(Long idSchool, SearchKidsHeightWeightRequest searchKidsHeightWeightRequest) {
        List<KidsHeightWeightResponse> responseList = new ArrayList<>();
        searchKidsHeightWeightRequest.getIdKidsList().stream().forEach(x -> {
            //Lấy all chiều cao - cân nặng theo id
            List<KidsWeight> kidsWeightList = kidsWeightRepository.findByKidsIdAndDelActiveTrue(x);
            List<KidsHeight> kidsHeightList = kidsHeightRepository.findByKidsIdAndDelActiveTrue(x);
            KidsDTO kidsDTO = kidsService.findByIdKid(idSchool, x).stream().findFirst().orElse(null);

            KidsHeightWeightResponse response = new KidsHeightWeightResponse();
            response.setId(x);
            response.setFullName(kidsDTO.getFullName());

            List<KidsHeightResponse> kidsHeightResponse = listMapper.mapList(kidsHeightList, KidsHeightResponse.class);
            List<KidsWeightResponse> kidsWeightResponse = listMapper.mapList(kidsWeightList, KidsWeightResponse.class);
            response.setKidsHeightList(kidsHeightResponse);
            response.setKidsWeightList(kidsWeightResponse);

            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<HeightWeightModel> detachedListKidsHeightWeightAll(List<KidsHeightWeightResponse> kidsHeightWeightResponses, List<Long> kidDTOList) {
        if (kidsHeightWeightResponses == null || kidDTOList == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<HeightWeightModel> heightWeightModelList = new ArrayList<>();

        for (Long idKidDTO : kidDTOList) {
            HeightWeightModel heightWeightModel = new HeightWeightModel();
            long i = 0;
            for (KidsHeightWeightResponse response : kidsHeightWeightResponses) {
                if (idKidDTO.equals(response.getId())) {
                    i++;
                    heightWeightModel.setStt(i);
                    heightWeightModel.setId(response.getId());

                    if (response.getFullName() != null) {
                        heightWeightModel.setKidName(response.getFullName());
                    } else {
                        heightWeightModel.setKidName("");
                    }
                    List<KidsHeightModel> kidsHeightModel = listMapper.mapList(response.getKidsHeightList(), KidsHeightModel.class);
                    List<KidsWeightModel> kidsWeightModel = listMapper.mapList(response.getKidsWeightList(), KidsWeightModel.class);
                    heightWeightModel.setKidsHeightModelList(kidsHeightModel);
                    heightWeightModel.setKidsWeightModelList(kidsWeightModel);
                }
            }
            heightWeightModelList.add(heightWeightModel);
        }
        return heightWeightModelList;
    }

    @Override
    public List<ExcelResponse> detachedListKidsHeightWeightAllNew(List<KidsHeightWeightResponse> kidsHeightWeightResponses, List<Long> kidDTOList, Long idSchool, Long idClass) {
        if (kidsHeightWeightResponses == null || kidDTOList == null) {
            return null;
        }
        List<ExcelResponse> responseList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("###.#");
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";
        MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
        String className = classDTO != null ? classDTO.getClassName() : "";
        for (Long idKidDTO : kidDTOList) {
            ExcelResponse response = new ExcelResponse();
            List<ExcelData> bodyList = new ArrayList<>();
            long i = 0;
            for (KidsHeightWeightResponse x : kidsHeightWeightResponses) {
                if (idKidDTO.equals(x.getId())) {
                    List<String> headerStringList = Arrays.asList("BẢNG KÊ SỐ ĐO CHIỀU CAO - CÂN NẶNG", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(className), AppConstant.EXCEL_KIDS_NAME.concat(x.getFullName()));
                    List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                    ExcelData headerMulti = this.setHeaderMulti();
                    headerList.add(headerMulti);
                    response.setSheetName(x.getFullName().concat(AppConstant.SPACE_EXPORT_ID.concat(x.getId().toString())));
                    response.setHeaderList(headerList);
                    for (KidsHeightResponse kidsHeightResponse : x.getKidsHeightList()) {
                        i++;
                        List<String> bodyStringList = Arrays.asList(String.valueOf(i), "", "", kidsHeightResponse.getHeight() != null ? df.format(kidsHeightResponse.getHeight()) : "", kidsHeightResponse.getTimeHeight() != null ? String.valueOf(kidsHeightResponse.getTimeHeight()) : "");
                        ExcelData modelData1 = ExportExcelUtils.setBodyExcel(bodyStringList);
                        bodyList.add(modelData1);
                    }
                    long j = i;
                    for (KidsWeightResponse kidsWeightResponse : x.getKidsWeightList()) {
                        j++;
                        List<String> bodyStringList = Arrays.asList(String.valueOf(j), kidsWeightResponse.getWeight() != null ? df.format(kidsWeightResponse.getWeight()) : "", kidsWeightResponse.getTimeWeight() != null ? String.valueOf(kidsWeightResponse.getTimeWeight()) : "", "", "");
                        ExcelData modelData2 = ExportExcelUtils.setBodyExcel(bodyStringList);
                        bodyList.add(modelData2);
                    }
                }
                response.setBodyList(bodyList);
            }
            responseList.add(response);
        }
        return responseList;
    }

    /**
     * Chuyển đổi đối tượng KidByHeightWeightResponse thành KidVM để đổ dữ liệu lên excel
     *
     * @param listKidsHeightWeightResponse
     * @return
     */
    @Override
    public List<HeightWeightModel> getFileAllKidByHeightWeight(ListKidsHeightWeightResponse listKidsHeightWeightResponse, String nameSchool) {

        List<HeightWeightModel> heightWeightModels = new ArrayList<>();

//        SchoolDTO school = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
//        String nameSchool  = school.getSchoolName()

        long i = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (KidsHeightWeightResponse kidsHeightWeightResponse : listKidsHeightWeightResponse.getKidsHeightWeightResponse()) {
            HeightWeightModel heightWeightModel = new HeightWeightModel();
            heightWeightModel.setStt(i++);
            if (kidsHeightWeightResponse.getFullName() != null) {
                heightWeightModel.setKidName(kidsHeightWeightResponse.getFullName());
            } else {
                heightWeightModel.setKidName("");
            }
            if (kidsHeightWeightResponse.getWeight() != null) {
                heightWeightModel.setWeight(kidsHeightWeightResponse.getWeight());
            } else {
                heightWeightModel.setWeight(null);
            }
            if (kidsHeightWeightResponse.getWeight() != null) {
                heightWeightModel.setTimeWeight(kidsHeightWeightResponse.getTimeWeight());
            } else {
                heightWeightModel.setTimeWeight(null);
            }
            if (kidsHeightWeightResponse.getHeight() != null) {
                heightWeightModel.setHeight(kidsHeightWeightResponse.getHeight());
            } else {
                heightWeightModel.setHeight(null);
            }
            if (kidsHeightWeightResponse.getTimeHeight() != null) {
//                LocalDate dateStart = kidsHeightWeightResponse.getTimeHeight().format(formatter);
                heightWeightModel.setTimeHeight(kidsHeightWeightResponse.getTimeHeight());
            } else {
                heightWeightModel.setTimeHeight(null);
            }

            heightWeightModels.add(heightWeightModel);
        }
        return heightWeightModels;
    }

    @Override
    public List<ExcelResponse> getFileAllKidByHeightWeightNew(ListKidsHeightWeightResponse listKidsHeightWeightResponse, Long idSchool, Long idClass) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("###.#");
        long i = 1;
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        assert schoolResponse != null;
        String schoolName = schoolResponse.getSchoolName();
        MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
        String className = classDTO != null ? classDTO.getClassName() : "";
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateToStrSheet = df1.format(LocalDate.now());
        List<String> headerStringList = Arrays.asList("BẢNG KÊ SỐ ĐO CHIỀU CAO - CÂN NẶNG MỚI NHẤT", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_DATE.concat(ConvertData.convertLocalDateToString(LocalDate.now())), AppConstant.EXCEL_CLASS.concat(className));
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        ExcelData headerMulti1 = this.setHeaderMulti1();
        headerList.add(headerMulti1);
        response.setSheetName(dateToStrSheet);
        response.setHeaderList(headerList);
        for (KidsHeightWeightResponse x : listKidsHeightWeightResponse.getKidsHeightWeightResponse()) {
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), x.getWeight() != null ? df.format(x.getWeight()) : "", x.getTimeWeight() != null ? String.valueOf(x.getTimeWeight()) : "",
                    x.getHeight() != null ? df.format(x.getHeight()) : "", x.getTimeHeight() != null ? String.valueOf(x.getTimeHeight()) : "");
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<HeightWeightSampleRespone> findHeightWeightSample(Long idKid) {
        List<HeightWeightSampleRespone> responeList = new ArrayList<>();
        Kids kids = kidsRepository.findByIdAndDelActive(idKid, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found kids by id"));
        String type = kids.getGender().equals("Nam") ? AppConstant.BOY : AppConstant.GIRL;
        List<HeightSample> heightSampleList = heightSampleRepository.findByType(type);
        List<WeightSample> weightSampleList = weightSampleRepository.findByType(type);

        List<String> yearOldList = heightSampleList.stream().map(HeightSample::getYearOld).collect(Collectors.toList());
        yearOldList.forEach(x -> {
            List<HeightSample> heightList = heightSampleList.stream().filter(a -> a.getYearOld().equals(x)).collect(Collectors.toList());
            List<WeightSample> weightList = weightSampleList.stream().filter(a -> a.getYearOld().equals(x)).collect(Collectors.toList());
            HeightWeightSampleRespone model = new HeightWeightSampleRespone();
            model.setYearOld(x);
            if (!CollectionUtils.isEmpty(heightList)) {
                HeightSample heightSample = heightList.get(0);
                model.setMinH(heightSample.getMin());
                model.setMediumH(heightSample.getMedium());
                model.setMaxH(heightSample.getMax());
            }
            if (!CollectionUtils.isEmpty(weightList)) {
                WeightSample weightSample = weightList.get(0);
                model.setMinW(weightSample.getMin());
                model.setMediumW(weightSample.getMedium());
                model.setMaxW(weightSample.getMax());
            }
            if (!CollectionUtils.isEmpty(heightList) || !CollectionUtils.isEmpty(weightList)) {
                responeList.add(model);
            }
        });
        return responeList;
    }

    private ExcelData setHeaderMulti() {
        ExcelData headerMulti = new ExcelData();
        headerMulti.setPro1("SỐ ĐO CHIỀU CAO - CÂN NẶNG");
        headerMulti.setPro2("");
        headerMulti.setPro3("");
        headerMulti.setPro4("");
        headerMulti.setPro5("");
        return headerMulti;
    }

    private ExcelData setHeaderMulti1() {
        ExcelData headerMulti = new ExcelData();
        headerMulti.setPro1("CÂN ĐO");
        headerMulti.setPro2("");
        headerMulti.setPro3("SỐ ĐO CHIỀU CAO - CÂN NẶNG");
        headerMulti.setPro4("");
        headerMulti.setPro5("");
        headerMulti.setPro6("");
        return headerMulti;
    }
}
