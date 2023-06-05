package com.example.onekids_project.validate;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.security.payload.AccountCreateData;
import com.example.onekids_project.security.payload.LoginRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

/**
 * validate với các thuộc tính đầu vào
 */
public class RequestValidate {

    private static final Logger logger = LoggerFactory.getLogger(RequestValidate.class);

    private static final String patternUsername = "[a-zA-Z0-9@.]*";
    private static final String patternPassword = "[a-zA-Z0-9@.]*";
    private static final String patternPhone = "\\d+";
    private static final String usernamePlus = "[a-zA-Z0-9@.]*#NT$";
    private static final String usernameTeacher = "[a-zA-Z0-9@.]*#GV$";
    private static final String usernameParent = "[a-zA-Z0-9@.]*#PH$";

    public static boolean loginRequestValidate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername().trim();
        String password = loginRequest.getPassword().trim();

        if (!StringUtils.isBlank(username)) {
            if (!username.matches(patternUsername)) {
                logger.error("username không đúng định dạng");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập không đúng định dạng");
            }
        }
        if (!StringUtils.isBlank(password)) {
            if (!password.matches(patternPassword)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không đúng định dạng");
            }
        }
        return true;
    }

    /**
     * check username chưa bao gồm phần mở rộng
     *
     * @param usernameNoExtend
     */
    public static void checkUsernameNoExtend(String usernameNoExtend) {
        if (StringUtils.isBlank(usernameNoExtend)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_BLANK);
        }
        if (usernameNoExtend.length() < AppConstant.USERNAME_MIN_LENGHT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_LENGHT);
        } else if (!usernameNoExtend.matches(patternUsername)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_INVALID);
        }
    }

    /**
     * check username đã bao gồm phần mở rộng
     *
     * @param usernameAndExtend
     */
    public static void checkUsernameAndExtend(String usernameAndExtend, String appType) {
        if (StringUtils.isBlank(usernameAndExtend)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_BLANK);
        }
        if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER) || appType.equals(AppTypeConstant.PARENT)) {
            if (usernameAndExtend.length() < AppConstant.USERNAME_EXTEND_MIN_LENGHT) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_LENGHT);
            }
        } else {
            if (usernameAndExtend.length() < AppConstant.USERNAME_MIN_LENGHT) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_LENGHT);
            }
        }
        if (AppTypeConstant.SCHOOL.equals(appType)) {
            if (!usernameAndExtend.matches(usernamePlus)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_INVALID_PLUS);
            }
        } else if (AppTypeConstant.TEACHER.equals(appType)) {
            if (!usernameAndExtend.matches(usernameTeacher)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_INVALID_TEACHER);
            }
        } else if (AppTypeConstant.PARENT.equals(appType)) {
            if (!usernameAndExtend.matches(usernameParent)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_INVALID_PARENT);
            }
        } else if (!usernameAndExtend.matches(patternUsername)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_INVALID);
        }
    }

    /**
     * check password
     *
     * @param password
     */
    public static void checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PASSWORD_BLANK);
        }
        if (password.length() < AppConstant.PASSWORD_MIN_LENGHT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PASSWORD_LENGHT);
        } else if (!password.matches(patternPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PASSWORD_INVALID);
        }
    }

    /**
     * check phone
     *
     * @param phone
     */
    public static void checkPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PHONE_BLANK);
        }
        if (phone.length() != 10)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PHONE_LENGH);
        if (!phone.matches(patternPhone)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PHONE_INVALID);
        }
    }

    /**
     * check appType
     *
     * @param appType
     */
    public static void checkAppType(String appType) {
        if (StringUtils.isBlank(appType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PHONE_BLANK);
        }
        List<String> appTypeList = Arrays.asList(AppTypeConstant.SUPPER_SYSTEM, AppTypeConstant.SYSTEM, AppTypeConstant.AGENT, AppTypeConstant.SUPPER_SCHOOL, AppTypeConstant.SCHOOL, AppTypeConstant.TEACHER, AppTypeConstant.PARENT);
        long count = appTypeList.stream().filter(x -> x.equals(appType)).count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPTYPE_INVALID);
        }
    }

    /**
     * check username đã bao gồm phần mở rộng
     *
     * @param accountCreateData
     * @return
     */
    public static boolean checkDataCreateAccount(AccountCreateData accountCreateData) {
        String fullName = accountCreateData.getFullName();
        String usernameAndExtend = accountCreateData.getUsername();
        String password = accountCreateData.getPassword();
        String appType = accountCreateData.getAppType();
        String phone = accountCreateData.getPhone();
        if (StringUtils.isBlank(fullName) || StringUtils.isBlank(usernameAndExtend) || StringUtils.isBlank(password) || StringUtils.isBlank(appType) || StringUtils.isBlank(phone)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_INFOR_ACCOUNT);
        }
        checkAppType(appType);
        checkPhone(phone);
        checkPassword(password);
        checkUsernameAndExtend(usernameAndExtend, appType);
        return true;
    }


    /**
     * kiểm tra chuỗi cố phải là số nằm trong khoảng từ [0-9]
     *
     * @param var
     * @return true nếu đúng
     */
    public static boolean checkStringInNumber(String var) {
        if (StringUtils.isNotBlank(var)) {
            return var.trim().matches(patternPhone);
        }
        return false;
    }

    /**
     * check số file nhập vào tối đa
     *
     * @param multipartFileList list file
     * @param number            số file tối đa
     */
    public static void checkMaxfileInput(List<MultipartFile> multipartFileList, int number) {
        if (!CollectionUtils.isEmpty(multipartFileList) && multipartFileList.size() > number) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.MAX_FILE + number);
        }
    }

    /**
     * check mã code admin
     *
     * @param codeAdmin
     */
    public static void checkVerificationCodeAdmin(String codeAdmin) {
        if (codeAdmin.length() != 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.VERIFY_CODE_LENGTH);
        } else if (!codeAdmin.matches("[A-Z]*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.VERIFY_CODE_ADMIN);
        }
    }

    /**
     * kiểm tra mã xác thực khi cấp lại tài khoản
     *
     * @param code
     */
    public static void checkVerifyCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CODE_BLANK);
        } else if (code.length() != 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CODE_LENGTH);
        }
    }
}
