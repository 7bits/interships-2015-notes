package it.sevenbits.telenote.web.controllers;


import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.RepositoryException;

import it.sevenbits.telenote.service.NoteService;
import it.sevenbits.telenote.service.ServiceException;
import it.sevenbits.telenote.utils.Helper;

import it.sevenbits.telenote.web.domain.forms.NoteForm;
import it.sevenbits.telenote.web.domain.forms.ShareForm;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import it.sevenbits.telenote.web.domain.models.NoteSocketCommand;
import it.sevenbits.telenote.web.domain.models.ResponseMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.MessageFormat;
import java.util.*;

import it.sevenbits.telenote.web.domain.forms.UserCreateForm;

@Controller
public class HomeController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NoteService noteService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static Logger LOG = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("home/welcome");
        //model.addObject("signinForm", new UserLoginForm());
        model.addObject("signupForm", new UserCreateForm());

        List<String> attrNames = new ArrayList<String>();
        attrNames.add("error");
        attrNames.add("username");

        try {
            Map<String, Object> attrMap = Helper.getAttributeMap(attrNames, request);
            for (Map.Entry<String,Object> entry: attrMap.entrySet()) {
                model.addObject(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {

        }

        return model;
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.GET)
    public ModelAndView getTelenote(Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        ModelAndView model = new ModelAndView("home/telenote");
        try {
            model.addObject("user", currentUser);
            model.addObject("noteSections", noteService.getSortedMap(currentUser));
            model.addObject("avatar", Helper.getAvatarUrl(currentUser.getUsername()));

        } catch (ServiceException se) {
            // show somehow error page
        }

        return model;
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.POST)
    public @ResponseBody
    Long editNote (HttpServletRequest request, HttpServletResponse response, Authentication auth) throws ServiceException{

        Long id = Long.parseLong(request.getParameter("id"));
        String text = request.getParameter("text");
        NoteForm form = new NoteForm(id, text);

        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        try {
            if (id < 0) {
                return noteService.addNote(form, currentUser.getId());
            } else {
                noteService.updateNote(form, currentUser.getId());
            }
        } catch (ServiceException se) {
            // show somehow error page
        }

        return form.getId();
    }

    @RequestMapping(value = "/telenote/checknote", method = RequestMethod.POST)
    public @ResponseBody List<UserDetailsImpl> checkSharedNote(HttpServletRequest request, HttpServletResponse response) throws ServiceException {

        Long noteId = Long.parseLong(request.getParameter("id"));

        try {
            return noteService.getListOfShareUsers(noteId);
        } catch (ServiceException se) {
            return null;
        }
    }


    @RequestMapping(value = "/telenote/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteNote(@PathVariable("id") Long noteId, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Note note = new Note();
        note.setId(noteId);

        try {
            noteService.deleteNote(note, currentUser.getId());
        } catch (ServiceException se) {
            return;// show somehow error page
        }

    }

    @RequestMapping(value = "/telenote/share", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<ResponseMessage> shareNote (HttpServletRequest request, HttpServletResponse response, Authentication auth) throws RepositoryException, ServiceException{
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Long noteId = Long.parseLong(request.getParameter("id"));
        String userEmail = request.getParameter("email");
        ShareForm form = new ShareForm(noteId, userEmail);

        try {
            return noteService.shareNote(form, currentUser.getId());
        } catch (ServiceException se) {
            return new ResponseEntity<>(new ResponseMessage(false, "Error on service"), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @RequestMapping(value = "telenote/deletesync", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> deleteSync(HttpServletRequest request,Authentication auth) throws ServiceException {

        try {
            Long noteId = Long.parseLong(request.getParameter("noteId"));
            Long userId = Long.parseLong(request.getParameter("userId"));
            noteService.deleteShareLink(noteId, userId);
        } catch (ServiceException se) {
            return new ResponseEntity<>(new ResponseMessage(true, messageSource.getMessage("message.deletesync.ok", null, LocaleContextHolder.getLocale())), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseMessage(true, messageSource.getMessage("message.deletesync.error", null, LocaleContextHolder.getLocale())), HttpStatus.OK);
    }

    @RequestMapping(value = "/telenote/order", method = RequestMethod.POST)
    public void updateOrder(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws RepositoryException, ServiceException {
        final OrderData orderData = new OrderData(
        Long.parseLong(request.getParameter("idPrev")),
        Long.parseLong(request.getParameter("idCur")),
        Long.parseLong(request.getParameter("idNext")));

        try {
            noteService.updateOrder(orderData);
        } catch (ServiceException se) {
            // show somehow error page
//            LOG.error(MessageFormat.format("Ошибка обновления порядка заметок: idPrev={0}, idCur={1}, idPrev{2}",
//                    orderData.getId_prev(), orderData.getId_cur(),orderData.getId_next()));
        }

    }

    @MessageMapping("/updatenote")
    public void updateNote(NoteSocketCommand note) throws Exception {
        //UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        try {
            List<NoteModel> models = noteService.getAllSharedNoteModels(note.getId());
            if (!models.isEmpty()) {
                for (NoteModel model : models) {
                    note.setId(model.getId());

                    //messagingTemplate.convertAndSendToUser(model.getUsernameOfShareUser(), "/queue/notes", note);
                    messagingTemplate.convertAndSendToUser(model.getEmailOfShareUser(), "/queue/notes", note);
                }
            }
        } catch (ServiceException se) {
            // show somehow error page
        }
    }
}
