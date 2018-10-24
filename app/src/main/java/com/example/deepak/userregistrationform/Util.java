package com.example.deepak.userregistrationform;

import android.net.Uri;

public class Util {
    public static final String DB_NAME="User.db";
    public static final int DB_VERSION=1;

    public static final String TAB_NAME="User";
    public static final String COL_ID="_ID";
    public static final String COL_NAME="NAME";
    public static final String COL_EMAIL="EMAIL";
    public static final String COL_DATE="CDATE";
    public static final String COL_GENDER="GENDER";
    public static final String COL_CITY="CITY";
    public static final String CREATE_TAB_QUERY="create table User("+
            "_ID integer primary key autoincrement,"+
            "NAME varchar(256),"+
            "EMAIL varchar(256),"+
            "CDATE varchar(20),"+
            "GENDER varchar(256),"+
            "CITY varchar(256)"+
            ")";
    public static final Uri USER_URI= Uri.parse("content://a.b.c.d/"+TAB_NAME);



}
