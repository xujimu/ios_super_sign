package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.SoftwareDistributeDownRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xjm
* @description 针对表【software_distribute_down_record(mdm企业分发 下载详细记录)】的数据库操作Mapper
* @createDate 2022-05-03 18:48:47
* @Entity com.wlznsb.iossupersign.entity.SoftwareDistributeDownRecordEntity
*/
public interface SoftwareDistributeDownRecordMapper extends BaseMapper<SoftwareDistributeDownRecordEntity> {

    List<SoftwareDistributeDownRecordEntity> selectByAccount(@Param("account") String account);

    Integer selectByAccountCount(@Param("account") String account);

    Integer selectByUuidCount(@Param("appId") Integer appId,@Param("day") String day);


}




