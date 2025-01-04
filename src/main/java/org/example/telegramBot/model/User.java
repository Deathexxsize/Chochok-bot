package org.example.telegramBot.model;

public class User {
    private String username;
    private int chochok;

    // Конструктор
    public User(String username, int chochok) {;
        this.username = username;
        this.chochok = chochok;
    }


    public String getUsername() {
        return username;
    }

    public int getChochok() {
        return chochok;
    }
}

