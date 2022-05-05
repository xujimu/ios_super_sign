package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlznsb.iossupersign.entity.DeviceStatusEntity;

/**
* @author Administrator
* @description 针对表【device_status】的数据库操作Mapper
* @createDate 2022-04-25 13:15:28
* @Entity com.mdm.entity.DeviceStatusEntity
*/
@DS("mdm")
public interface DeviceStatusMapper extends BaseMapper<DeviceStatusEntity> {

    int insertOrUpdate(DeviceStatusEntity deviceStatusEntity);


    DeviceStatusEntity selectByDeviceIdInAndStatus(@Param("deviceIdList") List<String> deviceIdList, @Param("status") Integer status);


}




