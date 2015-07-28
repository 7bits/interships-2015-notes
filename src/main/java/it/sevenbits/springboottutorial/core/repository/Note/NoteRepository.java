package it.sevenbits.springboottutorial.core.repository.Note;


import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.mappers.NoteMapper;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
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
    public boolean isNoteAlreadyShared(UserNote userNote) throws RepositoryException {
        return mapper.isNoteAlreadyShared(userNote) == 0 ? false : true;
    }

    @Override
    public void linkUserWithNote(final UserNote userNote) throws RepositoryException {
        mapper.linkUserWithNote(userNote);
    }

    @Override
    public void duplicateNote(final Note note) throws RepositoryException {
        mapper.duplicateNote(note);
    }

    @Override
    public boolean isNoteBelongToUser(final UserNote userNote) throws RepositoryException {
        return mapper.isNoteBelongToUser(userNote) == 0 ? false : true;
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
    public void updateFirstElementOrder(final OrderData orderData) throws RepositoryException {
        mapper.updateFirstElementOrder(orderData);
    }
}
