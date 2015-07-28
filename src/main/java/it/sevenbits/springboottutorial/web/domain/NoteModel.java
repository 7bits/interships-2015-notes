package it.sevenbits.springboottutorial.web.domain;

import java.sql.Timestamp;

/**
 * Created by Admin on 13.07.2015.
 */
public class NoteModel {

    private Long id;
    private String text;
    private Timestamp note_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String emailOfShareUser;
    private String usernameOfShareUser;

    //private List<String> category;
    //private List<String> category;
    //private Note.Priority priority;

    //private boolean state;
    //private enum state {TODO, IN_PROGRESS, DONE};
    //private List<String> subnotes;



    /*public Note.Priority getPriority() {
        return priority;
    }

    public void setPriority(Note.Priority priority) {
        this.priority = priority;
    }*/


    public NoteModel(Long id, String text, Timestamp note_date, Timestamp created_at, Timestamp updated_at,
                     String emailOfShareUser, String usernameOfShareUser) {
        this.id = id;
        this.text = text;
        this.note_date = note_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.emailOfShareUser = emailOfShareUser;
        this.usernameOfShareUser = usernameOfShareUser;
    }

    public NoteModel(Long id, String text, Timestamp note_date, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.text = text;
        this.note_date = note_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailOfShareUser() {
        return emailOfShareUser;
    }

    public void setEmailOfShareUser(String emailOfShareUser) {
        this.emailOfShareUser = emailOfShareUser;
    }

    public String getUsernameOfShareUser() {
        return usernameOfShareUser;
    }

    public void setUsernameOfShareUser(String usernameOfShareUser) {
        this.usernameOfShareUser = usernameOfShareUser;
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
