package com.example.onekids_project.controller.finance;

import com.example.onekids_project.request.finance.statistical.FinanceSearchKidsRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.ExportImportFinanceService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-03-20 10:23
 *
 * @author Nguyễn Thành
 */
@RestController
@RequestMapping("/web/fn/export-import")
public class ExportImportFinanceController {

    @Autowired
    private ExportImportFinanceService exportImportFinanceService;

    /**
     * Danh sách học sinh chưa hoàn thành khoản thu
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-in")
    public ResponseEntity exportFinanceKidIn(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        ByteArrayInputStream in = exportImportFinanceService.exportFinanceKid(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    /**
     * Danh sách học sinh chưa hoàn thành khoản thu NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-in-new")
    public ResponseEntity exportFinanceKidInNew(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelResponse> list = exportImportFinanceService.exportFinanceKidNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * Danh sách học sinh chưa hoàn thành khoản chi
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-out")
    public ResponseEntity exportFinanceKidOut(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        ByteArrayInputStream in = exportImportFinanceService.exportFinanceKidOut(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    /**
     * Danh sách học sinh chưa hoàn thành khoản chi NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-out-new")
    public ResponseEntity exportFinanceKidOutNew(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelResponse> list = exportImportFinanceService.exportFinanceKidOutNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * Danh sách học sinh có hóa đơn chưa hoàn thành
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-bill")
    public ResponseEntity exportFinanceKidInAndOut(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        ByteArrayInputStream in = exportImportFinanceService.exportFinanceKidInOut(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    /**
     * Danh sách học sinh có hóa đơn chưa hoàn thành NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-bill-new")
    public ResponseEntity exportFinanceKidInAndOutNew(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request){
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelResponse> list = exportImportFinanceService.exportFinanceKidInOutNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * Danh sachs thu chi tổng hợp của học sinh
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-in-out")
    public ResponseEntity exportFinanceKidInOut(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        ByteArrayInputStream in = exportImportFinanceService.exportFinanceKidInAndOut(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    /**
     * Danh sachs thu chi tổng hợp của học sinh chưa hoàn thành NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-in-out-false-new")
    public ResponseEntity exportFinanceKidInOutNew(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelResponse> list = exportImportFinanceService.exportFinanceKidInAndOutNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * Danh sachs thu chi tổng hợp của học sinh đã hoàn thành NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-in-out-true-new")
    public ResponseEntity exportFinanceKidInOutTrueNew(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelResponse> list = exportImportFinanceService.exportFinanceKidInAndOutTrueNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * Danh sachs thu chi tổng hợp của học sinh NEW
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-in-out-new")
    public ResponseEntity exportFinanceKidInOutTotalNew(@CurrentUser UserPrincipal principal, @Valid FinanceSearchKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelResponse> list = exportImportFinanceService.exportFinanceKidInAndOutTotalNew(principal, request);
        return NewDataResponse.setDataSearch(list);
    }

}
