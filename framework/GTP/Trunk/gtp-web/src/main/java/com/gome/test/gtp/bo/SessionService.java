package com.gome.test.gtp.bo;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lizonglin on 2015/6/4.
 */
@Service
public class SessionService {
    private HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getSessionUsername () {
        return request.getSession().getAttribute("userName").toString();
    }

    public boolean getSessionIsAdmin () {
        if (request.getSession().getAttribute("isAdmin") != null) {
            return Boolean.valueOf(request.getSession().getAttribute("isAdmin").toString());
        } else {
            return false;
        }
    }

    public boolean getSessionLogin () {
        if (request.getSession().getAttribute("login") != null) {
            return Boolean.valueOf(request.getSession().getAttribute("login").toString());
        } else {
            return false;
        }
    }

    public String getSessionGroup () {
        return request.getSession().getAttribute("group").toString();
    }

    public String getSessionEmail () {
        return request.getSession().getAttribute("email").toString();
    }

    public void setSessionUsername (String username) {
        request.getSession().setAttribute("userName", username);
    }

    public void setSessionIsAdmin (boolean isAdmin) {
        request.getSession().setAttribute("isAdmin", isAdmin);
    }

    public void setSessionLogin (boolean login) {
        request.getSession().setAttribute("login", login);
    }

    public void setSessionGroup (String group) {
        request.getSession().setAttribute("group", group);
    }

    public void setSessionEmail (String email) {
        request.getSession().setAttribute("email", email);
    }

    public void setSessionTimeOut (int timeOut) {
        request.getSession().setMaxInactiveInterval(timeOut);
    }
}
