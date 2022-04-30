package com.wlznsb.iossupersign.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wlznsb.iossupersign.entity.MdmSoftwareDistributeDownRecordInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【mdm_software_distribute_down_record_info(mdm企业分发 下载详细记录)】的数据库操作Mapper
* @createDate 2022-04-29 06:04:20
* @Entity com.wlznsb.iossupersign.entity.MdmSoftwareDistributeDownRecordInfoEntity
*/
public interface MdmSoftwareDistributeDownRecordInfoMapper extends BaseMapper<MdmSoftwareDistributeDownRecordInfoEntity> {
    List<MdmSoftwareDistributeDownRecordInfoEntity> selectByAccount(@Param("account") String account);

    /**
     * lastDay 昨天 day 昨天 null 总量
     * @param uuid
     * @return
     */
    Integer selectByUuidCount(@Param("uuid") String uuid,@Param("day") String day);

    Integer selectByAccountCount(@Param("account") String account);


    Integer selectByUuidCount(String account);

}




