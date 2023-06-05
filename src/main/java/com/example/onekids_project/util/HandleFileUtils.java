package com.example.onekids_project.util;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.integration.util.VNCharacterUtils;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.response.common.HandleFileResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HandleFileUtils {

    private static SysInforRepository sysInforRepository;

    @Autowired
    public HandleFileUtils(SysInforRepository sysInforRepository) {
        HandleFileUtils.sysInforRepository = sysInforRepository;
    }

    public static SysInforRepository getBeanSysInfor() {
        return sysInforRepository;
    }


    /**
     * create picture and file
     *
     * @param rootDir
     * @param multipartFile
     * @param fileName
     * @param scaledWidth
     * @throws IOException
     */
    public static void createFilePictureToDirectory(String rootDir, MultipartFile multipartFile, String fileName, int scaledWidth) throws IOException {
        checkMaxFile(multipartFile);
        fileName = VNCharacterUtils.removeAccent(fileName);
        String fileNameResize = UploadDownloadConstant.THUMBNAIL + fileName;
        Path fileNameAndPath = Paths.get(rootDir, fileName);
        Path fileNameAndPathThumbnail = Paths.get(rootDir, fileNameResize);
        Files.write((fileNameAndPath), multipartFile.getBytes());
        String fileExtension = Objects.requireNonNull(FilenameUtils.getExtension(multipartFile.getOriginalFilename())).toLowerCase();
        List<String> stringList = UploadDownloadConstant.EXTENDSION_PICTURE.stream().filter(y -> y.equals(fileExtension)).collect(Collectors.toList());
        //tạo thumbnail cho ảnh
        if (!CollectionUtils.isEmpty(stringList)) {
            //là ảnh gif thì không nén ảnh
            if (fileExtension.equals(UploadDownloadConstant.EXTENDSION_PICTURE.get(3))) {
                Files.write(fileNameAndPathThumbnail, multipartFile.getBytes());
            } else {
                //không phải là ảnh gif thì nén ảnh
                int scaledHeightNewInt = calculateHeightNew(rootDir + fileName);
                resize(rootDir + fileName, rootDir + fileNameResize, scaledWidth, scaledHeightNewInt);
                Files.write(fileNameAndPath, multipartFile.getBytes());
            }
        }
    }


    /**
     * create picture
     *
     * @param rootDir
     * @param multipartFile
     * @param fileName
     * @param scaledWidth
     * @throws IOException
     */
    public static void createPictureToDirectory(String rootDir, MultipartFile multipartFile, String fileName, int scaledWidth) throws IOException {
        checkMaxFile(multipartFile);
        fileName = VNCharacterUtils.removeAccent(fileName);
        String fileExtension = Objects.requireNonNull(FilenameUtils.getExtension(multipartFile.getOriginalFilename())).toLowerCase();
        long count = UploadDownloadConstant.EXTENDSION_PICTURE.stream().filter(y -> y.equals(fileExtension)).count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ảnh không đúng định dạng: " + UploadDownloadConstant.EXTENDSION_PICTURE);
        }
        String fileNameResize = UploadDownloadConstant.THUMBNAIL + fileName;
        Path fileNameAndPath = Paths.get(rootDir, fileName);
        Path fileNameAndPathThumbnail = Paths.get(rootDir, fileNameResize);
        Files.write((fileNameAndPath), multipartFile.getBytes());
        //tảo ảnh thumbnail:
        //nếu là ảnh gif thì không nén ảnh
        if (fileExtension.equals(UploadDownloadConstant.EXTENDSION_PICTURE.get(3))) {
            Files.write(fileNameAndPathThumbnail, multipartFile.getBytes());
        } else {
            //không phải là ảnh gif thì nén ảnh
            int scaledHeightNewInt = calculateHeightNew(rootDir + fileName);
            resize(rootDir + fileName, rootDir + fileNameResize, scaledWidth, scaledHeightNewInt);
            Files.write(fileNameAndPath, multipartFile.getBytes());
        }
    }

    /**
     * create file
     *
     * @param rootDir
     * @param multipartFile
     * @param fileName
     * @throws IOException
     */
    public static void createFileToDirectory(String rootDir, MultipartFile multipartFile, String fileName) throws IOException {
        checkMaxFile(multipartFile);
        fileName = VNCharacterUtils.removeAccent(fileName);
        String fileNameRemoveSpace = removeSpace(fileName);
        Path fileNameAndPath = Paths.get(rootDir, fileNameRemoveSpace);
        Files.write(fileNameAndPath, multipartFile.getBytes());
        String fileExtension = Objects.requireNonNull(FilenameUtils.getExtension(multipartFile.getOriginalFilename())).toLowerCase();
        List<String> stringList = UploadDownloadConstant.EXTENDSION_PICTURE.stream().filter(y -> y.equals(fileExtension)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(stringList)) {
            Files.write(fileNameAndPath, multipartFile.getBytes());
        }
    }


    /**
     * Xóa file trong thư mục
     *
     * @param urlLocal
     * @param urlLocalThumbnail
     */
    public static void deleteFilePictureInDirectory(String urlLocal, String urlLocalThumbnail) {
        File file = new File(urlLocal);
        boolean checkDelete1 = file.delete();

        File fileThumbnail = new File(urlLocalThumbnail);
        boolean checkDelete2 = fileThumbnail.delete();
    }

    /**
     * delete picture in folder
     *
     * @param urlLocal
     */
    public static void deletePictureInFolder(String urlLocal) {
        if (StringUtils.isNotBlank(urlLocal)) {
            File file = new File(urlLocal);
            file.delete();

            String nameFile = urlLocal.substring(urlLocal.lastIndexOf("\\") + 1);
            String urlLocalThumbnail = urlLocal.replaceAll(nameFile, "thumbnail_" + nameFile);
            File fileThumbnail = new File(urlLocalThumbnail);
            fileThumbnail.delete();
        }
    }

    /**
     * delete file in folder
     *
     * @param urlLocal
     */
    public static void deleteFileInFolder(String urlLocal) {
        if (StringUtils.isNotBlank(urlLocal)) {
            File file = new File(urlLocal);
            file.delete();
        }
    }

    /**
     * delete file or picture
     *
     * @param urlLocal
     */
    public static void deleteFileOrPictureInFolder(String urlLocal) {
        if (StringUtils.isNotBlank(urlLocal)) {
            File file = new File(urlLocal);
            file.delete();

            String fileExtends = urlLocal.substring(urlLocal.lastIndexOf(".") + 1);
            List<String> pictureExtendsList = UploadDownloadConstant.EXTENDSION_PICTURE_NO_COMPRESSION;
            List<String> stringList = pictureExtendsList.stream().filter(x -> x.equals(fileExtends)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(stringList)) {
                String nameFile = urlLocal.substring(urlLocal.lastIndexOf("\\") + 1);
                String urlLocalThumbnail = urlLocal.replaceAll(nameFile, "thumbnail_" + nameFile);
                File fileThumbnail = new File(urlLocalThumbnail);
                fileThumbnail.delete();
            }

        }
    }

    /**
     * get fileName
     *
     * @param idSchool
     * @param multipartFile
     * @return
     */
    public static String getFileNameOfSchool(Long idSchool, MultipartFile multipartFile) {
        String fileName = System.currentTimeMillis() + "_" + idSchool + "_" + VNCharacterUtils.removeAccent(multipartFile.getOriginalFilename());
        return HandleFileUtils.removeSpace(fileName);
    }

    /**
     * create url
     *
     * @param idSchool
     * @param type
     * @param folder
     * @return
     */
    public static String getUrl(Long idSchool, String type, String folder) {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        int yearCurrent = LocalDate.now().getYear();
        int monthCurrent = LocalDate.now().getMonthValue();
        String url = "";
        switch (type) {
            case UrlFileConstant.URL_LOCAL:
//                url = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + folder + "\\\\";
                url = sysInfor.getLocalUrl() + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + folder + "\\\\";
                break;
            case UrlFileConstant.URL_WEB:
//                url = AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + folder + "/";
                url = sysInfor.getWebUrl() + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + folder + "/";
                break;
        }

        return url;
    }

    /**
     * create url
     *
     * @param idSchool
     * @param type
     * @param folder
     * @return
     */
    public static String getUrlAlbum(Long idSchool, String type, String folder) {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        int yearCurrent = LocalDate.now().getYear();
        int monthCurrent = LocalDate.now().getMonthValue();
        String url = "";
        switch (type) {
//            case UrlFileConstant.URL_FOLDER:
//                url = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + folder + "\\";
//                break;
            case UrlFileConstant.URL_LOCAL:
//                url = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + folder + "\\\\";
                url = sysInfor.getLocalUrl() + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + folder + "\\\\";
                break;
            case UrlFileConstant.URL_WEB:
//                url = AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + folder + "/";
                url = sysInfor.getWebUrl() + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + folder + "/";
                break;
        }
        return url;
    }

    public static String getUrlAlbumApiTest(Long idSchool, String type, String folder) {
//        SysInfor sysInfor = HandleFileUtils.getBeanSysInfor().findFirstByDelActiveTrue().orElseThrow();
        int yearCurrent = LocalDate.now().getYear();
        int monthCurrent = LocalDate.now().getMonthValue();
        String url = "";
        switch (type) {
//            case UrlFileConstant.URL_FOLDER:
//                url = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + folder + "\\";
//                break;
            case UrlFileConstant.URL_LOCAL:
                url = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + folder + "\\\\";
//                url = sysInfor.getLocalUrl() + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + folder + "\\\\";
                break;
            case UrlFileConstant.URL_WEB:
                url = AppConstant.URL_DEFAULTTEST + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + folder + "/";
//                url = sysInfor.getWebUrl() + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + folder + "/";
                break;
        }
        return url;
    }

    /**
     * lưu vào folder trong 1 trường
     *
     * @param idSchool
     * @param type
     * @param folder
     * @return
     */
    public static String getUrlNoTime(Long idSchool, String type, String folder) {
        SysInfor sysInfor = HandleFileUtils.getBeanSysInfor().findFirstByDelActiveTrue().orElseThrow();
        String url = "";
        switch (type) {
//            case UrlFileConstant.URL_FOLDER:
//                url = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + folder + "\\";
//                break;
            case UrlFileConstant.URL_LOCAL:
                url = sysInfor.getLocalUrl() + idSchool + "\\\\" + folder + "\\\\";
                break;
            case UrlFileConstant.URL_WEB:
//                url = AppConstant.URL_DEFAULT + idSchool + "/" + folder + "/";
                url = sysInfor.getWebUrl() + idSchool + "/" + folder + "/";
                break;
        }
        return url;
    }

    /**
     * hàm xủ lý lưu file, ảnh cho trường
     *
     * @param multipartFile
     * @param idSchool
     * @return
     * @throws IOException
     */
    public static HandleFileResponse getUrlFieldOrPictureSaved(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
        HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

        String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    /**
     * hàm xủ lý lưu file, ảnh ko quan tâm thời gian
     *
     * @param multipartFile
     * @param idSchool
     * @return
     * @throws IOException
     */
    public static HandleFileResponse getUrlFieldOrPictureSavedNoTime(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
        HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

        String urlWeb = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    /**
     * hàm xủ lý lưu file cho trường
     *
     * @param multipartFile
     * @param idSchool
     * @return
     * @throws IOException
     */
    public static HandleFileResponse getUrlFileSaved(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
        HandleFileUtils.createFileToDirectory(urlFolder, multipartFile, fileName);

        String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    /**
     * save file of json
     *
     * @param multipartFile
     * @param idSchool
     * @param folder
     * @return
     * @throws IOException
     */

    public static HandleFileResponse getUrlFileJsonSavedJson(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String nameDateTime = ConvertData.getDateMillisecond();
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String name = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
//        String fileName = name + "." + fileExtension;
        String fileName = name + "_" + nameDateTime + "_0." + fileExtension;
        HandleFileUtils.createFileToDirectory(urlFolder, multipartFile, fileName);

        String urlWeb = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(fileName);
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    /**
     * hàm xủ lý lưu ảnh cho trường
     *
     * @param multipartFile
     * @param idSchool
     * @return
     * @throws IOException
     */
    public static HandleFileResponse getUrlPictureSaved(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
        HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, sysInfor.getWidthOther());

        String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    /**
     * hàm xủ lý lưu ảnh cho trường cho album
     *
     * @param multipartFile
     * @param idSchool
     * @return
     * @throws IOException
     */
    public static HandleFileResponse getUrlPictureAlbumSaved(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrlAlbum(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
        HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, sysInfor.getWidthAlbum());

        String urlWeb = HandleFileUtils.getUrlAlbum(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrlAlbum(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    public static HandleFileResponse getUrlPictureAlbumSavedApiTest(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
//        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrlAlbumApiTest(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
        HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, 512);

        String urlWeb = HandleFileUtils.getUrlAlbumApiTest(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrlAlbumApiTest(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    /**
     * lưu ảnh cho trường ko phụ thuộc thời gian
     *
     * @param multipartFile
     * @param idSchool
     * @param folder
     * @return
     * @throws IOException
     */
    public static HandleFileResponse getUrlPictureSavedNoTime(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = HandleFileUtils.getFileNameOfSchool(SystemConstant.ID_SYSTEM, multipartFile);
        HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

        String urlWeb = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrlNoTime(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }


    public static HandleFileResponse getUrlPictureIdentitySaved(MultipartFile multipartFile, Long idSchool, String folder) throws IOException {
        HandleFileResponse model = new HandleFileResponse();
        String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder);
        String fileName = multipartFile.getOriginalFilename();
        HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

        String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, folder) + fileName;
        String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, folder) + fileName;
        model.setName(multipartFile.getOriginalFilename());
        model.setUrlWeb(urlWeb);
        model.setUrlLocal(urlLocal);
        return model;
    }

    public static String getBase64EncodedImage(String imageURL) throws IOException {
        java.net.URL url = new java.net.URL(imageURL);
        InputStream is = url.openStream();
        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
        String append = "data:image/png;base64,";
        return append + Base64.encodeBase64String(bytes);
    }

    /**
     * Hàm chuẩn hóa chuỗi
     *
     * @param str
     * @return
     */
    public static String removeSpace(String str) {
        return str.replaceAll(" ", "");
    }

    /**
     * Tinh gia tri height moi sau khi resize
     *
     * @param pathFileName
     * @return
     * @throws IOException
     */
    private static int calculateHeightNew(String pathFileName) throws IOException {
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
        float scaledHeightNew = (float) Math.ceil(rate * 512);
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
    private static void resize(String inputImagePath,
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

    /**
     * check kích thước file
     *
     * @param multipartFile
     */
    private static void checkMaxFile(MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        float sizeMB = (float) size / (1024 * 1024);
        boolean check = sizeMB > UploadDownloadConstant.MAX_SIZE;
        if (check) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.MAX_SIZE_FILE);
        }
    }

}
