package com.example.sylvain.albumz;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Created by michaelguerfi on 05/12/2017.
 */

public class AlbumShowFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.album_show, container, false); //get current layout to get UI element

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final String albumName = getArguments().getString("albumName");

        Log.d("albumname", albumName);

        DatabaseReference ref = database.child("albumz").child("public").child(albumName);


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                          for (DataSnapshot item : dataSnapshot.getChildren()) {
                                              if (item.getValue().equals(albumName)) {
                                                  Album album = dataSnapshot.getValue(Album.class);
                                                  TextView albumNameInput = view.findViewById(R.id.albumNameShow);

                                                  albumNameInput.setText(album.getAlbumName());
                                                  TextView userNameInput = view.findViewById(R.id.userNameShow);
                                                  userNameInput.setText(album.getUser().getName());
                                                  DataSnapshot storageRef;
                                                  StorageReference islandRef = storageRef.child("images/"+);

                                                  final long ONE_MEGABYTE = 1024 * 1024;
                                                  islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                      @Override
                                                      public void onSuccess(byte[] bytes) {
                                                          // Data for "images/island.jpg" is returns, use this as needed
                                                      }
                                                  }).addOnFailureListener(new OnFailureListener() {
                                                      @Override
                                                      public void onFailure(@NonNull Exception exception) {
                                                          // Handle any errors
                                                      }
                                                  });
                                              }

                                          }
                                      }

                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {

                                      }
                                  });
            // Inflate the layout for this fragment

        return view;

    }


}
