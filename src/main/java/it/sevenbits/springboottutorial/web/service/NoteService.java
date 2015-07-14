package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by Admin on 13.07.2015.
 */
public class NoteService {
    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    private INoteRepository repository;

    public void saveNote(final NoteForm form) throws ServiceException {
        final Note note = new Note();
        //note.getUserId(); // где будем хранить ID пользовотеля,который создал заметку?
        note.setCategory(form.getCategory());
        note.setPriority(form.getPriority());
        note.setDate(form.getDate());
        note.setState(form.getState());
        note.setSubnote(form.getSubnote());
        try {
            repository.saveNote(note);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public void updateNote(final NoteForm form) throws ServiceException {
        final Note note = new Note();
        //note.getUserId(); // где будем хранить ID пользовотеля,который создал заметку?
        note.setCategory(form.getCategory());
        note.setPriority(form.getPriority());
        note.setDate(form.getDate());
        note.setState(form.getState());
        note.setSubnote(form.getSubnote());
        try {
            repository.updateNote(note);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }

    public void deleteNote(final NoteForm form) throws ServiceException {
        final Note note = new Note();
        //note.getUserId(); // где будем хранить ID пользовотеля,который создал заметку?
        try {
            repository.deleteNote(note);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving note: " + e.getMessage(), e);
        }
    }
}
