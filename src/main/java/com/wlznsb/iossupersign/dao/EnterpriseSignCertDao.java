package com.wlznsb.iossupersign.dao;


import com.wlznsb.iossupersign.entity.DownCode;
import com.wlznsb.iossupersign.entity.EnterpriseSignCert;
import com.wlznsb.iossupersign.entity.PackStatusEnterpriseSign;
import jnr.ffi.annotations.In;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface EnterpriseSignCertDao {

    /**
     * 新增证书
     * @param
     * @return
     */
    @Insert("insert into enterprise_sign_cert() values(#{enterpriseSignCert.id},#{enterpriseSignCert.account}," +
            "#{enterpriseSignCert.name},#{enterpriseSignCert.certPath},#{enterpriseSignCert.moblicPath}," +
            "#{enterpriseSignCert.password},#{enterpriseSignCert.status},#{enterpriseSignCert.count}," +
            "#{enterpriseSignCert.remark},#{enterpriseSignCert.createTime},#{enterpriseSignCert.expireTime},#{enterpriseSignCert.md5})")
    Integer addCert(@Param("enterpriseSignCert") EnterpriseSignCert enterpriseSignCert);

    /**
     * 删除证书
     */
    @Delete("delete from enterprise_sign_cert where  md5 = #{md5}")
    Integer deleteCert(String md5);

    /**
     * 修改证书状态
     */
    @Update("update enterprise_sign_cert set status = #{status}  where md5 = #{md5}")
    int updateCertStatus(String status, String md5);


    /**
     * 修改扣除公有池
     */
    @Update("update enterprise_sign_cert set count = #{count}  where md5 = #{md5}")
    int updateCertCount(Integer count, String md5);

    /**
     * 修改备注
     */
    @Update("update enterprise_sign_cert set remark = #{remark}  where md5 = #{md5}")
    int updateCertRemark(String remark, String md5);


    /**
     * 查询所有证书
     */
    @Select("select * from enterprise_sign_cert")
    List<EnterpriseSignCert> queryAllCert();

    /**
     * 查询证书是否存在
     */
    @Select("select * from enterprise_sign_cert where md5 = #{md5}")
    EnterpriseSignCert queryMd5(String md5);


    /**
     * 查询证书
     */
    @Select("select * from enterprise_sign_cert where id = #{id}")
    EnterpriseSignCert queryId(Integer id);

}
