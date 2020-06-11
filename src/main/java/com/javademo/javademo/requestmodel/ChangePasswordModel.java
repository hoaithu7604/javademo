package com.javademo.javademo.requestmodel;

public class ChangePasswordModel {
    private String oldPassword;
    private String password;
    private String password2;

    public ChangePasswordModel(String oldPassword, String password, String password2) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.password2 = password2;
    }

    public ChangePasswordModel() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
