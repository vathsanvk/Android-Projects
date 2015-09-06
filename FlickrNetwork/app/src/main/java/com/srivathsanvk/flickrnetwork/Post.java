package com.srivathsanvk.flickrnetwork;

import java.io.Serializable;

/**
 * Created by Srivathsan on 03-Sep-15.
 */
public class Post implements Serializable{
    String userFullName, photoLink;

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
