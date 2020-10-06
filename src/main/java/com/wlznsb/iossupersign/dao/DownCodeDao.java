package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.DownCode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface DownCodeDao {

    /**
     * 添加下载码
     * @param
     * @return
     */
    @Insert({
            "<script>",
            "insert into down_code values ",
            "<foreach collection='downCodeList' item='downCode' index='index' separator=','>",
            "(#{downCode.id},#{downCode.account},#{downCode.downCode},#{downCode.createTime},#{downCode.useTime},#{downCode.status})",
            "</foreach>",
            "</script>"
    })
    int addDownCode(@Param("downCodeList") List<DownCode> downCodeList);

    /**
     * 设置下载码状态
     */
    @Update("update down_code set status = #{status},use_time = #{useTime} where account = #{account} and down_code = #{downCode}")
    int updateDownCodeStatus(@Param("account") String account,@Param("downCode") String downCode,@Param("useTime") Date useTime,@Param("status") Integer status);


    /**
     * 删除下载码
     *
     */
    @Delete("delete from down_code where account = #{account} and id = #{id}")
    int deleDownCode(String account,Integer id);


    /**
     * 用户查询所有下载码
     */
    @Select("select * from down_code where account = #{account}")
    List<DownCode> queryAccountAllDownCode(String account);

    /**
     * 用户查询所有下载码
     */
    @Select("select * from down_code where account = #{account} and down_code = #{downCode}")
    DownCode queryAccountDownCode(String account,String downCode);

    /**
     * 用户查询所有下载码数量
     */
    @Select("select count(id) from  down_code  where account = #{account}")
    Integer queryAccountCount(String account);
}
