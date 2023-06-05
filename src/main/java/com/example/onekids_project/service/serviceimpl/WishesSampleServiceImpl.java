package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.sample.WishesSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.WishesSampleRepository;
import com.example.onekids_project.request.system.WishesSampleCreateRequest;
import com.example.onekids_project.request.system.WishesSampleUpdateRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.wishes.WishesSampleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WishesSampleService;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;

@Service
public class WishesSampleServiceImpl implements WishesSampleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private WishesSampleRepository wishesSampleRepository;

    @Override
    public List<WishesSampleResponse> findAllSystemConfig() {
        List<WishesSample> wishesSampleList = wishesSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(SystemConstant.ID_SYSTEM);
        List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList, WishesSampleResponse.class);
        return wishesSampleResponseList;
    }

    @Override
    public List<WishesSampleResponse> findAllSystemConfigSchool(Long idSchool, UserPrincipal principal) {
        List<WishesSample> wishesSampleList;
        if (principal.getSchoolConfig().isShowWishSys()) {
            wishesSampleList = wishesSampleRepository.findAllWishSample(idSchool, SystemConstant.ID_SYSTEM);
        } else {
            wishesSampleList = wishesSampleRepository.findAllWishSample(idSchool, null);
        }
        if (CollectionUtils.isEmpty(wishesSampleList)) {
            return null;
        }
        List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList, WishesSampleResponse.class);
        return wishesSampleResponseList;
    }

    @Override
    public WishesSampleResponse createWishesSample(Long idSchool, WishesSampleCreateRequest wishesSampleCreateRequest) throws IOException {
        WishesSample wishesSample = modelMapper.map(wishesSampleCreateRequest, WishesSample.class);
        if (wishesSampleCreateRequest.getMultipartFile() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(wishesSampleCreateRequest.getMultipartFile(), idSchool, UploadDownloadConstant.SAMPLE);
            wishesSample.setUrlPicture(handleFileResponse.getUrlWeb());
            wishesSample.setUrlPictureLocal(handleFileResponse.getUrlLocal());
        }
        wishesSample.setIdSchool(idSchool);
        WishesSample birthdaySampleSaved = wishesSampleRepository.save(wishesSample);
        WishesSampleResponse wishesSampleResponse = modelMapper.map(birthdaySampleSaved, WishesSampleResponse.class);
        return wishesSampleResponse;
    }

    @Override
    public WishesSampleResponse updateWishesSample(Long idSchool, WishesSampleUpdateRequest wishesSampleUpdateRequest) throws IOException {
        WishesSample wishesSample = wishesSampleRepository.findByIdAndDelActiveTrue(wishesSampleUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found wishesample by id"));
        if (wishesSampleUpdateRequest.getMultipartFile() != null) {
            String urlLocalOld = wishesSample.getUrlPictureLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);

            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(wishesSampleUpdateRequest.getMultipartFile(), idSchool, UploadDownloadConstant.SAMPLE);
            wishesSample.setUrlPicture(handleFileResponse.getUrlWeb());
            wishesSample.setUrlPictureLocal(handleFileResponse.getUrlLocal());
        }
        modelMapper.map(wishesSampleUpdateRequest, wishesSample);
        WishesSample birthdaySampleSaved = wishesSampleRepository.save(wishesSample);
        WishesSampleResponse wishesSampleResponse = modelMapper.map(birthdaySampleSaved, WishesSampleResponse.class);
        return wishesSampleResponse;
    }

    @Override
    public boolean deleteWishesSample(Long id) {
        WishesSample wishesSample = wishesSampleRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found wishesample by id"));
        if (StringUtils.isNotBlank(wishesSample.getUrlPictureLocal())) {
            String urlLocalOld = wishesSample.getUrlPictureLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);
        }
        wishesSampleRepository.deleteById(id);
        return true;
    }


}
