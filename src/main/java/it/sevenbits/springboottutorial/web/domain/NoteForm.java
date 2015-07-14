package it.sevenbits.springboottutorial.web.domain;

import it.sevenbits.springboottutorial.core.domain.Note;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Admin on 13.07.2015.
 */
public class NoteForm {

    private List<String> category;
    private Note.Priority priority;
    private Timestamp date;
    private boolean state;
    private String subnote;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Note.Priority getPriority() {
        return priority;
    }

    public void setPriority(Note.Priority priority) {
        this.priority = priority;
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
}
