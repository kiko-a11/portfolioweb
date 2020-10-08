package jp.summit_tech.domain.repository;

import java.util.List;

import jp.summit_tech.domain.model.Project;

/**
 * Interface for MyBatis to handle project
 *
 */
public interface ProjectMapper {

    public boolean insert(Project object) throws Exception;

    public Project selectOne(Project object) throws Exception;

    public List<Project> selectMany(Project object) throws Exception;

    public boolean updateOne(Project object) throws Exception;

    public boolean deleteOne(Project object) throws Exception;
}
