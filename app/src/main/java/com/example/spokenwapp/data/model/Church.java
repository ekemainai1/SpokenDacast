package com.example.spokenwapp.data.model;

import javax.inject.Inject;

public class Church {


    int churchLogo;
    String churchName;

    @Inject
    public Church(int churchLogo, String churchName) {
        this.churchLogo = churchLogo;
        this.churchName = churchName;
    }

    public int getChurchLogo() {
        return churchLogo;
    }

    public void setChurchLogo(int churchLogo) {
        this.churchLogo = churchLogo;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }
}
