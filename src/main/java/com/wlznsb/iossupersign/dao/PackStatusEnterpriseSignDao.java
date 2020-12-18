package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.EnterpriseSignCert;
import com.wlznsb.iossupersign.entity.PackStatusEnterpriseSign;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PackStatusEnterpriseSignDao {


    /**
     * 新增打包状态
     * @param
     * @return
     */
    @Insert("insert into pack_status_enterprise_sign() values(#{packStatusEnterpriseSign.id},#{packStatusEnterpriseSign.certId}," +
            "#{packStatusEnterpriseSign.certName},#{packStatusEnterpriseSign.account},#{packStatusEnterpriseSign.createTime}," +
            "#{packStatusEnterpriseSign.appName},#{packStatusEnterpriseSign.pageName},#{packStatusEnterpriseSign.version}," +
            "#{packStatusEnterpriseSign.status},#{packStatusEnterpriseSign.downUrl},#{packStatusEnterpriseSign.ipaPath},#{packStatusEnterpriseSign.url})")
    Integer add(@Param("packStatusEnterpriseSign") PackStatusEnterpriseSign packStatusEnterpriseSign);



    /**
     * 修改打包状态
     */
    @Update("update pack_status_enterprise_sign set status = #{status} , down_url = #{downUrl}  where id = #{id}")
    int updateStatus(String status,String downUrl,Integer id);


    /**
     * 查询用户所有打包
     */
    @Select("select * from pack_status_enterprise_sign where account = #{account}")
    List<PackStatusEnterpriseSign> queryAccount(String account);

    /**
     * 查询所有打包
     */
    @Select("select * from pack_status_enterprise_sign")
    List<PackStatusEnterpriseSign> queryAll();



}
