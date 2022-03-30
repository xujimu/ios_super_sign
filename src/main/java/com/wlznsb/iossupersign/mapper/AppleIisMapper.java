package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.AppleIisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.wlznsb.iossupersign.entity.AppleIisEntity
 */
public interface AppleIisMapper extends BaseMapper<AppleIisEntity> {
    List<AppleIisEntity> selectByStatus(@Param("status") Integer status);
}




