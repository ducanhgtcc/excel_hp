package com.example.onekids_project.repository.repositorycustom;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BaseRepositoryCustom<T> {
    /**
     * Hàm tìm kiếm chung findAll để tất cả các class sau chỉ cần extends khi cần tìm kiếm theo fields hoặc xử lí các nghiệp vụ phức tạp
     * có phân trang
     *
     * @param queryStr
     * @param mapParams
     * @param pageable
     * @return
     */
    List<T> findAll(String queryStr, Map<String, Object> mapParams, Pageable pageable);

    /**
     * tìm kiếm có phân trang cho web
     *
     * @param queryStr
     * @param mapParams
     * @param pageNumber
     * @param maxPageItem
     * @return
     */
    List<T> findAllWebPaging(String queryStr, Map<String, Object> mapParams, int pageNumber, int maxPageItem);

    /**
     * tìm kiếm theo đã xóa hay chưa xóa
     * true là chưa xóa
     * false là đã xóa
     *
     * @param queryStr
     * @param mapParams
     * @param pageNumber
     * @param maxPageItem
     * @param delete
     * @return
     */
    List<T> findAllWebPagingDeleteOrNot(String queryStr, Map<String, Object> mapParams, int pageNumber, int maxPageItem, boolean delete);

    /**
     * Hàm tìm kiếm chung findAll để tất cả các class sau chỉ cần extends khi cần tìm kiếm theo fields hoặc xử lí các nghiệp vụ phức tạp
     * có phân trang
     *
     * @param queryStr
     * @param mapParams
     * @param pageable
     * @return
     */
    List<T> findAllMobile(String queryStr, Map<String, Object> mapParams, Pageable pageable);

    /**
     * tìm kiếm cho mobile có phân trang theo pageNumber
     *
     * @param queryStr
     * @param mapParams
     * @param pageNumber
     * @return
     */
    List<T> findAllMobilePaging(String queryStr, Map<String, Object> mapParams, Integer pageNumber);

    /**
     * hàm tìm kiếm chung không cần phân trang
     *
     * @param queryStr
     * @param mapParams
     * @return
     */
    List<T> findAllNoPaging(String queryStr, Map<String, Object> mapParams);
    List<T> findAllNoPagingNoDelActive(String queryStr, Map<String, Object> mapParams);
    /**
     * hàm tìm kiếm chung không cần phân trang có del
     *
     * @param queryStr
     * @param mapParams
     * @return
     */
    List<T> findAllNoPagingDeleteOrNot(String queryStr, Map<String, Object> mapParams, boolean delete);

    /**
     * hàm tìm kiếm chung không cần phân trang có sắp xếp theo bảng con
     *
     * @param queryStr
     * @param mapParams
     * @return
     */
    List<T> findAllNoPagingandOrderBy(String queryStr, Map<String, Object> mapParams);


    /**
     * count all
     *
     * @param queryStr
     * @param mapParams
     * @return
     */
    Long countAll(String queryStr, Map<String, Object> mapParams);

    Long countAllDeleteOrNot(String queryStr, Map<String, Object> mapParams, boolean delete);

}
