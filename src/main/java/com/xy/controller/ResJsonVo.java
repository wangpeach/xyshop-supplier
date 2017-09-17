package com.xy.controller;

public class ResJsonVo {
    private int code=0;

    private String msg="操作成功";

    private Object data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 操作失败设置
     */
    public void setOpeFail(){
        this.code=-1;
        this.msg="操作失败，稍后再试";
    }

    /**
     * 登录失效
     */
    public void setSessionOut(){
        this.code=-99;
        this.msg="未登录";
    }

    /**
     * 参数校验失败
     */
    public void setParaCheckFail(){
        this.code=-1;
        this.msg="参数校验失败，请检查发送的参数";
    }

    /**
     * 后台异常
     */
    public void setError(){
        this.code=-1;
        this.msg="服务异常，稍后再试";
    }

    /**
     * @param code
     * @param msg
     */
    public void setCodeAndMsg(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
}
