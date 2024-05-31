package com.zsr.sales_system_java.controller;

import com.zsr.sales_system_java.common.lang.Result;
import com.zsr.sales_system_java.entity.SalesChannel;
import com.zsr.sales_system_java.entity.SalesChannelDetail;
import com.zsr.sales_system_java.entity.SalesOrganization;
import com.zsr.sales_system_java.entity.SalesStaff;
import com.zsr.sales_system_java.service.SalesChannelService;
import com.zsr.sales_system_java.service.SalesOrganizationService;
import com.zsr.sales_system_java.service.StaffChannelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/org")
public class SalesOrganizationController {
    @Autowired
    SalesChannelService salesChannelService;
    @Autowired
    StaffChannelService staffChannelService;
    @Autowired
    SalesOrganizationService salesOrganizationService;
    //查询所有销售机构列表
    @GetMapping("/findAllOrg")
    public Result findAllOrgList(){
        List<SalesOrganization> list = salesOrganizationService.list();
        return Result.succ(list);
    }
    //通过员工id查看销售机构列表
    @GetMapping("/findOrg")
    public Result findOrgListByStaffId(@RequestParam("id")Integer id){
        List<Integer> idList = new LinkedList<>();
        idList.add(id);
        //通过id去查找销售渠道id列表,销售渠道列表id要去重
        List<Integer> channelIdList = staffChannelService.findChannelListByIdList(idList);
        //通过channelIdList去查找销售渠道详细信息列表
        List<SalesChannel> channelList = salesChannelService.findChannelListByIdList(channelIdList);
        //获得销售机构id列表
        List<Integer> orgIDList = new LinkedList<>();
        for(SalesChannel channel: channelList){
            orgIDList.add(channel.getOrganizationId());
        }
        //根据销售机构id列表去查查销售机构的详细信息
        List<SalesOrganization> salesOrganizations = salesOrganizationService.listByIds(orgIDList);
        return Result.succ(salesOrganizations);
    }
}
