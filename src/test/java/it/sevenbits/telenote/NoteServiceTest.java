package it.sevenbits.telenote;

import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;

import it.sevenbits.telenote.core.repository.Note.INoteRepository;
import it.sevenbits.telenote.core.repository.User.IUserRepository;
import it.sevenbits.telenote.web.domain.forms.NoteForm;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import it.sevenbits.telenote.web.domain.models.ResponseMessage;
import it.sevenbits.telenote.web.domain.forms.ShareForm;

import it.sevenbits.telenote.service.NoteService;

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
//@ContextConfiguration(initializers = TestContextInitializer.class)
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

    private List<UserDetailsImpl> users = new ArrayList<>();

    private Long newNoteId;

    @Before
    public void create() throws Exception {
        user.setUsername("ok@ok.oke");
        user.setPassword("qwerty");
        user.setName("Leo");

        userRepository.create(user);

        assertNotNull(user.getId());
        assertTrue(user.getId().longValue() > 0);

        NoteForm form = new NoteForm(new Long(-1), "Блажен тот пастырь, кто во имя благосердия и доброй воли своей....");
        newNoteId = noteService.addNote(form, user.getId());
        //List<UserDetailsImpl> users = new ArrayList<>();

        //создал еще 3 пользователей
        for (int i = 0; i < 6; i++) {
            UserDetailsImpl user = new UserDetailsImpl();
            user.setUsername(RandomStringUtils.randomAlphabetic(4) + "@ok.oke");
            user.setPassword("qwerty");
            user.setName("Leo");

            userRepository.create(user);
            users.add(user);
        }
    }

    @Test
    public void deleteShareLinkTest() throws Exception {
        //расшарил с 0 пользователем заметку, парент юзер - глобальный юзер
        ShareForm share = new ShareForm(newNoteId, users.get(0).getUsername());
        ResponseEntity<ResponseMessage> message = noteService.shareNote(share, user.getId());
        //System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        //расшарил с 1 пользователем заметку, парент юзер - глобальный юзер
        share.setUserEmail(users.get(1).getUsername());
        message = noteService.shareNote(share, user.getId());
        //System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        List<Note> zeroUserNotes  = noteRep.findUserNotes(users.get(0).getId());
        assertTrue(zeroUserNotes.size() == 1);

        List<Note> firstUserNotes = noteRep.findUserNotes(users.get(1).getId());
        assertTrue(zeroUserNotes.size() == 1);

        //расшарил с 2 пользователем заметку 0 юзера, парент юзер - юзер номер 0
        share.setUserEmail(users.get(2).getUsername());
        share.setNoteId(zeroUserNotes.get(0).getId());
        message = noteService.shareNote(share, users.get(0).getId());
        //System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        //расшарил с 3 пользователем заметку, парент юзер - юзер номер 0
        share.setUserEmail(users.get(3).getUsername());
        message = noteService.shareNote(share, users.get(0).getId());
        //System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        //расшарил с 4 пользователем заметку, парент юзер - юзер номер 1
        share.setUserEmail(users.get(4).getUsername());
        share.setNoteId(firstUserNotes.get(0).getId());
        message = noteService.shareNote(share, users.get(1).getId());
        //System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        //расшарил с 5 пользователем заметку, парент юзер - юзер номер 1
        share.setUserEmail(users.get(5).getUsername());
        message = noteService.shareNote(share, users.get(1).getId());
        //System.out.println(message.getBody().getMessage());
        assertEquals(message.getStatusCode(), HttpStatus.OK);

        List<NoteModel> notes = noteService.findUserNotes(user.getId());
        assertTrue(notes.size() == 1);

        noteService.deleteShareLink(notes.get(0).getId(), users.get(0).getId());

        List<Note> globalUserNotes = noteRep.findUserNotes(user.getId());
        assertTrue(globalUserNotes.size() == 1);
        zeroUserNotes = noteRep.findUserNotes(users.get(0).getId());
        assertTrue(zeroUserNotes.size() == 1);
        firstUserNotes = noteRep.findUserNotes(users.get(1).getId());
        assertTrue(firstUserNotes .size() == 1);
        List<Note> secondUserNotes = noteRep.findUserNotes(users.get(2).getId());
        assertTrue(secondUserNotes.size() == 1);

        assertNotEquals(zeroUserNotes.get(0).getUuid(), globalUserNotes.get(0).getUuid());
        assertNotEquals(globalUserNotes.get(0).getUuid(), secondUserNotes.get(0).getUuid());
        assertEquals(zeroUserNotes.get(0).getUuid(), secondUserNotes.get(0).getUuid());
        assertEquals(globalUserNotes.get(0).getUuid(), firstUserNotes.get(0).getUuid());

        assertNull(zeroUserNotes.get(0).getParent_note_id());
    }

    @After
    public void destroy() throws Exception {
        userRepository.emptyBD();
    }
}
