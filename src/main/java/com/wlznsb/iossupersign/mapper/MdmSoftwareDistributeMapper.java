package com.wlznsb.iossupersign.mapper;
import com.wlznsb.iossupersign.entity.SoftwareDistribute;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.MdmSoftwareDistributeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【mdm_software_distribute】的数据库操作Mapper
* @createDate 2022-04-20 18:53:20
* @Entity com.wlznsb.iossupersign.entity.MdmSoftwareDistributeEntity
*/
public interface MdmSoftwareDistributeMapper extends BaseMapper<MdmSoftwareDistributeEntity> {

    int updateApkByUuidAndAccount(@Param("apk") String apk, @Param("uuid") String uuid, @Param("account") String account);

    int updateIntroduceByUuidAndAccount(@Param("introduce") String introduce, @Param("uuid") String uuid, @Param("account") String account);


    int updateLanguageByUuidAndAccount(@Param("language") String language, @Param("uuid") String uuid, @Param("account") String account);




    /**
     * 删除应用
     * @param uuid
     * @return
     */
    @Delete("delete from mdm_software_distribute where account = #{account} and  uuid = #{uuid}")
    Integer delete(String account, String uuid);


    /**
     * 管理删除应用
     * @param uuid
     * @return
     */
    @Delete("delete from mdm_software_distribute where uuid = #{uuid}")
    Integer adminDelete(String uuid);
    /**
     * 查询账号所有记录
     */
    @Select("select * from mdm_software_distribute where account = #{account}")
    List<MdmSoftwareDistributeEntity> queryAccountAll(String account);

    /**
     * 查询所有记录
     */
    @Select("select * from mdm_software_distribute")
    List<MdmSoftwareDistributeEntity> querAll();


}




