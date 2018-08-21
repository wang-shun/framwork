/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author Zonglin.Li
 */
public class AdminFilter implements Filter{
    @Override
    public void destroy() {
    }
    
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpSession session = request.getSession();
        Object login = session.getAttribute("login");
        Object admin = session.getAttribute("isAdmin");
        boolean isAdmin = false;
        if (admin != null) {
            isAdmin = Boolean.valueOf(admin.toString());
        }
        if (login == null || login.equals("") || admin == null || isAdmin == false || admin.equals("")) {
            request.getRequestDispatcher("/login").forward(arg0, arg1);
        } else {
            arg2.doFilter(arg0, arg1);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) {
    }
}
