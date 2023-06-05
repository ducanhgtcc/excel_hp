package com.example.onekids_project.mobile.teacher.service.serviceimpl;

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
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.request.album.*;
import com.example.onekids_project.mobile.teacher.response.album.AlbumDetailTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.response.album.AlbumTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.response.album.ListAlbumTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.response.album.ListPictureOtherRespone;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AlbumTeacherMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AlbumTeacherMobileServiceImpl implements AlbumTeacherMobileService {

    int monthCurrent = LocalDate.now().getMonthValue();
    int yearCurrent = LocalDate.now().getYear();
    int scaledWidth = 512;
    int scaledHeight = 132;

    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private ListPictureRepository listPictureRepository;
    @Autowired
    private ExAlbumKidsRepository exAlbumKidsRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Transactional
    @Override
    public boolean deleteMultiAlbumTeacher(UserPrincipal userPrincipal, Long
            idClassLogin, DeleteMultialbumTeacherRequest deleteMultialbumTeacherRequest) {
        for (Long idAlbum : deleteMultialbumTeacherRequest.getIdAlbumList()) {
            Optional<Album> albumOptional = albumRepository.findById(idAlbum);
            if (albumOptional.isEmpty()) {
                return false;
            }
            AtomicInteger cout = new AtomicInteger();
            Album album = albumOptional.get();
            if (!album.getIdCreated().equals(userPrincipal.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Album của người khác, xóa thất bại!");
            } else {
                if (!CollectionUtils.isEmpty(album.getAlistPictureList())) {
                    album.getAlistPictureList().forEach(
                            listPicture -> {
                                try {
                                    if (!listPicture.getIdCreated().equals(userPrincipal.getId())) {
                                        throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST, "Album có ảnh của người khác, xóa thất bại!");
                                    } else {
                                        File file = new File(listPicture.getUrlLocal());
                                        file.delete();
                                        listPictureRepository.deletePictureById(listPicture.getId());
                                        String urlLocal = listPicture.getUrlLocal();
                                        HandleFileUtils.deletePictureInFolder(urlLocal);
                                        cout.getAndIncrement();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                    if (!CollectionUtils.isEmpty(album.getExAlbumKidsListA())) {
                        album.getExAlbumKidsListA().forEach(x -> {
                            try {
                                exAlbumKidsRepository.deleteExAlbumByIdAlbum(x.getAlbum().getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
            if (cout.get() == album.getAlistPictureList().size()) {
                albumRepository.deleteAlbumById(album.getId());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Album có ảnh của người khác, xóa thất bại!");
            }
        }
        return true;
    }

    @Override
    public AlbumDetailTeacherMobileResponse findAlbummobdetailTeacher(
            UserPrincipal principal, Long id) {
        Album album = albumRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found album by id"));
        Long idAlbum = album.getId();
        List<ListPicture> listPictureList = listPictureRepository.findByAlbum_IdAndDelActiveTrue(idAlbum);
        AlbumDetailTeacherMobileResponse model = new AlbumDetailTeacherMobileResponse();

        model.setId(album.getId());
        for (ListPicture listPicture : album.getAlistPictureList()) {
            if (listPicture.getIsApproved() == AppConstant.APP_FALSE) {
                model.setConfirmStatus(AppConstant.APP_FALSE);
                continue;
            } else {
                model.setConfirmStatus(AppConstant.APP_TRUE);
            }
        }
        if (album.getAlbumType().equals("Lớp")) {
            List<ListPictureOtherRespone> listPictureOtherResponeList = listMapper.mapList(listPictureList, ListPictureOtherRespone.class);
            model.setListPictureList(listPictureOtherResponeList);
            model.setPictureNumber(album.getAlistPictureList().size());
        } else {
            List<ListPictureOtherRespone> listPictureOtherResponeList = listMapper.mapList(listPictureList, ListPictureOtherRespone.class);
            List<ListPictureOtherRespone> listPictureOtherResponeList1 = listPictureOtherResponeList.stream().filter(ListPictureOtherRespone::getIsApproved).collect(Collectors.toList());
            model.setListPictureList(listPictureOtherResponeList1);
            int cout = (int) album.getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
            model.setPictureNumber(cout);
        }
        model.setAlbumType(album.getAlbumType());
        model.setAlbumName(album.getAlbumName());
        model.setAlbumDescription(album.getAlbumDescription());
        model.setCreatedBy(album.getCreatedBy());
        model.setCreatedDate(ConvertData.convertLocalDateTimeToString(album.getCreatedDate()));
        if (album.getAlistPictureList().size() > 0) {
            model.setUrlPictureFirst(album.getAlistPictureList().get(0).getUrlPicture());
        }
        return model;
    }

    @Transactional
    @Override
    public boolean createAlbum(Long idSchoolLogin, UserPrincipal principal, CreateAlbumTeacherRequest createAlbumTeacherRequest) throws FirebaseMessagingException, IOException {
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        Album album = modelMapper.map(createAlbumTeacherRequest, Album.class);
        album.setCreatedBy(principal.getFullName());
        List<Kids> kidClassList = kidsRepository.findAllKidsAlbumClass(idSchool, principal.getIdClassLogin());
        album.setAlbumType(AppConstant.CLASS);
        album.setCreatedDate(LocalDateTime.now());
        album.setIdCreated(principal.getId());
        album.setDelActive(AppConstant.APP_TRUE);
        album.setIdSchool(principal.getIdSchoolLogin());
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow();
        album.setMaClass(maClass);
        List<ExAlbumKids> exAlbumKidsList = new ArrayList<>();
        kidClassList.forEach(kids -> {
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
        Album newAlbum = albumRepository.save(album);
        if (createAlbumTeacherRequest.getFileList() != null) {
            List<ListPicture> listPictureList = new ArrayList<>();
            for (MultipartFile file : createAlbumTeacherRequest.getFileList()) {
                ListPicture listPicture = new ListPicture();
                listPicture.setAlbum(album);
                listPicture.setIsApproved(principal.getSchoolConfig().isAlbum());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(file, idSchool, UploadDownloadConstant.ALBUM);
                listPicture.setUrlPicture(handleFileResponse.getUrlWeb());
                listPicture.setUrlLocal(handleFileResponse.getUrlLocal());
                listPictureList.add(listPicture);
            }
            album.setAlistPictureList(listPictureList);
            album.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
        }
        albumRepository.save(newAlbum);
        //firebase
        if (principal.getSchoolConfig().isAlbum()) {
            for (Kids x : kidClassList) {
                firebaseFunctionService.sendParentByTeacher(37L, x, FirebaseConstant.CATEGORY_ALBUM, createAlbumTeacherRequest.getAlbumName());
            }
        } else {
            //gửi thông báo duyệt album cho plus
            String title = "Lớp " + maClass.getClassName() + " có Album mới cần duyệt";
            firebaseFunctionService.sendPlusCommonHasPlusList(title, createAlbumTeacherRequest.getAlbumName(), idSchool, FirebaseConstant.CATEGORY_ALBUM);
        }
        return true;
    }

    @Override
    public ListAlbumTeacherMobileResponse findAllAlbumForTeachers(UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        List<Album> albumList = albumRepository.findAllAlbumForTeacherx(idSchool, idClass, albumTeacherRequest);

        ListAlbumTeacherMobileResponse listAlbumTeacherMobileResponse = new ListAlbumTeacherMobileResponse();
        List<AlbumTeacherMobileResponse> albumTeacherMobileResponses = new ArrayList<>();
        albumList.forEach(x -> {
            AlbumTeacherMobileResponse model = new AlbumTeacherMobileResponse();
            int cout = (int) x.getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
            if (x.getMaClass() == null) {
                if (cout > 0) {
                    model.setId(x.getId());
                    model.setPictureNumber(cout);
                    model.setAlbumName(x.getAlbumName());
                    model.setCreatedDate(ConvertData.convertLocalDateTimeToString(x.getCreatedDate()));
                    List<ListPicture> listAppove = x.getAlistPictureList().stream().filter(ListPicture::getIsApproved).collect(Collectors.toList());
                    if (listAppove.size() > 0) {
                        String url = listAppove.get(0).getUrlPicture();
                        model.setUrlPictureFirst(url);
                    }
                    model.setAlbumNew(x.getAlistPictureList().stream().anyMatch(a -> !a.getIsApproved()));
                    model.setApproved(x.getAlistPictureList().stream().anyMatch(ListPicture::getIsApproved));
                    albumTeacherMobileResponses.add(model);
                }
            } else {
                Long idCheck = x.getMaClass().getId();
                if (idCheck.equals(idClass)) {
                    if (x.getAlistPictureList().size() > 0) {
                        model.setId(x.getId());
                        model.setPictureNumber(x.getAlistPictureList().size());
                        model.setAlbumName(x.getAlbumName());
                        model.setCreatedDate(ConvertData.convertLocalDateTimeToString(x.getCreatedDate()));
                        model.setAlbumNew(x.getAlistPictureList().stream().anyMatch(a -> !a.getIsApproved()));
                        model.setUrlPictureFirst(x.getAlistPictureList().get(0).getUrlPicture());
                        model.setApproved(x.getAlistPictureList().stream().anyMatch(ListPicture::getIsApproved));
                        albumTeacherMobileResponses.add(model);
                    }
                }
            }
        });
        boolean lastPage = albumList.size() < MobileConstant.MAX_PAGE_ITEM;
        listAlbumTeacherMobileResponse.setDataList(albumTeacherMobileResponses);
        listAlbumTeacherMobileResponse.setLastPage(lastPage);
        return listAlbumTeacherMobileResponse;
    }

    @Override
    public ListAlbumTeacherMobileResponse findAlbumSchoolteacher(UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        Long idSchool = principal.getIdSchoolLogin();
        List<Album> albumList = albumRepository.findAllbumSchoolForTeachers(idSchool, idClass, albumTeacherRequest);
        ListAlbumTeacherMobileResponse listAlbumTeacherMobileResponse = new ListAlbumTeacherMobileResponse();
        List<AlbumTeacherMobileResponse> albumTeacherMobileResponses = new ArrayList<>();
        albumList.forEach(x -> {
            AlbumTeacherMobileResponse model = new AlbumTeacherMobileResponse();
            int cout = (int) x.getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
            if (cout > 0) {
                model.setId(x.getId());
                model.setPictureNumber(cout);
                model.setAlbumName(x.getAlbumName());
                model.setCreatedDate(ConvertData.convertLocalDateTimeToString(x.getCreatedDate()));
                List<ListPicture> listAppove = x.getAlistPictureList().stream().filter(ListPicture::getIsApproved).collect(Collectors.toList());
                if (listAppove.size() > 0) {
                    String url = listAppove.get(0).getUrlPicture();
                    model.setUrlPictureFirst(url);
                }
                model.setAlbumNew(x.getAlistPictureList().stream().anyMatch(a -> !a.getIsApproved()));
                model.setApproved(x.getAlistPictureList().stream().anyMatch(ListPicture::getIsApproved));
                albumTeacherMobileResponses.add(model);
            }
        });
        boolean lastPage = albumList.size() < MobileConstant.MAX_PAGE_ITEM;
        listAlbumTeacherMobileResponse.setDataList(albumTeacherMobileResponses);
        listAlbumTeacherMobileResponse.setLastPage(lastPage);
        return listAlbumTeacherMobileResponse;
    }

    @Override
    public ListAlbumTeacherMobileResponse findAlbumClassteacher(UserPrincipal principal, AlbumTeacherRequest albumTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        Long idSchool = principal.getIdSchoolLogin();
        List<Album> albumList = albumRepository.findAllbumClassForTeacher(idSchool, idClass, albumTeacherRequest);
        ListAlbumTeacherMobileResponse listAlbumTeacherMobileResponse = new ListAlbumTeacherMobileResponse();
        List<AlbumTeacherMobileResponse> albumTeacherMobileResponses = new ArrayList<>();
        albumList.forEach(
                x -> {
                    AlbumTeacherMobileResponse model = new AlbumTeacherMobileResponse();
                    Long idCheck = x.getMaClass().getId();
                    if (idCheck.equals(idClass)) {
                        if (x.getAlistPictureList().size() > 0) {
                            model.setId(x.getId());
                            model.setPictureNumber(x.getAlistPictureList().size());
                            model.setAlbumName(x.getAlbumName());
                            model.setCreatedDate(ConvertData.convertLocalDateTimeToString(x.getCreatedDate()));
                            if (x.getAlistPictureList().size() > 0) {
                                model.setUrlPictureFirst(x.getAlistPictureList().get(0).getUrlPicture());
                            } else {
                                model.setUrlPictureFirst(MobileConstant.NO_IMAGE);
                            }
                            model.setAlbumNew(
                                    x.getAlistPictureList().stream().anyMatch(a -> !a.getIsApproved()));
                            model.setApproved(
                                    x.getAlistPictureList().stream().anyMatch(ListPicture::getIsApproved));
                            albumTeacherMobileResponses.add(model);
                        }
                    }
                });
        boolean lastPage = albumList.size() < MobileConstant.MAX_PAGE_ITEM;
        listAlbumTeacherMobileResponse.setDataList(albumTeacherMobileResponses);
        listAlbumTeacherMobileResponse.setLastPage(lastPage);
        return listAlbumTeacherMobileResponse;
    }

    @Transactional
    @Override
    public int deleteMultiPictureTeacher(UserPrincipal principal, Long idClassLogin, DeleteMultpictureTeacherRequest deleteMultpictureTeacherRequest) {
        int coutSuccess = 0;
        for (Long idPicture : deleteMultpictureTeacherRequest.getIdPictureList()) {
            Optional<ListPicture> listPictureOptional = listPictureRepository.findById(idPicture);
            if (listPictureOptional.isPresent()) {
                ListPicture listPicture = listPictureOptional.get();
                Album album = albumRepository.findById(listPicture.getAlbum().getId()).orElseThrow();
                int coutNumber = album.getAlistPictureList().size();
                Long idAlbum = album.getId();
                if (listPicture.getIdCreated().equals(principal.getId())) {
                    listPictureRepository.deletePictureById(listPicture.getId());
                    String urlLocal = listPicture.getUrlLocal();
                    HandleFileUtils.deletePictureInFolder(urlLocal);
                    coutSuccess++;
                }
                if (coutSuccess == coutNumber) {
                    exAlbumKidsRepository.deleteExAlbumByIdAlbum(idAlbum);
                    albumRepository.deleteAlbumById(idAlbum);
                }
            }
        }
        return coutSuccess;
    }

    @Transactional
    @Override
    public int updateAlbumTeacher(UserPrincipal principal, Long idClassLogin, UpdateAlbumTeacherRequest updateAlbumTeacherRequest) throws IOException {
        int coutSuccess = 0;
        Long idSchool = principal.getIdSchoolLogin();
        Album albumOptional = albumRepository.findById(updateAlbumTeacherRequest.getId()).orElseThrow();
        Album album = modelMapper.map(updateAlbumTeacherRequest, Album.class);
        Long idClass = principal.getIdClassLogin();
        MaClass maClass = maClassRepository.findById(idClass).orElseThrow();
        MaUser maUser = maUserRepository.findById(albumOptional.getIdCreated()).orElseThrow();
        if (updateAlbumTeacherRequest.getIdPictureList() == null && updateAlbumTeacherRequest.getFileList() != null) {
            String uploadInAlbum = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\";
            List<ListPicture> listPictureList = new ArrayList<>();
            for (MultipartFile file : updateAlbumTeacherRequest.getFileList()) {
                String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + file.getOriginalFilename());
                String fileNameResize = "thumbnail_" + fileName;
                Path fileNameAndPath = Paths.get(uploadInAlbum, fileName);
                Path fileNameAndPathThumbnail = Paths.get(uploadInAlbum, fileNameResize);
                ListPicture listPicture = new ListPicture();
                listPicture.setAlbum(album);
                listPicture.setCreatedDate(LocalDateTime.now());
                listPicture.setIsApproved(principal.getSchoolConfig().isAlbum());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(file, idSchool, UploadDownloadConstant.ALBUM);
                listPicture.setUrlPicture(handleFileResponse.getUrlWeb());
                listPicture.setUrlLocal(handleFileResponse.getUrlLocal());
                listPictureList.add(listPicture);
                Files.write(fileNameAndPath, file.getBytes());
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                int scaledHeightNewInt = calculateHeightNew(uploadInAlbum + fileName);
                resize(uploadInAlbum + fileName, uploadInAlbum + fileNameResize, scaledWidth, scaledHeightNewInt);
                Files.write(fileNameAndPath, file.getBytes());
                album.setAlistPictureList(listPictureList);
                if (listPictureList.size() > 0) {
                    album.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
                }
            }
            album.setAlbumName(updateAlbumTeacherRequest.getAlbumName());
            album.setAlbumDescription(updateAlbumTeacherRequest.getAlbumDescription());
        }
        if (updateAlbumTeacherRequest.getIdPictureList() != null && updateAlbumTeacherRequest.getFileList() == null) {
            for (Long idPicture : updateAlbumTeacherRequest.getIdPictureList()) {
                Optional<ListPicture> listPictureOptional = listPictureRepository.findById(idPicture);
                if (listPictureOptional.isPresent()) {
                    ListPicture listPicture = listPictureOptional.get();
                    int coutNumber = albumOptional.getAlistPictureList().size();
                    Long idAlbum = album.getId();
                    if (listPicture.getIdCreated().equals(principal.getId())) {
                        listPictureRepository.deletePictureById(listPicture.getId());
                        String urlLocal = listPicture.getUrlLocal();
                        HandleFileUtils.deletePictureInFolder(urlLocal);
                        coutSuccess++;
                    }
                }
            }
            album.setAlbumName(updateAlbumTeacherRequest.getAlbumName());
            album.setAlbumDescription(updateAlbumTeacherRequest.getAlbumDescription());
        }
        if (updateAlbumTeacherRequest.getIdPictureList() != null && updateAlbumTeacherRequest.getFileList() != null) {
            for (Long idPicture : updateAlbumTeacherRequest.getIdPictureList()) {
                Optional<ListPicture> listPictureOptional = listPictureRepository.findById(idPicture);
                if (listPictureOptional.isPresent()) {
                    ListPicture listPicture = listPictureOptional.get();
                    if (listPicture.getIdCreated().equals(principal.getId())) {
                        listPictureRepository.deletePictureById(listPicture.getId());
                        String urlLocal = listPicture.getUrlLocal();
                        HandleFileUtils.deletePictureInFolder(urlLocal);
                        coutSuccess++;
                    }
                }
            }
            String uploadInAlbum = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\";
            List<ListPicture> listPictureList = new ArrayList<>();
            int pictureNumber = 0;
            int pictureApprovedNumber = 0;
            for (MultipartFile file : updateAlbumTeacherRequest.getFileList()) {
                String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + file.getOriginalFilename());
                String fileNameResize = "thumbnail_" + fileName;
                Path fileNameAndPathThumbnail = Paths.get(uploadInAlbum, fileNameResize);
                ListPicture listPicture = new ListPicture();
                listPicture.setAlbum(album);
                listPicture.setCreatedDate(LocalDateTime.now());
                listPicture.setIsApproved(principal.getSchoolConfig().isAlbum());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(file, idSchool, UploadDownloadConstant.ALBUM);
                listPicture.setUrlPicture(handleFileResponse.getUrlWeb());
                listPicture.setUrlLocal(handleFileResponse.getUrlLocal());
                listPictureList.add(listPicture);
                pictureNumber++;
                if (listPicture.getIsApproved()) {
                    pictureApprovedNumber++;
                }
                album.setAlistPictureList(listPictureList);
                album.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
            }
            album.setAlbumName(updateAlbumTeacherRequest.getAlbumName());
            album.setAlbumDescription(updateAlbumTeacherRequest.getAlbumDescription());
        }
        album.setAlbumName(updateAlbumTeacherRequest.getAlbumName());
        album.setAlbumDescription(updateAlbumTeacherRequest.getAlbumDescription());
        album.setMaClass(maClass);
        album.setIdSchool(idSchool);
        album.setUrlPictureFirst(albumOptional.getAlistPictureList().get(0).getUrlPicture());
        album.setAlbumType(albumOptional.getAlbumType());
        album.setIdCreated(albumOptional.getIdCreated());
        album.setCreatedBy(maUser.getFullName());
        album.setCreatedDate(albumOptional.getCreatedDate());
        albumRepository.save(album);
        return coutSuccess;
    }

    public int calculateHeightNew(String pathFileName) throws IOException {
        float height = 0f;
        float width = 0f;
        InputStream inputStream = new FileInputStream(pathFileName);
        try (ImageInputStream in = ImageIO.createImageInputStream(inputStream)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    height = reader.getHeight(0);
                    width = reader.getWidth(0);
                } finally {
                    reader.dispose();
                }
            }
        }
        float rate = (float) height / width;
        float scaledHeightNew = (float) Math.ceil(rate * scaledWidth);
        return (int) scaledHeightNew;
    }

    /**
     * Hàm chỉnh kích thước ảnh chiều rộng chiều cao
     *
     * @param inputImagePath
     * @param outputImagePath
     * @param scaledWidth
     * @param scaledHeight
     * @throws IOException
     */
    public void resize(
            String inputImagePath, String outputImagePath, int scaledWidth,
            int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /**
     * Hàm chỉnh kích thước ảnh theo phần trăm0, chưa sử dụng
     * ................................................................................................................................................................................................................................................................................................................................................................................................................
     *
     * @param inputImagePath
     * @param outputImagePath
     * @param percent
     * @throws IOException
     */
    public void resize(String inputImagePath, String outputImagePath, double percent)
            throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
}
