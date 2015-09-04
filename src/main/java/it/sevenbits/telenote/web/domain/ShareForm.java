package it.sevenbits.telenote.web.domain;

/**
 * Created by andy on 22.07.15.
 */
public class ShareForm {
    private Long noteId;
    private String userEmail;

    public ShareForm(Long noteId, String userEmail) {
        this.noteId = noteId;
        this.userEmail = userEmail;
    }

    public Long getNoteId() {

        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
