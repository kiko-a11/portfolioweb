package jp.summit_tech.domain.repository;

import java.util.List;

import jp.summit_tech.domain.model.Link;

/**
 * Interface for MyBatis to handle link
 *
 */
public interface LinkMapper {

    public boolean insert(Link object) throws Exception;

    public Link selectOne(Link object) throws Exception;

    public List<Link> selectMany(Link object) throws Exception;

    public boolean updateOne(Link object) throws Exception;

    public boolean deleteOne(Link object) throws Exception;
}
