package jp.summit_tech.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.model.Project;
import jp.summit_tech.domain.model.UserSetting;
import jp.summit_tech.domain.service.ProjectServiceImpl;
import jp.summit_tech.domain.service.UserSettingServiceImpl;

/**
 * Controller for the page to publish some projects data and one user-setting
 *
 */
@Controller
@RequestMapping(value = "/publish/{userId}")
public class PublishController extends BaseController {

    @Autowired
    private UserSettingServiceImpl userSettingSer;

    @Autowired
    private ProjectServiceImpl projectSer;

    private static final String MV_NAME = "publish";

    /**
     * Call some projects data and one user-setting, and set them on the page.
     * @param userId Id of user account (not {@code null}, not blank)
     * @return ModelAndView set user-setting data and projects data.(not {@code null})
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet(@PathVariable("userId") String userId) {
        ModelAndView mv = new ModelAndView(MV_NAME);

        UserSetting userSetting = null;
        List<Project> projectList = new ArrayList<Project>();

        // get data
        try {
            System.out.println("## Get data");
            // userSetting
            userSetting = userSettingSer.selectOne(new UserSetting(userId));
            System.out.println(userSetting);
            // project
            projectList = projectSer.selectMany(new Project(userId));
            System.out.println(projectList);
        }
        catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                msg = Conf.MSG_FAILED_OTHER;
            }
            mv.addObject("msg", msg);

            return mv;
        }
        mv.addObject("userSetting", userSetting);
        mv.addObject("projectList", projectList);

        return mv;
    }

}
