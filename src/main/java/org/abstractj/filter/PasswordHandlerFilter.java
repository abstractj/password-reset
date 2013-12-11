package org.abstractj.filter;

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

@WebFilter("/reset/*")
public class PasswordHandlerFilter implements Filter {

    private static Logger LOGGER = Logger.getLogger(PasswordHandlerFilter.class.getSimpleName());

    @Override
    public void init(FilterConfig config) throws ServletException {
        //TODO
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String tokenId = httpServletRequest.getParameter("id");

        LOGGER.info("Query string: " + tokenId);

        if (tokenId != null) {
            httpServletRequest.getRequestDispatcher("/reset/update.html")
            .forward(httpServletRequest, httpServletResponse);

        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
            return;
        }



    }

    @Override
    public void destroy() {
    }
}
