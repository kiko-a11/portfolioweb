package jp.summit_tech.controller;

import java.security.Principal;
import java.util.Objects;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.form.UserSettingForm;
import jp.summit_tech.domain.model.User;
import jp.summit_tech.domain.model.UserSetting;
import jp.summit_tech.domain.service.UserSettingServiceImpl;

/**
 * Controller for the page to input user-setting
 *
 */
@Controller
@RequestMapping(value = "/user-setting")
public class UserSettingController extends BaseController {

    @Autowired
    private UserSettingServiceImpl userSettingSer;

    private static final String MV_NAME = "user-setting";

    /**
     * Get new UserSettingForm.
     * @return New UserSettingForm
     */
    @ModelAttribute()
    public UserSettingForm init() {
        return new UserSettingForm();
    }

    /**
     * Call the user-setting data, and set them on the page.
     * @param principal Principal has login id (not {@code null})
     * @param session Information between an HTTP client and an HTTP serverSession. It has token String (not {@code null})
     * @return ModelAndView for user-setting set user-setting data.(not {@code null})
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet(Principal principal, HttpSession session) {
        ModelAndView mv = new ModelAndView(MV_NAME);
        User usr = null;
        String beforeToken;

        UserSetting userSetting = null;
        UserSettingForm userSettingForm = null;

        // user
        try {
            usr = getUsr(principal.getName());
        } catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                msg = Conf.MSG_FAILED_OTHER;
            }
            mv.addObject("msg", msg);
            return mv;
        }
        mv.addObject("userId", usr.getId());
        mv.addObject("userName", usr.getName());

        // get data
        try {
            userSetting = userSettingSer.selectOne(new UserSetting(usr.getId()));
            System.out.println("## Get data");
            System.out.println(userSetting);

            Object beforeTokenObj = session.getAttribute("token");
            if (Objects.isNull(beforeTokenObj)) {
                beforeToken = "";
            } else {
                beforeToken = beforeTokenObj.toString();
            }
            System.out.println(beforeToken);
        }
        catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                msg = Conf.MSG_FAILED_OTHER;
            }
            mv.addObject("msg", msg);

            mv.addObject("userId", usr.getId());
            mv.addObject("userName", usr.getName());

            return mv;
        }
        userSettingForm = new UserSettingForm(userSetting, beforeToken);
        mv.addObject("userSettingForm", userSettingForm);

        return mv;
    }

    /**
     * Register the user-setting data, and redisplay the page.
     * @param userSettingForm Form to post user-setting (not {@code null})
     * @param br BindingResult for form validation (not {@code null})
     * @param principal Principal has login id (not {@code null})
     * @param session Information between an HTTP client and an HTTP serverSession. It has token String (not {@code null})
     * @return ModelAndView for user-setting set user-setting data.(not {@code null})
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView doPost(@Valid @ModelAttribute UserSettingForm userSettingForm, BindingResult br,
            Principal principal, HttpSession session) {
        ModelAndView mv = new ModelAndView(MV_NAME);
        User usr;

        String beforeToken;
        String afterToken;
        UserSetting userSetting = userSettingForm.getUserSetting();
        Object beforeTokenObj = session.getAttribute("token");

        if (Objects.isNull(beforeTokenObj)) {
            beforeToken = "";
        } else {
            beforeToken = beforeTokenObj.toString();
        }
        afterToken = userSettingForm.getToken();

        System.out.println(userSetting);

        // If token has not changed, redisplay screen
        if (beforeToken.equals(afterToken)) {
            return doGet(principal, session);
        }

        // user
        try {
            usr = getUsr(principal.getName());
        } catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                msg = Conf.MSG_FAILED_OTHER;
            }
            mv.addObject("msg", msg);
            session.setAttribute("token", afterToken);
            System.out.println(session.getAttribute("token"));
            return mv;
        }

        // If br.hasErrors, screen remains the same
        if (br.hasErrors()) {
            System.out.println("## br :" + br);
            mv.addObject("msg", Conf.MSG_REGIST_FAILED_VALID);

            mv.addObject("userId", usr.getId());
            mv.addObject("userName", usr.getName());

            session.setAttribute("token", afterToken);
            System.out.println(session.getAttribute("token"));

            mv.addObject("userSettingForm", userSettingForm);
            return mv;
        }

        // set data
        System.out.println("## Set data");
        try {
            // update or insert
            userSettingSer.upsertOne(userSetting);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                msg = Conf.MSG_FAILED_OTHER;
            }
            mv.addObject("msg", msg);

            mv.addObject("userId", usr.getId());
            mv.addObject("userName", usr.getName());

            session.setAttribute("token", afterToken);
            System.out.println(session.getAttribute("token"));

            mv.addObject("userSettingForm", userSettingForm);
            return mv;
        }

        session.setAttribute("token", afterToken);
        System.out.println(session.getAttribute("token"));

        return doGet(principal, session);
    }
}
