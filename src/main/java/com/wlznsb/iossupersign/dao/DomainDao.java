package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.entity.Domain;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 多域名
 */
@Mapper
@Component
public interface DomainDao {

    /**
     * 新增
     */
    @Insert("insert into domain() values(#{domain.id},#{domain.domain},#{domain.createTime},#{domain.status})")
    int add(@Param("domain") Domain domain);

    /**
     * 删除
     */
    @Delete("delete from domain where id = #{id}")
    int dele(Integer id);

    /**
     * 查询所有
     */
    @Select("select * from domain")
    List<Domain> queryAll();


}
