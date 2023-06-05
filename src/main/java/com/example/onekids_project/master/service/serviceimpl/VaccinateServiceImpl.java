package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.kids.Vaccinate;
import com.example.onekids_project.master.request.VaccinateRequest;
import com.example.onekids_project.master.service.VaccinateService;
import com.example.onekids_project.repository.VaccinateRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaccinateServiceImpl implements VaccinateService {
    @Autowired
    private VaccinateRespository vaccinateRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createVaccineDefault() {
        List<Vaccinate> vaccinateList = vaccinateRespository.findAll();
        if (CollectionUtils.isEmpty(vaccinateList)) {
            VaccinateRequest model1 = new VaccinateRequest();
            model1.setYearsOld("Sơ sinh");
            model1.setVaccinate("Lao");
            model1.setInjectNumber("1/1");
            Vaccinate vaccinate1 = modelMapper.map(model1, Vaccinate.class);
            vaccinate1.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate1.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate1);

            VaccinateRequest model2 = new VaccinateRequest();
            model2.setYearsOld("Sơ sinh");
            model2.setVaccinate("Viêm gan B");
            model2.setInjectNumber("1/5");
            Vaccinate vaccinate2 = modelMapper.map(model2, Vaccinate.class);
            vaccinate2.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate2.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate2);

            VaccinateRequest model3 = new VaccinateRequest();
            model3.setYearsOld("2 tháng tuổi");
            model3.setVaccinate("Viêm gan B");
            model3.setInjectNumber("2/5");
            Vaccinate vaccinate3 = modelMapper.map(model3, Vaccinate.class);
            vaccinate3.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate3.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate3);

            VaccinateRequest model4 = new VaccinateRequest();
            model4.setYearsOld("2 tháng tuổi");
            model4.setVaccinate("Viêm dạ dày ruột do Rota virus (Bỉ)");
            model4.setInjectNumber("1/2");
            Vaccinate vaccinate4 = modelMapper.map(model4, Vaccinate.class);
            vaccinate4.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate4.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate4);

            VaccinateRequest model5 = new VaccinateRequest();
            model5.setYearsOld("2 tháng tuổi");
            model5.setVaccinate("Bạch hầu, Ho gà, Uốn ván, Bại liệt, Màng não mủ, Viêm mũi họng, Viêm phổi,…");
            model5.setInjectNumber("1/5");
            Vaccinate vaccinate5 = modelMapper.map(model5, Vaccinate.class);
            vaccinate5.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate5.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate5);

            VaccinateRequest model6 = new VaccinateRequest();
            model6.setYearsOld("3 tháng tuổi");
            model6.setVaccinate("Viêm gan B");
            model6.setInjectNumber("3/5");
            Vaccinate vaccinate6 = modelMapper.map(model6, Vaccinate.class);
            vaccinate6.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate6.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate6);

            VaccinateRequest model7 = new VaccinateRequest();
            model7.setYearsOld("3 tháng tuổi");
            model7.setVaccinate("Viêm dạ dày ruột do Rota virus (Bỉ)");
            model7.setInjectNumber("2/2");
            Vaccinate vaccinate7 = modelMapper.map(model7, Vaccinate.class);
            vaccinate7.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate7.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate7);

            VaccinateRequest model8 = new VaccinateRequest();
            model8.setYearsOld("3 tháng tuổi");
            model8.setVaccinate("Bạch hầu, Ho gà, Uốn ván, Bại liệt, Màng não mủ, Viêm mũi họng, Viêm phổi,…");
            model8.setInjectNumber("2/5");
            Vaccinate vaccinate8 = modelMapper.map(model8, Vaccinate.class);
            vaccinate8.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate8.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate8);

            VaccinateRequest model9 = new VaccinateRequest();
            model9.setYearsOld("4 tháng tuổi");
            model9.setVaccinate("Bạch hầu, Ho gà, Uốn ván, Bại liệt, Màng não mủ, Viêm mũi họng, Viêm phổi,…");
            model9.setInjectNumber("3/5");
            Vaccinate vaccinate9 = modelMapper.map(model9, Vaccinate.class);
            vaccinate9.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate9.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate9);

            VaccinateRequest model10 = new VaccinateRequest();
            model10.setYearsOld("6 tháng tuổi");
            model10.setVaccinate("Cúm");
            model10.setInjectNumber("1/5");
            Vaccinate vaccinate10 = modelMapper.map(model10, Vaccinate.class);
            vaccinate10.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate10.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate10
            );

            VaccinateRequest model11 = new VaccinateRequest();
            model11.setYearsOld("7 tháng tuổi");
            model11.setVaccinate("Cúm");
            model11.setInjectNumber("1/5");
            Vaccinate vaccinate11 = modelMapper.map(model11, Vaccinate.class);
            vaccinate11.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate11.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate11);

            VaccinateRequest model12 = new VaccinateRequest();
            model12.setYearsOld("9 tháng tuổi");
            model12.setVaccinate("Sởi, Quai bị, Rubella");
            model12.setInjectNumber("1/3");
            Vaccinate vaccinate12 = modelMapper.map(model12, Vaccinate.class);
            vaccinate12.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate12.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate12);

            VaccinateRequest model13 = new VaccinateRequest();
            model13.setYearsOld("12 tháng tuổi");
            model13.setVaccinate("Thủy đậu");
            model13.setInjectNumber("1/1");
            Vaccinate vaccinate13 = modelMapper.map(model13, Vaccinate.class);
            vaccinate13.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate13.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate13);

            VaccinateRequest model14 = new VaccinateRequest();
            model14.setYearsOld("12 tháng tuổi");
            model14.setVaccinate("Viêm não nhật bản");
            model14.setInjectNumber("1/7");
            Vaccinate vaccinate14 = modelMapper.map(model14, Vaccinate.class);
            vaccinate14.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate14.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate14);

            VaccinateRequest model15 = new VaccinateRequest();
            model15.setYearsOld("12 tháng tuổi");
            model15.setVaccinate("Viêm não nhật bản");
            model15.setInjectNumber("2/7");
            Vaccinate vaccinate15 = modelMapper.map(model15, Vaccinate.class);
            vaccinate15.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate15.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate15);

            VaccinateRequest model16 = new VaccinateRequest();
            model16.setYearsOld("12 tháng tuổi");
            model16.setVaccinate("Viêm gan A");
            model16.setInjectNumber("1/2");
            Vaccinate vaccinate16 = modelMapper.map(model16, Vaccinate.class);
            vaccinate16.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate16.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate16);

            VaccinateRequest model17 = new VaccinateRequest();
            model17.setYearsOld("15 tháng tuổi");
            model17.setVaccinate("Viêm gan B");
            model17.setInjectNumber("4/5");
            Vaccinate vaccinate17 = modelMapper.map(model17, Vaccinate.class);
            vaccinate17.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate17.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate17);

            VaccinateRequest model18 = new VaccinateRequest();
            model18.setYearsOld("15 tháng tuổi");
            model18.setVaccinate("Sởi, Quai bị, Rubella");
            model18.setInjectNumber("2/3");
            Vaccinate vaccinate18 = modelMapper.map(model18, Vaccinate.class);
            vaccinate18.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate18.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate18);

            VaccinateRequest model19 = new VaccinateRequest();
            model19.setYearsOld("16 tháng tuổi");
            model19.setVaccinate("Bạch hầu, Ho gà, Uốn ván, Bại liệt, Màng não mủ, Viêm mũi họng, Viêm phổi,…");
            model19.setInjectNumber("4/5");
            Vaccinate vaccinate19 = modelMapper.map(model19, Vaccinate.class);
            vaccinate19.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate19.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate19);

            VaccinateRequest model20 = new VaccinateRequest();
            model20.setYearsOld("18 tháng tuổi");
            model20.setVaccinate("Viêm gan A");
            model20.setInjectNumber("2/2");
            Vaccinate vaccinate20 = modelMapper.map(model20, Vaccinate.class);
            vaccinate20.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate20.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate20);

            VaccinateRequest model21 = new VaccinateRequest();
            model21.setYearsOld("19 tháng tuổi");
            model21.setVaccinate("Cúm");
            model21.setInjectNumber("3/5");
            Vaccinate vaccinate21 = modelMapper.map(model21, Vaccinate.class);
            vaccinate21.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate21.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate21);

            VaccinateRequest model22 = new VaccinateRequest();
            model22.setYearsOld("24 tháng tuổi");
            model22.setVaccinate("Viêm não nhật bản");
            model22.setInjectNumber("3/7");
            Vaccinate vaccinate22 = modelMapper.map(model22, Vaccinate.class);
            vaccinate22.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate22.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate22);

            VaccinateRequest model23 = new VaccinateRequest();
            model23.setYearsOld("24 tháng tuổi");
            model23.setVaccinate("Viêm màng não do mô cầu");
            model23.setInjectNumber("1/5");
            Vaccinate vaccinate23 = modelMapper.map(model23, Vaccinate.class);
            vaccinate23.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate23.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate23);

            VaccinateRequest model24 = new VaccinateRequest();
            model24.setYearsOld("24 tháng tuổi");
            model24.setVaccinate("Viêm phổi do phế cầu khuẩn");
            model24.setInjectNumber("1/5");
            Vaccinate vaccinate24 = modelMapper.map(model24, Vaccinate.class);
            vaccinate24.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate24.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate24);

            VaccinateRequest model25 = new VaccinateRequest();
            model25.setYearsOld("24 tháng tuổi");
            model25.setVaccinate("Thương hàn");
            model25.setInjectNumber("1/6");
            Vaccinate vaccinate25 = modelMapper.map(model25, Vaccinate.class);
            vaccinate25.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate25.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate25);

            VaccinateRequest model26 = new VaccinateRequest();
            model26.setYearsOld("2 năm 7 tháng tuổi");
            model26.setVaccinate("Cúm");
            model26.setInjectNumber("4/5");
            Vaccinate vaccinate26 = modelMapper.map(model26, Vaccinate.class);
            vaccinate26.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate26.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate26);

            VaccinateRequest model27 = new VaccinateRequest();
            model27.setYearsOld("3 năm 7 tháng tuổi");
            model27.setVaccinate("Cúm");
            model27.setInjectNumber("5/5");
            Vaccinate vaccinate27 = modelMapper.map(model27, Vaccinate.class);
            vaccinate27.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate27.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate27);

            VaccinateRequest model28 = new VaccinateRequest();
            model28.setYearsOld("5 tuổi");
            model28.setVaccinate("Viêm não nhật bản");
            model28.setInjectNumber("4/7");
            Vaccinate vaccinate28 = modelMapper.map(model28, Vaccinate.class);
            vaccinate28.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate28.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate28);

            VaccinateRequest model29 = new VaccinateRequest();
            model29.setYearsOld("5 tuổi");
            model29.setVaccinate("Viêm màng não do mô cầu");
            model29.setInjectNumber("2/5");
            Vaccinate vaccinate29 = modelMapper.map(model29, Vaccinate.class);
            vaccinate29.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate29.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate29);

            VaccinateRequest model30 = new VaccinateRequest();
            model30.setYearsOld("5 tuổi");
            model30.setVaccinate("Viêm phổi do phế cầu khuẩn");
            model30.setInjectNumber("2/5");
            Vaccinate vaccinate30 = modelMapper.map(model30, Vaccinate.class);
            vaccinate30.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate30.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate30);

            VaccinateRequest model31 = new VaccinateRequest();
            model31.setYearsOld("5 tuổi");
            model31.setVaccinate("Thương hàn");
            model31.setInjectNumber("2/6");
            Vaccinate vaccinate31 = modelMapper.map(model31, Vaccinate.class);
            vaccinate31.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate31.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate31);

            VaccinateRequest model32 = new VaccinateRequest();
            model32.setYearsOld("5 năm 3 tháng tuổi");
            model32.setVaccinate("Sởi, Quai bị, Rubella");
            model32.setInjectNumber("3/3");
            Vaccinate vaccinate32 = modelMapper.map(model32, Vaccinate.class);
            vaccinate32.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate32.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate32);

            VaccinateRequest model33 = new VaccinateRequest();
            model33.setYearsOld("8 tuổi");
            model33.setVaccinate("Viêm não nhật bản");
            model33.setInjectNumber("5/7");
            Vaccinate vaccinate33 = modelMapper.map(model33, Vaccinate.class);
            vaccinate33.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate33.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate33);

            VaccinateRequest model34 = new VaccinateRequest();
            model34.setYearsOld("8 tuổi");
            model34.setVaccinate("Viêm màng não do mô cầu");
            model34.setInjectNumber("3/5");
            Vaccinate vaccinate34 = modelMapper.map(model34, Vaccinate.class);
            vaccinate34.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate34.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate34);

            VaccinateRequest model35 = new VaccinateRequest();
            model35.setYearsOld("8 tuổi");
            model35.setVaccinate("Viêm phổi do phế cầu khuẩn");
            model35.setInjectNumber("3/5");
            Vaccinate vaccinate35 = modelMapper.map(model35, Vaccinate.class);
            vaccinate35.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate35.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate35);

            VaccinateRequest model36 = new VaccinateRequest();
            model36.setYearsOld("8 tuổi");
            model36.setVaccinate("Thương hàn");
            model36.setInjectNumber("3/6");
            Vaccinate vaccinate36 = modelMapper.map(model36, Vaccinate.class);
            vaccinate36.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate36.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate36);

            VaccinateRequest model37 = new VaccinateRequest();
            model37.setYearsOld("9 tuổi");
            model37.setVaccinate("Ung thu cổ tử cung (Cervarix - Bỉ)");
            model37.setInjectNumber("1/3");
            Vaccinate vaccinate37 = modelMapper.map(model37, Vaccinate.class);
            vaccinate37.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate37.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate37);

            VaccinateRequest model38 = new VaccinateRequest();
            model38.setYearsOld("9 năm 3 tháng tuổi");
            model38.setVaccinate("Viêm gan B");
            model38.setInjectNumber("5/5");
            Vaccinate vaccinate38 = modelMapper.map(model38, Vaccinate.class);
            vaccinate38.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate38.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate38);

            VaccinateRequest model39 = new VaccinateRequest();
            model39.setYearsOld("9 năm 3 tháng tuổi");
            model39.setVaccinate("Ung thu cổ tử cung (Cervarix - Bỉ)");
            model39.setInjectNumber("2/3");
            Vaccinate vaccinate39 = modelMapper.map(model39, Vaccinate.class);
            vaccinate39.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate39.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate39);

            VaccinateRequest model40 = new VaccinateRequest();
            model40.setYearsOld("9 năm 4 tháng tuổi");
            model40.setVaccinate("Bạch hầu, Ho gà, Uốn ván, Bại liệt, Màng não mủ, Viêm mũi họng, Viêm phổi,…");
            model40.setInjectNumber("5/5");
            Vaccinate vaccinate40 = modelMapper.map(model40, Vaccinate.class);
            vaccinate40.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate40.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate40);

            VaccinateRequest model41 = new VaccinateRequest();
            model41.setYearsOld("11 tuổi");
            model41.setVaccinate("Viêm não nhật bản");
            model41.setInjectNumber("6/7");
            Vaccinate vaccinate41 = modelMapper.map(model41, Vaccinate.class);
            vaccinate41.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate41.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate41);

            VaccinateRequest model42 = new VaccinateRequest();
            model42.setYearsOld("11 tuổi");
            model42.setVaccinate("Viêm màng não do mô cầu");
            model42.setInjectNumber("4/5");
            Vaccinate vaccinate42 = modelMapper.map(model42, Vaccinate.class);
            vaccinate42.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate42.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate42);

            VaccinateRequest model43 = new VaccinateRequest();
            model43.setYearsOld("11 tuổi");
            model43.setVaccinate("Viêm phổi do phế cầu khuẩn");
            model43.setInjectNumber("4/5");
            Vaccinate vaccinate43 = modelMapper.map(model43, Vaccinate.class);
            vaccinate43.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate43.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate43);

            VaccinateRequest model44 = new VaccinateRequest();
            model44.setYearsOld("11 tuổi");
            model44.setVaccinate("Thương hàn");
            model44.setInjectNumber("4/6");
            Vaccinate vaccinate44 = modelMapper.map(model44, Vaccinate.class);
            vaccinate44.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate44.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate44);

            VaccinateRequest model45 = new VaccinateRequest();
            model45.setYearsOld("14 tuổi");
            model45.setVaccinate("Viêm não nhật bản");
            model45.setInjectNumber("7/7");
            Vaccinate vaccinate45 = modelMapper.map(model45, Vaccinate.class);
            vaccinate45.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate45.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate45);

            VaccinateRequest model46 = new VaccinateRequest();
            model46.setYearsOld("14 tuổi");
            model46.setVaccinate("Viêm màng não do mô cầu");
            model46.setInjectNumber("5/5");
            Vaccinate vaccinate46 = modelMapper.map(model46, Vaccinate.class);
            vaccinate46.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate46.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate46);

            VaccinateRequest model47 = new VaccinateRequest();
            model47.setYearsOld("14 tuổi");
            model47.setVaccinate("Viêm phổi do phế cầu khuẩn");
            model47.setInjectNumber("5/5");
            Vaccinate vaccinate47 = modelMapper.map(model47, Vaccinate.class);
            vaccinate47.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate47.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate47);

            VaccinateRequest model48 = new VaccinateRequest();
            model48.setYearsOld("14 tuổi");
            model48.setVaccinate("Thương hàn");
            model48.setInjectNumber("5/6");
            Vaccinate vaccinate48 = modelMapper.map(model48, Vaccinate.class);
            vaccinate48.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate48.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate48);

            VaccinateRequest model49 = new VaccinateRequest();
            model49.setYearsOld("17 tuổi");
            model49.setVaccinate("Thương hàn");
            model49.setInjectNumber("6/6");
            Vaccinate vaccinate49 = modelMapper.map(model49, Vaccinate.class);
            vaccinate49.setIdCreated(SystemConstant.ID_SYSTEM);
            vaccinate49.setCreatedDate(LocalDateTime.now());
            vaccinateRespository.save(vaccinate49);

        }
    }
}
