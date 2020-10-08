package jp.summit_tech.domain.model;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * Model for link data. This is about any one project.
 *
 */
@Data
public class Link {
    long id;
    long projectId;
    long sortNo;
    @NotBlank
    String url;
    @NotBlank
    String displayName;
    boolean thumbnailFlag;
    String thumbnailFileName;
    String thumbnailFilePath;
    MultipartFile upload;

    /**
     * Default constructor (default id and no is -1)
     */
    public Link() {
        this.id = -1;
        this.projectId = -1;
        this.sortNo = -1;
        this.thumbnailFlag = false;
    }

    /**
     * Constructor Used when only the id is known, such as when deleting.
     * @param id Id of this link data
     */
    public Link(long id) {
        this();
        this.id = id;
    }

    /**
     * Constructor to set all items.
     * @param id Id of this link data
     * @param projectId Id of the project which owns this link data.
     * @param sortNo Sort order of this link on the page
     * @param url URL of this link
     * @param displayName The name used when this link data is published
     * @param thumbnailFlag If this flag is true, then thumbnail image uploaded by user is used, when this link published
     * @param thumbnailFileName The name of thumbnail image of this link (used only when thumbnailFlag is True)
     * @param thumbnailFilePath The saved path of thumbnail image of this link
     * @param upload Thumbnail image data (used only when uploading)
     */
    public Link(long id, long projectId, long sortNo, String url, String displayName, boolean thumbnailFlag,
          String thumbnailFileName, String thumbnailFilePath, MultipartFile upload) {
        super();
        this.id = id;
        this.projectId = projectId;
        this.sortNo = sortNo;
        this.url = url;
        this.displayName = displayName;
        this.thumbnailFlag = thumbnailFlag;
        this.thumbnailFileName = thumbnailFileName;
        this.thumbnailFilePath = thumbnailFilePath;
        this.upload = upload;
    }
}
