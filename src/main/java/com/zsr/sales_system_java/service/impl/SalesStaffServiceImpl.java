package com.zsr.sales_system_java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsr.sales_system_java.entity.SalesStaff;
import com.zsr.sales_system_java.mapper.SalesStaffMapper;
import com.zsr.sales_system_java.service.SalesStaffService;
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
public class SalesStaffServiceImpl extends ServiceImpl<SalesStaffMapper, SalesStaff> implements SalesStaffService {

    @Override
    public List<SalesStaff> findStaffByLeaderId(Integer id, Integer level) {
        QueryWrapper<SalesStaff> wrapper = new QueryWrapper<>();
        if(level== 1) {
            wrapper.eq("leader_id", id);
        }else{
            wrapper.eq("level", 2);
        }
        List<SalesStaff> salesStaffs = baseMapper.selectList(wrapper);

        return salesStaffs;
    }
}
