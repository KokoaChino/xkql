package com.example.mapper;

import org.apache.ibatis.annotations.*;


@Mapper
public interface CharacterMapper {

    @Insert("insert into character_library values (#{name}, #{json})")
    void insert(String name, String json);

    @Select("select json from character_library where name = #{name}")
    String selectByName(String name);
}