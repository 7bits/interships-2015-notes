package it.sevenbits.telenote.web.controllers;


import it.sevenbits.telenote.core.domain.Note;
import it.sevenbits.telenote.core.domain.OrderData;
import it.sevenbits.telenote.core.domain.Role;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.service.NoteService;
import it.sevenbits.telenote.service.ServiceException;
import it.sevenbits.telenote.utils.Helper;
import it.sevenbits.telenote.web.domain.forms.NoteForm;
import it.sevenbits.telenote.web.domain.forms.ShareForm;
import it.sevenbits.telenote.web.domain.forms.UserCreateForm;
import it.sevenbits.telenote.web.domain.models.NoteModel;
import it.sevenbits.telenote.web.domain.models.NoteSocketCommand;
import it.sevenbits.telenote.web.domain.models.ResponseMessage;
import it.sevenbits.telenote.web.domain.models.UserPresentModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NoteService noteService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private static Logger LOG = Logger.getLogger(HomeController.class);

    /**
     * Gets welcome page with attributes(include error).
     * @param request contains attributes.
     * @return welcome page.
     * @throws Exception
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView homePage(HttpServletRequest request) throws Exception{
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
            throw new Exception("An error occured while getting home page." + e.getMessage());
        }

        return model;
    }

    /**
     * Gets all user notes(own and shared). Returns specified model.
     * @param auth contains user data.
     * @return model with all user notes.
     * @throws ServiceException
     */
    @RequestMapping(value = "/telenote", method = RequestMethod.GET)
    public ModelAndView getTelenote(Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        ModelAndView model = new ModelAndView("home/telenote");
        try {
            model.addObject("user", currentUser);
            model.addObject("noteSections", noteService.getSortedMap(currentUser));
            model.addObject("avatar", Helper.getAvatarUrl(currentUser.getUsername()));

        } catch (ServiceException se) {
            throw new ServiceException("An error occurred while getting notes.", se);
        }

        return model;
    }

    /**
     * Adds note if it's a new note(note id less than 1). Edits note if it already exists in db.
     * Returns note id to set it to new note.
     * @param request contains note id.
     * @param auth contains note owner id.
     * @return updated note id.
     * @throws ServiceException
     */
    @RequestMapping(value = "/telenote", method = RequestMethod.POST)
    public @ResponseBody
    Long editNote (HttpServletRequest request, Authentication auth) throws ServiceException{

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
            throw new ServiceException("An error occurred while editing note.", se);
        }

        return form.getId();
    }

    /**
     * Gets list of share users for note.
     * @param request contains note id.
     * @return ist of share users for note.
     * @throws ServiceException
     */
    @RequestMapping(value = "/telenote/checknote", method = RequestMethod.POST)
    public @ResponseBody List<UserDetailsImpl> checkSharedNote(HttpServletRequest request) throws ServiceException {

        Long noteId = Long.parseLong(request.getParameter("id"));

        try {
            return noteService.getListOfShareUsers(noteId);
        } catch (ServiceException se) {
            throw new ServiceException("An error occurred while getting list of share users for note.", se);
        }
    }

    /**
     * Removes note from db.
     * @param noteId note id.
     * @param auth contains note owner id.
     * @throws ServiceException
     */
    @RequestMapping(value = "/telenote/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody Long deleteNote(@PathVariable("id") Long noteId, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Note note = new Note();
        note.setId(noteId);

        try {
            noteService.deleteNote(note, currentUser.getId());
        } catch (ServiceException se) {
            throw new ServiceException("An error occurred while deleting note.", se);
        }

        return note.getId();
    }

    /**
     * Shares note with specified user. Returns error/success message and http status.
     * @param request contains note id and user email.
     * @param auth contains note owner id.
     * @return error/success message and http status.
     * @throws RepositoryException
     * @throws ServiceException
     */
    @RequestMapping(value = "/telenote/share", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<UserPresentModel> shareNote (HttpServletRequest request, Authentication auth) throws RepositoryException, ServiceException{
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();

        Long noteId = Long.parseLong(request.getParameter("id"));
        String userEmail = request.getParameter("email");
        ShareForm form = new ShareForm(noteId, userEmail);

        try {
            UserPresentModel result = noteService.shareNote(form, currentUser.getId());
            result.setAvatar(Helper.getAvatarUrl(result.getUsername()));
            return new ResponseEntity<UserPresentModel>(result, HttpStatus.valueOf(result.getCode()));
        } catch (ServiceException se) {
            UserPresentModel error = new UserPresentModel(false, 406, "Возникла ошибка при шаринге");
            return new ResponseEntity<UserPresentModel>(error, HttpStatus.valueOf(error.getCode()));
        }
    }

    /**
     * Breaks note sync. Returns error/success message and http status.
     * @param request
     * @return success message and http status.
     * @throws ServiceException
     */
    @RequestMapping(value = "telenote/deletesync", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> deleteSync(HttpServletRequest request) throws ServiceException {

        try {
            Long noteId = Long.parseLong(request.getParameter("noteId"));
            Long userId = Long.parseLong(request.getParameter("userId"));
            noteService.deleteShareLink(noteId, userId);
        } catch (ServiceException se) {
            return new ResponseEntity<>(new ResponseMessage(true, messageSource.getMessage("message.deletesync.error", null,
            LocaleContextHolder.getLocale())), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResponseMessage(true, messageSource.getMessage("message.deletesync.ok", null,
        LocaleContextHolder.getLocale())), HttpStatus.OK);
    }

    /**
     * Updates note order of a note.
     * @param request contains previous, current and next note id.
     * @throws RepositoryException
     * @throws ServiceException
     */
    @RequestMapping(value = "/telenote/order", method = RequestMethod.POST)
    public void updateOrder(HttpServletRequest request) throws RepositoryException, ServiceException {
        final OrderData orderData = new OrderData(
        Long.parseLong(request.getParameter("idPrev")),
        Long.parseLong(request.getParameter("idCur")),
        Long.parseLong(request.getParameter("idNext")));

        try {
            noteService.updateOrder(orderData);
        } catch (ServiceException se) {
            LOG.error(MessageFormat.format("Ошибка обновления порядка заметок: idPrev={0}, idCur={1}, idPrev{2}",
                orderData.getId_prev(), orderData.getId_cur(), orderData.getId_next()));
            throw new ServiceException("An error occured while updating note order.", se);
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
            throw new ServiceException("An error occured while getting all shared note models.", se);
        } catch (MessagingException me) {
            throw new ServiceException("An error occured while messaging user.", me);
        }

    }
}
