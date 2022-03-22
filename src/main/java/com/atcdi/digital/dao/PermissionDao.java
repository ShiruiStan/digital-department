package com.atcdi.digital.dao;

import com.atcdi.digital.entity.auth.Menu;
import com.atcdi.digital.entity.auth.Permission;
import com.atcdi.digital.entity.auth.Role;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface PermissionDao {

    @Select("SELECT * FROM roles")
    @Results(id = "roleMap" , value = {
            @Result(property = "roleId", column = "role_Id"),
            @Result(property = "menus", column = "role_Id", many = @Many(select = "findMenusByRoleId")),
            @Result(property = "permissions", column = "role_Id", many = @Many(select = "findPermissionsByRoleId"))
    })
    Set<Role> getRolePermissionMap();


    @Select("SELECT m.* FROM role_menu rm LEFT JOIN menus m on rm.menu_id = m.menu_id WHERE rm.role_id = #{roleId}")
    Set<Menu> findMenusByRoleId(int roleId);

    @Select("SELECT p.* from role_permission rp LEFT JOIN permissions p on rp.permission_id = p.permission_id WHERE rp.role_id = #{roleId}")
    Set<Permission> findPermissionsByRoleId(int roleId);
}
