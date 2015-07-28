package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.core.repository.Note.NoteRepository;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import it.sevenbits.springboottutorial.web.domain.NoteModel;
import it.sevenbits.springboottutorial.web.domain.ShareForm;
import it.sevenbits.springboottutorial.web.domain.ShareResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            if(repository.isNoteBelongToUser(userNote))
                repository.updateNote(note);
            else
                throw new ServiceException("Current note is not belong to user!");
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public void deleteNote(final Note note, Long user_id) throws ServiceException {
        try {
            UserNote userNote = new UserNote(user_id, note.getId());

            if(repository.isNoteBelongToUser(userNote))
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

    public Long addNote(final NoteForm form, Long user_id) throws ServiceException {

        Note note = new Note();
        note.setText(form.getText());
        try {
            repository.addNote(note);

            UserNote userNote = new UserNote(user_id, note.getId());
            repository.linkUserWithNote(userNote);
            return note.getId();
        } catch (Exception e) {
            throw new ServiceException("An error occurred while adding note: " + e.getMessage());
        }
    }
    public ResponseEntity<ShareResponse> shareNote(final ShareForm form, Long user_id) throws RepositoryException, ServiceException {
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getUserEmail());

        final Note note = new Note();
        note.setId(form.getNoteId());

        final UserNote whoShare = new UserNote();
        whoShare.setNote_id(form.getNoteId());

        final UserNote toWhomShare = new UserNote();

        try {
            if (userRepository.isEmailExists(userDetails)) {
                whoShare.setUser_id(user_id);
                toWhomShare.setUser_id(userRepository.getIdByEmail(userDetails));

                if(whoShare.getUser_id() == toWhomShare.getUser_id())
                    return new ResponseEntity<>(new ShareResponse(false, "Вы не можете расшарить себе заметку!"), HttpStatus.NOT_ACCEPTABLE);

                if(repository.isNoteBelongToUser(whoShare)) {
                    repository.duplicateNote(note); // note id will be updated
                    toWhomShare.setNote_id(note.getId());
                    repository.linkUserWithNote(toWhomShare);
                } else {
                    return new ResponseEntity<>(new ShareResponse(false, "Вы не можете удалить не свою заметку!"), HttpStatus.NOT_ACCEPTABLE);
                }

            } else {
                return new ResponseEntity<>(new ShareResponse(false, "Введенный email не найден!"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ShareResponse(false, "Возникла ошибка при шаринге заметки: " + e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ShareResponse(true, "Успешно расшарено!"), HttpStatus.OK);
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
}
