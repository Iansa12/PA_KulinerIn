package com.papb.pa_kulinerin;

public class RestoModel {
    private String idresto;
    private String image;

    public RestoModel(String idresto, String image) {
        this.idresto = idresto;
        this.image = image;
    }

    public String getImage() {
        return image;
    }
    public String getIdresto() {
        return idresto;
    }

}

