package com.ytx.erp.base;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：
 */

public class ErrorBodyDTO {
    private String errCode;
    private String errMsg;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
