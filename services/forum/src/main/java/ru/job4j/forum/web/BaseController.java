package ru.job4j.forum.web;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * Base class for all forum controllers with common methods.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public abstract class BaseController {

    /**
     * Check access of provided user to resource with provided owner's key.
     *
     * @param servletRequest servlet's request.
     * @param user currently authenticated user data.
     * @param ownerKey resource owner's key.
     * @throws AccessDeniedException thrown if user doesn't have access to resource.
     */
    protected boolean checkAccessToResource(final HttpServletRequest servletRequest, final Principal user,
                                            final String ownerKey) throws AccessDeniedException {
        boolean result = true;
        final String userKey = this.getUserKey(user);
        if (!(userKey.equals(ownerKey) || servletRequest.isUserInRole("ROLE_ADMIN"))) {
            result = false;
        }
        return result;
    }

    /**
     * Get key of currently authenticated user.
     *
     * @param user currently authenticated user data.
     * @return user's key.
     */
    protected String getUserKey(final Principal user) {
        return user != null ? (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key") : "";
    }
}