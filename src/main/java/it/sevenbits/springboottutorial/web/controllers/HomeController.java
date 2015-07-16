package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.core.domain.Note;
import it.sevenbits.springboottutorial.web.domain.NoteForm;
import it.sevenbits.springboottutorial.web.domain.UserForm;
import it.sevenbits.springboottutorial.web.service.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    private static Logger LOG = Logger.getLogger(HomeController.class);
    private static Long user_id;

    @Autowired
    private UserFormValidator validator;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getWelcome(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/welcome";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String index(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/signin";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String subscribe(@ModelAttribute UserForm form, final Model model) throws ServiceException {
        /*final Map<String, String> errors = validator.validate(form);
        if (errors.size() != 0) {
            // Если есть ошибки в форме, то снова рендерим главную страницу
            model.addAttribute("subscription", form);
            model.addAttribute("errors", errors);
            LOG.info("Subscription form contains errors.");
            return "home/errors";
        }*/
        user_id = userService.signIn(form);

        //postTelenote(model);

        return (user_id > 0) ? "redirect:/telenote" : "home/errors";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String registration(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String registration(@ModelAttribute UserForm form, final Model model) throws ServiceException {
        /*final Map<String, String> errors = validator.validate(form);
        if (errors.size() != 0) {
            // Если есть ошибки в форме, то снова рендерим главную страницу
            model.addAttribute("subscription", form);
            model.addAttribute("errors", errors);
            LOG.info("Subscription form contains errors.");
            return "home/errors";
        }*/

        userService.save(form);
        model.addAttribute("subscription", form);
        return "home/telenote";
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.GET)
    public String resetPass(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/resetPass";
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    public String resetPassInDB(@ModelAttribute UserForm form, final Model model) throws ServiceException{

        final String password = "456";
        userService.updatePass(form, password);


        model.addAttribute("subscription", form);
        return "home/signin";
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.GET)
    public String getTelenote(final Model model) throws ServiceException {
        model.addAttribute("notes", noteService.findUserNotes(user_id));
        model.addAttribute("subscription", new UserForm());
        return "home/telenote";
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.POST)
    public @ResponseBody void editNote (HttpServletRequest request, HttpServletResponse response) throws ServiceException{
        Long id = Long.parseLong(request.getParameter("id"));
        String text = request.getParameter("text");
        NoteForm form = new NoteForm(id, text);

        if (id < 0) {
            noteService.addNote(form, user_id);
        } else {
            noteService.updateNote(form);
        }
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.DELETE)
    public @ResponseBody void deleteNote(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        Note note = new Note();
        note.setId(Long.parseLong(request.getParameter("id")));

        noteService.deleteNote(note);
    }
}
