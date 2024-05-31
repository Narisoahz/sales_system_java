package com.zsr.sales_system_java.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.zsr.sales_system_java.common.lang.Result;
import com.zsr.sales_system_java.entity.SalesStaff;
import com.zsr.sales_system_java.service.SalesStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.RemoteServer;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/staff")
public class SalesStaffController extends BaseController {
    @Autowired
    SalesStaffService salesStaffService;

    @PostMapping("/login")
        public Result login(@RequestBody SalesStaff staff){
        Integer id = staff.getStaffId();
        String password = staff.getPassword();
        SalesStaff salesStaff = salesStaffService.getById(id);
        if(salesStaff==null){
            //用户不存在
            return Result.fail("用户不存在");
        }else{
            if(salesStaff.getPassword().equals(password)) {
                return Result.succ(salesStaff);
            }else{
                return Result.fail("密码不正确");
            }
        }
    }
    @GetMapping("/logout")
    public Result logout(){
        return Result.succ(null);
    }
    @GetMapping("/staffList")
    public Result getStaffListByLeaderId(@RequestParam("id") Integer id, @RequestParam("level") Integer level){
        List<SalesStaff> staffs = salesStaffService.findStaffByLeaderId(id, level);
        return Result.succ(staffs);
    }
}
