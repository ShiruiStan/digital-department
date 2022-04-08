package com.atcdi.digital.entity.daliy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Dairy {
    @ApiModelProperty("日志id，用于用户修改日志")
    int dairyId;
    @JsonIgnore
    int userId;
    @ApiModelProperty("所属项目")
    String workItem;
    @ApiModelProperty("日志日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate date;
    @ApiModelProperty("工作描述")
    String workDesc;
    @ApiModelProperty("花费时间(小时)")
    double spendTime;

}
