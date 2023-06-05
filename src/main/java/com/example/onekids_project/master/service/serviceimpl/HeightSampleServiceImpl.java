package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.sample.HeightSample;
import com.example.onekids_project.master.request.HeightSampleRequest;
import com.example.onekids_project.master.service.HeightSampleService;
import com.example.onekids_project.repository.HeightSampleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HeightSampleServiceImpl implements HeightSampleService {
    @Autowired
    private HeightSampleRepository heightSampleRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public void createHeightDefault() {
        List<HeightSample> weightSampleList = heightSampleRepository.findAll();
        if (CollectionUtils.isEmpty(weightSampleList)) {
            HeightSampleRequest model1 = new HeightSampleRequest();
            model1.setYearOld("Sơ sinh");
            model1.setMin(46.1);
            model1.setMedium(49.9);
            model1.setMax(53.7);
            model1.setType("boy");
            HeightSample weightSample1 = modelMapper.map(model1, HeightSample.class);
            weightSample1.setIdCreated(SystemConstant.ID_SYSTEM);
            weightSample1.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(weightSample1);

            HeightSampleRequest model2 = new HeightSampleRequest();
            model2.setYearOld("1 tháng");
            model2.setMin(50.8);
            model2.setMedium(54.7);
            model2.setMax(58.6);
            model2.setType("boy");
            HeightSample WeightSample2 = modelMapper.map(model2, HeightSample.class);
            WeightSample2.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample2.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample2);

            HeightSampleRequest model3 = new HeightSampleRequest();
            model3.setYearOld("2 tháng");
            model3.setMin(54.4);
            model3.setMedium(58.4);
            model3.setMax(62.4);
            model3.setType("boy");
            HeightSample WeightSample3 = modelMapper.map(model3, HeightSample.class);
            WeightSample3.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample3.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample3);

            HeightSampleRequest model4 = new HeightSampleRequest();
            model4.setYearOld("3 tháng");
            model4.setMin(57.3);
            model4.setMedium(61.4);
            model4.setMax(65.5);
            model4.setType("boy");
            HeightSample WeightSample4 = modelMapper.map(model4, HeightSample.class);
            WeightSample4.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample4.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample4);

            HeightSampleRequest model5 = new HeightSampleRequest();
            model5.setYearOld("4 tháng");
            model5.setMin(59.7);
            model5.setMedium(63.9);
            model5.setMax(68);
            model5.setType("boy");
            HeightSample WeightSample5 = modelMapper.map(model5, HeightSample.class);
            WeightSample5.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample5.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample5);

            HeightSampleRequest model6 = new HeightSampleRequest();
            model6.setYearOld("5 tháng");
            model6.setMin(61.7);
            model6.setMedium(65.9);
            model6.setMax(70.1);
            model6.setType("boy");
            HeightSample WeightSample6 = modelMapper.map(model6, HeightSample.class);
            WeightSample6.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample6.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample6);

            HeightSampleRequest model699 = new HeightSampleRequest();
            model699.setYearOld("6 tháng");
            model699.setMin(63.3);
            model699.setMedium(67.6);
            model699.setMax(71.9);
            model699.setType("boy");
            HeightSample WeightSample699 = modelMapper.map(model699, HeightSample.class);
            WeightSample699.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample699.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample699);

            HeightSampleRequest model700 = new HeightSampleRequest();
            model700.setYearOld("7 tháng");
            model700.setMin(64.8);
            model700.setMedium(69.2);
            model700.setMax(73.5);
            model700.setType("boy");
            HeightSample WeightSample700 = modelMapper.map(model700, HeightSample.class);
            WeightSample700.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample700.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample700);


            HeightSampleRequest model7 = new HeightSampleRequest();
            model7.setYearOld("8 tháng");
            model7.setMin(66.2);
            model7.setMedium(70.6);
            model7.setMax(75);
            model7.setType("boy");
            HeightSample WeightSample7 = modelMapper.map(model7, HeightSample.class);
            WeightSample7.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample7.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample7);

            HeightSampleRequest model8 = new HeightSampleRequest();
            model8.setYearOld("9 tháng");
            model8.setMin(67.5);
            model8.setMedium(72);
            model8.setMax(76.5);
            model8.setType("boy");
            HeightSample WeightSample8 = modelMapper.map(model8, HeightSample.class);
            WeightSample8.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample8.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample8);

            HeightSampleRequest model9 = new HeightSampleRequest();
            model9.setYearOld("10 tháng");
            model9.setMin(68.7);
            model9.setMedium(73.3);
            model9.setMax(77.9);
            model9.setType("boy");
            HeightSample WeightSample9 = modelMapper.map(model9, HeightSample.class);
            WeightSample9.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample9.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample9);

            HeightSampleRequest model10 = new HeightSampleRequest();
            model10.setYearOld("11 tháng");
            model10.setMin(69.9);
            model10.setMedium(74.5);
            model10.setMax(79.2);
            model10.setType("boy");
            HeightSample WeightSample10 = modelMapper.map(model10, HeightSample.class);
            WeightSample10.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample10.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample10);

            HeightSampleRequest model11 = new HeightSampleRequest();
            model11.setYearOld("12 tháng");
            model11.setMin(71);
            model11.setMedium(75.7);
            model11.setMax(80.5);
            model11.setType("boy");
            HeightSample WeightSample11 = modelMapper.map(model11, HeightSample.class);
            WeightSample11.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample11.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample11);

            HeightSampleRequest model12 = new HeightSampleRequest();
            model12.setYearOld("13 tháng");
            model12.setMin(72.4);
            model12.setMedium(76.8);
            model12.setMax(81.8);
            model12.setType("boy");
            HeightSample WeightSample12 = modelMapper.map(model12, HeightSample.class);
            WeightSample12.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample12.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample12);

            HeightSampleRequest model13 = new HeightSampleRequest();
            model13.setYearOld("14 tháng");
            model13.setMin(73.4);
            model13.setMedium(77.9);
            model13.setMax(83);
            model13.setType("boy");
            HeightSample WeightSample13 = modelMapper.map(model13, HeightSample.class);
            WeightSample13.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample13.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample13);

            HeightSampleRequest model14 = new HeightSampleRequest();
            model14.setYearOld("15 tháng");
            model14.setMin(74.1);
            model14.setMedium(79.1);
            model14.setMax(84.2);
            model14.setType("boy");
            HeightSample WeightSample14 = modelMapper.map(model14, HeightSample.class);
            WeightSample14.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample14.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample14);

            HeightSampleRequest model15 = new HeightSampleRequest();
            model15.setYearOld("16 tháng");
            model15.setMin(75.1);
            model15.setMedium(80.2);
            model15.setMax(85.4);
            model15.setType("boy");
            HeightSample WeightSample15 = modelMapper.map(model15, HeightSample.class);
            WeightSample15.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample15.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample15);

            HeightSampleRequest model16 = new HeightSampleRequest();
            model16.setYearOld("17 tháng");
            model16.setMin(76);
            model16.setMedium(81.3);
            model16.setMax(86.6);
            model16.setType("boy");
            HeightSample WeightSample16 = modelMapper.map(model16, HeightSample.class);
            WeightSample16.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample16.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample16);

            HeightSampleRequest model17 = new HeightSampleRequest();
            model17.setYearOld("18 tháng");
            model17.setMin(76.9);
            model17.setMedium(82.3);
            model17.setMax(87.7);
            model17.setType("boy");
            HeightSample WeightSample17 = modelMapper.map(model17, HeightSample.class);
            WeightSample17.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample17.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample17);

            HeightSampleRequest model18 = new HeightSampleRequest();
            model18.setYearOld("19 tháng");
            model18.setMin(77.8);
            model18.setMedium(83.2);
            model18.setMax(88.7);
            model18.setType("boy");
            HeightSample WeightSample18 = modelMapper.map(model18, HeightSample.class);
            WeightSample18.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample18.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample18);

            HeightSampleRequest model19 = new HeightSampleRequest();
            model19.setYearOld("20 tháng");
            model19.setMin(78.7);
            model19.setMedium(84.1);
            model19.setMax(89.8);
            model19.setType("boy");
            HeightSample WeightSample19 = modelMapper.map(model19, HeightSample.class);
            WeightSample19.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample19.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample19);

            HeightSampleRequest model20 = new HeightSampleRequest();
            model20.setYearOld("21 tháng");
            model20.setMin(79.4);
            model20.setMedium(85.1);
            model20.setMax(90.9);
            model20.setType("boy");
            HeightSample WeightSample20 = modelMapper.map(model20, HeightSample.class);
            WeightSample20.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample20.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample20);

            HeightSampleRequest model21 = new HeightSampleRequest();
            model21.setYearOld("22 tháng");
            model21.setMin(80);
            model21.setMedium(85.8);
            model21.setMax(91.7);
            model21.setType("boy");
            HeightSample WeightSample21 = modelMapper.map(model21, HeightSample.class);
            WeightSample21.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample21.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample21);

            HeightSampleRequest model22 = new HeightSampleRequest();
            model22.setYearOld("23 tháng");
            model22.setMin(80.5);
            model22.setMedium(86.4);
            model22.setMax(92.4);
            model22.setType("boy");
            HeightSample WeightSample22 = modelMapper.map(model22, HeightSample.class);
            WeightSample22.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample22.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample22);

            HeightSampleRequest model23 = new HeightSampleRequest();
            model23.setYearOld("24 tháng");
            model23.setMin(81);
            model23.setMedium(87.1);
            model23.setMax(93.2);
            model23.setType("boy");
            HeightSample WeightSample23 = modelMapper.map(model23, HeightSample.class);
            WeightSample23.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample23.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample23);

            HeightSampleRequest model24 = new HeightSampleRequest();
            model24.setYearOld("2.5 tuổi");
            model24.setMin(85.1);
            model24.setMedium(91.9);
            model24.setMax(98.7);
            model24.setType("boy");
            HeightSample WeightSample24 = modelMapper.map(model24, HeightSample.class);
            WeightSample24.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample24.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample24);

            HeightSampleRequest model25 = new HeightSampleRequest();
            model25.setYearOld("3 tuổi");
            model25.setMin(88.7);
            model25.setMedium(96.1);
            model25.setMax(103.5);
            model25.setType("boy");
            HeightSample WeightSample25 = modelMapper.map(model25, HeightSample.class);
            WeightSample25.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample25.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample25);

            HeightSampleRequest model26 = new HeightSampleRequest();
            model26.setYearOld("3.5 tuổi");
            model26.setMin(91.9);
            model26.setMedium(99.9);
            model26.setMax(107.8);
            model26.setType("boy");
            HeightSample WeightSample26 = modelMapper.map(model26, HeightSample.class);
            WeightSample26.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample26.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample26);

            HeightSampleRequest model27 = new HeightSampleRequest();
            model27.setYearOld("4 tuổi");
            model27.setMin(94.9);
            model27.setMedium(103.3);
            model27.setMax(111.7);
            model27.setType("boy");
            HeightSample WeightSample27 = modelMapper.map(model27, HeightSample.class);
            WeightSample27.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample27.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample27);

            HeightSampleRequest model28 = new HeightSampleRequest();
            model28.setYearOld("4.5 tuổi");
            model28.setMin(97.8);
            model28.setMedium(106.7);
            model28.setMax(115.5);
            model28.setType("boy");
            HeightSample WeightSample28 = modelMapper.map(model28, HeightSample.class);
            WeightSample28.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample28.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample28);

            HeightSampleRequest model29 = new HeightSampleRequest();
            model29.setYearOld("5 tuổi");
            model29.setMin(100.7);
            model29.setMedium(110);
            model29.setMax(119.2);
            model29.setType("boy");
            HeightSample WeightSample29 = modelMapper.map(model29, HeightSample.class);
            WeightSample29.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample29.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample29);

            HeightSampleRequest model30 = new HeightSampleRequest();
            model30.setYearOld("5.5 tuổi");
            model30.setMin(103.4);
            model30.setMedium(112.9);
            model30.setMax(122.4);
            model30.setType("boy");
            HeightSample WeightSample30 = modelMapper.map(model30, HeightSample.class);
            WeightSample30.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample30.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample30);

            HeightSampleRequest model31 = new HeightSampleRequest();
            model31.setYearOld("6 tuổi");
            model31.setMin(106.1);
            model31.setMedium(116);
            model31.setMax(125.8);
            model31.setType("boy");
            HeightSample WeightSample31 = modelMapper.map(model31, HeightSample.class);
            WeightSample31.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample31.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample31);

            HeightSampleRequest model33 = new HeightSampleRequest();
            model33.setYearOld("Lớn hơn 6 tuổi");
            model33.setMin(106.1);
            model33.setMedium(116);
            model33.setMax(125.8);
            model33.setType("boy");
            HeightSample WeightSample33 = modelMapper.map(model33, HeightSample.class);
            WeightSample33.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample33.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample33);


            // weightsample girl

            HeightSampleRequest model35 = new HeightSampleRequest();
            model35.setYearOld("Sơ sinh");
            model35.setMin(45.4);
            model35.setMedium(49.1);
            model35.setMax(52.9);
            model35.setType("girl");
            HeightSample weightSample35 = modelMapper.map(model35, HeightSample.class);
            weightSample35.setIdCreated(SystemConstant.ID_SYSTEM);
            weightSample35.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(weightSample35);

            HeightSampleRequest model36 = new HeightSampleRequest();
            model36.setYearOld("1 tháng");
            model36.setMin(49.8);
            model36.setMedium(53.7);
            model36.setMax(57.6);
            model36.setType("girl");
            HeightSample WeightSample36 = modelMapper.map(model36, HeightSample.class);
            WeightSample36.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample36.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample36);

            HeightSampleRequest model37 = new HeightSampleRequest();
            model37.setYearOld("2 tháng");
            model37.setMin(53);
            model37.setMedium(57.1);
            model37.setMax(61.1);
            model37.setType("girl");
            HeightSample WeightSample37 = modelMapper.map(model37, HeightSample.class);
            WeightSample37.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample37.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample37);

            HeightSampleRequest model38 = new HeightSampleRequest();
            model38.setYearOld("3 tháng");
            model38.setMin(55.6);
            model38.setMedium(59.8);
            model38.setMax(64);
            model38.setType("girl");
            HeightSample WeightSample38 = modelMapper.map(model38, HeightSample.class);
            WeightSample38.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample38.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample38);

            HeightSampleRequest model39 = new HeightSampleRequest();
            model39.setYearOld("4 tháng");
            model39.setMin(57.8);
            model39.setMedium(62.1);
            model39.setMax(66.4);
            model39.setType("girl");
            HeightSample WeightSample39 = modelMapper.map(model39, HeightSample.class);
            WeightSample39.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample39.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample39);

            HeightSampleRequest model40 = new HeightSampleRequest();
            model40.setYearOld("5 tháng");
            model40.setMin(59.6);
            model40.setMedium(64);
            model40.setMax(68.5);
            model40.setType("girl");
            HeightSample WeightSample40 = modelMapper.map(model40, HeightSample.class);
            WeightSample40.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample40.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample40);

            HeightSampleRequest model6999 = new HeightSampleRequest();
            model6999.setYearOld("6 tháng");
            model6999.setMin(61.2);
            model6999.setMedium(65.7);
            model6999.setMax(70.3);
            model6999.setType("girl");
            HeightSample WeightSample6999 = modelMapper.map(model6999, HeightSample.class);
            WeightSample6999.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample6999.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample6999);

            HeightSampleRequest model7000 = new HeightSampleRequest();
            model7000.setYearOld("7 tháng");
            model7000.setMin(62.7);
            model7000.setMedium(67.3);
            model7000.setMax(71.9);
            model7000.setType("girl");
            HeightSample WeightSample7000 = modelMapper.map(model7000, HeightSample.class);
            WeightSample7000.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample7000.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample7000);

            HeightSampleRequest model41 = new HeightSampleRequest();
            model41.setYearOld("8 tháng");
            model41.setMin(64);
            model41.setMedium(68.7);
            model41.setMax(73.5);
            model41.setType("girl");
            HeightSample WeightSample41 = modelMapper.map(model41, HeightSample.class);
            WeightSample41.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample41.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample41);

            HeightSampleRequest model42 = new HeightSampleRequest();
            model42.setYearOld("9 tháng");
            model42.setMin(65.3);
            model42.setMedium(70.1);
            model42.setMax(75);
            model42.setType("girl");
            HeightSample WeightSample42 = modelMapper.map(model42, HeightSample.class);
            WeightSample42.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample42.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample42);

            HeightSampleRequest model43 = new HeightSampleRequest();
            model43.setYearOld("10 tháng");
            model43.setMin(66.5);
            model43.setMedium(71.5);
            model43.setMax(76.4);
            model43.setType("girl");
            HeightSample WeightSample43 = modelMapper.map(model43, HeightSample.class);
            WeightSample43.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample43.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample43);

            HeightSampleRequest model44 = new HeightSampleRequest();
            model44.setYearOld("11 tháng");
            model44.setMin(67.7);
            model44.setMedium(72.8);
            model44.setMax(77.8);
            model44.setType("girl");
            HeightSample WeightSample44 = modelMapper.map(model44, HeightSample.class);
            WeightSample44.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample44.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample44);

            HeightSampleRequest model45 = new HeightSampleRequest();
            model45.setYearOld("12 tháng");
            model45.setMin(68.9);
            model45.setMedium(74);
            model45.setMax(79.2);
            model45.setType("girl");
            HeightSample WeightSample45 = modelMapper.map(model45, HeightSample.class);
            WeightSample45.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample45.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample45);

            HeightSampleRequest model46 = new HeightSampleRequest();
            model46.setYearOld("13 tháng");
            model46.setMin(70);
            model46.setMedium(75.2);
            model46.setMax(80.5);
            model46.setType("girl");
            HeightSample WeightSample46 = modelMapper.map(model46, HeightSample.class);
            WeightSample46.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample46.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample46);

            HeightSampleRequest model47 = new HeightSampleRequest();
            model47.setYearOld("14 tháng");
            model47.setMin(71);
            model47.setMedium(76.4);
            model47.setMax(81.7);
            model47.setType("girl");
            HeightSample WeightSample47 = modelMapper.map(model47, HeightSample.class);
            WeightSample47.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample47.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample47);

            HeightSampleRequest model48 = new HeightSampleRequest();
            model48.setYearOld("15 tháng");
            model48.setMin(72);
            model48.setMedium(77.5);
            model48.setMax(83);
            model48.setType("girl");
            HeightSample WeightSample48 = modelMapper.map(model48, HeightSample.class);
            WeightSample48.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample48.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample48);

            HeightSampleRequest model49 = new HeightSampleRequest();
            model49.setYearOld("16 tháng");
            model49.setMin(73);
            model49.setMedium(78.7);
            model49.setMax(84.2);
            model49.setType("girl");
            HeightSample WeightSample49 = modelMapper.map(model49, HeightSample.class);
            WeightSample49.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample49.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample49);

            HeightSampleRequest model50 = new HeightSampleRequest();
            model50.setYearOld("17 tháng");
            model50.setMin(74);
            model50.setMedium(79.8);
            model50.setMax(85.3);
            model50.setType("girl");
            HeightSample WeightSample50 = modelMapper.map(model50, HeightSample.class);
            WeightSample50.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample50.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample50);

            HeightSampleRequest model51 = new HeightSampleRequest();
            model51.setYearOld("18 tháng");
            model51.setMin(74.9);
            model51.setMedium(80.7);
            model51.setMax(86.5);
            model51.setType("girl");
            HeightSample WeightSample51 = modelMapper.map(model51, HeightSample.class);
            WeightSample51.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample51.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample51);

            HeightSampleRequest model52 = new HeightSampleRequest();
            model52.setYearOld("19 tháng");
            model52.setMin(75.8);
            model52.setMedium(81.7);
            model52.setMax(87.6);
            model52.setType("girl");
            HeightSample WeightSample52 = modelMapper.map(model52, HeightSample.class);
            WeightSample52.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample52.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample52);

            HeightSampleRequest model53 = new HeightSampleRequest();
            model53.setYearOld("20 tháng");
            model53.setMin(76.7);
            model53.setMedium(82.7);
            model53.setMax(88.7);
            model53.setType("girl");
            HeightSample WeightSample53 = modelMapper.map(model53, HeightSample.class);
            WeightSample53.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample53.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample53);

            HeightSampleRequest model54 = new HeightSampleRequest();
            model54.setYearOld("21 tháng");
            model54.setMin(77.5);
            model54.setMedium(83.7);
            model54.setMax(89.8);
            model54.setType("girl");
            HeightSample WeightSample54 = modelMapper.map(model54, HeightSample.class);
            WeightSample54.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample54.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample54);

            HeightSampleRequest model55 = new HeightSampleRequest();
            model55.setYearOld("22 tháng");
            model55.setMin(78.4);
            model55.setMedium(84.6);
            model55.setMax(90.8);
            model55.setType("girl");
            HeightSample WeightSample55 = modelMapper.map(model55, HeightSample.class);
            WeightSample55.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample55.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample55);

            HeightSampleRequest model56 = new HeightSampleRequest();
            model56.setYearOld("23 tháng");
            model56.setMin(79.8);
            model56.setMedium(85.5);
            model56.setMax(91.9);
            model56.setType("girl");
            HeightSample WeightSample56 = modelMapper.map(model56, HeightSample.class);
            WeightSample56.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample56.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample56);

            HeightSampleRequest model57 = new HeightSampleRequest();
            model57.setYearOld("24 tháng");
            model57.setMin(80);
            model57.setMedium(86.4);
            model57.setMax(92.9);
            model57.setType("girl");
            HeightSample WeightSample57 = modelMapper.map(model57, HeightSample.class);
            WeightSample57.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample57.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample57);

            HeightSampleRequest model58 = new HeightSampleRequest();
            model58.setYearOld("2.5 tuổi");
            model58.setMin(83.6);
            model58.setMedium(90.7);
            model58.setMax(97.7);
            model58.setType("girl");
            HeightSample WeightSample58 = modelMapper.map(model58, HeightSample.class);
            WeightSample58.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample58.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample58);

            HeightSampleRequest model59 = new HeightSampleRequest();
            model59.setYearOld("3 tuổi");
            model59.setMin(87.4);
            model59.setMedium(95.1);
            model59.setMax(102.7);
            model59.setType("girl");
            HeightSample WeightSample59 = modelMapper.map(model59, HeightSample.class);
            WeightSample59.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample59.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample59);

            HeightSampleRequest model60 = new HeightSampleRequest();
            model60.setYearOld("3.5 tuổi");
            model60.setMin(90.9);
            model60.setMedium(99);
            model60.setMax(107.2);
            model60.setType("girl");
            HeightSample WeightSample60 = modelMapper.map(model60, HeightSample.class);
            WeightSample60.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample60.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample60);

            HeightSampleRequest model61 = new HeightSampleRequest();
            model61.setYearOld("4 tuổi");
            model61.setMin(94.1);
            model61.setMedium(102.7);
            model61.setMax(111.3);
            model61.setType("girl");
            HeightSample WeightSample61 = modelMapper.map(model61, HeightSample.class);
            WeightSample61.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample61.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample61);

            HeightSampleRequest model62 = new HeightSampleRequest();
            model62.setYearOld("4.5 tuổi");
            model62.setMin(97.1);
            model62.setMedium(106.2);
            model62.setMax(115.2);
            model62.setType("girl");
            HeightSample WeightSample62 = modelMapper.map(model62, HeightSample.class);
            WeightSample62.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample62.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample62);

            HeightSampleRequest model63 = new HeightSampleRequest();
            model63.setYearOld("5 tuổi");
            model63.setMin(99.9);
            model63.setMedium(109.4);
            model63.setMax(118.9);
            model63.setType("girl");
            HeightSample WeightSample63 = modelMapper.map(model63, HeightSample.class);
            WeightSample63.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample63.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample63);

            HeightSampleRequest model64 = new HeightSampleRequest();
            model64.setYearOld("5.5 tuổi");
            model64.setMin(102.3);
            model64.setMedium(112.2);
            model64.setMax(122);
            model64.setType("girl");
            HeightSample WeightSample64 = modelMapper.map(model64, HeightSample.class);
            WeightSample64.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample64.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample64);

            HeightSampleRequest model65 = new HeightSampleRequest();
            model65.setYearOld("6 tuổi");
            model65.setMin(104.9);
            model65.setMedium(115.1);
            model65.setMax(125.4);
            model65.setType("girl");
            HeightSample WeightSample65 = modelMapper.map(model65, HeightSample.class);
            WeightSample65.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample65.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample65);

            HeightSampleRequest model66 = new HeightSampleRequest();
            model66.setYearOld("Lớn hơn 6 tuổi");
            model66.setMin(105);
            model66.setMedium(118);
            model66.setMax(128);
            model66.setType("girl");
            HeightSample WeightSample66 = modelMapper.map(model66, HeightSample.class);
            WeightSample66.setIdCreated(SystemConstant.ID_SYSTEM);
            WeightSample66.setCreatedDate(LocalDateTime.now());
            heightSampleRepository.save(WeightSample66);
        }
    }
}
