package it.sevenbits.springboottutorial.core.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Admin on 13.07.2015.
 */
public class Note {

    private Long id;
    private String text;
    private Timestamp note_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Long parent_note_id;

    public Long getParent_note_id() {
        return parent_note_id;
    }

    public void setParent_note_id(Long parent_note_id) {
        this.parent_note_id = parent_note_id;
    }
    //private List<String> category;
    //private List<String> category;

    //public enum Priority {LOW, MEDIUM, HIGH};
    //private Priority priority;

    //private boolean state;
    //private enum state {TODO, IN_PROGRESS, DONE};
    //private List<String> subnotes;

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

    public Timestamp getNote_date() {
        return note_date;
    }

    public void setNote_date(Timestamp note_date) {
        this.note_date = note_date;
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
}
