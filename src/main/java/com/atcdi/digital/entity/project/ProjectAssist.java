package com.atcdi.digital.entity.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectAssist {
    @JsonIgnore
    private int assistId;
    @JsonIgnore
    private int projectId;
    @ApiModelProperty("外协单位名称")
    private String companyName;
    @ApiModelProperty("外协单位联系人")
    private String linkmanName;
    @ApiModelProperty("外协单位联系人手机")
    private String linkmanPhone;

}
