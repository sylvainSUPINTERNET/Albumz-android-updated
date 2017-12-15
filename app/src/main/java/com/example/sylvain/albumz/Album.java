package com.example.sylvain.albumz;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by michaelguerfi on 20/11/2017.
 */

public class Album {

    protected String albumName;
    protected User user;
    protected boolean publicAlbum;

    public Album(String albumName, User user, boolean publicAlbum) {
        this.albumName = albumName;
        this.user = user;
        this.publicAlbum = publicAlbum;
    }
    public Album(){}

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPublicAlbum() {
        return publicAlbum;
    }

    public void setPublicAlbum(boolean publicAlbum) {
        this.publicAlbum = publicAlbum;
    }


    public Map<String, String> isValideAlbum(String albumName){
        Map<String, String> validation_result = new HashMap<String, String>();

        //default, error == "no"
        validation_result.put("error", "no");

        if(albumName.length() < 3){
            validation_result.put("name", "Your name is too short");
            validation_result.put("error", "yes");
        }

        return validation_result;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
