package com.example.mapper;

import com.example.entity.common.Account;
import com.example.entity.common.AccountUser;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {

    @Select("select * from account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select * from account where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);

    @Insert("insert into account (email, username, password) values (#{email}, #{username}, #{password})")
    int createAccount(String username, String password, String email);

    @Insert("insert into datas (username) values (#{username})")
    void createData(String username);

    @Update("update account set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);

    @Delete("delete from account where username = #{username}")
    void deleteAccount(String username);

    @Delete("delete from datas where username = #{username}")
    void deleteAccountData(String username);

    @Select("select guess_number from datas where username = #{username}")
    String findGuessNumberHistoricalRecord(String username);

    @Update("update datas set guess_number = #{s} where username = #{username}")
    void updateGuessNumberHistoricalRecord(String username, String s);

    @Select("select link_game from datas where username = #{username}")
    String findLinkGameHistoricalRecord(String username);

    @Update("update datas set link_game = #{s} where username = #{username}")
    void updateLinkGameHistoricalRecord(String username, String s);
}