package com.example.onekids_project.firebase.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * date 2021-04-12 16:21
 *
 * @author lavanviet
 */
public interface FirebaseFunctionService {
    /**
     * gửi firebase cho plus từ học sinh
     *
     * @param idWebSystemTitle idWebTitle
     * @param kids             học sinh gửi
     * @param content          nội dung gửi
     * @param category         FirebaseConstant-category
     * @throws FirebaseMessagingException
     */
    void sendPlusByKids(Long idWebSystemTitle, Kids kids, String content, String category) throws FirebaseMessagingException;

    /**
     * gửi firebase cho plus từ teacher
     *
     * @param idWebSystemTitle idWebTitle
     * @param content          nội dung gửi
     * @param category         FirebaseConstant-category
     * @param idSchool         idSchool của teacher đang đăng nhập
     * @throws FirebaseMessagingException
     */
    void sendPlusByTeacher(Long idWebSystemTitle, String content, String category, Long idSchool) throws FirebaseMessagingException;

    /**
     * gửi firebase cho plus từ học sinh
     *
     * @param idWebSystemTitle idWebTitle
     * @param kids             học sinh gửi
     * @param content          nội dung gửi
     * @param category         FirebaseConstant-category
     * @throws FirebaseMessagingException
     */
    void sendTeacherByKids(Long idWebSystemTitle, Kids kids, String content, String category) throws FirebaseMessagingException;

    void sendTeacherByPlus(Long idWebSystemTitle, InfoEmployeeSchool infoEmployeeSchool, String content, String category, Long idSchool) throws FirebaseMessagingException;

    void sendTeacherByPlusNoContent(Long idWebSystemTitle, InfoEmployeeSchool infoEmployeeSchool, String category, Long idSchool) throws FirebaseMessagingException;

    /**
     * giáo viên xác nhận lời nhắn
     * gửi cho parent
     *
     * @param kids
     * @param teacherName
     * @throws FirebaseMessagingException
     */
    void sendParentByTeacherNoContent(Long idWebSystemTitle, Kids kids, String category, String teacherName) throws FirebaseMessagingException;

    /**
     * nhà trường xác nhận lời nhắn
     * gửi cho học sinh
     *
     * @param kids
     * @throws FirebaseMessagingException
     */
    void sendParentByPlusNoContent(Long idWebSystemTitle, Kids kids, String category) throws FirebaseMessagingException;

    /**
     * gửi cho phụ huynh từ plus có nội dung
     *
     * @param kids     học sinh nhận
     * @param category FirebaseConstant
     * @param content  nội dung gửi
     * @throws FirebaseMessagingException
     */
    void sendParentByPlus(Long idWebSystemTitle, Kids kids, String category, String content) throws FirebaseMessagingException;
    void sendParentByPlusList(Long idWebSystemTitle, List<Kids> kidsList, String category, String content, Long idSchool) throws FirebaseMessagingException;
    @Async
    void sendParentByPlusListNew(Long idWebSystemTitle, List<Long> idList, String category, String content, Long idSchool) throws FirebaseMessagingException;

    /**
     * gửi cho phụ huynh từ teacher có nội dung
     *
     * @param kids     học sinh nhận
     * @param category FirebaseConstant
     * @param content  nội dung gửi
     * @throws FirebaseMessagingException
     */
    void sendParentByTeacher(Long idWebSystemTitle, Kids kids, String category, String content) throws FirebaseMessagingException;

    /**
     * gửi cho học sinh có title và content tùy biến
     *
     * @param kidsList danh sách học sinh
     * @param title    tiều đề gửi
     * @param content  nội dung gửi
     * @param idSchool trường gửi
     * @param category FirebaseConstant - CATEGORY_
     * @throws FirebaseMessagingException
     */
    void sendParentCommon(List<Kids> kidsList, String title, String content, Long idSchool, String category) throws FirebaseMessagingException;

    /**
     * gửi cho nhân sự có title và content tùy biến
     *
     * @param dataList danh sách nhân sự
     * @param title    tiều đề gửi
     * @param content  nội dung gửi
     * @param idSchool trường gửi
     * @param category FirebaseConstant - CATEGORY_
     * @throws FirebaseMessagingException
     */
    void sendTeacherCommon(List<InfoEmployeeSchool> dataList, String title, String content, Long idSchool, String category) throws FirebaseMessagingException;
    /**
     * gửi cho plus có title và content tùy biến
     *
     * @param dataList danh sách nhân sự
     * @param title    tiều đề gửi
     * @param content  nội dung gửi
     * @param idSchool trường gửi
     * @param category FirebaseConstant - CATEGORY_
     * @throws FirebaseMessagingException
     */
    void sendPlusCommon(List<InfoEmployeeSchool> dataList, String title, String content, Long idSchool, String category) throws FirebaseMessagingException;
    void sendPlusCommonHasPlusList(String title, String content, Long idSchool, String category) throws FirebaseMessagingException;
}
