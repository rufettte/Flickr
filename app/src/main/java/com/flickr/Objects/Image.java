package com.flickr.Objects;

import android.text.Html;

import java.io.Serializable;


/*
* This class aims to hold the information of each Image, loaded as the result of the search operation.
*
* Note 1: We are using objectmapper, to easily map the json object data into this object.
  Therefore, we are using the names of the variables similar as the parameter names of the json object.

* Note 2: This class implements Serializable interface, because this makes it possible to pass the whole object
  from one activity to another; i.e., from SearchableActivity to ImageViewerActivity.

* */
public class Image implements Serializable {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private int ispublic;
    private int isfriend;
    private int isfamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return Html.fromHtml(title).toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }


    /*
    * This function creates the url of the image so as to be able to present it
    * to the user through it.
    * */
    public String getUrl() {
        return "https://farm"
                + String.valueOf(getFarm())
                + ".static.flickr.com/"
                + getServer()
                + "/"
                + getId()
                + "_"
                + getSecret()
                + ".jpg";
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", secret='" + secret + '\'' +
                ", server='" + server + '\'' +
                ", farm=" + farm +
                ", title='" + title + '\'' +
                ", ispublic=" + ispublic +
                ", isfriend=" + isfriend +
                ", isfamily=" + isfamily +
                '}';
    }
}
