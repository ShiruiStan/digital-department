package com.atcdi.digital.dao;

import com.atcdi.digital.entity.project.Project;
import com.atcdi.digital.entity.project.ProjectAssist;
import com.atcdi.digital.entity.project.ProjectMember;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.Set;

@Mapper
public interface ProjectDao {

    @Select("SELECT * FROM projects WHERE project_id = #{projectId}")
    @Results(id = "projectMap" , value = {
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectMembers", column = "project_id", many = @Many(select = "getProjectMembers")),
            @Result(property = "projectAssists", column = "project_id", many = @Many(select = "getProjectAssists")),
    })
    Project getProjectById(int projectId);

    @Select("SELECT * FROM user_project WHERE project_id = #{projectId}")
    Set<ProjectMember> getProjectMembers(int projectId);

    @Select("SELECT * FROM external_assists WHERE project_id = #{projectId}")
    Set<ProjectAssist> getProjectAssists(int projectId);

    @Select("SELECT * FROM projects")
    @ResultMap("projectMap")
    Set<Project> getAllProjects();

    @Deprecated
    @Update(value = "UPDATE projects set project_types = #{projectTypes} WHERE project_id = #{projectId}")
    boolean updateProject(Project project);


    @Select("SELECT p.* FROM user_project up LEFT JOIN projects p on up.project_id = p.project_id WHERE up.user_id=#{userId}")
    @ResultMap("projectMap")
    Set<Project> getUserProjects(int userId);

    @Select("SELECT p.* FROM user_project up LEFT JOIN projects p on up.project_id = p.project_id WHERE up.user_id=#{userId} and p.project_status='ONGOING'")
    @ResultMap("projectMap")
    Set<Project> getUserOngoingProjects(int userId);



}
