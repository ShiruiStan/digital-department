package com.atcdi.digital.dao;

import com.atcdi.digital.entity.daliy.WorkItem;
import com.atcdi.digital.entity.project.Project;
import com.atcdi.digital.entity.project.ProjectAssist;
import com.atcdi.digital.entity.project.ProjectMember;
import com.atcdi.digital.entity.project.ProjectWeeklyReport;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProjectDao {

    @Select("SELECT * FROM projects WHERE project_id = #{projectId}")
    @Results(id = "projectMap", value = {
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectMembers", column = "project_id", many = @Many(select = "getProjectMembers")),
            @Result(property = "projectAssists", column = "project_id", many = @Many(select = "getProjectAssists")),
            @Result(property = "projectReports", column = "project_id", many = @Many(select = "getProjectReports")),
    })
    Project getProjectById(int projectId);


    @Select("SELECT * FROM user_project WHERE project_id = #{projectId}")
    List<ProjectMember> getProjectMembers(int projectId);

    @Delete("DELETE FROM user_project WHERE member_id = #{memberId} AND project_id = #{projectId}")
    boolean deleteProjectMember(int memberId, int projectId);

    @Select("SELECT * FROM external_assists WHERE project_id = #{projectId}")
    List<ProjectAssist> getProjectAssists(int projectId);

    @Delete("DELETE FROM external_assists WHERE assist_id = #{assistId} AND project_id = #{projectId}")
    boolean deleteProjectAssist(int assistId, int projectId);

    @Select("SELECT * FROM project_reports WHERE project_id = #{projectId} ORDER BY start_date desc")
    List<ProjectWeeklyReport> getProjectReports(int projectId);

    @Delete("DELETE FROM project_reports WHERE report_id = #{reportId} AND project_id = #{projectId}")
    boolean deleteProjectReport(int reportId, int projectId);

    @Select("SELECT * FROM projects")
    @ResultMap("projectMap")
    Set<Project> getAllProjects();

    @Select("SELECT project_id, project_name, project_status, project_class FROM projects WHERE project_status != 'LAUNCH'")
    Set<Project> getProjectNameList();

    @Select("SELECT p.* FROM user_project up LEFT JOIN projects p on up.project_id = p.project_id WHERE up.user_id=#{userId}")
    @ResultMap("projectMap")
    Set<Project> getUserProjects(int userId);

    @Select("SELECT p.* FROM user_project up LEFT JOIN projects p on up.project_id = p.project_id WHERE up.user_id=#{userId} and p.project_status='ONGOING'")
    @ResultMap("projectMap")
    @Deprecated
    Set<Project> getUserOngoingProjects(int userId);

    @Select("SELECT project_name FROM projects WHERE project_id=#{projectId}")
    String getProjectNameById(int projectId);

    @Select("SELECT item_name, item_class FROM work_items")
    Set<WorkItem> getWorkItems();

    @Insert("INSERT INTO projects (project_name, project_status, manager_id, manager_name, description) VALUES (#{projectName}, #{projectStatus},#{managerId},#{managerName}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "projectId", keyColumn = "project_id")
    boolean createProject(Project project);


    @Update("<script> UPDATE projects" +
            "<trim prefix='SET' suffixOverrides=','>" +
            "project_id = #{projectId}," +
            "<if test=\"client != null\"> client = #{client}, </if>" +
            "<if test=\"clientLinkman != null\"> client_linkman = #{clientLinkman}, </if>" +
            "<if test=\"valueType != null\"> value_type = #{valueType}, </if>" +
            "<if test=\"value != 0\"> value = #{value}, </if>" +
            "<if test=\"description != null\"> description = #{description}, </if>" +
            "<if test=\"projectProperty != null\"> project_property = #{projectProperty}, </if>" +
            "<if test=\"projectClass != null\"> project_class = #{projectClass}, </if>" +
            "<if test=\"projectOrigin != null\"> project_origin = #{projectOrigin}, </if>" +
            "<if test=\"projectStage != null\"> project_stage = #{projectStage}, </if>" +
            "<if test=\"projectTypes != null\"> project_types = #{projectTypes}, </if>" +
            "<if test=\"achievements != null\"> achievements = #{achievements}, </if>" +
            "<if test=\"planStartDate != null\"> plan_start_date = #{planStartDate}, </if>" +
            "<if test=\"planEndDate != null\"> plan_end_date = #{planEndDate}, </if>" +
            "<if test=\"keyTechnology != null\"> key_technology = #{keyTechnology}, </if>" +
            "</trim> WHERE project_id = #{projectId}" +
            "</script>")
    boolean updateProjectInfo(Project project);

    @Update("UPDATE user_project SET project_id=#{projectId}, user_id=#{userId}, missions=#{missions} WHERE member_id = #{memberId}")
    boolean updateProjectMembers(ProjectMember member);

    @Insert("INSERT INTO user_project (project_id, user_id, missions) VALUES(#{projectId},#{userId},#{missions})")
    boolean insertProjectMember(ProjectMember member);

    @Update("UPDATE external_assists SET project_id=#{projectId}, company_name=#{companyName}, linkman_name=#{linkmanName}, linkman_phone=#{linkmanPhone} WHERE assist_id = #{assistId}")
    boolean updateProjectAssists(ProjectAssist assist);

    @Insert("INSERT INTO external_assists (project_id, company_name, linkman_name, linkman_phone) VALUES(#{projectId},#{companyName},#{linkmanName},#{linkmanPhone})")
    boolean insertProjectAssist(ProjectAssist assist);


    @Update("UPDATE projects SET project_status='ONGOING' WHERE project_id=#{projectId}")
    boolean approveProjectLaunch(int projectId);

    @Update("UPDATE projects SET project_status='FINISHED' WHERE project_id=#{projectId}")
    boolean finishProject(int projectId);

    @Insert("INSERT INTO project_reports (project_id, start_date, end_date, report) VALUES (#{projectId},#{startDate},#{endDate},#{report})")
    boolean insertProjectReport(ProjectWeeklyReport report);

    @Update("UPDATE project_reports SET project_id=#{projectId}, end_date=#{endDate}, start_date=#{startDate}, report=#{report} WHERE report_id = #{reportId}")
    boolean updateProjectReport(ProjectWeeklyReport report);

}
