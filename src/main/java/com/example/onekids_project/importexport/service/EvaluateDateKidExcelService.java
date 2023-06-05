package com.example.onekids_project.importexport.service;

import com.example.onekids_project.importexport.model.EvaluateDateKidModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EvaluateDateKidExcelService {
    /**
     * Xuất file excel đánh giá học sinh theo ngày
     * @param evaluateDateKidModels
     * @param idSchool
     * @param idClass
     * @param currentDate
     * @return
     * @throws IOException
     */
    public ByteArrayInputStream evaluateDateToExcel(List<EvaluateDateKidModel> evaluateDateKidModels, Long idSchool, Long idClass, LocalDate currentDate) throws IOException;

    /**
     * Xuất file excel đánh giá theo tháng
     * @param map
     * @param idSchool
     * @param idClass
     * @param date
     * @return
     * @throws IOException
     */
    ByteArrayInputStream evaluateDateToExcelMonth(Map<Long, List<EvaluateDateKidModel>> map, Long idSchool, Long idClass, LocalDate date) throws IOException;

}
