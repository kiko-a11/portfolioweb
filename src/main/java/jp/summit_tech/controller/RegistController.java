package jp.summit_tech.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.form.UserForm;
import jp.summit_tech.domain.model.User;
import jp.summit_tech.domain.service.UserServiceImpl;

/**
 * Controller for the page to input user-account
 *
 */
@Controller
@RequestMapping(value = "/regist")
public class RegistController extends BaseController {

    @Autowired
    private UserServiceImpl userSer;

    private static final String MV_NAME = "regist";
    private static final String LOGIN_MV_NAME = "login";

    /**
     * Get new UserForm.
     * @return New UserForm
     */
    @ModelAttribute()
    public UserForm init() {
        return new UserForm();
    }

    /**
     * Generate new UserForm, and set them on the page.
     * @return ModelAndView for registration in initial state (not {@code null})
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet() {
        ModelAndView mv = new ModelAndView(MV_NAME);

        UserForm userForm = null;

        userForm = new UserForm();
        mv.addObject("userForm", userForm);

        return mv;
    }

    /**
     * Register user-account data, and go back to the login page.
     * @param userForm Form to post one user (not {@code null})
     * @param br For validation (not {@code null})
     * @return ModelAndView for going to login page (not {@code null})
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView doPost(@Valid @ModelAttribute UserForm userForm, BindingResult br) {
        ModelAndView mv = new ModelAndView(MV_NAME);

        boolean result;


        // If br.hasErrors, screen remains the same
        if (br.hasErrors()) {
            System.out.println("## br :" + br);
            mv.addObject("userForm", userForm);
            return mv;
        }

        // set data
        User user = userForm.getUser();
        System.out.println("## Set data");
        System.out.println(user);
        try {
            // If data exists, then insert and go back to login page,
            // else the screen remains the same and output message.
            result = userSer.checkAndInsert(user);
            if (result) {
                return new ModelAndView(LOGIN_MV_NAME);
            } else {
                mv.addObject("msg", Conf.MSG_DUPLICATE);
                mv.addObject("userForm", userForm);
                return mv;
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                msg = Conf.MSG_FAILED_OTHER;
            }
            mv.addObject("msg", msg);

            return mv;
        }

    }

}
