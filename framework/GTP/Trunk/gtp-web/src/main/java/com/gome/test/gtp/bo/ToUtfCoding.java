/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gome.test.gtp.bo;

import org.springframework.stereotype.Service;

/**
 *
 * @author Zonglin.Li
 */
@Service
public class ToUtfCoding {
    public String toUtfCoding(String inputStr){
        
        String outputStr = "";
        try {
            outputStr = new String(inputStr.getBytes("ISO_8859_1"),"UTF-8");
            
        } catch (Exception e) {
        }
        return outputStr;
    }
}
