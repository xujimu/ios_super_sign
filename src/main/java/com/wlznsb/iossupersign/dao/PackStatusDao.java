package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.PackStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PackStatusDao {

    /**
     * 查询一条记录
     */
    @Select("select * from pack_status where id = #{id}")
    PackStatus query(String id);


    /**
     * 查询排队中的记录
     */
    @Select("select * from pack_status where status = #{status}")
    List<PackStatus> queryPage(String status);


    /**
     * 添加一条记录
     */
    @Insert("insert into pack_status() values(#{packStatus.id},#{packStatus.account},#{packStatus.pageName},#{packStatus.uuid},#" +
            "{packStatus.udid},#{packStatus.iis},#{packStatus.createTime},#{packStatus.ipa},#{packStatus.plist}" +
            ",#{packStatus.status},#{packStatus.signOff},#{packStatus.appId},#{packStatus.url},#{packStatus.ip})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(@Param("packStatus") PackStatus packStatus);

    /**
     * 修改状态
     * @param status
     * @param uuid
     * @return
     */
    @Update("update  pack_status set status = #{status} where uuid = #{uuid}")
    int updateStatus(String status,String uuid);


    /**
     * 修改大部分状态
     * @param packStatus
     * @return
     */
    @Update("update  pack_status set account = #{packStatus.account},page_name = #{packStatus.pageName}" +
            ",iis = #{packStatus.iis},ipa = #{packStatus.ipa},plist = #{packStatus.plist},status = #{packStatus.status} where uuid = #{uuid}")
    int update(@Param("packStatus") PackStatus packStatus,@Param("uuid") String uuid);

}

