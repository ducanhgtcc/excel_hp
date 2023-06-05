package com.example.onekids_project.controller;

import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.cronjobs.AccountCronjob;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.cronjobs.EvaluateKidsCronjobs;
import com.example.onekids_project.dto.MaUserDTO;
import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.entity.employee.EmployeeStatusTimeline;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsStatisticalService;
import com.example.onekids_project.service.servicecustom.chart.ChartKidsService;
import com.example.onekids_project.service.servicecustom.finance.ExportImportFinanceService;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/web/test")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private EmployeeStatusTimelineRepository employeeStatusTimelineRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private AttendanceEmployeeRepository attendanceEmployeeRepository;

    @Autowired
    private AttendanceKidsStatisticalService attendanceKidsStatisticalService;

    @Autowired
    private ExportImportFinanceService exportImportFinanceService;
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private ChartKidsService chartKidsService;
    @Autowired
    private AccountCronjob accountCronjob;
    @Autowired
    private EvaluateKidsCronjobs evaluateKidsCronjobs;
    @Autowired
    private EvaluateDateRepository evaluateDateRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;

    @PostMapping("/cuongnm")
    public static void cuongNMtest(List<String> key) throws Exception {
        Gson gson = new Gson();
        String str = "D://Data-backend//abc_out.json";
        Path fileName = Path.of("D://Data-backend//abc.json");
        String actual = Files.readString(fileName);
        Map<String, List<String>> map = gson.fromJson(actual, Map.class);
        // Tạo new file từ file gốc
        // Chuyển sang Multipartfile
        File file = new File(str);
        System.out.println(file.getName());
        FileInputStream fileInputStream = new FileInputStream(str);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(fileInputStream));
//        JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
//        JsonObject jsonObject2 = JsonParser.parseString().getAsJsonObject();

//      JSONObject jsonObject1 = (JSONObject)obj1;
//        JSONObject jsonObj1 = new ObjectMapper().readValue(new FileReader(convFile), JSONObject.class);
//        JSONObject jsonObj2 = new ObjectMapper().readValue(new FileReader(convFile1), JSONObject.class);
//        if (jsonObject != null && jsonObject2 == null) {
//            return jsonObject;
//        } else if (jsonObject == null && jsonObject2 != null) {
//            return jsonObject;
//        } else {
//            return mergeJSONObjects(jsonObject, jsonObject2);
//            return deepMerge(jsonObj1,jsonObj);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/async")
    public String testAysnc() {

        return "test async";
    }

    private CompletableFuture<String> taskMethod(String task) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Downloading: " + task);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Code to download and return the web page's content
            return "CompletableFuture Completed" + task;
        });
    }

    @RequestMapping(method = RequestMethod.GET, path = "/check-backend")
    public String findByIdExtra() {
        logger.info("check backend run");
        return environment.getProperty("local.server.port");
    }

    @GetMapping("/test-common")
    public List<MaUser> testCommon(MaUserDTO maUserDTO) {
        Pageable pageable = PageRequest.of(maUserDTO.getPageNumber(), maUserDTO.getMaxPageItem());
        List<MaUser> maUserList = maUserRepository.findAllMaUser(maUserDTO, pageable);
        System.out.println(maUserList);
        return maUserList;
    }


    @GetMapping("/base")
    public String test(@RequestBody @Valid SchoolDTO baseDto) {
        System.out.println("JJJJJJ");
        return "0k";
    }

    @GetMapping("/test-getlist-id")
    public String test() {
        return "0k";
    }

    @GetMapping()
    public String testData(@RequestParam String str) {
        LocalDate localDate = LocalDate.parse("2020-04-08");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd - MM - yyyy");
        String date = localDate.format(formatter);
        int ngày = localDate.getDayOfMonth();
        int thang = localDate.getMonthValue();
        int nam = localDate.getYear();
        boolean check = localDate.isBefore(LocalDate.now());
        return "this is my test";
    }

    @PostMapping("/cuongnm/multipartfile")
    public String test(@RequestParam("file") MultipartFile file) throws IOException {
        Gson gson = new Gson();
        ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
        String myString = IOUtils.toString(stream, "UTF-8");
//        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        Map<String, List<String>> map = gson.fromJson(myString, Map.class);
        map.forEach((k, v) -> System.out.println((k + ":" + v)));

//        MultipartFile mulPartFile = file;
//        String strTimeKey = "_" + (ConvertData.ConvertDateMillisecond()) + "_0";
//        String origName = file.getOriginalFilename();
//        String str = origName.replace(origName, FilenameUtils.getBaseName(origName) + (strTimeKey) + "." + (FilenameUtils.getExtension(origName)));
//        String dir = "D:\\" + file.getOriginalFilename();
//        File convFile = new File(dir, str);
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//        HandleFileUtils.getFileNameOfJson(file);
//        if (StringUtils.isNotBlank(dir)) {
//            Files.delete(Paths.get(dir));
//        }
        return " ok test";
    }

    @GetMapping("/vietlv")
    public ResponseEntity testVietLv() {
//        Map<String, Object> objectMap = new HashMap<>();
//        objectMap.put("username", "username1");
//        objectMap.put("password", "password1");
//
//        String username = (String) objectMap.get("username");
//        String lowerCase = RandomStringUtils.random(50, 97, 122, true, false);
//        String upperCase = RandomStringUtils.random(50, 65, 90, true, false);
//        String number = RandomStringUtils.random(50,  false, true);
//        String a="sihsaihf#FHDS23";
//        String b=a.substring(0, a.lastIndexOf("#"));
//        String c=a.substring(a.lastIndexOf("#")+1);
//        List<Integer> list = Arrays.asList(20, 10, 530, 140, 250);
//        Integer max = Collections.max(list);
//        String extend=a.substring(a.lastIndexOf("#")+3);

//        List<Long> longList = null;
//        longList.forEach(x -> {
//            System.out.println(x);
//        });
//        String a=UserInforUtils.getFullName(5l);
//        AvatarUtils.getAvatarEmployeeWithSchool(1l, 9l);
//        AvatarUtils.getAvatarParent(12l);

//        List<Long> list1=Arrays.asList(1l,2l,3l,4l);
//        List<Long> list2=Arrays.asList(3l,4l,5l,6l,7l);
//        List<Long> left= (List<Long>) CollectionUtils.subtract(list1, list2);//kq: 1,2
//        Kids kids = kidsRepository.findById(1l).orElseThrow();
//        AttendanceKidsStatisticalResponse response = attendanceKidsStatisticalService.attendanceKidsStatistical(kids, LocalDate.of(2021, 3, 5), LocalDate.now());
//        List<Kids> kidsList = kidsRepository.findByIdSchool(1L);
//        List<KidsPackageOrderExport> response = exportImportFinanceService.getKidsPackageInOrOut(kidsList, 2021, 1, 12, FinanceConstant.CATEGORY_IN);
//        List<KidsPackageInOutExport> response2 = exportImportFinanceService.getKidsPackageInAndOut(kidsList, 2021, 1, 12);
//        List<KidsPackageExport> response3 = exportImportFinanceService.getKidsPackageOrder(kidsList, 2021, 1, 12);
//        long unixTime = Instant.now().getEpochSecond();
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM");
//        LocalDateTime localDateTime = LocalDateTime.now();
//        String a = formatter.format(localDateTime);
//
//        String aaa = ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category} ", "").replace("{apptype}", AppConstant.PARENT_NAME.toLowerCase());
//        long no = 886123131;
//        String aaa = String.format("%,d", no).replace(",", ".");
////        List<Kids> attendanceKidsList=kidsRepository.findAll();
//        long a = 15;
//        long b = (long) (0.56 * a);
//        long c = (long) (0.57 * a);
//        long diff = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().plusDays(9));
//        abd("55");
//        abd("ff", "gg");
//        abd("ff", "gg", "gggg", "gg");
//        List<EvaluateDate> existEvaluateList = evaluateDateRepository.findByDate(LocalDate.of(2022, 12, 16));
//        List<Long> idKidExistEvaluateList = existEvaluateList.stream().map(x -> x.getKids().getId()).collect(Collectors.toList());
//        List<Kids> allKidList = kidsRepository.findByIdInAndKidStatusAndDelActiveTrue(new ArrayList<>(), KidsStatusConstant.STUDYING);


//        List<Kids> ddd=kidsRepository.findAll();

//        List<String> list = kidsRepository.findIdKidsList1();
//        LocalDate date = LocalDate.now().minusMonths(1);
//        for (int i = 0; i < 5; i++) {
//            date = date.plusDays(1);
//            attendanceKidsCronjobs.generateAttendanceKids(date);
//        }
        LocalDate startDate=LocalDate.of(2023, 01, 05);
        LocalDate endDate=LocalDate.of(2023, 01, 06);
        List<AttendanceEmployee> attendanceEmployeeList = attendanceEmployeeRepository.findByInfoEmployeeSchoolIdAndDateBetween(5l, startDate, endDate);
        return NewDataResponse.setDataSearch("abc");
    }


    private void abd(String a, String... b) {
        int con = b.length;
        if (con > 0) {
            String dd = b[0];
            System.out.println("gh");
        }
        System.out.println("gg");
    }

//    @GetMapping("/vietlv/cache")
//    public String testCache() {
//        return this.getDataTest();
//    }
//
//    @Cacheable(cacheNames = "myCache")
//    public String getDataTest(){
//        return "this is data";
//    }

    @GetMapping("/thangpm")
    public void testThangnt() {

        LocalDate date = LocalDate.now().minusDays(6);

        LocalDate dateCheck = LocalDate.now().minusDays(5);
        if (date.isBefore(dateCheck)) { //false

            int a = 1;
            int b = a + 1;
        }

        System.out.println(date);
//        return DataUtils.getWeekWebList();
    }

    @GetMapping("/thanh")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
//        response.setContentType("application/pdf");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
//        response.setHeader(headerKey, headerValue);
//
//        List<School> schoolList = schoolRepository.findAll();
//        HelloWorldPDF exporter = new HelloWorldPDF(schoolList);
//        exporter.export(response);

    }


    @RequestMapping(method = RequestMethod.POST, value = "/create/employee-timeline")
    public ResponseEntity ResponseEntity() {
        logger.info("create employee time line");
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findAll();

        infoEmployeeSchoolList.forEach(x -> {
            if (CollectionUtils.isEmpty(x.getEmployeeStatusTimelineList())) {
                EmployeeStatusTimeline entity = new EmployeeStatusTimeline();
                entity.setIdCreated(x.getIdCreated());
                entity.setCreatedDate(x.getCreatedDate());
                entity.setStartDate(x.getCreatedDate().toLocalDate());

                entity.setStatus(EmployeeConstant.STATUS_WORKING);
                entity.setInfoEmployeeSchool(x);
                employeeStatusTimelineRepository.save(entity);
            }
        });

        return NewDataResponse.setDataCustom(true, "tạo employee time line thành công");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/thanhnt")
    public String thanh() {
        int j = 123456;
        String x = Integer.toString(j);
        x = x.substring(0, 3) + "," + x.substring(4);

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String a = formatter.format(16656565);
        return a;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload/excel")
    public String thanh123(@RequestBody List<ExcelData> request) {
        int j = 123456;
        String x = Integer.toString(j);
        x = x.substring(0, 3) + "," + x.substring(4);

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String a = formatter.format(16656565);
        return a;
    }
//    private void test123(){
//        Map<Long, String> longMap=new HashMap<>();
//        longMap.put(2l,"ff");
//        longMap.put(5l,"ff");
//        longMap.put(5l,"yy");
//        for (Map.Entry<Long, String> entry : longMap.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//    }

}
