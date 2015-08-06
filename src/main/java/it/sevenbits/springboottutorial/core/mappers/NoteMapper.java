package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import org.apache.ibatis.annotations.*;
import javax.validation.groups.ConvertGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 09.07.2015.
 */
public interface NoteMapper {
    @Delete("DELETE " +
            "FROM notes " +
            "WHERE id=#{id}")
    void deleteNote(final Note note);

    @Update("UPDATE notes " +
            "SET text = #{text}, updated_at = DEFAULT " +
            "WHERE id=#{id}")
    void updateNote(final Note note);

    @Select("SELECT id, text, note_date, created_at, updated_at, parent_note_id, parent_user_id, uuid, note_order\n" +
            "FROM notes INNER JOIN usernotes\n" +
            "ON user_id=#{userId}\n" +
            "WHERE notes.id=usernotes.note_id\n" +
            "ORDER BY note_order DESC")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "text", property = "text"),
            @Result(column = "note_date", property = "note_date"),
            @Result(column = "created_at", property = "created_at"),
            @Result(column = "updated_at", property = "updated_at"),
            @Result(column = "parent_note_id", property = "parent_note_id"),
            @Result(column = "parent_user_id", property = "parent_user_id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "note_order", property = "note_order")
    })
    List<Note> findUserNotes(final Long userId);

    @Select("SELECT id, email, username, enabled\n" +
            "FROM users\n" +
            "WHERE id =\n" +
            "    (SELECT user_id\n" +
            "    FROM usernotes\n" +
            "    WHERE note_id = \n" +
            "       (SELECT id\n" +
            "       FROM notes\n" +
            "       WHERE parent_note_id=#{noteId}))")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "enabled", property = "enabled"),
    })
    List<UserDetailsImpl> findShareUsers(final Long noteId);

    @Insert("INSERT INTO notes\n" +
            "(text, uuid, parent_user_id, note_order)\n" +
            "VALUES (#{text}, #{uuid}, #{parent_user_id},\n" +
            "   (SELECT MAX(note_order) + 1\n" +
            "   FROM notes))")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addNote(final Note note);

    @Insert("INSERT INTO notes\n" +
            "(text, uuid, parent_user_id, note_order)\n" +
            "VALUES (#{text}, #{uuid}, #{parent_user_id}, nextval('note_order_seq'))")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addFirstNote(final Note note);

    @Insert("INSERT INTO usernotes " +
            "(user_id, note_id) " +
            "VALUES " +
            "(#{userId}, #{noteId})")
    void linkUserWithNote(@Param("userId") final Long userId, @Param("noteId") final Long noteId);

    @Insert("INSERT INTO notes\n" +
            "(text, note_date, created_at, parent_note_id, parent_user_id, uuid)\n" +
            "SELECT text, note_date, created_at, #{id}, #{parent_user_id}, uuid\n" +
            "FROM notes WHERE id=#{id}")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void duplicateNote(final Note note);

    @Select("SELECT count(*) " +
            "FROM usernotes " +
            "WHERE note_id=#{noteId} and user_id=#{userId}")
    int isNoteBelongToUser(@Param("noteId")final Long noteId, @Param("userId")final Long userId);

    @Select("SELECT id, email, username, enabled\n" +
            "FROM users\n" +
            "WHERE id =\n" +
            "    (SELECT user_id\n" +
            "    FROM usernotes\n" +
            "    WHERE note_id = \n" +
            "       (SELECT parent_note_id\n" +
            "       FROM notes\n" +
            "       WHERE id=#{noteId}))")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "username"),
            @Result(column = "enabled", property = "enabled"),
    })
    UserDetailsImpl getUserWhoSharedNote(final Long noteId);

    @Select("SELECT id, email, username, enabled\n" +
            "FROM users\n" +
            "WHERE id = \n" +
            "   (SELECT user_id\n" +
            "   FROM usernotes\n" +
            "   WHERE note_id = #{noteId})")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "username"),
            @Result(column = "enabled", property = "enabled"),
    })
    UserDetailsImpl getUserWhoOwnNote(final Long noteId);

    @Select("SELECT parent_note_id\n" +
            "FROM notes\n" +
            "WHERE id = #{noteId}")
    Long isParentNoteIdExists(Long noteId);

    @Select("select parent_note_id\n" +
            "from notes\n" +
            "where id = #{noteId}")
    @Results({
            @Result(column = "id", property = "id")
    })
    Long getParentNoteId(Long noteId);

    @Select("SELECT uuid\n" +
            "from notes\n" +
            "where id = #{id}")
    String getUuidById(Long noteId);

    @Select("UPDATE notes\n" +
            "SET text=#{text}, updated_at=DEFAULT\n" +
            "where uuid = #{uuid}")
    void updateNotesByUuid(Note note);

    @Select("SELECT COUNT(*)\n" +
            "from usernotes\n" +
            "where note_id IN\n" +
            "(SELECT id from notes where uuid=\n" +
            "(SELECT uuid from notes where id=#{note_id}))\n" +
            "and user_id=#{user_id};")
    int isNoteAlreadyShared(UserNote userNote);

    @Update("UPDATE notes\n" +
            "SET note_order = \n" +
            "(SELECT sum(note_order)/2.0\n" +
            "FROM notes\n" +
            "WHERE id = #{id_prev} or id = #{id_next})\n" +
            "WHERE id = #{id_cur}")
    void updateOrder(final OrderData orderData);

    @Update("UPDATE notes\n" +
            "SET note_order = \n" +
            "(SELECT note_order + 1\n" +
            "FROM notes\n" +
            "WHERE id = #{id_next})\n" +
            "WHERE id = #{id_cur}")
    void updateFirstElementOrder(final OrderData orderData);

    //SELECT id, text, note_date, created_at, updated_at, parent_note_id, parent_user_id, uuid, note_order
    //from notes INNER JOIN usernotes on user_id=1 where notes.id=usernotes.note_id AND parent_user_id IN (2,5);
    @Select({"<script>",
            "SELECT id, text, note_date, created_at, updated_at, parent_note_id, parent_user_id, uuid, note_order",
            "from notes",
            "INNER JOIN usernotes on user_id=#{currentUserId}",
            "<where> notes.id=usernotes.note_id",
            "AND (",
            "<if test='list.size() != 0 and showMyNotes'>",
                "parent_user_id IN",
                "<foreach item='item' index='index' collection='list'",
                "open='(' separator=',' close=')'>",
                "#{item}",
                "</foreach> OR parent_user_id IS NULL)",
            "</if>",
            "<if test='list.size() != 0 and !showMyNotes'>",
                "parent_user_id IN",
                "<foreach item='item' index='index' collection='list'",
                "open='(' separator=',' close=')'>",
                "#{item}",
                "</foreach>)",
            "</if>",
            "<if test='list.size() == 0 and showMyNotes'>",
                "parent_user_id IS NULL)",
            "</if>",
            "<if test='list.size() == 0 and !showMyNotes'>",
                "NULL)",
            "</if>",
            "</where> ORDER BY note_order desc",
            "</script>"})
    List<Note> getNotesByUserIdList(@Param("list") List<Long> shareUserIds,
                                    @Param("currentUserId") Long currentUserId,
                                    @Param("showMyNotes") boolean showMyNotes);

    @Select("SELECT style\n" +
            "FROM users\n" +
            "WHERE id=#{userId}")
    String getUserStyle(Long userId);
}