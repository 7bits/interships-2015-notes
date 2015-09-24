package it.sevenbits.telenote.core.mappers;


import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Mapper for user account operations.
 */
public interface AccountMapper {
    /**
     * Updates user theme by user id.
     * @param user POJO that contains theme name and user id.
     */
    @Update("UPDATE users\n" +
            "SET style = #{style}\n" +
            "WHERE id = #{id}")
    void changeStyle(UserDetailsImpl user);

    /**
     * Updates username by user id.
     * @param user POJO that contains username and user id.
     */
    @Update("UPDATE users\n" +
            "SET username = #{name}\n" +
            "WHERE id = #{id}")
    void changeUsername(UserDetailsImpl user);

    /**
     * Updates user password by user id.
     * @param user POJO that contains user password and user id.
     */
    @Update("UPDATE users\n" +
            "SET password = #{password}\n" +
            "WHERE id = #{id}")
    void changePass(UserDetailsImpl user);

    /**
     * Gets user theme by user id.
     * @param userId user id.
     * @return user theme.
     */
    @Select("SELECT style\n" +
            "FROM users\n" +
            "WHERE id=#{userId}")
    String getUserStyle(Long userId);
}
