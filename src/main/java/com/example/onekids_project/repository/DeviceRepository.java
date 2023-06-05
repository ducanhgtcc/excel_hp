package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.model.kids.KidsInfoModel;
import com.example.onekids_project.repository.repositorycustom.DeviceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long>, DeviceRepositoryCustom {
    /**
     * find device
     *
     * @param idUser
     * @param idDevice
     * @return
     */
    Optional<Device> findByMaUserIdAndIdDevice(Long idUser, String idDevice);


    List<Device> findByIdDeviceAndDelActiveTrue(String idDevice);

    List<Device> findByIdDeviceAndMaUserAppTypeAndDelActiveTrue(String idDevice, String appType);

    Optional<Device> findByTokenFirebaseAndDelActiveTrue(String tokenFirebase);

    List<Device> findByMaUserIdAndTypeInAndLoginTrueAndDelActiveTrueOrderByCreatedDateDesc(Long idUser, List<String> typeList);
    List<Device> findByMaUserIdAndTypeInAndLoginTrueAndDelActiveTrue(Long idUser, List<String> typeList);

    @Query(value = "select distinct k.id, d.token_firebase as token from ma_kids k join ma_parent p on k.id_parent=p.id join ma_user u on p.id_ma_user=u.id right join ma_device d on u.id=d.id_user where k.id in :idList and d.login is true and d.type!='web'", nativeQuery = true)
    <T> Collection<T> getTokeFirebase(@Param("idList") List<Long> idList, Class<T> type);


}
