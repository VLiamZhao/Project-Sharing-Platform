package com.psp.filter;

import com.psp.model.User;
import com.psp.service.JWTService;
import com.psp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
@WebFilter(filterName = "securityFilter", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST})
public class SecurityFilter implements Filter {
    @Autowired
    JWTService jwtService;
    @Autowired
    UserService userService;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final Set<String> IGNORED_PATHS = new HashSet<>(Arrays.asList("/auth", "/auth/registration", "/projects/all", "/projects", "/enterprises/ratestar"));


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (userService == null) {
            SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, request.getServletContext());
        }
        HttpServletRequest req = (HttpServletRequest)request;

        int statusCode = authorization(req);
        if (statusCode == HttpServletResponse.SC_ACCEPTED) {
            filterChain.doFilter(request, response);
        } else {
            System.out.println(statusCode);
            if (statusCode == 200) {
                ((HttpServletResponse)response).setStatus(statusCode);
            } else {
                ((HttpServletResponse)response).sendError(statusCode);
            }

        }
    }

    private int authorization(HttpServletRequest req) {
        HttpSession session = req.getSession();
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        String uri = req.getRequestURI();
        String verb = req.getMethod();
        if ("OPTIONS".equals(req.getMethod())) {
            return HttpServletResponse.SC_ACCEPTED;
        }
        System.out.println("1123");
        if (IGNORED_PATHS.contains(uri)) return HttpServletResponse.SC_ACCEPTED;
            String header = req.getHeader("Authorization");
            if (header == null || header.isEmpty()) return statusCode;
            String token = header.replaceAll("^(.*?) ", "");
            if (token == null || token.isEmpty()) return statusCode;
            try {
                Claims claims = jwtService.decodeJwtToken(token);

            if (claims.getId() != null) {
                User user = userService.getUserById(Long.valueOf(claims.getId()));

                if (user == null) {
                    return HttpServletResponse.SC_BAD_REQUEST;
                } else {
                    session.setAttribute("curUser", user.getId());
                }
            }
            String allowedResources = "/";
            switch(verb) {
                case "GET"    : allowedResources = (String)claims.get("allowedReadResources");   break;
                case "POST"   : allowedResources = (String)claims.get("allowedCreateResources"); break;
                case "PUT"    : allowedResources = (String)claims.get("allowedUpdateResources"); break;
                case "DELETE" : allowedResources = (String)claims.get("allowedDeleteResources"); break;
            }
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            for (String s : allowedResources.split(",")) {
                if (uri.trim().toLowerCase().startsWith(s.trim().toLowerCase())) {
                    statusCode = HttpServletResponse.SC_ACCEPTED;
                    break;
                }
            }

            logger.debug(String.format("Verb: %s, allowed resources: %s", verb, allowedResources));

        }catch (Exception e){
            e.printStackTrace();
            return statusCode;
        }
        return statusCode;
    }
}