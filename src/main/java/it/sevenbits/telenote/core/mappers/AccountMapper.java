package it.sevenbits.telenote.core.mappers;


import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper {

    @Update("UPDATE users\n" +
            "SET style = #{style}\n" +
            "WHERE id = #{id}")
    void changeStyle(UserDetailsImpl user);

    @Update("UPDATE users\n" +
            "SET username = #{name}\n" +
            "WHERE id = #{id}")
    void changeUsername(UserDetailsImpl user);

    @Update("UPDATE users\n" +
            "SET password = #{password}\n" +
            "WHERE id = #{id}")
    void changePass(UserDetailsImpl user);

    @Select("SELECT style\n" +
            "FROM users\n" +
            "WHERE id=#{userId}")
    String getUserStyle(Long userId);
}
