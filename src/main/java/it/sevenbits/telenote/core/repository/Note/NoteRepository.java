package it.sevenbits.telenote.core.repository.Note;


import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserNote;
import it.sevenbits.telenote.core.mappers.NoteMapper;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.web.domain.NoteModel;
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

    @Override
    public void deleteNote(final Note note) throws RepositoryException {
        try {
            mapper.deleteNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while deleting note: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Note> findUserNotes(final Long userId) throws RepositoryException {
        try {
             return mapper.findUserNotes(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user notes: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateNotesByUuid(Note note) throws RepositoryException {
        try {
            mapper.updateNotesByUuid(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating notes by UUID: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUuidById(Long noteId) throws RepositoryException {
        try {
            return mapper.getUuidById(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting note UUID by ID: " + e.getMessage(), e);
        }
    }

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

    @Override
    public void addNote(final Note note) throws RepositoryException {
        try {
            mapper.addNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while adding note: " + e.getMessage(), e);
        }
    }

    @Override
    public void addFirstNote(final Note note) throws RepositoryException {
        try {
            mapper.addFirstNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while adding first note: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isNoteAlreadyShared(UserNote userNote) throws RepositoryException {
        try {
            return mapper.isNoteAlreadyShared(userNote) == 0 ? false : true;
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking shared state of note: " + e.getMessage(), e);
        }
    }

    @Override
    public void linkUserWithNote(final Long userId, final Long noteId) throws RepositoryException {
        try {
            mapper.linkUserWithNote(userId, noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while linking user with note: " + e.getMessage(), e);
        }
    }

    @Override
    public void duplicateNote(final Note note) throws RepositoryException {
        try {
            mapper.duplicateNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while duplicating note: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isNoteBelongToUser(final Long noteId, final Long userId) throws RepositoryException {
        try {
            return mapper.isNoteBelongToUser(noteId, userId) == 0 ? false : true;
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking owner of note: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDetailsImpl getUserWhoSharedNote(final Long noteId) throws RepositoryException {
        try {
            return mapper.getUserWhoSharedNote(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user who shared note: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateOrder(final OrderData orderData) throws RepositoryException {
        try {
            mapper.updateOrder(orderData);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating order of note: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserDetailsImpl> findShareUsers(Long noteId) throws RepositoryException {
        try {
            return mapper.findShareUsers(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting share users of note: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateFirstElementOrder(final OrderData orderData) throws RepositoryException {
        try {
            mapper.updateFirstElementOrder(orderData);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating order of first element note: " + e.getMessage(), e);
        }
    }

    @Override
    public Long isParentNoteIdExists(Long noteId) throws RepositoryException {
        try {
            return mapper.isParentNoteIdExists(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking field 'parent_note_id' of note: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDetailsImpl getUserWhoOwnsNote(Long noteId) throws RepositoryException {
        try {
            return mapper.getUserWhoOwnsNote(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user who owns note: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Note> getNotesWithSameUuidById(Long noteId) throws RepositoryException{
        try {
            return mapper.getNotesWithSameUuidById(noteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting with same UUID: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateUuidByIds(final List<Long> notes, String uuid) throws RepositoryException{
        try {
            mapper.updateUuidByIds(notes, uuid);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating notes UUID: " + e.getMessage(), e);
        }
    }

    @Override
    public Long getNoteIdByUserIdParentId(Long userId, Long parentNoteId) throws RepositoryException {
        try {
            return mapper.getNoteIdByUserIdParentId(userId, parentNoteId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting note_id by user_id and parent_note_id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NoteModel> getAllSharedNoteModels(Long userId) throws RepositoryException {
        try {
            return mapper.getAllSharedNoteModels(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user's shared notes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NoteModel> getNotesWithSameNoteUuidByUserId(Long userId) throws RepositoryException {
        try {
            return mapper.getNotesWithSameNoteUuidByUserId(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user's notes with same uuids: " + e.getMessage(), e);
        }
    }
}
