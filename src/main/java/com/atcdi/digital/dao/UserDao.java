package com.atcdi.digital.dao;

import com.atcdi.digital.entity.auth.Role;
import com.atcdi.digital.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results(id = "userMap" , value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roles", column = "user_id", many = @Many(select = "findRolesByUserId")),
    })
    User getUserByName(String username);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    @ResultMap("userMap")
    User getUserById(int userId);


    @Select("SELECT r.* FROM user_role ur LEFT JOIN roles r on ur.role_id = r.role_id WHERE ur.user_id = #{userId}")
    Set<Role> findRolesByUserId(int userId);



    @Update("<script> UPDATE users" +
            "<trim prefix='SET' suffixOverrides=','>" +
            "<if test=\"username != null\"> username = #{username}, </if>" +
            "<if test=\"nickname != null\"> nickname = #{nickname}, </if>" +
            "<if test=\"phone != null\"> phone = #{phone}, </if>" +
            "<if test=\"group != null\"> group = #{group}, </if>" +
            "</trim> WHERE user_id = #{userId}" +
            "</script>")
    boolean updateUser(User user);

    @Update("UPDATE users SET password = #{password} WHERE user_id = #{userId}")
    boolean setUserPassword(int userId, String password);

    @Deprecated
    @Update("UPDATE users SET enabled = #{enabled} WHERE user_id = #{userId}")
    boolean changeUserAccountEnable(int userId, int enabled);

    @Select("SELECT nickname FROM users WHERE user_id = #{userId}")
    String getUserNickNameById(int userId);


}
