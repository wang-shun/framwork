package com.gome.test.gtp.model;

/**
 * Created by zhangjiadi on 16/2/3.
 */
public enum JMTEditType {

    JMTEditSceneName(1,"edit SceneName"),

    JMTEditEnable(2,"edit Enable");

    private  JMTEditType(int key,String value)
    {
        this.editTypekey=key;
        this.editTypeValue=value;
    }

    private int editTypekey;
    private String editTypeValue;

    public int getEditTypekey() {
        return editTypekey;
    }

    public void setEditTypekey(int editTypekey) {
        editTypekey = editTypekey;
    }

    public String getEditTypeValue() {
        return editTypeValue;
    }

    public void setEditTypeValue(String editTypeValue) {
        editTypeValue = editTypeValue;
    }


    public static JMTEditType getValue(int key)
    {
        switch (key) {
            case 1:
                return JMTEditSceneName;
            case 2:
                return JMTEditEnable;

            default:
                return null;
        }

    }
}
