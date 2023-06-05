package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.common.UrlFileConstant;
import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.entity.school.NotifySchoolAttachFile;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.notifyschool.NotifySchoolActivePlusRequest;
import com.example.onekids_project.mobile.plus.request.notifyschool.NotifySchoolPlusRequest;
import com.example.onekids_project.mobile.plus.request.notifyschool.SearchNotifySchoolPlusRequest;
import com.example.onekids_project.mobile.plus.response.notifyschool.ListNotifySchoolPlusResponse;
import com.example.onekids_project.mobile.plus.response.notifyschool.NotifySchoolPlusResponse;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.service.servicecustom.NotifySchoolPlusService;
import com.example.onekids_project.repository.NotifySchoolAttachFileRepository;
import com.example.onekids_project.repository.NotifySchoolRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-10-22 11:13 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class NotifySchoolPlusServiceImpl implements NotifySchoolPlusService {

    @Autowired
    private NotifySchoolRepository notifySchoolRepository;
    @Autowired
    private NotifySchoolAttachFileRepository notifySchoolAttachFileRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListNotifySchoolPlusResponse searchNotifySchoolPlus(Long idSchool, SearchNotifySchoolPlusRequest request) {
        ListNotifySchoolPlusResponse response = new ListNotifySchoolPlusResponse();
        List<NotifySchool> notifySchoolList = notifySchoolRepository.searchNotifySchoolPlusMobile(idSchool, request);
        List<NotifySchoolPlusResponse> dataList = new ArrayList<>();
        notifySchoolList.forEach(x->{
            NotifySchoolPlusResponse model = modelMapper.map(x, NotifySchoolPlusResponse.class);
            if (CollectionUtils.isNotEmpty(x.getNotifySchoolAttachFileList())){
                List<AttachFileMobileResponse> fileList = listMapper.mapList(x.getNotifySchoolAttachFileList(), AttachFileMobileResponse.class);
                model.setFileList(fileList);
            }
            dataList.add(model);
        });
        boolean lastPage = notifySchoolList.size() < MobileConstant.MAX_PAGE_ITEM;
        response.setLastPage(lastPage);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public NotifySchoolPlusResponse findByNotifySchoolPlus(Long id) {
        NotifySchoolPlusResponse response = new NotifySchoolPlusResponse();
        NotifySchool notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if (CollectionUtils.isNotEmpty(notifySchool.getNotifySchoolAttachFileList())){
            List<AttachFileMobileResponse> fileList = listMapper.mapList(notifySchool.getNotifySchoolAttachFileList(), AttachFileMobileResponse.class);
            response.setFileList(fileList);
        }
        modelMapper.map(notifySchool, response);
        return response;
    }

    @Transactional
    @Override
    public boolean saveNotifySchoolPlus(UserPrincipal principal, NotifySchoolPlusRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        NotifySchool notifySchool = new NotifySchool();
        if(request.getId() != null){
            notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
            notifySchool.setLastModifieBy(principal.getFullName());
        }else {
            notifySchool.setCreatedBy(principal.getFullName());
        }
        modelMapper.map(request, notifySchool);
        notifySchool.setIdSchool(idSchool);
        NotifySchool saveNotifySchool = notifySchoolRepository.save(notifySchool);
        if (CollectionUtils.isNotEmpty(request.getFileDeleteList())){
            this.deleteFile(request.getFileDeleteList());
        }
        if (CollectionUtils.isNotEmpty(request.getMultipartFileList())) {
            this.addFile(idSchool, saveNotifySchool, request.getMultipartFileList());
        }
        return true;
    }

    @Override
    public boolean deleteNotifySchoolPlus(Long id) {
        NotifySchool notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        notifySchool.setDelActive(AppConstant.APP_FALSE);
        notifySchool.getNotifySchoolAttachFileList().forEach(x->{
            x.setDelActive(AppConstant.APP_FALSE);
            notifySchoolAttachFileRepository.save(x);
        });
        notifySchoolRepository.save(notifySchool);
        return true;
    }

    @Override
    public boolean activeNotifySchoolPlus(NotifySchoolActivePlusRequest request) {
        NotifySchool notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        notifySchool.setActive(request.isActive());
        notifySchoolRepository.save(notifySchool);
        return true;
    }

    /**
     * Thêm ds ảnh
     */
    private void addFile(Long idSchool, NotifySchool notifySchool, List<MultipartFile> multipartFileList) {
        if (CollectionUtils.isNotEmpty(multipartFileList)) {
            multipartFileList.forEach(multipartFile -> {
                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THONG_BAO);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                try {
                    HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                NotifySchoolAttachFile notifySchoolAttachFile = new NotifySchoolAttachFile();
                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.THONG_BAO) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THONG_BAO) + fileName;
                notifySchoolAttachFile.setUrl(urlWeb);
                String nameFileSave = multipartFile.getOriginalFilename();
                notifySchoolAttachFile.setName(nameFileSave);
                notifySchoolAttachFile.setUrlLocal(urlLocal);
                notifySchoolAttachFile.setNotifySchool(notifySchool);
                notifySchoolAttachFileRepository.save(notifySchoolAttachFile);
            });
        }
    }
    private void deleteFile(List<Long> fileDeleteList) {
        fileDeleteList.forEach(x -> {
            NotifySchoolAttachFile notifySchoolAttachFile = notifySchoolAttachFileRepository.findById(x).orElseThrow();
            HandleFileUtils.deleteFileOrPictureInFolder(notifySchoolAttachFile.getUrlLocal());
            notifySchoolAttachFileRepository.deleteById(x);
        });
    }
}
