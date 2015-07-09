package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by Admin on 09.07.2015.
 */
public interface UserMapper {
    @Insert("INSERT INTO users " +
            "(id, email, username, password, created_at, updated_at, is_confirmed)" +
            "VALUES" +
            "(DEFAULT, #{email}, #{name}, 'pass', DEFAULT, DEFAULT, DEFAULT);")
    void save(final UserDetailsImpl userDetails);
}
