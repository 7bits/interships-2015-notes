package it.sevenbits.springboottutorial;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@FlywayTest
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
        note.setUuid(Note.generateUUID());
        noteRep.addFirstNote(note);

        assertNotNull(note.getId());
        assertTrue(note.getId().longValue() > 0);

        user.setEmail("ok@ok.oke");
        user.setPassword("qwerty");
        user.setName("Leo");

        userRep.create(user);
        assertNotNull(note.getId());
        assertTrue(note.getId().longValue() > 0);

        noteRep.linkUserWithNote(user.getId(), note.getId());
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

        noteRep.updateNotesByUuid(note);


        List<Note> list = noteRep.findUserNotes(user.getId());

        assertFalse(list.isEmpty());
        assertFalse(list.get(0).getText().equals(text));
        int i = 1;
        assertTrue(i == 1);
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

        noteRep.linkUserWithNote(user.getId(), note.getId());

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
        String test = "Ololo";

        note.setText(test);
        noteRep.updateNotesByUuid(note);

        List<Note> notes = noteRep.findUserNotes(user.getId());

        assertFalse(notes.isEmpty());
        assertTrue(notes.get(0).getText().equals(test));
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

    @Test
    public void getNotesWithSameUuidByIdTest() throws Exception {
        List<Note> list = noteRep.getNotesWithSameUuidById(note.getId());

        assertFalse(list.isEmpty());
        assertTrue(list.size() == 1);
    }

    @Test
    public void updateUuidByIdTest() throws Exception {
        Note tnote = new Note();
        tnote.setText("Путь праведника труден, ибо препятствуют ему самолюбивые и тираны, из злых людей.");
        tnote.setUuid(Note.generateUUID());
        noteRep.addNote(tnote);

        assertNotNull(tnote.getId());
        assertTrue(tnote.getId().longValue() > 0);

        ArrayList<Long> notes = new ArrayList<>();
        notes.add(tnote.getId());
        notes.add(note.getId());

        noteRep.updateUuidByIds(notes, Note.generateUUID());

        List<Note> list = noteRep.getNotesWithSameUuidById(note.getId());

        assertFalse(list.isEmpty());
        assertTrue(list.size() == 2);

        noteRep.deleteNote(tnote);
    }

    @Test
    public void getUserNoteByParentIdTest() throws Exception {
        UserDetailsImpl tuser = new UserDetailsImpl();
        tuser.setEmail("123olo@ok.oke");
        tuser.setPassword("qwerty");
        tuser.setName("Leo");

        userRep.create(tuser);

        Note tNote = new Note();
        tNote.setId(note.getId());
        tNote.setText(note.getText());
        tNote.setCreated_at(note.getCreated_at());
        tNote.setNote_date(note.getUpdated_at());
        tNote.setUpdated_at(note.getNote_date());
        noteRep.duplicateNote(note);

        noteRep.linkUserWithNote(tuser.getId(), note.getId());

        Long id = noteRep.getNoteIdByUserIdParentId(tuser.getId(), tNote.getId());
        assertNotNull(id);
        assertTrue(id > 0);

        noteRep.deleteNote(note);

        note = tNote;

        userRep.remove(tuser);
    }
}
