package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.IosSignSoftwareDistribute;
import com.wlznsb.iossupersign.entity.IosSignUdidCert;
import com.wlznsb.iossupersign.entity.SoftwareDistribute;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * ios自助分发证书
 */
@Mapper
@Component
public interface IosSignUdidCertDao {

    /**
     * 创建分发应用
     * @param
     * @return
     */
    @Insert("insert into ios_sign_udid_cert() values(#{cert.certId},#{cert.account},#{cert.p12Path},#{cert.mobileprovisionPath},#{cert.p12Password},#{cert.udid},#{cert.introduce},#{cert.createTime})")
    int add(@Param("cert") IosSignUdidCert iosSignUdidCert);





    /**
     * 删除证书
     * @param certId
     * @return
     */
    @Delete("delete from ios_sign_udid_cert where cert_id = #{certId} and account = #{account}")
    Integer delete(String certId,String account);


    /**
     * 查询账号所有记录
     */
    @Select("select * from ios_sign_udid_cert where account = #{account}")
    List<IosSignUdidCert> queryAccountAll(String account);


    /**
     *
     */
    @Select("select * from ios_sign_udid_cert where cert_id = #{certId}")
    IosSignUdidCert query(String certId);



    /**
     * 修改介绍
     */
    @Update("update ios_sign_udid_cert set introduce = #{introduce} where cert_id = #{certId} and account = #{account}")
    int updateIntroduce(String introduce, String certId,String account);


}

