package com.example.sylvain.albumz;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

/**
 * Created by julien on 28/11/2017.
 */

public class UploadFragment extends Fragment implements View.OnClickListener {


    private FirebaseAuth mAuth;

    //User Auth via FireBase
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser currentUser;


    private Uri selectedImageUri;

    // For search when name album is posted with upload if we find on private or public its fine, else if both true => Toast 0 album found
    Boolean privateError = false;
    Boolean publicError = false;

    //UI creation
    EditText albumName;
    Switch publicSwitch;
    Button album_submit;
    Button select;
    ImageView preview;
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

        View view = inflater.inflate(R.layout.upload, container, false); //get current layout to get UI element

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Login
        albumName = view.findViewById(R.id.albumName);
        publicSwitch = view.findViewById(R.id.publicAlbum);
        //Submit login
        album_submit = view.findViewById(R.id.album_submit);
        select = view.findViewById(R.id.select);
        select.setOnClickListener(this);
        album_submit.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();


        preview = view.findViewById(R.id.preview);

        // Inflate the layout for this fragment

        return view;

    }


    public void onClickUploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose picture"), 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            selectedImageUri = data.getData();

            Glide.with(getContext())
                    .load(selectedImageUri)
                    .override(300, 200)
                    .into(preview);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.select:

                onClickUploadImage(view);
                break;
            case R.id.album_submit:

                Uri file = selectedImageUri;

                final String key = mDatabase.child("messages").push().getKey();

                if (file != null && albumName.getText().toString().length() != 0) {

                    final StorageReference imageRef = storageRef.child("images/" + key + " .jpg");
                    UploadTask uploadTask = null;
                    try {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();



                        final DatabaseReference refPrivate = database.child("albumz").child("private").child(currentUser.getUid()); //current users private albums
                        final DatabaseReference refPublic = database.child("albumz").child("public").child(currentUser.getUid()); //current users private albums

                                //Log.d("PRIVATE ALBUM => ", refPrivate.toString());

                        refPrivate.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                //System.out.println("fait fier" + snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                                for (DataSnapshot data: snapshot.getChildren()) { // child == album
                                    //Log.d("childValue",data.child("albumName").getValue(String.class));
                                    /*
                                    child.getValue(Album.class);
                                    Log.d("okkk", child.getValue(Album.class).getAlbumName());
                                    Log.d("okkk2", albumName.getText().toString());
                                    */
                                    if(albumName.getText().toString().equals(data.child("albumName").getValue(String.class))){
                                        //private found
                                        //todo add picture
                                        //Log.d("PRIVATE FOUND", "found ! todo add picture node");
                                        //Log.d("key data",data.getKey().toString());
                                        refPrivate.child(data.getKey()).child("pictures").push().setValue(key + "jpg"); //last key correspond to image name
                                        return;
                                    }else{
                                        privateError = true;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        refPublic.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot data: snapshot.getChildren()) { // child == album
                                   // Log.d("childValue",data.child("albumName").getValue(String.class));

                                    if(albumName.getText().toString().equals(data.child("albumName").getValue(String.class))){
                                        //public found
                                        //todo add picture
                                        //Log.d("PUBLIC FOUND", "found ! todo add picture node");
                                        //Log.d("key data",data.getKey().toString());
                                        refPublic.child(data.getKey()).child("pictures").push().setValue(key + "jpg"); //last key correspond to image name
                                        return;
                                    }else{
                                        publicError = true;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        //if both error === false we push image name into child pictures (private or public album correspond) ELSE return toast Error no album found
                        if(!privateError || !publicError){
                            uploadTask = imageRef.putStream(getActivity().getContentResolver().openInputStream(selectedImageUri));
                        }else{
                            CharSequence text = "No albums found ";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    CharSequence text = "Please fill all fields !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                }


                break;


        }


    }


}
