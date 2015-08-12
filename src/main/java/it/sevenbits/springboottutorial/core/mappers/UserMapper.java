package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

/**
 * Created by Admin on 09.07.2015.
 */
public interface UserMapper {
    @Insert("INSERT INTO users " +
            "(email, username, password, role, enabled) " +
            "VALUES " +
            "(#{email}, #{username}, #{password}, 'USER', TRUE)")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(final UserDetailsImpl userDetails);

    //Повесить уникальный индекс на email, и тогда он не вставит запись если такой mail есть. ???
    @Select("SELECT count(*)\n" +
            "FROM users\n" +
            "WHERE email = #{email}")
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
            "WHERE email=#{email};")
    void updatePassword(final UserDetailsImpl userDetails);

    @Select("SELECT * FROM users WHERE id=#{id};")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "is_confirmed", property = "isConfirmed"),
            @Result(column = "enabled", property = "enabled"),
            @Result(column = "role", property = "role", javaType = Role.class)
    })
    UserDetailsImpl getUserById(Long id);

    @Select("SELECT * FROM users WHERE email=#{email};")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "is_confirmed", property = "isConfirmed"),
            @Result(column = "enabled", property = "enabled"),
            @Result(column = "role", property = "role", javaType = Role.class),
            @Result(column = "token", property = "token")
    })
    UserDetailsImpl getUserByEmail(String email);

    @Delete("DELETE FROM users WHERE email=#{email} OR id=#{id}")
    void remove(final UserDetailsImpl user);

    @Update("UPDATE users SET is_confirmed=TRUE " +
            "WHERE email=#{email};")
    void confirm(String email);

    @Select("SELECT token FROM users WHERE email=#{email}")
    String getTokenByEmail(String email);

    @Update("UPDATE users SET token=#{token}, token_at=DEFAULT WHERE email=#{email}")
    void setTokenByEmail(@Param("email") String email, @Param("token") String token);
}
