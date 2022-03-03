package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.PackStatusIosApk;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 免签封装
 */
@Mapper
@Component
public interface PackStatusIosApkDao {

    /**
     * 查询所有
     * @return
     */
    @Select("select * from pack_status_ios_apk;")
    List<PackStatusIosApk> queryAll();

    /**
     * 提交一条记录pack_status_ios_apk
     */
    @Insert("insert into pack_status_ios_apk() values(#{id},#{account},#{createTime},#{appName},#{url},#{name},#{organization},\n" +
            "        #{describe},#{consentMessage},#{icon},#{startIcon},#{isRemove},#{isVariable},#{pageName},\n" +
            "        #{version},#{isXfive},#{status},#{preview},#{down},#{expirationTime},\n" +
            "        #{rootCert},#{serverCert},#{keyCert},#{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int submit(PackStatusIosApk packStatusIosApk);

    /**
     * 查询一个人的打包信息
     */
    @Select("select * from pack_status_ios_apk where account = #{account}  order by id desc")
    List<PackStatusIosApk> queryUserAll(String account);

    /**
     * 更新状态
     * @param
     * @return
     */
    @Update("update  pack_status_ios_apk set status = #{status},preview = #{preview},down = #{down},expiration_time = #{time} where id = #{id};")
    int updateStatus(@Param("status") String status, @Param("preview") String preview, @Param("down") String down, @Param("time") Date time, @Param("id") Integer id);

    /**
     * 查询指定id
     */
    @Select("select * from pack_status_ios_apk where id = #{id}")
    PackStatusIosApk queryId(Integer id);

    /**
     * 修改指定idURl
     */
    @Select("update  pack_status_ios_apk set url = #{url} where id = #{id}")
    PackStatusIosApk updateIdUrl(String url,Integer id);




}
