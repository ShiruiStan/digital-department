package com.atcdi.digital.entity.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Project {
    int projectId;
    @ApiModelProperty("项目名称")
    String projectName;
    @ApiModelProperty("项目状态：虚拟，发起, 进行中, 完成")
    ProjectStatus projectStatus;
    @ApiModelProperty("委托单位")
    String client;
    @ApiModelProperty("委托联系人+手机")
    String clientLinkman;
    @ApiModelProperty("项目负责人用户id")
    int managerId; // 项目负责人
    @ApiModelProperty("项目负责人")
    String managerName;
    @ApiModelProperty("产值类型：产值, 合同额")
    ValueType valueType;
    @ApiModelProperty("产值")
    int value;
    @ApiModelProperty("项目说明")
    String description;
    @ApiModelProperty("有无实际项目：任务明确, 提前启动")
    ProjectProperty projectProperty;
    @ApiModelProperty("项目类别：生产项目, 科研项目")
    ProjectClass projectClass;
    @ApiModelProperty("项目来源：内部支撑, 外部项目")
    ProjectOrigin projectOrigin;
    @ApiModelProperty("设计阶段：设计, 施工, 养护")
    Set<ProjectStage> projectStage;
    @ApiModelProperty("项目类型：自主投标,辅助分院投标,基础数据生产,BIM应用综合服务,智慧工地,其他")
    Set<ProjectType> projectTypes;
    @ApiModelProperty("成果形式：效果图,渲染视频,模型文件,咨询报告,设计优化,硬件代采购及维护,硬件租赁及维护,软件平台,现场服务,PPT,其他")
    Set<AchievementForm> achievements;
    @ApiModelProperty("计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate planStartDate;
    @ApiModelProperty("计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate planEndDate;
    @ApiModelProperty("重点技术要求")
    String keyTechnology;
    @ApiModelProperty("项目成员")
    Set<ProjectMember> projectMembers;
    @ApiModelProperty("外协单位")
    Set<ProjectAssist> projectAssists;
    @ApiModelProperty("项目周报")
    Set<ProjectWeeklyReport> projectReports;

    public enum ProjectStatus{
        LAUNCH,
        ONGOING,
        FINISHED,
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





}
