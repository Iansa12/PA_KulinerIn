package com.papb.pa_kulinerin;

import java.io.Serializable;

public class RegisterUserDB implements Serializable {
    private String username;
    private String nohp;
    private String email;
    private String image;

    public RegisterUserDB(String username, String nohp, String email, String image) {
        this.username = username;
        this.nohp = nohp;
        this.email = email;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String noHp) {
        this.nohp = noHp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString(){
        return getUsername() + "\n" + getNohp() + "\n" + getEmail() + "\n" + getImage() +"\n";
    }
}
