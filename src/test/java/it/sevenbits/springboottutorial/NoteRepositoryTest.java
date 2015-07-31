package it.sevenbits.springboottutorial;


import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import org.junit.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class NoteRepositoryTest {

    @Autowired
    @Qualifier(value = "noteRepository")
    public INoteRepository noteRep;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository userRep;

    private Note note = new Note();
    private UserDetailsImpl user = new UserDetailsImpl();

    @Before
    public void create() throws Exception {
        note.setText("Путь праведника труден, ибо препятствуют ему самолюбивые и тираны, из злых людей.");
        note.setUuid(note.generateUUID());
        noteRep.addFirstNote(note);

        assertNotNull(note.getId());
        assertTrue(note.getId().longValue() > 0);

        user.setEmail("ok@ok.oke");
        user.setPassword("qwerty");
        user.setUsername("Leo");

        userRep.create(user);

        noteRep.linkUserWithNote(note.getId(), user.getId());
    }

    @After
    public void remove() throws Exception {
        noteRep.deleteNote(note);
        userRep.remove(user);
    }

    @Test
    public void updateTest() throws Exception {
        String text = note.getText();
        note.setText("Отче наш, сущий на небесах, да святится имя твое, да придет царство твое, да будет воля твоя.");

        noteRep.updateNote(note);

        List<Note> list = noteRep.findUserNotes(user.getId());

        assertFalse(list.isEmpty());
        assertTrue(list.get(0).getText().equals(note.getText()));
    }

    @Test
    public void duplicateNoteTest() throws Exception {
        Note tNote = new Note();
        tNote.setId(note.getId());
        tNote.setText(note.getText());
        tNote.setCreated_at(note.getCreated_at());
        tNote.setNote_date(note.getUpdated_at());
        tNote.setUpdated_at(note.getNote_date());

        noteRep.duplicateNote(note);

        noteRep.linkUserWithNote(note.getId(), user.getId());

        List<Note> list = noteRep.findUserNotes(user.getId());
        assertEquals(2, list.size());

        noteRep.deleteNote(note);
        note = tNote;
    }

    @Test
    public void isNoteBelongToUserTest() throws Exception {
        assertTrue(noteRep.isNoteBelongToUser(note.getId(), user.getId()));
    }

    @Test
    public void getUserWhoSharedNoteTest() throws Exception {
        Note tNote = new Note();
        tNote.setId(note.getId());
        tNote.setText(note.getText());
        tNote.setCreated_at(note.getCreated_at());
        tNote.setNote_date(note.getUpdated_at());
        tNote.setUpdated_at(note.getNote_date());

        noteRep.duplicateNote(note);

        UserDetailsImpl tuser = noteRep.getUserWhoSharedNote(note.getId());
        assertNotNull(tuser);
        assertEquals(tuser.getEmail(), user.getEmail());

        noteRep.deleteNote(note);
        note = tNote;
    }

    @Test
    public void getUuidByIdTest() throws Exception {
        String uuid = noteRep.getUuidById(note.getId());

        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
    }

    @Test
    public void updateNotesByUuidTest() throws Exception {
        String uuid = noteRep.getUuidById(note.getId());

        note.setText("Ololo");
        noteRep.updateNotesByUuid(note);
    }

    @Test
    public void isNoteAlreadySharedTest() throws Exception {
        UserNote link = new UserNote();
        link.setNote_id(note.getId());
        link.setUser_id(user.getId());

        assertTrue(noteRep.isNoteAlreadyShared(link));
    }

    @Test
    public void updateOrderTest() throws Exception {

    }

    @Test
    public void updateFirstElementOrderTest() throws Exception {

    }
}
