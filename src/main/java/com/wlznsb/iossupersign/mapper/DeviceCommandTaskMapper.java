package com.wlznsb.iossupersign.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wlznsb.iossupersign.entity.DeviceCommandTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【device_command_task(执行命令任务)】的数据库操作Mapper
* @createDate 2022-04-17 21:19:38
* @Entity com.wlznsb.iossupersign.entity.DeviceCommandTaskEntity
*/
@DS("mdm")
public interface DeviceCommandTaskMapper extends BaseMapper<DeviceCommandTaskEntity> {

}




