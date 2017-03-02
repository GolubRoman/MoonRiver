package com.golubroman.golub.weatherv201.Settings;


public class ElementSettingsListView {
    private String title, subtitle;

    public ElementSettingsListView(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() { return title; }

    public String getSubtitle() { return subtitle;  }

    public void setSubtitle(String s){ subtitle = s; }

}
