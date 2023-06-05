package com.example.onekids_project.service.serviceimpl.notifyschool;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.common.UrlFileConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.entity.school.NotifySchoolAttachFile;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.NotifySchoolAttachFileRepository;
import com.example.onekids_project.repository.NotifySchoolRepository;
import com.example.onekids_project.request.notifyschool.NotifySchoolActiveRequest;
import com.example.onekids_project.request.notifyschool.NotifySchoolRequest;
import com.example.onekids_project.request.notifyschool.SearchNotifySchoolRequest;
import com.example.onekids_project.response.notifyschool.ListNotifySchoolResponse;
import com.example.onekids_project.response.notifyschool.NotifySchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.notifyschool.NotifySchoolService;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.util.UserInforUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-10-21 9:13 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class NotifySchoolServiceImpl implements NotifySchoolService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private NotifySchoolRepository notifySchoolRepository;
    @Autowired
    private NotifySchoolAttachFileRepository notifySchoolAttachFileRepository;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListNotifySchoolResponse searchNotifySchool(Long idSchool, SearchNotifySchoolRequest request) {
        ListNotifySchoolResponse response = new ListNotifySchoolResponse();
        List<NotifySchool> notifySchoolList = notifySchoolRepository.searchNotifySchool(idSchool, request);
        List<NotifySchoolResponse> notifySchoolResponseList = listMapper.mapList(notifySchoolList, NotifySchoolResponse.class);
        long total = notifySchoolRepository.countNotifySchool(idSchool, request);
        response.setDataList(notifySchoolResponseList);
        response.setTotal(total);
        return response;
    }

    @Override
    public NotifySchoolResponse findByNotifySchool(Long id) {
        NotifySchoolResponse response = new NotifySchoolResponse();
        NotifySchool notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        modelMapper.map(notifySchool, response);
        return response;
    }

    @Transactional
    @Override
    public boolean saveNotifySchool(UserPrincipal principal, NotifySchoolRequest request) throws FirebaseMessagingException {
        Long idSchool = principal.getIdSchoolLogin();
        NotifySchool notifySchool = new NotifySchool();
        if (request.getId() != null) {
            notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
            notifySchool.setLastModifieBy(principal.getFullName());
        } else {
            notifySchool.setCreatedBy(principal.getFullName());
        }
        modelMapper.map(request, notifySchool);
        notifySchool.setIdSchool(idSchool);
        NotifySchool saveNotifySchool = notifySchoolRepository.save(notifySchool);
        if (!CollectionUtils.isEmpty(request.getFileDeleteList())) {
            this.deleteFile(request.getFileDeleteList());
        }
        if (!CollectionUtils.isEmpty(request.getMultipartFileList())) {
            this.addFile(idSchool, saveNotifySchool, request.getMultipartFileList());
        }
        //create ->send firebase
        if (request.getId() == null) {
            List<Kids> kidsList = UserInforUtils.getKidsInSchoolHasAccount();
            List<InfoEmployeeSchool> teacherList = UserInforUtils.getTeacherInSchoolHasAccount();
            List<InfoEmployeeSchool> plusList = UserInforUtils.getPlusInSchoolHasAccount();
            firebaseFunctionService.sendParentCommon(kidsList, request.getTitle(), request.getContent(), idSchool, FirebaseConstant.CATEGORY_NEWS);
            firebaseFunctionService.sendTeacherCommon(teacherList, request.getTitle(), request.getContent(), idSchool, FirebaseConstant.CATEGORY_NEWS);
            firebaseFunctionService.sendPlusCommon(plusList, request.getTitle(), request.getContent(), idSchool, FirebaseConstant.CATEGORY_NEWS);
        }
        return true;
    }

    @Override
    public boolean deleteNotifySchool(Long id) {
        NotifySchool notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        notifySchool.setDelActive(AppConstant.APP_FALSE);
        notifySchool.getNotifySchoolAttachFileList().forEach(x -> {
            x.setDelActive(AppConstant.APP_FALSE);
            notifySchoolAttachFileRepository.save(x);
        });
        notifySchoolRepository.save(notifySchool);
        return true;
    }

    @Override
    public boolean activeNotifySchool(NotifySchoolActiveRequest request) {
        NotifySchool notifySchool = notifySchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        notifySchool.setActive(request.isActive());
        notifySchoolRepository.save(notifySchool);
        return true;
    }

    /**
     * Thêm ds ảnh
     */
    private void addFile(Long idSchool, NotifySchool notifySchool, List<MultipartFile> multipartFileList) {
        if (!CollectionUtils.isEmpty(multipartFileList)) {
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
