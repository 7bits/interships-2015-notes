package it.sevenbits.telenote.core.repository.Note;


import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserNote;
import it.sevenbits.telenote.core.mappers.NoteMapper;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Qualifier(value = "noteRepository")
public class NoteRepository implements INoteRepository {
    private static Logger LOG = Logger.getLogger(NoteRepository.class);

    @Autowired
    private NoteMapper mapper;

    /**
     * Updates note fields updated_at to update time, parent_note_id to specified in note by note id.
     * @param note POJO that contains note data.
     */
    @Override
    public void updateNote(final Note note) throws RepositoryException {
        if (note == null) {
            throw new RepositoryException("Note is null");
        }
        try {
            mapper.updateNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating note: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes note by note id.
     * @param note POJO for note.
     */
    @Override
    public void deleteNote(final Note note) throws RepositoryException {
        try {
            mapper.deleteNote(note.getId());
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while deleting note: " + e.getMessage(), e);
        }
    }
    /**
     * Gets list of notes in a descending order owned by user with user id.
     * @param userId user id.
     * @return list of notes in a descending order owned by user with user id
     */
    @Override
    public List<Note> findUserNotes(final Long userId) throws RepositoryException {
        try {
             return mapper.findUserNotes(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user notes: " + e.getMessage(), e);
        }
    }

    /**
     * Updates fields note, updated_at of note with specified uuid.
     * @param note POJO that contains note data.
     */
    @Override
    public void updateNotesByUuid(Note note) throws RepositoryException {
        try {
            mapper.updateNotesByUuid(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating notes by UUID: " + e.getMessage(), e);
        }
    }

    /**
     * Gets field uuid of note with specified note id.
     * @param noteId note id.
     * @return field uuid of note with specified note id.
     */
    @Override
    public String getUuidById(Long noteId) throws RepositoryException {
        try {
            return mapper.getUuidById(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting note UUID by ID: " + e.getMessage(), e);
        }
    }

    /**
     * Gets list of users which have note with specified uuid.
     * @param noteUuid uuid of a note.
     * @return list of users which have note with specified uuid.
     */
    @Override
    public List<UserDetailsImpl> getUsersWithSameNoteUuid(String noteUuid) throws RepositoryException{
        try {
            return mapper.getUsersWithSameNoteUuid(noteUuid);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting users with same note UUID: " + e.getMessage(), e);
        }
    }


    @Override
    public void resetAllParentNoteUserId(Long parentNoteId) throws RepositoryException {
        try {
            mapper.resetAllParentNoteUserId(parentNoteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while resetting parent_note_id and parent_user_id: " + e.getMessage(), e);
        }
    }

    /**
     * Adds note with text, uuid, parent_user_id specified in note.
     * @param note POJO that contains note data.
     */
    @Override
    public void addNote(final Note note) throws RepositoryException {
        try {
            mapper.addNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while adding note: " + e.getMessage(), e);
        }
    }

    /**
     * Adds first note with text, uuid, parent_user_id specified in note.
     * @param note POJO that contains note data.
     */
    @Override
    public void addFirstNote(final Note note) throws RepositoryException {
        try {
            mapper.addFirstNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while adding first note: " + e.getMessage(), e);
        }
    }

    /**
     * Checks is note already shared to user with user id specified in userNote
     * by counting records in usernotes with the same uuid as in note with note id specified in userNote.
     * User id fixes user. Note id fixes uuid of a note.
     * @param userNote POJO that contains pair user id - note id.
     * @return true if count of pairs user id - note id not equals 0.
     */
    @Override
    public boolean isNoteAlreadyShared(UserNote userNote) throws RepositoryException {
        try {
            return mapper.isNoteAlreadyShared(userNote) == 0 ? false : true;
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking shared state of note: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a record user id - note id in usernotes table.
     * @param userId user id.
     * @param noteId note id.
     */
    @Override
    public void linkUserWithNote(final Long userId, final Long noteId) throws RepositoryException {
        try {
            mapper.linkUserWithNote(userId, noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while linking user with note: " + e.getMessage(), e);
        }
    }

    /**
     * Adds a clone of a specified note with new note id.
     * @param note POJO that contains note data.
     */
    @Override
    public void duplicateNote(final Note note) throws RepositoryException {
        try {
            mapper.duplicateNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while duplicating note: " + e.getMessage(), e);
        }
    }

    /**
     * Checks is note belongs to user.
     * @param noteId note id.
     * @param userId user id.
     * @return true if count of pairs user id - note id not equals 0.
     */
    @Override
    public boolean isNoteBelongToUser(final Long noteId, final Long userId) throws RepositoryException {
        try {
            return mapper.isNoteBelongsToUser(noteId, userId) == 0 ? false : true;
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking owner of note: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user who shared note with specified note id.
     * @param noteId note id.
     * @return user who shared note with specified note id.
     */
    @Override
    public UserDetailsImpl getUserWhoSharedNote(final Long noteId) throws RepositoryException {
        try {
            return mapper.getUserWhoSharedNote(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user who shared note: " + e.getMessage(), e);
        }
    }

    /**
     * Updates field note_order of note that is between the other two.
     * New value is (previous note_order + next note_order) / 2.
     * @param orderData POJO for ordering notes.
     */
    @Override
    public void updateOrder(final OrderData orderData) throws RepositoryException {
        try {
            mapper.updateOrder(orderData);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating order of note: " + e.getMessage(), e);
        }
    }

    /**
     * Gets list of users, with which note shared.
     * @param noteId note id.
     * @return list of users, with which note shared.
     */
    @Override
    public List<UserDetailsImpl> findShareUsers(Long noteId) throws RepositoryException {
        try {
            return mapper.findShareUsers(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting share users of note: " + e.getMessage(), e);
        }
    }

    /**
     * Updates field note_order of note that is first in the list.
     * New value is (next note_order + 1).
     * @param orderData POJO for ordering notes.
     */
    @Override
    public void updateFirstElementOrder(final OrderData orderData) throws RepositoryException {
        try {
            mapper.updateFirstElementOrder(orderData);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating order of first element note: " + e.getMessage(), e);
        }
    }

    /**
     * Gets field parent_note_id of note with specified note id.
     * @param noteId note id.
     * @return field parent_note_id of note with specified note id.
     */
    @Override
    public Long isParentNoteIdExists(Long noteId) throws RepositoryException {
        try {
            return mapper.isParentNoteIdExists(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking field 'parent_note_id' of note: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user who owns note with specified note id.
     * @param noteId note id.
     * @return user who owns note with0 specified note id.
     */
    @Override
    public UserDetailsImpl getUserWhoOwnsNote(Long noteId) throws RepositoryException {
        try {
            return mapper.getUserWhoOwnsNote(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user who owns note: " + e.getMessage(), e);
        }
    }

    /**
     * Gets list of notes with same uuid as it is in note with specified note id.
     * @param noteId note id.
     * @return list of notes with same uuid as it is in note with specified note id.
     */
    @Override
    public List<Note> getNotesWithSameUuidById(Long noteId) throws RepositoryException{
        try {
            return mapper.getNotesWithSameUuidById(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting with same UUID: " + e.getMessage(), e);
        }
    }

    /**
     * Updates field uuid of notes which specified by notesId list.
     * @param notesIdList list of note ids to be updated.
     * @param uuid uuid to update with.
     */
    @Override
    public void updateUuidByIds(final List<Long> notesIdList, String uuid) throws RepositoryException{
        try {
            mapper.updateUuidByIds(notesIdList, uuid);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating notes UUID: " + e.getMessage(), e);
        }
    }

    /**
     * Gets id of a note with specified field parent_note_id owned by user with userId.
     * @param userId user id.
     * @param parentNoteId parent note id.
     * @return id of a note with specified field parent_note_id owned by user with userId.
     */
    @Override
    public Long getNoteIdByUserIdParentId(Long userId, Long parentNoteId) throws RepositoryException {
        try {
            return mapper.getNoteIdByUserIdParentId(userId, parentNoteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting note_id by user_id and parent_note_id: " + e.getMessage(), e);
        }
    }

    /**
     * Gets note models(note id, owner username, owner email) of notes with specified uuid.
     * @param noteId note id.
     * @return note models(note id, owner username, owner email) of notes with specified uuid.
     */
    @Override
    public List<NoteModel> getAllSharedNoteModels(Long noteId) throws RepositoryException {
        try {
            return mapper.getAllSharedNoteModels(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user's shared notes: " + e.getMessage(), e);
        }
    }

    /**
     * Gets list of notes that contains notes owned by user and notes, which uuid is the same with one of own notes.
     * @param userId user id.
     * @return list of notes that contains notes owned by user and notes, which uuid is the same with one of own notes.
     */
    @Override
    public List<NoteModel> getNotesWithSameNoteUuidByUserId(Long userId) throws RepositoryException {
        try {
            return mapper.getNotesWithSameNoteUuidByUserId(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user's notes with same uuids: " + e.getMessage(), e);
        }
    }
}
