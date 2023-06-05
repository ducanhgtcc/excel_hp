package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.phonebook.ContactTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.KidTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.ListContactTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.ParentTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ContactTeacherService;
import com.example.onekids_project.repository.EmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.StudentUtil;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactTeacherServiceImpl implements ContactTeacherService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    KidsRepository kidsRepository;

    @Override
    public KidTeacherResponse findKidPhoneBook(UserPrincipal principal, Long idKid) {
        Long idSchool = principal.getIdSchoolLogin();
        KidTeacherResponse kidTeacherResponse = new KidTeacherResponse();
        Kids kid = kidsRepository.findByIdKid(idSchool, idKid).orElseThrow(() -> new NotFoundException("Not found kid by id in contact"));
        kidTeacherResponse.setFullName(kid.getFullName());
        kidTeacherResponse.setGender(kid.getGender());
        String birth = ConvertData.convertDateString(kid.getBirthDay());
        kidTeacherResponse.setBirthDay(birth);
        kidTeacherResponse.setAvatarKid(ConvertData.getAvatarKid(kid));
        Parent parent = kid.getParent();

        kidTeacherResponse.setAddress(kid.getAddress());
        kidTeacherResponse.setFatherName(kid.getFatherName());
        kidTeacherResponse.setMotherName(kid.getMotherName());
        kidTeacherResponse.setFatherEmail(kid.getFatherEmail());
        kidTeacherResponse.setMotherEmail(kid.getMotherEmail());
//        Cho phép hiển thị số điện thoại của phụ huynh với App giáo viên?
        if (principal.getSchoolConfig().isParentPhone()) {
            kidTeacherResponse.setParentPhone(parent == null ? "" : parent.getMaUser().getPhone());
            kidTeacherResponse.setFatherPhone(kid.getFatherPhone());
            kidTeacherResponse.setMotherPhone(kid.getMotherPhone());
        } else {
            if (parent != null) {
                kidTeacherResponse.setParentPhone(MobileConstant.HIDDEN_PHONE);
            }
            if (StringUtils.isNotBlank(kid.getFatherPhone())) {
                kidTeacherResponse.setFatherPhone(MobileConstant.HIDDEN_PHONE);
            }
            if (StringUtils.isNotBlank(kid.getMotherPhone())) {
                kidTeacherResponse.setMotherPhone(MobileConstant.HIDDEN_PHONE);
            }
        }
        return kidTeacherResponse;
    }

    @Override
    public List<ParentTeacherResponse> findParentPhoneBook(UserPrincipal principal) {
        CommonValidate.checkDataNoClassTeacher(principal);
        List<ParentTeacherResponse> parentTeacherResponseList = new ArrayList<>();
        Long idClassLogin = principal.getIdClassLogin();
        List<Kids> kidsList = kidsRepository.findKidsOfClass(idClassLogin);
        kidsList.forEach(x -> {
            ParentTeacherResponse parentTeacherResponse = new ParentTeacherResponse();
            parentTeacherResponse.setFullNameKid(x.getFullName());
            parentTeacherResponse.setNickName(StringUtils.isNotBlank(x.getNickName()) ? x.getNickName() : "");
            parentTeacherResponse.setAvatar(ConvertData.getAvatarKid(x));
            parentTeacherResponse.setIdKid(x.getId());
            parentTeacherResponse.setRepresentation(x.getRepresentation().equals(AppConstant.MOTHER) ? x.getMotherName() : x.getFatherName());
            parentTeacherResponse.setLoginStatus(StudentUtil.setLoginStatus(x));
            parentTeacherResponseList.add(parentTeacherResponse);
        });
        return parentTeacherResponseList;
    }


    @Override
    public ListContactTeacherResponse findTeacherPhoneBook(UserPrincipal principal, PageNumberRequest pageNumberRequest) {
        CommonValidate.checkDataNoClassTeacher(principal);
        ListContactTeacherResponse listContactTeacherResponse = new ListContactTeacherResponse();
        List<ContactTeacherResponse> contactTeacherResponseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findContactTeacher(idSchool, pageNumberRequest.getPageNumber());
        infoEmployeeSchoolList.forEach(x -> {
            ContactTeacherResponse model = new ContactTeacherResponse();
            List<ExEmployeeClass> exEmployeeClassList = x.getExEmployeeClassList();
            model.setClassName(CollectionUtils.isEmpty(exEmployeeClassList) ? "" : exEmployeeClassList.get(0).getMaClass().getClassName());
            model.setAvatar(StringUtils.isNotBlank(x.getAvatar()) ? x.getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER);
            model.setTeacherName(x.getFullName());
            model.setPhone(x.getPhone());
            contactTeacherResponseList.add(model);
        });
        boolean lastPage = contactTeacherResponseList.size() < MobileConstant.MAX_PAGE_ITEM;
        listContactTeacherResponse.setLastPage(lastPage);
        listContactTeacherResponse.setDataList(contactTeacherResponseList);

        return listContactTeacherResponse;
    }
}
