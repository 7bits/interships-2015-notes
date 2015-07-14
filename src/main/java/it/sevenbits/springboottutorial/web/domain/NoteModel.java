package it.sevenbits.springboottutorial.web.domain;

import it.sevenbits.springboottutorial.core.domain.Note;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Admin on 13.07.2015.
 */
public class NoteModel {

    private Long id;
    private Long userId;

    //private List<String> category;
    private List<String> category;
    private Note.Priority priority;
    private Timestamp date;
    private boolean state;
    //private enum state {TODO, IN_PROGRESS, DONE};
    //private List<String> subnotes;
    private String subnote;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Note.Priority getPriority() {
        return priority;
    }

    public void setPriority(Note.Priority priority) {
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getSubnote() {
        return subnote;
    }

    public void setSubnote(String subnote) {
        this.subnote = subnote;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
