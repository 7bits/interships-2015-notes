package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import org.apache.ibatis.annotations.*;
import javax.validation.groups.ConvertGroup;
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

    @Select("SELECT id, text, note_date, created_at, updated_at, note_order, parent_note_id\n" +
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
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "note_order", property = "note_order")
    })
    List<Note> findUserNotes(final Long userId);

    @Insert("INSERT INTO notes " +
            "(text, uuid, note_order) " +
            "VALUES (#{text}, #{uuid}, nextval('note_order_seq'))")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addNote(final Note note);

    @Insert("INSERT INTO usernotes " +
            "(user_id, note_id) " +
            "VALUES " +
            "(#{user_id}, #{note_id})")
    void linkUserWithNote(final UserNote userNote);

    @Insert("INSERT INTO notes\n" +
            "(text, note_date, created_at, parent_note_id, uuid)\n" +
            "SELECT text, note_date, created_at, #{id}, uuid\n" +
            "FROM notes WHERE id=#{id}")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void duplicateNote(final Note note);

    @Select("SELECT count(*) " +
            "FROM usernotes " +
            "WHERE note_id=#{note_id} and user_id=#{user_id}")
    int isNoteBelongToUser(final UserNote userNote);

    @Select("select users.email, users.username\n" +
            "from users\n" +
            "where users.id = (select user_id from usernotes\n" +
            "where note_id = (select parent_note_id from notes where id=#{noteId}))")
    @Results({
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "username"),
    })
    UserDetailsImpl getUserWhoSharedNote(final Long noteId);

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
}