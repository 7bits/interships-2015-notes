package it.sevenbits.springboottutorial.core.repository.Note;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;

import java.util.List;

/**
 * Created by Admin on 09.07.2015.
 */
public interface INoteRepository {

    void updateNote(final Note note) throws RepositoryException;

    void deleteNote(final Note note) throws RepositoryException;

    List<Note> findUserNotes(final Long userId) throws RepositoryException;

    void addNote(final Note note) throws RepositoryException;

    void linkUserWithNote(final UserNote userNote) throws RepositoryException;

    void duplicateNote(final Note note) throws RepositoryException;

    boolean isNoteBelongToUser(final UserNote userNote) throws RepositoryException;

    UserDetailsImpl getUserWhoSharedNote(final Long noteId) throws RepositoryException;

    String getUuidById(Long noteId) throws RepositoryException;

    void updateNotesByUuid(final Note note) throws RepositoryException;

    boolean isNoteAlreadyShared(UserNote userNote) throws RepositoryException;
}
