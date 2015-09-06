package com.srivathsanvk.flickrnetwork;

import java.io.Serializable;

/**
 * Created by Srivathsan on 03-Sep-15.
 */
public class Photo implements Serializable {

    String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
