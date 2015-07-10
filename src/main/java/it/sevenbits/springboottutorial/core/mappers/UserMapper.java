package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 09.07.2015.
 */
public interface UserMapper {
    @Insert("INSERT INTO users " +
            "(id, email, username, password, created_at, updated_at, is_confirmed) " +
            "VALUES " +
            "(DEFAULT, #{email}, #{username}, #{password}, DEFAULT, DEFAULT, DEFAULT);")
    void save(final UserDetailsImpl userDetails);

    @Select("SELECT count(*) " +
            "FROM users " +
            "WHERE email=#{email};")
    int isEmailExists(final UserDetailsImpl userDetails);
}
