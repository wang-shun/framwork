/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.filter;

import com.gome.test.gtp.utils.Util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Zonglin.Li
 */
public class SimpleFilter implements Filter{
    @Override
    public void destroy() {
    }
    
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpSession session = request.getSession();
        Object s = session.getAttribute("login");
        String path = request.getServletPath();
        Set<String> allowPathSet = new HashSet<String>();
        //未登录可访问的路径
//        allowPathSet.add("/task");
//        allowPathSet.add("/task/all");
//        allowPathSet.add("/task/details/*");
//        allowPathSet.add("/task/report/*");
//        allowPathSet.add("/task/report/list/*");
//        allowPathSet.add("/task/status");
//        allowPathSet.add("/task/search/*");
//        allowPathSet.add("/testResult/detail/*");
//        allowPathSet.add("/branch");
//        allowPathSet.add("/branch/all");
//        allowPathSet.add("/branch/detail/*");
//        allowPathSet.add("/branch/search/*");
//        allowPathSet.add("/regularRule");
//        allowPathSet.add("/regularRule/all");
//        allowPathSet.add("/machine");
//        allowPathSet.add("/machine/all");
//        allowPathSet.add("/hosts");
//        allowPathSet.add("/hosts/all");
//        allowPathSet.add("/hosts/detail/*");
//        allowPathSet.add("/suggestion");
//        allowPathSet.add("/suggestion/all");
//        allowPathSet.add("/load/scene");
//        allowPathSet.add("/load/scene/all");
//        allowPathSet.add("/load/conf");
//        allowPathSet.add("/load/conf/all");
//        allowPathSet.add("/load/conf/detail/*");
        if ((s == null || s.equals("")) && !containPath(allowPathSet, path)) {
            request.getRequestDispatcher("/login").forward(arg0, arg1);
        } else {
            arg2.doFilter(arg0, arg1);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) {
    }

    private boolean containPath(Set<String> allowPathSet, String url) {
        for(String path : allowPathSet) {
            if(Util.matchPath(path, url)) {
                return true;
            }
        }
        return false;
    }
}

