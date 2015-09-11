package it.sevenbits.telenote.web.controllers;


import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.web.domain.forms.NoteForm;
import it.sevenbits.telenote.web.domain.forms.ShareForm;
import it.sevenbits.telenote.web.domain.forms.UserCreateForm;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import it.sevenbits.telenote.web.domain.models.NoteSocketCommand;
import it.sevenbits.telenote.web.domain.models.ResponseMessage;
import it.sevenbits.telenote.web.service.AccountService;
import it.sevenbits.telenote.web.service.NoteService;
import it.sevenbits.telenote.web.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.servlet.ModelAndView;
//import it.sevenbits.telenote.web.domain.forms.UserCreateForm;
//import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private AccountService accService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static Logger LOG = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("home/welcome");
        //model.addObject("signinForm", new UserLoginForm());
        model.addObject("signupForm", new UserCreateForm());

        // TODO: need to move to utils
        Object error = request.getSession().getAttribute("ERROR");
        Object name = request.getSession().getAttribute("USER_NAME");
        Boolean is_error = Boolean.FALSE;
        String username = "";
        if (error != null) {
            is_error = (Boolean)error;
            request.getSession().removeAttribute("ERROR");
        }
        if (name != null) {
            username = (String)name;
            request.getSession().removeAttribute("USER_NAME");
        }
        // TODO: end(need to move to utils)

        if (is_error) {
            model.addObject("error", is_error);
            model.addObject("username", username);
        }

        return model;
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.GET)
    public String getTelenote(final Model model, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        List<NoteModel> listNotes = noteService.getNotesWithSameNoteUuidByUserId(currentUser.getId());
        List<NoteModel> myNotes = new ArrayList<>();

        Map<String, Long> uuidIdMap= new HashMap<String, Long>();

        Iterator<NoteModel> it  = listNotes.iterator();
        while (it.hasNext()) {
            NoteModel noteModel = it.next();

            if(noteModel.getEmailOfShareUser().equals(currentUser.getEmail())) {
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

        String avatar;
        listNotes.addAll(myNotes);

        Collections.sort(listNotes, new NoteModel.NoteOrderDescComparator());
        //Collections.sort(myNotes, new NoteModel.UpdatedAtDescComparator());

        Map<String, List<NoteModel>> map = new HashMap<String, List<NoteModel>>();
        for (NoteModel noteModel : listNotes) {
            noteModel.setId(uuidIdMap.get(noteModel.getUuid())); // меняем id любой заметки на свой

            List<NoteModel> list = map.get(noteModel.getEmailOfShareUser());
            if (list == null) {
                list = new ArrayList<NoteModel>();

                avatar = accService.getAvatarHash(noteModel.getEmailOfShareUser());
                noteModel.setUserAvatar(avatar);
                map.put(noteModel.getEmailOfShareUser(), list);
            }
            list.add(noteModel);
        }

        //Map<String, List<NoteModel>> treeMap = new TreeMap<String, List<NoteModel>>(map);
        model.addAttribute("user", currentUser);
        model.addAttribute("noteSections", map);
        model.addAttribute("avatar", accService.getAvatarHash(currentUser.getEmail()));

        return "home/telenote";
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.POST)
    public @ResponseBody
    Long editNote (HttpServletRequest request, HttpServletResponse response, Authentication auth) throws ServiceException{
        Long id = Long.parseLong(request.getParameter("id"));
        String text = request.getParameter("text");
        NoteForm form = new NoteForm(id, text);

        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        if (id < 0) {
            return noteService.addNote(form, currentUser.getId());
        } else {
            noteService.updateNote(form, currentUser.getId());
        }

        return form.getId();
    }

    @RequestMapping(value = "/telenote/checknote", method = RequestMethod.POST)
    public @ResponseBody List<UserDetailsImpl> checkSharedNote(HttpServletRequest request, HttpServletResponse response) throws ServiceException {

        Long noteId = Long.parseLong(request.getParameter("id"));
        List<UserDetailsImpl> shareUsers = noteService.getUsersWithSameNoteUuidById(noteId);

        List<UserDetailsImpl> users = new ArrayList<UserDetailsImpl>();
        users.add(noteService.getUserWhoSharedNote(noteId));
        users.get(0).setAvatar(accService.getAvatarHash(users.get(0).getEmail()));

        for (UserDetailsImpl u : shareUsers) {
            u.setAvatar(accService.getAvatarHash(u.getEmail()));
            if (!users.get(0).getEmail().equals(u.getEmail())) users.add(u);
        }

        return users;
    }


    @RequestMapping(value = "/telenote/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteNote(@PathVariable("id") Long noteId, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Note note = new Note();
        note.setId(noteId);

        noteService.deleteNote(note, currentUser.getId());
    }

    @RequestMapping(value = "/telenote/share", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ResponseMessage> shareNote (HttpServletRequest request, HttpServletResponse response, Authentication auth) throws RepositoryException, ServiceException{
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Long noteId = Long.parseLong(request.getParameter("id"));
        String userEmail = request.getParameter("email");
        ShareForm form = new ShareForm(noteId, userEmail);

        return noteService.shareNote(form, currentUser.getId());
    }

    @RequestMapping(value = "telenote/deletesync", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> deleteSync(HttpServletRequest request,Authentication auth) throws ServiceException {
        noteService.deleteShareLink(Long.parseLong(request.getParameter("noteId")), Long.parseLong(request.getParameter("userId")));

        return new ResponseEntity<>(new ResponseMessage(true, "Разрыв совершён"), HttpStatus.OK);
    }

    @RequestMapping(value = "/telenote/order", method = RequestMethod.POST)
    public void updateOrder(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws RepositoryException, ServiceException {
        final OrderData orderData = new OrderData(
        Long.parseLong(request.getParameter("idPrev")),
        Long.parseLong(request.getParameter("idCur")),
        Long.parseLong(request.getParameter("idNext")));

        noteService.updateOrder(orderData);
    }

    @MessageMapping("/updatenote")
    public void updateNote(NoteSocketCommand note) throws Exception {
        //UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        List<NoteModel> models = noteService.getAllSharedNoteModels(note.getId());
        if (!models.isEmpty()) {
            for (NoteModel model : models) {
                note.setId(model.getId());

                //messagingTemplate.convertAndSendToUser(model.getUsernameOfShareUser(), "/queue/notes", note);
                messagingTemplate.convertAndSendToUser(model.getEmailOfShareUser(), "/queue/notes", note);
            }
        }
    }
}
