package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.IosSignSoftwareDistributeStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * ios自助分发
 */
@Mapper
@Component
public interface IosSignSoftwareDistributeStatusDao {

    /**
     * 创建分发应用
     * @param
     * @return
     */
    @Insert("insert into ios_sign_software_distribute_status() values(#{ios.uuid},#{ios.account},#{ios.iosId},#{ios.certId},#{ios.appName},#{ios.pageName},#{ios.appVersion},#{ios.downUrl},#{ios.status},#{ios.createTime},#{ios.updateTime})")
    int add(@Param("ios") IosSignSoftwareDistributeStatus ios);

    /**
     * 修改介绍
     */
    @Update("update ios_sign_software_distribute_status set status = #{status} where uuid = #{uuid} ")
    int updateStatus(String status,String uuid);

    @Update("update ios_sign_software_distribute_status set status = #{status} , down_url = #{downUrl} where uuid = #{uuid} ")
    int updateDownUrl(String status,String downUrl,String uuid);

    @Select("select * from ios_sign_software_distribute_status where uuid = #{uuid} ")
    IosSignSoftwareDistributeStatus query(String uuid);

    @Select("select * from ios_sign_software_distribute_status where status = #{status}  ")
    List<IosSignSoftwareDistributeStatus> queryStatusAll(String status);

    @Select("select * from ios_sign_software_distribute_status where account = #{account} ")
    List<IosSignSoftwareDistributeStatus> queryAccountAll(String account);

    @Select("select * from ios_sign_software_distribute_status ")
    List<IosSignSoftwareDistributeStatus> queryAll();

}

