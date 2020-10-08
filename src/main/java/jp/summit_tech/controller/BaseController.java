package jp.summit_tech.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.model.User;
import jp.summit_tech.domain.service.UserServiceImpl;

/**
 * BaseController is to get user info at controllers
 *
 */
public class BaseController {

    @Autowired
    private UserServiceImpl userSer;

    /**
     * Call User information.
     * @param id User id (not {@code null}, not blank)
     * @return User. If it isn't found, return user in the initial state
     * @throws Exception DB exception
     */
    public User getUsr(String id) throws Exception {
        try {
            return userSer.selectOne(new User(id));
        } catch (Exception e) {
            String msg = e.getMessage();
            if (Objects.isNull(msg) || msg.equals("")) {
                throw new Exception(Conf.MSG_FAILED_OTHER);
            }
            throw e;
        }
    }

}
