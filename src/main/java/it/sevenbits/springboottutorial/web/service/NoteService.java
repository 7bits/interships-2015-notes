package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import it.sevenbits.springboottutorial.web.domain.NoteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public void updateNote(final NoteForm form) throws ServiceException {
        final Note note = new Note();
        note.setId(form.getId());
        note.setText(form.getText());
        try {
            repository.updateNote(note);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public void deleteNote(final Note note) throws ServiceException {

        try {
            repository.deleteNote(note);
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

    public void addNote(final NoteForm form, Long user_id) throws ServiceException {

        Note note = new Note();
        note.setText(form.getText());
        try {
            repository.addNote(note);

            UserNote userNote = new UserNote(user_id, note.getId());
            repository.linkUserWithNote(userNote);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while adding note: " + e.getMessage());
        }
    }
}
