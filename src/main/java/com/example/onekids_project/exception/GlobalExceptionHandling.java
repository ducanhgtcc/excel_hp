package com.example.onekids_project.exception;

import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.response.common.NewDataResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 200	OK	/  thao tác thành công
 * 400	lỗi dữ liệu truyền vào /   do bên frontend
 * 412	Lỗi dữ liệu không có trong database /	do bên frontend
 * 501	code có lỗi có thể kiểm soát được / do người code backend
 * 500	tất cả các trường hợp còn lại, không kiểm soát được /	do người code backend
 */
@ControllerAdvice
public class GlobalExceptionHandling {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandling.class);

    @Autowired
    private ListMapper listMapper;

    /**
     * sai mật khẩu người dùng
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity badCredentialsException(BadCredentialsException ex) {
        logger.error("Exception wrong password: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    /**
     * không tìm thấy username người dùng trong database
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    private ResponseEntity internalAuthenticationException(InternalAuthenticationServiceException ex) {
        logger.error("Exception not found by username in database: " + ex.getMessage());
        String message = ex.getMessage();
        message = StringUtils.isNotBlank(message) && message.length() > 17 && message.contains("400 BAD_REQUEST") ? message.substring(17, message.length() - 1) : message;
        return NewDataResponse.setData(ex.getMessage(), message, HttpStatus.BAD_REQUEST);
    }

    /**
     * thiết tham số truyền vào controller của đối tượng trong truy vấn ở object
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity BindRequestException(BindException ex) {
        logger.error("Exception error data input: ");
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        List<ErrorsObject> errorList = listMapper.mapList(fieldErrorList, ErrorsObject.class);
        AtomicInteger i = new AtomicInteger(1);
        errorList.forEach(x -> System.out.println(i.getAndIncrement() + ", " + x.getField() + " : " + x.getDefaultMessage()));
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(errorList, ErrorsConstant.ERROR_INPUT, HttpStatus.BAD_REQUEST);
    }

    /**
     * các thiết tham số truyền vào controller trong truy vấn ở @RequestParem
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity MidssingParameter(MissingServletRequestParameterException ex) {
        logger.error("Exception missing parameter data input: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_INPUT_PARAMETER, HttpStatus.BAD_REQUEST);
    }

    /**
     * tham số truyền vào không hợp lệ ở controller
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity ArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("Exception error invalid input: ");
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        List<ErrorsObject> errorList = listMapper.mapList(fieldErrorList, ErrorsObject.class);
        AtomicInteger i = new AtomicInteger(1);
        errorList.forEach(x -> System.out.println(i.getAndIncrement() + ", " + x.getField() + " : " + x.getDefaultMessage()));
        return NewDataResponse.setData(errorList, ErrorsConstant.ERROR_INPUT, HttpStatus.BAD_REQUEST);
    }

    /**
     * vượt quá kích thước file
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity maxFileExceeded(MaxUploadSizeExceededException ex) {
        logger.error("Exception Max file: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_MAX_FILE, HttpStatus.BAD_REQUEST);
    }

    /**
     * lỗi liên quan đến độ dài trong sql
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity SQLValidException(DataIntegrityViolationException ex) {
        logger.error("Exception SQL length data: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_INPUT_SQL, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * method not support
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity notSupportMethodException(HttpRequestMethodNotSupportedException ex) {
        logger.error("Exception not support method: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.NOT_SUPPORT_METHOD, HttpStatus.BAD_REQUEST);
    }

    /**
     * các Exception trong nghiệp vụ hệ thống
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity BadRequestException(ResponseStatusException ex) {
        logger.error("Exception business: " + ex.getReason());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ex.getReason(), HttpStatus.BAD_REQUEST);
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity NoSuchException(NoSuchElementException ex) {
        logger.error("Exception not found in database when find: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.NOT_FOUND_DATABASE_FIND, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * không tồn tại trong database khi thực hiện xóa
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity EmptyRequestException(EmptyResultDataAccessException ex) {
        logger.error("Exception not found in database when delete: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.NOT_FOUND_DATABASE_DELETE, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * không tìm thấy dữ liệu trong database khi theo tác tìm kiếm với các loại tìm kiếm cũ
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity NotFoundException(NotFoundException ex) {
        logger.error("Exception not found in database: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.NOT_FOUND_DATABASE_FIND, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * tìm thấy nhiều hơn một row trong database
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity nonUniqueResultException(IncorrectResultSizeDataAccessException ex) {
        logger.error("Exception non unique result: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.NON_UNIQUE_RESULT, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * lỗi firebase
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(FirebaseMessagingException.class)
    public ResponseEntity firebaseException(FirebaseMessagingException ex) {
        logger.error("Exception firebase message: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_FIREBASE, HttpStatus.NOT_IMPLEMENTED);
    }


    /**
     * lỗi null poiter
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity NullPointerException(NullPointerException ex) {
        logger.error("Exception null pointer: " + ex.getMessage());
        System.out.println("Error at line: " + Arrays.stream(ex.getStackTrace()).findFirst());
        return NewDataResponse.setData("Exception null pointer: " + ex.getMessage(), ErrorsConstant.ERROR_COMMON_NULL_POINT, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * lỗi nhập xuất file
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity iOException(IOException ex) {
        logger.error("Exception import export file: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_COMMON_FILE, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * lỗi cú pháp sql
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity SQLException(InvalidDataAccessResourceUsageException ex) {
        logger.error("Exception SQL syntax: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_COMMON_SQL, HttpStatus.NOT_IMPLEMENTED);
    }


    /**
     * bắt tất cả các exception còn lại không kiểm soát được trong hệ thống
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity processIOException(Exception ex) {
        logger.error("Exception error code: " + ex.getMessage());
        this.showLogException(ex.getStackTrace());
        return NewDataResponse.setData(ex.getMessage(), ErrorsConstant.ERROR_SYSTEM, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void showLogException(StackTraceElement[] stackTraceElements) {
        int i = 1;
        for (StackTraceElement line : stackTraceElements) {
            System.out.println(line);
            if (i == 2) {
                break;
            }
            i++;
        }
    }
}
