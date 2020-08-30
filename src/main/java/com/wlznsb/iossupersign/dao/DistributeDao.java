package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.Distribute;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 分发引用信息和位置
 */
@Mapper
@Component
public interface DistributeDao {

    /**
     * 创建分发引用
     * @param distribute
     * @return
     */
    @Insert("insert into distribute() values(#{distribute.id},#{distribute.account},#{distribute.appName}," +
            "#{distribute.pageName},#{distribute.version},#{distribute.icon}" +
            ",#{distribute.ipa},#{distribute.apk},#{distribute.url},#{distribute.createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(@Param("distribute") Distribute distribute);


    /**
     * 删除应用
     * @param id
     * @return
     */
    @Delete("delete from distribute where account = #{account} and  id = #{id}")
    int dele(String account,int id);


    /**
     * 查询一个应用
     * @param id
     * @return
     */
    @Select("select * from distribute where id = #{id}")
    Distribute query(int id);


    /**
     * 查询账号所有记录
     */
    @Select("select * from distribute where account = #{account}")
    List<Distribute> queryAccountAll(String account);

    /**
     * 查询下个主键
     * @return
     */
    @Select("select id from distribute order by id DESC limit 1")
    Integer getId();


}
