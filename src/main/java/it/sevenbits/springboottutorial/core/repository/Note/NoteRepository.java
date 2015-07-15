package it.sevenbits.springboottutorial.core.repository.Note;


import it.sevenbits.springboottutorial.core.domain.Note;
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
    public void saveNote(final Note note) throws RepositoryException {
        if (note == null) {
            throw new RepositoryException("Note is null");
        }
        try {
            mapper.saveNote(note);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateNote(Note note) throws RepositoryException {
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
    public void deleteNote(Note note) throws RepositoryException {
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
}
