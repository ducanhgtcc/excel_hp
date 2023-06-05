package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.kids.ExAlbumKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.parent.response.album.AlbumDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.album.ExAlbumKidsResponse;
import com.example.onekids_project.mobile.parent.response.album.ListExAlbumKidsMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AlbumMobileService;
import com.example.onekids_project.repository.AlbumRepository;
import com.example.onekids_project.repository.ExAlbumKidsRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumMobileServiceImpl implements AlbumMobileService {

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ExAlbumKidsRepository exAlbumKidsRepository;

    @Transactional
    @Override
    public AlbumDetailMobileResponse findAlbummobdetail(UserPrincipal principal, Long id) {
        Album album = albumRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        Long idKid = principal.getIdKidLogin();
        Long idAlbum = album.getId();
        ExAlbumKids exAlbumKids = exAlbumKidsRepository.findByKidsIdAndAlbumId(idKid, idAlbum).orElseThrow();
        exAlbumKids.setStatusUnread(AppConstant.APP_TRUE);
        exAlbumKidsRepository.save(exAlbumKids);
        AlbumDetailMobileResponse model = new AlbumDetailMobileResponse();
        model.setAlbumType(album.getAlbumType());
        model.setAlbumName(album.getAlbumName());
        model.setAlbumDescription(album.getAlbumDescription());
        model.setCreatedBy(album.getCreatedBy());
        model.setCreatedDate(album.getCreatedDate());
        int cout = (int) album.getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
        model.setPictureNumber(cout);
        model.setPictureList(album.getAlistPictureList().stream().filter(ListPicture::getIsApproved).map(ListPicture::getUrlPicture).collect(Collectors.toList()));
        List<ListPicture> listPictureList = album.getAlistPictureList().stream().filter(ListPicture::getIsApproved).collect(Collectors.toList());
        model.setUrlPictureFirst(listPictureList.get(0).getUrlPicture());
        albumRepository.save(album);
        return model;
    }

    @Override
    public ListExAlbumKidsMobileResponse findAlbumforClassmob(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idKids = principal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow();
        Long idClass = kids.getMaClass().getId();
        List<ExAlbumKids> exAlbumKidsList = exAlbumKidsRepository.findAlbumMoblieforClass(idKids, idClass);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = new ListExAlbumKidsMobileResponse();
        List<ExAlbumKidsResponse> exAlbumKidsResponses = this.setProperties(exAlbumKidsList);
        Long countAll2 = albumRepository.getCountMessageforClass(idSchool, localDateTime);
        boolean checkLastPage = countAll2 <= 20;

        listExAlbumKidsMobileResponse.setListAllAlbum(exAlbumKidsResponses);
        listExAlbumKidsMobileResponse.setLastPage(checkLastPage);
        return listExAlbumKidsMobileResponse;
    }

    @Override
    public ListExAlbumKidsMobileResponse findAlbumAll(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime) {
        Long idKids = principal.getIdKidLogin();
        List<ExAlbumKids> exAlbumKidsList = exAlbumKidsRepository.findAlbumMoblie(idKids, localDateTime, pageable);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = new ListExAlbumKidsMobileResponse();
        List<ExAlbumKidsResponse> exAlbumKidsResponses = this.setProperties(exAlbumKidsList);
        Long countAll2 = exAlbumKidsRepository.countAlbumMobile(idKids, localDateTime);
        boolean checkLastPage = countAll2 <= 20;
        listExAlbumKidsMobileResponse.setListAllAlbum(exAlbumKidsResponses);
        listExAlbumKidsMobileResponse.setLastPage(checkLastPage);
        return listExAlbumKidsMobileResponse;

    }

    @Override
    public ListExAlbumKidsMobileResponse findAlbumforSchool(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        Long idKids = principal.getIdKidLogin();
        List<ExAlbumKids> exAlbumKidsList = exAlbumKidsRepository.findAlbumMoblieforSchool1(idKids, idClass);
        ListExAlbumKidsMobileResponse listExAlbumKidsMobileResponse = new ListExAlbumKidsMobileResponse();
        List<ExAlbumKidsResponse> exAlbumKidsResponses = this.setProperties(exAlbumKidsList);
        Long countAll2 = albumRepository.getCountMessageforClass(idSchool, localDateTime);
        boolean checkLastPage = countAll2 <= 20;
        listExAlbumKidsMobileResponse.setListAllAlbum(exAlbumKidsResponses);
        listExAlbumKidsMobileResponse.setLastPage(checkLastPage);
        return listExAlbumKidsMobileResponse;
    }


    /**
     * set thuoc tinh
     *
     * @param exAlbumKidsList
     * @return
     */
    private List<ExAlbumKidsResponse> setProperties(List<ExAlbumKids> exAlbumKidsList) {
        List<ExAlbumKidsResponse> exAlbumKidsResponses = new ArrayList<>();
        exAlbumKidsList.forEach(x -> {
            int count = (int) x.getAlbum().getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
            if (count > 0) {
                ExAlbumKidsResponse model = new ExAlbumKidsResponse();
                model.setId(x.getAlbum().getId());
                model.setAlbumName(x.getAlbum().getAlbumName());
                model.setAlbumNew(x.isStatusUnread());
                model.setCreatedDate(x.getAlbum().getCreatedDate());
                model.setPictureNumber(count);
                List<ListPicture> listAppove = x.getAlbum().getAlistPictureList().stream().filter(ListPicture::getIsApproved).collect(Collectors.toList());
                model.setUrlPictureFirst(listAppove.get(0).getUrlPicture());
                model.setAlbumType(x.getAlbum().getAlbumType());
                exAlbumKidsResponses.add(model);
            }
        });
        return exAlbumKidsResponses;
    }

}