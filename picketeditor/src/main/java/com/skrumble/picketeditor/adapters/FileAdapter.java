package com.skrumble.picketeditor.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnClickAction;
import com.skrumble.picketeditor.utility.Utility;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter {

    ArrayList<Media> mediaArrayList;
    OnClickAction<Media> clickAction;

    public FileAdapter(){
        mediaArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_file_cell, viewGroup, false);

        FileViewHolder fileViewHolder = new FileViewHolder(view);

        fileViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickAction != null){
                    clickAction.onClick(mediaArrayList.get(i));
                }
            }
        });

        return fileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        FileViewHolder fileViewHolder = (FileViewHolder) viewHolder;
        fileViewHolder.setFile(mediaArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return mediaArrayList.size();
    }

    public void setClickAction(OnClickAction<Media> clickAction) {
        this.clickAction = clickAction;
    }

    public void addFiles(ArrayList<Media> media) {
        mediaArrayList.clear();
        mediaArrayList.addAll(media);
        notifyDataSetChanged();
    }

    public void filterData(String s) {
        ArrayList<Media> filteredList = new ArrayList<>();

        for (Media media: mediaArrayList){
            if (media.getName().contains(s)){
                filteredList.add(media);
            }
        }

        mediaArrayList = filteredList;
    }

    public class FileViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView subTitleTextView;
        ImageView iconImageView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            subTitleTextView = itemView.findViewById(R.id.sub_title);
            iconImageView = itemView.findViewById(R.id.image_view);
        }

        public void setFile(Media media) {
            titleTextView.setText(media.getFileName());
            subTitleTextView.setText(Utility.getSizeByUnit(media.getSize()));
            iconImageView.setImageResource(media.getExtension().getImageIconSource());
        }
    }
}