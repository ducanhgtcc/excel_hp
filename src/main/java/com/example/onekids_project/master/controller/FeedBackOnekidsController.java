package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.master.request.FeedBackOnekidsRequest;
import com.example.onekids_project.master.request.SearchFeedBackOneKidsRequest;
import com.example.onekids_project.master.response.FeedBackOnekidsResponse;
import com.example.onekids_project.master.response.feedback.FeedbackDetailAdminResponse;
import com.example.onekids_project.master.response.feedback.ListFeedbackAdminResponse;
import com.example.onekids_project.master.service.FeedBackOnekidsService;
import com.example.onekids_project.repository.FeedBackFileRepository;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/web/feedback-onekids")
public class FeedBackOnekidsController {

    private static final Logger logger = LoggerFactory.getLogger(FeedBackOnekidsController.class);

    @Autowired
    private FeedBackOnekidsService feedBackOnekidsService;

    @Autowired
    private FeedBackFileRepository feedBackFileRepository;

    @GetMapping(value = "/search")
    public ResponseEntity getAllFeedBackOneKids(@CurrentUser UserPrincipal principal, @Valid SearchFeedBackOneKidsRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListFeedbackAdminResponse response = feedBackOnekidsService.searchFeedbackAdmin(request);
        return NewDataResponse.setDataSearch(response);
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity viewFeedbackAdmin(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        FeedbackDetailAdminResponse response = feedBackOnekidsService.viewFeedbackDetail(id);
        return NewDataResponse.setDataSearch(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAllFeedBackOneKidsById(@PathVariable("id") Long id) {
        try {
            FeedBackOnekidsResponse feedBackOnekidsResponse = feedBackOnekidsService.findFeedBackOnekidsById(id);
            if (feedBackOnekidsResponse == null) {
                logger.error("Không có góp ý nào theo id");
                return DataResponse.getData("Không có góp ý nào theo id", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm góp ý theo id thành công");
            return DataResponse.getData(feedBackOnekidsResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm góp ý theo tùy chọn: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi tìm kiếm góp ý theo id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFeedBackOneKidsById(@PathVariable("id") Long id) {
        try {
            boolean checkDelete = feedBackOnekidsService.deleteFeedBackById(id);
            if (!checkDelete) {
                logger.error("Không thể xóa góp ý");
                return DataResponse.getData("Không thể xóa góp ý", HttpStatus.NOT_FOUND);
            }
            logger.info("Xóa góp ý thành công");
            return DataResponse.getData(checkDelete, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi xóa góp ý: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi xóa góp ý", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity deleteMultiFeedBackOneKidsById(@RequestBody List<Long> idList) {
        try {
            boolean checkDelete = feedBackOnekidsService.deleteMultiFeedBackById(idList);
            if (!checkDelete) {
                logger.error("Không thể xóa góp ý");
                return DataResponse.getData("Không thể xóa góp ý", HttpStatus.NOT_FOUND);
            }
            logger.info("Xóa góp ý thành công");
            return DataResponse.getData("checkDelete", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi xóa góp ý: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi xóa góp ý", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity createFeedBackOnekids(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute FeedBackOnekidsRequest feedBackOnekidsRequest) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), feedBackOnekidsRequest);
        boolean checkCreate = feedBackOnekidsService.createFeedbackHidden(principal, feedBackOnekidsRequest);
        return NewDataResponse.setDataCustom(checkCreate, MessageWebConstant.CREATE_NOTIFY);
    }


    /**
     * Downlaod file cách 2
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download/{idUrlFile}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlFile") Long idUrlFile) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        FeedBackFile feedBackFile = feedBackFileRepository.findById(idUrlFile).get();
        try {
            File file = null;
//            if (StringUtils.isNotBlank(urlFileFeedBack.getUrlLocalFile())) {
//                file = new File(urlFileFeedBack.getUrlLocalFile());
//            } else if (StringUtils.isNotBlank(urlFileFeedBack.getUrlLocalPicture())) {
//                file = new File(urlFileFeedBack.getUrlLocalPicture());
//            }

            byte[] data = FileUtils.readFileToByteArray(file);
            // Set mimeType trả về
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Thiết lập thông tin trả về
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<InputStreamResource>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
