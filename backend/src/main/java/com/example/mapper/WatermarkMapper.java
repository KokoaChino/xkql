package com.example.mapper;

import com.example.entity.other.WatermarkData;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface WatermarkMapper {

    @Insert("insert into user_defined_style (username, background_image, preview_image, json)" +
            "values (#{username}, #{backgroundImage}, #{previewImage}, #{json})")
    void insert(WatermarkData watermarkData);

    @Delete("delete from user_defined_style where id = #{id}")
    void deleteById(int id);

    @Select("select * from user_defined_style where username = #{username}")
    List<WatermarkData> selectByUsername(String username);

    @Update("update user_defined_style set background_image = #{backgroundImage}," +
            "preview_image = #{previewImage}, json = #{json} where id = #{id}")
    void update(WatermarkData watermarkData);
}