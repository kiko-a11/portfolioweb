package jp.summit_tech.domain.repository;

import java.util.List;

import jp.summit_tech.domain.model.User;

/**
 * Interface for MyBatis to handle user-account
 *
 */
public interface UserMapper {

    public boolean insert(User object) throws Exception;

    public User selectOne(User object) throws Exception;

    public List<User> selectMany(User object) throws Exception;

    public boolean updateOne(User object) throws Exception;

    public boolean deleteOne(User object) throws Exception;
}
