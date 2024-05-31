package com.zsr.sales_system_java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsr.sales_system_java.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zsr
 * @since 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesStaff extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "staff_id", type = IdType.AUTO)
    private Integer staffId;

    private String name;

    private String email;

    private String phone;

    private Integer leaderId;

    private Integer level;

    private String password;
}
