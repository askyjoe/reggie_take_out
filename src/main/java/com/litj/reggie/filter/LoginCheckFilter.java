package com.litj.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.litj.reggie.common.BaseContext;
import com.litj.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(urls, requestURI);

        if(check) {
            filterChain.doFilter(request, response);
        }else if (request.getSession().getAttribute("employee") != null) {
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
        } else if (request.getSession().getAttribute("user") != null) {
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
        } else {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        }

    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = matcher.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
