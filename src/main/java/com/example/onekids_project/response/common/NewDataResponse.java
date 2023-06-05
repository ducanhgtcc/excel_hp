package com.example.onekids_project.response.common;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * kết quả trả về khi có lỗi
 */
public class NewDataResponse {

    private static HttpStatus statusValue = null;

    /**
     * hàm trả về kết quả khi có lỗi xảy ra
     *
     * @param body
     * @param message
     * @param httpStatus
     * @return
     */
    public static ResponseEntity<Object> setData(Object body, String message, HttpStatus httpStatus) {
        statusValue = httpStatus;
        return new ResponseEntity(new Data(body, message), httpStatus);
    }

    public static ResponseEntity<Object> setDataCustom(Object body, String message) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, message), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setMessage(String message) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(message), HttpStatus.OK);
    }

    /**
     * hàm trả về kết quả khi có lỗi xảy ra
     *
     * @param body
     * @return
     */
    public static ResponseEntity<Object> setDataSave(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_SAVE), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataCreate(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_CREATE), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataUpdate(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_UPDATE), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataDelete(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_DELETE), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataRestore(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_RESTORE), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataRevoke(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.REVOKE_SUCCESS), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataSearch(Object body) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_SEARCH), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataSearchTotal(Object body, long total) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(body, AppConstant.SUCCESS_SEARCH, total), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataActive(boolean status) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(status, status ? AppConstant.SUCCESS_ACTIVE : AppConstant.SUCCESS_UN_ACTIVE), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataConfirm() {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(AppConstant.APP_TRUE, AppConstant.SUCCESS_CONFIRM), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataApproved(boolean status) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(status, status ? AppConstant.SUCCESS_APPROVED : AppConstant.SUCCESS_UN_APPROVED), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataLocked(boolean status) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(status, status ? AppConstant.SUCCESS_LOCKED : AppConstant.SUCCESS_UN_LOCKED), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setDataGenerate(boolean status) {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(status, AppConstant.GENERATE_SUCCESS), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setSendSms() {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(AppConstant.APP_TRUE, AppConstant.NOTIFY_SMS), HttpStatus.OK);
    }

    public static ResponseEntity<Object> setSendApp() {
        statusValue = HttpStatus.OK;
        return new ResponseEntity(new Data(AppConstant.APP_TRUE, AppConstant.NOTIFY_APP), HttpStatus.OK);
    }


    @Getter
    private static class Data {
        private String companyName = "One Group";
        private String appName = "OneKids";
        private LocalDate nowDate = LocalDate.now();
        private LocalDateTime nowDateTime = LocalDateTime.now();
        private int status = statusValue.value();
        private Object data;
        private String message;

        private long total;

        private Data(Object data, String message) {
            this.data = data;
            this.message = message;
        }

        private Data(Object data, String message, long total) {
            this.data = data;
            this.message = message;
            this.total = total;
        }

        private Data(String message) {
            this.message = message;
        }
    }
}
