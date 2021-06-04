package com.papb.pa_kulinerin;

public class MenuModel {

    private String nama;
    private String image;
    private int harga;

    public MenuModel(String nama, String image, int harga) {
        this.nama = nama;
        this.image = image;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }
    public String getImage() {
        return image;
    }
    public int getHarga(){ return  harga;}

}
