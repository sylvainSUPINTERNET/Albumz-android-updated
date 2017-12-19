package com.example.sylvain.albumz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.UUID;

/**
 * Created by michaelguerfi on 21/11/2017.
 */

public class AlbumCreationFragment extends Fragment implements View.OnClickListener {


    private FirebaseAuth mAuth;

    //User Auth via FireBase
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser currentUser;


    //UI creation
    EditText albumName;
    Switch publicSwitch;
    Button album_submit;
    User userConnected;
    //User.Class
    Album Album_utils = new Album(); //constructor only to get Utils methods (clear, isExist etc )

    //user try to auth
    User userToAuthRegister;
    User userToAuthLogin;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.album_creation, container, false); //get current layout to get UI element

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUtils.retrieveUser(new FirebaseUtils.OnUserRetrieved() {
            @Override
            public void onUserRetrieved(User user) {
                userConnected = user;
            }
        });
        //Login
        albumName = view.findViewById(R.id.albumName);
        publicSwitch = view.findViewById(R.id.publicAlbum);
        //Submit login
        album_submit = view.findViewById(R.id.album_submit);
        album_submit.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();


        // Inflate the layout for this fragment

        return view;

    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.album_submit:

                Map<String, String> validation_response_creation = Album_utils.isValideAlbum(
                        albumName.getText().toString());


                if (validation_response_creation.get("error") == "yes") {
                    String error_str = "";
                    //we hit error display all error
                    for (String item : validation_response_creation.keySet()) {
                        String key = item.toString();
                        String value = validation_response_creation.get(item).toString();
                        if (validation_response_creation.get(item).toString() == "yes") {
                            //remove yes from error message;
                        } else {
                            error_str += value + "\n";
                        }
                    }
                    //make toast with all error _ ERROR MESSAGE
                    Toast.makeText(getActivity(), error_str, Toast.LENGTH_SHORT).show();
                } else {

                    Album album = new Album(albumName.getText().toString(), userConnected, publicSwitch.isChecked());
                    UUID uniqueKey = UUID.randomUUID();

                    if (publicSwitch.isChecked()){
                        //public

                        //albumz -> public -> album

                        storageRef.child(album.getAlbumName());
                        DatabaseReference albumCreated = mDatabase.child("albumz").child("public").child(album.getAlbumName());
                        albumCreated.child("albumName").setValue(album.getAlbumName());
                        albumCreated.child("user").setValue(userConnected);
                        albumCreated.child("publicAlbum").setValue(album.isPublicAlbum());

                    } else{
                        //private

                        // albumz -> private -> user -> album

                        storageRef.child(album.getAlbumName());
                        mDatabase.child("albumz").child("private").child(currentUser.getUid()).child(album.getAlbumName()).child("albumName").setValue(album.getAlbumName());
                        mDatabase.child("albumz").child("private").child(currentUser.getUid()).child(album.getAlbumName()).child("user").setValue(userConnected);
                        mDatabase.child("albumz").child("private").child(currentUser.getUid()).child(album.getAlbumName()).child("publicAlbum").setValue(album.isPublicAlbum());

                        /*
                        mDatabase.child("albumz").child(noeud).child("albumName").setValue(album.getAlbumName());
                        mDatabase.child("albumz").child(noeud).child("user").setValue(currentUser);
                        mDatabase.child("albumz").child(noeud).child("publicAlbum").setValue(album.isPublicAlbum());
                        */

                    }

                    goBackToHome();
                }

        }
    }



    public void goBackToHome() {
        Intent home = new Intent(getActivity(), MainActivity.class);
        /*
        home.putExtra("name", userToAuth.getName());
        home.putExtra("email", userToAuth.getEmail());
        */
        startActivity(home);
    }
}
