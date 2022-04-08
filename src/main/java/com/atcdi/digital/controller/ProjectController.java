package com.atcdi.digital.controller;

import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.service.ProjectService;
import com.atcdi.digital.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @GetMapping("/byuser/all")
    @Deprecated
    public StandardResponse getUserAllProjects(){
        return StandardResponse.success(projectService.getUserProjects(true));
    }

    @GetMapping("/byuser/ongoing")
    @Deprecated
    public StandardResponse getUserOngoingProjects(){
        return StandardResponse.success(projectService.getUserProjects(false));
    }

    @GetMapping("/user")
    public StandardResponse getUserProjects(){
        return StandardResponse.success(projectService.getUserProjects(true));
    }

    @GetMapping("/info")
    public StandardResponse getProjectById(@NonNull int id){
        return StandardResponse.success(projectService.getProjectById(id));
    }

    @GetMapping("/name")
    public StandardResponse getUserAllProjectNames(){
        return StandardResponse.success(projectService.getAllProjectNameList());
    }
}
