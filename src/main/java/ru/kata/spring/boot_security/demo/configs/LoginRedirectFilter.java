//package ru.kata.spring.boot_security.demo.configs;
//
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(urlPatterns = "/login")
//public class LoginRedirectFilter extends BasicAuthenticationFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        // Перенаправляем запросы на /login на кастомный путь /auth/login
//        response.sendRedirect("/auth/login");
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Ничего не нужно инициализировать
//    }
//
//    @Override
//    public void destroy() {
//        // Очистка ресурсов при уничтожении фильтра
//    }
//}
