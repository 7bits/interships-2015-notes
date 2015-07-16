package it.sevenbits.springboottutorial.web.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Admin on 13.07.2015.
 */
public class NoteForm {

    private Long id;
    private String text;
    private Timestamp note_date;
    private Timestamp created_at;
    private Timestamp updated_at;

    public NoteForm(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getNote_date() {
        return note_date;
    }

    public void setNote_date(Timestamp note_date) {
        this.note_date = note_date;
    }
}
