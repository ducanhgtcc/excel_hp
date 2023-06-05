package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.MaKidPics;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.response.KidsReco.KidsRecoRespone;
import com.example.onekids_project.mobile.plus.response.KidsReco.ListKidsRecoResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsRecoPlusService;
import com.example.onekids_project.mobile.teacher.request.identifykid.IdentityKidRequest;
import com.example.onekids_project.mobile.teacher.request.identifykid.IdentityModel;
import com.example.onekids_project.mobile.teacher.response.identitykid.IdentifyKid;
import com.example.onekids_project.mobile.teacher.response.identitykid.InfoIdentityKid;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.MaKidPicsRepository;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.example.onekids_project.validate.RequestValidate;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KidsRecoPlusServiceImpl implements KidsRecoPlusService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private MaKidPicsRepository maKidPicsRepository;

    @Override
    public ListKidsRecoResponse searchClass(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<MaClass> maClassList = maClassRepository.findAllMaClassAlbum(idSchool);
        ListKidsRecoResponse listMessagePlusResponse = new ListKidsRecoResponse();
        List<KidsRecoRespone> recoResponeList = new ArrayList<>();
        maClassList.forEach(x -> {
            KidsRecoRespone model = new KidsRecoRespone();
            model.setIdClass(x.getId());
            model.setClassName(x.getClassName());
            //số học sinh ở trạng thái đang học
            List<Kids> studyKidList = x.getKidsList().stream().filter(a -> a.isDelActive() && a.getKidStatus().equals(KidsStatusConstant.STUDYING)).collect(Collectors.toList());
            model.setNumberStudent(studyKidList.size());
            List<ExEmployeeClass> exEmployeeClassList = x.getExEmployeeClassList();
            //số giáo viên chủ nhiệm
            List<ExEmployeeClass> masterList = exEmployeeClassList.stream().filter(ExEmployeeClass::isMaster).collect(Collectors.toList());
            List<InfoEmployeeSchool> infoEmployeeSchoolList = this.getEmployeeNumberFromExEmployeeClass(masterList);
            List<String> masterName = infoEmployeeSchoolList.stream().map(InfoEmployeeSchool::getFullName).collect(Collectors.toList());
            model.setMasterNameList(masterName);
            recoResponeList.add(model);
        });
        listMessagePlusResponse.setRecoResponeList(recoResponeList);
        return listMessagePlusResponse;
    }

    @Override
    public List<IdentifyKid> getKidsClass(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        List<IdentifyKid> listKids = new ArrayList<>();
        List<Kids> idKidList = kidsRepository.findKidsOfClass(id);
        idKidList.forEach(x -> {
            IdentifyKid model = new IdentifyKid();
            model.setAvatarKid(ConvertData.getAvatarKid(x));
            model.setName(x.getFullName());
            model.setIdKids(x.getId());
            model.setBirthDay(ConvertData.getBithDay(x.getBirthDay()));
            model.setStatus(StringUtils.isNotBlank(x.getPicJsonUrl()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            listKids.add(model);
        });
        return listKids;
    }

    @Override
    public List<InfoIdentityKid> getKidIdentity(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        List<InfoIdentityKid> infoIdentityKidList = new ArrayList<>();
        List<MaKidPics> maKidPicsList;
        Optional<Kids> kidsOptional = kidsRepository.findById(id);
        Kids kid = kidsOptional.get();
        maKidPicsList = kid.getMaKidPicsList();
        if (!CollectionUtils.isEmpty(maKidPicsList)) {
            maKidPicsList.forEach(l -> {
                InfoIdentityKid infoIdentityKid = new InfoIdentityKid();
                infoIdentityKid.setIdImage(l.getId());
                infoIdentityKid.setLinkImage(l.getUrlWeb());
                infoIdentityKidList.add(infoIdentityKid);
            });
        }
        return infoIdentityKidList;
    }

    @Transactional
    @Override
    public boolean updateDelInsKidIdentity(UserPrincipal principal, IdentityKidRequest identityKidRequest) throws IOException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = identityKidRequest.getIdKid();
        MultipartFile afterDelete = null;
        if (!CollectionUtils.isEmpty(identityKidRequest.getMultipartFileImageList())) {
            RequestValidate.checkMaxfileInput(identityKidRequest.getMultipartFileImageList(), 6);
        }
        Kids kid = kidsRepository.findById(idKid).orElseThrow();
        // Xóa ảnh, có key file Json xóa trong file Json cũ, không có file Json mới, không có ảnh mới
        if ((identityKidRequest.getNewFileJson() == null) && (StringUtils.isNotBlank(kid.getPicJsonUrlLocal())) &&
                !CollectionUtils.isEmpty(identityKidRequest.getDelIdentityModelList()) && !CollectionUtils.isEmpty(kid.getMaKidPicsList())) {
            List<IdentityModel> identityModelList = identityKidRequest.getDelIdentityModelList();
            List<String> listKey = new ArrayList<>();
            for (IdentityModel x : identityModelList) {
                MaKidPics maKidPics = maKidPicsRepository.findById(x.getDelImage()).orElseThrow();
                if (!maKidPics.isMainStatus()) {
                    deleteImage(maKidPics.getUrlLocal(), x);    // Delete file image
                    listKey.add(x.getDelKeyJson());
                }
            }
            String strUrl = kid.getPicJsonUrlLocal();    // Lấy link Local của file Json
            MultipartFile fileDelete = deleteFileJson(strUrl, listKey);    // đối tượng lưu file Json sau khi đã xóa key Json dùng sau khi thêm ảnh và thêm key Json mới
            HandleFileResponse handleFileJson = HandleFileUtils.getUrlFileJsonSavedJson(fileDelete, idSchool, "diemdanhjson");
            kid.setPicJsonUrlLocal(handleFileJson.getUrlLocal());
            kid.setPicJsonUrl(handleFileJson.getUrlWeb());
            Kids kidNew = kidsRepository.save(kid);
            return true;
        }
        // Thêm mới File Json và ảnh, chưa có file Json chưa có ảnh
        if (!identityKidRequest.getNewFileJson().isEmpty() && StringUtils.isBlank(kid.getPicJsonUrl())) {
            HandleFileResponse handleFilejson = HandleFileUtils.getUrlFileJsonSavedJson(identityKidRequest.getNewFileJson(), idSchool, "diemdanhjson");
            kid.setPicJsonUrl(handleFilejson.getUrlWeb());
            kid.setPicJsonUrlLocal(handleFilejson.getUrlLocal());
            kidsRepository.save(kid);
            List<MultipartFile> multipartFileList = identityKidRequest.getMultipartFileImageList();
            multipartFileList.forEach(x -> {
                MaKidPics maKidPics = new MaKidPics();
                HandleFileResponse handleFileResponse = null;
                try {
                    handleFileResponse = HandleFileUtils.getUrlPictureSaved(x, idSchool, UploadDownloadConstant.ALBUM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert handleFileResponse != null;
                maKidPics.setUrlWeb(handleFileResponse.getUrlWeb());
                maKidPics.setUrlLocal(handleFileResponse.getUrlLocal());
                String nameFile = handleFileResponse.getName();
                String indexNumber = getMainName(nameFile);
                maKidPics.setMainStatus(indexNumber.equals("0") ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                maKidPics.setKid(kid);
                maKidPicsRepository.save(maKidPics);    // Save MaKidPics
            });
        } else if (!identityKidRequest.getNewFileJson().isEmpty() && !StringUtils.isBlank(kid.getPicJsonUrl()) &&
                CollectionUtils.isEmpty(identityKidRequest.getDelIdentityModelList())) {
            // add nội dung vào file Json
            Gson gson = new Gson();
            Path path = Paths.get(kid.getPicJsonUrlLocal());
            String name = path.getFileName().toString();
            String nameResult = getJsonName(name);
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(identityKidRequest.getNewFileJson().getBytes());
            String inputContent = IOUtils.toString(arrayInputStream, StandardCharsets.UTF_8);
            String strContent = Files.readString(path);
            Map recentMap = gson.fromJson(strContent, Map.class);
            Map newMap = gson.fromJson(inputContent, Map.class);
            newMap.forEach((k, v) -> {
                recentMap.remove(k);
            });
            Files.delete(path);
            recentMap.putAll(newMap);
            String newContent = new Gson().toJson(recentMap);
            MultipartFile resuleFile = new MockMultipartFile("file", nameResult, "application/json", newContent.getBytes());
            HandleFileResponse fileRes = HandleFileUtils.getUrlFileJsonSavedJson(resuleFile, idSchool, "diemdanhjson");
            kid.setPicJsonUrlLocal(fileRes.getUrlLocal());
            kid.setPicJsonUrl(fileRes.getUrlWeb());
            kidsRepository.save(kid);
            // Thêm ảnh
            List<MultipartFile> multipartFileList = identityKidRequest.getMultipartFileImageList();
            multipartFileList.forEach(x -> {
                MaKidPics maKidPics = new MaKidPics();
                HandleFileResponse handleFileResponse = null;
                try {
                    handleFileResponse = HandleFileUtils.getUrlPictureIdentitySaved(x, idSchool, UploadDownloadConstant.ALBUM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert handleFileResponse != null;
                maKidPics.setUrlWeb(handleFileResponse.getUrlWeb());
                maKidPics.setUrlLocal(handleFileResponse.getUrlLocal());
                String nameFile = handleFileResponse.getName();
                String indexNumber = getMainName(nameFile);
                maKidPics.setMainStatus(indexNumber.equals("0") ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                maKidPics.setKid(kid);
                maKidPicsRepository.save(maKidPics);    // Save MaKidPics
            });
        }
        // Xóa ảnh, Thêm ảnh mới, xóa keyJson, , thêm file Json, lưu lại
        if (!identityKidRequest.getNewFileJson().isEmpty() && !CollectionUtils.isEmpty(identityKidRequest.getMultipartFileImageList())
                && !CollectionUtils.isEmpty(identityKidRequest.getDelIdentityModelList())) {
            // Kiểm tra có main picture
            boolean mainName = false;
            for (MultipartFile x : identityKidRequest.getMultipartFileImageList()) {
                mainName = getMainName(Objects.requireNonNull(x.getOriginalFilename())).equals("0");
                if (mainName) break;
            }
            // Delete Picture
            List<IdentityModel> delModelList = identityKidRequest.getDelIdentityModelList();
            List<String> listKey = new ArrayList<>();
            for (IdentityModel x : delModelList) {
                Optional<MaKidPics> optionalMaKidPics = maKidPicsRepository.findById(x.getDelImage());
                MaKidPics maKidPics = null;
                if (optionalMaKidPics.isPresent()) {
                    maKidPics = optionalMaKidPics.get();
                }
                assert maKidPics != null;
                if (!maKidPics.isMainStatus()) {
                    deleteImage(maKidPics.getUrlLocal(), x);
                } else if (maKidPics.isMainStatus() && mainName) {
                    deleteImage(maKidPics.getUrlLocal(), x);
                }
                listKey.add(x.getDelKeyJson()); // lấy list key cần xóa trong file Json
            }
            String strUrl = kid.getPicJsonUrlLocal();    // Lấy link Local của file Json
            // Xóa list key trong file json cũ
            afterDelete = deleteFileJson(strUrl, listKey);
            // Add ảnh, thêm thông tin bảng MaKidPics
            List<MultipartFile> multipartFileList = identityKidRequest.getMultipartFileImageList(); // Lấy ảnh thêm vào
            multipartFileList.forEach(x -> {
                MaKidPics maKidPics = new MaKidPics();
                HandleFileResponse handleFileResponse = null;
                try {
                    handleFileResponse = HandleFileUtils.getUrlPictureIdentitySaved(x, idSchool, UploadDownloadConstant.ALBUM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert handleFileResponse != null;
                maKidPics.setUrlWeb(handleFileResponse.getUrlWeb());
                maKidPics.setUrlLocal(handleFileResponse.getUrlLocal());
                // Set Main status
                String nameFile = handleFileResponse.getName();
                String indexNumber = getMainName(nameFile);
                maKidPics.setMainStatus(indexNumber.equals("0") ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                maKidPics.setKid(kid);
                maKidPicsRepository.save(maKidPics);    // Save MaKidPics
            });
            // Add key của file json mới vào trong file Json cũ
            Gson gson = new Gson();
//            String nameAdd = getJsonName(afterDelete.getOriginalFilename());
            String nameAdd = afterDelete.getOriginalFilename();
            ByteArrayInputStream streamOri = new ByteArrayInputStream(afterDelete.getBytes());
            ByteArrayInputStream streamIn = new ByteArrayInputStream(identityKidRequest.getNewFileJson().getBytes());
            String strOri = IOUtils.toString(streamOri, StandardCharsets.UTF_8);
            String strIn = IOUtils.toString(streamIn, StandardCharsets.UTF_8);
            Map map = gson.fromJson(strOri, Map.class);
            Map map1 = gson.fromJson(strIn, Map.class);
            map.putAll(map1);
            String strJson = new Gson().toJson(map);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    nameAdd, "application/json", strJson.getBytes());
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFileJsonSavedJson(multipartFile, idSchool, "diemdanhjson");
            kid.setPicJsonUrl(handleFileResponse.getUrlWeb());
            kid.setPicJsonUrlLocal(handleFileResponse.getUrlLocal());
            kidsRepository.save(kid);
        }
        return true;
    }

    private List<InfoEmployeeSchool> getEmployeeNumberFromExEmployeeClass(List<ExEmployeeClass> exEmployeeClassList) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = exEmployeeClassList.stream().map(ExEmployeeClass::getInfoEmployeeSchool).collect(Collectors.toList());
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        return infoEmployeeSchoolList;
    }


    // Xóa nội dung file Json và đổi tên
    private MultipartFile deleteFileJson(String linkFile, List<String> key) throws IOException {
        Gson gson = new Gson();
        File file = new File(linkFile);
        String nameResult = getJsonName(file.getName());
        Reader reader = Files.newBufferedReader(Paths.get(linkFile));
        Map map = gson.fromJson(reader, Map.class);
        key.forEach(map::remove);
        reader.close();
        Files.delete(Paths.get(linkFile));
        String strJson = new Gson().toJson(map);
//        MultipartFile multipartFile = new MockMultipartFile(nameResult, strJson.getBytes());
        MultipartFile multipartFile = new MockMultipartFile("file",
                nameResult, "application/json", strJson.getBytes());
        return multipartFile;
    }

    private String getNamePath(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        String pathParent = path.getParent().toString();
        String fileName = "\\thumbnail_" + path.getFileName().toString();
        String fileLink = pathParent + fileName;
//      Files.delete(Paths.get(fileLink));
        return fileLink;
    }

    private String getMainName(String fileName) {
        int lastIndex = fileName.lastIndexOf("_");
        int dotindex = fileName.indexOf(".");
        return fileName.substring(dotindex - 1, dotindex);
    }

    private String getJsonName(String filename) {
        String[] arrStr = filename.split("_");
        if (arrStr.length == 0) return "";
        else {
            String result = arrStr[0] + "_" + arrStr[1] + ".json";
            return result;
        }
    }

    private void deleteImage(String url, IdentityModel x) {
        File file = new File(url);
        file.delete();
//            Files.delete(Paths.get(url));
        maKidPicsRepository.deleteByIdCustom(x.getDelImage());
    }
}
