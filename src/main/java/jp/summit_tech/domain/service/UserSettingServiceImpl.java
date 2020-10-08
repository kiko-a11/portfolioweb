package jp.summit_tech.domain.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.model.UserSetting;
import jp.summit_tech.domain.repository.UserSettingMapper;
import jp.summit_tech.domain.repository.UtilMapper;

/**
 * Handle user-setting object.
 *
 */
@Service
public class UserSettingServiceImpl implements BaseService<UserSetting> {

    @Autowired
    UserSettingMapper mapper;

    @Autowired
    UtilMapper utilMapper;

    @Autowired
    PlatformTransactionManager txManager;

    /**
     * Insert one record about user-setting object
     * @param object User-setting object to insert (not {@code null}. Id isn't required for object)
     * @return If success to insert the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean insertOne(UserSetting object) throws Exception {
        boolean result = false;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);
        try {
            // make birthday null instead of ""
            setData(object);
            // insert one record
            result = mapper.insert(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * Select one record about user-setting object
     * @param object User-setting object which only set user-id to select data (not {@code null})
     * @return Selected user-setting object.(not {@code null}. If no user-setting is found, then return user-setting in initial state)
     * @throws Exception DB exception
     */
    @Override
    public UserSetting selectOne(UserSetting object) throws Exception {
        UserSetting result = null;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // select one record
            result = mapper.selectOne(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        // return empty data set if record not found
        if (Objects.isNull(result)) {
            result = new UserSetting(object.getUserId());
        }

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * This is dummy. Now don't works right now.
     * @param object dummy
     * @return null
     * @throws Exception Always throw exception.
     */
    @Override
    public List<UserSetting> selectMany(UserSetting object) throws Exception {
        throw new Exception("UserSetting.selectMany is dummy method yet.");
    }

    /**
     * Update one record about user-setting object
     * @param object User-setting object which set items to update one user-setting data.(not {@code null}. User-id required for object other than "" or {@code null})
     * @return If success to update the record, then return true
     * @throws Exception User-id is none exception, DB exception.
     */
    @Override
    public boolean updateOne(UserSetting object) throws Exception {
        boolean result = false;

        // UserId must be other than "" or null
        if (object.getUserId().equals("") || Objects.isNull(object.getUserId())) {
            throw new IllegalArgumentException("UserId is none.");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // make birthday null instead of ""
            setData(object);
            // update one record
            result = mapper.updateOne(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * Delete one record about user-setting object
     * @param object User-setting object which only set user-id to delete data (not {@code null}. User-id required for object other than "" or {@code null})
     * @return If success to delete the record, then return true
     * @throws Exception User-id is none exception, DB exception
     */
    @Override
    public boolean deleteOne(UserSetting object) throws Exception {
        boolean result = false;

        // UserId must be other than "" or null
        if (object.getUserId().equals("") || Objects.isNull(object.getUserId())) {
            throw new IllegalArgumentException("UserId is none.");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // delete one record
            result = mapper.deleteOne(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * Check for the existence of user-setting object
     * @param object User-setting object which only set user-id to select data (not {@code null}. Id isn't required for object)
     * @return If success to select the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean checkExist(UserSetting object) throws Exception {
        UserSetting result = null;

        // if UserId is "" or null, return false (not exist)
        if (object.getUserId().equals("") || Objects.isNull(object.getUserId())) {
            return false;
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // select one record
            result = mapper.selectOne(object);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        // COMMIT
        txManager.commit(txStatus);

        // if there is no select result, return false (not exist)
        if (Objects.isNull(result)) {
            return false;
        }

        // if there is select result, return true (exist)
        return true;
    }

    /**
     * Check for the existence of user-setting object. If it exists then update one project record.
     * @param object User-setting object to insert or update (not {@code null}. Id isn't required for object)
     * @return If success to insert or update the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean upsertOne(UserSetting object) throws Exception {
        boolean result = false;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            if (checkExist(object)) {
                // if there is select result, update
                result = updateOne(object);
            } else {
                // if there is no select result, insert
                result = insertOne(object);
            }
        } catch (Exception e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * To register to database, make birthday of User-Setting object null instead of ""
     * @param object User-Setting object (not {@code null}, but birthday can be null)
     */
    private void setData(UserSetting object) {
        String birthday = object.getBirthday();
        if (Objects.isNull(birthday) || birthday.equals("")) {
            // make birthday null instead of ""
            object.setBirthday(null);
        }
    }
}
