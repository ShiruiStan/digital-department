package com.atcdi.digital.entity.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectMember {
    @JsonIgnore
    private int memberId;
    @JsonIgnore
    private int projectId;
    private int userId;
    @ApiModelProperty("分配任务说明")
    private String missions;
}
