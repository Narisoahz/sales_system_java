package com.zsr.sales_system_java.service;

import com.zsr.sales_system_java.entity.SalesChannel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsr.sales_system_java.entity.SalesStaff;

import java.nio.channels.Channel;
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
public interface SalesChannelService extends IService<SalesChannel> {
    List<SalesChannel> findChannelListByIdList(List<Integer> channelIdList);

    //通过channelIdList和orgIdList去查找销售渠道详细信息
    List<SalesChannel> findChannelListBystaffIdListAndOrgIdList(List<Integer> channelIdList, List<Integer> orgIdList);
    //通过channelName模糊查询销售渠道
    List<SalesChannel> findChannelListByChannelName(String channelName);

    List<SalesChannel> findChannelListBySearchObj(List<Integer> channelIdList, String channelName, Integer orgId);


    Map<String, Object> findChannelListByIdListAndPage(List<Integer> channelIdList, int page, int size);
}
