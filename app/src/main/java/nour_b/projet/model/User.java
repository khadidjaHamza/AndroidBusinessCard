package nour_b.projet.model;

import android.content.Context;

import nour_b.projet.R;

public class User {

    private String mail;
    private String password;
    private String name;
    private String surname;
    private String birth;
    private String address;
    private String tel1;
    private String tel2;
    private String website;
    private String photo;
    private Context ctx;

    public User(Context ctxt) {
        this.ctx = ctx;
    }
    public User() {}
    public User(String mail, String password, String name, String surname) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
    public Context getContext(){ return this.ctx; }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String toString(Context ctxt) {
        String userInfo = "BusinessCard \n";

        if (getName() != null) {
            userInfo += getName() + "\n";
        }
        if (getSurname() != null) {
            userInfo += getSurname() + "\n";
        }
        if (getMail() != null) {
            userInfo += getMail() + "\n";
        }
        if (getAddress() != null) {
            userInfo += ctxt.getString(R.string.address) + " : " + getAddress() + "\n";
        }
        if (getTel1() != null) {
            userInfo += ctxt.getString(R.string.tel1) + " : " + getTel1() + "\n";
        }
        if (getTel2() != null) {
            userInfo += ctxt.getString(R.string.tel2) + " : " + getTel2() + "\n";
        }
        if (getWebsite() != null) {
            userInfo += ctxt.getString(R.string.site) + " : " + getWebsite() + "\n";
        }

        return userInfo;
    }
}
