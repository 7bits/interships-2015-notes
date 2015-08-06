package it.sevenbits.springboottutorial.core.mappers;


import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper {

    @Update("UPDATE users\n" +
            "SET style = #{style}\n" +
            "WHERE id = #{id}")
    void changeTheme(UserDetailsImpl user);

    @Update("UPDATE users\n" +
            "SET username = #{username}\n" +
            "WHERE id = #{id}")
    void changeUsername(UserDetailsImpl user);
}
