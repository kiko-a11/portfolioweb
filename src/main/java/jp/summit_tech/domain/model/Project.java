package jp.summit_tech.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * Model for project data. This contains some links data and files data.
 *
 */
@Data
public class Project {
    long id;
    String userId;
    long sortNo;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String startDate;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String endDate;
    @NotBlank
    String name;
    String overview;
    String memo;
    @Valid
    List<Link> linkList;
    @Valid
    List<FileLink> fileLinkList;

    /**
     * Default constructor (default id and no is -1)
     */
    public Project() {
        this.id = -1;
        this.sortNo = -1;
    }

    /**
     * Constructor Used when only the id is known, such as when deleting.
     * @param id Id of this project
     */
    public Project(long id) {
        this();
        this.id = id;
    }

    /**
     * Constructor Used when only the user-id is known, such as when selecting many data.
     * @param userId Id of the user who owns this project
     */
    public Project(String userId) {
        this();
        this.userId = userId;
    }

    /**
     * Constructor to set all items.
     * @param id Id of this project
     * @param userId Id of the user owns this project
     * @param sortNo Sort order of this project on the page
     * @param startDate start-date of this project
     * @param endDate end-date of this project
     * @param name The name of this project
     * @param overview Overview of this project
     * @param memo Memo about this project
     * @param linkList List of links about this project
     * @param fileLinkList List of files about this project
     */
    public Project(long id, String userId, long sortNo, String startDate, String endDate, String name, String overview, String memo,
            List<Link> linkList, List<FileLink> fileLinkList) {
        super();
        this.id = id;
        this.userId = userId;
        this.sortNo = sortNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.overview = overview;
        this.memo = memo;
        this.linkList = linkList;
        this.fileLinkList = fileLinkList;
    }
}
