//package com.example.onekids_project.controller.parentdiary;
//
//
//import com.example.onekids_project.common.AppConstant;
//import com.example.onekids_project.dto.AbsentDateDTO;
//import com.example.onekids_project.request.base.BaseRequest;
//import com.example.onekids_project.response.common.DataResponse;
//import com.example.onekids_project.response.common.ErrorResponse;
//import com.example.onekids_project.response.parentdiary.ListAbsentDateResponse;
//import com.example.onekids_project.security.model.CurrentUser;
//import com.example.onekids_project.security.model.UserPrincipal;
//import com.example.onekids_project.service.servicecustom.AbsentDateSerVice;
//import com.example.onekids_project.util.ConvertData;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//
//@RequestMapping("/web/absent-date")
//public class AbsentDateController {
//    private static final Logger logger = LoggerFactory.getLogger(AbsentDateController.class);
//    @Autowired
//    AbsentDateSerVice absentDateSerVice;
//
//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        try {
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
//            ListAbsentDateResponse listAbsentDateResponse = absentDateSerVice.findAllAbsentDate(idSchoolLogin, pageable);
//            if (listAbsentDateResponse == null) {
//                logger.warn("Không có ");
//                return DataResponse.getData("Không có ", HttpStatus.NOT_FOUND);
//            }
//            logger.info("Tìm kiếm tất cả thành công");
//            return DataResponse.getData(listAbsentDateResponse, HttpStatus.OK);
//
//        } catch (Exception e) {
//            //bắt tất cả các ngoại lệ ném ra trong quá trình tìm kiếm
//            logger.error("Exception tìm kiếm tất cả: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Lỗi hệ thống tìm kiếm", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//}
