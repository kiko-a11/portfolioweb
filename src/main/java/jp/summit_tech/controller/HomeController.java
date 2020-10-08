package jp.summit_tech.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.form.ProjectForm;
import jp.summit_tech.domain.model.FileLink;
import jp.summit_tech.domain.model.Link;
import jp.summit_tech.domain.model.Project;
import jp.summit_tech.domain.model.User;
import jp.summit_tech.domain.service.FileLinkServiceImpl;
import jp.summit_tech.domain.service.LinkServiceImpl;
import jp.summit_tech.domain.service.ProjectServiceImpl;

/**
 * Controller for the page to enter projects data and links data, files data
 *
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController extends BaseController {

    @Autowired
    private ProjectServiceImpl projectSer;

    @Autowired
    private LinkServiceImpl linkSer;

    @Autowired
    private FileLinkServiceImpl fileLinkSer;

    private static final String MV_NAME = "home";

    /**
     * Get new ProjectForm.
     * @return New ProjectForm
     */
    @ModelAttribute()
    public ProjectForm init() {
        return new ProjectForm();
    }

    /**
     * Call projects data and links data, files data, and set them on the page.
     * @param principal Principal has login id (not {@code null})
     * @param session Information between an HTTP client and an HTTP serverSession. It has token String (not {@code null})
     * @return ModelAndView for home set projects data and links data, files data to form object.(not {@code null})
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet(Principal principal, HttpSession session) {
        ModelAndView mv = new ModelAndView(MV_NAME);
        User usr = null;
        String beforeToken;

        List<Project> projectList = new ArrayList<Project>();
        ProjectForm projectForm = null;

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
            projectList = projectSer.selectMany(new Project(usr.getId()));
            System.out.println("## Get data");
            System.out.println(projectList);

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
        projectForm = new ProjectForm(projectList, beforeToken);
        mv.addObject("projectForm", projectForm);

        return mv;
    }

    /**
     * Register projects data and links data, files data, and redisplay the page.
     * @param projectForm Form to post projects data and links data, files data (not {@code null}, but projectForm.projectList can be empty)
     * @param br BindingResult for form validation (not {@code null})
     * @param principal Principal has login id (not {@code null})
     * @param session Information between an HTTP client and an HTTP serverSession. It has token String (not {@code null})
     * @return ModelAndView for home set projects data and links data, files data to form object.(not {@code null})
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView doPost(@Valid @ModelAttribute ProjectForm projectForm, BindingResult br, Principal principal,
            HttpSession session) {
        ModelAndView mv = new ModelAndView(MV_NAME);
        User usr;

        String beforeToken;
        String afterToken;
        List<Project> projectList = projectForm.getProjectList();
        Object beforeTokenObj = session.getAttribute("token");

        if (Objects.isNull(beforeTokenObj)) {
            beforeToken = "";
        } else {
            beforeToken = beforeTokenObj.toString();
        }
        afterToken = projectForm.getToken();

        // If null, set empty list
        if (Objects.isNull(projectList)) {
            projectList = Collections.emptyList();
        }
        System.out.println(projectList);

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

            mv.addObject("projectForm", projectForm);
            return mv;
        }

        // set data
        System.out.println("## Set data");
        try {
            for (Project projectObj : projectList) {
                // update or insert
                projectSer.upsertOne(projectObj);
            }
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

            mv.addObject("projectForm", projectForm);
            return mv;
        }

        session.setAttribute("token", afterToken);
        System.out.println(session.getAttribute("token"));

        return doGet(principal, session);

    }

    /**
     * Delete one project data and some links data, files data.
     * @param data JSON object for ajax to delete one project data and some links data, files data (not {@code null}, not blank. Id is required)
     * @return Deleted project id (not {@code null}, not blank)
     * @throws Exception DB exception, file delete exception
     */
    @ResponseBody
    @RequestMapping(value = "/deleteproject", method = RequestMethod.POST)
    public String doDeleteProject(@RequestBody String data) throws Exception {
        System.out.println("## Delete data (project)");
        System.out.println(data);

        JSONObject json = new JSONObject(data);
        String id = json.getString("id");

        Project project = new Project(Long.parseLong(id));
        projectSer.deleteOne(project);

        return id;
    }

    /**
     * Delete one link data.
     * @param data JSON object for ajax to delete one link data (not {@code null}, not blank. Id is required)
     * @return Deleted link id (not {@code null}, not blank)
     * @throws Exception DB exception, file delete exception
     */
    @ResponseBody
    @RequestMapping(value = "/deletelink", method = RequestMethod.POST)
    public String doDeleteLink(@RequestBody String data) throws Exception {
        System.out.println("## Delete data (link)");
        System.out.println(data);

        JSONObject json = new JSONObject(data);
        String id = json.getString("id");

        Link link = new Link(Long.parseLong(id));
        linkSer.deleteOne(link);

        return id;
    }

    /**
     * Delete one file data.
     * @param data JSON object for ajax to delete one file data (not {@code null}, not blank. Id is required)
     * @return Deleted file id (not {@code null}, not blank)
     * @throws Exception DB exception, file delete exception
     */
    @ResponseBody
    @RequestMapping(value = "/deletefile", method = RequestMethod.POST)
    public String doDeleteFileLink(@RequestBody String data) throws Exception {
        System.out.println("## Delete data (fileLink)");
        System.out.println(data);

        JSONObject json = new JSONObject(data);
        String id = json.getString("id");

        FileLink link = new FileLink(Long.parseLong(id));
        fileLinkSer.deleteOne(link);

        return id;
    }
}
