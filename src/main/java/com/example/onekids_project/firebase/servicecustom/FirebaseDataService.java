package com.example.onekids_project.firebase.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.model.firebase.ContentFirebaseModel;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

/**
 * date 2021-03-31 14:24
 *
 * @author lavanviet
 */
public interface FirebaseDataService {

    /**
     * gửi firebase  cho danh sách học sinh
     *
     * @param dataList danh sách học sinh nhận firebase
     * @param model    nội dung gửi firebase
     * @param category loại gửi: FirebaseConstant-category
     * @param idSchool idSchool của người gửi
     * @throws FirebaseMessagingException
     */
    void sendFirebaseKids(List<Kids> dataList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException;
    void sendFirebaseKidsNew(List<Long> idList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException;

    /**
     * gửi firebase cho danh sách teacher có cùng idClass
     *
     * @param dataList danh sách nhân sự nhận firebase
     * @param model    nội dung gửi firebase
     * @param category loại gửi: FirebaseConstant-category
     * @param idSchool idSchool của người gửi
     * @param idClass  idClass của nhóm teacher nhận
     * @throws FirebaseMessagingException
     */
    void sendFirebaseTeacherSameClass(List<InfoEmployeeSchool> dataList, ContentFirebaseModel model, String category, Long idSchool, Long idClass) throws FirebaseMessagingException;

    void sendFirebaseTeacher(List<InfoEmployeeSchool> dataList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException;

    /**
     * gửi firebase cho danh sách plus có cùng idSchool
     *
     * @param dataList danh sách nhân sự nhận firebase
     * @param model    nội dung gửi firebase
     * @param category loại gửi: FirebaseConstant-category
     * @param idSchool idSchool của người gửi
     * @param idSchool idSchool của người gửi, đồng thời nó cũng chính là idSchool của nhóm người nhận
     * @throws FirebaseMessagingException
     */
    void sendFirebasePlusSameSchool(List<InfoEmployeeSchool> dataList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException;



}
