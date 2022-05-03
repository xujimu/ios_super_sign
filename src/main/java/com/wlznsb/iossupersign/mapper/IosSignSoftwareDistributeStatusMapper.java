package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.IosSignSoftwareDistributeStatusEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xjm
* @description 针对表【ios_sign_software_distribute_status】的数据库操作Mapper
* @createDate 2022-05-03 20:30:16
* @Entity com.wlznsb.iossupersign.entity.IosSignSoftwareDistributeStatusEntity
*/
public interface IosSignSoftwareDistributeStatusMapper extends BaseMapper<IosSignSoftwareDistributeStatusEntity> {

    List<IosSignSoftwareDistributeStatusEntity> selectByAccount(@Param("account") String account);
}




