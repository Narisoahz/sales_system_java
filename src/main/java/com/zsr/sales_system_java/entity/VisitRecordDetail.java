package com.zsr.sales_system_java.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class VisitRecordDetail extends VisitRecord{
    private String staffName;
    private String orgName;
    private String channelName;
    private String province;
    private String city;
}
