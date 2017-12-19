package com.example.sylvain.albumz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by michaelguerfi on 21/11/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<Album> listAlbums = new ArrayList<>();
    private ListAlbumFragment fragment;

    private DatabaseReference mDatabase;

    public AlbumAdapter(ListAlbumFragment fragment) {
        this.context = fragment.getContext();
        this.fragment = fragment;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.album_item,parent,false);

        //mDatabase = FirebaseDatabase.getInstance().getReference("albumz/public");
        return new ViewHolder(view);
    }


    public void addAlbum(Album album){
        listAlbums.add(0, album);
        notifyDataSetChanged();
    }

    public void removeAlbum(Album album){
        listAlbums.remove(album);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Album album = listAlbums.get(position);
        holder.userName.setText(album.getUser().getEmail());
        holder.albumName.setText(album.getAlbumName());

        holder.albumName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onAlbumSelected(listAlbums.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAlbums.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView albumName;
        TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);

            albumName = itemView.findViewById(R.id.albumName);
            userName = itemView.findViewById(R.id.userName);
        }
    }


    public interface OnAlbumSelectedListener{
        void onAlbumSelected(Album album);
    }
}
