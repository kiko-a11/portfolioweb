package jp.summit_tech.domain.model;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * Model for file data. This is about any one project.
 *
 */
@Data
public class FileLink {
    long id;
    long projectId;
    long sortNo;
    @NotBlank
    String displayName;
    @NotBlank(message="{dataNotBlank}")
    String dataFileName;
    String dataFilePath;
    MultipartFile dataFileUpload;
    boolean thumbnailFlag;
    String thumbnailFileName;
    String thumbnailFilePath;
    MultipartFile thumbnailFileUpload;

    /**
     * Default constructor (default id and no is -1)
     */
    public FileLink() {
        this.id = -1;
        this.projectId = -1;
        this.sortNo = -1;
        this.thumbnailFlag = false;
    }

    /**
     * Constructor Used when only the id is known, such as when deleting.
     * @param id Id of this file data
     */
    public FileLink(long id) {
        this();
        this.id = id;
    }

    /**
     * Constructor to set all items.
     * @param id Id of this file data
     * @param projectId Id of the project which owns this file data.
     * @param sortNo Sort order of this file on the page
     * @param displayName The name used when this file data is published
     * @param dataFileName The name of this file
     * @param dataFilePath The saved path of this file
     * @param dataFileUpload File data (used only when uploading)
     * @param thumbnailFlag If this flag is true, then thumbnail image uploaded by user is used, when this file published
     * @param thumbnailFileName The name of thumbnail image of this file (used only when thumbnailFlag is True)
     * @param thumbnailFilePath The saved path of thumbnail image of this file
     * @param thumbnailFileUpload Thumbnail image data (used only when uploading)
     */
    public FileLink(long id, long projectId, long sortNo, String displayName, String dataFileName, String dataFilePath,
            MultipartFile dataFileUpload, boolean thumbnailFlag, String thumbnailFileName, String thumbnailFilePath,
            MultipartFile thumbnailFileUpload) {
        super();
        this.id = id;
        this.projectId = projectId;
        this.sortNo = sortNo;
        this.displayName = displayName;
        this.dataFileName = dataFileName;
        this.dataFilePath = dataFilePath;
        this.dataFileUpload = dataFileUpload;
        this.thumbnailFlag = thumbnailFlag;
        this.thumbnailFileName = thumbnailFileName;
        this.thumbnailFilePath = thumbnailFilePath;
        this.thumbnailFileUpload = thumbnailFileUpload;
    }
}
