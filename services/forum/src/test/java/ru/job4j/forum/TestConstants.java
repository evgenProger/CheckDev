package ru.checkdev.forum;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Constant values for tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public class TestConstants {

    public static final int OAUTH_SERVER_PORT = 9900;

    public static final String OAUTH_SERVER_PATH = "/user";

    public static final String OAUTH_USER_KEY = "testUserKey";

    public static final String OAUTH_USER_INFO = String.format(
            "{\"authorities\":[{\"authority\":\"ROLE_ADMIN\"}],\"principal\":{\"key\":\"%s\"}}",
            OAUTH_USER_KEY);

    public static final String OAUTH_TOKEN = "testToken";

    public static final int CATEGORY_ID = 1;

    public static final String CATEGORY_NAME = "testCategory";

    public static final String CATEGORY_DESCRIPTION = "testCategoryDescription";

    public static final String CATEGORY_NAME2 = "testCategory2";

    public static final String CATEGORY_DESCRIPTION2 = "testCategoryDescription2";

    public static final int SUBJECT_ID = 1;

    public static final String SUBJECT_NAME = "testSubject";

    public static final String SUBJECT_DESCRIPTION = "testSubjectDescription";

    public static final String SUBJECT_BRIEF = "testSubjectBrief";

    public static final String SUBJECT_NAME2 = "testSubject2";

    public static final String SUBJECT_DESCRIPTION2 = "testSubjectDescription2";

    public static final String SUBJECT_BRIEF2 = "testSubjectBrief2";

    public static final int MESSAGE_ID = 1;

    public static final String MESSAGE_TEXT = "testMessage";

    public static final String MESSAGE_TEXT2 = "testMessage2";

    public static final Pageable PAGEABLE = new PageRequest(0, 2);

    public static final String CATEGORY_JSON = String.format("{\"name\":\"%s\",\"description\":\"%s\"}",
            CATEGORY_NAME, CATEGORY_DESCRIPTION);

    public static final String CATEGORY_JSON2 = String.format("{\"name\":\"%s\",\"description\":\"%s\"}",
            CATEGORY_NAME2, CATEGORY_DESCRIPTION2);

    public static final String SUBJECT_JSON = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"brief\":\"%s\"}",
            SUBJECT_NAME, SUBJECT_DESCRIPTION, SUBJECT_BRIEF);

    public static final String SUBJECT_JSON2 = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"brief\":\"%s\"}",
            SUBJECT_NAME2, SUBJECT_DESCRIPTION2, SUBJECT_BRIEF2);

    public static final String MESSAGE_JSON = String.format("{\"text\":\"%s\"}", MESSAGE_TEXT);

    public static final String MESSAGE_JSON2 = String.format("{\"text\":\"%s\"}", MESSAGE_TEXT2);
}