package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Admin on 09.07.2015.
 */
public interface UserMapper {
    @Insert("INSERT INTO users " +
            "(email, username, password) " +
            "VALUES " +
            "(#{email}, #{username}, #{password})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void save(final UserDetailsImpl userDetails);

    @Select("SELECT count(*) " +
            "FROM users " +
            "WHERE email=#{email};")
    int isEmailExists(final UserDetailsImpl userDetails);

    @Select("SELECT id " +
            "FROM users " +
            "WHERE email=#{email};")
    Object getIdByEmail(final UserDetailsImpl userDetails);

    @Select("SELECT password " +
            "FROM users " +
            "WHERE id=#{id};")
    String getPasswordById(final UserDetailsImpl userDetails);

    @Update("UPDATE users " +
            "SET password=#{password} " +
            "WHERE id=#{id};")
    void updatePass(final UserDetailsImpl userDetails);
}
