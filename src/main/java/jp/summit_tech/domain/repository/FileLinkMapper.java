package jp.summit_tech.domain.repository;

import java.util.List;

import jp.summit_tech.domain.model.FileLink;

/**
 * Interface for MyBatis to handle fileLink
 *
 */
public interface FileLinkMapper {

    public boolean insert(FileLink object) throws Exception;

    public FileLink selectOne(FileLink object) throws Exception;

    public List<FileLink> selectMany(FileLink object) throws Exception;

    public boolean updateOne(FileLink object) throws Exception;

    public boolean deleteOne(FileLink object) throws Exception;
}
