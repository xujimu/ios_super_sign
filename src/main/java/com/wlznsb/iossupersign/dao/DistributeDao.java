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
            ",#{distribute.ipa},#{distribute.apk},#{distribute.url},#{distribute.createTime},#{distribute.introduce}," +
            "#{distribute.images},#{distribute.downCode},#{distribute.buyDownCodeUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(@Param("distribute") Distribute distribute);


    @Update("update distribute set apk = #{apk} where id = #{id}")
    int uploadApk(String apk,Integer id);

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
     * 查询账号所有记录
     */
    @Select("select * from distribute")
    List<Distribute> querAll( );
    /**
     * 查询下个主键
     * @return
     */
    @Select("select id from distribute order by id DESC limit 1")
    Integer getId();

    /**
     * 修改简介
     * @return
     */
    @Update("update distribute set introduce = #{introduce} where  account = #{account} and id = #{id}")
    int updateIntroduce(String introduce,String account,Integer id);

    /**
     * 修改images这是只是文字
     * @return
     */
    @Update("update distribute set images = #{images} where  account = #{account} and id = #{id}")
    int updateImages(String images,String account,Integer id);

    /**
     * 修改购买地址
     */
    @Update("update distribute set buy_down_code_url = #{buyDownCodeUrl} where  account = #{account} and id = #{id}")
    int updateBuyDownCodeUrl(@Param("account") String account,@Param("id") Integer id,@Param("buyDownCodeUrl") String buyDownCodeUrl);

    /**
     * 启用下载码
     */
    @Update("update distribute set down_code = #{downCode} where  account = #{account} and id = #{id}")
    int updateDownCode(@Param("account") String account,@Param("id") Integer id,@Param("downCode") Integer downCode);




}

