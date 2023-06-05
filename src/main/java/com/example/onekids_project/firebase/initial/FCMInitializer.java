package com.example.onekids_project.firebase.initial;

import com.example.onekids_project.common.AppTypeConstant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FCMInitializer {
    private static final Logger logger = LoggerFactory.getLogger(FCMInitializer.class);

    @Value("${app.firebase-parent-file}")
    private String firebaseConfigPathParent;

    @Value("${app.firebase-teacher-file}")
    private String firebaseConfigPathTeacher;

    @Value("${app.firebase-plus-file}")
    private String firebaseConfigPathPlus;

    @Bean
    public void initializeParent() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPathParent).getInputStream())).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options, AppTypeConstant.PARENT);
                logger.info("---------Firebase parent started-----------");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Bean
    public void initializeTeacher() {
        try {
            FirebaseOptions optionsTeacher = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPathTeacher).getInputStream())).build();
            FirebaseApp.initializeApp(optionsTeacher, AppTypeConstant.TEACHER);
            logger.info("---------Firebase teacher started-----------");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    @Bean
    public void initializePlus() {
        try {
            FirebaseOptions optionsTeacher = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPathPlus).getInputStream())).build();
            FirebaseApp.initializeApp(optionsTeacher, AppTypeConstant.SCHOOL);
            logger.info("---------Firebase plus started-----------");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
