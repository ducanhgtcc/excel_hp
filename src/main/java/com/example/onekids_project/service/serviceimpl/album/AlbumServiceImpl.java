package com.example.onekids_project.service.serviceimpl.album;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.album.*;
import com.example.onekids_project.response.album.AlbumDetailResponse;
import com.example.onekids_project.response.album.AlbumNewResponse;
import com.example.onekids_project.response.album.ListAlbumNewResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.album.AlbumService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class AlbumServiceImpl implements AlbumService {

    int yearCurrent = LocalDate.now().getYear();
    int scaledWidth = 512;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private ExAlbumKidsRepository exAlbumKidsRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private ListPictureRepository listPictureRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Transactional
    @Override
    public boolean createAlbum(UserPrincipal principal, AlbumCreateRequest request) throws IOException, FirebaseMessagingException {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList;
        Album album = modelMapper.map(request, Album.class);
        album.setIdSchool(idSchool);
        album.setCreatedBy(principal.getFullName());
        if (request.getIdClass() == 0) {
            album.setAlbumType(AppConstant.ALBUMSCHOOL);
            kidsList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(idSchool, KidsStatusConstant.STUDYING);
        } else {
            album.setAlbumType(AppConstant.CLASS);
            album.setMaClass(maClassRepository.findByIdAndDelActiveTrue(request.getIdClass()).orElseThrow());
            kidsList = kidsRepository.findByIdSchoolAndMaClassIdAndKidStatusAndDelActiveTrue(idSchool, request.getIdClass(), KidsStatusConstant.STUDYING);
        }
        Album albumSaved = albumRepository.save(album);
        for (MultipartFile multipartFile : request.getMultipartFileList()) {
            ListPicture picture = new ListPicture();
            picture.setAlbum(albumSaved);
            picture.setIdCreated(principal.getId());
            picture.setIsApproved(principal.getSchoolConfig().isAlbum());
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(multipartFile, idSchool, UploadDownloadConstant.ALBUM);
            picture.setUrlPicture(handleFileResponse.getUrlWeb());
            picture.setUrlLocal(handleFileResponse.getUrlLocal());
            listPictureRepository.save(picture);
            albumSaved.setUrlPictureFirst(picture.getUrlPicture());
            albumRepository.save(albumSaved);
        }
        for (Kids x : kidsList) {
            ExAlbumKids exAlbumKids = new ExAlbumKids();
            exAlbumKids.setKids(x);
            exAlbumKids.setCreatedBy(principal.getFullName());
            exAlbumKids.setAlbum(albumSaved);
            exAlbumKidsRepository.save(exAlbumKids);
            if (principal.getSchoolConfig().isAlbum()) {
                firebaseFunctionService.sendParentByPlus(37L, x, FirebaseConstant.CATEGORY_ALBUM, request.getAlbumName());
            }
        }
        return true;
    }


    @Override
    @Transactional
    public boolean updateAlbum(Long idSchool, UserPrincipal principal, UpdateAlbumRequest updateAlbumRequest) throws
            IOException {
        Optional<Album> albumOptional = albumRepository.findById(updateAlbumRequest.getId());
        if (albumOptional.isEmpty()) {
            return false;
        }
        Album album = modelMapper.map(updateAlbumRequest, Album.class);
        Album oldAlbum = albumOptional.get();
        modelMapper.map(updateAlbumRequest, oldAlbum);
        if (updateAlbumRequest.getFileList() != null) {
            int monthCurrent = LocalDate.now().getMonthValue();
            String uploadInAlbum = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\";
            List<ListPicture> listPictureList = new ArrayList<>();
            int pictureNumber = 0;
            /**
             * Khai báo list các url ảnh đã được uploaded
             */
            List<String> urlListUploaded = new ArrayList<>();
            for (MultipartFile file : updateAlbumRequest.getFileList()) {
                /**
                 * Khai báo file name,file thumbnail
                 */
                String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + file.getOriginalFilename());
                String fileNameResize = "thumbnail_" + fileName;
                Path fileNameAndPath = Paths.get(uploadInAlbum, fileName);
                Path fileNameAndPathThumbnail = Paths.get(uploadInAlbum, fileNameResize);
                ListPicture listPicture = new ListPicture();
                listPicture.setAlbum(oldAlbum);
                listPicture.setIsApproved(AppConstant.APP_TRUE);
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureAlbumSaved(file, idSchool, UploadDownloadConstant.ALBUM);
                listPicture.setUrlPicture(handleFileResponse.getUrlWeb());
                listPicture.setUrlLocal(handleFileResponse.getUrlLocal());
                listPictureList.add(listPicture);
                try {
                    pictureNumber++;
                    if ((oldAlbum.getAlistPictureList().size() + pictureNumber) > AppConstant.NUMBER_PICTURE_IN_ALBUM) {
                        return false;
                    }

                    urlListUploaded.add(AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\" + fileName);
                    /**
                     * Ghi file chưa resize vào thư mục
                     */
                    Files.write(fileNameAndPath, file.getBytes());
                    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                    if (extension.equals("gif")) {
                        Files.write(fileNameAndPathThumbnail, file.getBytes());
                    } else {
                        int scaledHeightNewInt = calculateHeightNew(uploadInAlbum + fileName);
                        resize(uploadInAlbum + fileName, uploadInAlbum + fileNameResize, scaledWidth, scaledHeightNewInt);
                        Files.write(fileNameAndPath, file.getBytes());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /**
             * Set giá trị listPictureList cho album
             */
            oldAlbum.setAlistPictureList(listPictureList);
            if (CollectionUtils.isEmpty(listPictureList)) {
                oldAlbum.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
            }
        }

        // update truowngf sang lop
        if (oldAlbum.getMaClass() == null && updateAlbumRequest.getIdClass() != 0) {
            oldAlbum.setAlbumType(AppConstant.CLASS);
            oldAlbum.setMaClass(maClassRepository.findByIdMaClass(idSchool, updateAlbumRequest.getIdClass()).get());
            List<Kids> albumSchoollist1 = kidsRepository.findAllKidsA(idSchool); // tất cả học sinh -- list 1
            List<Kids> albumClasslist2 = kidsRepository.findAlbumClass(idSchool, updateAlbumRequest.getIdClass()); // danh sách học sinh theo idclass -- list 2
            List<Kids> kidsList3 = (List<Kids>) CollectionUtils.intersection(albumClasslist2, albumSchoollist1);
            // giao của list (1) và (2)
            exAlbumKidsRepository.deleteExAlbumByIdAlbum(updateAlbumRequest.getId());
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
                oldAlbum.setExAlbumKidsListA(exAlbumKidsList);
            }
        } else if (oldAlbum.getMaClass() != null && updateAlbumRequest.getIdClass() == 0) {
            // update  lớp sang trường
            oldAlbum.setAlbumType(AppConstant.ALBUMSCHOOL);
            List<Kids> kidslist1 = kidsRepository.findAllKidsAlbumClass(idSchool, oldAlbum.getMaClass().getId());
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
                oldAlbum.setExAlbumKidsListA(exAlbumKidsList);
            }
        } else if (oldAlbum.getMaClass() != null && updateAlbumRequest.getIdClass() != 0) {
            oldAlbum.setAlbumType(AppConstant.CLASS);
            oldAlbum.setMaClass(maClassRepository.findByIdMaClass(idSchool, updateAlbumRequest.getIdClass()).get());
            // update lớp sang lớp
            // xóa kids theo idAlum
            exAlbumKidsRepository.deleteExAlbumByIdAlbum(updateAlbumRequest.getId());
            //  thêm mới kids vào ex_album_kids
            List<MaClass> maClassList = maClassRepository.findAllMaClassAlbum(idSchool);
            List<Kids> kidClassList = kidsRepository.findAllKidsAlbumClass(idSchool, updateAlbumRequest.getIdClass());
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
                oldAlbum.setExAlbumKidsListA(exAlbumKidsList);
            }
        }
        if (updateAlbumRequest.getIdClass() == 0) {
            oldAlbum.setMaClass(null);
        }
        albumRepository.save(oldAlbum);
        return true;
    }

    @Override
    public AlbumDetailResponse findByIdAlbum(UserPrincipal principal, Long idSchool, Long idAlbum) {
        Optional<Album> albumOptional = albumRepository.findById(idAlbum);
        if (albumOptional.isEmpty()) {
            return null;
        }
        Album album = albumOptional.get();
        AlbumDetailResponse albumDetailResponse = modelMapper.map(album, AlbumDetailResponse.class);
        MaUser maUser = maUserRepository.findById(album.getIdCreated()).get();
        albumDetailResponse.setCreatedBy(maUser.getFullName());
        albumDetailResponse.setPictureApprovedNumber((int) album.getAlistPictureList().stream().filter(x -> x.getIsApproved() == AppConstant.APP_TRUE).count());
        albumDetailResponse.setPictureNumber(album.getAlistPictureList().size());
        if (album.getAlbumType().equalsIgnoreCase(AppConstant.CLASS)) {
            Long idClassInAlbum = album.getMaClass().getId();
            albumDetailResponse.setIdClass(idClassInAlbum);
        } else {
            albumDetailResponse.setIdClass(0L);
        }
        return albumDetailResponse;
    }

    @Override
    public Long countAllAlbum(UserPrincipal principal, Long idSchool, SearchAlbumRequest searchAlbumRequest) {
        return albumRepository.countAllAlbum(idSchool, searchAlbumRequest);
    }

    @Override
    @Transactional
    public boolean deletePicture(UserPrincipal principal, Long idSchool, Long idPicture) {
        Optional<ListPicture> listPicture = listPictureRepository.findById(idPicture);
        int numberPictureInAlbum = listPicture.get().getAlbum().getAlistPictureList().size();
        listPicture.get().getAlbum().getId();
        long idAlbum = listPicture.get().getAlbum().getId();
        try {
            String urlLocal = listPicture.get().getUrlLocal();
            String urlLocalThmbnail = listPicture.get().getUrlLocal().replace("\\" + UploadDownloadConstant.ALBUM + "\\", "\\" + UploadDownloadConstant.ALBUM + "\\thumbnail_");
            File file = new File(urlLocal);
            File fileThumbnail = new File(urlLocalThmbnail);
            FileUtils.touch(file);
            File fileToDelete = FileUtils.getFile(file);
            boolean check = FileUtils.deleteQuietly(fileToDelete);
            FileUtils.touch(fileThumbnail);
            File fileToDeleteThumbnail = FileUtils.getFile(fileThumbnail);
            boolean checkThumbnail = FileUtils.deleteQuietly(fileToDeleteThumbnail);
            Album album = albumRepository.findByAlistPictureListId(idPicture);
            album.getAlistPictureList().remove(listPicture.get());
            listPictureRepository.deleteById(idPicture);
            HandleFileUtils.deletePictureInFolder(urlLocal);
            if (!org.springframework.util.CollectionUtils.isEmpty(album.getAlistPictureList())) {
                album.setUrlPictureFirst(album.getAlistPictureList().get(0).getUrlPicture());
                albumRepository.save(album);
            }
            if (numberPictureInAlbum == 1) {
                exAlbumKidsRepository.deleteExAlbumByIdAlbum(idAlbum);
                albumRepository.deleteAlbumById(idAlbum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAlbum(UserPrincipal principal, Long idSchool, Long idAlbum) {
        Optional<Album> albumOptional = albumRepository.findById(idAlbum);
        if (albumOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy Album");
        }
        Album album = albumOptional.get();
        if (!CollectionUtils.isEmpty(album.getAlistPictureList())) {
            album.getAlistPictureList().forEach(listPicture -> {
                        try {
                            File file = new File(listPicture.getUrlLocal());
                            file.delete();
                            listPictureRepository.deletePictureById(listPicture.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        if (!CollectionUtils.isEmpty(album.getExAlbumKidsListA())) {
            album.getExAlbumKidsListA().forEach(x -> {
                        try {
                            exAlbumKidsRepository.deleteExAlbumByIdAlbum(x.getAlbum().getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        albumRepository.deleteAlbumById(idAlbum);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteMultiAlbum(UserPrincipal principal, Long idSchool, List<Long> idAlbumList) {
        for (Long idAlbum : idAlbumList) {
            Album album = albumRepository.findById(idAlbum).orElseThrow();
            if (!CollectionUtils.isEmpty(album.getAlistPictureList())) {
                album.getAlistPictureList().forEach(listPicture -> {
                    listPictureRepository.deletePictureById(listPicture.getId());
                });
            }
            if (!CollectionUtils.isEmpty(album.getExAlbumKidsListA())) {
                album.getExAlbumKidsListA().forEach(x -> {
                    exAlbumKidsRepository.deleteExAlbumByIdAlbum(x.getAlbum().getId());
                });
            }
            albumRepository.deleteAlbumById(idAlbum);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updatePictureApprove(Long id, Long idAlbum, Boolean checkApprove, UserPrincipal principal) {
        Optional<ListPicture> listPictureOptional = listPictureRepository.findById(id);
        if (listPictureOptional.isEmpty()) {
            return false;
        }
        int idAlbum1 = Math.toIntExact(listPictureOptional.get().getAlbum().getId());
        List<ExAlbumKids> exAlbumKidsList = exAlbumKidsRepository.findByidAlbum(idAlbum1);
        if (exAlbumKidsList.isEmpty()) {
            return false;
        }
        exAlbumKidsList.forEach(x -> {
            x.setStatusUnread(AppConstant.APP_FALSE);
        });
        listPictureOptional.get().getAlbum().getId();
        ListPicture listPicture = listPictureOptional.get();
        listPicture.setIsApproved(checkApprove);
        listPicture.getAlbum().getId();
        listPictureRepository.save(listPicture);
        return true;
    }

    private void sendFireBaseOnePicture(Long id, UserPrincipal principal) throws FirebaseMessagingException {
        Optional<Album> albums = albumRepository.findByIdAndDelActiveTrue(id);
        List<ListPicture> listPictureCheck = new ArrayList<>();
        if (albums.isPresent()) {
            List<ListPicture> listPicture = albums.get().getAlistPictureList();
            listPicture.forEach(x -> {
                if (x.getIsApproved()) {
                    listPictureCheck.add(x);
                }
            });
        }

    }

    @Override
    @Transactional
    public boolean updateAllPictureApprove(Long idAlbum, UserPrincipal principal) throws FirebaseMessagingException {
        List<ListPicture> listPictures = new ArrayList<>();
        final ListPicture[] listPicture = {new ListPicture()};
        listPictureRepository.findByAlbum_IdAndDelActiveTrue(idAlbum).forEach(item -> {
            if (item.getIsApproved()) {
                listPictures.add(item);
            }
            item.setIsApproved(AppConstant.APP_TRUE);
            item = listPictureRepository.save(item);
            listPicture[0] = item;
        });
        return true;
    }

    @Transactional
    @Override
    public boolean updateMultiApproveAlbum(UserPrincipal principal, List<Long> idAlbumList) throws FirebaseMessagingException {
        for (Long idAlbum : idAlbumList) {
            Album album = albumRepository.findByIdAndIdSchoolAndDelActiveTrue(idAlbum, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException(("not found album by id")));
            album.getAlistPictureList().forEach(item -> item.setIsApproved(AppConstant.APP_TRUE));
            albumRepository.save(album);
        }
        return true;
    }

    @Override
    public boolean updateMultiUnApproveAlbum(UserPrincipal principal, Long idSchoolLogin, List<Long> idAlbumList, Boolean status) {
        for (Long idAlbum : idAlbumList) {
            Album album = albumRepository.findById(idAlbum).orElseThrow(() -> new NotFoundException(("not found album by id")));
            album.getAlistPictureList().forEach(item -> {
                item.setIsApproved(AppConstant.APP_FALSE);
            });
            albumRepository.save(album);
        }
        return true;
    }

    @Override
    public ListAlbumNewResponse searchAlbumNew(UserPrincipal principal, SearchAlbumNewRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        ListAlbumNewResponse response = new ListAlbumNewResponse();
        List<Album> albumList = albumRepository.findalBumNew(idSchool, request);
        List<AlbumNewResponse> albumNewResponseList = new ArrayList<>();
        albumList.forEach(album -> {
            AlbumNewResponse albumNewResponse = modelMapper.map(album, AlbumNewResponse.class);
            if (!CollectionUtils.isEmpty(album.getAlistPictureList())) {
                for (ListPicture listPicture : album.getAlistPictureList()) {
                    if (listPicture.getIsApproved()) {
                        albumNewResponse.setAlbumView(AppConstant.APP_TRUE);
                        albumNewResponse.setAlbumApprovalDefault(AppConstant.APP_TRUE);
                    } else {
                        if (albumNewResponse.getAlbumNew() == null) {
                            albumNewResponse.setAlbumNew(AppConstant.APP_TRUE);
                        }
                    }
                }
                album.setUrlPictureFirst(album.getAlistPictureList().get(0).getUrlPicture());
                albumNewResponse.setPictureNumber(album.getAlistPictureList().size());
            }
            albumNewResponseList.add(albumNewResponse);
        });
        long total = albumRepository.countSearchAlbum(idSchool, request);
        response.setTotal(total);
        response.setResponseList(albumNewResponseList);
        return response;
    }

    @Override
    public String downloadAlbum(UserPrincipal principal, Long id) throws IOException {
        Album album = albumRepository.findById(id).orElseThrow();
        String filename = album.getAlbumName() + ".zip";
        // loại bỏ ký tự đặc biệt và dấu
        String filename1 = filename.replaceAll("[ / : * ? \" < > |]", "");
        String filename2 = ConvertData.removeAccentFinal(filename1);
        if (album.getAlistPictureList().size() > 0) {
            List<String> urlPictureList = album.getAlistPictureList().stream().map(ListPicture::getUrlLocal).collect(Collectors.toList());
            FileOutputStream fos = new FileOutputStream(AppConstant.UPLOAD_IN_ALBUM + UploadDownloadConstant.TEMP_DATA + "/" + filename2);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : urlPictureList) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
        }

        return AppConstant.URL_DEFAULT + UploadDownloadConstant.TEMP_DATA + "/" + filename2;
    }


    /**
     * Tinh gia tri height moi sau khi resize
     *
     * @param pathFileName
     * @return
     * @throws IOException
     */
    public int calculateHeightNew(String pathFileName) throws IOException {
//        //converting file format
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
        float rate = height / width;
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
    public void resize(String inputImagePath,
                       String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
}
