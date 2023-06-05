package com.example.onekids_project.util;

import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelDataNew;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-06-04 11:35
 *
 * @author lavanviet
 */
public class ExportExcelUtils {
    public static List<ExcelData> setHeaderExcel(List<String> stringList) {
        List<ExcelData> dataList = new ArrayList<>();
        stringList.forEach(x -> {
            ExcelData model = new ExcelData();
            model.setPro1(x);
            dataList.add(model);
        });
        return dataList;
    }

    public static ExcelData setBodyExcel(List<String> stringList) {
        ExcelData model = new ExcelData();
        for (int i = 0; i < stringList.size(); i++) {
            if (i == 0) {
                model.setPro1(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 1) {
                model.setPro2(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 2) {
                model.setPro3(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 3) {
                model.setPro4(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 4) {
                model.setPro5(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 5) {
                model.setPro6(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 6) {
                model.setPro7(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 7) {
                model.setPro8(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 8) {
                model.setPro9(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 9) {
                model.setPro10(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 10) {
                model.setPro11(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 11) {
                model.setPro12(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 12) {
                model.setPro13(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 13) {
                model.setPro14(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 14) {
                model.setPro15(stringList.get(i) != null ? stringList.get(i) : "");
            }
        }
        return model;
    }

    public static List<ExcelDataNew> setHeaderExcelNew(List<String> stringList) {
        List<ExcelDataNew> dataList = new ArrayList<>();
        stringList.forEach(x -> {
            ExcelDataNew model = new ExcelDataNew();
            model.setPro1(x);
            dataList.add(model);
        });
        return dataList;
    }

    public static ExcelDataNew setBodyExcelNew(List<String> stringList) {
        ExcelDataNew model = new ExcelDataNew();
        for (int i = 0; i < stringList.size(); i++) {
            if (i == 0) {
                model.setPro1(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 1) {
                model.setPro2(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 2) {
                model.setPro3(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 3) {
                model.setPro4(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 4) {
                model.setPro5(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 5) {
                model.setPro6(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 6) {
                model.setPro7(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 7) {
                model.setPro8(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 8) {
                model.setPro9(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 9) {
                model.setPro10(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 10) {
                model.setPro11(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 11) {
                model.setPro12(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 12) {
                model.setPro13(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 13) {
                model.setPro14(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 14) {
                model.setPro15(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 15) {
                model.setPro16(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 16) {
                model.setPro17(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 17) {
                model.setPro18(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 18) {
                model.setPro19(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 19) {
                model.setPro20(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 20) {
                model.setPro21(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 21) {
                model.setPro22(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 22) {
                model.setPro23(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 23) {
                model.setPro24(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 24) {
                model.setPro25(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 25) {
                model.setPro26(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 26) {
                model.setPro27(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 27) {
                model.setPro28(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 28) {
                model.setPro29(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 29) {
                model.setPro30(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 30) {
                model.setPro31(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 31) {
                model.setPro32(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 32) {
                model.setPro33(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 33) {
                model.setPro34(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 34) {
                model.setPro35(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 35) {
                model.setPro36(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 36) {
                model.setPro37(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 37) {
                model.setPro38(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 38) {
                model.setPro39(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 39) {
                model.setPro40(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 40) {
                model.setPro41(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 41) {
                model.setPro42(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 42) {
                model.setPro43(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 43) {
                model.setPro44(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 44) {
                model.setPro45(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 45) {
                model.setPro46(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 46) {
                model.setPro47(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 47) {
                model.setPro48(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 48) {
                model.setPro49(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 49) {
                model.setPro50(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 50) {
                model.setPro51(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 51) {
                model.setPro52(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 52) {
                model.setPro53(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 53) {
                model.setPro54(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 54) {
                model.setPro55(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 55) {
                model.setPro56(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 56) {
                model.setPro57(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 57) {
                model.setPro58(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 57) {
                model.setPro59(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 59) {
                model.setPro60(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 60) {
                model.setPro61(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 61) {
                model.setPro62(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 62) {
                model.setPro63(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 63) {
                model.setPro64(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 64) {
                model.setPro65(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 65) {
                model.setPro66(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 66) {
                model.setPro67(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 67) {
                model.setPro68(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 68) {
                model.setPro69(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 69) {
                model.setPro70(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 70) {
                model.setPro71(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 71) {
                model.setPro72(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 72) {
                model.setPro73(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 73) {
                model.setPro74(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 74) {
                model.setPro75(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 75) {
                model.setPro76(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 76) {
                model.setPro77(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 77) {
                model.setPro78(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 78) {
                model.setPro79(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 79) {
                model.setPro80(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 80) {
                model.setPro81(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 81) {
                model.setPro82(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 82) {
                model.setPro83(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 83) {
                model.setPro84(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 84) {
                model.setPro85(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 85) {
                model.setPro86(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 86) {
                model.setPro87(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 87) {
                model.setPro88(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 88) {
                model.setPro89(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 89) {
                model.setPro90(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 90) {
                model.setPro91(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 91) {
                model.setPro92(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 92) {
                model.setPro93(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 93) {
                model.setPro94(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 94) {
                model.setPro95(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 95) {
                model.setPro96(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 96) {
                model.setPro97(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 97) {
                model.setPro98(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 98) {
                model.setPro99(stringList.get(i) != null ? stringList.get(i) : "");
            } else if (i == 99) {
                model.setPro100(stringList.get(i) != null ? stringList.get(i) : "");
            }
        }
        return model;
    }
}
