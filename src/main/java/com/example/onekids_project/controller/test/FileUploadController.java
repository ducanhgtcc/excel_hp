package com.example.onekids_project.controller.test;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.util.ConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/web/test")
public class FileUploadController {
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    public static String uploadDirectory="D:\\Directory3";
    @RequestMapping("/uploadview")
    public String UploadPage() {
        return "uploadview";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile[] files) {
//        for (MultipartFile file : files) {
//            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
//            try {
//                Files.write(fileNameAndPath, file.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
//        Files.write(fileNameAndPath, file.getBytes());
        return "uploadstatusview";
    }
    @PostMapping("/upload-file")
    public void saveProductInfo(@RequestParam("files") MultipartFile files) throws IOException {
        // tránh khi upload file mà tên các ảnh trùng nhau thêm System.currentMillis
        String fileName=System.currentTimeMillis() + "_" + files.getOriginalFilename();
        processUploadFile(files,fileName);
        InfoEmployeeSchool infoEmployeeSchool= infoEmployeeSchoolRepository.findById(2L).get();
        infoEmployeeSchool.setAvatar("/upload/"+fileName);
        infoEmployeeSchool=infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }
    @GetMapping("/test-get-upload")
    public InfoEmployeeSchool getEmployee2(){
        InfoEmployeeSchool infoEmployeeSchool=infoEmployeeSchoolRepository.findById(2L).get();
        Path fileNameAndPath = Paths.get(uploadDirectory);
        return infoEmployeeSchool;
    }
    public static void processUploadFile(MultipartFile multipartFile,String fileName) throws IllegalStateException, IOException {
        if (!multipartFile.getOriginalFilename().isEmpty()) {// Nếu trong trường hợp có file khác null
            File dir = new File(ConfigLoader.getInstance().getValue("upload.location"));
            if (!dir.exists()) {
                dir.mkdirs();// nếu chưa tồn tại thì tạo ra để tránh trường hợp lỗi
            }

            File file = new File(ConfigLoader.getInstance().getValue("upload.location"), fileName);
            multipartFile.transferTo(file);// copy File từ file upload bên phía client lên server
        }
    }
}
