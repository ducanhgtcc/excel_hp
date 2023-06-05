package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.request.finance.order.SearchOrderKidsAllRequest;

import java.util.List;

/**
 * date 2021-02-23 15:20
 *
 * @author lavanviet
 */
public interface FnOrderKidsRepositoryCustom {
    List<FnOrderKids> searchOrderForKids(SearchOrderKidsAllRequest request);

    List<FnOrderKids> searchOrderKidsYearParent(Long idKid, int year);

    List<FnOrderKids> findOrderKidsParent(Long idKid);
}
