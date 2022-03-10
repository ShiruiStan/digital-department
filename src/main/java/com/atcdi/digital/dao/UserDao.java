package com.atcdi.digital.dao;

import com.atcdi.digital.entity.Permission;
import com.atcdi.digital.entity.Role;
import com.atcdi.digital.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByName(String username);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    @Results(id = "userMap" , value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "roles", column = "user_id", many = @Many(select = "getRolesByUserId")),
    })
    User getUserById(int userId);


    @Deprecated
    @Update("<script> UPDATE users" +
            "<trim prefix='SET' suffixOverrides=','>" +
            "<if test=\"username != null\"> username = #{username}, </if>" +
            "<if test=\"password != null\"> password = #{password}, </if>" +
            "<if test=\"nickname != null\"> nickname = #{nickname}, </if>" +
            "<if test=\"phone != null\"> phone = #{phone}, </if>" +
            "</trim> WHERE user_id = #{userId}" +
            "</script>")
    boolean updateUser(User user);


    @Select("SELECT * FROM roles WHERE role_id = #{roleId}")
    @Results(id = "roleMap" , value = {
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "label", column = "label"),
            @Result(property = "permissions", column = "role_Id", many = @Many(select = "findPermissionsByRoleId")),
            @Result(property = "menus", column = "role_Id", many = @Many(select = "findMenusByRoleId"))
    })
    Role getRoleById(int roleId);

    @Select("SELECT r.* FROM user_role ur LEFT JOIN roles r on ur.role_id = r.role_id WHERE ur.user_id = #{userId}")
    @ResultMap("roleMap")
    Set<Role> getRolesByUserId(int userId);


    @Select("select p.* from role_permission rp " +
            "LEFT JOIN permissions p on rp.permission_id = p.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.filter = 1")
    Set<Permission> findPermissionsByRoleId(int roleId);

    @Select("select p.* from role_permission rp " +
            "LEFT JOIN permissions p on rp.permission_id = p.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.filter = 0")
    Set<Permission> findMenusByRoleId(int roleId);

}
