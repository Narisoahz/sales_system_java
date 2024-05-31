package com.zsr.sales_system_java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsr.sales_system_java.entity.SalesStaff;
import com.zsr.sales_system_java.entity.StaffChannel;
import com.zsr.sales_system_java.mapper.StaffChannelMapper;
import com.zsr.sales_system_java.service.StaffChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
@Service
public class StaffChannelServiceImpl extends ServiceImpl<StaffChannelMapper, StaffChannel> implements StaffChannelService {
    //通过staffIdList获取channelIdList
    @Override
    public List<Integer> findChannelListByIdList(List<Integer> idList) {
        QueryWrapper<StaffChannel> wrapper = new QueryWrapper<>();
        wrapper.in("staff_id", idList);
        List<StaffChannel> staffChannels = baseMapper.selectList(wrapper);
        List<Integer> channelIdList = new LinkedList<>();
        for(StaffChannel staffChannel: staffChannels){
            channelIdList.add(staffChannel.getChannelId());
        }
        return channelIdList;
    }
    //获取该销售渠道下的负责人的id集合
    @Override
    public List<Integer> findIdListByChannel(int channelId) {
        QueryWrapper<StaffChannel> wrapper = new QueryWrapper<>();
        wrapper.eq("channel_id", channelId);
        List<StaffChannel> staffChannels = baseMapper.selectList(wrapper);
        List<Integer> idList = new LinkedList<>();
        for(StaffChannel staffChannel: staffChannels){
            idList.add(staffChannel.getStaffId());
        }
        return idList;
    }
}
