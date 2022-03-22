package com.atcdi.digital.service;

import com.atcdi.digital.dao.ProjectDao;
import com.atcdi.digital.entity.project.Project;
import com.atcdi.digital.entity.project.ProjectMember;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Resource
    ProjectDao projectDao;
    @Resource
    UserService userService;
    @Resource
    SessionHandler sessionHandler;
    @Resource
    ObjectMapper mapper;

    public JsonNode getProjectById(int projectId) {
        Project project = projectDao.getProjectById(projectId);
        ObjectNode res = mapper.valueToTree(project);
        Set<JsonNode> projectMembers = new HashSet<>();
        for (ProjectMember member : project.getProjectMembers()) {
            ObjectNode memberNode = userService.getUserDisplayLabel(member.getUserId());
            if (member.getMissions() != null) memberNode.put("mission", member.getMissions());
            projectMembers.add(memberNode);
        }
        res.set("projectMembers", mapper.valueToTree(projectMembers));
        return res;
    }

    public Set<JsonNode> getAllProjects() {
        return briefProjectList(projectDao.getAllProjects());
    }

    public Set<JsonNode> getUserProjects(boolean all) {
        int userId = sessionHandler.getCurrentUser().getUserId();
        if (all) {
            return briefProjectList(projectDao.getUserProjects(userId));
        } else {
            return briefProjectList(projectDao.getUserOngoingProjects(userId));
        }
    }

    public Set<JsonNode> briefProjectList(Set<Project> projects) {
        return projects.stream().map(Project::projectBriefInfo).collect(Collectors.toSet());
    }

}
