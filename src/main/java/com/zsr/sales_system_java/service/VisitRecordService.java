package com.zsr.sales_system_java.service;

import com.zsr.sales_system_java.entity.VisitRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
public interface VisitRecordService extends IService<VisitRecord> {

    List<VisitRecord> findRecordListByChannelId(Integer channelId);

    List<VisitRecord> findRecordListByStaffId(Integer staffId);

    List<VisitRecord> findRecordListBySearchObj(Integer staffId, LocalDate date, Integer orgId, List<Integer> salesChannelIdList);

    Map<String, Object> findStatistic(List<Integer> idList);
}
