package com.example.mapper;

import com.example.entity.order.WatermarkData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;


@Mapper
public interface WatermarkMapper {

    @Select("select (id, background_image, modified_image, json) from user_defined_style where username = #{username}")
    List<WatermarkData> selectByUsername(String username);
}
