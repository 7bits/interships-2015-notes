package it.sevenbits.springboottutorial.core.domain;

/**
 * Created by vik on 16.07.15.
 */
public class UserNote {

    private Long user_id;
    private Long note_id;

    public UserNote() {}

    public UserNote(Long user_id, Long note_id) {
        this.user_id = user_id;
        this.note_id = note_id;
    }

    public Long getUser_id() {

        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getNote_id() {
        return note_id;
    }

    public void setNote_id(Long note_id) {
        this.note_id = note_id;
    }
}
