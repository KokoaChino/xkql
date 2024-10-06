package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface EchoMapper {
    @Select("select echo_scoring_system from datas where username = #{username}")
    String findEchoScoringSystem(String username);

    @Update("update datas set echo_scoring_system = #{s} where username = #{username}")
    int updateEchoScoringSystem(String username, String s);
}