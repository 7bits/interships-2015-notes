package it.sevenbits.springboottutorial;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;

import it.sevenbits.springboottutorial.core.repository.Note.INoteRepository;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import it.sevenbits.springboottutorial.web.domain.NoteModel;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import it.sevenbits.springboottutorial.web.domain.ShareForm;
import it.sevenbits.springboottutorial.web.service.NoteService;

import org.junit.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class NoteServiceTest {

    @Autowired
    public NoteService noteService;

    private UserDetailsImpl user = new UserDetailsImpl();

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository userRepository;

    @Autowired
    @Qualifier(value = "noteRepository")
    public INoteRepository noteRep;

    @Before
    public void create() throws Exception {
        user.setEmail("ok@ok.oke");
        user.setPassword("qwerty");
        user.setUsername("Leo");

        userRepository.create(user);

        assertNotNull(user.getId());
        assertTrue(user.getId().longValue() > 0);
    }

    @Test
    public void deleteShareLinkTest() throws Exception {
        List<UserDetailsImpl> users = new ArrayList<>();
        Long newNoteId;
        //создал заметку, для пользователя
        NoteForm form = new NoteForm(new Long(-1), "Блажен тот пастырь, кто во имя благосердия и доброй воли своей....");
        newNoteId = noteService.addNote(form, user.getId());
        //List<UserDetailsImpl> users = new ArrayList<>();

        //создал еще 3 пользователей
        for (int i = 0; i < 4; i++) {
            UserDetailsImpl user = new UserDetailsImpl();
            user.setEmail(RandomStringUtils.randomAlphabetic(4) + "@ok.oke");
            user.setPassword("qwerty");
            user.setUsername("Leo");

            userRepository.create(user);
            users.add(user);
        }

        //расшарил с 0 пользователем заметку, парент юзер - глобальный юзер
        ShareForm share = new ShareForm(newNoteId, users.get(0).getEmail());
        ResponseEntity<ResponseMessage> message = noteService.shareNote(share, user.getId());
        System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);
        //расшарил с 1 пользователем заметку, парент юзер - глобальный юзер
        share.setUserEmail(users.get(1).getEmail());
        message = noteService.shareNote(share, user.getId());
        System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        List<Note> zeroUserNotes  = noteRep.findUserNotes(users.get(0).getId());
        assertTrue(zeroUserNotes.size() == 1);

        //расшарил с 2 пользователем заметку 0 юзера, парент юзер - юзер номер 0
        share.setUserEmail(users.get(2).getEmail());
        share.setNoteId(zeroUserNotes.get(0).getId());
        message = noteService.shareNote(share, users.get(0).getId());
        System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        //расшарил с 3 пользователем заметку, парент юзер - юзер номер 0
        share.setUserEmail(users.get(3).getEmail());
        message = noteService.shareNote(share, users.get(0).getId());
        System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        List<NoteModel> notes = noteService.findUserNotes(users.get(0).getId());
        assertTrue(notes.size() == 1);
        System.out.println("zero user email = " + users.get(0).getEmail());

        noteService.deleteShareLink(notes.get(0));

        List<Note> globalUserNotes = noteRep.findUserNotes(user.getId());
        assertTrue(globalUserNotes.size() == 1);
        zeroUserNotes = noteRep.findUserNotes(users.get(0).getId());
        assertTrue(zeroUserNotes.size() == 1);

        assertNotEquals(zeroUserNotes.get(0).getUuid(), globalUserNotes.get(0).getUuid());
        assertNull(zeroUserNotes.get(0).getParent_note_id());

        for (UserDetailsImpl user : users) {
            userRepository.remove(user);
        }
    }

    @After
    public void destroy() throws Exception {
        userRepository.remove(user);
    }
}
