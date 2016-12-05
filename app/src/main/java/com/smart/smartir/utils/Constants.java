package com.smart.smartir.utils;

public class Constants {
    /**
     * 日志级别
     * LogUtils.LEVEL_ALL:显示所有日志
     * LogUtils.LEVEL_OFF:不显示所有日志(关闭日志)
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final long PROTOCOLTIMEOUT = 5 * 60 * 1000;

    // SP filename
    public static final String SPFILENAME = "smartir_file";
    public static final String ISSETUPFINISH = "is_first_start";

    public static final class URlS {
        public static final String BASEURL = "http://188.188.4.100:8080/";
        public static final String IMAGEBASEURL = BASEURL + "image?name=";
    }

    public static final class REQ {

    }

    public static final class RESPONSE {

    }

    public static final class PAY {
        public static final int PAYTYPE_ZHIFUBAO = 1;
        public static final int PAYTYPE_UUPAY = 2;
        public static final int PAYTYPE_WEIXIN = 3;
    }
}
