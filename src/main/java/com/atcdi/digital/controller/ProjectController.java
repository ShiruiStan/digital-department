package com.atcdi.digital.controller;

import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.project.Project;
import com.atcdi.digital.entity.project.ProjectWeeklyReport;
import com.atcdi.digital.service.ProjectService;
import io.swagger.annotations.Api;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/project")
@Api(tags  = "项目相关接口")
public class ProjectController {
    @Resource
    ProjectService projectService;

    @GetMapping("/all")
    public StandardResponse getProjectsList(){
        return StandardResponse.success(projectService.getAllProjects());
    }


    @GetMapping("/user")
    public StandardResponse getUserProjects(){
        return StandardResponse.success(projectService.getUserProjects());
    }

    @GetMapping("/manage")
    public StandardResponse getUserManageProjects(){
        return StandardResponse.success(projectService.getUserManageProjects());
    }

    @GetMapping("/info")
    public StandardResponse getProjectById(@NonNull int id){
        return StandardResponse.success(projectService.getProjectById(id));
    }

    @GetMapping("/name")
    public StandardResponse getUserAllProjectNames(){
        return StandardResponse.success(projectService.getAllProjectNameList());
    }

    @PostMapping("/update")
    public StandardResponse createAndUpdateProject(@RequestBody Project project){
        return  StandardResponse.success(projectService.createAndUpdateProject(project));
    }

    @DeleteMapping("/member")
    public StandardResponse deleteProjectMember(@NonNull int project, @NonNull int member){
        projectService.deleteProjectMember(project, member);
        return  StandardResponse.success();
    }
    @DeleteMapping("/assist")
    public StandardResponse deleteProjectAssist(@NonNull int project, @NonNull int assist){
        projectService.deleteProjectAssist(project, assist);
        return  StandardResponse.success();
    }
    @DeleteMapping("/report")
    public StandardResponse deleteProjectReport(@NonNull int project, @NonNull int report){
        projectService.deleteProjectReport(project, report);
        return  StandardResponse.success();
    }

    @PostMapping("/approve")
    public StandardResponse approveProjectLaunch(@NonNull int id){
        projectService.approveProjectLaunch(id);
        return  StandardResponse.success();
    }

    @PostMapping("/report")
    public StandardResponse submitWeeklyReport(@RequestBody ProjectWeeklyReport report){
        projectService.insertWeeklyReport(report);
        return StandardResponse.success();
    }
}
