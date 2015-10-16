package it.sevenbits.telenote.core.mappers;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.Role;
import org.apache.ibatis.annotations.*;

/**
 * Mapper for user operations.
 */
public interface UserMapper {
    /**
     * Adds new user with specified email. username, password, role, enabled flag.
     * @param userDetails POJO for user.
     */
    @Insert("INSERT INTO users " +
            "(email, username, password, role, enabled) " +
            "VALUES " +
            "(#{email}, #{name}, #{password}, 'USER', TRUE)")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void create(final UserDetailsImpl userDetails);

    //Add an unique index on the email
    /**
     * Checks does email exist by counting records in users with specified email.
     * @param email user email.
     * @return count of records with specified email.
     */
    @Select("SELECT count(*)\n" +
            "FROM users\n" +
            "WHERE email = #{email}")
    int isEmailExists(final String email);

    /**
     * Gets user id by user email.
     * @param email POJO for user.
     * @return user id.
     */
    @Select("SELECT id " +
            "FROM users " +
            "WHERE email=#{email};")
    Object getIdByEmail(final String email);

    /**
     * Gets user password(hash) by user id.
     * @param userId POJO for user.
     * @return user password(hash).
     */
    @Select("SELECT password " +
            "FROM users " +
            "WHERE id=#{id};")
    String getPasswordById(final Long userId);

    /**
     * Updates user password(hash) by user email.
     * @param userDetails POJO for user.
     */
    @Update("UPDATE users " +
            "SET password=#{password} " +
            "WHERE email=#{email};")
    void updatePassword(final UserDetailsImpl userDetails);

    /**
     * Gets user data by user id.
     * @param userId user id.
     * @return user data by user id.
     */
    @Select("SELECT * FROM users WHERE id=#{userId};")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "is_confirmed", property = "isConfirmed"),
            @Result(column = "enabled", property = "enabled"),
            @Result(column = "role", property = "role", javaType = Role.class)
    })
    UserDetailsImpl getUserById(Long userId);

    /**
     * Gets user data by user email.
     * @param email user email.
     * @return user data by user email.
     */
    @Select("SELECT * FROM users WHERE email=#{email};")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "is_confirmed", property = "isConfirmed"),
            @Result(column = "enabled", property = "enabled"),
            @Result(column = "role", property = "role", javaType = Role.class),
            @Result(column = "token", property = "token")
    })
    UserDetailsImpl getUserByEmail(String email);

    /**
     * Removes user from users by email or id.
     * @param userDetails POJO for user.
     */
    @Delete("DELETE FROM users\n" +
            "WHERE email=#{email} OR id=#{id}")
    void remove(final UserDetailsImpl userDetails);

    /**
     * Removes all records from users.
     */
    @Delete("DELETE FROM users")
    void deleteUsers();

    /**
     * Removes all records from notes.
     */
    @Delete("DELETE FROM notes")
    void deleteNotes();

    /**
     * Removes all records from usernotes.
     */
    @Delete("DELETE FROM usernotes")
    void deleteUsernotes();

    /**
     * Sets user is_confirmed flag to true by email.
     * @param email user email.
     */
    @Update("UPDATE users SET is_confirmed=TRUE " +
            "WHERE email=#{email};")
    void confirm(String email);

    /**
     * Gets user token by user email.
     * @param email user email.
     * @return user token by user email.
     */
    @Select("SELECT token FROM users WHERE email=#{email}")
    String getTokenByEmail(String email);

    /**
     * Sets user token by user email
     * @param email user email.
     * @param token user token.
     */
    @Update("UPDATE users SET token=#{token}, token_at=DEFAULT\n" +
            "WHERE email=#{email}")
    void setTokenByEmail(@Param("email") String email, @Param("token") String token);

    /**
     * Sets user typesorting by user id
     * @param userId
     * @param type
     */
    @Update("UPDATE users SET typesorting=#{type}\n" +
    "       WHERE id=#{userId}")
    void updateTypesorting(@Param("userId") Long userId, @Param("type") int type);
}
