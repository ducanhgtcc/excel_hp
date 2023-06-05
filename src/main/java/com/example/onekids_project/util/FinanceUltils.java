package com.example.onekids_project.util;

import com.example.onekids_project.common.CycleMoneyConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.kids.AttendanceArriveKids;
import com.example.onekids_project.entity.kids.AttendanceEatKids;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.school.CycleMoney;
import com.example.onekids_project.enums.DateEnum;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.CycleMoneyRepository;
import com.example.onekids_project.repository.FnKidsPackageRepository;
import com.example.onekids_project.repository.FnOrderKidsRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageService;
import com.example.onekids_project.util.objectdata.FnMonthObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FinanceUltils {

    private static KidsRepository kidsRepository;
    private static FnOrderKidsRepository fnOrderKidsRepository;
    private static FnKidsPackageRepository fnKidsPackageRepository;
    private static CycleMoneyRepository cycleMoneyRepository;
    private static FnKidsPackageService fnKidsPackageService;

    @Autowired
    public FinanceUltils(KidsRepository kidsRepository, FnOrderKidsRepository fnOrderKidsRepository, FnKidsPackageRepository fnKidsPackageRepository, CycleMoneyRepository cycleMoneyRepository, FnKidsPackageService fnKidsPackageService) {
        FinanceUltils.kidsRepository = kidsRepository;
        FinanceUltils.fnOrderKidsRepository = fnOrderKidsRepository;
        FinanceUltils.fnKidsPackageRepository = fnKidsPackageRepository;
        FinanceUltils.cycleMoneyRepository = cycleMoneyRepository;
        FinanceUltils.fnKidsPackageService = fnKidsPackageService;
    }

    public static boolean getStatusMonth(String type, FnMonthObject fnMonthObject) {
        int month = LocalDate.now().getMonthValue();
        if (type.equals(FinanceConstant.NEXT_MONTH)) {
            month = LocalDate.now().plusMonths(1).getMonthValue();
        }
        return getStatusOfMonth(month, fnMonthObject);
    }

    public static boolean getStatusOfMonth(int month, FnMonthObject fnMonthObject) {
        switch (month) {
            case 1:
                return fnMonthObject.isT1();
            case 2:
                return fnMonthObject.isT2();
            case 3:
                return fnMonthObject.isT3();
            case 4:
                return fnMonthObject.isT4();
            case 5:
                return fnMonthObject.isT5();
            case 6:
                return fnMonthObject.isT6();
            case 7:
                return fnMonthObject.isT7();
            case 8:
                return fnMonthObject.isT8();
            case 9:
                return fnMonthObject.isT9();
            case 10:
                return fnMonthObject.isT10();
            case 11:
                return fnMonthObject.isT11();
            case 12:
                return fnMonthObject.isT12();
        }
        return false;
    }

    /**
     * lấy ra ngày từ tháng truyền vào
     *
     * @param month
     * @return
     */
//    public static LocalDate getDate(String month) {
//        LocalDate nowDate = LocalDate.now();
//        if (FinanceConstant.NOW_MONTH.equals(month)) {
//            return nowDate;
//        } else if (FinanceConstant.NEXT_MONTH.equals(month)) {
//            return nowDate.plusMonths(1);
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.DATE_NOT_FOUND);
//        }
//    }
    public static LocalDate getDateNew(String month) {
        LocalDate nowDate = LocalDate.now();
        if (FinanceConstant.PAST_MONTH.equals(month)) {
            return nowDate.minusMonths(1);
        } else if (FinanceConstant.NOW_MONTH.equals(month)) {
            return nowDate;
        } else if (FinanceConstant.NEXT_MONTH.equals(month)) {
            return nowDate.plusMonths(1);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.DATE_NOT_FOUND);
        }
    }

    /**
     * lấy tiền tính toán các khoản của học sinh
     *
     * @param dataList
     * @return
     */
    public static OrderMoneyModel getOrderMoneyKidsModel(List<FnKidsPackage> dataList) {
        OrderMoneyModel model = new OrderMoneyModel();
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<FnKidsPackage> inList = dataList.stream().filter(x -> FinanceConstant.CATEGORY_IN.equals(x.getFnPackage().getCategory())).collect(Collectors.toList());
            List<FnKidsPackage> outList = dataList.stream().filter(x -> FinanceConstant.CATEGORY_OUT.equals(x.getFnPackage().getCategory())).collect(Collectors.toList());
            model.setMoneyTotalIn(inList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum());
            model.setMoneyTotalPaidIn(inList.stream().mapToDouble(FnKidsPackage::getPaid).sum());
            model.setMoneyTotalRemainIn(model.getMoneyTotalIn() - model.getMoneyTotalPaidIn());
            model.setMoneyTotalOut(outList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum());
            model.setMoneyTotalPaidOut(outList.stream().mapToDouble(FnKidsPackage::getPaid).sum());
            model.setMoneyTotalRemainOut(model.getMoneyTotalOut() - model.getMoneyTotalPaidOut());

            model.setMoneyTotalInOut(model.getMoneyTotalIn() - model.getMoneyTotalOut());
            model.setMoneyTotalInOutPaid(model.getMoneyTotalPaidIn() - model.getMoneyTotalPaidOut());
            model.setMoneyTotalInOutRemain(model.getMoneyTotalRemainIn() - model.getMoneyTotalRemainOut());
        }
        return model;
    }

    /**
     * lấy tiền tính toán các khoản của nhân sự
     *
     * @param dataList
     * @return
     */
    public static OrderMoneyModel getOrderMoneyEmployeeModel(List<FnEmployeeSalary> dataList) {
        OrderMoneyModel model = new OrderMoneyModel();
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<FnEmployeeSalary> inList = dataList.stream().filter(x -> FinanceConstant.CATEGORY_IN.equals(x.getCategory())).collect(Collectors.toList());
            List<FnEmployeeSalary> outList = dataList.stream().filter(x -> FinanceConstant.CATEGORY_OUT.equals(x.getCategory())).collect(Collectors.toList());
            model.setMoneyTotalIn(inList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum());
            model.setMoneyTotalPaidIn(inList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum());
            model.setMoneyTotalRemainIn(model.getMoneyTotalIn() - model.getMoneyTotalPaidIn());
            model.setMoneyTotalOut(outList.stream().mapToDouble(FinanceUltils::getMoneySalary).sum());
            model.setMoneyTotalPaidOut(outList.stream().mapToDouble(FnEmployeeSalary::getPaid).sum());
            model.setMoneyTotalRemainOut(model.getMoneyTotalOut() - model.getMoneyTotalPaidOut());

            model.setMoneyTotalInOut(model.getMoneyTotalOut() - model.getMoneyTotalIn());
            model.setMoneyTotalInOutPaid(model.getMoneyTotalPaidOut() - model.getMoneyTotalPaidIn());
            model.setMoneyTotalInOutRemain(model.getMoneyTotalRemainOut() - model.getMoneyTotalRemainIn());
        }
        return model;
    }

    /**
     * tính toán trạng thái các khoản trong hóa đơn của nhân sự
     *
     * @param fnEmployeeSalaryList
     * @return
     */
    public static OrderNumberModel getNumberOrderEmployeeModel(List<FnEmployeeSalary> fnEmployeeSalaryList) {
        OrderNumberModel model = new OrderNumberModel();
        long countEmpty = fnEmployeeSalaryList.stream().filter(x -> x.getPaid() == 0).count();
        long countPart = fnEmployeeSalaryList.stream().filter(x -> x.getPaid() > 0 && x.getPaid() < getMoneySalary(x)).count();
        long countEnough = fnEmployeeSalaryList.stream().filter(x -> x.getPaid() > 0 && x.getPaid() >= getMoneySalary(x)).count();
        model.setEmptyNumber((int) countEmpty);
        model.setPartNumber((int) countPart);
        model.setEnoughNumber((int) countEnough);
        model.setTotalNumber(fnEmployeeSalaryList.size());
        checkNumberModel(model);
        return model;
    }

    /**
     * tính toán trạng thái các khoản trong hóa đơn của học sinh
     *
     * @param fnKidsPackageList
     * @return
     */
    public static OrderNumberModel getNumberOrderModel(List<FnKidsPackage> fnKidsPackageList) {
        OrderNumberModel model = new OrderNumberModel();
        long countEmpty = fnKidsPackageList.stream().filter(x -> x.getPaid() == 0).count();
        long countPart = fnKidsPackageList.stream().filter(x -> x.getPaid() > 0 && x.getPaid() < getMoneyCalculate(x)).count();
        long countEnough = fnKidsPackageList.stream().filter(x -> x.getPaid() > 0 && x.getPaid() >= getMoneyCalculate(x)).count();
        model.setEmptyNumber((int) countEmpty);
        model.setPartNumber((int) countPart);
        model.setEnoughNumber((int) countEnough);
        model.setTotalNumber(fnKidsPackageList.size());
        checkNumberModel(model);
        return model;
    }

    private static void checkNumberModel(OrderNumberModel model) {
        if (model.getTotalNumber() != (model.getEmptyNumber() + model.getPartNumber() + model.getEnoughNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số khoản không đúng với tổng số");
        }
    }


    /**
     * lấy trạng thái của một danh sách khoản
     * true là có ít nhất 1 khoản chưa hoàn thành
     *
     * @param fnKidsPackageList
     * @return
     */
    public static boolean getOrderStatus(List<FnKidsPackage> fnKidsPackageList) {
        long count = fnKidsPackageList.stream().filter(x -> x.getPaid() == 0 || x.getPaid() < getMoneyCalculate(x)).count();
        return count > 0;
    }

    /**
     * lấy trạng thái của một danh sách khoản
     * true là có ít nhất 1 khoản chưa hoàn thành
     *
     * @param fnEmployeeSalaryList
     * @return
     */
    public static boolean getOrderStatusEmployee(List<FnEmployeeSalary> fnEmployeeSalaryList) {
        long count = fnEmployeeSalaryList.stream().filter(x -> x.getPaid() == 0 || x.getPaid() < getMoneySalary(x)).count();
        return count > 0;
    }

    public static long getMoneyCalculateDynamic(FnKidsPackage fnKidsPackage) {
        long moneyFinal;
        long moneyPackage = getMoneyPackageDynamic(fnKidsPackage);
        long moneyExtend = PackageExtendUtils.getMoneyExtend(fnKidsPackage, moneyPackage);
        moneyFinal = moneyPackage + moneyExtend;
        return moneyFinal < 0 ? 0 : moneyFinal;
    }

    private static long getMoneyPackageDynamic(FnKidsPackage fnKidsPackage) {
        long money = 0;
        if (fnKidsPackage.getNumber() <= 0) {
            return money;
        }
        double price = getPriceData(fnKidsPackage);
        int numberDynamic = fnKidsPackageService.getCalculateNumber(fnKidsPackage.getKids(), fnKidsPackage);
        money = (long) ((numberDynamic * price) / fnKidsPackage.getNumber());
        return money;
    }


    /**
     * thành tiền của một khoản học sinh
     *
     * @param fnKidsPackage
     * @return
     */
    public static long getMoneyCalculate(FnKidsPackage fnKidsPackage) {
        long moneyFinal;
        long moneyPackage = getMoneyPackage(fnKidsPackage);
        long moneyExtend = PackageExtendUtils.getMoneyExtend(fnKidsPackage, moneyPackage);
        moneyFinal = moneyPackage + moneyExtend;
        return moneyFinal < 0 ? 0 : moneyFinal;
    }


    /**
     * khoản tiền của một gói
     *
     * @param fnKidsPackage
     * @return
     */
    private static long getMoneyPackage(FnKidsPackage fnKidsPackage) {
        long money = 0;
        if (fnKidsPackage.getNumber() <= 0) {
            return money;
        }
        double price = getPriceData(fnKidsPackage);
        money = (long) ((fnKidsPackage.getUsedNumber() * price) / fnKidsPackage.getNumber());
        return money;
    }

    /**
     * Số tiền biến động lấy từ khoản đính kèm sau khi tính toán với khoản gốc
     *
     * @param fnKidsPackage
     * @return
     */
    public static long getMoneyExtendFinal(FnKidsPackage fnKidsPackage) {
        long moneyPackage = getMoneyPackage(fnKidsPackage);
        long moneyExtend = PackageExtendUtils.getMoneyExtend(fnKidsPackage, moneyPackage);
        long money = moneyPackage + moneyExtend;
        return money > 0 ? moneyExtend : moneyPackage * (-1);
    }

    /**
     * tính toán số tiền theo số lượng truyền vào
     *
     * @param fnKidsPackage
     * @param number
     * @return
     */
    public static long getMoneyWidthNumber(FnKidsPackage fnKidsPackage, int number) {
        long money = 0;
        double price = getPriceData(fnKidsPackage);
        if (fnKidsPackage.getNumber() > 0) {
            money = (long) ((number * price) / fnKidsPackage.getNumber());
        }
        return money;
    }

    /**
     * tính toán số tiền theo số lượng truyền vào employee
     *
     * @param fnEmployeeSalary
     * @param number
     * @return
     */
    public static double getMoneyWidthNumberEmployee(FnEmployeeSalary fnEmployeeSalary, float number) {
        double money = 0;
        double price = getPriceDataEmployee(fnEmployeeSalary);
        if (fnEmployeeSalary.getNumber() > 0) {
            money = ((number * price) / fnEmployeeSalary.getNumber());
        }
        return money;
    }


    /**
     * thành tiền của một khoản công lương nhân sự
     *
     * @param fnEmployeeSalary
     * @return
     */
    public static long getMoneySalary(FnEmployeeSalary fnEmployeeSalary) {
        long money = 0;
        double price = getPriceDataSalary(fnEmployeeSalary);
        if (fnEmployeeSalary.getNumber() > 0) {
            money = (long) ((fnEmployeeSalary.getUserNumber() * price) / fnEmployeeSalary.getNumber());
        }
        return money;
    }

    /**
     * đơn giá tiền của một khoản thu
     *
     * @param fnEmployeeSalary
     * @return
     */
    public static double getPriceDataSalary(FnEmployeeSalary fnEmployeeSalary) {
        return fnEmployeeSalary.isDiscount() ? fnEmployeeSalary.getDiscountPrice() : fnEmployeeSalary.getPrice();
    }

    /**
     * đơn giá tiền của một khoản thu
     *
     * @param fnKidsPackage
     * @return
     */
    public static double getPriceData(FnKidsPackage fnKidsPackage) {
        return fnKidsPackage.isDiscount() ? fnKidsPackage.getDiscountPrice() : fnKidsPackage.getPrice();
    }

    /**
     * đơn giá tiền của một khoản thu employee
     *
     * @param fnEmployeeSalary
     * @return
     */
    public static double getPriceDataEmployee(FnEmployeeSalary fnEmployeeSalary) {
        return fnEmployeeSalary.isDiscount() ? fnEmployeeSalary.getDiscountPrice() : fnEmployeeSalary.getPrice();
    }

    /**
     * lấy ra ví từ một học sinh
     *
     * @param kids
     * @return
     */
    public static WalletParent getWalletParentFromKids(Kids kids) {
        if (kids.getParent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh chưa có tài khoản");
        }
        List<WalletParent> walletParentList = kids.getParent().getWalletParentList().stream().filter(a -> a.getIdSchool().equals(kids.getIdSchool())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(walletParentList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh không có ví nào");
        } else if (walletParentList.size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Có nhiều hơn một ví");
        }
        return walletParentList.get(0);
    }

    /**
     * số dư ví
     *
     * @param walletParent
     * @return
     */
    public static double getWalletParentBalance(WalletParent walletParent) {
        return walletParent.getMoneyIn() - walletParent.getMoneyOut();
    }

    /**
     * check số dư trong ví khi rút tiền từ ví
     *
     * @param walletParent
     * @param moneyOut
     */
    public static void checkBalanceWalletParent(WalletParent walletParent, double moneyOut) {
        double moneyRemain = walletParent.getMoneyIn() - walletParent.getMoneyOut();
        if (moneyOut > moneyRemain) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.WALLET_BALANCE);
        }
    }

    /**
     * lấy ngày hết hạn của khoản thu học sinh
     *
     * @param fnKidsPackage
     * @return
     */
    public static LocalDate getExpireDate(FnKidsPackage fnKidsPackage) {
        LocalDate date = null;
        if (fnKidsPackage.getFnPackage().getType().equals(FinanceConstant.TYPE_SINGLE)) {
            date = fnKidsPackage.getEndDateExpired();
        } else if (fnKidsPackage.getFnPackage().getType().equals(FinanceConstant.TYPE_MULTIPLE)) {
            int dateNumber;
            LocalDate expireMonth = LocalDate.now();
            //tháng kế tiếp
            if (fnKidsPackage.getMonthNumber() == 1) {
                expireMonth = expireMonth.plusMonths(1);
            }
            int month = expireMonth.getMonthValue();
            if (month == 2) {
                dateNumber = fnKidsPackage.getFebNumberExpired();
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                dateNumber = fnKidsPackage.getSmallNumberExpired();
            } else {
                dateNumber = fnKidsPackage.getLargeNumberExpired();
            }
            date = LocalDate.of(expireMonth.getYear(), expireMonth.getMonthValue(), dateNumber);
        }
        return date;
    }

//    public static void checkDateGenerateOrder(LocalDate date) {
//        LocalDate nowDate = LocalDate.now();
//        LocalDate startDate = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), 1).minusMonths(FinanceConstant.ORDER_MONTH_PAST);
//        LocalDate endDate = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), 1).plusMonths(FinanceConstant.ORDER_MONTH_FUTURE).minusDays(1);
//        if (date.isBefore(startDate)) {
//            int number = FinanceConstant.ORDER_MONTH_PAST + 1;
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được khởi tạo hóa đơn trước đó " + number + " tháng");
//        }
//        if (date.isAfter(endDate)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không được khởi tạo hóa đơn sau đó " + FinanceConstant.ORDER_MONTH_FUTURE + " tháng");
//        }
//    }


    public static List<FnKidsPackage> getFnKidsPackageFromFnPackageAndMonthYear(FnPackage fnPackage, int startMonth, int endMonth, int year) {
        return fnPackage.getFnKidsPackageList().stream().filter(x -> x.isDelActive() && x.isActive() && x.isApproved() && x.getMonth() >= startMonth && x.getMonth() <= endMonth && x.getYear() == year).collect(Collectors.toList());
    }

    public static List<FnEmployeeSalary> getFnEmployeeSalaryFromFnSalaryAndMonthYear(List<FnEmployeeSalary> fnEmployeeSalaryList, int startMonth, int endMonth, int year) {
        return fnEmployeeSalaryList.stream().filter(x -> x.isDelActive() && x.isApproved() && x.getMonth() >= startMonth && x.getMonth() <= endMonth && x.getYear() == year).collect(Collectors.toList());
    }

    /**
     * lấy các khoản chưa bị xóa học sinh theo tháng
     *
     * @param kids
     * @param date
     * @param active   true là đã đăng ký
     * @param approved true là đã duyệt
     * @return
     */
    public static List<FnKidsPackage> getKidsPackageListFromKid(Kids kids, LocalDate date, boolean active, boolean approved) {
        List<FnKidsPackage> fnKidsPackageList = kids.getFnKidsPackageList().stream().filter(x -> x.isDelActive() && x.getMonth() == date.getMonthValue() && x.getYear() == date.getYear()).collect(Collectors.toList());
        if (active) {
            fnKidsPackageList = fnKidsPackageList.stream().filter(FnKidsPackage::isActive).collect(Collectors.toList());
        }
        if (approved) {
            fnKidsPackageList = fnKidsPackageList.stream().filter(FnKidsPackage::isApproved).collect(Collectors.toList());
        }
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        return fnKidsPackageList;
    }

    public static List<FnKidsPackage> getKidsPackageListFromKidBefore(Kids kids, LocalDate date, boolean active, boolean approved) {
        List<FnKidsPackage> fnKidsPackageList = kids.getFnKidsPackageList().stream().filter(x -> x.isDelActive() && ((x.getMonth() < date.getMonthValue() && x.getYear() == date.getYear()) || x.getYear() < date.getYear())).collect(Collectors.toList());
        if (active) {
            fnKidsPackageList = fnKidsPackageList.stream().filter(FnKidsPackage::isActive).collect(Collectors.toList());
        }
        if (approved) {
            fnKidsPackageList = fnKidsPackageList.stream().filter(FnKidsPackage::isApproved).collect(Collectors.toList());
        }
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        return fnKidsPackageList;
    }

    public static List<FnKidsPackage> getKidsPackageFromKidDate(Kids kids, LocalDate date) {
        return kids.getFnKidsPackageList().stream().filter(x -> x.isDelActive() && x.getMonth() == date.getMonthValue() && x.getYear() == date.getYear()).collect(Collectors.toList());
    }

    /**
     * lấy các khoản chưa bị xóa nhân sự theo tháng
     *
     * @param infoEmployeeSchool
     * @param date
     * @param approved           true khoản đã duyệt
     * @return
     */
    public static List<FnEmployeeSalary> getEmployeeSalaryListFromInfoEmployee(InfoEmployeeSchool infoEmployeeSchool, LocalDate date, boolean approved) {
        List<FnEmployeeSalary> fnEmployeeSalaryList = infoEmployeeSchool.getFnEmployeeSalaryList().stream().filter(x -> x.isDelActive() && x.getMonth() == date.getMonthValue() && x.getYear() == date.getYear()).collect(Collectors.toList());
        if (approved) {
            fnEmployeeSalaryList = fnEmployeeSalaryList.stream().filter(FnEmployeeSalary::isApproved).collect(Collectors.toList());
        }
        fnEmployeeSalaryList = fnEmployeeSalaryList.stream().sorted(Comparator.comparing(FnEmployeeSalary::getCategory).reversed()).collect(Collectors.toList());
        return fnEmployeeSalaryList;
    }

    public static String getMessageGenerate(int number) {
        if (number <= 0) {
            return "Không có khoản nào được khởi tạo";
        } else {
            return "Khởi tạo thành công " + number + " khoản";
        }
    }

    /**
     * danh sách các ngày chủ nhật
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<LocalDate> getSundayList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            DayOfWeek dayOfWeek = startDate.getDayOfWeek();
            if (dayOfWeek.toString().equals(DateEnum.SUNDAY.name())) {
                list.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        return list;
    }

    /**
     * danh sách các ngày thứ 7 và CN
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<LocalDate> getSaturdaySundayList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            DayOfWeek dayOfWeek = startDate.getDayOfWeek();
            if (StringUtils.equalsAny(dayOfWeek.toString(), DateEnum.SATURDAY.name(), DateEnum.SUNDAY.name())) {
                list.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        return list;
    }

    /**
     * danh sách các ngày khác thứ 7
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<LocalDate> getAllIgnoreSaturdayList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> list = new ArrayList<>();
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            DayOfWeek dayOfWeek = startDate.getDayOfWeek();
            if (!dayOfWeek.toString().equals(DateEnum.SATURDAY.name())) {
                list.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        return list;
    }


    /**
     * lấy số ngày đi học của tháng trong 1 lớp
     *
     * @param maClass
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getAttendanceNumberLearnMonth(MaClass maClass, LocalDate startDate, LocalDate endDate, List<LocalDate> ignoreDateList) {
        int lengthOfMonth = (int) startDate.datesUntil(endDate).count() + 1;
        List<LocalDate> absentList = getAbsentDateInClass(maClass, startDate, endDate);
        ignoreDateList = CollectionUtils.isEmpty(ignoreDateList) ? new ArrayList<>() : ignoreDateList;
        List<LocalDate> absentConcatList = ListUtils.union(absentList, ignoreDateList);
        absentConcatList = absentConcatList.stream().distinct().collect(Collectors.toList());
        return lengthOfMonth - absentConcatList.size();
    }

    /**
     * sanh sách các ngày nghỉ trong tháng tính cả những ngày ignoreDateList
     *
     * @param maClass
     * @param startDate
     * @param endDate
     * @param ignoreDateList
     * @return
     */
    public static List<LocalDate> getAttendanceNumberLearnMonthDateList(MaClass maClass, LocalDate startDate, LocalDate endDate, List<LocalDate> ignoreDateList) {
        List<LocalDate> absentList = getAbsentDateInClass(maClass, startDate, endDate);
        ignoreDateList = CollectionUtils.isEmpty(ignoreDateList) ? new ArrayList<>() : ignoreDateList;
        List<LocalDate> absentConcatList = ListUtils.union(absentList, ignoreDateList);
        absentConcatList = absentConcatList.stream().distinct().collect(Collectors.toList());
        return absentConcatList;
    }

    public static int getAttendanceNumberLearn27(MaClass maClass, LocalDate startDate, LocalDate endDate, List<LocalDate> ignoreDateList) {
        int lengthOfMonth = (int) startDate.datesUntil(endDate).count() + 1;
        List<LocalDate> absentList = getAbsentDateInClass(maClass, startDate, endDate);
        absentList = absentList.stream().filter(x -> !x.getDayOfWeek().toString().equals(DateEnum.SATURDAY.name())).collect(Collectors.toList());

        ignoreDateList = CollectionUtils.isEmpty(ignoreDateList) ? new ArrayList<>() : ignoreDateList;
        List<LocalDate> absentConcatList = ListUtils.union(absentList, ignoreDateList);
        absentConcatList = absentConcatList.stream().distinct().collect(Collectors.toList());
        return lengthOfMonth - absentConcatList.size();
    }

    private static List<LocalDate> getAbsentDateConfig(MaClass maClass, LocalDate startDate, LocalDate endDate, List<LocalDate> absentDateList) {
        List<LocalDate> list;
        List<LocalDate> absentList = getAbsentDateInClass(maClass, startDate, endDate);
        list = absentList;
        if (CollectionUtils.isNotEmpty(absentDateList)) {
            list = CollectionUtils.union(absentList, absentDateList).stream().distinct().collect(Collectors.toList());
        }
        return list;
    }

    /**
     * lấy lại khoảng ngày khi rơi vào ngày đầu tháng
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<LocalDate> getDateSameMonth(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate startD = startDate;
        LocalDate endD = endDate;
        if (startDate.getYear() == endDate.getYear() && startDate.getMonthValue() == endDate.getMonthValue()) {
            startD = startDate.minusMonths(1);
            endD = startDate.minusDays(1);
        } else {
            startD = startDate.minusMonths(1);
            endD = endDate.minusMonths(1);
        }
        dateList.add(startD);
        dateList.add(endD);
        return dateList;
    }

    public static int getAttendanceNumberNot(Kids kids, LocalDate startDate, LocalDate endDate, String attendanceDetail) {
        int number = 0;
        //trừ đi 1 tháng để tính tiền trả lại cho tháng trước
//        LocalDate date = LocalDate.of(year, month, 1).minusMonths(1);
//        int monthCal = date.getMonthValue();
//        int yearCal = date.getYear();
        List<LocalDate> dateList = getDateSameMonth(startDate, endDate);
        LocalDate startDateCal = dateList.get(0);
        LocalDate endDateCal = dateList.get(1);
        //số ngày học trong tháng
        int learnOfMonth = getAttendanceNumberLearnMonth(kids.getMaClass(), startDateCal, endDateCal, null);
        List<AttendanceKids> attendanceKidsList = kids.getAttendanceKidsList().stream().filter(x -> x.getAttendanceDate().isAfter(startDateCal.minusDays(1)) && x.getAttendanceDate().isBefore(endDateCal.plusDays(1))).collect(Collectors.toList());
        List<AttendanceEatKids> eatList = attendanceKidsList.stream().map(AttendanceKids::getAttendanceEatKids).collect(Collectors.toList());
        //bỏ đi những ngày ko đi học
        List<AttendanceArriveKids> arriveList = attendanceKidsList.stream().map(AttendanceKids::getAttendanceArriveKids).collect(Collectors.toList());
        //lấy những học sinh ko đi học buổi nào trong ngày
        arriveList = arriveList.stream().filter(x -> !x.isMorning() && !x.isAfternoon() && !x.isEvening()).collect(Collectors.toList());
        switch (attendanceDetail) {
            //không ăn sáng loại 1
            case FinanceConstant.EAT_BREAKFAST_OUT_1:
                int eatBreakfastOut1 = (int) eatList.stream().filter(AttendanceEatKids::isBreakfast).count();
                number = learnOfMonth - eatBreakfastOut1;
                break;
            //không ăn các bữa còn lại loại 1
            case FinanceConstant.EAT_REMAIN_OUT_1:
                int eatRemainOut1 = (int) eatList.stream().filter(x -> x.isSecondBreakfast() || x.isLunch() || x.isAfternoon() || x.isSecondAfternoon() || x.isDinner()).count();
                number = learnOfMonth - eatRemainOut1;
                break;
            case FinanceConstant.EAT_DAY_OUT_2:
                int eatDayOut2 = (int) eatList.stream().filter(x -> x.isBreakfast() || x.isSecondBreakfast() || x.isLunch() || x.isAfternoon() || x.isSecondAfternoon() || x.isDinner()).count();
                number = learnOfMonth - eatDayOut2;
                break;
            case FinanceConstant.EAT_BREAKFAST_OUT_2:
                //có ăn ít nhất 1 bữa trong ngày, vì nếu ko ăn bữa nào thì rơi vào cái EAT_DAY_OUT_2
                eatList = eatList.stream().filter(x -> x.isBreakfast() || x.isSecondBreakfast() || x.isLunch() || x.isAfternoon() || x.isSecondAfternoon() || x.isDinner()).collect(Collectors.toList());
                int eatBreakfastOut2 = (int) eatList.stream().filter(AttendanceEatKids::isBreakfast).count();
                number = learnOfMonth - eatBreakfastOut2;
                break;
            case FinanceConstant.EAT_REPAY_DINNER26:
                //trả lại tiền ăn tối từ thứ 2 -6
                List<LocalDate> saturdaySundayList = FinanceUltils.getSaturdaySundayList(startDateCal, endDateCal);
                List<LocalDate> absentDateList = getAttendanceNumberLearnMonthDateList(kids.getMaClass(), startDateCal, endDateCal, saturdaySundayList);
                List<AttendanceEatKids> eatNoDinnerMonthList = eatList.stream().filter(a -> !a.isDinner()).collect(Collectors.toList());
                eatNoDinnerMonthList = eatNoDinnerMonthList.stream().filter(a -> absentDateList.stream().noneMatch(b -> b.equals(a.getAttendanceKids().getAttendanceDate()))).collect(Collectors.toList());
                number = eatNoDinnerMonthList.size();
                break;
            case FinanceConstant.ARRIVE_ABSENT_YES:
                //ko đi học và nghỉ ít nhất một ngày có phép
                number = (int) arriveList.stream().filter(x -> x.isMorningYes() || x.isAfternoonYes() || x.isEveningYes()).count();
                break;
            case FinanceConstant.ARRIVE_ABSENT_NO:
                //ko đi học, ko nghỉ có phép và có ít nhất 1 buổi nghỉ ko phép
                arriveList = arriveList.stream().filter(x -> !x.isMorningYes() && !x.isAfternoonYes() && !x.isEveningYes()).collect(Collectors.toList());
                number = (int) arriveList.stream().filter(x -> x.isMorningNo() || x.isAfternoonNo() || x.isEveningNo()).count();
                break;
            case FinanceConstant.ARRIVE_ABSENT_YES_NO27:
            case FinanceConstant.ARRIVE_ABSENT_YES_NO26:
            case FinanceConstant.ARRIVE_ABSENT_YES_NO7:
                List<LocalDate> list1;
                if (attendanceDetail.equals(FinanceConstant.ARRIVE_ABSENT_YES_NO27)) {
                    list1 = getSundayList(startDateCal, endDateCal);
                } else if (attendanceDetail.equals(FinanceConstant.ARRIVE_ABSENT_YES_NO26)) {
                    list1 = getSaturdaySundayList(startDateCal, endDateCal);
                } else {
                    list1 = getAllIgnoreSaturdayList(startDateCal, endDateCal);
                }
                List<LocalDate> ignoreList1 = getAbsentDateConfig(kids.getMaClass(), startDateCal, endDateCal, list1);
                List<AttendanceKids> attendanceList1 = attendanceKidsList.stream().filter(x -> ignoreList1.stream().noneMatch(a -> a.equals(x.getAttendanceDate()))).collect(Collectors.toList());
                List<AttendanceArriveKids> arriveList1 = attendanceList1.stream().map(AttendanceKids::getAttendanceArriveKids).collect(Collectors.toList());
                number = (int) arriveList1.stream().filter(x -> (x.isMorningYes() || x.isMorningNo()) && (x.isAfternoonYes() || x.isAfternoonNo())).count();
                break;
            case FinanceConstant.ARRIVE_ABSENT_YES26:
            case FinanceConstant.ARRIVE_ABSENT_YES7:
                List<LocalDate> list2;
                if (attendanceDetail.equals(FinanceConstant.ARRIVE_ABSENT_YES26)) {
                    list2 = getSaturdaySundayList(startDateCal, endDateCal);
                } else {
                    list2 = getAllIgnoreSaturdayList(startDateCal, endDateCal);
                }
                List<LocalDate> ignoreList2 = getAbsentDateConfig(kids.getMaClass(), startDateCal, endDateCal, list2);
                List<AttendanceKids> attendanceList2 = attendanceKidsList.stream().filter(x -> ignoreList2.stream().noneMatch(a -> a.equals(x.getAttendanceDate()))).collect(Collectors.toList());
                List<AttendanceArriveKids> arriveList2 = attendanceList2.stream().map(AttendanceKids::getAttendanceArriveKids).collect(Collectors.toList());
                number = (int) arriveList2.stream().filter(x -> (x.isMorningYes()) && (x.isAfternoonYes())).count();
                break;
        }
        return number;
    }

    public static List<LocalDate> getRangeDateFees(int month, int year) {
        List<LocalDate> list = new ArrayList<>();
        LocalDate startDate;
        LocalDate endDate;
        int startNumber = 1;
        int endNumber = 1;
        Long idSchool = SchoolUtils.getIdSchool();
        CycleMoney cycleMoney = cycleMoneyRepository.findBySchoolId(idSchool).orElseThrow();
        if (cycleMoney.getTypeFees().equals(CycleMoneyConstant.TYPE_DEFAULT)) {
            startDate = LocalDate.of(year, month, startNumber);
            endDate = LocalDate.of(year, month, endNumber).plusMonths(1).minusDays(1);
        } else {
            startNumber = cycleMoney.getStartDateFees();
            endNumber = cycleMoney.getEndDateFees();
            if (cycleMoney.getRangeFees().equals(CycleMoneyConstant.RANGE1)) {
                startDate = LocalDate.of(year, month, startNumber).minusMonths(1);
                endDate = LocalDate.of(year, month, endNumber).minusDays(1);
            } else {
                startDate = LocalDate.of(year, month, startNumber);
                endDate = LocalDate.of(year, month, endNumber).plusMonths(1).minusDays(1);
            }
        }
        list.add(startDate);
        list.add(endDate);
        return list;
    }

    //todo co thể bổ sung [a,b] như học sinh
    public static List<LocalDate> getRangeDateSalary(int month, int year) {
        List<LocalDate> list = new ArrayList<>();
        LocalDate startDate;
        LocalDate endDate;
        int startNumber = 1;
        int endNumber = 1;
        Long idSchool = SchoolUtils.getIdSchool();
        CycleMoney cycleMoney = cycleMoneyRepository.findBySchoolId(idSchool).orElseThrow();
        if (cycleMoney.getTypeSalary().equals(CycleMoneyConstant.TYPE_DEFAULT)) {
            startDate = LocalDate.of(year, month, startNumber);
            endDate = LocalDate.of(year, month, endNumber).plusMonths(1);
        } else {
            startNumber = cycleMoney.getStartDateSalary();
            endNumber = cycleMoney.getEndDateSalary();
            if (cycleMoney.getRangeSalary().equals(CycleMoneyConstant.RANGE1)) {
                startDate = LocalDate.of(year, month, startNumber).minusMonths(1);
                endDate = LocalDate.of(year, month, endNumber);
            } else {
                startDate = LocalDate.of(year, month, startNumber);
                endDate = LocalDate.of(year, month, endNumber).plusMonths(1);
            }
        }
        list.add(startDate);
        list.add(endDate);
        return list;
    }

    /**
     * format money
     *
     * @param money
     * @return
     */
    public static String formatMoney(long money) {
        return String.format("%,d", money).replace(",", ".");
    }

    public static String getDateString(int startMonth, int endMonth, int year) {
        String startMonthString = startMonth > 10 ? String.valueOf(startMonth) : "0" + startMonth;
        String endMonthString = endMonth > 10 ? String.valueOf(endMonth) : "0" + endMonth;
        return "Thời gian: Từ tháng " + startMonthString + " đến tháng " + endMonthString + " năm " + year;
    }

    /**
     * lấy khoản thu học sinh từ idKid và idOrder
     *
     * @param idKid
     * @param idOrder
     * @param category
     * @return
     */
    public static List<FnKidsPackage> getFnKidsPackageInOrder(Long idKid, Long idOrder, String category) {
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(idOrder).orElseThrow();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        List<FnKidsPackage> fnKidsPackageList = category.equals(FinanceConstant.CATEGOTY_BOTH) ? fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(kids.getId(), fnOrderKids.getMonth(), fnOrderKids.getYear()) : fnKidsPackageRepository.findByKidsIdAndFnPackageCategoryAndMonthAndYearAndApprovedTrueAndDelActiveTrue(kids.getId(), category, fnOrderKids.getMonth(), fnOrderKids.getYear());
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        return fnKidsPackageList;
    }

    /**
     * lấy danh sách ngày nghỉ của một lớp theo startDate<= x <=endDate
     *
     * @param maClass
     * @param startDate
     * @param endDate
     * @return
     */
    private static List<LocalDate> getAbsentDateInClass(MaClass maClass, LocalDate startDate, LocalDate endDate) {
        return maClass.getDayOffClassList().stream().filter(x -> x.isDelActive() && x.getDate().isAfter(startDate.minusDays(1)) && x.getDate().isBefore(endDate.plusDays(1))).map(DayOffClass::getDate).collect(Collectors.toList());
    }

}
