package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.album.DeleteMultpicturePlusRequest;
import com.example.onekids_project.mobile.plus.request.album.*;
import com.example.onekids_project.mobile.plus.response.album.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AlbumPlusMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumPlusMobileServiceImpl implements AlbumPlusMobileService {

    int monthCurrent = LocalDate.now().getMonthValue();
    int yearCurrent = LocalDate.now().getYear();
    int scaledWidth = 512;
    int scaledHeight = 132;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ListPictureRepository listPictureRepository;
    @Autowired
    private ExAlbumKidsRepository exAlbumKidsRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListAlbumPlusMobileResponse findAllAlbumForPlus(UserPrincipal principal, SearchAlbumPlusRequest searchAlbumPlusRequest) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Album> albumList = albumRepository.findAllAlbumForPlus(idSchool, searchAlbumPlusRequest);
        ListAlbumPlusMobileResponse listAlbumPlusMobileResponse = new ListAlbumPlusMobileResponse();
        List<AlbumPlusMobileResponse> albumPlusMobileResponses = new ArrayList<>();
        albumList.forEach(x -> {
            AlbumPlusMobileResponse model = new AlbumPlusMobileResponse();
            model.setId(x.getId());
            model.setAlbumName(x.getAlbumName());
            model.setCreatedDate(ConvertData.convertLocalDateTimeToString(x.getCreatedDate()));
            model.setPictureNumber(x.getAlistPictureList().size());
            if (x.getAlistPictureList().size() > 0) {
                model.setUrlPictureFirst(x.getAlistPictureList().get(0).getUrlPicture());
            } else if (x.getAlistPictureList().size() == 0) {
                model.setUrlPictureFirst("");
            }
            model.setAlbumNew(x.getAlistPictureList().stream().anyMatch(a -> !a.getIsApproved()));
            model.setApproved(x.getAlistPictureList().stream().anyMatch(ListPicture::getIsApproved));
            albumPlusMobileResponses.add(model);
        });
        boolean lastPage = albumList.size() < MobileConstant.MAX_PAGE_ITEM;
        listAlbumPlusMobileResponse.setDataList(albumPlusMobileResponses);
        listAlbumPlusMobileResponse.setLastPage(lastPage);
        return listAlbumPlusMobileResponse;
    }

    @Transactional
    @Override
    public boolean deleteMultiAlbumPlus(UserPrincipal principal, Long idClassLogin, UpdateApproveMultialbumPlusRequest deleteMultialbumPlusRequest) {
        for (Long idAlbum : deleteMultialbumPlusRequest.getIdAlbumList()) {
            Optional<Album> albumOptional = albumRepository.findById(idAlbum);
            if (albumOptional.isEmpty()) {
                return false;
            }
            Album album = albumOptional.get();
            if (!CollectionUtils.isEmpty(album.getAlistPictureList())) {
                album.getAlistPictureList().forEach(listPicture -> {
                            try {
                                File file = new File(listPicture.getUrlLocal());
                                file.delete();
                                listPicture.setDelActive(AppConstant.APP_FALSE);
                                listPicture.setIsApproved(AppConstant.APP_FALSE);
                                listPictureRepository.deleteById(listPicture.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
            if (!CollectionUtils.isEmpty(album.getExAlbumKidsListA())) {
                album.getExAlbumKidsListA().forEach(x -> {
                            try {
                                x.setDelActive(AppConstant.APP_FALSE);
                                exAlbumKidsRepository.deleteById(x.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            exAlbumKidsRepository.save(x);
                        }
                );
            }
            album.setDelActive(AppConstant.APP_FALSE);
        }
        return true;
    }

    @Override

    public boolean updateApprovedAlbum(UserPrincipal principal, UpdateApproveMultialbumPlusRequest request) throws FirebaseMessagingException {
        for (Long idAlbum : request.getIdAlbumList()) {
            Album album = albumRepository.findById(idAlbum).orElseThrow();
            album.getAlistPictureList().forEach(item -> item.setIsApproved(AppConstant.APP_TRUE));
            albumRepository.save(album);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deleteMultiAlbum(UserPrincipal principal, DeleteMultialbumPlusRequest request) {
        for (Long idAlbum : request.getIdAlbumList()) {
            Album album = albumRepository.findById(idAlbum).orElseThrow();
            if (album.getAlistPictureList().size() > 0) {
                album.getAlistPictureList().forEach(listPicture -> {
                    try {
                        File file = new File(listPicture.getUrlLocal());
                        file.delete();
                        listPictureRepository.deletePictureById(listPicture.getId());
                        String urlLocal = listPicture.getUrlLocal();
                        HandleFileUtils.deletePictureInFolder(urlLocal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            if (!CollectionUtils.isEmpty(album.getExAlbumKidsListA())) {
                album.getExAlbumKidsListA().forEach(x -> {
                    try {
                        exAlbumKidsRepository.deleteExAlbumByIdAlbum(x.getAlbum().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            albumRepository.deleteAlbumById(idAlbum);
        }
        return true;
    }

    @Override
    public AlbumDetailPlusMobileResponse findDetailalbumplus(UserPrincipal principal, Long id) {
        Album album = albumRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        Long idAlbum = album.getId();
        List<ListPicture> listPictureList = listPictureRepository.findByAlbum_IdAndDelActiveTrue(idAlbum);
        AlbumDetailPlusMobileResponse model = new AlbumDetailPlusMobileResponse();
        model.setId(album.getId());
        model.setAlbumName(album.getAlbumName());
        model.setAlbumType(album.getMaClass() == null || album.getMaClass().getId() == 0 ? AppConstant.TYPE_SCHOOL : album.getMaClass().getClassName());
        model.setAlbumDescription(album.getAlbumDescription());
        MaUser maUser = maUserRepository.findById(album.getIdCreated()).orElseThrow();
        model.setCreatedBy(maUser.getFullName());
        model.setCreatedDate(ConvertData.convertLocalDateTimeToString(album.getCreatedDate()));
        model.setApproved(album.getAlistPictureList().stream().anyMatch(ListPicture::getIsApproved));
        if (listPictureList.size() > 0) {
            model.setUrlPictureFirst(album.getAlistPictureList().get(0).getUrlPicture());
        }
        model.setIdClass(album.getMaClass() == null || album.getMaClass().getId() == 0 ? 0L : album.getMaClass().getId());
        List<ListPicturePlusOtherRespone> listPictureOtherResponeList = listMapper.mapList(listPictureList, ListPicturePlusOtherRespone.class);
        model.setListPictureList(listPictureOtherResponeList);
        model.setPictureNumber(listPictureList.size());

        return model;
    }

    @Transactional
    @Override
    public boolean deleteMultiPicture(UserPrincipal principal, DeleteMultpicturePlusRequest request) {
        int coutSuccess = 0;
        for (Long idPicture : request.getIdPictureList()) {
            Optional<ListPicture> listPictureOptional = listPictureRepository.findById(idPicture);
            ListPicture listPicture = listPictureOptional.get();
            Album album = albumRepository.findById(listPicture.getAlbum().getId()).orElseThrow();
            int coutNumber = album.getAlistPictureList().size();

            Long idAlbum = album.getId();
            listPictureRepository.deletePictureById(idPicture);
            String urlLocal = listPicture.getUrlLocal();
            HandleFileUtils.deletePictureInFolder(urlLocal);
            coutSuccess++;
            if (coutSuccess == coutNumber) {
                exAlbumKidsRepository.deleteExAlbumByIdAlbum(idAlbum);
                albumRepository.deleteAlbumById(idAlbum);
            }
        }
        return true;
    }

    @Transactional
    @Override

    public boolean approvePicture(UserPrincipal principal, ApproveMultpicturePlusRequest request) throws FirebaseMessagingException {
        ListPicture listPictureOne = listPictureRepository.findById(request.getIdPictureList().get(0)).orElseThrow();
        for (Long idPicture : request.getIdPictureList()) {
            ListPicture listPicture = listPictureRepository.findById(idPicture).orElseThrow();
            listPicture.setIsApproved(AppConstant.APP_TRUE);
            listPictureRepository.save(listPicture);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean createAlbumPlus(UserPrincipal principal, CreateAlbumPlusRequest request) throws IOException, FirebaseMessagingException {
        Long idSchool = principal.getIdSchoolLogin();
        Album album = modelMapper.map(request, Album.class);
        album.setIdSchool(idSchool);
        album.setCreatedBy(principal.getFullName());
        List<Kids> kidsList;
        if (request.getIdClass() == null || request.getIdClass() == 0) {
            album.setAlbumType(AppConstant.ALBUMSCHOOL);
            kidsList = kidsRepository.findAllKidsAlbum(idSchool);
            List<ExAlbumKids> exAlbumKidsList = new ArrayList<>();
            kidsList.forEach(kids -> {
                ExAlbumKids exAlbumKids = new ExAlbumKids();
                exAlbumKids.setKids(kids);
                exAlbumKids.setCreatedBy(principal.getFullName());
                exAlbumKids.setIdCreated(principal.getId());
                exAlbumKids.setCreatedDate(LocalDateTime.now());
                exAlbumKids.setStatusUnread(AppConstant.APP_FALSE);
                exAlbumKids.setAlbum(album);
                album.setExAlbumKidsListA(exAlbumKidsList);
                exAlbumKidsRepository.save(exAlbumKids);
            });
            album.setExAlbumKidsListA(exAlbumKidsList);
        } else {
            kidsList = kidsRepository.findAllKidsAlbumClass(idSchool, request.getIdClass());
            album.setAlbumType(AppConstant.CLASS);
            album.setMaClass(maClassRepository.findByIdMaClass(idSchool, request.getIdClass()).get());
            List<ExAlbumKids> exAlbumKidsList = new ArrayList<>();
            kidsList.forEach(kids -> {
                ExAlbumKids exAlbumKids = new ExAlbumKids();
                exAlbumKids.setKids(kids);
                exAlbumKids.setCreatedBy(principal.getFullName());
                exAlbumKids.setCreatedDate(LocalDateTime.now());
                exAlbumKids.setStatusUnread(AppConstant.APP_FALSE);
                exAlbumKids.setAlbum(album);
                album.setExAlbumKidsListA(exAlbumKidsList);
                exAlbumKidsRepository.save(exAlbumKids);
            });
            album.setExAlbumKidsListA(exAlbumKidsList);
        }
        Album newAlbum = albumRepository.save(album);
        if (request.getFileList() != null) {
            /**
             * Khai báo thư mục đường dẫn local để lưu file
             */
            List<ListPicture> listPictureList = new ArrayList<>();
            for (MultipartFile file : request.getFileList()) {
                ListPicture listPicture = new ListPicture();
                listPicture.setAlbum(album);
                listPicture.setIdCreated(principal.getId());
                listPicture.setIsApproved(principal.getSchoolConfig().isAlbum());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(file, idSchool, UploadDownloadConstant.ALBUM);
                listPicture.setUrlPicture(handleFileResponse.getUrlWeb());
                listPicture.setUrlLocal(handleFileResponse.getUrlLocal());
                listPictureList.add(listPicture);
            }
            listPictureRepository.saveAll(listPictureList);
            newAlbum.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
        }
        if (principal.getSchoolConfig().isAlbum()) {
            for (Kids x : kidsList) {
                firebaseFunctionService.sendParentByPlus(37L, x, FirebaseConstant.CATEGORY_ALBUM, request.getAlbumName());
            }
        }
        return true;
    }

    private void sendFireBase(List<Kids> kidsList, String nameAlbum, boolean approved) throws FirebaseMessagingException {
        if (approved) {
            for (Kids x : kidsList) {
                //gửi firebase
                firebaseFunctionService.sendParentByPlus(37L, x, FirebaseConstant.CATEGORY_ALBUM, nameAlbum);
            }
        }
    }

    @Transactional
    @Override
    public boolean updateAlbum(UserPrincipal principal, UpdateAlbumPlusRequest request) throws IOException {
        Long idSchool = principal.getIdSchoolLogin();
        Album album = albumRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (request.getIdPictureList() != null) {
            for (Long idPicture : request.getIdPictureList()) {
                Optional<ListPicture> listPictureOptional = listPictureRepository.findById(idPicture);
                ListPicture listPicture = listPictureOptional.get();
                listPictureRepository.deletePictureById(listPicture.getId());
                String urlLocal = listPicture.getUrlLocal();
                HandleFileUtils.deletePictureInFolder(urlLocal);
            }
        }
        if (request.getFileList() != null) {
            int monthCurrent = LocalDate.now().getMonthValue();
            String uploadInAlbum = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\";
            List<ListPicture> listPictureList = new ArrayList<>();
            int pictureNumber = 0;
            List<String> urlListUploaded = new ArrayList<>();
            for (MultipartFile file : request.getFileList()) {
                String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + file.getOriginalFilename());
                Path fileNameAndPath = Paths.get(uploadInAlbum, fileName);
                ListPicture listPicture = new ListPicture();
                listPicture.setAlbum(album);
                listPicture.setIsApproved(AppConstant.APP_TRUE);
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(file, idSchool, UploadDownloadConstant.ALBUM);
                listPicture.setUrlPicture(handleFileResponse.getUrlWeb());
                listPicture.setUrlLocal(handleFileResponse.getUrlLocal());
                listPictureList.add(listPicture);
                try {
                    pictureNumber++;
                    if ((album.getAlistPictureList().size() + pictureNumber) > AppConstant.NUMBER_PICTURE_IN_ALBUM) {
                        return false;
                    }
                    urlListUploaded.add(AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\" + fileName);
                    Files.write(fileNameAndPath, file.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            album.setAlistPictureList(listPictureList);
            if (CollectionUtils.isEmpty(listPictureList)) {
                album.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
            }
        }

        // update truowngf sang lop
        if (album.getMaClass() == null && request.getIdClass() != 0) {
            album.setAlbumType(AppConstant.CLASS);
            album.setMaClass(maClassRepository.findByIdMaClass(idSchool, request.getIdClass()).get());
            List<Kids> albumSchoollist1 = kidsRepository.findAllKidsA(idSchool); // tất cả học sinh -- list 1
            List<Kids> albumClasslist2 = kidsRepository.findAlbumClass(idSchool, request.getIdClass()); // danh sách học sinh theo idclass -- list 2
            List<Kids> kidsList3 = (List<Kids>) CollectionUtils.intersection(albumClasslist2, albumSchoollist1); // giao của list (1) và (2)
            exAlbumKidsRepository.deleteExAlbumByIdAlbum(request.getId());
            if (!CollectionUtils.isEmpty(kidsList3)) {
                List<ExAlbumKids> exAlbumKidsList = new ArrayList<>();
                kidsList3.forEach(kids -> {
                    ExAlbumKids exAlbumKids = new ExAlbumKids();
                    exAlbumKids.setKids(kids);
                    exAlbumKids.setCreatedDate(LocalDateTime.now());
                    exAlbumKids.setAlbum(album);
                    exAlbumKids.setStatusUnread(AppConstant.APP_TRUE);
                    exAlbumKidsRepository.save(exAlbumKids);
                });
                album.setExAlbumKidsListA(exAlbumKidsList);
            }
        } else if (album.getMaClass() != null && request.getIdClass() == 0) {
            // update  lớp sang trường
            album.setAlbumType(AppConstant.ALBUMSCHOOL);
            List<Kids> kidslist1 = kidsRepository.findAllKidsAlbumClass(idSchool, album.getMaClass().getId());
            List<Kids> kidsList2 = kidsRepository.findAllKidsA(idSchool);
            List<Kids> kidsList3 = (List<Kids>) CollectionUtils.subtract(kidsList2, kidslist1);
            if (!CollectionUtils.isEmpty(kidslist1)) {
                List<ExAlbumKids> exAlbumKidsList = new ArrayList<>();
                kidsList3.forEach(kids -> {
                    ExAlbumKids exAlbumKids = new ExAlbumKids();
                    exAlbumKids.setKids(kids);
                    exAlbumKids.setCreatedDate(LocalDateTime.now());
                    exAlbumKids.setAlbum(album);
                    exAlbumKids.setStatusUnread(AppConstant.APP_FALSE);
                    exAlbumKidsRepository.save(exAlbumKids);
                });
                album.setMaClass(null);
                album.setExAlbumKidsListA(exAlbumKidsList);
            }
        } else if (album.getMaClass() != null && request.getIdClass() != 0) {
            album.setAlbumType(AppConstant.CLASS);
            album.setMaClass(maClassRepository.findByIdMaClass(idSchool, request.getIdClass()).get());
            // update lớp sang lớp
            // xóa kids theo idAlum
            exAlbumKidsRepository.deleteExAlbumByIdAlbum(request.getId());
            //  thêm mới kids vào ex_album_kids
            List<MaClass> maClassList = maClassRepository.findAllMaClassAlbum(idSchool);
            List<Kids> kidClassList = kidsRepository.findAllKidsAlbumClass(idSchool, request.getIdClass());
            if (!CollectionUtils.isEmpty(maClassList)) {
                List<ExAlbumKids> exAlbumKidsList = new ArrayList<>();
                kidClassList.forEach(kids -> {
                    ExAlbumKids exAlbumKids = new ExAlbumKids();
                    exAlbumKids.setKids(kids);
                    exAlbumKids.setCreatedDate(LocalDateTime.now());
                    exAlbumKids.setAlbum(album);
                    exAlbumKids.setStatusUnread(AppConstant.APP_FALSE);
                    exAlbumKidsRepository.save(exAlbumKids);
                });
                album.setExAlbumKidsListA(exAlbumKidsList);
            }
        }
        album.setAlbumName(request.getAlbumName());
        album.setAlbumDescription(request.getAlbumDescription());
        albumRepository.save(album);
        return true;
    }

}
