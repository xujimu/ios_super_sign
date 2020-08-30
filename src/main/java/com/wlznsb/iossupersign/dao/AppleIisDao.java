package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.AppleIis;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 苹果iis账号
 *
 */
@Mapper
@Component
public interface AppleIisDao {

    /**
     * 新增账号
     */

    @Insert("insert into apple_iis() values(#{AppleIis.id},#{AppleIis.account},#{AppleIis.iis},#{AppleIis.kid}," +
            "#{AppleIis.certId},#{AppleIis.identifier},#{AppleIis.p8},#{AppleIis.p12},#{AppleIis.start},#{AppleIis.status},#{AppleIis.ispublic},#{AppleIis.count},#{AppleIis.createTime})")
    int add(@Param("AppleIis") AppleIis appleIis);

    /**
     * 删除账号
     */

    @Delete("delete from apple_iis where account = #{account} and iis = #{iis}")
    int dele(String account,String iis);

    /**
     *
     * 修改status
     *
     */
    @Update("update apple_iis set status = #{status} where iis = #{iis}")
    int updateStatus(int status,String iis);

    /**
     * 修改是否启用
     *
     */
    @Update("update apple_iis set start = #{start} where iis = #{iis}")
    int updateStart(int start,String iis);


    /**
     * 修改是否启用
     *
     */
    @Update("update apple_iis set isPublic = #{isPublic} where iis = #{iis}")
    int updateIspublic(int isPublic,String iis);



    /**
     *
     *查询所有证书
     *
     */
    @Select("select * from apple_iis")
    List<AppleIis>  queryAll();

    /**
     *
     * 查询某个证书
     *
     */
    @Select("select * from apple_iis where account = #{account} and iis = #{iis}")
    AppleIis query(String account,String iis);


    /**
     * 查询某个账号所有证书
     */
    @Select("select * from apple_iis where account = #{account}")
    List<AppleIis> queryAccount(String account);

    /**
     * 查询可用证书
     * @param account
     * @return
     */
    @Select("select * from apple_iis where count >= 0 and account = #{account} and start = 1 and status = 1")
    List<AppleIis> queryUsIis(String account);

    /**
     * 修改次数
     * @param count
     * @return
     */
    @Update("update apple_iis set count = #{count} where iis = #{iis}")
    int updateCount(int count,String iis);

}
