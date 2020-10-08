package jp.summit_tech.domain.form;

import javax.validation.Valid;

import jp.summit_tech.domain.model.UserSetting;
import lombok.Data;

/**
 * Form for the user-setting
 *
 */
@Data
public class UserSettingForm {
    @Valid
    UserSetting userSetting;
    String token;

    /**
     * Default constructor
     */
    public UserSettingForm() {
        this.userSetting = new UserSetting();
        this.token = "";
    }

    /**
     * Constructor to set all items.
     * @param userSetting user-setting data
     * @param token
     */
    public UserSettingForm(UserSetting userSetting, String token) {
        super();
        this.userSetting = userSetting;
        this.token = token;
    }
}
