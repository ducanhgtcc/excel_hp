package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.school.BirthdaySample;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.BirthdaySampleRepository;
import com.example.onekids_project.request.schoolconfig.BirthdaySampleActiveRequest;
import com.example.onekids_project.request.system.BirthdaySampleUpdateRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.schoolconfig.BirthdaySampleResponse;
import com.example.onekids_project.service.servicecustom.BirthdaySampleService;
import com.example.onekids_project.util.HandleFileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BirthdaySampleServiceImpl implements BirthdaySampleService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BirthdaySampleRepository birthdaySampleRepository;

    @Override
    public void createBirthSampleForSystem() {
        List<BirthdaySample> birthdaySampleList = birthdaySampleRepository.findByIdSchoolAndDelActiveTrue(SystemConstant.ID_SYSTEM);
        if (CollectionUtils.isEmpty(birthdaySampleList)) {
            for (int i = 1; i <= 4; i++) {
                BirthdaySample birthdaySample = new BirthdaySample();
                birthdaySample.setIdCreated(SystemConstant.ID_SYSTEM);
                birthdaySample.setCreatedDate(LocalDateTime.now());
                birthdaySample.setIdSchool(SystemConstant.ID_SYSTEM);
                if (i == 1) {
                    //todo thiếu dấu nặng
                    birthdaySample.setContent("Chúc mừng sinh nhật con {ten}. Chúc con có một ngày tuyệt vời với nhiều niềm vui cùng gia đình và bạn bè. Happy Birthday!");
                    birthdaySample.setBirthdayType(SampleConstant.KIDS);
                } else if (i == 2) {
                    birthdaySample.setContent("Chúc mừng sinh nhật Quý Phụ Huynh {ten}. Chúc Quý phụ huynh có một sinh nhật vui vẻ và hạnh phúc bên gia đình. Trân trọng!");
                    birthdaySample.setBirthdayType(SampleConstant.PARENT);
                } else if (i == 3) {
                    //todo thiếu dấu nặng
                    birthdaySample.setContent("Chúc mừng sinh nhật Thầy/Cô {ten}. Chúc Thầy/Cô có một sinh nhật vui vẻ và hạnh phúc bên gia đình. Trân trọng!");
                    birthdaySample.setBirthdayType(SampleConstant.TEACHER);
                } else if (i == 4) {
                    //todo thiếu dấu nặng
                    birthdaySample.setContent("Chúc mừng sinh nhật Thầy/Cô {ten}. Chúc Thầy/Cô có một sinh nhật vui vẻ và hạnh phúc bên gia đình. Trân trọng!");
                    birthdaySample.setBirthdayType(SampleConstant.PLUS);
                }
                birthdaySampleRepository.save(birthdaySample);
            }
        }
    }

    @Override
    public void createBirthSampleForSchool(School school) {
        Long idSchool = school.getId();
        List<BirthdaySample> birthdaySampleList = birthdaySampleRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        if (CollectionUtils.isEmpty(birthdaySampleList)) {
            for (int i = 1; i <= 3; i++) {
                BirthdaySample birthdaySample = new BirthdaySample();
                birthdaySample.setIdCreated(SystemConstant.ID_SYSTEM);
                birthdaySample.setCreatedDate(LocalDateTime.now());
                birthdaySample.setIdSchool(idSchool);
                if (i == 1) {
                    //todo thiếu dấu nặng
                    birthdaySample.setContent(school.getSchoolName() + ": Chúc mừng sinh nhật con {ten}. Chúc con có một ngày tuyệt vời với nhiều niềm vui cùng gia đình và bạn bè. Happy Birthday!");
                    birthdaySample.setBirthdayType(SampleConstant.KIDS);
                } else if (i == 2) {
                    birthdaySample.setContent(school.getSchoolName() + ": Chúc mừng sinh nhật Quý Phụ Huynh {ten}. Chúc Quý phụ huynh có một sinh nhật vui vẻ và hạnh phúc bên gia đình. Trân trọng!");
                    birthdaySample.setBirthdayType(SampleConstant.PARENT);
                } else if (i == 3) {
                    //todo thiếu dấu nặng
                    birthdaySample.setContent(school.getSchoolName() + ": Chúc mừng sinh nhật Thầy/Cô {ten}. Chúc Thầy/Cô có một sinh nhật vui vẻ và hạnh phúc bên gia đình. Trân trọng!");
                    birthdaySample.setBirthdayType(SampleConstant.TEACHER);
                }
                birthdaySampleRepository.save(birthdaySample);
            }
        }
    }

    @Override
    public List<BirthdaySampleResponse> findAllBirthdaySample(Long idSchool) {
        List<BirthdaySample> birthdaySampleList = birthdaySampleRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        if (CollectionUtils.isEmpty(birthdaySampleList)) {
            return null;
        }
        List<BirthdaySampleResponse> birthdaySampleResponseList = listMapper.mapList(birthdaySampleList, BirthdaySampleResponse.class);
        return birthdaySampleResponseList;
    }

    @Override
    public BirthdaySampleResponse updateBirthdaySampleActive(BirthdaySampleActiveRequest birthdaySampleActiveRequest) {
        Optional<BirthdaySample> birthdaySampleOptional = birthdaySampleRepository.findByIdAndDelActiveTrue(birthdaySampleActiveRequest.getId());
        if (birthdaySampleOptional.isEmpty()) {
            return null;
        }
        BirthdaySample birthdaySample = birthdaySampleOptional.get();
        modelMapper.map(birthdaySampleActiveRequest, birthdaySample);
        BirthdaySample birthdaySampleSaved = birthdaySampleRepository.save(birthdaySample);
        BirthdaySampleResponse birthdaySampleResponse = modelMapper.map(birthdaySampleSaved, BirthdaySampleResponse.class);
        return birthdaySampleResponse;
    }

//    @Override
//    public BirthdaySampleResponse updateBirthdaySample(BirthdaySampleUpdateRequest birthdaySampleUpdateRequest) throws IOException {
//        BirthdaySample birthdaySample = birthdaySampleRepository.findByIdAndDelActiveTrue(birthdaySampleUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found birthdaysample by id"));
//        if (birthdaySampleUpdateRequest.getMultipartFile() != null) {
//            String urlLocalOld = birthdaySample.getUrlPictureLocal();
//            HandleFileUtils.deletePictureInFolder(urlLocalOld)
//
//            String urlFolder = HandleFileUtils.getUrl(SystemConstant.ID_SYSTEM, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.KHAC);
//            String fileName = HandleFileUtils.getFileNameOfSchool(SystemConstant.ID_SYSTEM, birthdaySampleUpdateRequest.getMultipartFile());
//            HandleFileUtils.createFilePictureToDirectory(urlFolder, birthdaySampleUpdateRequest.getMultipartFile(), fileName, UploadDownloadConstant.WIDTH_OTHER);
//
//            String urlWeb = HandleFileUtils.getUrl(SystemConstant.ID_SYSTEM, UrlFileConstant.URL_WEB, UploadDownloadConstant.KHAC) + fileName;
//            String urlLocal = HandleFileUtils.getUrl(SystemConstant.ID_SYSTEM, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.KHAC) + fileName;
//            birthdaySample.setUrlPicture(urlWeb);
//            birthdaySample.setUrlPictureLocal(urlLocal);
//        }
//        modelMapper.map(birthdaySampleUpdateRequest, birthdaySample);
//        BirthdaySample birthdaySampleSaved = birthdaySampleRepository.save(birthdaySample);
//        BirthdaySampleResponse birthdaySampleResponse = modelMapper.map(birthdaySampleSaved, BirthdaySampleResponse.class);
//        return birthdaySampleResponse;
//    }

    @Override
    public BirthdaySampleResponse updateBirthdaySample(Long idSchool, BirthdaySampleUpdateRequest birthdaySampleUpdateRequest) throws IOException {
        BirthdaySample birthdaySample = birthdaySampleRepository.findByIdAndDelActiveTrue(birthdaySampleUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found birthdaysample by id"));
        if (birthdaySampleUpdateRequest.getMultipartFile() != null) {
            HandleFileUtils.deletePictureInFolder(birthdaySample.getUrlPictureLocal());

            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(birthdaySampleUpdateRequest.getMultipartFile(), idSchool, UploadDownloadConstant.SAMPLE);
            birthdaySample.setUrlPicture(handleFileResponse.getUrlWeb());
            birthdaySample.setUrlPictureLocal(handleFileResponse.getUrlLocal());
        }
        modelMapper.map(birthdaySampleUpdateRequest, birthdaySample);
        BirthdaySample birthdaySampleSaved = birthdaySampleRepository.save(birthdaySample);
        BirthdaySampleResponse birthdaySampleResponse = modelMapper.map(birthdaySampleSaved, BirthdaySampleResponse.class);
        return birthdaySampleResponse;
    }

}
