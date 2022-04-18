package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.DownCode;
import com.wlznsb.iossupersign.entity.MdmDownCodeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【mdm_down_code】的数据库操作Mapper
* @createDate 2022-04-18 18:44:12
* @Entity com.wlznsb.iossupersign.entity.MdmDownCodeEntity
*/
public interface MdmDownCodeMapper extends BaseMapper<MdmDownCodeEntity> {
    /**
     * 用户查询所有下载码数量
     */
    @Select("select count(id) from  mdm_down_code  where account = #{account}")
    Integer queryAccountCount(String account);



    /**
     * 用户查询所有下载码
     */
    @Select("select * from mdm_down_code where account = #{account} and down_code = #{downCode}")
    MdmDownCodeEntity queryAccountDownCode(String account,String downCode);

    /**
     * 添加下载码
     * @param
     * @return
     */
    @Insert({
            "<script>",
            "insert into mdm_down_code values ",
            "<foreach collection='downCodeList' item='downCode' index='index' separator=','>",
            "(#{downCode.id},#{downCode.account},#{downCode.downCode},#{downCode.createTime},#{downCode.useTime},#{downCode.status})",
            "</foreach>",
            "</script>"
    })
    int addDownCode(@Param("downCodeList") List<MdmDownCodeEntity> downCodeList);

    /**
     * 设置下载码状态
     */
    @Update("update mdm_down_code set status = #{status},use_time = #{useTime} where account = #{account} and down_code = #{downCode}")
    int updateDownCodeStatus(@Param("account") String account, @Param("downCode") String downCode, @Param("useTime") Date useTime, @Param("status") Integer status);


    /**
     * 删除下载码
     *
     */
    @Delete("delete from mdm_down_code where account = #{account} and id = #{id}")
    int deleDownCode(String account,String id);


    /**
     * 用户查询所有下载码
     */
    @Select("select * from mdm_down_code where account = #{account}")
    List<MdmDownCodeEntity> queryAccountAllDownCode(String account);


}




