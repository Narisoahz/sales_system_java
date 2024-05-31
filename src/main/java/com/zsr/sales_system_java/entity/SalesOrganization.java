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
public class SalesOrganization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "organization_id", type = IdType.AUTO)
    private Integer organizationId;

    private String name;

    private String type;


}
