package com.jiuchunjiaoyu.micro.aggregate.wzb.constant;

import com.jiuchunjiaoyu.micro.aggregate.wzb.util.ConfigUtil;

/**
 * 微智宝常量信息
 */
public class WzbConstant {

    public static final String VALIDATE_TOKEN_NAME = "validateToken";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String GZH_APP_ID = ConfigUtil.getProperty("gzh.app.id");//"wxe9de68a39a2d239f";
    public static final String GZH_APP_SECRET = "9db814aac7d9ec00d497ef5128ce203e";
    public static final String PC_DEVICE_INFO = "WEB";

    public static final String NOTIFY_URL = "https://dev-wzb.jcweixiaoyuan.cn/wzb/wx/callBack";

    public static final String TRADE_TYPE = "JSAPI";

    public static final String WX_RETURN_SUCCESS = "SUCCESS";

    public static final String WX_RETURN_FAIL = "FAIL";

    public static final String WZB_DATA_WRITE_SECRET = "QsnFjwJrZmbRYsjHhrhL";
    public static final String WZB_DATA_WRITE_CLIENTID = "7dde57b203714c269883a3a3205c0b26";

    public static final String WX_AUTH_NAME="wx_auth";

    public static final String SESSION_USER_NAME = "user";

    public static final String TOKEN_FOR_WX="jiuchun123";

    public static final String WX_TRADE_SUCCESS = "SUCCESS";

    public static final String WX_TRADE_REFUND = "REFUND";
    public static final String WX_TRADE_NOTPAY = "NOTPAY";
    public static final String WX_TRADE_CLOSED = "CLOSED";
    public static final String WX_TRADE_REVOKED= "REVOKED";
    public static final String WX_TRADE_USERPAYING = "USERPAYING";
    public static final String WX_TRADE_PAYERROR= "PAYERROR";

    public static final String REDIRECT_URL_IN_SESSION="REDIRECT_URL_IN_SESSION";


}
