package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.sample.WeightSample;
import com.example.onekids_project.master.request.WeightSampleRequest;
import com.example.onekids_project.master.service.WeightSampleService;
import com.example.onekids_project.repository.WeightSampleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeightSampleServiceImpl implements WeightSampleService {
    @Autowired
    private WeightSampleRepository weightSampleRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public void createWeightDefault() {
        List<WeightSample> weightSampleList = weightSampleRepository.findAll();
        if (CollectionUtils.isEmpty(weightSampleList)) {
            WeightSampleRequest model1 = new WeightSampleRequest();
            model1.setYearOld("Sơ sinh");
            model1.setMin(2.5);
            model1.setMedium(3.3);
            model1.setMax(4.4);
            model1.setType("boy");
            WeightSample weightSample1 = modelMapper.map(model1, WeightSample.class);
            weightSample1.setIdCreated(SystemConstant.ID_SYSTEM);
            weightSample1.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(weightSample1);

            WeightSampleRequest model2 = new WeightSampleRequest();
            model2.setYearOld("1 tháng");
            model2.setMin(3.4);
            model2.setMedium(4.5);
            model2.setMax(5.8);
            model2.setType("boy");
            WeightSample WeightSample2 = modelMapper.map(model2, WeightSample.class);
            WeightSample2.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample2.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample2);

            WeightSampleRequest model3 = new WeightSampleRequest();
            model3.setYearOld("2 tháng");
            model3.setMin(4.3);
            model3.setMedium(5.6);
            model3.setMax(7.1);
            model3.setType("boy");
            WeightSample WeightSample3 = modelMapper.map(model3, WeightSample.class);
            WeightSample3.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample3.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample3);

            WeightSampleRequest model4 = new WeightSampleRequest();
            model4.setYearOld("3 tháng");
            model4.setMin(5.0);
            model4.setMedium(6.4);
            model4.setMax(8.0);
            model4.setType("boy");
            WeightSample WeightSample4 = modelMapper.map(model4, WeightSample.class);
            WeightSample4.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample4.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample4);

            WeightSampleRequest model5 = new WeightSampleRequest();
            model5.setYearOld("4 tháng");
            model5.setMin(5.6);
            model5.setMedium(7.0);
            model5.setMax(8.7);
            model5.setType("boy");
            WeightSample WeightSample5 = modelMapper.map(model5, WeightSample.class);
            WeightSample5.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample5.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample5);

            WeightSampleRequest model6 = new WeightSampleRequest();
            model6.setYearOld("5 tháng");
            model6.setMin(6.0);
            model6.setMedium(7.5);
            model6.setMax(9.3);
            model6.setType("boy");
            WeightSample WeightSample6 = modelMapper.map(model6, WeightSample.class);
            WeightSample6.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample6.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample6);


            WeightSampleRequest model699 = new WeightSampleRequest();
            model699.setYearOld("6 tháng");
            model699.setMin(6.4);
            model699.setMedium(7.9);
            model699.setMax(9.8);
            model699.setType("boy");
            WeightSample WeightSample699 = modelMapper.map(model699, WeightSample.class);
            WeightSample699.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample699.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample699);

            WeightSampleRequest model700 = new WeightSampleRequest();
            model700.setYearOld("7 tháng");
            model700.setMin(6.7);
            model700.setMedium(8.3);
            model700.setMax(10.3);
            model700.setType("boy");
            WeightSample WeightSample700 = modelMapper.map(model700, WeightSample.class);
            WeightSample700.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample700.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample700);


            WeightSampleRequest model7 = new WeightSampleRequest();
            model7.setYearOld("8 tháng");
            model7.setMin(6.9);
            model7.setMedium(8.6);
            model7.setMax(10.7);
            model7.setType("boy");
            WeightSample WeightSample7 = modelMapper.map(model7, WeightSample.class);
            WeightSample7.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample7.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample7);

            WeightSampleRequest model8 = new WeightSampleRequest();
            model8.setYearOld("9 tháng");
            model8.setMin(7.1);
            model8.setMedium(8.9);
            model8.setMax(11.0);
            model8.setType("boy");
            WeightSample WeightSample8 = modelMapper.map(model8, WeightSample.class);
            WeightSample8.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample8.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample8);

            WeightSampleRequest model9 = new WeightSampleRequest();
            model9.setYearOld("10 tháng");
            model9.setMin(7.4);
            model9.setMedium(9.2);
            model9.setMax(11.4);
            model9.setType("boy");
            WeightSample WeightSample9 = modelMapper.map(model9, WeightSample.class);
            WeightSample9.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample9.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample9);

            WeightSampleRequest model10 = new WeightSampleRequest();
            model10.setYearOld("11 tháng");
            model10.setMin(7.6);
            model10.setMedium(9.4);
            model10.setMax(11.7);
            model10.setType("boy");
            WeightSample WeightSample10 = modelMapper.map(model10, WeightSample.class);
            WeightSample10.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample10.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample10);

            WeightSampleRequest model11 = new WeightSampleRequest();
            model11.setYearOld("12 tháng");
            model11.setMin(7.7);
            model11.setMedium(9.6);
            model11.setMax(12.0);
            model11.setType("boy");
            WeightSample WeightSample11 = modelMapper.map(model11, WeightSample.class);
            WeightSample11.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample11.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample11);

            WeightSampleRequest model12 = new WeightSampleRequest();
            model12.setYearOld("13 tháng");
            model12.setMin(7.9);
            model12.setMedium(9.8);
            model12.setMax(12.3);
            model12.setType("boy");
            WeightSample WeightSample12 = modelMapper.map(model12, WeightSample.class);
            WeightSample12.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample12.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample12);

            WeightSampleRequest model13 = new WeightSampleRequest();
            model13.setYearOld("14 tháng");
            model13.setMin(8.1);
            model13.setMedium(10.0);
            model13.setMax(12.5);
            model13.setType("boy");
            WeightSample WeightSample13 = modelMapper.map(model13, WeightSample.class);
            WeightSample13.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample13.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample13);

            WeightSampleRequest model14 = new WeightSampleRequest();
            model14.setYearOld("15 tháng");
            model14.setMin(8.3);
            model14.setMedium(10.3);
            model14.setMax(12.8);
            model14.setType("boy");
            WeightSample WeightSample14 = modelMapper.map(model14, WeightSample.class);
            WeightSample14.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample14.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample14);

            WeightSampleRequest model15 = new WeightSampleRequest();
            model15.setYearOld("16 tháng");
            model15.setMin(8.5);
            model15.setMedium(10.5);
            model15.setMax(13.1);
            model15.setType("boy");
            WeightSample WeightSample15 = modelMapper.map(model15, WeightSample.class);
            WeightSample15.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample15.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample15);

            WeightSampleRequest model16 = new WeightSampleRequest();
            model16.setYearOld("17 tháng");
            model16.setMin(8.7);
            model16.setMedium(10.7);
            model16.setMax(13.4);
            model16.setType("boy");
            WeightSample WeightSample16 = modelMapper.map(model16, WeightSample.class);
            WeightSample16.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample16.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample16);

            WeightSampleRequest model17 = new WeightSampleRequest();
            model17.setYearOld("18 tháng");
            model17.setMin(8.8);
            model17.setMedium(10.9);
            model17.setMax(13.7);
            model17.setType("boy");
            WeightSample WeightSample17 = modelMapper.map(model17, WeightSample.class);
            WeightSample17.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample17.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample17);

            WeightSampleRequest model18 = new WeightSampleRequest();
            model18.setYearOld("19 tháng");
            model18.setMin(8.9);
            model18.setMedium(11.1);
            model18.setMax(14.0);
            model18.setType("boy");
            WeightSample WeightSample18 = modelMapper.map(model18, WeightSample.class);
            WeightSample18.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample18.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample18);

            WeightSampleRequest model19 = new WeightSampleRequest();
            model19.setYearOld("20 tháng");
            model19.setMin(9.0);
            model19.setMedium(11.3);
            model19.setMax(14.2);
            model19.setType("boy");
            WeightSample WeightSample19 = modelMapper.map(model19, WeightSample.class);
            WeightSample19.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample19.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample19);

            WeightSampleRequest model20 = new WeightSampleRequest();
            model20.setYearOld("21 tháng");
            model20.setMin(9.2);
            model20.setMedium(11.5);
            model20.setMax(14.5);
            model20.setType("boy");
            WeightSample WeightSample20 = modelMapper.map(model20, WeightSample.class);
            WeightSample20.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample20.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample20);

            WeightSampleRequest model21 = new WeightSampleRequest();
            model21.setYearOld("22 tháng");
            model21.setMin(9.4);
            model21.setMedium(11.8);
            model21.setMax(14.8);
            model21.setType("boy");
            WeightSample WeightSample21 = modelMapper.map(model21, WeightSample.class);
            WeightSample21.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample21.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample21);

            WeightSampleRequest model22 = new WeightSampleRequest();
            model22.setYearOld("23 tháng");
            model22.setMin(9.5);
            model22.setMedium(12.0);
            model22.setMax(15.0);
            model22.setType("boy");
            WeightSample WeightSample22 = modelMapper.map(model22, WeightSample.class);
            WeightSample22.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample22.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample22);

            WeightSampleRequest model23 = new WeightSampleRequest();
            model23.setYearOld("24 tháng");
            model23.setMin(9.7);
            model23.setMedium(12.2);
            model23.setMax(15.3);
            model23.setType("boy");
            WeightSample WeightSample23 = modelMapper.map(model23, WeightSample.class);
            WeightSample23.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample23.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample23);

            WeightSampleRequest model24 = new WeightSampleRequest();
            model24.setYearOld("2.5 tuổi");
            model24.setMin(10.5);
            model24.setMedium(13.3);
            model24.setMax(16.9);
            model24.setType("boy");
            WeightSample WeightSample24 = modelMapper.map(model24, WeightSample.class);
            WeightSample24.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample24.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample24);

            WeightSampleRequest model25 = new WeightSampleRequest();
            model25.setYearOld("3 tuổi");
            model25.setMin(11.3);
            model25.setMedium(14.3);
            model25.setMax(18.3);
            model25.setType("boy");
            WeightSample WeightSample25 = modelMapper.map(model25, WeightSample.class);
            WeightSample25.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample25.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample25);

            WeightSampleRequest model26 = new WeightSampleRequest();
            model26.setYearOld("3.5 tuổi");
            model26.setMin(12.0);
            model26.setMedium(15.3);
            model26.setMax(18.3);
            model26.setType("boy");
            WeightSample WeightSample26 = modelMapper.map(model26, WeightSample.class);
            WeightSample26.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample26.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample26);

            WeightSampleRequest model27 = new WeightSampleRequest();
            model27.setYearOld("4 tuổi");
            model27.setMin(12.7);
            model27.setMedium(16.3);
            model27.setMax(21.2);
            model27.setType("boy");
            WeightSample WeightSample27 = modelMapper.map(model27, WeightSample.class);
            WeightSample27.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample27.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample27);

            WeightSampleRequest model28 = new WeightSampleRequest();
            model28.setYearOld("4.5 tuổi");
            model28.setMin(13.4);
            model28.setMedium(17.3);
            model28.setMax(22.7);
            model28.setType("boy");
            WeightSample WeightSample28 = modelMapper.map(model28, WeightSample.class);
            WeightSample28.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample28.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample28);

            WeightSampleRequest model29 = new WeightSampleRequest();
            model29.setYearOld("5 tuổi");
            model29.setMin(14.1);
            model29.setMedium(18.3);
            model29.setMax(24.2);
            model29.setType("boy");
            WeightSample WeightSample29 = modelMapper.map(model29, WeightSample.class);
            WeightSample29.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample29.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample29);

            WeightSampleRequest model30 = new WeightSampleRequest();
            model30.setYearOld("5.5 tuổi");
            model30.setMin(15.0);
            model30.setMedium(19.4);
            model30.setMax(25.5);
            model30.setType("boy");
            WeightSample WeightSample30 = modelMapper.map(model30, WeightSample.class);
            WeightSample30.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample30.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample30);

            WeightSampleRequest model31 = new WeightSampleRequest();
            model31.setYearOld("6 tuổi");
            model31.setMin(15.9);
            model31.setMedium(20.5);
            model31.setMax(27.1);
            model31.setType("boy");
            WeightSample WeightSample31 = modelMapper.map(model31, WeightSample.class);
            WeightSample31.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample31.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample31);

            WeightSampleRequest model33 = new WeightSampleRequest();
            model33.setYearOld("Lớn hơn 6 tuổi");
            model33.setMin(15.9);
            model33.setMedium(20.5);
            model33.setMax(27.1);
            model33.setType("boy");
            WeightSample WeightSample33 = modelMapper.map(model33, WeightSample.class);
            WeightSample33.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample33.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample33);


            // weightsample girl

            WeightSampleRequest model35 = new WeightSampleRequest();
            model35.setYearOld("Sơ sinh");
            model35.setMin(2.4);
            model35.setMedium(3.2);
            model35.setMax(4.2);
            model35.setType("girl");
            WeightSample weightSample35 = modelMapper.map(model35, WeightSample.class);
            weightSample35.setIdCreated(SystemConstant.ID_SYSTEM);
            weightSample35.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(weightSample35);

            WeightSampleRequest model36 = new WeightSampleRequest();
            model36.setYearOld("1 tháng");
            model36.setMin(3.2);
            model36.setMedium(4.2);
            model36.setMax(5.5);
            model36.setType("girl");
            WeightSample WeightSample36 = modelMapper.map(model36, WeightSample.class);
            WeightSample36.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample36.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample36);

            WeightSampleRequest model37 = new WeightSampleRequest();
            model37.setYearOld("2 tháng");
            model37.setMin(3.9);
            model37.setMedium(5.1);
            model37.setMax(6.6);
            model37.setType("girl");
            WeightSample WeightSample37 = modelMapper.map(model37, WeightSample.class);
            WeightSample37.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample37.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample37);

            WeightSampleRequest model38 = new WeightSampleRequest();
            model38.setYearOld("3 tháng");
            model38.setMin(4.5);
            model38.setMedium(5.8);
            model38.setMax(7.5);
            model38.setType("girl");
            WeightSample WeightSample38 = modelMapper.map(model38, WeightSample.class);
            WeightSample38.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample38.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample38);

            WeightSampleRequest model39 = new WeightSampleRequest();
            model39.setYearOld("4 tháng");
            model39.setMin(5.0);
            model39.setMedium(6.4);
            model39.setMax(8.2);
            model39.setType("girl");
            WeightSample WeightSample39 = modelMapper.map(model39, WeightSample.class);
            WeightSample39.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample39.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample39);

            WeightSampleRequest model40 = new WeightSampleRequest();
            model40.setYearOld("5 tháng");
            model40.setMin(5.4);
            model40.setMedium(6.9);
            model40.setMax(8.8);
            model40.setType("girl");
            WeightSample WeightSample40 = modelMapper.map(model40, WeightSample.class);
            WeightSample40.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample40.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample40);

            WeightSampleRequest model6999 = new WeightSampleRequest();
            model6999.setYearOld("6 tháng");
            model6999.setMin(5.7);
            model6999.setMedium(7.3);
            model6999.setMax(9.3);
            model6999.setType("girl");
            WeightSample WeightSample6999 = modelMapper.map(model6999, WeightSample.class);
            WeightSample6999.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample6999.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample6999);

            WeightSampleRequest model7000 = new WeightSampleRequest();
            model7000.setYearOld("7 tháng");
            model7000.setMin(6.0);
            model7000.setMedium(7.6);
            model7000.setMax(9.8);
            model7000.setType("girl");
            WeightSample WeightSample7000 = modelMapper.map(model7000, WeightSample.class);
            WeightSample7000.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample7000.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample7000);

            WeightSampleRequest model41 = new WeightSampleRequest();
            model41.setYearOld("8 tháng");
            model41.setMin(6.3);
            model41.setMedium(7.9);
            model41.setMax(10.2);
            model41.setType("girl");
            WeightSample WeightSample41 = modelMapper.map(model41, WeightSample.class);
            WeightSample41.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample41.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample41);

            WeightSampleRequest model42 = new WeightSampleRequest();
            model42.setYearOld("9 tháng");
            model42.setMin(6.5);
            model42.setMedium(8.2);
            model42.setMax(10.5);
            model42.setType("girl");
            WeightSample WeightSample42 = modelMapper.map(model42, WeightSample.class);
            WeightSample42.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample42.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample42);

            WeightSampleRequest model43 = new WeightSampleRequest();
            model43.setYearOld("10 tháng");
            model43.setMin(6.7);
            model43.setMedium(8.5);
            model43.setMax(10.9);
            model43.setType("girl");
            WeightSample WeightSample43 = modelMapper.map(model43, WeightSample.class);
            WeightSample43.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample43.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample43);

            WeightSampleRequest model44 = new WeightSampleRequest();
            model44.setYearOld("11 tháng");
            model44.setMin(6.9);
            model44.setMedium(8.7);
            model44.setMax(11.2);
            model44.setType("girl");
            WeightSample WeightSample44 = modelMapper.map(model44, WeightSample.class);
            WeightSample44.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample44.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample44);

            WeightSampleRequest model45 = new WeightSampleRequest();
            model45.setYearOld("12 tháng");
            model45.setMin(7.0);
            model45.setMedium(8.9);
            model45.setMax(11.5);
            model45.setType("girl");
            WeightSample WeightSample45 = modelMapper.map(model45, WeightSample.class);
            WeightSample45.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample45.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample45);

            WeightSampleRequest model46 = new WeightSampleRequest();
            model46.setYearOld("13 tháng");
            model46.setMin(7.2);
            model46.setMedium(9.2);
            model46.setMax(11.8);
            model46.setType("girl");
            WeightSample WeightSample46 = modelMapper.map(model46, WeightSample.class);
            WeightSample46.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample46.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample46);

            WeightSampleRequest model47 = new WeightSampleRequest();
            model47.setYearOld("14 tháng");
            model47.setMin(7.4);
            model47.setMedium(9.4);
            model47.setMax(12.1);
            model47.setType("girl");
            WeightSample WeightSample47 = modelMapper.map(model47, WeightSample.class);
            WeightSample47.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample47.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample47);

            WeightSampleRequest model48 = new WeightSampleRequest();
            model48.setYearOld("15 tháng");
            model48.setMin(7.6);
            model48.setMedium(9.6);
            model48.setMax(12.4);
            model48.setType("girl");
            WeightSample WeightSample48 = modelMapper.map(model48, WeightSample.class);
            WeightSample48.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample48.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample48);

            WeightSampleRequest model49 = new WeightSampleRequest();
            model49.setYearOld("16 tháng");
            model49.setMin(7.8);
            model49.setMedium(9.38);
            model49.setMax(12.8);
            model49.setType("girl");
            WeightSample WeightSample49 = modelMapper.map(model49, WeightSample.class);
            WeightSample49.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample49.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample49);

            WeightSampleRequest model50 = new WeightSampleRequest();
            model50.setYearOld("17 tháng");
            model50.setMin(8.0);
            model50.setMedium(10.0);
            model50.setMax(13.0);
            model50.setType("girl");
            WeightSample WeightSample50 = modelMapper.map(model50, WeightSample.class);
            WeightSample50.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample50.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample50);

            WeightSampleRequest model51 = new WeightSampleRequest();
            model51.setYearOld("18 tháng");
            model51.setMin(8.1);
            model51.setMedium(10.2);
            model51.setMax(13.2);
            model51.setType("girl");
            WeightSample WeightSample51 = modelMapper.map(model51, WeightSample.class);
            WeightSample51.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample51.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample51);

            WeightSampleRequest model52 = new WeightSampleRequest();
            model52.setYearOld("19 tháng");
            model52.setMin(8.3);
            model52.setMedium(10.4);
            model52.setMax(13.5);
            model52.setType("girl");
            WeightSample WeightSample52 = modelMapper.map(model52, WeightSample.class);
            WeightSample52.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample52.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample52);

            WeightSampleRequest model53 = new WeightSampleRequest();
            model53.setYearOld("20 tháng");
            model53.setMin(8.5);
            model53.setMedium(10.6);
            model53.setMax(13.7);
            model53.setType("girl");
            WeightSample WeightSample53 = modelMapper.map(model53, WeightSample.class);
            WeightSample53.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample53.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample53);

            WeightSampleRequest model54 = new WeightSampleRequest();
            model54.setYearOld("21 tháng");
            model54.setMin(8.6);
            model54.setMedium(10.9);
            model54.setMax(14.0);
            model54.setType("girl");
            WeightSample WeightSample54 = modelMapper.map(model54, WeightSample.class);
            WeightSample54.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample54.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample54);

            WeightSampleRequest model55 = new WeightSampleRequest();
            model55.setYearOld("22 tháng");
            model55.setMin(8.8);
            model55.setMedium(11.1);
            model55.setMax(14.3);
            model55.setType("girl");
            WeightSample WeightSample55 = modelMapper.map(model55, WeightSample.class);
            WeightSample55.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample55.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample55);

            WeightSampleRequest model56 = new WeightSampleRequest();
            model56.setYearOld("23 tháng");
            model56.setMin(8.9);
            model56.setMedium(11.3);
            model56.setMax(14.5);
            model56.setType("girl");
            WeightSample WeightSample56 = modelMapper.map(model56, WeightSample.class);
            WeightSample56.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample56.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample56);

            WeightSampleRequest model57 = new WeightSampleRequest();
            model57.setYearOld("24 tháng");
            model57.setMin(9.0);
            model57.setMedium(11.5);
            model57.setMax(14.8);
            model57.setType("girl");
            WeightSample WeightSample57 = modelMapper.map(model57, WeightSample.class);
            WeightSample57.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample57.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample57);

            WeightSampleRequest model58 = new WeightSampleRequest();
            model58.setYearOld("2.5 tuổi");
            model58.setMin(10.0);
            model58.setMedium(12.7);
            model58.setMax(16.5);
            model58.setType("girl");
            WeightSample WeightSample58 = modelMapper.map(model58, WeightSample.class);
            WeightSample58.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample58.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample58);

            WeightSampleRequest model59 = new WeightSampleRequest();
            model59.setYearOld("3 tuổi");
            model59.setMin(10.8);
            model59.setMedium(13.9);
            model59.setMax(18.1);
            model59.setType("girl");
            WeightSample WeightSample59 = modelMapper.map(model59, WeightSample.class);
            WeightSample59.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample59.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample59);

            WeightSampleRequest model60 = new WeightSampleRequest();
            model60.setYearOld("3.5 tuổi");
            model60.setMin(11.6);
            model60.setMedium(15.0);
            model60.setMax(19.8);
            model60.setType("girl");
            WeightSample WeightSample60 = modelMapper.map(model60, WeightSample.class);
            WeightSample60.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample60.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample60);

            WeightSampleRequest model61 = new WeightSampleRequest();
            model61.setYearOld("4 tuổi");
            model61.setMin(14.3);
            model61.setMedium(16.1);
            model61.setMax(21.5);
            model61.setType("girl");
            WeightSample WeightSample61 = modelMapper.map(model61, WeightSample.class);
            WeightSample61.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample61.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample61);

            WeightSampleRequest model62 = new WeightSampleRequest();
            model62.setYearOld("4.5 tuổi");
            model62.setMin(13.0);
            model62.setMedium(17.2);
            model62.setMax(23.2);
            model62.setType("girl");
            WeightSample WeightSample62 = modelMapper.map(model62, WeightSample.class);
            WeightSample62.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample62.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample62);

            WeightSampleRequest model63 = new WeightSampleRequest();
            model63.setYearOld("5 tuổi");
            model63.setMin(13.7);
            model63.setMedium(18.2);
            model63.setMax(24.9);
            model63.setType("girl");
            WeightSample WeightSample63 = modelMapper.map(model63, WeightSample.class);
            WeightSample63.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample63.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample63);

            WeightSampleRequest model64 = new WeightSampleRequest();
            model64.setYearOld("5.5 tuổi");
            model64.setMin(14.6);
            model64.setMedium(19.1);
            model64.setMax(26.2);
            model64.setType("girl");
            WeightSample WeightSample64 = modelMapper.map(model64, WeightSample.class);
            WeightSample64.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample64.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample64);

            WeightSampleRequest model65 = new WeightSampleRequest();
            model65.setYearOld("6 tuổi");
            model65.setMin(15.3);
            model65.setMedium(20.2);
            model65.setMax(27.8);
            model65.setType("girl");
            WeightSample WeightSample65 = modelMapper.map(model65, WeightSample.class);
            WeightSample65.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample65.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample65);

            WeightSampleRequest model66 = new WeightSampleRequest();
            model66.setYearOld("Lớn hơn 6 tuổi");
            model66.setMin(15.3);
            model66.setMedium(20.2);
            model66.setMax(27.8);
            model66.setType("girl");
            WeightSample WeightSample66 = modelMapper.map(model66, WeightSample.class);
            WeightSample66.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample66.setCreatedDate(LocalDateTime.now());
            weightSampleRepository.save(WeightSample66);
        }
    }
}
