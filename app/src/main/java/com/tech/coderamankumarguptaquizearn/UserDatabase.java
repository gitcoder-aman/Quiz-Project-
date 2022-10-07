package com.tech.coderamankumarguptaquizearn;

public class UserDatabase {
    private String name,email,pass,referCode,uid,profile = "https://www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png";
    private long coins = 0;
    private int spinCount = 0;
    private int spinCountTill = 20;
    private int QCoins = 20;

    public UserDatabase() {  //for firebase
    }

    public UserDatabase(String name, String email, String pass, String referCode,String uid ) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.referCode = referCode;
        this.uid = uid;
    }

    public int getQCoins() {
        return QCoins;
    }

    public void setQCoins(int QCoins) {
        this.QCoins = QCoins;
    }

    public int getSpinCountTill() {
        return spinCountTill;
    }

    public void setSpinCountTill(int spinCountTill) {
        this.spinCountTill = spinCountTill;
    }

    public int getSpinCount() {
        return spinCount;
    }

    public void setSpinCount(int spinCount) {
        this.spinCount = spinCount;
    }

    public String getUid(){
        return uid;
    }
    public  void setUid(){
        this.uid = uid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
