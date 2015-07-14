package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Admin on 09.07.2015.
 */
public interface NoteMapper {
    @Insert("INSERT INTO notes " +
            "(id, userId, category, date, priority, state, subnote, createdAt, updatedAt) " +
            "VALUES " +
            "(DEFAULT, 1, #{category}, #{date}, #{priority} #{state}, #{subnote}, DEFAULT, DEFAULT);")
    void saveNote(final Note note);

    @Select("DELETE " +
            "FROM notes " +
            "WHERE id=#{id};")
    void deleteNote(final Note note);

    @Select("UPDATE users" +
            "SET category = #{category}, date= #{date}, priority = #{priority}, state = #{state}, subnote = #{subnote} " +
            "WHERE id=#{id};")
    void updateNote(final Note note);

}