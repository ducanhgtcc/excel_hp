package com.example.onekids_project.controller.news;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.controller.AppSendController;
import com.example.onekids_project.dto.FeedbackDTO;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.feedback.FeedBackRequest;
import com.example.onekids_project.request.feedback.SearchParentFeedbackRequest;
import com.example.onekids_project.request.feedback.SearchTeacherFeedbackRequest;
import com.example.onekids_project.request.feedback.UpdateFeedbackRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.feedback.FeedBackResponse;
import com.example.onekids_project.response.feedback.ListFeedBackResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.FeedBackSerVice;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController


@RequestMapping("/web/feedbacktc")
public class TeacherFeedBackController {
    private static final Logger logger = LoggerFactory.getLogger(AppSendController.class);
    @Autowired
    FeedBackSerVice feedBackSerVice;

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        RequestUtils.getFirstRequest(principal, baseRequest);
//        CommonValidate.checkDataPlus(principal);
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
//            ListFeedBackResponse listFeedBackResponse = feedBackSerVice.findAllTeacherFeedBack(idSchoolLogin, pageable);
//            if (listFeedBackResponse == null) {
//                logger.warn("Không có ");
//                return DataResponse.getData("Không có ", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Tìm kiếm tất cả thành công");
//            return DataResponse.getData(listFeedBackResponse, HttpStatus.OK);
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchTeacherFeedbackRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ListFeedBackResponse listFeedBackResponse = feedBackSerVice.searchTitleTeacher(principal, request);
        return NewDataResponse.setDataSearch(listFeedBackResponse);

    }

//    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
//    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
//        RequestUtils.getFirstRequest(principal, id);
//        CommonValidate.checkDataPlus(principal);
//            //check id_school_login có tồn tại trong tài khoản của người dùng đang đăng nhập hay không
//            Long idSchoolLogin = principal.getIdSchoolLogin();
//            if (idSchoolLogin == null || idSchoolLogin <= 0) {
//                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//            }
//            //thực hiện tìm kiếm theo id
//            FeedbackDTO feedbackDTOOptional = feedBackSerVice.findByIdTeacherFeedback(id);
//            //trả ra thông tin khối học theo id
//            logger.info("Tìm kiếm theo id thành công");
//            return DataResponse.getData(feedbackDTOOptional, HttpStatus.OK);
//
//    }

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
        return NewDataResponse.setDataSearch(feedBackRequest);
    }


}
