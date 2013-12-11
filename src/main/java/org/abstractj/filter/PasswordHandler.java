package org.abstractj.filter;

import org.abstractj.api.ExpirationTime;
import org.abstractj.model.Token;
import org.abstractj.service.PasswordService;
import org.abstractj.util.URLUtil;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebFilter({"/reset/*", "/forgot/*"})
public class PasswordHandler implements Filter {

    private static Logger LOGGER = Logger.getLogger(PasswordHandler.class.getSimpleName());

    @Inject
    private PasswordService passwordService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        //TODO
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String email = httpServletRequest.getParameter("email");
        String tokenId = httpServletRequest.getParameter("id");

        if (email != null) {
            tokenValidation(email);
        } else if (tokenId != null && passwordService.isValid(tokenId)) {
            resetPassword(httpServletRequest, httpServletResponse);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
            return;
        }
    }

    private void tokenValidation(String email) {
        Token token;

        //Here of course we need to validate the e-mail against the database or PicketLink
        if (passwordService.userExists(email)) {
            token = passwordService.generate(new ExpirationTime());
            passwordService.send(URLUtil.uri(token.getId()));
        }
    }

    private void resetPassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletRequest.getRequestDispatcher("/reset/update.html").forward(httpServletRequest, httpServletResponse);

        //When submited token must be destroyed
    }

    @Override
    public void destroy() {
    }

}

