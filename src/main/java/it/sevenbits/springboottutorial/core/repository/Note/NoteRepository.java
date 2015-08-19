package it.sevenbits.springboottutorial.core.repository.Note;


import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.mappers.NoteMapper;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.web.domain.NoteModel;
import org.apache.catalina.User;
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
        mapper.updateNote(note);
    }

    @Override
    public void deleteNote(final Note note) throws RepositoryException {
        if (note == null) {
            throw new RepositoryException("Note is null");
        }
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
            throw new RepositoryException("An error occurred while db accessing: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateNotesByUuid(Note note) throws RepositoryException {
        mapper.updateNotesByUuid(note);
    }

    @Override
    public String getUuidById(Long noteId) throws RepositoryException {
        return mapper.getUuidById(noteId);
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
            throw new RepositoryException("An error occurred while adding note: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isNoteAlreadyShared(UserNote userNote) throws RepositoryException {
        return mapper.isNoteAlreadyShared(userNote) == 0 ? false : true;
    }

    @Override
    public void linkUserWithNote(final Long userId, final Long noteId) throws RepositoryException {
        mapper.linkUserWithNote(userId, noteId);
    }

    @Override
    public void duplicateNote(final Note note) throws RepositoryException {
        mapper.duplicateNote(note);
    }

    @Override
    public boolean isNoteBelongToUser(final Long noteId, final Long userId) throws RepositoryException {
        return mapper.isNoteBelongToUser(noteId, userId) == 0 ? false : true;
    }

    @Override
    public List<Note> getNotesByUserIdList(final List<Long> shareUserIds, final Long parentUserId, final boolean showMyNotes) throws RepositoryException {
        return mapper.getNotesByUserIdList(shareUserIds, parentUserId, showMyNotes);
    }

    @Override
    public UserDetailsImpl getUserWhoSharedNote(final Long noteId) throws RepositoryException {
        return mapper.getUserWhoSharedNote(noteId);
    }

    @Override
    public void updateOrder(final OrderData orderData) throws RepositoryException {
        mapper.updateOrder(orderData);
    }

    @Override
    public List<UserDetailsImpl> findShareUsers(Long noteId) throws RepositoryException {
        return mapper.findShareUsers(noteId);
    }

    @Override
    public void updateFirstElementOrder(final OrderData orderData) throws RepositoryException {
        mapper.updateFirstElementOrder(orderData);
    }

    @Override
    public Long isParentNoteIdExists(Long noteId) throws RepositoryException {
        return mapper.isParentNoteIdExists(noteId);
    }

    @Override
    public UserDetailsImpl getUserWhoOwnNote(Long noteId) throws RepositoryException {
        return mapper.getUserWhoOwnNote(noteId);
    }

    @Override
    public String getUserStyle(Long userId) throws RepositoryException {
        return mapper.getUserStyle(userId);
    }

    @Override
    public List<Note> getNotesWithSameUuidById(Long id) { return mapper.getNotesWithSameUuidById(id); }

    @Override
    public void updateUuidById(final List<Long> notes, String uuid) { mapper.updateUuidById(notes, uuid); }

    @Override
    public Long getUserNoteByParentId(Long userId, Long parentId) throws RepositoryException {
        return mapper.getUserNoteByParentId(userId, parentId);
    }

    @Override
    public List<NoteModel> getMyNotSharedNoteModelsByUserId(Long userId) throws RepositoryException {
        return mapper.getMyNotSharedNoteModelsByUserId(userId);

    }

    @Override
    public List<NoteModel> getMySharedNoteModelsByUserId(Long userId) throws RepositoryException {
        return mapper.getMySharedNoteModelsByUserId(userId);
    }

    @Override
    public List<NoteModel> getForeignSharedNoteModelsByUserId(Long userId) throws RepositoryException {
        return mapper.getForeignSharedNoteModelsByUserId(userId);
    }

    @Override
    public List<NoteModel> getAllSharedNoteModels(Long userId) throws RepositoryException {
        return mapper.getAllSharedNoteModels(userId);
    }
}
