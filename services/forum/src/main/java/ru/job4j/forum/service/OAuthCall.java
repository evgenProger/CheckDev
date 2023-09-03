package ru.job4j.forum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import ru.job4j.forum.domain.User;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service to make OAuth2 calls to external microservice.
 *
 * @author LightStar
 * @since 07.06.2017
 */
@Component
public class OAuthCall {

    static {
        HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
    }
    /**
     * Object for JSON manipulating.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * URL of the auth microservice.
     */
    @Value("${server.auth}")
    private String authUrl;

    /**
     * Access key for auth microservice.
     */
    @Value("${access.auth}")
    private String authKey;

    /**
     * Retrieve map of users for given list of user keys.
     *
     * @param userKeys list of user keys.
     * @return map of retrieved users by keys.
     * @throws Exception thrown if data can't be retrieved.
     */
    @SuppressWarnings("unchecked")
    public Map<String, User> getUsersByKeys(final Set<String> userKeys)
            throws Exception {
        final String personsJson = this.doPost(null,
                String.format("%s/person/by?key=%s", this.authUrl, this.authKey),
                this.mapper.writeValueAsString(userKeys));
        final CollectionType personsListType = this.mapper.getTypeFactory().constructCollectionType(List.class,
                User.class);
        return Maps.uniqueIndex((List<User>) this.mapper.readValue(personsJson, personsListType), User::getKey);
    }

    /**
     * Perform <code>GET</code> call to external microservice.
     *
     * @param user authenticated user on behalf of which to perform request
     *             (can be null if authentication is not required).
     * @param url  target URL.
     * @return received data.
     * @throws Exception thrown if data can't be retrieved.
     */
    public String doGet(final Principal user, final String url) throws Exception {
        return this.call(user, "GET", url, null);
    }

    /**
     * Perform <code>POST</code> call to external microservice.
     *
     * @param user authenticated user on behalf of which to perform request
     *             (can be null if authentication is not required).
     * @param url  target URL.
     * @param data data sent in request's body.
     * @return received data.
     * @throws Exception thrown if request can't be made.
     */
    public String doPost(final Principal user, final String url, final String data) throws Exception {
        return this.call(user, "POST", url, data);
    }

    /**
     * Perform generic call to external microservice.
     *
     * @param user   authenticated user on behalf of which to perform request
     *               (can be null if authentication is not required).
     * @param method request's HTTP method.
     * @param url    target URL.
     * @param data   data sent in request's body.
     * @return received data.
     * @throws Exception thrown if request can't be made.
     */
    @SuppressWarnings("unchecked")
    private String call(final Principal user, final String method, final String url, final String data)
            throws Exception {
        final String access = user != null ? ((Map<String, String>) ((Map<String, Object>) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails())
                .get("details")).get("tokenValue") : null;
        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.addRequestProperty("User-Agent", "Mozilla/5.0");
        if (access != null) {
            con.addRequestProperty("Authorization", "Bearer " + access);
        }
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("charset", "utf-8");
        con.setUseCaches(false);
        if (data != null) {
            try (final DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(data.getBytes(StandardCharsets.UTF_8));
            }
        }
        final StringBuilder response = new StringBuilder();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            in.lines().forEach(response::append);
        }
        return response.toString();
    }
}