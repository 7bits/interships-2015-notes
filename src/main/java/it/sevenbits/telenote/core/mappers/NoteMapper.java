package it.sevenbits.telenote.core.mappers;

import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserNote;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/***
 * Mapper for note operations.
 */
public interface NoteMapper {
    /**
     * Deletes note by note id.
     * @param noteId note id.
     */
    @Delete("DELETE " +
            "FROM notes " +
            "WHERE id=#{id}")
    void deleteNote(Long noteId);

    /**
     * Resets db field parent_note_id to null, changes db field parent_user_id to user id who owns note.
     * Do this for all notes with parent_note_id field equals parentNoteId.
     * @param parentNoteId id of a note, which created other note by sharing.
     */
    @Update("UPDATE notes\n" +
            "SET\n" +
            "parent_note_id=null, parent_user_id=(SELECT user_id from usernotes where note_id=notes.id)\n" +
            "WHERE parent_note_id=#{parentNoteId}")
    void resetAllParentNoteUserId(final Long parentNoteId);

    /**
     * Updates note fields updated_at to update time, parent_note_id to specified in note by note id.
     * @param note POJO that contains note data.
     */
    @Update("UPDATE notes\n" +
            "SET " +
            "updated_at = DEFAULT, parent_note_id = #{parent_note_id}\n" +
            "WHERE id=#{id}")
    void updateNote(final Note note);

    /**
     * Gets list of notes in a descending order owned by user with user id.
     * @param userId user id.
     * @return list of notes in a descending order owned by user with user id
     */
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

    /**
     * Gets list of users, with which note shared.
     * @param noteId note id.
     * @return list of users, with which note shared.
     */
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

    /**
     * Adds note with text, uuid, parent_user_id specified in note.
     * @param note POJO that contains note data.
     */
    @Insert("INSERT INTO notes\n" +
            "(text, uuid, parent_user_id, note_order)\n" +
            "VALUES (#{text}, #{uuid}, #{parent_user_id},\n" +
            "   (SELECT MAX(note_order) + 1\n" +
            "   FROM notes))")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addNote(final Note note);

    /**
     * Adds first note with text, uuid, parent_user_id specified in note.
     * @param note POJO that contains note data.
     */
    @Insert("INSERT INTO notes\n" +
            "(text, uuid, parent_user_id, note_order)\n" +
            "VALUES (#{text}, #{uuid}, #{parent_user_id}, nextval('note_order_seq'))")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addFirstNote(final Note note);

    /**
     * Adds a record user id - note id in usernotes table.
     * @param userId user id.
     * @param noteId note id.
     */
    @Insert("INSERT INTO usernotes " +
            "(user_id, note_id) " +
            "VALUES " +
            "(#{userId}, #{noteId})")
    void linkUserWithNote(@Param("userId") final Long userId, @Param("noteId") final Long noteId);

    /**
     * Adds a clone of a specified note with new note id.
     * @param note POJO that contains note data.
     */
    @Insert("INSERT INTO notes\n" +
            "(text, note_date, created_at, parent_note_id, parent_user_id, uuid)\n" +
            "SELECT text, note_date, created_at, #{id}, #{parent_user_id}, uuid\n" +
            "FROM notes WHERE id=#{id}")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void duplicateNote(final Note note);

    /**
     * Checks is note belongs to user by counting pairs user id - note id.
     * @param noteId note id.
     * @param userId user id.
     * @return count of pairs user id - note id.
     */
    @Select("SELECT count(*) " +
            "FROM usernotes " +
            "WHERE note_id=#{noteId} and user_id=#{userId}")
    int isNoteBelongsToUser(@Param("noteId") final Long noteId, @Param("userId") final Long userId);

    /**
     * Gets user who shared note with specified note id.
     * @param noteId note id.
     * @return user who shared note with specified note id.
     */
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

    /**
     * Gets user who owns note with specified note id.
     * @param noteId note id.
     * @return user who owns note with specified note id.
     */
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

    /**
     * Gets field parent_note_id of note with specified note id.
     * @param noteId note id.
     * @return field parent_note_id of note with specified note id.
     */
    @Select("SELECT parent_note_id\n" +
            "FROM notes\n" +
            "WHERE id = #{noteId}")
    Long isParentNoteIdExists(Long noteId);

    /**
     * Gets field uuid of note with specified note id.
     * @param noteId note id.
     * @return field uuid of note with specified note id.
     */
    @Select("SELECT uuid\n" +
            "from notes\n" +
            "where id = #{id}")
    String getUuidById(Long noteId);

    /**
     * Updates fields note, updated_at of note with specified uuid.
     * @param note POJO that contains note data.
     */
    @Select("UPDATE notes\n" +
            "SET text=#{text}, updated_at=DEFAULT\n" +
            "where uuid = #{uuid}")
    void updateNotesByUuid(Note note);

    /**
     * Checks is note already shared to user. with user id specified in userNote
     * by counting records in usernotes with the same uuid as in note with note id specified in userNote.
     * User id fixes user. Note id fixes uuid of a note.
     * @param userNote POJO that contains pair user id - note id.
     * @return count of records user id - note id.
     */
    @Select("SELECT COUNT(*)\n" +
            "from usernotes\n" +
            "where note_id IN\n" +
            "(SELECT id from notes where uuid=\n" +
            "(SELECT uuid from notes where id=#{note_id}))\n" +
            "and user_id=#{user_id};")
    int isNoteAlreadyShared(UserNote userNote);

    /**
     * Updates field note_order of note that is between the other two.
     * New value is (previous note_order + next note_order) / 2.
     * @param orderData POJO for ordering notes.
     */
    @Update("UPDATE notes\n" +
            "SET note_order = \n" +
            "(SELECT sum(note_order)/2.0\n" +
            "FROM notes\n" +
            "WHERE id = #{id_prev} or id = #{id_next})\n" +
            "WHERE id = #{id_cur}")
    void updateOrder(final OrderData orderData);

    /**
     * Updates field note_order of note that is first in the list.
     * New value is (next note_order + 1).
     * @param orderData POJO for ordering notes.
     */
    @Update("UPDATE notes\n" +
            "SET note_order = \n" +
            "(SELECT note_order + 1\n" +
            "FROM notes\n" +
            "WHERE id = #{id_next})\n" +
            "WHERE id = #{id_cur}")
    void updateFirstElementOrder(final OrderData orderData);

    /**
     * Gets list of notes with same uuid as it is in note with specified note id.
     * @param noteId note id.
     * @return list of notes with same uuid as it is in note with specified note id.
     */
    @Select("SELECT parent_note_id, id\n" +
            "FROM notes\n" +
            "WHERE uuid=\n" +
            "   (SELECT uuid\n" +
            "   FROM notes\n" +
            "   WHERE id=#{noteId})")
    List<Note> getNotesWithSameUuidById(Long noteId);

    /**
     * Gets list of users which have note with specified uuid.
     * @param noteUuid uuid of a note.
     * @return list of users which have note with specified uuid.
     */
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

    /**
     * Updates field uuid of notes which specified by notesId list.
     * @param notesId list of note ids to be updated.
     * @param uuid uuid to update with.
     */
    @Update({"<script>UPDATE notes SET uuid=#{uuid} WHERE id IN ",
            "<foreach item='item' index='index' collection='list'",
                "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach></script>"})
    void updateUuidByIds(@Param("list") List<Long> notesId, @Param("uuid") String uuid);

    /**
     * Gets id of a note with specified field parent_note_id owned by user with userId.
     * @param userId user id.
     * @param parentNoteId parent note id.
     * @return id of a note with specified field parent_note_id owned by user with userId.
     */
    @Select("SELECT id\n" +
            "FROM notes\n" +
            "INNER JOIN usernotes\n" +
            "ON id=note_id\n" +
            "WHERE parent_note_id=#{parentNoteId}\n" +
            "AND user_id=#{userId}")
    Long getNoteIdByUserIdParentId(@Param("userId") Long userId, @Param("parentId") Long parentNoteId);

    /**
     * Gets list of notes that contains notes owned by user and notes, which uuid is the same with one of own notes.
     * @param userId user id.
     * @return list of notes that contains notes owned by user and notes, which uuid is the same with one of own notes.
     */
    @Select("select notes.*, users.email, users.username\n" +
            "from notes\n" +
            "inner join usernotes on usernotes.note_id=notes.id\n" +
            "inner join users on users.id=usernotes.user_id\n" +
            //"where users.id<>#{userId}\n" + // uncomment if need to exclude own note
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

    /**
     * Gets note models(note id, owner username, owner email) of notes with specified uuid.
     * @param noteId note id.
     * @return note models(note id, owner username, owner email) of notes with specified uuid.
     */
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
