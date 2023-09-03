package ru.job4j.forum.web;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.job4j.forum.TestConstants;
import ru.job4j.forum.domain.Category;
import ru.job4j.forum.domain.Message;
import ru.job4j.forum.domain.Subject;
import ru.job4j.forum.service.CategoryService;
import ru.job4j.forum.service.MessageService;
import ru.job4j.forum.service.SubjectService;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * <code>ControllerTest</code> class.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTest extends Mockito {

    /**
     * Auto-created mocked Spring MVC infrastructure.
     */
    @Autowired
    protected MockMvc mvc;

    /**
     * Mocked category service bean.
     */
    @MockBean
    protected CategoryService categoryService;

    /**
     * Mocked subject service bean.
     */
    @MockBean
    protected SubjectService subjectService;

    /**
     * Mocked message service bean.
     */
    @MockBean
    protected MessageService messageService;

    /**
     * <code>Category</code> object used in tests.
     */
    protected final Category category = new Category();
    {
        this.category.setName(TestConstants.CATEGORY_NAME);
        this.category.setDescription(TestConstants.CATEGORY_DESCRIPTION);
    }

    /**
     * Another <code>Category</code> object used in tests.
     */
    protected final Category category2 = new Category();
    {
        this.category2.setName(TestConstants.CATEGORY_NAME2);
        this.category2.setDescription(TestConstants.CATEGORY_DESCRIPTION2);
    }

    /**
     * <code>Subject</code> object used in tests.
     */
    protected final Subject subject = new Subject();
    {
        this.subject.setName(TestConstants.SUBJECT_NAME);
        this.subject.setDescription(TestConstants.SUBJECT_DESCRIPTION);
        this.subject.setBrief(TestConstants.SUBJECT_BRIEF);
    }

    /**
     * Another <code>Subject</code> object used in tests.
     */
    protected final Subject subject2 = new Subject();
    {
        this.subject2.setName(TestConstants.SUBJECT_NAME2);
        this.subject2.setDescription(TestConstants.SUBJECT_DESCRIPTION2);
        this.subject2.setBrief(TestConstants.SUBJECT_BRIEF2);
    }

    /**
     * <code>Message</code> object used in tests.
     */
    protected final Message message = new Message();
    {
        this.message.setText(TestConstants.MESSAGE_TEXT);
    }

    /**
     * Another <code>Message</code> object used in tests.
     */
    protected final Message message2 = new Message();
    {
        this.message2.setText(TestConstants.MESSAGE_TEXT2);
    }

    /**
     * Rule for mocked OAUTH2 authorization server.
     */
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(TestConstants.OAUTH_SERVER_PORT));

    /**
     * Init mocked OAUTH2 authorization server.
     */
    @Before
    public void init() {
        stubFor(WireMock.get(urlPathEqualTo(TestConstants.OAUTH_SERVER_PATH))
                .withHeader("Authorization", equalTo(String.format("Bearer %s", TestConstants.OAUTH_TOKEN)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(TestConstants.OAUTH_USER_INFO)));
    }

    /**
     * Make a mock get request with response type set as JSON.
     *
     * @param path request path.
     * @return request builder.
     */
    protected MockHttpServletRequestBuilder getJson(final String path) {
        return get(path)
                .accept(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * Make a mock post request with request and response types set as JSON.
     *
     * @param path request path.
     * @return request builder.
     */
    protected MockHttpServletRequestBuilder postJson(final String path) {
        return post(path)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * Make a mock put request with request and response types set as JSON.
     *
     * @param path request path.
     * @return request builder.
     */
    protected MockHttpServletRequestBuilder putJson(final String path) {
        return put(path)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * Make a mock request properly authorized.
     *
     * @param builder request builder.
     * @return request builder.
     */
    protected MockHttpServletRequestBuilder makeAuthorized(final MockHttpServletRequestBuilder builder) {
        return builder.header("Authorization", String.format("Bearer %s", TestConstants.OAUTH_TOKEN));
    }
}