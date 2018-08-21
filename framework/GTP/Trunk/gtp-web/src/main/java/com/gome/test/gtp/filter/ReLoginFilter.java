package com.gome.test.gtp.filter;

/**
 * Created by lizonglin on 2015/6/5.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ReLoginFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpSession session = request.getSession();
        Object login = session.getAttribute("login");
        boolean isLogin = false;
        if (login != null) {
            isLogin = Boolean.valueOf(login.toString());
        }
        if (isLogin){
            request.getRequestDispatcher("/home").forward(arg0, arg1);
        } else {
            arg2.doFilter(arg0, arg1);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
}

