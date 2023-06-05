package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.FnCashBookRepository;
import com.example.onekids_project.repository.SchoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author lavanviet
 */
@PropertySource(value = "cronjob.properties")
@Component
public class CashbookCronjobs {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PortBean portBean;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private FnCashBookRepository fnCashBookRepository;

    @Scheduled(cron = "${cashbook.create}")
    public void createCronjobNowYear() {
        logger.info("---start create cashbook for all school auto---");
        portBean.checkPortForCronjob();
        List<School> schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        schoolList.forEach(x -> {
            Long idSchool = x.getId();
            Optional<FnCashBook> fnCashBookOptional = fnCashBookRepository.findBySchoolIdAndYear(idSchool, year);
            if (fnCashBookOptional.isEmpty()) {
                Optional<FnCashBook> lastYearOptional = fnCashBookRepository.findBySchoolIdAndYear(idSchool, year - 1);
                FnCashBook fnCashBook = new FnCashBook();
                fnCashBook.setSchool(x);
                fnCashBook.setYear(year);
                fnCashBook.setStartDate(startDate);
                fnCashBook.setEndDate(endDate);
                fnCashBook.setCreatedDate(LocalDateTime.now());
                if (lastYearOptional.isPresent()) {
                    FnCashBook lastYear = lastYearOptional.get();
                    fnCashBook.setMoneyStart(lastYear.getMoneyIn() - lastYear.getMoneyOut() + lastYear.getMoneyStart());
                }
                fnCashBookRepository.save(fnCashBook);
            }
        });
        logger.info("---end create cashbook for all school auto---");
    }
}
