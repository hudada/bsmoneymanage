package com.example.bsproperty.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.103";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String NOTICE = "/notice";
    private static final String USER = "/user";
    private static final String RECORD = "/record";
    private static final String FORUM = "/forum";


    public static final String HOME_LIST = HOST + API + NOTICE + "/";
    public static final String REGISTER = HOST + API + USER + "/";
    public static final String LOGIN = HOST + API + USER + "/login";
    public static final String EDITUSER = HOST + API + USER + "/editUser";
    public static final String EDITPASS = HOST + API + USER + "/editPass";
    public static final String RECORD_ADDMONEY = HOST + API + USER + "/addMoney/";
    public static final String RECORD_LIST = HOST + API + RECORD + "/";
    public static final String RECORD_COMMIT = HOST + API + RECORD + "/editState/";
    public static final String FORUM_LIST = HOST + API + FORUM + "/";
    public static final String FORUM_DETAIL_LIST = HOST + API + FORUM + "/";
    public static final String POST_REPLY = HOST + API + FORUM + "/";
    public static final String POST_FORUM = HOST + API + FORUM + "/add";
}
