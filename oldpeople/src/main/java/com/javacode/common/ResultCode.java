package com.javacode.common;

/**
 *统一返回码
 */
public enum ResultCode {
    //定义了一个枚举类，枚举类有一个构造函数，传入code和msg之后会赋值，得到一个json结构的返回结果
    SUCCESS("0","成功"),
    ERROR("-1","系统异常"),
    PARAM_ERROR("1001","参数异常"),
    USER_EXIST_ERROR("2001","用户名已存在"),
    USER_EXIST_PHONE_ERROR("2004","用户手机已注册"),
    USER_ACCOUNT_ERROR("2002","账号或密码错误"),
    USER_NOT_EXIST_ERROR("2003","未找到用户"),
    FAMILY_EXIST_ERROR("2005","家属已注册");
    public String code;
    public String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
