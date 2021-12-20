package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Update("UPDATE USERS SET password = #{password} WHERE username = #{username}")
    int updateUser(String username);

    @Delete("DELETE FROM USERS WHERE username = #{username}")
    void deleteUser(String username);

}
