package it.sevenbits.springboottutorial.core.repository.Note;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;

/**
 * Created by Admin on 09.07.2015.
 */
public interface INoteRepository {

    void saveNote(final Note note) throws RepositoryException;
    void updateNote(final Note note) throws RepositoryException;
    void deleteNote(final Note note) throws RepositoryException;
}
