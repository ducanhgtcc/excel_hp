package com.example.onekids_project.common;

import java.util.Arrays;
import java.util.List;

public interface UploadDownloadConstant {
    String SYSTEM_FILES = "sysfiles";

    String TEMP_DATA = "tempdata";

    String LOI_NHAN = "loinhan";

    String DON_THUOC = "danthuoc";

    String XIN_NGHI = "xinnghi";

    String DIEM_DANH = "diemdanh";

    String DIEM_DANH_JSON = "diemdanhjson";

    String SAVE_FILE = "savefile";

    String ALBUM = "album";

    String NHAN_XET = "nhanxet";

    String HOC_TAP = "hoctap";

    String THUC_DON = "thucdon";

    String GOP_Y = "gopy";

    String THONG_BAO = "thongbao";

    String AVATAR = "avatar";

    String TAI_CHINH = "taichinh";

    String KHAC = "khac";

    String OTHER = "other";

    String SAMPLE = "sample";

    String THUMBNAIL = "thumbnail_";

    List<String> EXTENDSION_PICTURE = Arrays.asList("jpg", "jpeg", "png", "gif");

    List<String> EXTENDSION_PICTURE_NO_COMPRESSION = Arrays.asList("jpg", "jpeg", "png");

    int WIDTH_ALBUM = 256;

    int WIDTH_OTHER = 512;

    int MAX_FILE = 3;

    //tối đa 5MB mỗi file
    int MAX_SIZE = 5;

}
