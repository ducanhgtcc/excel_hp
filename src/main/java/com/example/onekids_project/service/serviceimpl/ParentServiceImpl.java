package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.ParentRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.repository.WalletParentRepository;
import com.example.onekids_project.request.kids.UpdateParentInforRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.kids.ParentInforResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ParentService;
import com.example.onekids_project.util.GenerateCode;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private WalletParentRepository walletParentRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public Optional<ParentInforResponse> findByIdParent(Long id) {
        Optional<Parent> parentOptional = parentRepository.findByIdParent(id, AppConstant.APP_TRUE);
        if (parentOptional.isEmpty()) {
            return Optional.empty();
        }
        Optional<ParentInforResponse> parentInforResponseOptional = Optional.ofNullable(modelMapper.map(parentOptional.get().getKidsList().get(0), ParentInforResponse.class));
        return parentInforResponseOptional;
    }

    @Override
    public ParentInforResponse updateParent(UpdateParentInforRequest updateParentInforRequest) {
        Optional<Parent> parentOptional = parentRepository.findByIdParent(updateParentInforRequest.getId(), AppConstant.APP_TRUE);
        if (parentOptional.isEmpty()) {
            return null;
        }
        Parent oldParent = parentOptional.get();
        modelMapper.map(updateParentInforRequest, oldParent);
        Parent newParent = parentRepository.save(oldParent);
        ParentInforResponse parentInforResponse = modelMapper.map(newParent, ParentInforResponse.class);

        return parentInforResponse;
    }

    @Override
    public boolean saveAvatar(UserPrincipal principal, MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            return false;
        }
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in parents"));
        Parent parent = maUser.getParent();
        if (StringUtils.isNotBlank(parent.getAvatar())) {
            HandleFileUtils.deletePictureInFolder(parent.getAvatarLocal());
        }
        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(multipartFile, SystemConstant.ID_SYSTEM, UploadDownloadConstant.AVATAR);
        parent.setAvatar(handleFileResponse.getUrlWeb());
        parent.setAvatarLocal(handleFileResponse.getUrlLocal());
        parentRepository.save(parent);
        return true;
    }

    @Override
    public void createWalletParent(Parent parent, Long idSchool) {
        List<WalletParent> walletParentList = CollectionUtils.isEmpty(parent.getWalletParentList()) ? parent.getWalletParentList() : parent.getWalletParentList().stream().filter(x -> x.getIdSchool().equals(idSchool)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(walletParentList)) {
            WalletParent walletParent = new WalletParent();
            School school = schoolRepository.findById(idSchool).orElseThrow();
            Optional<WalletParent> walletParentOptional;
            String code;
            do {
                code = school.getSchoolCode().substring(2).concat("-").concat(GenerateCode.getNumberInput(6));
                walletParentOptional = walletParentRepository.findByCode(code);
            } while (walletParentOptional.isPresent());
            walletParent.setCode(code);
            walletParent.setParent(parent);
            walletParent.setIdSchool(idSchool);
            walletParentRepository.save(walletParent);
        }
    }
}
