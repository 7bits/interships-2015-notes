package it.sevenbits.springboottutorial.core.mappers;


import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper {

    @Update("UPDATE users\n" +
            "SET style = #{style}\n" +
            "WHERE id = #{id}")
    void changeTheme(UserDetailsImpl user);

    @Update("UPDATE users\n" +
            "SET username = #{name}\n" +
            "WHERE id = #{id}")
    void changeUsername(UserDetailsImpl user);

    @Update("UPDATE users\n" +
            "SET password = #{password}\n" +
            "WHERE id = #{id}")
    void changePass(UserDetailsImpl user);
}
