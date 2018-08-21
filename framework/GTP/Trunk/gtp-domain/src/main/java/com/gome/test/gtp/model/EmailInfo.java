package com.gome.test.gtp.model;

/**
 * Created by lizonglin on 2015/9/2/0002.
 */
public class EmailInfo {
    private String username;
    private String password;
    private String serverHost;
    private String subject;
    private String mail_head_info;
    private String mail_head_value;
    private String mail_from;
    private String personalName;
    private String toEmail1;
    private String toEmail2;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMail_head_info() {
        return mail_head_info;
    }

    public void setMail_head_info(String mail_head_info) {
        this.mail_head_info = mail_head_info;
    }

    public String getMail_head_value() {
        return mail_head_value;
    }

    public void setMail_head_value(String mail_head_value) {
        this.mail_head_value = mail_head_value;
    }

    public String getMail_from() {
        return mail_from;
    }

    public void setMail_from(String mail_form) {
        this.mail_from = mail_form;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public String getToEmail1() {
        return toEmail1;
    }

    public void setToEmail1(String toEmail1) {
        this.toEmail1 = toEmail1;
    }

    public String getToEmail2() {
        return toEmail2;
    }

    public void setToEmail2(String toEmail2) {
        this.toEmail2 = toEmail2;
    }
}
