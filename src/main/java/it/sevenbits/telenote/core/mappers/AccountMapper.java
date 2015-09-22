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
     * @param userId user id.
     */
    @Update("UPDATE users\n" +
            "SET style = #{style}\n" +
            "WHERE id = #{userId}")
    void changeStyle(Long userId);

    /**
     * Updates username by user id.
     * @param userId user id.
     */
    @Update("UPDATE users\n" +
            "SET username = #{name}\n" +
            "WHERE id = #{userId}")
    void changeUsername(Long userId);

    /**
     * Updates userpassword by user id.
     * @param userId user id.
     */
    @Update("UPDATE users\n" +
            "SET password = #{password}\n" +
            "WHERE id = #{userId}")
    void changePass(Long userId);

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
