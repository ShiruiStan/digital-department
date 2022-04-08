package com.atcdi.digital.service;

import com.atcdi.digital.dao.ProjectDao;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.daliy.WorkItem;
import com.atcdi.digital.entity.project.Project;
import com.atcdi.digital.entity.project.ProjectMember;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
            ObjectNode memberUser = mapper.valueToTree(userService.getUserById(member.getUserId()));
            if (member.getMissions() != null) memberUser.put("mission", member.getMissions());
            projectMembers.add(memberUser);
        }
        res.set("projectMembers", mapper.valueToTree(projectMembers));
        return res;
    }

    public List<JsonNode> getAllProjects() {
        return briefProjectList(projectDao.getAllProjects());
    }

    public List<JsonNode> getUserProjects(boolean all) {
        int userId = sessionHandler.getCurrentUser().getUserId();
        if (all) {
            return briefProjectList(projectDao.getUserProjects(userId));
        } else {
            return briefProjectList(projectDao.getUserOngoingProjects(userId));
        }
    }

    public List<JsonNode> briefProjectList(Set<Project> projects) {
        return projects.stream().map(this::projectBriefInfo).collect(Collectors.toList());
    }

    private JsonNode projectBriefInfo(Project project){
        ObjectNode briefInfo = mapper.createObjectNode();
        briefInfo.put("projectId" , project.getProjectId());
        briefInfo.put("projectName" , project.getProjectName());
        briefInfo.set("projectStatus", mapper.valueToTree(project.getProjectStatus()));
        if (project.getManagerName() != null) briefInfo.put("managerName" , project.getManagerName());
        if (project.getProjectClass() != null) briefInfo.set("projectClass" , mapper.valueToTree(project.getProjectClass()));
        if (project.getProjectOrigin() != null) briefInfo.set("projectOrigin" , mapper.valueToTree(project.getProjectOrigin()));
        if (project.getProjectStage() != null) briefInfo.set("projectStage" , mapper.valueToTree(project.getProjectStage()));
        if (project.getDescription() != null) briefInfo.put("description" , project.getDescription());
        if (project.getPlanStartDate() != null) briefInfo.set("planStartDate" , mapper.valueToTree(project.getPlanStartDate()));
        if (project.getPlanEndDate() != null) briefInfo.set("planEndDate" , mapper.valueToTree(project.getPlanEndDate()));
        return briefInfo;
    }

    public Set<JsonNode> getAllProjectNameList(){
        Set<WorkItem> items = projectDao.getWorkItems();
        projectDao.getProjectNameList().forEach(project -> {
            WorkItem item = new WorkItem();
            item.setItemName(project.getProjectName());
            String c = "";
            if (project.getProjectStatus() == Project.ProjectStatus.ONGOING){
                c += "1-进行中的项目-";
            }else if (project.getProjectStatus() == Project.ProjectStatus.FINISHED){
                c += "1-完成的项目-";
            }
            if (project.getProjectClass() == Project.ProjectClass.PRODUCE_PROJECT){
                c += "生产项目";
            }else if(project.getProjectClass() == Project.ProjectClass.RESEARCH_PROJECT){
                c += "科研项目";
            }else{
                c += "其他项目";
            }
            item.setItemClass(c);
            items.add(item);
        });
        Set<JsonNode> res = new HashSet<>();
        items.stream().collect(Collectors.groupingBy(WorkItem::getItemClass)).forEach((workClass, workItem)->{
            ObjectNode node = mapper.createObjectNode();
            node.put("value", workClass);
            node.put("label", workClass);
            node.put("selectable", false);
            Set<JsonNode> children = new HashSet<>();
            workItem.forEach(item->{
                ObjectNode child = mapper.createObjectNode();
                child.put("value", item.getItemName());
                child.put("label", item.getItemName());
                children.add(child);
            });
            node.set("children", mapper.valueToTree(children));
            res.add(node);
        });
        return res;
    }

    public String getProjectNameById(int projectId){
        return projectDao.getProjectNameById(projectId);
    }


}
