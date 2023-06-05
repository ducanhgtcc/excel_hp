package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.school.CelebrateSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.CelebrateSampleRepository;
import com.example.onekids_project.request.celebrate.CelebrateSampleActiveRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleCreateRequest;
import com.example.onekids_project.request.celebrate.CelebrateSampleUpdateRequest;
import com.example.onekids_project.response.celebrate.CelebrateSampleResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.service.servicecustom.CelebrateSampleService;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;

@Service
public class CelebrateSampleServiceImpl implements CelebrateSampleService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private CelebrateSampleRepository celebrateSampleRepository;

    @Override
    public List<CelebrateSampleResponse> findAllCelebrateSystem(Long idSchool) {
        List<CelebrateSample> celebrateSampleList = celebrateSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(idSchool);
        List<CelebrateSampleResponse> celebrateSampleResponseList = listMapper.mapList(celebrateSampleList, CelebrateSampleResponse.class);
        return celebrateSampleResponseList;
    }

    @Override
    public CelebrateSampleResponse updateCelebrateSampleActive(CelebrateSampleActiveRequest celebrateSampleActiveRequest) {
        CelebrateSample celebrateSample = celebrateSampleRepository.findByIdAndDelActiveTrue(celebrateSampleActiveRequest.getId()).orElseThrow(() -> new NotFoundException("not found celebrate sample by id"));
        modelMapper.map(celebrateSampleActiveRequest, celebrateSample);
        CelebrateSample celebrateSampleSaved = celebrateSampleRepository.save(celebrateSample);
        CelebrateSampleResponse celebrateSampleResponse = modelMapper.map(celebrateSampleSaved, CelebrateSampleResponse.class);
        return celebrateSampleResponse;
    }

    @Override
    public CelebrateSampleResponse createCelebrateSample(Long idSchool, CelebrateSampleCreateRequest celebrateSampleCreateRequest) throws IOException {
        CelebrateSample celebrateSample = modelMapper.map(celebrateSampleCreateRequest, CelebrateSample.class);
        if (celebrateSampleCreateRequest.getMultipartFile() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(celebrateSampleCreateRequest.getMultipartFile(), idSchool, UploadDownloadConstant.SAMPLE);
            celebrateSample.setUrlPicture(handleFileResponse.getUrlWeb());
            celebrateSample.setUrlPictureLocal(handleFileResponse.getUrlLocal());
        }
        celebrateSample.setIdSchool(idSchool);
        CelebrateSample celebrateSampleSaved = celebrateSampleRepository.save(celebrateSample);
        CelebrateSampleResponse celebrateSampleResponse = modelMapper.map(celebrateSampleSaved, CelebrateSampleResponse.class);
        return celebrateSampleResponse;
    }

    @Override
    public CelebrateSampleResponse updateCelebrateSample(Long idSchool, CelebrateSampleUpdateRequest celebrateSampleUpdateRequest) throws IOException {
        CelebrateSample celebrateSample = celebrateSampleRepository.findByIdAndDelActiveTrue(celebrateSampleUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found wishesample by id"));
        if (celebrateSampleUpdateRequest.getMultipartFile() != null) {
            String urlLocalOld = celebrateSample.getUrlPictureLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);

            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(celebrateSampleUpdateRequest.getMultipartFile(), idSchool, UploadDownloadConstant.SAMPLE);
            celebrateSample.setUrlPicture(handleFileResponse.getUrlWeb());
            celebrateSample.setUrlPictureLocal(handleFileResponse.getUrlLocal());
        }
        modelMapper.map(celebrateSampleUpdateRequest, celebrateSample);
        CelebrateSample celebrateSampleSaved = celebrateSampleRepository.save(celebrateSample);
        CelebrateSampleResponse celebrateSampleResponse = modelMapper.map(celebrateSampleSaved, CelebrateSampleResponse.class);
        return celebrateSampleResponse;
    }

    @Override
    public boolean deleteCelebrateSample(Long id) {
        CelebrateSample celebrateSample = celebrateSampleRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found wishesample by id"));
        if (StringUtils.isNotBlank(celebrateSample.getUrlPictureLocal())) {
            String urlLocalOld = celebrateSample.getUrlPictureLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);
        }
        celebrateSampleRepository.deleteById(id);
        return true;
    }
}
