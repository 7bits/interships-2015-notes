package it.sevenbits.telenote.core.repository.Note;

import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserNote;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.web.domain.models.NoteModel;

import java.util.List;

/**
 * Created by Admin on 09.07.2015.
 */
public interface INoteRepository {

    void updateNote(final Note note) throws RepositoryException;

    void deleteNote(final Note note) throws RepositoryException;

    List<Note> findUserNotes(final Long userId) throws RepositoryException;

    void addNote(final Note note) throws RepositoryException;

    void addFirstNote(final Note note) throws RepositoryException;

    void linkUserWithNote(final Long userId, final Long noteId) throws RepositoryException;

    void duplicateNote(final Note note) throws RepositoryException;

    boolean isNoteBelongToUser(final Long noteId, final Long userId) throws RepositoryException;

    UserDetailsImpl getUserWhoSharedNote(final Long noteId) throws RepositoryException;

    String getUuidById(Long noteId) throws RepositoryException;

    void updateNotesByUuid(final Note note) throws RepositoryException;

    boolean isNoteAlreadyShared(UserNote userNote) throws RepositoryException;

    void updateOrder(final OrderData orderData) throws RepositoryException;

    void updateFirstElementOrder(final OrderData orderData) throws RepositoryException;

    List<UserDetailsImpl> findShareUsers(final Long noteId) throws  RepositoryException;

    Long isParentNoteIdExists(Long noteId) throws RepositoryException;

    UserDetailsImpl getUserWhoOwnsNote(Long noteId) throws RepositoryException;

    List<Note> getNotesWithSameUuidById(Long id) throws RepositoryException;

    void updateUuidByIds(final List<Long> notes, String uuid) throws RepositoryException;

    Long getNoteIdByUserIdParentId(Long userId, Long parentNoteId) throws RepositoryException;

    List<NoteModel> getNotesWithSameNoteUuidByUserId(Long userId) throws RepositoryException;

    List<UserDetailsImpl> getUsersWithSameNoteUuid(final String noteUuid) throws RepositoryException;

    List<NoteModel> getAllSharedNoteModels(Long userId) throws RepositoryException;

    void resetAllParentNoteUserId(final Long parentNoteId) throws RepositoryException;
}
