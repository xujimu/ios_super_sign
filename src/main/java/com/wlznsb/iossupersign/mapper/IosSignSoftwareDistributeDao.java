package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.IosSignSoftwareDistribute;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * ios自助分发
 */
@Mapper
@Component
public interface IosSignSoftwareDistributeDao {

    /**
     * 创建分发应用
     * @param
     * @return
     */
    @Insert("insert into ios_sign_software_distribute() values(#{iosSignSoftwareDistribute.iosId},#{iosSignSoftwareDistribute.account},#{iosSignSoftwareDistribute.appName}," +
            "#{iosSignSoftwareDistribute.pageName},#{iosSignSoftwareDistribute.version},#{iosSignSoftwareDistribute.icon}" +
            ",#{iosSignSoftwareDistribute.ipa},#{iosSignSoftwareDistribute.apk},#{iosSignSoftwareDistribute.url},#{iosSignSoftwareDistribute.certId},#{iosSignSoftwareDistribute.createTime},#{iosSignSoftwareDistribute.introduce},#{iosSignSoftwareDistribute.autoPageName})")
    int add(@Param("iosSignSoftwareDistribute") IosSignSoftwareDistribute iosSignSoftwareDistribute);



    /**
     * 删除应用
     * @param uuid
     * @return
     */
    @Delete("delete from ios_sign_software_distribute where ios_id = #{iosId} and account = #{account}")
    Integer delete(String iosId,String account);


    /**
     * 查询账号所有记录
     */
    @Select("select * from ios_sign_software_distribute where account = #{account}")
    List<IosSignSoftwareDistribute> queryAccountAll(String account);

    /**
     * 查询账号所有记录
     */
    @Select("select * from ios_sign_software_distribute where ios_id = #{iosId}")
    IosSignSoftwareDistribute query(String iosId);

    /**
     * 修改介绍
     */
    @Update("update ios_sign_software_distribute set introduce = #{introduce} where ios_id = #{iosId} and account = #{account}")
    int updateIntroduce(String introduce, String iosId,String account);

    /**
     * 更换证书id
     */
    @Update("update ios_sign_software_distribute set cert_id = #{certId} where ios_id = #{iosId} ")
    int updateCert(String certId,String iosId);

    /**
     * 更换证书id
     */
    @Update("update ios_sign_software_distribute set auto_page_name = #{status} where ios_id = #{iosId} and account = #{account} ")
    int updateAutoPageName(Integer status,String iosId,String account);
}

