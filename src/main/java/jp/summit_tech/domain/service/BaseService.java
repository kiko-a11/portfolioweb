package jp.summit_tech.domain.service;

import java.util.List;

/**
 * Interface to handle data about T object
 *
 * @param <T> target data
 */
public interface BaseService<T> {

    /**
     * Insert one record about T object
     * @param object target of insertion (not {@code null})
     * @return If success to insert the record, then return true
     * @throws Exception DB exception, etc.
     */
    public boolean insertOne(T object) throws Exception;

    /**
     * Select one record about T object
     * @param object T object which only set id to select data (not {@code null})
     * @return Selected T object (or {@code null} at some classes)
     * @throws Exception DB exception, and some classes throws exception about data not found, etc.
     */
    public T selectOne(T object) throws Exception;

    /**
     * Select many records about T object
     * @param object T object which only set some items to select data (not {@code null})
     * @return List of selected T object (not {@code null}, but may be empty list)
     * @throws Exception DB exception, etc.
     */
    public List<T> selectMany(T object) throws Exception;

    /**
     * Update one record about T object
     * @param object T object which set items to update one data (not {@code null})
     * @return If success to update the record, then return true
     * @throws Exception DB exception, etc.
     */
    public boolean updateOne(T object) throws Exception;

    /**
     * Delete one record about T object
     * @param object T object which only set id to delete data (not {@code null})
     * @return If success to delete the record, then return true
     * @throws Exception DB exception, etc.
     */
    public boolean deleteOne(T object) throws Exception;

    /**
     * Check for the existence of T object
     * @param object T object which only set id to select data (not {@code null})
     * @return If success to select the record, then return true
     * @throws Exception exception at select data
     */
    public boolean checkExist(T object) throws Exception;

    /**
     * Check for the existence of T object. If it exists then update one record, else insert one record.
     * @param object target of insertion or update (not {@code null})
     * @return If success to insert or update the record, then return true
     * @throws Exception Exception at insert or update data
     */
    public boolean upsertOne(T object) throws Exception;

}
