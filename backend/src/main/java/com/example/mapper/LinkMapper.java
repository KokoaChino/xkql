package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface LinkMapper {
    @Select("select json from link_game_library where id = #{id}")
    String findLinkById(int id);

    @Update("update link_game_library set json = #{json} where id = #{id}")
    void updateLinkById(int id, String json);
}
