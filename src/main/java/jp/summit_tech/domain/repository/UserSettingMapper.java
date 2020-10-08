package jp.summit_tech.domain.repository;

import java.util.List;

import jp.summit_tech.domain.model.UserSetting;

/**
 * Interface for MyBatis to handle user-setting
 *
 */
public interface UserSettingMapper {

    public boolean insert(UserSetting object) throws Exception;

    public UserSetting selectOne(UserSetting object) throws Exception;

    public List<UserSetting> selectMany(UserSetting object) throws Exception;

    public boolean updateOne(UserSetting object) throws Exception;

    public boolean deleteOne(UserSetting object) throws Exception;
}
