package com.example.onekids_project.service.serviceimpl.sms;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.user.SmsCode;
import com.example.onekids_project.repository.SmsCodeRepository;
import com.example.onekids_project.request.smsNotify.SmsCodeRequest;
import com.example.onekids_project.service.servicecustom.sms.SmsSampleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsSampleServiceImpl implements SmsSampleService {
    @Autowired
    SmsCodeRepository smsCodeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createSmsCodeDefault() {
        List<SmsCode> smsCodes = smsCodeRepository.findAll();
        if (CollectionUtils.isEmpty(smsCodes)) {

            SmsCodeRequest model = new SmsCodeRequest();
            model.setCodeError("0");
            model.setDescription("Success");
            model.setServiceProvider(3);
            SmsCode smsCode = modelMapper.map(model, SmsCode.class);
            smsCode.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode);

            SmsCodeRequest model0 = new SmsCodeRequest();
            model0.setCodeError("1");
            model0.setDescription("Username or Password is null");
            model0.setServiceProvider(3);
            SmsCode smsCode0 = modelMapper.map(model0, SmsCode.class);
            smsCode0.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode0.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode0);

            SmsCodeRequest model1 = new SmsCodeRequest();
            model1.setCodeError("2");
            model1.setDescription("Lỗi xác thực (Sai mật khẩu hoặc IP hoặc User bị khóa hoặc User không tồn tại)");
            model1.setServiceProvider(3);
            SmsCode smsCode1 = modelMapper.map(model1, SmsCode.class);
            smsCode1.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode1.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode1);

            SmsCodeRequest model2 = new SmsCodeRequest();
            model2.setCodeError("3");
            model2.setDescription("Receiver is null");
            model2.setServiceProvider(3);
            SmsCode smsCode2 = modelMapper.map(model2, SmsCode.class);
            smsCode2.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode2.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode2);

            SmsCodeRequest model3 = new SmsCodeRequest();
            model3.setCodeError("4");
            model3.setDescription("Invalid receiver");
            model3.setServiceProvider(3);
            SmsCode smsCode3 = modelMapper.map(model3, SmsCode.class);
            smsCode3.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode3.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode3);

            SmsCodeRequest model4 = new SmsCodeRequest();
            model4.setCodeError("5");
            model4.setDescription("Target is null");
            model4.setServiceProvider(3);
            SmsCode smsCode4 = modelMapper.map(model4, SmsCode.class);
            smsCode4.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode4.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode4);

            SmsCodeRequest model5 = new SmsCodeRequest();
            model5.setCodeError("6");
            model5.setDescription("Error: <chi tiết lỗi gửi tin>");
            model5.setServiceProvider(3);
            SmsCode smsCode5 = modelMapper.map(model5, SmsCode.class);
            smsCode5.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode5.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode5);

            SmsCodeRequest model6 = new SmsCodeRequest();
            model6.setCodeError("99");
            model6.setDescription("Lỗi hệ thống");
            model6.setServiceProvider(3);
            SmsCode smsCode6 = modelMapper.map(model6, SmsCode.class);
            smsCode6.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode6.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode6);


            SmsCodeRequest model7 = new SmsCodeRequest();
            model7.setCodeError("-2");
            model7.setDescription("Sai content type");
            model7.setServiceProvider(1);
            SmsCode smsCode7 = modelMapper.map(model7, SmsCode.class);
            smsCode7.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode7.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode7);

            SmsCodeRequest model8 = new SmsCodeRequest();
            model8.setCodeError("-1");
            model8.setDescription("Thông tin gửi sang không đầy đủ");
            model8.setServiceProvider(1);
            SmsCode smsCode8 = modelMapper.map(model8, SmsCode.class);
            smsCode8.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode8.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode8);

            SmsCodeRequest model9 = new SmsCodeRequest();
            model9.setCodeError("-3");
            model9.setDescription("Sai username/password");
            model9.setServiceProvider(1);
            SmsCode smsCode9 = modelMapper.map(model9, SmsCode.class);
            smsCode9.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode9.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode9);

            SmsCodeRequest model11 = new SmsCodeRequest();
            model11.setCodeError("-4");
            model11.setDescription("Không có quyền upload tin qua webservice này");
            model11.setServiceProvider(1);
            SmsCode smsCode11 = modelMapper.map(model11, SmsCode.class);
            smsCode11.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode11.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode11);

            SmsCodeRequest model12 = new SmsCodeRequest();
            model12.setCodeError("-5");
            model12.setDescription("Sai serviceID (Brandname). Lưu ý, serviceID phải do Media cung cấp cho từng đối tác");
            model12.setServiceProvider(1);
            SmsCode smsCode12 = modelMapper.map(model12, SmsCode.class);
            smsCode12.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode12.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode12);

            SmsCodeRequest model13 = new SmsCodeRequest();
            model13.setCodeError("-7");
            model13.setDescription("Nội dung bản tin vượt quá độ dài quy định. Độ dài với nội dung không dấu là 612 ký tự, nội dung có dấu là 335 ký tự");
            model13.setServiceProvider(1);
            SmsCode smsCode13 = modelMapper.map(model13, SmsCode.class);
            smsCode13.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode13.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode13);

            SmsCodeRequest model14 = new SmsCodeRequest();
            model14.setCodeError("-12");
            model14.setDescription("Sai tham số dataCoding");
            model14.setServiceProvider(1);
            SmsCode smsCode14 = modelMapper.map(model14, SmsCode.class);
            smsCode14.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode14.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode14);

            SmsCodeRequest model15 = new SmsCodeRequest();
            model15.setCodeError("-13");
            model15.setDescription("Tin nhắn có nội dung tiếng việt, trong khi chọn gửi tin không dấu");
            model15.setServiceProvider(1);
            SmsCode smsCode15 = modelMapper.map(model15, SmsCode.class);
            smsCode15.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode15.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode15);

            SmsCodeRequest model16 = new SmsCodeRequest();
            model16.setCodeError("-8");
            model16.setDescription("Lỗi hệ thống");
            model16.setServiceProvider(1);
            SmsCode smsCode16 = modelMapper.map(model16, SmsCode.class);
            smsCode16.setIdCreated(SystemConstant.ID_SYSTEM);
            smsCode16.setCreatedDate(LocalDateTime.now());
            smsCodeRepository.save(smsCode16);

        }

    }
}
