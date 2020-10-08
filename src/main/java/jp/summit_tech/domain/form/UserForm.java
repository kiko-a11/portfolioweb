package jp.summit_tech.domain.form;

import javax.validation.Valid;

import jp.summit_tech.domain.model.User;
import lombok.Data;

/**
 * Form for the user-account
 *
 */
@Data
public class UserForm {
    @Valid
    User user;
    String token;

    /**
     * Default constructor
     */
    public UserForm() {
        this.user = new User();
        this.token = "";
    }

    /**
     * Constructor to set all items.
     * @param user user-account data
     * @param token (not used now)
     */
    public UserForm(User user, String token) {
        super();
        this.user = user;
        this.token = token;
    }
}
