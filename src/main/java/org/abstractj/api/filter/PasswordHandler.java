package org.abstractj.api.filter;

import org.abstractj.api.service.TokenService;
import org.abstractj.api.util.Configuration;

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

    public static final String TOKEN_ID_PARAM = "id";

    @Inject
    private TokenService tokenService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        Configuration.loadFilterConfig(config);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String tokenId = httpServletRequest.getParameter(TOKEN_ID_PARAM);

        String method = httpServletRequest.getMethod();

        if (tokenService.isValid(tokenId) && method.equalsIgnoreCase("GET")) {
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

}

