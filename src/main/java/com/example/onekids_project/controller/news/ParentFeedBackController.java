package com.example.onekids_project.controller.news;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.FeedbackDTO;
import com.example.onekids_project.entity.user.FeedBackFile;
import com.example.onekids_project.repository.FeedBackFileRepository;
import com.example.onekids_project.request.feedback.FeedBackRequest;
import com.example.onekids_project.request.feedback.SearchParentFeedbackRequest;
import com.example.onekids_project.request.feedback.UpdateFeedbackRequest;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.feedback.FeedBackResponse;
import com.example.onekids_project.response.feedback.ListFeedBackResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.FeedBackSerVice;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/web/feedbackpr")
public class ParentFeedBackController {
    private static final Logger logger = LoggerFactory.getLogger(ParentFeedBackController.class);
    @Autowired
    FeedBackSerVice feedBackSerVice;

    @Autowired
    private FeedBackFileRepository feedBackFileRepository;

    //    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        RequestUtils.getFirstRequest(principal,baseRequest);
//            //check id_school_login có tồn tại trong tài khoản của người dùng đang đăng nhập hay không
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
//            if (pageNumber == -1) {
//                logger.error("Số trang không hợp lệ");
//                return DataResponse.getData("Số trang không hợp lệ", HttpStatus.BAD_REQUEST);
//            }
//            Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);
//
//            ListFeedBackResponse listFeedBackResponse = feedBackSerVice.findAllFeedBack(idSchoolLogin, pageable);
//            if (listFeedBackResponse == null) {
//                logger.warn("Không có ");
//                return DataResponse.getData("Không có ", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Tìm kiếm tất cả thành công");
//            return DataResponse.getData(listFeedBackResponse, HttpStatus.OK);
//
//
//    }
    @RequestMapping(value = "/feedbackdownload/{idUrlFileFeedback}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlFileFeedback") Long idUrlFileFeedback) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        FeedBackFile feedBackFile = feedBackFileRepository.findById(idUrlFileFeedback).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(feedBackFile.getUrlLocalPicture())) {
                file = new File(feedBackFile.getUrlLocalPicture());
            } else if (StringUtils.isNotBlank(feedBackFile.getUrlLocalPicture())) {
                file = new File(feedBackFile.getUrlLocalPicture());
            }

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

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchParentFeedbackRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListFeedBackResponse listFeedBackResponse = feedBackSerVice.searchTitle(principal, request);
        return NewDataResponse.setDataSearch(listFeedBackResponse);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        FeedbackDTO feedbackDTOOptional = feedBackSerVice.findByIdFeedback(id);
        return NewDataResponse.setDataSearch(feedbackDTOOptional);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateFeedbackRequest feedBackEditRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, feedBackEditRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!feedBackEditRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + feedBackEditRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        FeedBackResponse feedBackResponse = feedBackSerVice.updateFeedback(principal.getIdSchoolLogin(), principal, feedBackEditRequest);
        return NewDataResponse.setDataUpdate(feedBackResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-read")
    public ResponseEntity updateRead(@RequestBody List<FeedBackRequest> feedBackResponses, Long id) {
        FeedBackRequest feedBackRequest = feedBackSerVice.updateRead(id, feedBackResponses);
        return NewDataResponse.setDataUpdate(feedBackRequest);
    }
}
