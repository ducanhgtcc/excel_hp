package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.KidsGroup;
import com.example.onekids_project.repository.repositorycustom.KidsGroupRepositoryCustom;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KidsGroupRepositoryImpl extends BaseRepositoryimpl<KidsGroup> implements KidsGroupRepositoryCustom {
    @Override
    public List<KidsGroup> findAllKidsGroup(Long idSchool, PageNumberWebRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countKidsGroup(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<KidsGroup> findByIdKidsGroup(Long idSchool, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id=:id ");
        mapParams.put("id", id);
        List<KidsGroup> kidsGroupList = findAllNoPaging(queryStr.toString(), mapParams);
        if (kidsGroupList.size() > 0) {
            return Optional.ofNullable(kidsGroupList.get(0));
        }
        return Optional.empty();
    }
}
