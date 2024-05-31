package com.zsr.sales_system_java.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.zsr.sales_system_java.common.lang.Result;
import com.zsr.sales_system_java.entity.*;
import com.zsr.sales_system_java.service.SalesChannelService;
import com.zsr.sales_system_java.service.SalesOrganizationService;
import com.zsr.sales_system_java.service.SalesStaffService;
import com.zsr.sales_system_java.service.VisitRecordService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
@RequestMapping("/record")
public class VisitRecordController extends BaseController {
    @Autowired
    VisitRecordService visitRecordService;
    @Autowired
    SalesStaffService salesStaffService;
    @Autowired
    SalesChannelService salesChannelService;
    @Autowired
    SalesOrganizationService salesOrganizationService;


    @PostMapping("/addRecord")
    public Result addRecord(@RequestBody VisitRecord visitRecord) {
        boolean save = visitRecordService.save(visitRecord);
        if (save) {
            return Result.succ(null);
        } else {
            return Result.fail("添加失败");
        }
    }

    @GetMapping("/findRecord")
    public Result findRecordListByChannelId(@RequestParam("id") Integer id) {
        //根据销售渠道id查找拜访记录
        List<VisitRecord> visitRecordList = visitRecordService.findRecordListByChannelId(id);
        List<VisitRecordDetail> recordDetailList = new LinkedList<>();
        for (VisitRecord record : visitRecordList) {
            VisitRecordDetail recordDetail = new VisitRecordDetail();
            BeanUtils.copyProperties(record, recordDetail);
            //给拜访记录添加上负责的经理的名字
            int staffId = record.getStaffId();
            SalesStaff staff = salesStaffService.getById(staffId);
            recordDetail.setStaffName(staff.getName());

            recordDetailList.add(recordDetail);
        }
        return Result.succ(recordDetailList);
    }

    @GetMapping("/findRecord1")
    public Result findRecordListByStaffId(@RequestParam("id") Integer staffId) {
        //根据员工id查看拜访记录
        List<VisitRecord> visitRecordList = visitRecordService.findRecordListByStaffId(staffId);
        List<VisitRecordDetail> visitRecordDetails = getVisitRecordDetail(visitRecordList);
        return Result.succ(visitRecordDetails);
    }

    @PostMapping("/findRecordBySearchObj")
    public Result findRecordListBySearchObj(@RequestBody RecordSearchObj recordSearchObj) {
        Integer staffId = recordSearchObj.getStaffId();
        LocalDate date = recordSearchObj.getDate();
        Integer orgId = recordSearchObj.getOrgId();
        String channelName = recordSearchObj.getChannelName();
        //根据channelName模糊查询channelIdList
        List<SalesChannel> salesChannelList = salesChannelService.findChannelListByChannelName(channelName);
        List<Integer> salesChannelIdList = new LinkedList<>();
        for (SalesChannel channel : salesChannelList) {
            salesChannelIdList.add(channel.getChannelId());
        }
        //根据channelIdList, date, orgId, staffId查找visitRecord
        List<VisitRecord> visitRecordList = visitRecordService.findRecordListBySearchObj(staffId, date, orgId, salesChannelIdList);
        List<VisitRecordDetail> visitRecordDetails = getVisitRecordDetail(visitRecordList);
        return Result.succ(visitRecordDetails);
    }

    public List<VisitRecordDetail> getVisitRecordDetail(List<VisitRecord> visitRecordList) {
        List<VisitRecordDetail> visitRecordDetails = new LinkedList<>();
        for (VisitRecord record : visitRecordList) {
            VisitRecordDetail recordDetail = new VisitRecordDetail();
            BeanUtils.copyProperties(record, recordDetail);
            //根据员工id查找员工姓名, 根据渠道id查找渠道的信息，根据机构id查找机构的信息
            recordDetail.setStaffName(salesStaffService.getById(record.getStaffId()).getName());
            int channelId = record.getChannelId();
            int orgId = record.getOrganizationId();
            SalesChannel channel = salesChannelService.getById(channelId);
            recordDetail.setChannelName(channel.getName());
            recordDetail.setProvince(channel.getProvince());
            recordDetail.setCity(channel.getCity());
            recordDetail.setOrgName(salesOrganizationService.getById(orgId).getName());

            visitRecordDetails.add(recordDetail);
        }
        return visitRecordDetails;
    }

    @DeleteMapping("/deleteRecord")
    public Result deleteRecord(@RequestParam("id") Integer staffId, @RequestParam("level") Integer level, @RequestParam("visit_id") Integer visitId){
        //删除记录
        //判断有无权限
        VisitRecord visitRecord = visitRecordService.getById(visitId);
        boolean haveRoot = false;
        if(level == 0){
            //大领导
            haveRoot = true;
        }else if(level == 2){
            //经理
            if(visitRecord.getStaffId() == staffId){
                haveRoot = true;
            }
        }else{
            //小领导
            //找到他的下属
            List<SalesStaff> staffByLeaderId = salesStaffService.findStaffByLeaderId(staffId, level);
            for(SalesStaff salesStaff: staffByLeaderId){
                if(visitRecord.getStaffId() == salesStaff.getStaffId()){
                    haveRoot = true;
                    break;
                }
            }
        }
        if(haveRoot){
            //有权限
            visitRecordService.removeById(visitId);
            return Result.succ(200, null, null);
        }else{
//            System.out.println("1");
            return Result.succ(200,"没有权限",null);

        }
    }
    //编辑拜访记录信息
    @PostMapping("editRecord")
    public Result editRecord(@RequestBody VisitRecord visitRecord){
//        System.out.println(visitRecord);
        boolean b = visitRecordService.updateById(visitRecord);
        if(b){
            return Result.succ(null);
        }else{
            return Result.fail("更新失败");
        }
    }
    //查看统计信息
    @GetMapping("/statistic")
    public Result statistic(Integer id, Integer level){
        List<Integer> idList = new LinkedList<>();
        if(level == 1 || level ==0){
            //小领导和大领导
            List<SalesStaff> staffsList = salesStaffService.findStaffByLeaderId(id, level);
            for(SalesStaff staff: staffsList){
                idList.add(staff.getStaffId());
            }
            //根据idList获取统计信息,idList不重复
            Map<String, Object> statistics = visitRecordService.findStatistic(idList);
            statistics.put("total_staff", idList.size());
            Set<Integer> notVisitStaffId = (Set<Integer>) statistics.get("not_visit_staff");
            List<Integer> notVisitIdList = new ArrayList<>(notVisitStaffId);
            List<SalesStaff> salesStaffs = salesStaffService.listByIds(notVisitIdList);
            statistics.put("not_visit_staff", salesStaffs);
            return Result.succ(statistics);
        }else{
            return Result.fail("错误");
        }
    }


}
