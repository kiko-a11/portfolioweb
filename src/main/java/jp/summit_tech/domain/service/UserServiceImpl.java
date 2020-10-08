package jp.summit_tech.domain.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.model.User;
import jp.summit_tech.domain.repository.UserMapper;
import jp.summit_tech.domain.repository.UtilMapper;

/**
 * Handle user-account object.
 *
 */
@Service
public class UserServiceImpl implements BaseService<User> {

    @Autowired
    UserMapper mapper;

    @Autowired
    UtilMapper utilMapper;

    @Autowired
    PlatformTransactionManager txManager;

    /**
     * Insert one record about user-account object
     * @param object User-account object to insert (not {@code null}. Id isn't required for object)
     * @return If success to insert the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean insertOne(User object) throws Exception {
        boolean result = false;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
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
     * Select one record about user-account object
     * @param object User-account object which only set id to select data (not {@code null})
     * @return Selected user-account object.(not {@code null}. If no user-account is found, then return user-account in initial state)
     * @throws Exception DB exception
     */
    @Override
    public User selectOne(User object) throws Exception {
        User result = null;

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
            result = new User();
        }

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * Select many records about user-account object
     * @param object User-account object which only authority to select data (not {@code null}. Authority required for object other than "" or {@code null})
     * @return List of selected user-account object.(not {@code null}, but may be empty list)
     * @throws Exception Authority is none exception, DB exception.
     */
    @Override
    public List<User> selectMany(User object) throws Exception {
        List<User> result = null;

        // Authority must be other than "" or null
        if (object.getAuthority().equals("") || Objects.isNull(object.getAuthority())) {
            throw new IllegalArgumentException("Authority is none.");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // select records
            result = mapper.selectMany(object);

            // make result of null to empty list
            if (Objects.isNull(result)) {
                result = Collections.emptyList();
            }
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
     * Update one record about user-account object
     * @param object User-account object which set items to update one user-account data.(not {@code null}. Id required for object other than "" or {@code null})
     * @return If success to update the record, then return true
     * @throws Exception Id is none exception, DB exception.
     */
    @Override
    public boolean updateOne(User object) throws Exception {
        boolean result = false;

        // UserId must be other than "" or null
        if (object.getId().equals("") || Objects.isNull(object.getId())) {
            throw new IllegalArgumentException("UserId is none.");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
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
     * Delete one record about user-account object
     * @param object User-account object which only set id to delete data (not {@code null}. Id required for object other than "" or {@code null})
     * @return If success to delete the record, then return true
     * @throws Exception Id is none exception, DB exception
     */
    @Override
    public boolean deleteOne(User object) throws Exception {
        boolean result = false;

        // UserId must be other than "" or null
        if (object.getId().equals("") || Objects.isNull(object.getId())) {
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
     * Check for the existence of user-account object
     * @param object User-account object which only set id to select data (not {@code null})
     * @return If success to select the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean checkExist(User object) throws Exception {
        User result = null;

        // if UserId is "" or null, return false (not exist)
        if (object.getId().equals("") || Objects.isNull(object.getId())) {
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
     * Check for the existence of user-account object. If it exists then update one project record.
     * @param object User-account object to insert or update (not {@code null}. Id isn't required for object)
     * @return If success to insert or update the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean upsertOne(User object) throws Exception {
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
     * Check for the existence of user-account object. If it exists then update one user-account record and return true, else update no records and return false.
     * @param object User-account object to insert(not {@code null}. Id isn't required for object)
     * @return If record exists then return true, else return false.
     * @throws Exception DB exception
     */
    public boolean checkAndInsert(User object) throws Exception {
        boolean result = false;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            if (checkExist(object)) {
                // if there is select result, update no records and return false
                result = false;
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
}
