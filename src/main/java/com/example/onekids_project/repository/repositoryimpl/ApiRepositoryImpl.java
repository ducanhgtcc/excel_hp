package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.user.Api;
import com.example.onekids_project.repository.repositorycustom.ApiRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiRepositoryImpl extends BaseRepositoryimpl<Api> implements ApiRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Api> getApiList() {

        String sql = "select * from ma_api WHERE del_active=1 order by id asc";

        Query query = entityManager.createNativeQuery(sql, Api.class);

        return query.getResultList();
    }

    @Override
    public List<Api> getApiFromTo(int from, int to) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id>=:from and id<=:to ");
        mapParams.put("from", from);
        mapParams.put("to", to);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
