package com.example.onekids_project.util;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.mobile.response.LinkResponse;
import com.example.onekids_project.repository.MediaRepository;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * class chung cho nhiều thứ
 */
@Component
public class CommonUtil {
    private static SysInforRepository sysInforRepository;

    private static MediaRepository mediaRepository;

    @Autowired
    public CommonUtil(SysInforRepository sysInforRepository, MediaRepository mediaRepository) {
        CommonUtil.sysInforRepository = sysInforRepository;
        CommonUtil.mediaRepository = mediaRepository;
    }

    /**
     * lấy tên từ fullName
     *
     * @param fullName
     * @return
     */
    public static String convertFistName(String fullName) {
        String fullNameNormal = StringUtils.normalizeSpace(fullName);
        int index = fullNameNormal.lastIndexOf(' ');
        String firstName = fullNameNormal.substring(index + 1);
        return firstName;
    }

    /**
     * lấy họ và tên đệm từ fullName
     *
     * @param fullName
     * @return
     */
    public static String convertLastName(String fullName) {
        String fullNameNormal = StringUtils.normalizeSpace(fullName);
        int index = fullNameNormal.lastIndexOf(' ');
        if (index < 0) {
            return "";
        }
        String lastName = fullNameNormal.substring(0, index);
        return lastName;
    }

    /**
     * lấy fullName
     *
     * @param fullName
     * @return
     */
    public static String getFullName(String fullName) {
        return StringUtils.normalizeSpace(fullName);
    }

    /**
     * create forder for school
     *
     * @param idSchool
     */
    public static void createFolderSchool(Long idSchool) {
        int monthCurrent = 1;
        int yearCurrent = LocalDate.now().getYear();
        StringBuilder urlLocal;
        for (int i = monthCurrent; i <= 12; i++) {
            urlLocal = new StringBuilder();
            urlLocal.append(AppConstant.UPLOAD_IN_ALBUM).append(idSchool.toString()).append("\\").append(yearCurrent).append("\\T").append(i).append("\\");
            createFolderCommon(urlLocal.toString());
        }
        createFolderSchoolExtend(idSchool);
    }

    /**
     * create folder for system
     */
    public static void createFolderSystem() {
        Long idSchool = SystemConstant.ID_SYSTEM;
        StringBuilder urlLocal = new StringBuilder();
        urlLocal.append(AppConstant.UPLOAD_IN_ALBUM).append(idSchool.toString()).append("\\");
        createFolderCommon(urlLocal.toString());
        createFolderSystemExtend(urlLocal.toString());
        createFolderSystemFile();
    }

    private static void createFolderSchoolExtend(Long idSchool) {
        String schoolInSchool = AppConstant.UPLOAD_IN_ALBUM.concat(idSchool.toString());
        String folderJson = schoolInSchool.concat("\\" + UploadDownloadConstant.DIEM_DANH_JSON);
        String saveFile = schoolInSchool.concat("\\" + UploadDownloadConstant.SAVE_FILE);
        String otherFile = schoolInSchool.concat("\\" + UploadDownloadConstant.OTHER);
        String sampleFile = schoolInSchool.concat("\\" + UploadDownloadConstant.SAMPLE);
        File urlLocalJson = new File(folderJson);
        if (!urlLocalJson.exists()) {
            urlLocalJson.mkdirs();
        }
        File urlLocalsaveFile = new File(saveFile);
        if (!urlLocalsaveFile.exists()) {
            urlLocalsaveFile.mkdirs();
        }
        File urlOtherFile = new File(otherFile);
        if (!urlOtherFile.exists()) {
            urlOtherFile.mkdirs();
        }
        File urlSampleFile = new File(sampleFile);
        if (!urlSampleFile.exists()) {
            urlSampleFile.mkdirs();
        }
    }

    private static void createFolderSystemExtend(String urlLocal) {
        String ulrOther = urlLocal.concat(UploadDownloadConstant.OTHER);
        String urlSample = urlLocal.concat(UploadDownloadConstant.SAMPLE);
        File urlOtherFile = new File(ulrOther);
        if (!urlOtherFile.exists()) {
            urlOtherFile.mkdirs();
        }
        File urlSampleFile = new File(urlSample);
        if (!urlSampleFile.exists()) {
            urlSampleFile.mkdirs();
        }
    }

    private static void createFolderSystemFile() {
        String urlLocalSystemFiles = AppConstant.UPLOAD_IN_ALBUM + "\\" + UploadDownloadConstant.SYSTEM_FILES;
        String fileTempData = AppConstant.UPLOAD_IN_ALBUM + "\\" + UploadDownloadConstant.TEMP_DATA;
        File urlLocalPathSysFiles = new File(urlLocalSystemFiles);
        File localFileTempData = new File(fileTempData);
        if (!urlLocalPathSysFiles.exists()) {
            urlLocalPathSysFiles.mkdirs();
        }
        if (!localFileTempData.exists()) {
            localFileTempData.mkdirs();
        }
        //tạo folder trong systemfile
        File avatar = new File(urlLocalSystemFiles + "\\" + UploadDownloadConstant.AVATAR);
        File other = new File(urlLocalSystemFiles + "\\" + UploadDownloadConstant.OTHER);
        if (!avatar.exists()) {
            avatar.mkdirs();
        }
        if (!other.exists()) {
            other.mkdirs();
        }
    }

    /**
     * tạo các mục
     *
     * @param urlLocal
     */
    private static void createFolderCommon(String urlLocal) {
        List<String> folderNameList = Arrays.asList(UploadDownloadConstant.LOI_NHAN, UploadDownloadConstant.DON_THUOC, UploadDownloadConstant.XIN_NGHI,
                UploadDownloadConstant.DIEM_DANH, UploadDownloadConstant.ALBUM, UploadDownloadConstant.NHAN_XET,
                UploadDownloadConstant.HOC_TAP, UploadDownloadConstant.THUC_DON, UploadDownloadConstant.GOP_Y,
                UploadDownloadConstant.THONG_BAO, UploadDownloadConstant.AVATAR, UploadDownloadConstant.TAI_CHINH, UploadDownloadConstant.KHAC);
        folderNameList.forEach(x -> {
            String folder = urlLocal.concat(x);
            File urlLocalPath = new File(folder);
            if (!urlLocalPath.exists()) {
                urlLocalPath.mkdirs();
            }
        });
    }

    public static LinkResponse getLink(Long idSchool) {
        LinkResponse model = new LinkResponse();
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        Optional<Media> mediaOptional = mediaRepository.findFirstByIdSchoolAndScopeTypeAndMediaTypeAndMediaActiveTrueAndDelActiveTrue(idSchool, AppConstant.TYPE_SCHOOL, AppConstant.TYPE_FACEBOOK);
        if (sysInfor != null) {
            model.setGuideLink(sysInfor.getGuidePlusLink());
            model.setPolicyLink(sysInfor.getPolicyLink());
            model.setSupportLink(sysInfor.getSupportLink());
        }
        model.setFacebookLink(mediaOptional.isPresent() ? mediaOptional.get().getLinkMedia() : "");
        return model;
    }

    public static void checkDeleteObject(UserPrincipal principal) {
        if (!principal.getSchoolConfig().isDeleteStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.DELETE_OBJECT);
        }
    }

}

