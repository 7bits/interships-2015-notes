package it.sevenbits.telenote.core.mappers;

import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserNote;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/***
 * Created by Admin on 09.07.2015.
 */
public interface NoteMapper {
    @Delete("DELETE " +
            "FROM notes " +
            "WHERE id=#{id}")
    void deleteNote(final Note note);

    @Update("UPDATE notes N\n" +
            "SET\n" +
            "parent_note_id=null, parent_user_id=(SELECT user_id from usernotes where note_id=N.id)\n" +
            "WHERE parent_note_id=#{parentNoteId}")
    void resetAllParentNoteUserId(final Long parentNoteId);

    @Update("UPDATE notes\n" +
            "SET " +
            "updated_at = DEFAULT, parent_note_id = #{parent_note_id}\n" +
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

    @Select("select users.id, email, username, enabled\n" +
            "from users\n" +
            "inner join usernotes\n" +
            "on users.id=user_id\n" +
            "inner join notes\n" +
            "on usernotes.note_id=notes.id\n" +
            "where notes.parent_note_id=#{noteId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "name"),
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
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "name"),
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
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "email"),
            @Result(column = "username", property = "name"),
            @Result(column = "enabled", property = "enabled"),
    })
    UserDetailsImpl getUserWhoOwnsNote(final Long noteId);

    @Select("SELECT parent_note_id\n" +
            "FROM notes\n" +
            "WHERE id = #{noteId}")
    Long isParentNoteIdExists(Long noteId);

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

    @Select("SELECT parent_note_id, id\n" +
            "FROM notes\n" +
            "WHERE uuid=\n" +
            "   (SELECT uuid\n" +
            "   FROM notes\n" +
            "   WHERE id=#{noteId})")
    List<Note> getNotesWithSameUuidById(Long noteId);

    @Select("select users.id, email, username, enabled\n" +
            "from users\n" +
            "inner join usernotes\n" +
            "on users.id=usernotes.user_id\n" +
            "inner join notes\n" +
            "on usernotes.note_id=notes.id\n" +
            "where notes.uuid=#{noteUuid}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "enabled", property = "enabled"),
    })
    List<UserDetailsImpl> getUsersWithSameNoteUuid(final String noteUuid);

    @Update({"<script>UPDATE notes SET uuid=#{uuid} WHERE id IN ",
            "<foreach item='item' index='index' collection='list'",
                "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach></script>"})
    void updateUuidByIds(@Param("list") List<Long> notesId, @Param("uuid") String uuid);

    @Select("SELECT id\n" +
            "FROM notes\n" +
            "INNER JOIN usernotes\n" +
            "ON id=note_id\n" +
            "WHERE parent_note_id=#{parentId}\n" +
            "AND user_id=#{userId}")
    Long getNoteIdByUserIdParentId(@Param("userId") Long userId, @Param("parentId") Long parentId);

    @Select("select notes.*, users.email, users.username\n" +
            "from notes\n" +
            "inner join usernotes on usernotes.note_id=notes.id\n" +
            "inner join users on users.id=usernotes.user_id\n" +
            //"where users.id<>#{userId}\n" + // раскомментировать, если надо исключить свою заметку
            //"and uuid IN \n" +
            "where uuid IN \n" +
            "(select uuid from notes\n" +
            " inner join usernotes on usernotes.note_id=notes.id\n" +
            " inner join users on users.id=usernotes.user_id\n" +
            " where users.id=#{userId});")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "text", property = "text"),
            @Result(column = "note_date", property = "note_date"),
            @Result(column = "created_at", property = "created_at"),
            @Result(column = "updated_at", property = "updated_at"),
            @Result(column = "parent_note_id", property = "parent_note_id"),
            @Result(column = "parent_user_id", property = "parent_user_id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "note_order", property = "note_order"),
            @Result(column = "email", property = "emailOfShareUser"),
            @Result(column = "username", property = "usernameOfShareUser")
    })
    List<NoteModel> getNotesWithSameNoteUuidByUserId(Long userId);

    @Select("SELECT notes.id, users.email, users.username\n" +
            "from notes \n" +
            "inner join usernotes on notes.id = usernotes.note_id\n" +
            "inner join users on users.id = usernotes.user_id\n" +
            "where uuid=(select uuid from notes where id=#{noteId})\n" +
            "and notes.id != #{noteId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "email", property = "emailOfShareUser"),
            @Result(column = "username", property = "usernameOfShareUser")
    })
    List<NoteModel> getAllSharedNoteModels(@Param("noteId") Long noteId);

}