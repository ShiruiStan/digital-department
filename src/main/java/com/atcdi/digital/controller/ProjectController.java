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
    @Resource
    ObjectMapper mapper;

    @GetMapping("/all")
    public StandardResponse getProjectsList(){
        return StandardResponse.success(projectService.getAllProjects());
    }

    @GetMapping("/byuser/all")
    public StandardResponse getUserAllProjects(){
        return StandardResponse.success(projectService.getUserProjects(true));
    }

    @GetMapping("/byuser/ongoing")
    public StandardResponse getUserOngoingProjects(){
        return StandardResponse.success(projectService.getUserProjects(false));
    }

    @GetMapping("/info")
    public StandardResponse getUserOngoingProjects(@NonNull int id){
        return StandardResponse.success(projectService.getProjectById(id));
    }
}
