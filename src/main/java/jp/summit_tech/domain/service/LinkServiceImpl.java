package jp.summit_tech.domain.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import jp.summit_tech.conf.Conf;
import jp.summit_tech.domain.model.Link;
import jp.summit_tech.domain.repository.LinkMapper;
import jp.summit_tech.domain.repository.UtilMapper;

/**
 * Handle link object.
 *
 */
@Service
public class LinkServiceImpl implements BaseService<Link> {

    private static final String PUT_BASE_DIR_PATH = Conf.CONST_PUT_FILE_PATH + "link" + File.separator + "thumbnail"
            + File.separator;
    private static final String GET_BASE_DIR_PATH = Conf.CONST_GET_FILE_PATH + "link/thumbnail/";

    @Autowired
    LinkMapper mapper;

    @Autowired
    UtilMapper utilMapper;

    @Autowired
    PlatformTransactionManager txManager;

    /**
     * Insert one record about link object, and put file of thumbnail on the server.
     * @param object link object to insert (not {@code null}. Id for object can be -1)
     * @return If success to insert the record, then return true
     * @throws Exception DB exception, file put exception
     */
    @Override
    public boolean insertOne(Link object) throws Exception {
        boolean result;
        long id = -1;

        // BEGIN
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);

        try {
            // insert one record
            result = mapper.insert(object);

            // get id of the inserted record
            id = utilMapper.lastInsertId();
            object.setId(id);

            // put file on the server
            putData(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (IOException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new IOException(Conf.MSG_FILE_ERR);
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
     * Select one record about link object
     * @param object link object which only set id to select data (not {@code null}. Id required for object other than -1)
     * @return Selected link object (not {@code null})
     * @throws Exception Id is -1 exception, DB exception, data not found exception
     */
    @Override
    public Link selectOne(Link object) throws Exception {
        Link result = null;

        // Id must be other than -1 (-1 is initial state of id)
        if (object.getId() == -1) {
            throw new IllegalArgumentException("Id = -1");
        }

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

        // exception if record not found
        if (Objects.isNull(result)) {
            throw new Exception(Conf.MSG_NOT_FOUND);
        }
        // generate path of the file on the server
        setPath(result);

        // COMMIT
        txManager.commit(txStatus);
        return result;
    }

    /**
     * Select many records about link object
     * @param object link object which only Project-id to select data (not {@code null}. Project-id required for object other than -1)
     * @return List of selected link object (not {@code null}, but may be empty list)
     * @throws Exception ProjectId is -1 exception, DB exception
     */
    @Override
    public List<Link> selectMany(Link object) throws Exception {
        List<Link> result = null;

        // ProjectId must be other than -1 (-1 is initial state of ProjectId)
        if (object.getProjectId() == -1) {
            throw new IllegalArgumentException("ProjectId = -1");
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
            // generate path of the file on the server
            for (Link link : result) {
                setPath(link);
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
     * Update one record about link object, and put file of thumbnail on the server.
     * @param object link object which set items to update one data (not {@code null}. Id required for object other than -1)
     * @return If success to update the record, then return true
     * @throws Exception Id is -1 exception, DB exception, file put exception
     */
    @Override
    public boolean updateOne(Link object) throws Exception {
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
            // update one record
            result = mapper.updateOne(object);
            // put file on the server
            putData(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (IOException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new IOException(Conf.MSG_FILE_ERR);
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
     * Delete one record about link object, and delete file of thumbnail on the server.
     * @param object link object which only set id to delete data (not {@code null}. Id required for object other than -1)
     * @return If success to delete the record, then return true
     * @throws Exception Id is -1 exception, DB exception, file delete exception
     */
    @Override
    public boolean deleteOne(Link object) throws Exception {
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
            // delete one record
            result = mapper.deleteOne(object);
            // delete file on the server
            daleteData(object);
        } catch (SQLException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new SQLException(Conf.MSG_DB_ERR);
        } catch (IOException e) {
            txManager.rollback(txStatus);
            e.printStackTrace();
            throw new IOException(Conf.MSG_FILE_ERR);
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
     * Check for the existence of link object
     * @param object link object which only set id to select data (not {@code null}. Id for object can be -1)
     * @return If success to select the record, then return true
     * @throws Exception DB exception
     */
    @Override
    public boolean checkExist(Link object) throws Exception {
        Link result = null;

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
     * Check for the existence of link object. If it exists then update one record, else insert one record.
     * @param object link object to insert or update (not {@code null}. Id for object can be -1)
     * @return If success to insert or update the record, then return true
     * @throws Exception DB exception, file put exception
     */
    @Override
    public boolean upsertOne(Link object) throws Exception {
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
     * About link object, if uploaded file exists, put them on the file server.
     * @param object Link object (not {@code null}, but uploaded file can be null)
     * @throws IOException When file put or pre-delete fails
     */
    private void putData(Link object) throws IOException {
        MultipartFile upload = object.getUpload();
        try {
            // if file is uploaded,
            if (!Objects.isNull(upload)) {
                String ba = new String(IOUtils.toByteArray(upload.getInputStream()));
                // if file is not empty, put file.
                if (!(ba.equals(""))) {
                    // generate path of directory
                    Path dirPath = Paths.get(PUT_BASE_DIR_PATH + object.getId());
                    // if there isn't directory, create directory
                    if (!Files.exists(dirPath)) {
                        Files.createDirectories(dirPath);
                    }

                    // pre-delete files at this directory
                    DirectoryStream<Path> ds = Files.newDirectoryStream(dirPath, "*.*");
                    for (Path deleteFilePath : ds) {
                        System.out.println("DEL:" + deleteFilePath.toString());
                        Files.delete(deleteFilePath);
                    }

                    // generate path of file
                    Path filePath = Paths.get(dirPath + File.separator + object.getThumbnailFileName());
                    System.out.println("PUT:" + filePath.toString());
                    // set path of file on the object
                    object.setThumbnailFilePath(filePath.toString());

                    // put file at the directory
                    Files.copy(upload.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(Conf.MSG_FILE_ERR);
        }
    }

    /**
     * About link object, delete uploaded file on the file server.
     * @param object Link object (not {@code null}, and id is required for object)
     * @throws IOException When file delete fails
     */
    private void daleteData(Link object) throws IOException {
        // generate path of directory
        Path dirPath = Paths.get(PUT_BASE_DIR_PATH + object.getId());
        try {
            // if there is directory, delete files there
            if(Files.exists(dirPath)) {
                DirectoryStream<Path> ds = Files.newDirectoryStream(dirPath, "*.*");
                for (Path deleteFilePath : ds) {
                    System.out.println("DEL:" + deleteFilePath.toString());
                    Files.delete(deleteFilePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(Conf.MSG_FILE_ERR);
        }
    }

    /**
     * About link object, set filePath from FileName and Id.
     * @param object (not {@code null}, and id is required for object)
     */
    private void setPath(Link object) {
        String thumbnailFileName = object.getThumbnailFileName();
        // if there is thumbnailFileName, set file path
        if (!(Objects.isNull(thumbnailFileName)) && !(thumbnailFileName.equals(""))) {
            Path dirPath = Paths.get(GET_BASE_DIR_PATH + object.getId());
            Path filePath = Paths.get(dirPath + "/" + object.getThumbnailFileName());
            System.out.println("GET:" + filePath);
            object.setThumbnailFilePath(filePath.toString());
        }
    }
}
