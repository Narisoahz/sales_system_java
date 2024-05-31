package com.zsr.sales_system_java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsr.sales_system_java.entity.SalesChannel;
import com.zsr.sales_system_java.entity.SalesStaff;
import com.zsr.sales_system_java.mapper.SalesChannelMapper;
import com.zsr.sales_system_java.service.SalesChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
@Service
public class SalesChannelServiceImpl extends ServiceImpl<SalesChannelMapper, SalesChannel> implements SalesChannelService {

    @Override
    public List<SalesChannel> findChannelListByIdList(List<Integer> idList) {
        QueryWrapper<SalesChannel> wrapper = new QueryWrapper<>();
        wrapper.in("channel_id", idList);
        wrapper.orderByAsc("organization_id");
        List<SalesChannel> salesChannels = baseMapper.selectList(wrapper);
        return salesChannels;
    }

    @Override
    public List<SalesChannel> findChannelListBystaffIdListAndOrgIdList(List<Integer> channelIdList, List<Integer> orgIdList) {
        QueryWrapper<SalesChannel> wrapper = new QueryWrapper<>();
        wrapper.in("channel_id", channelIdList).in("organization_id", orgIdList);
        wrapper.orderByAsc("organization_id");
        List<SalesChannel> salesChannels = baseMapper.selectList(wrapper);
        return salesChannels;
    }

    @Override
    public List<SalesChannel> findChannelListByChannelName(String channelName) {
        QueryWrapper<SalesChannel> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(channelName), "name", channelName);
        List<SalesChannel> salesChannelList = baseMapper.selectList(wrapper);
        return salesChannelList;
    }

    @Override
    public List<SalesChannel> findChannelListBySearchObj(List<Integer> channelIdList, String channelName, Integer orgId) {
        QueryWrapper<SalesChannel> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(channelName),"name", channelName);
        if(orgId != null && orgId !=0){
            wrapper.eq("organization_id", orgId);
        }
        wrapper.in("channel_id", channelIdList);
        List<SalesChannel> salesChannelList = baseMapper.selectList(wrapper);
        return salesChannelList;
    }

    @Override
    public Map<String, Object> findChannelListByIdListAndPage(List<Integer> channelIdList, int page, int size) {
        // 假设要进行分页查询，每页显示10条记录，查询第1页，查询条件为 age > 25
        IPage<SalesChannel> page1 = new Page<>(page, size);
        QueryWrapper<SalesChannel> wrapper = new QueryWrapper<>();
        wrapper.in("channel_id", channelIdList);
        wrapper.orderByAsc("organization_id");
        IPage<SalesChannel> channelPage = baseMapper.selectPage(page1, wrapper);
        long total = channelPage.getTotal();
        List<SalesChannel> salesChannels = channelPage.getRecords();

        Map<String, Object> result = new HashMap<>();
        result.put("channels", salesChannels);
        result.put("total", total);
        return result;
    }


}
