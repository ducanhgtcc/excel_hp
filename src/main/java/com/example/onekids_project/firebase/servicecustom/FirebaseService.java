package com.example.onekids_project.firebase.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FirebaseService {
    /**
     * gửi cho 1 token
     *
     * @param token
     * @param route
     * @param id
     * @param notifyRequest
     * @throws FirebaseMessagingException
     */
    void sendToToken(String token, String route, Long id, NotifyRequest notifyRequest) throws FirebaseMessagingException;

    /**
     * gửi cho nhiều token và thông báo lỗi
     *
     * @param tokenList
     * @param route:        trong file 'keyfirebase'
     * @param id:           apprent->idKid, appTeacher->idClass, appPlus->idSchool
     * @param notifyRequest
     * @throws FirebaseMessagingException
     */
    void sendMulticast(List<String> tokenList, String route, Long id, NotifyRequest notifyRequest) throws FirebaseMessagingException;

    /**
     * gửi cho nhiều token và có số thông báo thành công và lỗi
     *
     * @param
     * @param route
     * @param
     * @param notifyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    FirebaseResponse sendMulticastAndHandleErrorsParent(List<TokenFirebaseObject> tokenFirebaseObjectList, String route, NotifyRequest notifyRequest, String idKid) throws FirebaseMessagingException;

    /**
     * gửi cho giáo viên kèm lớp
     *
     * @param tokenFirebaseObjectList
     * @param route
     * @param notifyRequest
     * @param idClass
     * @return
     * @throws FirebaseMessagingException
     */
    FirebaseResponse sendMulticastAndHandleErrorsTeacherClass(List<TokenFirebaseObject> tokenFirebaseObjectList, String route, NotifyRequest notifyRequest, String idClass) throws FirebaseMessagingException;

    /**
     * gửi cho giáo viên không theo lớp
     *
     * @param tokenFirebaseObjectList
     * @param route
     * @param notifyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    FirebaseResponse sendMulticastAndHandleErrorsTeacher(List<TokenFirebaseObject> tokenFirebaseObjectList, String route, NotifyRequest notifyRequest) throws FirebaseMessagingException;


    /**
     * gửi cho plus
     *
     * @param tokenFirebaseObjectList
     * @param route
     * @param notifyRequest
     * @param idClass
     * @return
     * @throws FirebaseMessagingException
     */
    FirebaseResponse sendMulticastAndHandleErrorsPlus(List<TokenFirebaseObject> tokenFirebaseObjectList, String route, NotifyRequest notifyRequest, Long idClass) throws FirebaseMessagingException;

    /**
     * lấy token cho nhân viên
     *
     * @param infoEmployeeSchoolList
     * @return
     */
    List<TokenFirebaseObject> getEmployeeTokenFirebases(List<InfoEmployeeSchool> infoEmployeeSchoolList);

    /**
     * lấy token cho một phụ huynh
     *
     * @param parent
     * @return
     */
    List<TokenFirebaseObject> getParentOneTokenFirebases(Parent parent);

    /**
     * lấy token cho plus
     *
     * @param infoEmployeeSchoolList
     * @param function
     * @return
     */
    List<TokenFirebaseObject> getPlusTokenFirebases(List<InfoEmployeeSchool> infoEmployeeSchoolList, String function);

    /**
     * gửi lại thông báo firebase lỗi >15=> xóa
     *
     * @throws FirebaseMessagingException
     */
    void sendFirebaseFail() throws FirebaseMessagingException;

    /**
     * gửi tự động sinh nhật
     *
     * @throws FirebaseMessagingException
     */
    void sendAutoFirebaseBirthday() throws FirebaseMessagingException, ExecutionException, InterruptedException;
    void sendAutoFirebaseBirthdaySystem() throws FirebaseMessagingException, ExecutionException, InterruptedException;

}
