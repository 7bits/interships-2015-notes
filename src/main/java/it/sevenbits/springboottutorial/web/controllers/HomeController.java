package it.sevenbits.springboottutorial.web.controllers;


import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.core.domain.OrderData;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.UserNote;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import it.sevenbits.springboottutorial.web.domain.ShareForm;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
import it.sevenbits.springboottutorial.web.service.NoteService;
import it.sevenbits.springboottutorial.web.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.servlet.ModelAndView;
//import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
//import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

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
/*
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rel='stylesheet', type='text/css', href='css/myCSS/");
        stringBuilder.append(noteService.getUserStyle(currentUser.getId()));
        stringBuilder.append(".css'");
*/
        model.addAttribute("user", currentUser);
        model.addAttribute("notes", noteService.findUserNotes(currentUser.getId()));
        //model.addAttribute("shareUsers", noteService.findShareUsers(currentUser.getId()));
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

        Long id = Long.parseLong(request.getParameter("id"));
        List<UserDetailsImpl> shareUsers = noteService.findShareUsers(id);

        List<UserDetailsImpl> users = new ArrayList<UserDetailsImpl>();
        users.add(noteService.getUserWhoSharedNote(id));

        for (UserDetailsImpl u : shareUsers) {
            if (!users.get(0).getEmail().equals(u.getEmail())) users.add(u);
        }

        return users;
    }

    @RequestMapping(value = "/telenote/{noteId:\\d+}={userId:\\d+}", method = RequestMethod.GET)
    public void breakSync(@PathVariable("noteId") Long noteId, @PathVariable("userId") Long userId) throws ServiceException {
        UserNote userNote = new UserNote();
        userNote.setNote_id(noteId);
        userNote.setUser_id(userId);
    }

    @RequestMapping(value = "/telenote/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteNote(@PathVariable("id") Long id, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Note note = new Note();
        note.setId(id);

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

    @RequestMapping(value = "/telenote/order", method = RequestMethod.POST)
    public void updateOrder(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws RepositoryException, ServiceException {
        final OrderData orderData = new OrderData(
                Long.parseLong(request.getParameter("id_prev")),
                Long.parseLong(request.getParameter("id_cur")),
                Long.parseLong(request.getParameter("id_next")));

        noteService.updateOrder(orderData);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    //public ModelAndView handleRegistrationPost(@Valid @ModelAttribute("form") UserCreateForm form) throws ServiceException {
    public String getSharedNotesByUserIdList(final Model model, HttpServletRequest request,
                                             HttpServletResponse response, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        boolean showMyNotes = false;
        List<Long> shareUserIds = new ArrayList<>();

        // read all checked params
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet())
        {
            if(entry.getValue()[0].equals("on"))
                shareUserIds.add(Long.parseLong(entry.getKey()));
        }

        // if nothing is checked - redirect
        if(shareUserIds.size() == 0)
            return "redirect:/telenote";

        // check first param(showMyNotes) and delete it from list
        if(shareUserIds.get(0) == 0) {
            showMyNotes = true;
            shareUserIds.remove(0);
        }

        List<Note> sharedNotesByUsers = noteService.getNotesByUserIdList(shareUserIds, currentUser.getId(), showMyNotes);

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("notes", sharedNotesByUsers);
        model.addAttribute("shareUsers", noteService.findShareUsers(currentUser.getId()));
        return "home/telenote";
    }
}
