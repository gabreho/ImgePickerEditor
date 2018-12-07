package com.skrumble.picketeditor.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.gallery.GalleryActivity;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnClickAction;
import com.skrumble.picketeditor.utility.Constants;
import com.skrumble.picketeditor.utility.Utility;

import java.util.ArrayList;

public class MediaGridAdapter extends RecyclerView.Adapter<MediaGridAdapter.MediaViewHolder> {

    ArrayList<Media> mediaArrayList = new ArrayList<>();
    OnClickAction<Media> onClickAction;

    public MediaGridAdapter(){
        mediaArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_view_item, viewGroup, false);
        return new MediaViewHolder(view, onClickAction);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder mediaViewHolder, int i) {
        final Media media = mediaArrayList.get(i);
        mediaViewHolder.setMediaFile(media);
    }

    @Override
    public int getItemCount() {
        return mediaArrayList.size();
    }

    public void setOnClickAction(OnClickAction<Media> onClickAction) {
        this.onClickAction = onClickAction;
    }

    public void setData(ArrayList<Media> media) {
        mediaArrayList.addAll(media);
        notifyDataSetChanged();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {

        ImageView previewImageView;
        View maskView;
        RelativeLayout videoInfoLayout;
        RelativeLayout gifInfoLayout;
        TextView videoDuration;
        TextView videoSize;
        View rootView;

        Media media;

        MediaViewHolder(@NonNull View itemView, final OnClickAction<Media> onClickAction) {
            super(itemView);

            rootView = itemView;
            previewImageView = itemView.findViewById(R.id.media_image);
            maskView = itemView.findViewById(R.id.mask_view);
            videoInfoLayout = itemView.findViewById(R.id.video_info);
            gifInfoLayout = itemView.findViewById(R.id.gif_info);
            videoDuration = itemView.findViewById(R.id.video_duration);
            videoSize = itemView.findViewById(R.id.video_size);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAction.onClick(media);
                }
            });

            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getItemWidth()));
        }

        private int getItemWidth() {
            return (Utility.WIDTH / Constants.SPAN_COUNT) - Constants.SPAN_COUNT;
        }

        public void setMediaFile(Media mediaFile) {

            media = mediaFile;

            Uri path = Uri.parse("file://" + mediaFile.path);

            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.IMMEDIATE);

            Glide.with(rootView.getContext()).load(path)
                    .apply(options)
                    .into(previewImageView);

            gifInfoLayout.setVisibility(mediaFile.getExtension().contains("gif") ? View.VISIBLE : View.GONE);

            if (mediaFile.getMimeType().contains("video")){
                videoInfoLayout.setVisibility(View.VISIBLE);
                videoDuration.setText(Utility.convertMillisecondToTime(mediaFile.duration));
                videoSize.setText(Utility.getSizeByUnit(mediaFile.getSize(), true));
            }else {
                videoInfoLayout.setVisibility(View.GONE);
            }

        }
    }
}
