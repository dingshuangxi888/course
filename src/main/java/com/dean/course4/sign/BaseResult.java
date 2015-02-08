package com.dean.course4.sign;

/**
 * Created by dean on 14/12/28.
 */
public class BaseResult {

    private String resultCode;

    private String resultMessage;

    public String isResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
