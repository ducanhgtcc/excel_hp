package com.example.onekids_project.firebase.serviceimpl;

import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.firebase.servicecustom.FirebaseDataService;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.model.firebase.ContentFirebaseModel;
import com.example.onekids_project.util.FirebaseUtils;
import com.example.onekids_project.util.UserInforUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * date 2021-04-12 16:22
 *
 * @author lavanviet
 */
@Service
public class FirebaseFunctionServiceImpl implements FirebaseFunctionService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FirebaseDataService firebaseDataService;

    public void sendPlusByKids(Long idWebSystemTitle, Kids kids, String content, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        Long idSchool = kids.getIdSchool();
        model.setTitle(model.getTitle().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()));
        model.setBody(content);
        List<InfoEmployeeSchool> plusList = FirebaseUtils.getPlusList(idSchool);
        firebaseDataService.sendFirebasePlusSameSchool(plusList, model, category, idSchool);
    }

    @Override
    public void sendPlusByTeacher(Long idWebSystemTitle, String content, String category, Long idSchool) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(content);
        List<InfoEmployeeSchool> plusList = FirebaseUtils.getPlusList(idSchool);
        firebaseDataService.sendFirebasePlusSameSchool(plusList, model, category, idSchool);
    }

    @Override
    public void sendTeacherByKids(Long idWebSystemTitle, Kids kids, String content, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        Long idClass = kids.getMaClass().getId();
        Long idSchool = kids.getIdSchool();
        model.setTitle(model.getTitle().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()));
        model.setBody(content);
        List<InfoEmployeeSchool> teacherList = FirebaseUtils.getTeacherList(idClass);
        firebaseDataService.sendFirebaseTeacherSameClass(teacherList, model, category, idSchool, idClass);
    }

    @Override
    public void sendTeacherByPlus(Long idWebSystemTitle, InfoEmployeeSchool infoEmployeeSchool, String content, String category, Long idSchool) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(content);
        firebaseDataService.sendFirebaseTeacher(Collections.singletonList(infoEmployeeSchool), model, category, idSchool);
    }

    @Override
    public void sendTeacherByPlusNoContent(Long idWebSystemTitle, InfoEmployeeSchool infoEmployeeSchool, String category, Long idSchool) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        firebaseDataService.sendFirebaseTeacher(Collections.singletonList(infoEmployeeSchool), model, category, idSchool);
    }

    @Override
    public void sendParentByTeacherNoContent(Long idWebSystemTitle, Kids kids, String category, String teacherName) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(model.getBody().replace(FirebaseConstant.REPLACE_FULL_NAME, teacherName).replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()));
        firebaseDataService.sendFirebaseKids(Collections.singletonList(kids), model, category, kids.getIdSchool());
    }

    @Override
    public void sendParentByPlusNoContent(Long idWebSystemTitle, Kids kids, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(model.getBody().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()));
        firebaseDataService.sendFirebaseKids(Collections.singletonList(kids), model, category, kids.getIdSchool());
    }

    @Override
    public void sendParentByPlus(Long idWebSystemTitle, Kids kids, String category, String content) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(content);
        firebaseDataService.sendFirebaseKids(Collections.singletonList(kids), model, category, kids.getIdSchool());
    }

    @Override
    public void sendParentByPlusList(Long idWebSystemTitle, List<Kids> kidsList, String category, String content, Long idSchool) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(content);
        firebaseDataService.sendFirebaseKids(kidsList, model, category, idSchool);
    }

    @Override
    public void sendParentByPlusListNew(Long idWebSystemTitle, List<Long> idList, String category, String content, Long idSchool) throws FirebaseMessagingException {
        logger.info("--send firebase new--");
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(content);
        firebaseDataService.sendFirebaseKidsNew(idList, model, category, idSchool);
    }

    @Override
    public void sendParentByTeacher(Long idWebSystemTitle, Kids kids, String category, String content) throws FirebaseMessagingException {
        ContentFirebaseModel model = FirebaseUtils.getWelSystemTitle(idWebSystemTitle);
        model.setBody(content);
        firebaseDataService.sendFirebaseKids(Collections.singletonList(kids), model, category, kids.getIdSchool());
    }

    @Override
    public void sendParentCommon(List<Kids> kidsList, String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = new ContentFirebaseModel();
        model.setTitle(title);
        model.setBody(content);
        firebaseDataService.sendFirebaseKids(kidsList, model, category, idSchool);
    }

    @Override
    public void sendTeacherCommon(List<InfoEmployeeSchool> dataList, String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = new ContentFirebaseModel();
        model.setTitle(title);
        model.setBody(content);
        firebaseDataService.sendFirebaseTeacher(dataList, model, category, idSchool);
    }

    @Override
    public void sendPlusCommon(List<InfoEmployeeSchool> dataList, String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = new ContentFirebaseModel();
        model.setTitle(title);
        model.setBody(content);
        firebaseDataService.sendFirebasePlusSameSchool(dataList, model, category, idSchool);
    }

    @Override
    public void sendPlusCommonHasPlusList(String title, String content, Long idSchool, String category) throws FirebaseMessagingException {
        ContentFirebaseModel model = new ContentFirebaseModel();
        model.setTitle(title);
        model.setBody(content);
        List<InfoEmployeeSchool> dataList = UserInforUtils.getPlusInSchoolHasAccount();
        firebaseDataService.sendFirebasePlusSameSchool(dataList, model, category, idSchool);
    }

}
