package com.zsr.sales_system_java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsr.sales_system_java.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

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
public class VisitRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "visit_id", type = IdType.AUTO)
    private Integer visitId;

    @TableField(value = "staff_id")
    private Integer staffId;
    @TableField(value = "organization_id")
    private Integer organizationId;
    @TableField(value = "channel_id")
    private Integer channelId;

    private String product;

    private String interviewee;
    @TableField(value = "visit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitDate;

    private String notes;


}
