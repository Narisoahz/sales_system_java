package com.zsr.sales_system_java.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsr.sales_system_java.common.lang.Result;
import com.zsr.sales_system_java.entity.*;
import com.zsr.sales_system_java.service.SalesChannelService;
import com.zsr.sales_system_java.service.SalesOrganizationService;
import com.zsr.sales_system_java.service.SalesStaffService;
import com.zsr.sales_system_java.service.StaffChannelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.Channel;
import java.util.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/channel")
public class SalesChannelController extends BaseController {
    @Autowired
    SalesChannelService salesChannelService;
    @Autowired
    SalesStaffService salesStaffService;
    @Autowired
    StaffChannelService staffChannelService;
    @Autowired
    SalesOrganizationService salesOrganizationService;
    //通过员工id查看销售渠道列表
    @GetMapping("/findChannel")
    public Result findChannelListByStaffId(@RequestParam("id") Integer id, @RequestParam("level") Integer level){
        //分为领导和经理两种方式
        List<Integer> idList = new LinkedList<>();
        if(level == 2){
            //经理
            idList.add(id);
        }else{
            List<SalesStaff> staffsList = salesStaffService.findStaffByLeaderId(id, level);
            for(SalesStaff staff: staffsList){
                idList.add(staff.getStaffId());
            }
        }
        //通过idList去查找销售渠道id列表,销售渠道列表id要去重
        List<Integer> channelIdList = staffChannelService.findChannelListByIdList(idList);
        //通过channelIdList去查找销售渠道详细信息
        List<SalesChannel> channelList = salesChannelService.findChannelListByIdList(channelIdList);
        //查找每个销售渠道的销售机构的详细信息
        List<SalesChannelDetail> channelDetailList = new LinkedList<>();
        for(SalesChannel channel:channelList){
            SalesChannelDetail channelDetail = new SalesChannelDetail();
            BeanUtils.copyProperties(channel, channelDetail);
            // 获取其销售机构的信息,并添加到channelDetail
            int organizationId = channel.getOrganizationId();
            SalesOrganization org = salesOrganizationService.getById(organizationId);
            channelDetail.setType(org.getType());
            channelDetail.setOrganizationName(org.getName());
            // 获取其负责人的信息
            int channelId = channel.getChannelId();
            List<Integer> staffIds = staffChannelService.findIdListByChannel(channelId);
            List<SalesStaff> salesStaffs = salesStaffService.listByIds(staffIds);
            channelDetail.setStaffs(salesStaffs);

            channelDetailList.add(channelDetail);
        }
        return Result.succ(channelDetailList);
    }
    //通过员工id和机构id查看销售渠道列表
    @GetMapping("/findChannel1")
    public Result findChannelListByStaffIdAndOrgId(@RequestParam("staffId") Integer staffId, @RequestParam("orgId") Integer orgId){
        List<Integer> staffIdList = new LinkedList<>();
        staffIdList.add(staffId);
        List<Integer> orgIdList = new LinkedList<>();
        orgIdList.add(orgId);
        //通过idList去查找销售渠道id列表,销售渠道列表id要去重
        List<Integer> channelIdList = staffChannelService.findChannelListByIdList(staffIdList);
        //通过channelIdList和orgIdList去查找销售渠道详细信息
        List<SalesChannel> channelList = salesChannelService.findChannelListBystaffIdListAndOrgIdList(channelIdList, orgIdList);
        return Result.succ(channelList);
    }
    //编辑销售渠道信息
    @PostMapping("/editChannel")
    public Result editChannel(@RequestBody SalesChannelDetail salesChannelDetail){
//        System.out.println(salesChannelDetail);
//        修改销售机构信息
        SalesOrganization salesOrganization = new SalesOrganization();
        salesOrganization.setOrganizationId(salesChannelDetail.getOrganizationId());
        salesOrganization.setName(salesChannelDetail.getOrganizationName());
        salesOrganization.setType(salesChannelDetail.getType());

        salesOrganizationService.updateById(salesOrganization);
        //修改销售渠道信息
        SalesChannel salesChannel = new SalesChannel();
        salesChannel.setChannelId(salesChannelDetail.getChannelId());
        salesChannel.setOrganizationId(salesChannelDetail.getOrganizationId());
        salesChannel.setName(salesChannelDetail.getName());
        salesChannel.setProvince(salesChannelDetail.getProvince());
        salesChannel.setCity(salesChannelDetail.getCity());
        salesChannel.setAddress(salesChannelDetail.getAddress());
        salesChannel.setContactInfo(salesChannelDetail.getContactInfo());
//        System.out.println(salesChannel);
        boolean b = salesChannelService.updateById(salesChannel);
        if(b){
            return Result.succ(null);
        }else{
            return Result.fail("更新失败");
        }

    }
    @PostMapping("/findChannelBySearchObj")
    public Result findChannelListBySearchObj(@RequestBody ChannelSearchObj channelSearchObj){
        //分为领导和经理两种方式
        List<Integer> idList = new LinkedList<>();
        int level = channelSearchObj.getLevel();
        int staffId = channelSearchObj.getStaffId();
        String channelName = channelSearchObj.getChannelName();
        Integer orgId = channelSearchObj.getOrgId();
        if(level == 2){
            //经理
            idList.add(staffId);
        }else{
            List<SalesStaff> staffsList = salesStaffService.findStaffByLeaderId(staffId, level);
            for(SalesStaff staff: staffsList){
                idList.add(staff.getStaffId());
            }
        }
        //通过idList去查找销售渠道id列表,销售渠道列表id要去重
        List<Integer> channelIdList = staffChannelService.findChannelListByIdList(idList);
//        System.out.println(channelIdList);
        //通过channelIdList,销售渠道名称，销售机构id去查找销售渠道列表
        List<SalesChannel> salesChannelList = salesChannelService.findChannelListBySearchObj(channelIdList, channelName, orgId);

        //查找每个销售渠道的销售机构的详细信息
        List<SalesChannelDetail> channelDetailList = new LinkedList<>();
        for(SalesChannel channel: salesChannelList){
            SalesChannelDetail channelDetail = new SalesChannelDetail();
            BeanUtils.copyProperties(channel, channelDetail);
            // 获取其销售机构的信息,并添加到channelDetail
            int organizationId = channel.getOrganizationId();
            SalesOrganization org = salesOrganizationService.getById(organizationId);
            channelDetail.setType(org.getType());
            channelDetail.setOrganizationName(org.getName());
            // 获取其负责人的信息
            int channelId = channel.getChannelId();
            List<Integer> staffIds = staffChannelService.findIdListByChannel(channelId);
            List<SalesStaff> salesStaffs = salesStaffService.listByIds(staffIds);
            channelDetail.setStaffs(salesStaffs);

            channelDetailList.add(channelDetail);
        }
        return Result.succ(channelDetailList);
    }
    @PostMapping("/addChannel")
    public Result addChannel(@RequestBody SalesChannel salesChannel, @RequestParam("staffId")Integer staffId){
        //添加销售渠道
        boolean save = salesChannelService.save(salesChannel);
        if (save) {
            //添加staff和销售渠道的关系
            int channelId = salesChannel.getChannelId();
            StaffChannel staffChannel = new StaffChannel();
            staffChannel.setChannelId(channelId);
            staffChannel.setStaffId(staffId);
            boolean save1 = staffChannelService.save(staffChannel);
            if(save1){
                return Result.succ("添加成功");
            }else{
                return Result.fail("添加失败");
            }
        } else {
            return Result.fail("添加失败");
        }
    }
    @GetMapping("/findChannelByPage")
    public Result findChannelByPage(Integer id, Integer level, String pageNow, String pageSize){
        //分为领导和经理两种方式
        List<Integer> idList = new LinkedList<>();
        if(level == 2){
            //经理
            idList.add(id);
        }else{
            List<SalesStaff> staffsList = salesStaffService.findStaffByLeaderId(id, level);
            for(SalesStaff staff: staffsList){
                idList.add(staff.getStaffId());
            }
        }
        //通过idList去查找销售渠道id列表,销售渠道列表id要去重
        List<Integer> channelIdList = staffChannelService.findChannelListByIdList(idList);

        int page = Integer.parseInt(pageNow);
        int size = Integer.parseInt(pageSize);
        //通过channelIdList和分页信息去查找销售渠道详细信息
        IPage<SalesChannel> page1 = new Page<>(page, size);
        QueryWrapper<SalesChannel> wrapper = new QueryWrapper<>();
        wrapper.in("channel_id", channelIdList);
        wrapper.orderByAsc("organization_id");

        IPage<SalesChannel> channelPage = salesChannelService.page(page1, wrapper);
        long total = channelPage.getTotal();
        List<SalesChannel> channelList = channelPage.getRecords();

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);

        //查找每个销售渠道的销售机构的详细信息
        List<SalesChannelDetail> channelDetailList = new LinkedList<>();
        for(SalesChannel channel: channelList){
            SalesChannelDetail channelDetail = new SalesChannelDetail();
            BeanUtils.copyProperties(channel, channelDetail);
            // 获取其销售机构的信息,并添加到channelDetail
            int organizationId = channel.getOrganizationId();
            SalesOrganization org = salesOrganizationService.getById(organizationId);
            channelDetail.setType(org.getType());
            channelDetail.setOrganizationName(org.getName());
            // 获取其负责人的信息
            int channelId = channel.getChannelId();
            List<Integer> staffIds = staffChannelService.findIdListByChannel(channelId);
            List<SalesStaff> salesStaffs = salesStaffService.listByIds(staffIds);
            channelDetail.setStaffs(salesStaffs);

            channelDetailList.add(channelDetail);
        }
        result.put("channels", channelDetailList);

        return Result.succ(result);

    }

}
