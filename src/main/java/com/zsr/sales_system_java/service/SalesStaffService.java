package com.zsr.sales_system_java.service;

import com.zsr.sales_system_java.entity.SalesStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
public interface SalesStaffService extends IService<SalesStaff> {

    List<SalesStaff> findStaffByLeaderId(Integer id, Integer level);
}
