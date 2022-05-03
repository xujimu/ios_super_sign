package com.wlznsb.iossupersign.mapper;

import com.wlznsb.iossupersign.entity.PackStatusEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author xjm
* @description 针对表【pack_status】的数据库操作Mapper
* @createDate 2022-05-03 16:48:17
* @Entity com.wlznsb.iossupersign.entity.PackStatusEntity
*/
public interface PackStatusMapper extends BaseMapper<PackStatusEntity> {

    /**
     * lastDay 昨天 day 昨天 null 总量
     * @param uuid
     * @return
     */
    Integer selectByUuidCount(@Param("uuid") String uuid, @Param("day") String day);



}




