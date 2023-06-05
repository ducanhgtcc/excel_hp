package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.plus.request.GetLinkPlusRequest;
import com.example.onekids_project.mobile.teacher.request.getson.GetJsonTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.jsonattendance.ListUrlJsonResponse;
import com.example.onekids_project.mobile.teacher.response.jsonattendance.UrlJsonResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.UrlJsonMobileService;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class UrlJsonMobileServiceImpl implements UrlJsonMobileService {

    private static final String FILE_DIR = "C:\\xampp\\htdocs\\upload\\2\\savefile";
    private static final String FILE_TEXT_EXT = ".txt";

    public static void deleteFiles(String folder, String ext) {
        File dir = new File(folder);
        String[] list = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(FILE_TEXT_EXT);
            }
        });
        // delete files
        if (list.length == 0) {
            File fileDelete;
            for (String file : list) {
                String temp = FILE_DIR + File.separator +
                        file;
                fileDelete = new File(temp);
                boolean isdeleted = fileDelete.delete();
                System.out.println("file : " + temp + " is deleted : " + isdeleted);
            }
        }
    }


    public ListUrlJsonResponse searchUrlJson1(UserPrincipal principal, GetJsonTeacherRequest getJsonTeacherRequest) throws IOException {
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        // lấy ra các file trong thư mục
        String idSchool9 = idSchool.toString();
        File dir = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + "diemdanhjson");
        File dirsavefile = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + "savefile");
        // xóa file cũ
        for (File file1 : Objects.requireNonNull(dirsavefile.listFiles()))
            if (!file1.isDirectory())
                file1.delete();
        //
        ListUrlJsonResponse listUrlJsonResponse = new ListUrlJsonResponse();
        String[] paths = dir.list();
        if (paths != null) {
            String sumJson = "";
            if (paths.length > 0) {
                for (String path : paths) {
                    if (path.length() > 10) {
                        String dateInput = getJsonTeacherRequest.getDateInput();
                        if (dateInput.equals("")) {
                            String nameFolder = AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + "diemdanhjson";
                            String[] s1 = path.split("_");
                            LocalDateTime dateName = LocalDateTime.now();
                            String dateNameS = dateName.toString();
                            String dateNames1 = dateNameS.replace(":", "-");
                            String dateNames2 = idClass.toString();
                            String dateNames3 = dateNames2 + "-" + dateNames1;
                            String key = s1[3];
                            String Classid = s1[0];
                            int convertint = Integer.parseInt(Classid);
                            if (key.equals("0.json") && idClass == convertint) {
                                String urlFolder = nameFolder + "\\" + path;
                                String content = Files.readString(Paths.get(urlFolder));//đưa về chuẩn utf-8
                                String a = content.replace("{", " ");
                                String b = a.replace("}", ",");
                                sumJson = sumJson.concat(b);
                                String endJson = "{" + sumJson + "}";
                                String finishJson = endJson.replace("],}",
                                        "]}");
                                FileWriter myWriter = new FileWriter(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + "savefile" + "\\" + dateNames3 + ".json");
                                myWriter.write(finishJson);
                                myWriter.close();
                            } else {
                                listUrlJsonResponse.setUrl("");
                                listUrlJsonResponse.setDate(LocalDateTime.now());
                            }
                        } else {
                            LocalDateTime dateName = LocalDateTime.now();
                            String dateNameS = dateName.toString();
                            String dateNames1 = dateNameS.replace(":", "-");
                            String[] s1 = path.split("_");
                            StringBuilder stringBuilder1 = new StringBuilder(s1[2]);
                            String ch = "-";
                            String hc = " ";
                            String time = ":";
                            stringBuilder1.insert(4, ch);
                            stringBuilder1.insert(7, ch);
                            stringBuilder1.insert(10, hc);
                            stringBuilder1.insert(13, time);
                            stringBuilder1.insert(16, time);
                            String a = stringBuilder1.toString();
                            String sss = s1[0];
                            int best = Integer.parseInt(sss);
                            String key = s1[3];
                            String dateNames2 = idClass.toString();
                            String dateNames3 = dateNames2 + "-" + dateNames1;
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                            LocalDateTime dateTime = LocalDateTime.parse(a, formatter);
                            LocalDateTime dateTime1 = LocalDateTime.parse(dateInput, formatter);

                            if (dateTime.isAfter(dateTime1) && key.equals("0.json") && idClass == best || dateTime.equals(dateTime1) && key.equals("0.json") && idClass == best) {
                                // luu file hoc sinh thay doi
                                String nameFolder = AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + "diemdanhjson";
                                String urlFolder = nameFolder + "\\" + path;
                                String content = Files.readString(Paths.get(urlFolder));//đưa về chuẩn utf-8
                                String t = content.replace("{", " ");
                                String b = t.replace("}", ",");
                                sumJson = sumJson.concat(b);
                                String endJson = "{" + sumJson + "}";
                                String finishJson = endJson.replace("],}",
                                        "]}");
                                LocalDateTime localDateTime = LocalDateTime.now();
                                DateTimeFormatter formatter1 = DateTimeFormatter.ISO_DATE_TIME;
                                String formartDt = localDateTime.format(formatter1);
                                FileWriter myWriter = new FileWriter(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE + "\\" + dateNames3 + ".json");
                                myWriter.write(finishJson);
                                myWriter.close();
                            } else if (dateTime.isBefore(dateTime1) && key.equals("0.json")) {
                                listUrlJsonResponse.setUrl("");
                                listUrlJsonResponse.setDate(LocalDateTime.now());
                            }
                        }
                    }
                }
                // link luu file
                File serve = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE);
                String[] pathsSever = serve.list();
                for (String psv : pathsSever) {
                    UrlJsonResponse model = new UrlJsonResponse();
                    String[] sv = psv.split("-");
                    String keyIdClass = sv[0];
                    String thanh1 = sv[1];
                    int convertIdClass = Integer.parseInt(keyIdClass);
                    String urlServe = AppConstant.URL_DEFAULT;
                    // check idClass
                    File file = new File(serve + "\\" + psv);
                    if (convertIdClass == 99999 && thanh1.equals("errrr")) {
                        file.delete();
                        model.setUrl("");
                        model.setDate(LocalDateTime.now());
                        listUrlJsonResponse.setUrl("");
                        listUrlJsonResponse.setDate(LocalDateTime.now());
                    } else if (idClass == convertIdClass) {
                        String linkview = urlServe + idSchool + "/savefile/" + psv;
                        if (linkview != null) {
                            model.setUrl(linkview);
                            model.setDate(LocalDateTime.now());
                            listUrlJsonResponse.setUrl(linkview);
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        }
                    } else {
                        listUrlJsonResponse.setUrl("");
                        listUrlJsonResponse.setDate(LocalDateTime.now());
                    }
                }
            } else {
                listUrlJsonResponse.setUrl("");
                listUrlJsonResponse.setDate(LocalDateTime.now());
            }
        }
        return listUrlJsonResponse;
    }

    @Override
    public ListUrlJsonResponse searchUrlForPlus(UserPrincipal principal, GetLinkPlusRequest request) throws IOException {
        Long idSchool = principal.getIdSchoolLogin();
        // lấy ra các file trong thư mục
        String idSchool9 = principal.getIdSchoolLogin().toString();
        File dir = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.JSON_FOLDER);
        File dirsavefile = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE);
        // xóa file cũ
        for (File file1 : Objects.requireNonNull(dirsavefile.listFiles()))
            if (!file1.isDirectory())
                file1.delete();
        ListUrlJsonResponse listUrlJsonResponse = new ListUrlJsonResponse();
        String[] paths = dir.list();
        if (paths != null) {
            String sumJson = "";
            if (request.getIdClass() != null) {
                if (paths.length > 0) {
                    Long idClass = request.getIdClass();
                    for (String path : paths) {
                        if (path.length() > 10) {
                            String dateInput = request.getDateInput();
                            if (dateInput.equals("")) {
                                String nameFolder = AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.JSON_FOLDER;
                                String[] s1 = path.split("_");
                                LocalDateTime dateName = LocalDateTime.now();
                                String dateNameS = dateName.toString();
                                String dateNames1 = dateNameS.replace(":", "-");
                                String dateNames2 = idClass.toString();
                                String dateNames3 = dateNames2 + "-" + dateNames1;
                                String key = s1[3];
                                String Classid = s1[0];
                                int convertint = Integer.parseInt(Classid);
                                if (key.equals("0.json") && idClass == convertint) {
                                    String urlFolder = nameFolder + "\\" + path;
                                    String content = Files.readString(Paths.get(urlFolder));//đưa về chuẩn utf-8
                                    String a = content.replace("{", " ");
                                    String b = a.replace("}", ",");
                                    sumJson = sumJson.concat(b);
                                    String endJson = "{" + sumJson + "}";
                                    String finishJson = endJson.replace("],}", "]}");
                                    FileWriter myWriter = new FileWriter(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE + "\\" + dateNames3 + ".json");
                                    myWriter.write(finishJson);
                                    myWriter.close();
                                }
                            } else {
                                LocalDateTime dateName = LocalDateTime.now();
                                String dateNameS = dateName.toString();
                                String dateNames1 = dateNameS.replace(":", "-");
                                String[] s1 = path.split("_");
                                StringBuilder stringBuilder1 = new StringBuilder(s1[2]);
                                String ch = "-";
                                String hc = " ";
                                String time = ":";
                                stringBuilder1.insert(4, ch);
                                stringBuilder1.insert(7, ch);
                                stringBuilder1.insert(10, hc);
                                stringBuilder1.insert(13, time);
                                stringBuilder1.insert(16, time);
                                String a = stringBuilder1.toString();
                                String sss = s1[0];
                                int best = Integer.parseInt(sss);
                                String key = s1[3];
                                String dateNames2 = idClass.toString();
                                String dateNames3 = dateNames2 + "-" + dateNames1;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                                LocalDateTime dateTime = LocalDateTime.parse(a, formatter);
                                LocalDateTime dateTime1 = LocalDateTime.parse(dateInput, formatter);
                                if (dateTime.isAfter(dateTime1) && key.equals("0.json") && idClass == best || dateTime.equals(dateTime1) && key.equals("0.json") && idClass == best) {
                                    // luu file hoc sinh thay doi
                                    String nameFolder = AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.JSON_FOLDER;
                                    String urlFolder = nameFolder + "\\" + path;
                                    String content = Files.readString(Paths.get(urlFolder));//đưa về chuẩn utf-8
                                    String t = content.replace("{", " ");
                                    String b = t.replace("}", ",");
                                    sumJson = sumJson.concat(b);
                                    String endJson = "{" + sumJson + "}";
                                    String finishJson = endJson.replace("],}", "]}");
                                    FileWriter myWriter = new FileWriter(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE + "\\" + dateNames3 + ".json");
                                    myWriter.write(finishJson);
                                    myWriter.close();
                                } else if (dateTime.isBefore(dateTime1) && key.equals("0.json")) {
                                    listUrlJsonResponse.setUrl("");
                                    listUrlJsonResponse.setDate(LocalDateTime.now());
                                }
                            }
                        } else {
                            listUrlJsonResponse.setUrl("");
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        }
                    }
                    // link luu file
                    File serve = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE);
                    String[] pathsSever = serve.list();
                    for (String psv : pathsSever) {
                        UrlJsonResponse model = new UrlJsonResponse();
                        String[] sv = psv.split("-");
                        String keyIdClass = sv[0];
                        String thanh1 = sv[1];
                        int convertIdClass = Integer.parseInt(keyIdClass);
                        String urlServe = AppConstant.URL_DEFAULT;
                        // check idClass
                        File file = new File(serve + "\\" + psv);
                        if (convertIdClass == 99999 && thanh1.equals("errrr")) {
                            file.delete();
                            model.setUrl("");
                            model.setDate(LocalDateTime.now());
                            listUrlJsonResponse.setUrl("");
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        } else if (idClass == convertIdClass) {
                            String linkview = urlServe + idSchool + "/savefile/" + psv;
                            model.setUrl(linkview);
                            model.setDate(LocalDateTime.now());
                            listUrlJsonResponse.setUrl(linkview);
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        } else {
                            listUrlJsonResponse.setUrl("");
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        }
                    }
                } else {
                    listUrlJsonResponse.setUrl("");
                    listUrlJsonResponse.setDate(LocalDateTime.now());
                }
            } else {
                if (paths.length > 0) {
                    for (String path : paths) {
                        if (path.length() > 10) {
                            String dateInput = request.getDateInput();
                            if (dateInput == null || dateInput.equals("")) {
                                String nameFolder = AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.JSON_FOLDER;
                                String[] s1 = path.split("_");
                                LocalDateTime dateName = LocalDateTime.now();
                                String dateNameS = dateName.toString();
                                String dateNames1 = dateNameS.replace(":", "-");
                                String dateNames2 = "0";
                                String dateNames3 = dateNames2 + "-" + dateNames1;
                                String key = s1[3];
                                String Classid = s1[0];
                                if (key.equals("0.json")) {
                                    String urlFolder = nameFolder + "\\" + path;
                                    String content = Files.readString(Paths.get(urlFolder));//đưa về chuẩn utf-8
                                    String a = content.replace("{", " ");
                                    String b = a.replace("}", ",");
                                    sumJson = sumJson.concat(b);
                                    String endJson = "{" + sumJson + "}";
                                    String finishJson = endJson.replace("],}", "]}");
                                    FileWriter myWriter = new FileWriter(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE + "\\" + dateNames3 + ".json");
                                    myWriter.write(finishJson);
                                    myWriter.close();
                                } else {
                                    listUrlJsonResponse.setUrl("");
                                    listUrlJsonResponse.setDate(LocalDateTime.now());
                                }
                            } else {
                                LocalDateTime dateName = LocalDateTime.now();
                                String dateNameS = dateName.toString();
                                String dateNames1 = dateNameS.replace(":", "-");
                                String[] s1 = path.split("_");
                                StringBuilder stringBuilder1 = new StringBuilder(s1[2]);
                                String ch = "-";
                                String hc = " ";
                                String time = ":";
                                stringBuilder1.insert(4, ch);
                                stringBuilder1.insert(7, ch);
                                stringBuilder1.insert(10, hc);
                                stringBuilder1.insert(13, time);
                                stringBuilder1.insert(16, time);
                                String a = stringBuilder1.toString();
                                String key = s1[3];
                                String dateNames2 = "0";
                                String dateNames3 = dateNames2 + "-" + dateNames1;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                                LocalDateTime dateTime = LocalDateTime.parse(a, formatter);
                                LocalDateTime dateTime1 = LocalDateTime.parse(dateInput, formatter);
                                if (dateTime.isAfter(dateTime1) && key.equals("0.json") || dateTime.equals(dateTime1) && key.equals("0.json")) {
                                    // luu file hoc sinh thay doi
                                    String nameFolder = AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.JSON_FOLDER;
                                    String urlFolder = nameFolder + "\\" + path;
                                    String content = Files.readString(Paths.get(urlFolder));//đưa về chuẩn utf-8
                                    String t = content.replace("{", " ");
                                    String b = t.replace("}", ",");
                                    sumJson = sumJson.concat(b);
                                    String endJson = "{" + sumJson + "}";
                                    String finishJson = endJson.replace("],}", "]}");
                                    FileWriter myWriter = new FileWriter(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE + "\\" + dateNames3 + ".json");
                                    myWriter.write(finishJson);
                                    myWriter.close();
                                } else if (dateTime.isBefore(dateTime1) && key.equals("0.json")) {
                                    listUrlJsonResponse.setUrl("");
                                    listUrlJsonResponse.setDate(LocalDateTime.now());
                                }
                            }
                        }
                    }
                    // link luu file
                    File serve = new File(AppConstant.DIRECTORY_DEFAULT + idSchool9 + "\\" + AppConstant.SAVE_FILE);
                    String[] pathsSever = serve.list();
                    for (String psv : pathsSever) {
                        UrlJsonResponse model = new UrlJsonResponse();
                        String[] sv = psv.split("-");
                        String keyIdClass = sv[0];
                        int convertIdClass = Integer.parseInt(keyIdClass);
                        // check idClass
                        if (0 == convertIdClass) {
                            String linkview = AppConstant.URL_DEFAULT + idSchool + "/savefile/" + psv;
                            model.setUrl(linkview);
                            model.setDate(LocalDateTime.now());
                            listUrlJsonResponse.setUrl(linkview);
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        } else {
                            listUrlJsonResponse.setUrl("");
                            listUrlJsonResponse.setDate(LocalDateTime.now());
                        }
                    }
                } else {
                    listUrlJsonResponse.setUrl("");
                    listUrlJsonResponse.setDate(LocalDateTime.now());
                }
            }
        } else {
            listUrlJsonResponse.setUrl("");
            listUrlJsonResponse.setDate(LocalDateTime.now());
        }

        return listUrlJsonResponse;
    }

}
