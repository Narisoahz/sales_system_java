package com.zsr.sales_system_java.entity;
import com.zsr.sales_system_java.entity.base.BaseSearchObj;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RecordSearchObj extends BaseSearchObj {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
