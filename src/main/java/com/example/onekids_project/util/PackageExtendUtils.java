package com.example.onekids_project.util;

import com.example.onekids_project.common.PackageExtendConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.entity.finance.feesextend.FnMoneyKidsExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageDefaultExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageKidsExtend;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.model.attendance.AttendanceNumber;
import com.example.onekids_project.request.finance.extend.PackageExtendCreateRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.util.objectdata.RangeExtendModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-10-01 16:47
 *
 * @author lavanviet
 */
public class PackageExtendUtils {
    private static final String SEPARATE1 = ",";
    private static final String SEPARATE2 = "-";

    public static void checkCreateMoneyList(PackageExtendCreateRequest request) {
        request.getMoneyList().forEach(x -> {
            checkRangeInput(x.getDataList());
            checkRangeInput(x.getAbsentYesList());
            checkRangeInput(x.getAbsentNoList());
            checkRangeInput(x.getNoAttendanceList());
        });
    }

    public static void checkUpdateMoneyList(PackageExtendUpdateRequest request) {
        request.getMoneyList().forEach(x -> {
            checkRangeInput(x.getDataList());
            checkRangeInput(x.getAbsentYesList());
            checkRangeInput(x.getAbsentNoList());
            checkRangeInput(x.getNoAttendanceList());
        });
    }


    public static String convertListToString(List<RangeExtendModel> list) {
        String result = "";
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> rangeList = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            list.forEach(x -> {
                long range = x.getRange();
                long value = x.getValue();
                rangeList.add(String.valueOf(range));
                valueList.add(String.valueOf(value));
            });
            List<String> stringList = Arrays.asList(String.join(SEPARATE1, rangeList), String.join(SEPARATE1, valueList));
            result = String.join(SEPARATE2, stringList);
        }
        return result;
    }

    public static List<RangeExtendModel> convertStringToList(String str) {
        List<RangeExtendModel> modelList = new ArrayList<>();
        if (StringUtils.isNotBlank(str)) {
            String[] array = str.split(SEPARATE2);
            List<String> rangeList = new ArrayList<>(Arrays.asList(array[0].split(SEPARATE1)));
            List<String> valueList = new ArrayList<>(Arrays.asList(array[1].split(SEPARATE1)));
            for (int i = 0; i < rangeList.size(); i++) {
                RangeExtendModel model = new RangeExtendModel();
                model.setRange(Long.parseLong(rangeList.get(i)));
                model.setValue(Long.parseLong(valueList.get(i)));
                modelList.add(model);
            }
        }
        return modelList;
    }

    /**
     * for active, delete
     *
     * @param kidsList
     * @return
     */
    public static List<FnPackageDefaultExtend> getDefaultExtendFromKids(List<Kids> kidsList) {
        List<FnPackageDefaultExtend> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            List<FnPackageDefaultExtend> fnPackageDefaultExtendList = getDefaultExtendFromDefaultPackage(x.getFnKidsPackageDefaultList());
            responseList.addAll(fnPackageDefaultExtendList);
        });
        return responseList;
    }

    /**
     * for active, delete
     *
     * @param defaultPackageList
     * @return
     */
    public static List<FnPackageDefaultExtend> getDefaultExtendFromDefaultPackage(List<FnKidsPackageDefault> defaultPackageList) {
        List<FnKidsPackageDefault> fnKidsPackageDefaultList = defaultPackageList.stream().filter(a -> a.getFnPackageDefaultExtend() != null).collect(Collectors.toList());
        return fnKidsPackageDefaultList.stream().map(FnKidsPackageDefault::getFnPackageDefaultExtend).collect(Collectors.toList());
    }

    /**
     * for active, delete
     * chưa duyệt, chưa khóa, chưa thanh toán, đã có khoản đính kèm
     *
     * @param fnKidsPackageList
     * @return
     */
    public static List<FnPackageKidsExtend> getKidsExtendFromKidsPackage(List<FnKidsPackage> fnKidsPackageList) {
        fnKidsPackageList = fnKidsPackageList.stream().filter(x -> x.isDelActive() && !x.isApproved() && !x.isLocked() && x.getPaid() == 0 && x.getFnPackageKidsExtend() != null).collect(Collectors.toList());
        return fnKidsPackageList.stream().map(FnKidsPackage::getFnPackageKidsExtend).collect(Collectors.toList());
    }

    public static void checkUpdateKidsExtend(FnPackageKidsExtend fnPackageKidsExtend) {
        FnKidsPackage fnKidsPackage = fnPackageKidsExtend.getFnKidsPackage();
        if (fnKidsPackage.isApproved()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoản đã duyệt");
        }
        if (fnKidsPackage.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoản đã khóa");
        }
        if (fnKidsPackage.getPaid() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoản đã thanh toán");
        }
    }

    private static void checkRangeInput(List<RangeExtendModel> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.size() == 1) {
                if (list.get(0).getRange() == 0 || list.get(0).getValue() == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập giá trị.");
                }
            } else {
                for (int i = 1; i < list.size(); i++) {
                    if (list.get(i - 1).getRange() >= list.get(i).getRange()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhập khoảng theo thứ tự tăng dần.");
                    }
                }
            }
            if (list.stream().noneMatch(x -> x.getValue() > 0)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhập ít nhất một giá trị giảm khác 0.");
            }
        }
    }

    public static long getMoneyExtend(FnKidsPackage fnKidsPackage, long money) {
        long moneyExtend = 0;
        FnPackageKidsExtend extend = fnKidsPackage.getFnPackageKidsExtend();
        if (extend != null) {
            if (extend.isActive()) {
                List<FnMoneyKidsExtend> rangeList = extend.getFnMoneyKidsExtendList();
                for (FnMoneyKidsExtend x : rangeList) {
                    long moneyRange = 0;
                    if (x.getCategory1().equals(PackageExtendConstant.INCREASE)) {
                        moneyRange = getDataMoney(x.getTypeFixed(), x.getFixedData(), money);
                    } else if (x.getCategory1().equals(PackageExtendConstant.REDUCE)) {
                        if (x.getCategory2().equals(PackageExtendConstant.FIXED)) {
                            moneyRange = getDataMoney(x.getTypeFixed(), x.getFixedData(), money);
                        } else if (x.getCategory2().equals(PackageExtendConstant.DYNAMIC)) {
                            Kids kids = fnKidsPackage.getKids();
                            MaClass maClass = kids.getMaClass();
                            int month = fnKidsPackage.getMonth();
                            int year = fnKidsPackage.getYear();
                            if (year >= 2022) {
                                int monthPackage = fnKidsPackage.getMonth();
                                int yearPackage = fnKidsPackage.getYear();
                                LocalDate dateChange = LocalDate.of(yearPackage, monthPackage, 1);
                                dateChange = dateChange.minusMonths(1);
                                month = dateChange.getMonthValue();
                                year = dateChange.getYear();
                            }
                            List<LocalDate> dateList = FinanceUltils.getRangeDateFees(month, year);
                            LocalDate startDate = dateList.get(0);
                            LocalDate endDate = dateList.get(1);
                            int learnNumber = FinanceUltils.getAttendanceNumberLearnMonth(maClass, startDate, endDate, null);
                            AttendanceNumber attendanceNumber = AttendanceKidsUtil.getAttendanceKidsMonth(kids, maClass, month, year);
                            if (x.getCategory3().equals(PackageExtendConstant.ABSENT_ALL)) {
                                int absentNumber = attendanceNumber.getAbsentYes() + attendanceNumber.getAbsentNo() + attendanceNumber.getNoAttendance();
                                float absentNumberFloat = x.getTypeDynamic().equals(PackageExtendConstant.PERCENT) ? (float) (absentNumber * 100 / learnNumber) : absentNumber;
                                moneyRange = getMoneyRange(convertStringToList(x.getRangeALl()), x.getTypeDiscount(), absentNumberFloat, money);
                            } else if (x.getCategory3().equals(PackageExtendConstant.ABSENT_DETAIL)) {
                                //nghỉ có phép
                                float absentNumberYes = x.getTypeDynamic().equals(PackageExtendConstant.PERCENT) ? ((float) attendanceNumber.getAbsentYes() * 100 / learnNumber) : attendanceNumber.getAbsentYes();
                                long moneyYes = getMoneyRange(convertStringToList(x.getRangeAbsentYes()), x.getTypeDiscount(), absentNumberYes, money);
                                //nghỉ không phép
                                float absentNumberNo = x.getTypeDynamic().equals(PackageExtendConstant.PERCENT) ? ((float) attendanceNumber.getAbsentNo() * 100 / learnNumber) : attendanceNumber.getAbsentNo();
                                long moneyNo = getMoneyRange(convertStringToList(x.getRangeAbsentNo()), x.getTypeDiscount(), absentNumberNo, money);
                                //chưa điểm danh
                                float absentNumberNoAttendance = x.getTypeDynamic().equals(PackageExtendConstant.PERCENT) ? ((float) attendanceNumber.getNoAttendance() * 100 / learnNumber) : attendanceNumber.getNoAttendance();
                                long moneyNoAttendance = getMoneyRange(convertStringToList(x.getRangeNoAttendance()), x.getTypeDiscount(), absentNumberNoAttendance, money);
                                moneyRange = moneyYes + moneyNo + moneyNoAttendance;
                            }
                        }
                    }
                    moneyRange = x.getCategory1().equals(PackageExtendConstant.REDUCE) ? moneyRange * (-1) : moneyRange;
                    moneyExtend += moneyRange;
                }
            }
        }
        return moneyExtend;
    }

    private static long getMoneyRange(List<RangeExtendModel> extendModelList, String typeDiscount, float absentNumber, long money) {
        List<RangeExtendModel> modelList = extendModelList.stream().filter(x -> x.getRange() <= absentNumber).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(modelList)) {
            return 0;
        }
        RangeExtendModel model = modelList.get(modelList.size() - 1);
        return getDataMoney(typeDiscount, model.getValue(), money);
    }

    private static long getDataMoney(String type, long value, long money) {
        if (type.equals(PackageExtendConstant.MONEY)) {
            return value;
        } else if (type.equals(PackageExtendConstant.PERCENT)) {
            return (value * money) / 100;
        }
        return 0;
    }


}
