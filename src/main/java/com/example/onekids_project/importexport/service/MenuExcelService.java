package com.example.onekids_project.importexport.service;

import com.example.onekids_project.request.classmenu.CreateFileExcelRequest;
import com.example.onekids_project.request.classmenu.CreateMultiClassMenu;
import com.example.onekids_project.response.classmenu.TabClassMenuByIdClassInWeek;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface MenuExcelService {

    ByteArrayInputStream customMenuExcel(List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList, Long idSchool, Long idClass, LocalDate date) throws IOException;

    CreateMultiClassMenu saveMenuFileExcel(Long idSchool, CreateFileExcelRequest createFileExcelRequest) throws IOException;

}
