package com.atcdi.digital.service;

import com.atcdi.digital.dao.ProjectDao;
import com.atcdi.digital.entity.StandardException;
import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.daliy.WorkItem;
import com.atcdi.digital.entity.project.Project;
import com.atcdi.digital.entity.project.ProjectMember;
import com.atcdi.digital.entity.project.ProjectWeeklyReport;
import com.atcdi.digital.handler.SessionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
            memberUser.put("memberId", member.getMemberId());
            projectMembers.add(memberUser);
        }
        res.set("projectMembers", mapper.valueToTree(projectMembers));
        return res;
    }

    public List<JsonNode> getAllProjects() {
        return briefProjectList(projectDao.getAllProjects().stream().filter(project -> !Project.ProjectStatus.LAUNCH.equals(project.getProjectStatus())).collect(Collectors.toSet()));
    }

    public List<JsonNode> getUserProjects() {
        int userId = sessionHandler.getCurrentUser().getUserId();
        return briefProjectList(projectDao.getUserProjects(userId));
    }

    public List<JsonNode> getUserManageProjects(){
        Set<Project> projects = projectDao.getAllProjects();
        return briefProjectList(projects.stream().filter(this::canManageProject).collect(Collectors.toSet()));
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

    public int createAndUpdateProject(Project project){
        User user = userService.getCurrentUser();
        project.setManagerId(user.getUserId());
        if (project.getProjectId() == 0){
            project.setProjectStatus(Project.ProjectStatus.LAUNCH);
            project.setManagerName(user.getNickname());
            if (!projectDao.createProject(project)){
                throw new StandardException(500, "创建项目失败");
            }
        }
        Project target = projectDao.getProjectById(project.getProjectId());
        if(canManageProject(target)){
            projectDao.updateProjectInfo(project);
            project.getProjectMembers().forEach(member->{
                member.setProjectId(project.getProjectId());
                if (member.getMemberId() == 0) {
                    projectDao.insertProjectMember(member);
                }else {
                    projectDao.updateProjectMembers(member);
                }
            });
            project.getProjectAssists().forEach(assist -> {
                assist.setProjectId(project.getProjectId());
                if (assist.getAssistId() == 0) {
                    projectDao.insertProjectAssist(assist);
                }else {
                    projectDao.updateProjectAssists(assist);
                }
            });
            project.getProjectReports().forEach(report -> {
                report.setProjectId(project.getProjectId());
                if (report.getReportId() == 0) {
                    projectDao.insertProjectReport(report);
                }else {
                    projectDao.updateProjectReport(report);
                }
            });
        }else{
            throw new StandardException(403, "您没有权限操作该项目："+ project.getProjectName());
        }
        return project.getProjectId();
    }


    public void approveProjectLaunch(int projectId){
        projectDao.approveProjectLaunch(projectId);
    }

    public void insertWeeklyReport(ProjectWeeklyReport report){
        if (!projectDao.insertProjectReport(report)) throw new StandardException(500, "周报填写错误");
    }

    public  void deleteProjectMember(int projectId, int memberId){
        Project project = projectDao.getProjectById(projectId);
        if (canManageProject(project) && !projectDao.deleteProjectMember(memberId, projectId)) throw new StandardException(500, "删除失败");
    }
    public  void deleteProjectAssist(int projectId, int assistId){
        Project project = projectDao.getProjectById(projectId);
        if (canManageProject(project) && !projectDao.deleteProjectAssist(assistId, projectId)) throw new StandardException(500, "删除失败");
    }
    public  void deleteProjectReport(int projectId, int reportId){
        Project project = projectDao.getProjectById(projectId);
        if (canManageProject(project) && !projectDao.deleteProjectReport(reportId, projectId)) throw new StandardException(500, "删除失败");
    }

    public boolean canManageProject(Project project){
        System.out.println(project.getManagerId() == sessionHandler.getCurrentUser().getUserId());
        if (sessionHandler.hasRole("ROLE_ADMIN")) return true;
        else if (sessionHandler.hasRole("ROLE_MASTER") && Project.ProjectStatus.LAUNCH.equals(project.getProjectStatus())) return true;
        else return project.getManagerId() == sessionHandler.getCurrentUser().getUserId() && !Project.ProjectStatus.FINISHED.equals(project.getProjectStatus());
    }

}
