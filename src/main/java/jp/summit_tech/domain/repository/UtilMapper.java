package jp.summit_tech.domain.repository;

/**
 * Interface for MyBatis to use general SQL
 *
 */
public interface UtilMapper {

    public long lastInsertId() throws Exception;

}
