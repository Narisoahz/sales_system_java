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
public class SalesChannel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "channel_id", type = IdType.AUTO)
    private Integer channelId;

    private Integer organizationId;

    private String name;

    private String province;

    private String city;

    private String address;

    private String contactInfo;


}
