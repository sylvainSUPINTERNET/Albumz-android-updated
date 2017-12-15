package com.example.sylvain.albumz;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michaelguerfi on 28/11/2017.
 */

public class FirebaseUtils {

    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    Map<String, String> dictionary = new HashMap<String, String>();


    public Map<String, String>  isValideRegister(String email, String name, String password, String password_confirmed){
        Map<String, String> validation_result = new HashMap<String, String>();

        //default, error == "no"
        validation_result.put("error", "no");

        if(name.length() < 3){
            validation_result.put("name", "Your name is too short");
            validation_result.put("error", "yes");
        }

        if(password.length() < 6){

            validation_result.put("password", "Your password is too short ");
            validation_result.put("error", "yes");
        }
        if(password_confirmed == ""){
            validation_result.put("password_confirmed", "Your password dosnt correspond");
            validation_result.put("error", "yes");
            if(password.equals(password_confirmed)){
                validation_result.put("password_confirmed", "Your password dosnt correspond");
                validation_result.put("error", "yes");
            }
        }


        return validation_result;
    }


    public Map<String, String>  isValideLogin(String email, String password){
        Map<String, String> validation_result = new HashMap<String, String>();

        //default, error == "no"
        validation_result.put("error", "no");

        if(password.length() < 6){

            validation_result.put("password", "Your password is too short ");
            validation_result.put("error", "yes");
        }

        return validation_result;
    }

    public static void listenToPublicAlbumz(final OnAlbumzRetrieved listener){
        FirebaseDatabase.getInstance().getReference("albumz/public").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.onAlbumzAdded(dataSnapshot.getValue(Album.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface OnAlbumzRetrieved{
        void onAlbumzAdded(Album album);
    }

    static void retrieveUser(final OnUserRetrieved listener){
        FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.onUserRetrieved(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public interface OnUserRetrieved{
        void onUserRetrieved(User user);
    }

    //Utils methods

}
