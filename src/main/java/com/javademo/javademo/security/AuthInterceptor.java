package com.javademo.javademo.security;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    // preHandle sẽ được gọi khi có request (config request url ở dưới)
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        Auth roleAnnotation = AnnotationUtils
                .findAnnotation(method, Auth.class);
        Auth.Role role = roleAnnotation != null ? roleAnnotation.role() : null;

        HttpSession session = request.getSession();
        boolean isLogin = session.getAttribute("isLogin") != null ? (Boolean) session
                .getAttribute("isLogin") : false;
        Auth.Role loginRole = session.getAttribute("role") != null ? (Auth.Role) session
                .getAttribute("role") : null;

        if (role != null) {
            if (!isLogin) {
                response.sendRedirect("/login");
                return false;
            } else {
                if (role != Auth.Role.LOGIN && role != loginRole) {
                    response.sendRedirect("deny");
                    return false;
                }
            }
        }

        return true;
    }
}