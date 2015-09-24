package it.sevenbits.telenote.web.domain.models;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Model for notes. Also contains user fields: emailOfShareUser, usernameOfShareUser, userAvatar.
 */
public class NoteModel {

    private Long id;
    private String text;
    private Timestamp note_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Long parent_note_id;
    private Long parent_user_id;
    private String uuid;
    private Float note_order;
    private String emailOfShareUser;
    private String usernameOfShareUser;
    private String userAvatar;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    //private boolean state;
    //private enum state {TODO, IN_PROGRESS, DONE};
    //private List<String> subnotes;



    /*public Note.Priority getPriority() {
        return priority;
    }

    public void setPriority(Note.Priority priority) {
        this.priority = priority;
    }*/

    public static class NoteOrderDescComparator implements Comparator<NoteModel> {
        @Override
        public int compare(NoteModel o1, NoteModel o2) {
            return o2.getNote_order().compareTo(o1.getNote_order());
        }
    }

    public static class UpdatedAtDescComparator implements Comparator<NoteModel> {
        @Override
        public int compare(NoteModel o1, NoteModel o2) {
            return o2.getUpdated_at().compareTo(o1.getUpdated_at());
        }
    }

    public NoteModel() {}

    public NoteModel(Long id, String text, Timestamp note_date, Timestamp created_at, Timestamp updated_at, Long parent_note_id, Long parent_user_id, String uuid, Float note_order, String emailOfShareUser, String usernameOfShareUser) {
        this.id = id;
        this.text = text;
        this.note_date = note_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.parent_note_id = parent_note_id;
        this.parent_user_id = parent_user_id;
        this.uuid = uuid;
        this.note_order = note_order;
        this.emailOfShareUser = emailOfShareUser;
        this.usernameOfShareUser = usernameOfShareUser;
    }

    public NoteModel(Long id, String text, Timestamp note_date, Timestamp created_at, Timestamp updated_at, Long parent_note_id, Long parent_user_id, String uuid, Float note_order) {
        this.id = id;
        this.text = text;
        this.note_date = note_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.parent_note_id = parent_note_id;
        this.parent_user_id = parent_user_id;
        this.uuid = uuid;
        this.note_order = note_order;
    }

    public Long getParent_note_id() {
        return parent_note_id;
    }

    public void setParent_note_id(Long parent_note_id) {
        this.parent_note_id = parent_note_id;
    }

    public Long getParent_user_id() {
        return parent_user_id;

    }

    public void setParent_user_id(Long parent_user_id) {
        this.parent_user_id = parent_user_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Float getNote_order() {
        return note_order;
    }

    public void setNote_order(Float note_order) {
        this.note_order = note_order;
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
