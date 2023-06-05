package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsVaccinate;
import com.example.onekids_project.entity.kids.Vaccinate;
import com.example.onekids_project.mobile.parent.request.VaccinateParentRequest;
import com.example.onekids_project.mobile.parent.response.kids.VaccinateParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.VaccinateParentService;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.KidsVaccinateRepository;
import com.example.onekids_project.repository.VaccinateRespository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VaccinateParentServiceImpl implements VaccinateParentService {
    @Autowired
    private VaccinateRespository vaccinateRespository;

    @Autowired
    private KidsVaccinateRepository kidsVaccinateRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public List<VaccinateParentResponse> finVaccinateList(UserPrincipal principal) {
        List<Vaccinate> vaccinateList = vaccinateRespository.findAll();
        List<KidsVaccinate> kidsVaccinateList = kidsVaccinateRepository.findByKidIdAndDoneTrue(principal.getIdKidLogin());
        List<VaccinateParentResponse> dataList = new ArrayList<>();
        vaccinateList.forEach(x -> {
            VaccinateParentResponse model = new VaccinateParentResponse();
            model.setId(x.getId());
            model.setYearsOld(x.getYearsOld());
            model.setVaccinate(x.getVaccinate());
            model.setInjectNumber(x.getInjectNumber());
            kidsVaccinateList.forEach(y -> {
                if (y.getVaccinate().getId().equals(x.getId())) {
                    model.setStatus(AppConstant.APP_TRUE);
                    model.setDate(y.getDate());
                }
            });
            dataList.add(model);
        });
        return dataList;
    }

    @Override
    public VaccinateParentResponse saveVaccinate(UserPrincipal principal, VaccinateParentRequest vaccinateParentRequest) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id in vaccinate"));
        Vaccinate vaccinate = vaccinateRespository.findById(vaccinateParentRequest.getId()).orElseThrow(() -> new NotFoundException("not found vaccinate by id"));
        Optional<KidsVaccinate> vaccinateOptional = kidsVaccinateRepository.findByVaccinateIdAndKidId(vaccinateParentRequest.getId(), principal.getIdKidLogin());
        KidsVaccinate kidsVaccinate;
        //tạo mới
        if (vaccinateOptional.isEmpty()) {
            kidsVaccinate = new KidsVaccinate();
            kidsVaccinate.setKid(kids);
            kidsVaccinate.setVaccinate(vaccinate);
            kidsVaccinate.setDate(vaccinateParentRequest.getDate());
            kidsVaccinate.setDone(vaccinateParentRequest.getStatus());
        } else {
            //cập nhật
            kidsVaccinate = vaccinateOptional.get();
            kidsVaccinate.setDate(vaccinateParentRequest.getDate());
            kidsVaccinate.setDone(vaccinateParentRequest.getStatus());
        }
        KidsVaccinate dataSaved = kidsVaccinateRepository.save(kidsVaccinate);
        VaccinateParentResponse response = new VaccinateParentResponse();
        response.setId(vaccinate.getId());
        response.setYearsOld(vaccinate.getYearsOld());
        response.setVaccinate(vaccinate.getVaccinate());
        response.setInjectNumber(vaccinate.getInjectNumber());
        response.setStatus(dataSaved.isDone());
        response.setDate(dataSaved.getDate());
        return response;
    }
}
