package jp.summit_tech.domain.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import jp.summit_tech.domain.model.Project;
import lombok.Data;

/**
 * Form for the project
 *
 */
@Data
public class ProjectForm {

    @Valid
    List<Project> projectList;
    String token;

    /**
     * Default constructor
     */
    public ProjectForm() {
        this.projectList = new ArrayList<Project>();
        this.token = "";
    }

    /**
     * Constructor to set all items.
     * @param projectList List of projects
     * @param token
     */
    public ProjectForm(List<Project> projectList, String token) {
        super();
        this.projectList = projectList;
        this.token = token;
    }
}
