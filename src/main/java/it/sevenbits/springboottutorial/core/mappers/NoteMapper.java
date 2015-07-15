package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.Note;
import org.apache.ibatis.annotations.*;

import javax.validation.groups.ConvertGroup;
import java.util.List;

/**
 * Created by Admin on 09.07.2015.
 */
public interface NoteMapper {
    @Insert("INSERT INTO notes " +
            "(id, date, text, createdAt, updatedAt) " +
            "VALUES " +
            "(DEFAULT, #{date}, #{text}, DEFAULT, DEFAULT)")
    void saveNote(final Note note);

    @Delete("DELETE " +
            "FROM notes " +
            "WHERE id=#{id}")
    void deleteNote(final Note note);

    @Update("UPDATE users" +
            "SET date= #{date}, text = #{text} " +
            "WHERE id=#{id}")
    void updateNote(final Note note);

    @Select("SELECT id, text, note_date, created_at, updated_at\n" +
            "FROM notes INNER JOIN usernotes\n" +
            "ON user_id=#{userId}\n" +
            "WHERE notes.id=usernotes.note_id")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "text", property = "text"),
            @Result(column = "note_date", property = "note_date"),
            @Result(column = "created_at", property = "created_at"),
            @Result(column = "updated_at", property = "updated_at")
    } )
    List<Note> findUserNotes(final Long userId);
}