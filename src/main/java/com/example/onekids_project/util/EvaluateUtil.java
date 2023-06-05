package com.example.onekids_project.util;

import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.repository.KidsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class EvaluateUtil {

    /**
     * check có nhận xét ngày hay chưa
     * true là đã có nhận xét ngày
     * false là chưa có nhận xét ngày
     *
     * @param evaluateDate
     * @return
     */
    public static boolean checkHasEvaluateDate(EvaluateDate evaluateDate) {
        if (StringUtils.isNotBlank(evaluateDate.getLearnContent()) || StringUtils.isNotBlank(evaluateDate.getEatContent()) || StringUtils.isNotBlank(evaluateDate.getSleepContent()) ||
                StringUtils.isNotBlank(evaluateDate.getSanitaryContent()) || StringUtils.isNotBlank(evaluateDate.getHealtContent()) || StringUtils.isNotBlank(evaluateDate.getCommonContent()) || CollectionUtils.isNotEmpty(evaluateDate.getEvaluateAttachFileList())) {
            return true;
        }
        return false;
    }

    /**
     * check có nhận xét tuần hay chưa
     * true là đã có nhận xét tuần
     * false là chưa
     *
     * @param evaluateWeek
     * @return
     */
    public static boolean checkHasEvaluateWeek(EvaluateWeek evaluateWeek) {
        if (StringUtils.isNotBlank(evaluateWeek.getContent()) || CollectionUtils.isNotEmpty(evaluateWeek.getEvaluateWeekFileList())) {
            return true;
        }
        return false;
    }

    /**
     * check có nhận xét tháng hay chưa
     * true là đã có nhận xét tuần
     * false là chưa
     *
     * @param evaluateWeeMonth
     * @return
     */
    public static boolean checkHasEvaluateMonth(EvaluateMonth evaluateWeeMonth) {
        if (StringUtils.isNotBlank(evaluateWeeMonth.getContent()) || CollectionUtils.isNotEmpty(evaluateWeeMonth.getEvaluateMonthFileList())) {
            return true;
        }
        return false;
    }



}
