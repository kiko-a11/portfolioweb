package jp.summit_tech.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import jp.summit_tech.conf.Conf;
import lombok.Data;

/**
 * Model for user-account data.
 *
 */
@Data
public class User {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message="{userId}")
    String id;

    @NotBlank
    String name;

    @NotBlank
    String authority;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message="{password}")
    String password;

    /**
     * Default constructor (default authority is normal)
     */
    public User() {
        this.authority = Conf.CONST_AUTHORITY;
    }

    /**
     * Constructor Used when only the id is known, such as when selecting one data.
     * @param id Id of this user
     */
    public User(String id) {
        this();
        this.id = id;
    }

    /**
     * Constructor to set all items.
     * @param id Id of this user
     * @param name The name of this user-account
     * @param password Password of this user-account
     */
    public User(String id, String name, String password) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
    }
}

