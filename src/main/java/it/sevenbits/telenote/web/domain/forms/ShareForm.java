package it.sevenbits.telenote.web.domain.forms;

/**
 * Form for sharing note with other users.
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
