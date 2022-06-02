package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.SuperSignAppApiEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xjm
* @description 针对表【super_sign_app_api】的数据库操作Mapper
* @createDate 2022-05-31 21:36:52
* @Entity com.wlznsb.iossupersign.entity.SuperSignAppApiEntity
*/
public interface SuperSignAppApiMapper extends BaseMapper<SuperSignAppApiEntity> {
    List<SuperSignAppApiEntity> selectByAccount(@Param("account") String account);
}




