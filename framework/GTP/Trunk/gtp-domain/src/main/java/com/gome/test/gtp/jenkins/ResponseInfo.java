package com.gome.test.gtp.jenkins;

/**
 * Created by lizonglin on 2015/12/7/0007.
 */
public class ResponseInfo {
    int httpCode;
    String response;

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
