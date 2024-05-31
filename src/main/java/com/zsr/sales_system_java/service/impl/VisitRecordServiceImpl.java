package com.zsr.sales_system_java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsr.sales_system_java.entity.SalesChannel;
import com.zsr.sales_system_java.entity.VisitRecord;
import com.zsr.sales_system_java.mapper.VisitRecordMapper;
import com.zsr.sales_system_java.service.VisitRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord> implements VisitRecordService {

    @Override
    public List<VisitRecord> findRecordListByChannelId(Integer channelId) {
        QueryWrapper<VisitRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("channel_id", channelId);
        wrapper.orderByAsc("visit_date");
        List<VisitRecord> visitRecordList = baseMapper.selectList(wrapper);
        return visitRecordList;
    }

    @Override
    public List<VisitRecord> findRecordListByStaffId(Integer staffId) {
        QueryWrapper<VisitRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("staff_id", staffId);
        wrapper.orderByAsc("visit_date");
        List<VisitRecord> visitRecordList = baseMapper.selectList(wrapper);
        return visitRecordList;
    }

    @Override
    public List<VisitRecord> findRecordListBySearchObj(Integer staffId, LocalDate date, Integer orgId, List<Integer> salesChannelIdList) {
        QueryWrapper<VisitRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("staff_id", staffId);
        if(date != null){
            wrapper.eq("visit_date", date);
        }
        //==0表示没有选定销售机构
        if(orgId!=null && orgId != 0){
            wrapper.eq("organization_id", orgId);
        }
        wrapper.in("channel_id", salesChannelIdList);
        List<VisitRecord> visitRecordList = baseMapper.selectList(wrapper);
        return visitRecordList;
    }

    @Override
    public Map<String, Object> findStatistic(List<Integer> idList) {
        Map<String, Object> result = new HashMap<>();
        //查询近30天内的统计信息
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);
        //总拜访记录数
        QueryWrapper<VisitRecord> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT visit_id").in("staff_id", idList).between("visit_date", thirtyDaysAgo, today);
        int count1 = baseMapper.selectCount(wrapper);
        result.put("total_record", count1);
        //总拜访的机构数
        wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT organization_id").in("staff_id", idList).between("visit_date", thirtyDaysAgo, today);
        int count2 = baseMapper.selectCount(wrapper);
        result.put("total_visit_org", count2);
        //总拜访的渠道数
        wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT channel_id").in("staff_id", idList).between("visit_date", thirtyDaysAgo, today);
        int count3 = baseMapper.selectCount(wrapper);
        result.put("total_visit_channel", count3);
        //拜访过的总经理数
        wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT staff_id").in("staff_id", idList).between("visit_date", thirtyDaysAgo, today);
        int count4 = baseMapper.selectCount(wrapper);
        result.put("total_visit_staff", count4);
        //找到30天内没拜访销售渠道的经理id
        List<VisitRecord> visitStaffList = baseMapper.selectList(wrapper);
        Set<Integer> idSet = new HashSet<>(idList);
        for(VisitRecord visitRecord: visitStaffList){
            int visit_staffId = visitRecord.getStaffId();
            idSet.remove(visit_staffId);
        }
        result.put("not_visit_staff", idSet);

        return result;
    }

}
