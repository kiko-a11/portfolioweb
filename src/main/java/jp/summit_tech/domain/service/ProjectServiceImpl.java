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
import jp.summit_tech.domain.model.FileLink;
import jp.summit_tech.domain.model.Link;
import jp.summit_tech.domain.model.Project;
import jp.summit_tech.domain.repository.ProjectMapper;
import jp.summit_tech.domain.repository.UtilMapper;

/**
 * Handle project object.
 *
 */
@Service
public class ProjectServiceImpl implements BaseService<Project> {

    @Autowired
    ProjectMapper mapper;

    @Autowired
    LinkServiceImpl linkSer;

    @Autowired
    FileLinkServiceImpl fileLinkSer;

    @Autowired
    UtilMapper utilMapper;

    @Autowired
    PlatformTransactionManager txManager;

    /**
     * Insert one record about project object, and link and file records about it.
     * @param object project object to insert (not {@code null}. Id for object can be -1)
     * @return If success to insert the record, then return true
     * @throws Exception DB exception about project, and file put exception at files and links data.
     */
    @Override
    public boolean insertOne(Project object) throws Exception {
        boolean result = false;
        long projectId = -1;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // insert one record of project
            result = mapper.insert(object);
            // get id of the inserted record
            projectId = utilMapper.lastInsertId();

            // insert records of links related to this project
            if (Objects.isNull(object.getLinkList())) {
                object.setLinkList(Collections.emptyList());
            }
            for (Link linkObj : object.getLinkList()) {
                linkObj.setProjectId(projectId);
                result = linkSer.insertOne(linkObj);
            }

            // insert records of files related to this project
            if (Objects.isNull(object.getFileLinkList())) {
                object.setFileLinkList(Collections.emptyList());
            }
            for (FileLink fileLinkObj : object.getFileLinkList()) {
                fileLinkObj.setProjectId(projectId);
                result = fileLinkSer.insertOne(fileLinkObj);
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
     * Select one record about project object, and link and file records about it.
     * @param object project object which only set id to select data (not {@code null}. Id required for object other than -1)
     * @return Selected project object.(not {@code null}) It contains list of links and files about it.(not {@code null}, but may be empty list)
     * @throws Exception Id is -1 exception, DB exception, data not found exception.
     */
    @Override
    public Project selectOne(Project object) throws Exception {
        Project result = null;

        // Id must be other than -1 (-1 is initial state of id)
        if (object.getId() == -1) {
            throw new IllegalArgumentException("Id = -1");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // select one record of project
            result = mapper.selectOne(object);

            // exception if record not found
            if (Objects.isNull(result)) {
                throw new Exception(Conf.MSG_NOT_FOUND);
            }

            // select records of links related to this project
            Link link = new Link();
            link.setProjectId(result.getId());
            List<Link> linkList = linkSer.selectMany(link);
            result.setLinkList(linkList);

            // select records of files related to this project
            FileLink fileLink = new FileLink();
            fileLink.setProjectId(result.getId());
            List<FileLink> fileLinkList = fileLinkSer.selectMany(fileLink);
            result.setFileLinkList(fileLinkList);
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
     * Select many records about project object, and link and file records about it.
     * @param object project object which only user-id to select data (not {@code null}. User-id required for object other than "" or {@code null})
     * @return List of selected project object.(not {@code null}, but may be empty list) It contains list of links and files about it.(not {@code null}, but may be empty list)
     * @throws Exception UserId is none exception, DB exception.
     */
    @Override
    public List<Project> selectMany(Project object) throws Exception {
        List<Project> result = null;

        // UserId must be other than "" or null
        if (object.getUserId().equals("") || Objects.isNull(object.getUserId())) {
            throw new IllegalArgumentException("UserId is none.");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // select records of project
            result = mapper.selectMany(object);

            // make result of null to empty list
            if (Objects.isNull(result)) {
                result = Collections.emptyList();
            }

            // select records of links and files related to these project
            for (Project project : result) {
                // select records of links
                Link link = new Link();
                link.setProjectId(project.getId());
                List<Link> linkList = linkSer.selectMany(link);
                project.setLinkList(linkList);

                // select records of files
                FileLink fileLink = new FileLink();
                fileLink.setProjectId(project.getId());
                List<FileLink> fileLinkList = fileLinkSer.selectMany(fileLink);
                project.setFileLinkList(fileLinkList);
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
     * Update one record about project object, and link and file records about it.
     * @param object project object which set items to update one project data.(not {@code null}. Id required for object other than -1) It contains some links and files data.({@code null} is OK)
     * @return If success to update the record, then return true
     * @throws Exception Id is -1 exception, DB exception, and file put exception at files and links data.
     */
    @Override
    public boolean updateOne(Project object) throws Exception {
        boolean result = false;

        // Id must be other than -1 (-1 is initial state of id)
        if (object.getId() == -1) {
            throw new IllegalArgumentException("Id = -1");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // update one record of project
            result = mapper.updateOne(object);

            // update or insert records of links related to this project
            if (Objects.isNull(object.getLinkList())) {
                object.setLinkList(Collections.emptyList());
            }
            for (Link linkObj : object.getLinkList()) {
                result = linkSer.upsertOne(linkObj);
            }

            // update or insert records of files related to this project
            if (Objects.isNull(object.getFileLinkList())) {
                object.setFileLinkList(Collections.emptyList());
            }
            for (FileLink fileLinkObj : object.getFileLinkList()) {
                result = fileLinkSer.upsertOne(fileLinkObj);
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
     * Delete one record about project object, and link and file records about it.
     * @param object Project object which only set id to delete data (not {@code null}. Id required for object other than -1)
     * @return If success to delete the record, then return true
     * @throws Exception Id is -1 exception, DB exception, and file delete exception at files and links data.
     */
    @Override
    public boolean deleteOne(Project object) throws Exception {
        boolean result = false;

        // Id must be other than -1 (-1 is initial state of id)
        if (object.getId() == -1) {
            throw new IllegalArgumentException("Id = -1");
        }

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // delete one record of project
            result = mapper.deleteOne(object);

            // delete records of links related to this project
            Link link = new Link();
            link.setProjectId(object.getId());
            List<Link> linkList = linkSer.selectMany(link);
            for (Link deleteLink : linkList) {
                result = linkSer.deleteOne(deleteLink);
            }

            // delete records of files related to this project
            FileLink fileLink = new FileLink();
            fileLink.setProjectId(object.getId());
            List<FileLink> fileLinkList = fileLinkSer.selectMany(fileLink);
            for (FileLink deleteLink : fileLinkList) {
                result = fileLinkSer.deleteOne(deleteLink);
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
     * Check for the existence of project object
     * @param object project object which only set id to select data (not {@code null}. Id for object can be -1)
     * @return If success to select the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean checkExist(Project object) throws Exception {
        Project result = null;

        // if id is -1 (initial state), return false (not exist)
        if (object.getId() == -1) {
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
     * Check for the existence of project object. If it exists then update one project record and some link and file records, else insert them.
     * @param object project object to insert or update (not {@code null}. Id for object can be -1)
     * @return If success to insert or update the record, then return true
     * @throws Exception Exception at insert or update data
     * @throws Exception DB exception, and file put exception at files and links data
     */
    @Override
    public boolean upsertOne(Project object) throws Exception {
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

}
