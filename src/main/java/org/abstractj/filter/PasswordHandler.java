package org.abstractj.filter;

import org.abstractj.service.PasswordService;
import org.abstractj.util.Configuration;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PasswordHandler implements Filter {

    private static final String EMAIL_PARAM = "email";
    public static final String TOKEN_ID_PARAM = "id";

    @Inject
    private PasswordService passwordService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        Configuration.loadFilterConfig(config);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String email = httpServletRequest.getParameter(EMAIL_PARAM);
        String tokenId = httpServletRequest.getParameter(TOKEN_ID_PARAM);

        if (isNotEmpty(email)) {
            passwordService.send(email);
        } else if (isNotEmpty(tokenId) && passwordService.isValid(tokenId)) {
            redirectPage(httpServletRequest, httpServletResponse);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
            return;
        }
    }

    private void redirectPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletRequest.getRequestDispatcher(Configuration.getRedirectPage()).forward(httpServletRequest, httpServletResponse);

        //TODO When submited token must be destroyed
    }

    @Override
    public void destroy() {
    }


    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

}

