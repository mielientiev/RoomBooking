package com.roombooking.auth;

import com.roombooking.entity.User;
import com.roombooking.utils.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AuthenticationPollingFilter implements Filter {

    @Autowired
    private AuthorizationUtil authorizationUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(filterConfig.getServletContext());
        AutowireCapableBeanFactory ctx = context.getAutowireCapableBeanFactory();
        ctx.autowireBean(this);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String authorization = req.getHeader("Authorization");
        User user = authorizationUtil.userAuthorization(authorization);
        if (user == null) {
            resp.setStatus(401);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
