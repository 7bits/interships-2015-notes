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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Admin on 13.07.2015.
 */
@Service
public class NoteService {
    @Autowired
    @Qualifier(value = "noteRepository")
    private INoteRepository repository;

    @Autowired
    @Qualifier(value = "userRepository")
    private IUserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public void updateNote(final NoteForm form, Long user_id) throws ServiceException {
        final Note note = new Note();
        note.setId(form.getId());
        note.setText(form.getText());

        try {
            if (repository.isNoteBelongToUser(note.getId(), user_id)) {
                note.setUuid(repository.getUuidById(note.getId()));
                repository.updateNotesByUuid(note);
                //repository.updateNote(note);
            } else {
                throw new ServiceException("Current note is not belong to user!");
            }
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public void deleteNote(final Note note, Long userId) throws ServiceException {
        try {
            UserNote userNote = new UserNote(userId, note.getId());

            if (repository.isNoteBelongToUser(note.getId(), userId)) {
                repository.deleteNote(note);
                repository.resetAllParentNoteUserId(note.getId());
            } else {
                throw new ServiceException("Current note is not belong to user!");
            }
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public List<NoteModel> findUserNotes(final Long userId) throws ServiceException {

        try {
            List<Note> notes = repository.findUserNotes(userId);
            List<NoteModel> models = new ArrayList<>(notes.size());
            for (Note n : notes) {
                models.add(new NoteModel(n.getId(), n.getText(), n.getNote_date(), n.getCreated_at(), n.getUpdated_at(),
                        n.getParent_note_id(), n.getParent_user_id(), n.getUuid(), n.getNote_order()));
            }
            return models;
        } catch (Exception e) {
            throw new ServiceException("An error occurred while finding user notes: " + e.getMessage());
        }
    }

    public List<UserDetailsImpl> findShareUsers(final Long noteId) throws ServiceException {
        try {
            return repository.findShareUsers(noteId);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while finding share users: " + e.getMessage());
        }
    }

    public Long addNote(final NoteForm form, Long user_id) throws ServiceException {
        Note note = new Note();
        note.setText(form.getText());
        note.setUuid(Note.generateUUID());
        note.setParent_user_id(user_id);
        try {

            if (repository.findUserNotes(user_id).size() == 0) {
                repository.addFirstNote(note);
            } else {
                repository.addNote(note);
            }

            UserNote userNote = new UserNote(user_id, note.getId());
            repository.linkUserWithNote(user_id, note.getId());
            return note.getId();
        } catch (Exception e) {
            throw new ServiceException("An error occurred while adding note: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseMessage> shareNote(final ShareForm form, Long parentUserId) throws RepositoryException, ServiceException {
        Long parentNoteId = form.getNoteId();
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(form.getUserEmail());

        final Note note = new Note();
        note.setId(form.getNoteId());

        final UserNote whoShare = new UserNote();
        whoShare.setNote_id(form.getNoteId());

        final UserNote toWhomShare = new UserNote();

        try {
            if (userRepository.isEmailExists(userDetails)) {
                whoShare.setUser_id(parentUserId);
                toWhomShare.setUser_id(userRepository.getIdByEmail(userDetails));

                if (whoShare.getUser_id() == toWhomShare.getUser_id())
                    return new ResponseEntity<>(new ResponseMessage(false, "Это ваша заметка"), HttpStatus.NOT_ACCEPTABLE);

                if (repository.isNoteBelongToUser(parentNoteId, parentUserId)) {
                    final UserNote curNoteIdNextUser = new UserNote();
                    curNoteIdNextUser.setNote_id(whoShare.getNote_id());
                    curNoteIdNextUser.setUser_id(toWhomShare.getUser_id());

                    if (repository.isNoteAlreadyShared(curNoteIdNextUser)) {
                        return new ResponseEntity<>(new ResponseMessage(false, "Пользователь уже добавен"), HttpStatus.NOT_ACCEPTABLE);
                    }

                    note.setParent_user_id(parentUserId);
                    repository.duplicateNote(note); // note id will be updated
                    toWhomShare.setNote_id(note.getId());
                    repository.linkUserWithNote(toWhomShare.getUser_id(), toWhomShare.getNote_id());
                } else {
                    return new ResponseEntity<>(new ResponseMessage(false, "Вы не можете удалить не свою заметку!"), HttpStatus.NOT_ACCEPTABLE);
                }

            } else {
                return new ResponseEntity<>(new ResponseMessage(false, "Введенный email не найден!"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage(false, "Возникла ошибка при шаринге заметки: " + e.getMessage()), HttpStatus.NOT_FOUND);
        }

        Optional<UserDetailsImpl> user = userRepository.getUserByEmail(form.getUserEmail());

        String avatar = Helper.getAvatarUrl(user.get().getUsername());
        user.get().setAvatar(avatar);

        return new ResponseEntity<>(new ResponseMessage(true, "Успешно расшарено!", user.get()), HttpStatus.OK);
    }

    public void updateOrder(final OrderData orderData) throws ServiceException {

        try {
            if (orderData.getId_next() == 0L) {
                repository.updateOrder(orderData);
            } else if (orderData.getId_prev().equals(0L)) {
                repository.updateFirstElementOrder(orderData);
            } else {
                repository.updateOrder(orderData);
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось сохранить порядок заметок" + e.getMessage());
        }
    }

    public UserDetailsImpl getUserWhoSharedNote(Long noteId) throws ServiceException {
        try {
            if (repository.isParentNoteIdExists(noteId) != null) {
                return repository.getUserWhoSharedNote(noteId);
            } else {
                return repository.getUserWhoOwnsNote(noteId);
            }

        } catch (RepositoryException e) {
            throw new ServiceException("не удалось найти пользователя в базе" + e.getMessage());
        }
    }

    public void deleteShareLink(Long root, Long userId) throws ServiceException {
        try {
            HashMap<Long, ArrayList<Long>> map = new HashMap<>();
            List<Note> notes = repository.getNotesWithSameUuidById(root);
            Long unsyncNote = repository.getNoteIdByUserIdParentId(userId, root);
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

            map.get(root).remove(unsyncNote);
            update(map, result, root);

            Note updNote = new Note();
            updNote.setId(unsyncNote);
            //updNote.setText(root);
            updNote.setParent_note_id(null);

            repository.updateNote(updNote);
            repository.updateUuidByIds(result, Note.generateUUID());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
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
        try {
            return repository.getUsersWithSameNoteUuid(repository.getUuidById(noteId));
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось получить пользователей по uuid заметки" + e.getMessage());
        }
    }

    public List<NoteModel> getNotesWithSameNoteUuidByUserId(Long userId) throws ServiceException {
        try {
            return repository.getNotesWithSameNoteUuidByUserId(userId);
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось получить заметки пользователей с неуникальным uuid" + e.getMessage());
        }
    }

    public List<NoteModel> getAllSharedNoteModels(Long userId) throws ServiceException {
        try {
            return repository.getAllSharedNoteModels(userId);
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось получить чужие расшаренные заметки" + e.getMessage());
        }
    }

    public Map<String, List<NoteModel>> getSortedMap(UserDetailsImpl currentUser) throws ServiceException {
        List<NoteModel> listNotes = getNotesWithSameNoteUuidByUserId(currentUser.getId());
        List<NoteModel> myNotes = new ArrayList<>();

        Map<String, Long> uuidIdMap= new HashMap<String, Long>();

        Iterator<NoteModel> it  = listNotes.iterator();
        while (it.hasNext()) {
            NoteModel noteModel = it.next();

            if(noteModel.getEmailOfShareUser().equals(currentUser.getUsername())) {
                uuidIdMap.put(noteModel.getUuid(), noteModel.getId());
                noteModel.setEmailOfShareUser(null); // зануляем свой имейл, чтобы поместить в "Мои заметки"

                myNotes.add(noteModel); // добавляемм заметку в наш myNotes, удаляем ее из общего листа
                it.remove();
            }
        }

        for (Map.Entry<String, Long> entry : uuidIdMap.entrySet()) {
            boolean isExists = listNotes.stream().filter(o -> o.getUuid().equals(entry.getKey())).findFirst().isPresent();

            if(isExists) {
                myNotes.removeIf(o -> o.getUuid().equals(entry.getKey()));
            }
        }
        listNotes.addAll(myNotes);

        Collections.sort(listNotes, new NoteModel.NoteOrderDescComparator());
        //Collections.sort(myNotes, new NoteModel.UpdatedAtDescComparator());

        Map<String, List<NoteModel>> map = new HashMap<String, List<NoteModel>>();
        String avatar;
        for (NoteModel noteModel : listNotes) {
            noteModel.setId(uuidIdMap.get(noteModel.getUuid())); // меняем id любой заметки на свой

            List<NoteModel> list = map.get(noteModel.getEmailOfShareUser());
            if (list == null) {
                list = new ArrayList<NoteModel>();

                avatar = Helper.getAvatarUrl(noteModel.getEmailOfShareUser());
                noteModel.setUserAvatar(avatar);
                map.put(noteModel.getEmailOfShareUser(), list);
            }
            list.add(noteModel);
        }
        return map;
    }

    public List<UserDetailsImpl> checkSharedNote(Long noteId) throws  ServiceException {
        List<UserDetailsImpl> shareUsers = getUsersWithSameNoteUuidById(noteId);

        List<UserDetailsImpl> users = new ArrayList<UserDetailsImpl>();
        users.add(getUserWhoSharedNote(noteId));
        users.get(0).setAvatar(Helper.getAvatarUrl(users.get(0).getUsername()));

        for (UserDetailsImpl u : shareUsers) {
            u.setAvatar(Helper.getAvatarUrl(u.getUsername()));
            if (!users.get(0).getUsername().equals(u.getUsername())) users.add(u);
        }

        return users;
    }
}
