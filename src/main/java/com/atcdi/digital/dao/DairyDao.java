package com.atcdi.digital.dao;

import com.atcdi.digital.entity.User;
import com.atcdi.digital.entity.daliy.Dairy;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DairyDao {
    @Select("SELECT * FROM dairy WHERE user_id = #{userId}")
    List<Dairy> getDairyByUser(int userId);

    @Update("<script> UPDATE dairy" +
            "<trim prefix='SET' suffixOverrides=','>" +
            "work_item = #{workItem}," +
            "date = #{date}," +
            "spend_time = #{spendTime}," +
            "<if test=\"workDesc != null\"> work_desc = #{workDesc}, </if>" +
            "</trim> WHERE dairy_id = #{dairyId}" +
            "</script>")
    boolean updateDairy(Dairy dairy);


    @Delete("DELETE FROM dairy WHERE dairy_id=#{dairyId}")
    boolean deleteDairy(int dairyId);

    @Insert("INSERT INTO dairy (user_id,work_item,date,work_desc,spend_time) VALUES(#{userId},#{workItem},#{date},#{workDesc},#{spendTime})")
    @Options(useGeneratedKeys = true, keyProperty = "dairyId", keyColumn = "dairy_id")
    boolean insertDairy(Dairy dairy);
}
