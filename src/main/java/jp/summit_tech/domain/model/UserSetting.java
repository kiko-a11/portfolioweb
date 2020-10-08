package jp.summit_tech.domain.model;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * Model for user-account data. (Related to one user account)
 *
 */
@Data
public class UserSetting {

    long id;
    String userId;
    @NotBlank
    String name;
    boolean nameFlag;
    String sex;
    boolean sexFlag;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String birthday;
    boolean birthdayFlag;
    String area;
    boolean areaFlag;
    String specialty;
    String qualification;
    String pr;

    /**
     * Default constructor (default id is -1 and sex is other)
     */
    public UserSetting() {
        this.id = -1;
        this.sex = "999";
    }
    /**
     * Constructor Used when only the user-id is known, such as when selecting data.
     * @param userId Id of this user
     */
    public UserSetting(String userId) {
        this();
        this.userId = userId;
    }

    /**
     * Constructor to set all items.
     * @param id Id of this user-setting
     * @param userId Id of this user
     * @param name The name of this user
     * @param nameFlag If this flag is true, then name is published on the publish-page.
     * @param sex Man, woman, etc.
     * @param sexFlag If this flag is true, then sex is published on the publish-page.
     * @param birthday Birthday of this user (used for calculation of age)
     * @param birthdayFlag If this flag is true, then birthday and age is published on the publish-page.
     * @param area Area of ​​activity of this user
     * @param areaFlag If this flag is true, then area is published on the publish-page.
     * @param specialty This User's special skill
     * @param qualification Qualifications held by this user
     * @param pr Sales point of this user
     */
    public UserSetting(long id, String userId, String name, boolean nameFlag, String sex, boolean sexFlag,
            String birthday, boolean birthdayFlag, String area, boolean areaFlag, String specialty,
            String qualification, String pr) {
        super();
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.nameFlag = nameFlag;
        this.sex = sex;
        this.sexFlag = sexFlag;
        this.birthday = birthday;
        this.birthdayFlag = birthdayFlag;
        this.area = area;
        this.areaFlag = areaFlag;
        this.specialty = specialty;
        this.qualification = qualification;
        this.pr = pr;
    }

}
