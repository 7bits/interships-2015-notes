package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.Note;
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

    @Select("SELECT id, text, note_date, created_at, updated_at\n" +
            "FROM notes INNER JOIN usernotes\n" +
            "ON user_id=#{userId}\n" +
            "WHERE notes.id=usernotes.note_id\n" +
            "ORDER BY updated_at DESC")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "text", property = "text"),
            @Result(column = "note_date", property = "note_date"),
            @Result(column = "created_at", property = "created_at"),
            @Result(column = "updated_at", property = "updated_at")
    } )
    List<Note> findUserNotes(final Long userId);

    @Insert("INSERT INTO notes " +
            "(text) " +
            "VALUES (#{text})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addNote(final Note note);

    @Insert("INSERT INTO usernotes " +
            "(user_id, note_id) " +
            "VALUES " +
            "(#{user_id}, #{note_id})")
    void linkUserWithNote(final UserNote userNote);
}