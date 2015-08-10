package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import it.sevenbits.springboottutorial.web.domain.NoteModel;
import it.sevenbits.springboottutorial.web.domain.ShareForm;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import org.apache.catalina.User;
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
    @Qualifier(value = "theUserPersistRepository")
    private IUserRepository userRepository;



  /*  public void saveNote(final NoteForm form) throws ServiceException {
        final Note note = new Note();
        //note.getUserId(); // где будем хранить ID пользовотеля,который создал заметку?
        /*note.setCategory(form.getCategory());
        note.setPriority(form.getPriority());
        note.setNote_date(form.getNote_date());
        /*note.setState(form.getState());
        note.setSubnote(form.getSubnote());
        try {
            repository.saveNote(note);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }*/

    public void updateNote(final NoteForm form, Long user_id) throws ServiceException {
        final Note note = new Note();
        note.setId(form.getId());
        note.setText(form.getText());

        UserNote userNote = new UserNote(user_id, note.getId());
        try {
            if(repository.isNoteBelongToUser(note.getId(), user_id)) {
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

            if(repository.isNoteBelongToUser(note.getId(), userId))
                repository.deleteNote(note);
            else
                throw new ServiceException("Current note is not belong to user!");
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public List<NoteModel> findUserNotes(final Long userId) throws ServiceException{

        try {
            List<Note> notes = repository.findUserNotes(userId);
            List<NoteModel> models = new ArrayList<>(notes.size());
            for (Note n : notes) {
                    models.add(new NoteModel(n.getId(), n.getText(), n.getNote_date(), n.getCreated_at(), n.getUpdated_at()));
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
        userDetails.setEmail(form.getUserEmail());

        final Note note = new Note();
        note.setId(form.getNoteId());

        final UserNote whoShare = new UserNote();
        whoShare.setNote_id(form.getNoteId());

        final UserNote toWhomShare = new UserNote();

        try {
            if (userRepository.isEmailExists(userDetails)) {
                whoShare.setUser_id(parentUserId);
                toWhomShare.setUser_id(userRepository.getIdByEmail(userDetails));

                if(whoShare.getUser_id() == toWhomShare.getUser_id())
                    return new ResponseEntity<>(new ResponseMessage(false, "Это ваша заметка"), HttpStatus.NOT_ACCEPTABLE);

                if(repository.isNoteBelongToUser(parentNoteId, parentUserId)) {
                    final UserNote curNoteIdNextUser = new UserNote();
                    curNoteIdNextUser.setNote_id(whoShare.getNote_id());
                    curNoteIdNextUser.setUser_id(toWhomShare.getUser_id());

                    if(repository.isNoteAlreadyShared(curNoteIdNextUser)) {
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
        String username = user.get().getUsername();

        return new ResponseEntity<>(new ResponseMessage(true, "Успешно расшарено!", username), HttpStatus.OK);
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

    public List<Note> getNotesByUserIdList(List<Long> shareUserIds, Long parentUserId, boolean showMyNotes) throws ServiceException {
        try {
            return repository.getNotesByUserIdList(shareUserIds, parentUserId, showMyNotes);
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось получить расшаренные заметки" + e.getMessage());
        }
    }

    public UserDetailsImpl getUserWhoSharedNote(Long noteId) throws ServiceException {
        try {
            if (repository.isParentNoteIdExists(noteId) != null){
                return repository.getUserWhoSharedNote(noteId);
            } else {
                return repository.getUserWhoOwnNote(noteId);
            }

        } catch (RepositoryException e) {
            throw new ServiceException("не удалось найти пользователя в базе" + e.getMessage());
        }
    }

    public String getUserStyle(Long userId) throws ServiceException {
        try {
            return repository.getUserStyle(userId);
        } catch (RepositoryException e) {
            throw new ServiceException("Ошибка чтения стиля" + e.getMessage());
        }
    }

    public void deleteShareLink(Long root, Long userId) throws ServiceException {
        /*try {
            HashMap<Long, ArrayList<Long>> map = new HashMap<>();
            List<Note> notes = repository.getNotesWithSameUuidById(root.getId());
            List<Long> result = new ArrayList<>();
            Stack<Long> stack = new Stack<>();

            for (Note note : notes) {
                if (map.containsKey(note.getParent_note_id())) {
                    map.get(note.getParent_note_id()).add(note.getId());
                } else {
                    ArrayList<Long> list = new ArrayList<>();
                    list.add(note.getId());

                    map.put(note.getParent_note_id(), list);
                }
            }

            stack.push(root.getId());
            while (!stack.isEmpty()) {
                Long key = stack.pop();
                List<Long> list = map.get(key);
                result.add(key);
                if (list != null && !list.isEmpty()) {
                    for (Long item : list) {
                        stack.push(item);
                    }
                }
            }

            Note updNote = new Note();
            updNote.setId(root.getId());
            updNote.setText(root.getText());
            updNote.setParent_note_id(null);

            repository.updateNote(updNote);
            repository.updateUuidById(result, Note.generateUUID());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }*/

        try {
            HashMap<Long, ArrayList<Long>> map = new HashMap<>();
            List<Note> notes = repository.getNotesWithSameUuidById(root);
            Long unsyncNote = repository.getUserNoteByParentId(userId, root);
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
            repository.updateUuidById(result, Note.generateUUID());
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private static void update(HashMap<Long, ArrayList<Long>> map, List<Long> result, Long root) {
        result.add(root);
        if (map.containsKey(root)) {
            for (Long id: map.get(root)) {
                update(map, result, id);
            }
        }
    }
}
