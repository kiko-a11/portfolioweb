package jp.summit_tech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the page to login
 *
 */
@Controller
public class LoginController {

    /**
     * Get the login page.
     * @return Page to login (not {@code null}, not blank)
     */
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String loginGet() {
        return "login";
    }
}