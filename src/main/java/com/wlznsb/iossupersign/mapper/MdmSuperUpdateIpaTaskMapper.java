package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.MdmSuperUpdateIpaTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【mdm_super_update_ipa_task】的数据库操作Mapper
* @createDate 2022-04-22 19:20:39
* @Entity com.wlznsb.iossupersign.entity.MdmSuperUpdateIpaTaskEntity
*/
public interface MdmSuperUpdateIpaTaskMapper extends BaseMapper<MdmSuperUpdateIpaTaskEntity> {
    List<MdmSuperUpdateIpaTaskEntity> selectByStatus(@Param("status") String status);
}




