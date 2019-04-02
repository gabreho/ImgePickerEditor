package com.skrumble.picketeditor.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.skrumble.picketeditor.PickerEditorConfig;
import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.enumeration.FileExtension;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnClickAction;
import com.skrumble.picketeditor.utility.Constants;
import com.skrumble.picketeditor.utility.Utility;

import java.util.ArrayList;

public class MediaGridAdapter extends RecyclerView.Adapter<MediaGridAdapter.MediaViewHolder> {

    public enum LayoutManagerType{
        Liner,
        Grid
    }

    ArrayList<Media> mediaArrayList;
    OnClickAction<Media> onClickAction;
    RequestManager glide;
    RequestOptions options;
    LayoutManagerType layoutManagerType;

    public MediaGridAdapter(Context context){
        this(context, LayoutManagerType.Grid);
    }

    public MediaGridAdapter(Context context, LayoutManagerType type){
        mediaArrayList = new ArrayList<>();
        layoutManagerType = type;

        options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE);

        glide = Glide.with(context);
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
        mediaViewHolder.setMediaFile(media, glide, options);
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
        ImageView selectionImageView;
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
            selectionImageView = itemView.findViewById(R.id.selection);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAction.onClick(media);
                }
            });

            switch (layoutManagerType){
                case Grid:
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getItemWidth());
                    itemView.setLayoutParams(layoutParams);
                    break;
                case Liner:
                    FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(250, 250);
                    layoutParams2.setMarginEnd(5);
                    itemView.setLayoutParams(layoutParams2);
                    break;
            }
        }

        private int getItemWidth() {
            return (PickerEditorConfig.WIDTH / Constants.SPAN_COUNT) - Constants.SPAN_COUNT;
        }

        public void setMediaFile(Media mediaFile, RequestManager glide, RequestOptions options) {

            media = mediaFile;

            selectionImageView.setVisibility(mediaFile.isSelected() ? View.VISIBLE : View.GONE);

            Uri path = Uri.parse("file://" + mediaFile.getPath());

            glide.load(path)
                    .apply(options)
                    .into(previewImageView);

            gifInfoLayout.setVisibility(mediaFile.getExtension() == FileExtension.Gif ? View.VISIBLE : View.GONE);

            if (mediaFile.getMimeType().contains("video")){
                videoInfoLayout.setVisibility(View.VISIBLE);
                videoDuration.setText(Utility.convertMillisecondToTime(mediaFile.getDuration()));
                videoSize.setText(Utility.getSizeByUnit(mediaFile.getSize(), true));
            }else {
                videoInfoLayout.setVisibility(View.GONE);
            }

        }
    }
}