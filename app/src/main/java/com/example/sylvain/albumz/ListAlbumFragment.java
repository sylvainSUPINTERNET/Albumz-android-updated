package com.example.sylvain.albumz;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;


/**
 * Created by michaelguerfi on 21/11/2017.
 */

public class ListAlbumFragment extends Fragment implements AlbumAdapter.OnAlbumSelectedListener {

    RecyclerView rvAlbumz;
    AlbumAdapter adapter;


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_album, container, false); //get current layout to get UI element


        //Login
        rvAlbumz = view.findViewById(R.id.rvAlbumz);

        //mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        rvAlbumz.setLayoutManager(manager);
        adapter = new AlbumAdapter(this);
        rvAlbumz.setAdapter(adapter);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        // Inflate the layout for this fragment

        FirebaseUtils.listenToPublicAlbumz(new FirebaseUtils.OnAlbumzRetrieved() {
            @Override
            public void onAlbumzAdded(Album album) {
                adapter.addAlbum(album);
            }
        });

        return view;

    }

    @Override
    public void onAlbumSelected(Album album) {
        ((MainActivity) getActivity()).redirectAlbumShow(album);
    }

}
