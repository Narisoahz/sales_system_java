package com.zsr.sales_system_java.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesChannelDetail extends SalesChannel{
    private String organizationName;
    private String type;
//    private List<Integer> staffIds;
    private List<SalesStaff> staffs;
}
