package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.SoftwareDistribute;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 自助分发
 */
@Mapper
@Component
public interface SoftwareDistributeDao {

    /**
     * 创建分发引用
     * @param
     * @return
     */
    @Insert("insert into software_distribute() values(#{softwareDistribute.id},#{softwareDistribute.account},#{softwareDistribute.appName}," +
            "#{softwareDistribute.pageName},#{softwareDistribute.version},#{softwareDistribute.icon}" +
            ",#{softwareDistribute.ipa},#{softwareDistribute.apk},#{softwareDistribute.url},#{softwareDistribute.createTime},#{softwareDistribute.introduce},#{softwareDistribute.uuid})")
    int add(@Param("softwareDistribute") SoftwareDistribute softwareDistribute);


    /**
     * 上传apk
     * @param apk
     * @param uuid
     * @return
     */
    @Update("update software_distribute set apk = #{apk} where uuid = #{uuid} and account = #{account}")
    int uploadApk(String apk, String uuid,String account);


    /**
     * 删除应用
     * @param uuid
     * @return
     */
    @Delete("delete from software_distribute where account = #{account} and  uuid = #{uuid}")
    Integer delete(String account, String uuid);


    /**
     * 管理删除应用
     * @param uuid
     * @return
     */
    @Delete("delete from software_distribute where uuid = #{uuid}")
    Integer adminDelete(String uuid);


    /**
     * 查询一个应用
     * @param id
     * @return
     */
    @Select("select * from software_distribute where id = #{id}")
    SoftwareDistribute query(int id);


    /**
     * 查询一个应用uuid
     * @param uuid
     * @return
     */
    @Select("select * from software_distribute where uuid = #{uuid}")
    SoftwareDistribute queryUuid(String uuid);

    /**
     * 查询账号所有记录
     */
    @Select("select * from software_distribute where account = #{account}")
    List<SoftwareDistribute> queryAccountAll(String account);

    /**
     * 查询所有记录
     */
    @Select("select * from software_distribute")
    List<SoftwareDistribute> querAll();


    /**
     * 修改介绍
     */
    @Update("update software_distribute set introduce = #{introduce} where uuid = #{uuid} and account = #{account}")
    int updateIntroduce(String introduce, String uuid,String account);
}

