package com.zsr.sales_system_java.entity;

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
public class StaffChannel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer staffId;

    private Integer channelId;


}
