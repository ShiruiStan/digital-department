package com.atcdi.digital.entity.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Project {
    private int projectId;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("项目状态：发起, 确认, 进行中, 完成")
    private ProjectStatus projectStatus;
    @ApiModelProperty("委托单位")
    private String client;
    @ApiModelProperty("委托联系人+手机")
    private String clientLinkman;
    @ApiModelProperty("项目负责人用户id")
    private int managerId; // 项目负责人
    @ApiModelProperty("项目负责人")
    private String managerName;
    @ApiModelProperty("产值类型：产值, 合同额")
    private ValueType valueType;
    @ApiModelProperty("产值")
    private int value;
    @ApiModelProperty("项目说明")
    private String description;
    @ApiModelProperty("有无实际项目：任务明确, 提前启动")
    private ProjectProperty projectProperty;
    @ApiModelProperty("项目类别：生产项目, 科研项目")
    private Set<ProjectClass> projectClass;
    @ApiModelProperty("项目来源：内部支撑, 外部项目")
    private ProjectOrigin projectOrigin;
    @ApiModelProperty("设计阶段：设计, 施工, 养护")
    private Set<ProjectStage> projectStage;
    @ApiModelProperty("项目类型：自主投标,辅助分院投标,基础数据生产,BIM应用综合服务,智慧工地,其他")
    private Set<ProjectType> projectTypes;
    @ApiModelProperty("成果形式：效果图,渲染视频,模型文件,咨询报告,设计优化,硬件代采购及维护,硬件租赁及维护,软件平台,现场服务,PPT,其他")
    private Set<AchievementForm> achievements;
    @ApiModelProperty("计划开始时间")
    private LocalDate planStartDate;
    @ApiModelProperty("计划结束时间")
    private LocalDate planEndDate;
    @ApiModelProperty("重点技术要求")
    private String keyTechnology;
    @ApiModelProperty("项目成员")
    @JsonIgnore
    private Set<ProjectMember> projectMembers;
    @ApiModelProperty("外协单位")
    private Set<ProjectAssist> projectAssists;

    public enum ProjectStatus{
        LAUNCH,
        CONFIRM,
        ONGOING,
        END,
    }

    public enum ValueType{
        PRODUCE_VALUE,  // 产值
        CONTRACT_VALUE, // 合同额

    }

    public enum ProjectProperty{
        TASK_CLEARLY, // 任务明确 有实际项目
        PRE_START, // 提前启动 无实际项目
    }

    public enum ProjectClass {
        PRODUCE_PROJECT,  // 生产项目
        RESEARCH_PROJECT, // 科研项目
    }

    public enum ProjectOrigin{
        INTERNAL, //内部支撑
        EXTERNAL, //外部项目
    }

    public enum ProjectStage{
        DESIGN,   // 设计
        CONSTRUCT, // 施工
        MAINTAIN, // 养护
    }
    public enum ProjectType{
        SELF_TENDER,  // 自主投标
        ASSIST_TENDER, // 辅助分院投标
        BASIC_PRODUCE, // 基础数据生产
        BIM_SERVICE, // BIM应用综合服务
        SMART_CONSTRUCT, // 智慧工地
        OTHER, // 其他
    }

    public enum AchievementForm{
        DESIGN_PICTURE, // 效果图
        DESIGN_VIDEO, // 渲染视频
        MODEL_FILES, // 模型文件
        CONSULT_REPORT, // 咨询报告
        OPTIMIZE_DESIGN, // 设计优化
        DEVICE_PURCHASE_MAINTAIN,  // 硬件代采购及维护
        DEVICE_RENT_MAINTAIN, // 硬件租赁及维护
        SOFTWARE_PLATFORM, // 软件平台
        PRESENT_SERVICE, // 现场服务
        POWER_POINT, // PPT
        OTHER, // 其他
    }

    public void setProjectClass(Set<?> res){
        if (projectClass == null){
            projectClass = new HashSet<>();
        }
        for(Object item : res){
            if (item instanceof String){
                projectClass.add(ProjectClass.valueOf((String) item));
            }else if (item instanceof ProjectClass){
                projectClass.add((ProjectClass) item);
            }
        }
    }

    public void setProjectStage(Set<?> res){
        if (projectStage == null){
            projectStage = new HashSet<>();
        }
        for(Object item : res){
            if (item instanceof String){
                projectStage.add(ProjectStage.valueOf((String) item));
            }else if (item instanceof ProjectStage){
                projectStage.add((ProjectStage) item);
            }
        }
    }

    public void setAchievements(Set<?> res){
        if (achievements == null){
            achievements = new HashSet<>();
        }
        for(Object achievement : res){
            if (achievement instanceof String){
                achievements.add(AchievementForm.valueOf((String) achievement));
            }else if (achievement instanceof AchievementForm){
                achievements.add((AchievementForm) achievement);
            }
        }
    }

    public void setProjectTypes(Set<?> res){
        if (projectTypes == null){
            projectTypes = new HashSet<>();
        }
        for(Object projectType : res){
            if (projectType instanceof String){
                projectTypes.add(ProjectType.valueOf((String) projectType));
            }else if (projectType instanceof ProjectType){
                projectTypes.add((ProjectType) projectType);
            }
        }
    }

    public JsonNode projectBriefInfo(){
        ObjectNode briefInfo = new ObjectMapper().createObjectNode();
        briefInfo.put("projectId" , projectId);
        briefInfo.put("projectName" , projectName);
        if (managerName != null) briefInfo.put("managerName" , managerName);
        if (projectClass != null) briefInfo.put("projectClass" , projectClass.toString());
        if (projectOrigin != null) briefInfo.put("projectOrigin" , projectOrigin.toString());
        if (projectStage != null) briefInfo.put("projectStage" , projectStage.toString());
        if (description != null) briefInfo.put("description" , description);
        if (planStartDate != null) briefInfo.put("planStartDate" , planStartDate.toString());
        if (planEndDate != null) briefInfo.put("planEndDate" , planEndDate.toString());
        return briefInfo;
    }

}
