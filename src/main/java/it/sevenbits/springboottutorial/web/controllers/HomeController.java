package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.web.domain.UserForm;
import it.sevenbits.springboottutorial.web.service.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private static Logger LOG = Logger.getLogger(HomeController.class);

    @Autowired
    private UserFormValidator validator;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String index(final Model model) {
        // В модель добавим новый объект формы подписки
        model.addAttribute("userForm", new UserForm());
        // Так как нет аннотации @ResponseBody, то spring будет искать шаблон по адресу home/index
        // Если шаблона не будет найдено, то вернется 404 ошибка
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
        // В запросе пришла заполненная форма. Отправим в модель этот объект и отрендерим ее на другом шаблоне.

        model.addAttribute("userForm", form);
        /*if (form.getConfirm()) {
            emailService.sendMail(form.getEmail(), "Ololo mail sended", "Take that bastard!");
        }*/
        return (userService.signIn(form)) ? "home/telenote" : "home/errors";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String registration(final Model model) {
        // В модель добавим новый объект формы подписки
        model.addAttribute("userForm", new UserForm());
        // Так как нет аннотации @ResponseBody, то spring будет искать шаблон по адресу home/index
        // Если шаблона не будет найдено, то вернется 404 ошибка
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
        // В запросе пришла заполненная форма. Отправим в модель этот объект и отрендерим ее на другом шаблоне.
        model.addAttribute("userForm", form);
        /*if (form.getConfirm()) {
            emailService.sendMail(form.getEmail(), "Ololo mail sended", "Take that bastard!");
        }*/
        return "home/signin";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String telenote(final Model model) {
        // В модель добавим новый объект формы подписки
        model.addAttribute("userForm", new UserForm());
        // Так как нет аннотации @ResponseBody, то spring будет искать шаблон по адресу home/index
        // Если шаблона не будет найдено, то вернется 404 ошибка
        return "home/telenote";
    }

    /*@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
    @ResponseBody
    public List<SubscriptionModel> getSubscriptions() throws ServiceException {
        return userService.findAll();
    }*/
}
