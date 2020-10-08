package jp.summit_tech.conf;

import org.springframework.stereotype.Component;

/**
 * Constant (Message, return code, path, etc.)
 *
 */
@Component
public class Conf {

    /**
     * Message : Duplicate exception for registration id
     */
    public static final String MSG_DUPLICATE = "このIDは既に使用されています。";

    /**
     * Message : Registration failed
     */
    public static final String MSG_REGIST_FAILED_VALID = "登録に失敗しました。入力内容を確認してください。";

    /**
     * Message : Other Exception
     */
    public static final String MSG_FAILED_OTHER = "不明なエラーのため、処理に失敗しました。申し訳ありませんが、管理者までご連絡ください。";

    /**
     * Message : Data not found
     */
    public static final String MSG_NOT_FOUND = "データが見つかりませんでした。";

    /**
     * Message : DB exception
     */
    public static final String MSG_DB_ERR = "データベース障害が発生しています。申し訳ありませんが、管理者までご連絡ください。";

    /**
     * Message : file exception
     */
    public static final String MSG_FILE_ERR = "ファイルのアップロードに失敗しました。";

    /**
     * Constant : Default authority (the only one right now)
     */
    public static final String CONST_AUTHORITY = "normal";

    /**
     * Constant : Path to put files
     */
  public static final String CONST_PUT_FILE_PATH = "/portfoliowebfile/";

    /**
     * Constant : Path to get files
     */
public static final String CONST_GET_FILE_PATH = "/resources/";

}