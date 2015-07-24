package it.sevenbits.springboottutorial;


import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import org.junit.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class NoteRepositoryTest {

    @Autowired
    @Qualifier(value = "noteRepository")
    public INoteRepository repository;

    private Note note = new Note();

    //private UserDetailsImpl user = new UserDetailsImpl();

    @Before
    public void create() throws Exception {
        note.setText("Путь праведника труден, ибо препятствуют ему самолюбивые и тираны, из злых людей.");
        repository.addNote(note);

        assertNotNull(note.getId());
        assertTrue(note.getId().longValue() > 0);
    }

    @Test
    public void update() throws Exception {
        note.setText("Отче наш, сущий на небесах, да святится имя твое, да придет царство твое, да будет воля твоя.");

        repository.updateNote(note);
    }

    @After
    public void remove() throws Exception {
        repository.deleteNote(note);
    }
}
