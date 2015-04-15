package com.nkdroid.sos;

/**
 * Created by Android on 18-03-2015.
 */
public class User {

    public String policename;

    public String address;

    public String mobile;

    public String latitude;

    public String longitude;
    public User(){}
    public User(String policename,
                String latitude, String longitude, String address, String mobile) {
        super();
        this.policename = policename;
        this.address = address;
        this.mobile = mobile;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isLogin=false;

    private String username;
    private String password;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
