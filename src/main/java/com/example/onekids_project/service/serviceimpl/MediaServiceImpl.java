package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MediaRepository;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.schoolconfig.MediaCreateRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.request.schoolconfig.MediaUpdateRequest;
import com.example.onekids_project.response.schoolconfig.MediaConfigResponse;
import com.example.onekids_project.response.schoolconfig.MediaForClassResponse;
import com.example.onekids_project.response.schoolconfig.MediaSettingResponse;
import com.example.onekids_project.service.servicecustom.MediaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Override
    public List<MediaConfigResponse> findAllMedia(Long idSchool) {
        List<Media> mediaList = mediaRepository.findByIdSchoolAndDelActiveTrueOrderByMediaNameAsc(idSchool);
        return listMapper.mapList(mediaList, MediaConfigResponse.class);
    }

    @Override
    public boolean checkActiveMedia(Long id, boolean active) {
        Optional<Media> mediaOptional = mediaRepository.findByIdAndDelActiveTrue(id);
        if (mediaOptional.isEmpty()) {
            return false;
        }
        Media media = mediaOptional.get();
        media.setMediaActive(active);
        mediaRepository.save(media);
        return true;
    }

    @Override
    public MediaConfigResponse updateMedia(MediaUpdateRequest mediaUpdateRequest) {
        Optional<Media> mediaOptional = mediaRepository.findByIdAndDelActiveTrue(mediaUpdateRequest.getId());
        if (mediaOptional.isEmpty()) {
            return null;
        }
        Media media = mediaOptional.get();
        modelMapper.map(mediaUpdateRequest, media);
        Media mediaSaved = mediaRepository.save(media);
        MediaConfigResponse mediaConfigResponse = modelMapper.map(mediaSaved, MediaConfigResponse.class);
        return mediaConfigResponse;
    }

    @Override
    public boolean deleteMediaOne(Long id) {
        Optional<Media> mediaOptional = mediaRepository.findByIdAndDelActiveTrue(id);
        if (mediaOptional.isEmpty()) {
            return false;
        }
        Media media = mediaOptional.get();
        long countNumber = media.getMaClassList().stream().filter(x -> x.isDelActive()).count();
        if (countNumber > 0) {
            return false;
        }
        media.setDelActive(AppConstant.APP_FALSE);
        mediaRepository.save(media);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteMediaMany(List<MediaUpdateRequest> mediaUpdateRequestList) {
        if (CollectionUtils.isEmpty(mediaUpdateRequestList)) {
            return false;
        }
        for (MediaUpdateRequest mediaUpdateRequest : mediaUpdateRequestList) {
            Optional<Media> mediaOptional = mediaRepository.findByIdAndDelActiveTrue(mediaUpdateRequest.getId());
            if (mediaOptional.isEmpty()) {
                return false;
            }
            Media media = mediaOptional.get();
            media.setDelActive(AppConstant.APP_FALSE);
            mediaRepository.save(media);
        }
        return true;
    }

    @Override
    public MediaConfigResponse createMedia(Long idSchool, MediaCreateRequest mediaCreateRequest) {
        Media media = modelMapper.map(mediaCreateRequest, Media.class);
        media.setIdSchool(idSchool);
        Media mediaSaved = mediaRepository.save(media);
        MediaConfigResponse mediaConfigResponse = modelMapper.map(mediaSaved, MediaConfigResponse.class);
        return mediaConfigResponse;
    }

    @Override
    public List<MediaSettingResponse> findAllMediaSetting(Long idSchool, MediaSettingSearchRequest mediaSettingSearchRequest) {
        List<MaClass> maClassList = maClassRepository.searchMaclassForMediaSetting(idSchool, mediaSettingSearchRequest);
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        List<MediaSettingResponse> mediaSettingResponseList = listMapper.mapList(maClassList, MediaSettingResponse.class);
        return mediaSettingResponseList;
    }

    @Override
    public List<MediaForClassResponse> findMediaForClass(Long idSchool, Long idClass) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndDelActiveTrue(idClass);
        if (maClassOptional.isEmpty()) {
            return null;
        }
        List<Media> mediaList = mediaRepository.findByIdSchoolAndDelActiveTrueOrderByMediaNameAsc(idSchool);
        if (CollectionUtils.isEmpty(mediaList)) {
            return null;
        }
        List<Long> mediaOfClassList = mediaRepository.findByMaClassList_Id(idClass).stream().map(x -> x.getId()).collect(Collectors.toList());
        List<MediaForClassResponse> mediaForClassResponseList = listMapper.mapList(mediaList, MediaForClassResponse.class);
        mediaForClassResponseList.forEach(y -> {
            mediaOfClassList.forEach(z -> {
                if (y.getId().equals(z)) {
                    y.setUsed(AppConstant.APP_TRUE);
                }
            });
        });
        return mediaForClassResponseList;
    }

    @Transactional
    @Override
    public boolean updateMediaForClass(Long idClass, List<IdObjectRequest> idObjectRequestList) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndDelActiveTrue(idClass);
        if (maClassOptional.isEmpty()) {
            return false;
        }
        mediaRepository.deleteMediaForClass(idClass);
        idObjectRequestList.forEach(x -> {
            mediaRepository.insertMediaForClass(idClass, x.getId());
        });
        return true;
    }
}
