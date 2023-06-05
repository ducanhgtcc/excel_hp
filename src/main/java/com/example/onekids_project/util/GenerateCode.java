package com.example.onekids_project.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * tạo mã cho các đối tượng
 */
public class GenerateCode {
    private static final String ADMIN = "AD";
    private static final String SCHOOL = "SC";
    private static final String EMPLOYEE = "EP";
    private static final String KID = "KD";
    private static final String PARENT = "PA";

    /**
     * tạo mã cho trường
     *
     * @return
     */
    public static String codeSchool() {
        String random = RandomStringUtils.random(3, true, false);
        String code = SCHOOL.concat(random.toUpperCase());
        return code;
    }

    /**
     * tạo mã cho nhân viên
     *
     * @return
     */
    public static String codeEmployee() {
        String random = RandomStringUtils.random(5, true, true);
        String code = EMPLOYEE.concat(random.toUpperCase());
        return code;
    }

    /**
     * tạo mã cho học sinh
     *
     * @return
     */
    public static String codeKid() {
        String random = RandomStringUtils.random(6, true, true);
        String code = KID.concat(random.toUpperCase());
        return code;
    }

    /**
     * tạo mã cho học sinh
     *
     * @return
     */
    public static String codeParent() {
        String random = RandomStringUtils.random(6, true, true);
        String code = PARENT.concat(random.toUpperCase());
        return code;
    }

    /**
     * tạo mã môn học
     *
     * @return
     */
    public static String codeSubject() {
        String random = RandomStringUtils.random(5, true, false);
        String code = random.toUpperCase();
        return code;
    }

    /**
     * tạo mã admin
     *
     * @return
     */
    public static String codeAdmin() {
        String random = RandomStringUtils.random(4, true, false);
        String code = ADMIN.concat(random.toUpperCase());
        return code;
    }

    /**
     * chỉ bao gồm các ký tự từ a-z thường
     *
     * @return
     */
    public static String passwordAuto() {
        String random = RandomStringUtils.random(6, false, true);
        return random;
    }

    /**
     * các ký tự A-Z hoa
     *
     * @return
     */
    public static String getLetterUpperCase() {
        String random = RandomStringUtils.random(6, 65, 90, true, false);
        return random;
    }

    /**
     * các ký tự số từ 0-9
     *
     * @return
     */
    public static String getNumber() {
        return RandomStringUtils.random(6, false, true);
    }

    public static String getNumberInput(int number) {
        return RandomStringUtils.random(number, false, true);
    }

    public static String getLetterInput(int number){
        return RandomStringUtils.random(number, true, false);
    }
}
