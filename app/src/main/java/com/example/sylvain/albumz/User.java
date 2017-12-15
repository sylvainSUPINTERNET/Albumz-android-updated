package com.example.sylvain.albumz;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SYLVAIN on 11/11/2017.
 */

public class User {

    protected String name;
    protected String email;
    protected String password;
    protected String password_confirmed;
    protected List<Album> userAlbumz;

    protected FirebaseUser currentUser;

    //For create new User use this constructpr
    public User(String email, String name, String password, String password_confirmed ){
        this.name = name;
        this.email = email;
        this.password = password;
        this.password_confirmed = password_confirmed;
    }

    //If you need only utils method use this constructor
    public  User(){}


    //Constructor for login
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public String getPassword(){
        return this.password;
    }

    public void setConfirmedPassword(String newConfirmedPassword){
        this.password_confirmed = newConfirmedPassword;
    }

    public List<Album> getUserAlbumz() {
        return userAlbumz;
    }

    public void setUserAlbumz(List<Album> userAlbumz) {
        this.userAlbumz = userAlbumz;
    }

    public void addUserAlbumz(Album albumz){
        this.userAlbumz.add(albumz);
    }

    public String getConfirmedPassword(){
        return this.password_confirmed;
    }


}
