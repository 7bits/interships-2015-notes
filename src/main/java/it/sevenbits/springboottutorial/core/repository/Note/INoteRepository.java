package it.sevenbits.springboottutorial.core.repository.Note;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.web.domain.NoteModel;

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

    List<Note> getNotesByUserIdList(final List<Long> shareUserIds, final Long parentUserId, final boolean showMyNotes) throws RepositoryException;

    List<UserDetailsImpl> findShareUsers(final Long noteId) throws  RepositoryException;

    Long isParentNoteIdExists(Long noteId) throws RepositoryException;

    UserDetailsImpl getUserWhoOwnNote(Long noteId) throws RepositoryException;

    String getUserStyle(Long userId) throws RepositoryException;

    List<Note> getNotesWithSameUuidById(Long id) throws RepositoryException;

    void updateUuidById(final List<Long> notes, String uuid) throws RepositoryException;

    Long getUserNoteByParentId(Long userId, Long parentId) throws RepositoryException;

    List<NoteModel> getMyNotSharedNoteModelsByUserId(Long userId) throws RepositoryException;

    List<NoteModel> getMySharedNoteModelsByUserId(Long userId) throws RepositoryException;

    List<NoteModel> getForeignSharedNoteModelsByUserId(Long userId) throws RepositoryException;

    List<NoteModel> getAllSharedNoteModels(Long userId) throws RepositoryException;
}
