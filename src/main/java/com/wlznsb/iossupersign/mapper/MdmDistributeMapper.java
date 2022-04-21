package com.wlznsb.iossupersign.mapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.wlznsb.iossupersign.entity.Distribute;
import org.apache.ibatis.annotations.*;

import com.wlznsb.iossupersign.entity.MdmDistributeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【mdm_distribute】的数据库操作Mapper
* @createDate 2022-04-18 18:44:12
* @Entity com.wlznsb.iossupersign.entity.MdmDistributeEntity
*/
public interface MdmDistributeMapper extends BaseMapper<MdmDistributeEntity> {



    /**
     * 创建分发引用
     * @param distribute
     * @return
     */
    @Insert("insert into mdm_distribute() values(#{distribute.id},#{distribute.account},#{distribute.appName}," +
            "#{distribute.pageName},#{distribute.version},#{distribute.icon}" +
            ",#{distribute.ipa},#{distribute.apk},#{distribute.url},#{distribute.createTime},#{distribute.introduce}," +
            "#{distribute.images},#{distribute.downCode},#{distribute.buyDownCodeUrl},#{distribute.language})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(@Param("distribute") MdmDistributeEntity distribute);


    @Update("update mdm_distribute set apk = #{apk} where id = #{id}")
    int uploadApk(String apk,String id);

    @Update("update mdm_distribute set app_name = #{distribute.appName} ,page_name = #{distribute.pageName}, version = #{distribute.version}  where id = #{distribute.id}")
    int updateIpa(@Param("distribute") MdmDistributeEntity distribute);

    /**
     * 删除应用
     * @param id
     * @return
     */
    @Delete("delete from mdm_distribute where account = #{account} and  id = #{id}")
    int dele(String account,String id);


    /**
     * 修改简介
     * @return
     */
    @Update("update mdm_distribute set language = #{language} where  account = #{account} and id = #{id}")
    int updateLanguage(String language,String account,String id);

    /**
     * 查询一个应用
     * @param id
     * @return
     */
    @Select("select * from mdm_distribute where id = #{id}")
    MdmDistributeEntity query(String id);


    /**
     * 查询账号所有记录
     */
    @Select("select * from mdm_distribute where account = #{account}")
    List<MdmDistributeEntity> queryAccountAll(String account);

    /**
     * 查询账号所有记录
     */
    @Select("select * from mdm_distribute")
    List<MdmDistributeEntity> querAll( );
    /**
     * 查询下个主键
     * @return
     */
    @Select("select id from mdm_distribute order by id DESC limit 1")
    Integer getId();

    /**
     * 修改简介
     * @return
     */
    @Update("update mdm_distribute set introduce = #{introduce} where  account = #{account} and id = #{id}")
    int updateIntroduce(String introduce,String account,String id);

    /**
     * 修改域名
     * @return
     */
    @Update("update mdm_distribute set url = #{url} where account = #{account} and id = #{id}")
    int updateDomain(String url,String account,String id);


    /**
     * 修改images这是只是文字
     * @return
     */
    @Update("update mdm_distribute set images = #{images} where  account = #{account} and id = #{id}")
    int updateImages(String images,String account,String id);

    /**
     * 修改购买地址
     */
    @Update("update mdm_distribute set buy_down_code_url = #{buyDownCodeUrl} where  account = #{account} and id = #{id}")
    int updateBuyDownCodeUrl(@Param("account") String account,@Param("id") String id,@Param("buyDownCodeUrl") String buyDownCodeUrl);

    /**
     * 启用下载码
     */
    @Update("update mdm_distribute set down_code = #{downCode} where  account = #{account} and id = #{id}")
    int updateDownCode(@Param("account") String account,@Param("id") String id,@Param("downCode") Integer downCode);


}




