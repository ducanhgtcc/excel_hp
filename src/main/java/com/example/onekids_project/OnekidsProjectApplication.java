package com.example.onekids_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class OnekidsProjectApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(OnekidsProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println("----------------ONEKIDS APPLICATION STARTED---------------");
        System.out.println("---------------------------28/04---------------");
        System.out.println();
    }
}
