package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.MdmPackStatusEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author Administrator
* @description 针对表【mdm_pack_status】的数据库操作Mapper
* @createDate 2022-04-18 19:48:14
* @Entity com.wlznsb.iossupersign.entity.MdmPackStatusEntity
*/
public interface MdmPackStatusMapper extends BaseMapper<MdmPackStatusEntity> {
    /**
     * 查询一条记录
     */
    @Select("select * from mdm_pack_status where id = #{id}")
    MdmPackStatusEntity query(String id);


    /**
     * 查询排队中的记录
     */
    @Select("select * from mdm_pack_status where status = #{status}")
    List<MdmPackStatusEntity> queryPage(String status);


    /**
     * 添加一条记录
     */
    @Insert("insert into mdm_pack_status() values(#{packStatus.id},#{packStatus.account},#{packStatus.pageName},#{packStatus.uuid},#" +
            "{packStatus.udid},#{packStatus.iis},#{packStatus.p12Path},#{packStatus.mobilePath},#{packStatus.createTime},#{packStatus.ipa},#{packStatus.plist}" +
            ",#{packStatus.status},#{packStatus.signOff},#{packStatus.appId},#{packStatus.url},#{packStatus.ip},#{packStatus.downCode},#{packStatus.deviceId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(@Param("packStatus") MdmPackStatusEntity packStatus);

    /**
     * 修改状态
     * @param status
     * @param uuid
     * @return
     */
    @Update("update  mdm_pack_status set status = #{status} where uuid = #{uuid}")
    int updateStatus(String status,String uuid);


    /**
     * 只修改准备中的状态
     * @param status
     * @param uuid
     * @return
     */
    @Update("update  mdm_pack_status set status = #{newStatus} , down_code = #{downCode} where uuid = #{uuid} and status = #{status}")
    int updateStatusExec(String newStatus,String downCode,String uuid,String status);

    /**
     * 只修改准备中的状态,并添加证书签名路径
     * @param status
     * @param uuid
     * @return
     */
    @Update("update  mdm_pack_status set status = #{newStatus},iis = #{iis},down_code = #{downCode},p12_path = #{p12Path},mobile_path = #{mobilePath} where uuid = #{uuid} and status = #{status}")
    int updateStatusExecS(String newStatus,String iis,String downCode,String p12Path,String mobilePath,String uuid,String status);

    /**
     * 修改大部分状态
     * @param packStatus
     * @return
     */
    @Update("update  mdm_pack_status set account = #{packStatus.account},page_name = #{packStatus.pageName}" +
            ",iis = #{packStatus.iis},p12_path = #{packStatus.p12Path},mobile_path = #{packStatus.mobilePath},ipa = #{packStatus.ipa},plist = #{packStatus.plist},status = #{packStatus.status} where uuid = #{uuid}")
    int update(@Param("packStatus") MdmPackStatusEntity packStatus,@Param("uuid") String uuid);




    /**
     * 查询用户自己的下载记录
     */
    @Select("select * from mdm_pack_status where account = #{account} order by id desc")
    List<MdmPackStatusEntity> queryDown(String account);

    /**
     * 查询该udid是否下载过,证书是否失效
     */
    @Select("select * from mdm_pack_status where udid = #{udid} and status = '点击下载' and account = #{account}  and iis in (select iis from apple_iis where status = 1)  ORDER BY id DESC  LIMIT 1")
    MdmPackStatusEntity queryUdidCert(String udid,String account);

    /**
     * 根据账号和id查询证书
     */
    @Select("select * from mdm_pack_status where id = #{id} and account = #{account}")
    MdmPackStatusEntity queryDownCert(String id,String account);
}




