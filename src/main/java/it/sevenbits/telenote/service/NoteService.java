package it.sevenbits.telenote.service;

import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.UserNote;
import it.sevenbits.telenote.core.repository.Note.INoteRepository;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.core.repository.User.IUserRepository;
import it.sevenbits.telenote.utils.Helper;

import it.sevenbits.telenote.web.domain.forms.NoteForm;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import it.sevenbits.telenote.web.domain.forms.ShareForm;
import it.sevenbits.telenote.web.domain.models.ResponseMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.StringReader;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Admin on 13.07.2015.
 */
@Service
public class NoteService {
    private static final Logger LOG = Logger.getLogger(Service.class);

    @Autowired
    @Qualifier(value = "noteRepository")
    private INoteRepository repository;

    @Autowired
    @Qualifier(value = "userRepository")
    private IUserRepository userRepository;

    @Autowired
    private AccountService accountService;

    /** Transaction settings name */
    private static final String TX_NAME = "txService";
    /** Spring Transaction Manager */
    @Autowired
    private PlatformTransactionManager txManager;
    /** Transaction settings object */
    private DefaultTransactionDefinition customTx;

    public NoteService() {
            this.customTx = new DefaultTransactionDefinition();
            this.customTx.setName(TX_NAME);
            this.customTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void updateNote(final NoteForm form, Long userId) throws ServiceException {
        final Note note = new Note();
        note.setId(form.getId());
        note.setText(form.getText());
        TransactionStatus status = null;

        try {
            status = txManager.getTransaction(customTx);
            if (repository.isNoteBelongToUser(note.getId(), userId)) {
                note.setUuid(repository.getUuidById(note.getId()));
                repository.updateNotesByUuid(note);
            } else {
                LOG.error("An error occurred while saving note. Current note is not belong to user.");
                throw new ServiceException("Current note is not belong to user!");
            }
            txManager.commit(status);
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while saving note. UserId: %d, NoteId: %d. Rolling back.", userId, note.getId()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}

            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public void deleteNote(final Note note, Long userId) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);

            UserNote userNote = new UserNote(userId, note.getId());

            if (repository.isNoteBelongToUser(note.getId(), userId)) {
                repository.deleteNote(note);
                repository.resetAllParentNoteUserId(note.getId());
            } else {
                LOG.error("Current note is not belong to user.");
                throw new ServiceException("Current note is not belong to user!");
            }
            txManager.commit(status);
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while deleting note. UserId: %d, NoteId: %d. Rolling back.", userId, note.getId()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}

            throw new ServiceException("An error occurred while deleting note: " + e.getMessage(), e);
        }
    }

    public List<NoteModel> findUserNotes(final Long userId) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            List<Note> notes = repository.findUserNotes(userId);
            List<NoteModel> models = new ArrayList<>(notes.size());
            for (Note n : notes) {
                models.add(new NoteModel(n.getId(), n.getText(), n.getNote_date(), n.getCreated_at(), n.getUpdated_at(),
                        n.getParent_note_id(), n.getParent_user_id(), n.getUuid(), n.getNote_order()));
            }

            txManager.commit(status);
            return models;
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while finding user notes. UserId: %d. Rolling back.", userId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while finding user notes: " + e.getMessage());
        }
    }

    public List<UserDetailsImpl> findShareUsers(final Long noteId) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            List<UserDetailsImpl> shareUsers = repository.findShareUsers(noteId);
            txManager.commit(status);
            return shareUsers;
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while finding share users. NoteId: %d. Rolling back.", noteId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while finding share users: " + e.getMessage());
        }
    }

    public Long addNote(final NoteForm form, Long userId) throws ServiceException {
        Note note = new Note();
        note.setText(form.getText());
        note.setUuid(Helper.generateUUID());
        note.setParent_user_id(userId);

        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            if (repository.findUserNotes(userId).size() == 0) {
                repository.addFirstNote(note);
            } else {
                repository.addNote(note);
            }

            UserNote userNote = new UserNote(userId, note.getId());
            repository.linkUserWithNote(userId, note.getId());

            txManager.commit(status);
            return note.getId();
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while  adding note. UserId: %d, NoteId: %d. Rolling back.", userId, note.getId()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while adding note: " + e.getMessage());
        }
    }

    public String[] shareNote(final ShareForm form, Long parentUserId) throws RepositoryException, ServiceException {
        Long parentNoteId = form.getNoteId();
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(form.getUserEmail());
        String[] result = new String[3];

        final Note note = new Note();
        note.setId(form.getNoteId());

        final UserNote whoShare = new UserNote();
        whoShare.setNote_id(form.getNoteId());

        final UserNote toWhomShare = new UserNote();
        TransactionStatus status = null;

        try {
            status = txManager.getTransaction(customTx);
            if (userRepository.isEmailExists(userDetails)) {
                whoShare.setUser_id(parentUserId);
                toWhomShare.setUser_id(userRepository.getIdByEmail(userDetails));

                if (whoShare.getUser_id() == toWhomShare.getUser_id()) {
                    LOG.warn(String.format("Can't share own note. UserId: %d, NoteId: %d.",
                            whoShare.getUser_id(), parentNoteId));
                    result[0] = "Can't share own note.";
                    result[1] = "406";
                    return result;
                }

                if (repository.isNoteBelongToUser(parentNoteId, whoShare.getUser_id())) {
                    final UserNote curNoteIdNextUser = new UserNote();
                    curNoteIdNextUser.setNote_id(whoShare.getNote_id());
                    curNoteIdNextUser.setUser_id(toWhomShare.getUser_id());

                    if (repository.isNoteAlreadyShared(curNoteIdNextUser)) {
                        LOG.warn(String.format("Note is already shared to user. WhoShare: %d," +
                                " ToWhomShare: %d, NoteId: %d.", whoShare.getUser_id(), toWhomShare.getUser_id(), parentNoteId));
                        result[0] = "Пользователь уже добавен";
                        result[1] = "406";
                        return result;
                    }

                    note.setParent_user_id(parentUserId);
                    repository.duplicateNote(note); // note id will be updated
                    toWhomShare.setNote_id(note.getId());
                    repository.linkUserWithNote(toWhomShare.getUser_id(), toWhomShare.getNote_id());

                    Optional<UserDetailsImpl> user = userRepository.getUserByEmail(form.getUserEmail());

                    txManager.commit(status);
                    result[0] = "Успешно расшарено!";
                    result[1] = "200";
                    result[2] = user.get().getName();
                    return result;
                } else {
                    LOG.warn(String.format("Can't share not own note. UserId: %d, NoteId: %d.", whoShare.getUser_id(), parentNoteId));
                    result[0] = "Вы не можете удалить не свою заметку";
                    result[1] = "406";
                    return result;
                }

            } else {
                LOG.warn(String.format("Email(%s) is not found.", userDetails.getUsername()));
                result[0] = "Введенный email не найден";
                result[1] = "404";
                return result;
            }
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while sharing note. WhoShare: %d, ToWhomShare: %d, WhoShareNoteId: %d, NewNoteId: %d. Rolling back.",
                    whoShare.getUser_id(), toWhomShare.getUser_id(), whoShare.getNote_id(), note.getId()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}

            result[0] = "Возникла ошибка при шаринге заметки";
            result[1] = "404";
            return result;
        }
    }

    public void updateOrder(final OrderData orderData) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            if (orderData.getId_next() == 0L) {
                repository.updateOrder(orderData);
            } else if (orderData.getId_prev().equals(0L)) {
                repository.updateFirstElementOrder(orderData);
            } else {
                repository.updateOrder(orderData);
            }
            txManager.commit(status);
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while saving note order. PrevNoteId: %d, CurNoteId: %d, NextNoteId: %d." +
                    " Rolling back.", orderData.getId_prev(), orderData.getId_cur(), orderData.getId_next()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");} //if (status != null) {\if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}\nLOG.info("Rollback done.");\n}
            throw new ServiceException("An error occurred while saving note order." + e.getMessage());
        }
    }

    public UserDetailsImpl getUserWhoSharedNote(Long noteId) throws ServiceException {
        UserDetailsImpl user = null;
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            if (repository.isParentNoteIdExists(noteId) != null) {
                user = repository.getUserWhoSharedNote(noteId);
            } else {
                user = repository.getUserWhoOwnsNote(noteId);
            }
            txManager.commit(status);
            return user;
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while finding share user. NoteId: %d. Rolling back.", noteId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("Couldn't find user in db" + e.getMessage());
        }
    }

    public void deleteShareLink(Long rootNoteId, Long userId) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            HashMap<Long, ArrayList<Long>> map = new HashMap<>();
            List<Note> notes = repository.getNotesWithSameUuidById(rootNoteId);
            Long unsyncNote = repository.getNoteIdByUserIdParentId(userId, rootNoteId);
            List<Long> result = new ArrayList<Long>();

            for (Note note : notes) {
                if (map.containsKey(note.getParent_note_id())) {
                    map.get(note.getParent_note_id()).add(note.getId());
                } else {
                    ArrayList<Long> list = new ArrayList<>();
                    list.add(note.getId());
                    map.put(note.getParent_note_id(), list);
                }
            }

            map.get(rootNoteId).remove(unsyncNote);
            update(map, result, rootNoteId);

            Note updNote = new Note();
            updNote.setId(unsyncNote);
            //updNote.setText(root);
            updNote.setParent_note_id(null);

            repository.updateNote(updNote);
            repository.updateUuidByIds(result, Helper.generateUUID());
            txManager.commit(status);
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while deleting share link. UserId: %d, RootNoteId: %d. Rolling back.", userId, rootNoteId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while deleting share link.", e);
        }
    }

    private static void update(HashMap<Long, ArrayList<Long>> map, List<Long> result, Long root) {
        result.add(root);
        if (map.containsKey(root)) {
            for (Long id : map.get(root)) {
                update(map, result, id);
            }
        }
    }

    public List<UserDetailsImpl> getUsersWithSameNoteUuidById(Long noteId) throws ServiceException {
        List<UserDetailsImpl> users = null;
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            users = repository.getUsersWithSameNoteUuid(repository.getUuidById(noteId));
            txManager.commit(status);
            return users;
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while getting users by note uuid. NoteId: %d. Rolling back.", noteId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while getting users by note uuid" + e.getMessage());
        }
    }

    public List<NoteModel> getNotesWithSameNoteUuidByUserId(Long userId) throws ServiceException {
        List<NoteModel> notes = null;
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            notes = repository.getNotesWithSameNoteUuidByUserId(userId);
            txManager.commit(status);
            return notes;
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while getting notes with same uuid by userId. UserId: %d. Rolling back.", userId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while getting notes with same uuid by userId." + e.getMessage());
        }
    }

    public List<NoteModel> getAllSharedNoteModels(Long noteId) throws ServiceException {
        List<NoteModel> notes = null;
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            notes = repository.getAllSharedNoteModels(noteId);
            txManager.commit(status);
            return notes;
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while getting all shared notes. UserId: %d. Rolling back.", noteId));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while getting all shared notes." + e.getMessage());
        }
    }

    public Map<String, List<NoteModel>> getSortedMap(UserDetailsImpl currentUser) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            List<NoteModel> listNotes = getNotesWithSameNoteUuidByUserId(currentUser.getId());
            List<NoteModel> myNotes = new ArrayList<>();
            Map<String, Long> uuidIdMap = new HashMap<String, Long>();

            Iterator<NoteModel> it = listNotes.iterator();
            while (it.hasNext()) {
                NoteModel noteModel = it.next();
                if (noteModel.getEmailOfShareUser().equals(currentUser.getUsername())) {
                    uuidIdMap.put(noteModel.getUuid(), noteModel.getId());
                    noteModel.setEmailOfShareUser(null); // set email to null to place note in "My notes" section"

                    myNotes.add(noteModel); // Add note wih current user email to myNotes, remove this note from listNotes
                    it.remove();
                }
            }

            for (Map.Entry<String, Long> entry : uuidIdMap.entrySet()) {
                boolean isExists = listNotes.stream().filter(o -> o.getUuid().equals(entry.getKey())).findFirst().isPresent();
                if (isExists) {
                    myNotes.removeIf(o -> o.getUuid().equals(entry.getKey()));
                }
            }
            listNotes.addAll(myNotes);

            Collections.sort(listNotes, new NoteModel.NoteOrderDescComparator());
            //Collections.sort(myNotes, new NoteModel.UpdatedAtDescComparator());

            Map<String, List<NoteModel>> map = new HashMap<String, List<NoteModel>>();
            String avatar;
            for (NoteModel noteModel : listNotes) {
                noteModel.setId(uuidIdMap.get(noteModel.getUuid())); // change every noteId on own noteId
                List<NoteModel> list = map.get(noteModel.getEmailOfShareUser());
                if (list == null) {
                    list = new ArrayList<NoteModel>();

                    avatar = Helper.getAvatarUrl(noteModel.getEmailOfShareUser());
                    noteModel.setUserAvatar(avatar);
                    map.put(noteModel.getEmailOfShareUser(), list);
                }
                list.add(noteModel);
            }
            txManager.commit(status);
            return map;
        } catch (ServiceException e){
            LOG.error(String.format("An error occurred while getting sorted map of notes. UserId: %d, UserEmail: %d." +
                    " Rolling back.", currentUser.getId(), currentUser.getUsername()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while getting sorted map of notes." + e.getMessage());
        }
    }

    public List<UserDetailsImpl> getListOfShareUsers(Long noteId) throws  ServiceException {
        List<UserDetailsImpl> users = new ArrayList<UserDetailsImpl>();
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            List<UserDetailsImpl> shareUsers = getUsersWithSameNoteUuidById(noteId);

            UserDetailsImpl owner = getUserWhoSharedNote(noteId);

            if(owner == null) {
                throw new ServiceException("Invalid noteId.");
            }

            users.add(owner);
            users.get(0).setAvatar(Helper.getAvatarUrl(users.get(0).getUsername()));

            for (UserDetailsImpl u : shareUsers) {
                u.setAvatar(Helper.getAvatarUrl(u.getUsername()));
                if (!users.get(0).getUsername().equals(u.getUsername()))
                    users.add(u);
            }

            txManager.commit(status);
            return users;
        } catch (ServiceException e) {
            LOG.error(String.format("An error occurred while getting share users of note." +
                    " NoteId: %d. Cause: %s. Rolling back.", noteId, e.getMessage()));
            if (status != null) {txManager.rollback(status);LOG.info("Rollback done.");}
            throw new ServiceException("An error occurred while getting share users of note.", e);
        }
    }
}
