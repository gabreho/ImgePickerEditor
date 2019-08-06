package com.skrumble.picketeditor.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.skrumble.picketeditor.PickerEditorConfig;
import com.skrumble.picketeditor.PickerEditor;
import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.adapters.MediaGridAdapter;
import com.skrumble.picketeditor.adapters.SpacingDecoration;
import com.skrumble.picketeditor.data_loaders.FileFilters;
import com.skrumble.picketeditor.enumeration.FileExtension;
import com.skrumble.picketeditor.enumeration.GalleryType;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnClickAction;
import com.skrumble.picketeditor.public_interface.OnCompletion;
import com.skrumble.picketeditor.utility.PickerEditorStyleParams;

import java.util.ArrayList;

import static com.skrumble.picketeditor.PickerEditor.PICKER_EDITOR_STYLE;

public class GalleryActivity  extends AppCompatActivity implements OnClickAction<Media> {

    public static final String EXTRA_GALLERY_TYPE = "GALLERY_TYPE";
    public static final int GAlLERY_TYPE_PICTURE = 1;
    public static final int GAlLERY_TYPE_VIDEO = 2;
    public static final int GAlLERY_TYPE_PHOTO_AND_VIDEO = 3;
    public static GalleryActivity activity;

//    private int typeOfGallery;

    private GalleryType galleryType;

    private RecyclerView recyclerView;
    private MediaGridAdapter mediaGridAdapter;
    private Toolbar toolbar;

    private PickerEditorStyleParams mStyleParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        activity = this;

        setGalleryType(getIntent());

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);

        setupAppBar();

        mediaGridAdapter = new MediaGridAdapter(this);
        mediaGridAdapter.setOnClickAction(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, PickerEditorConfig.GRID_SPAN_COUNT);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addItemDecoration(new SpacingDecoration(PickerEditorConfig.GRID_SPAN_COUNT, 4));

        recyclerView.setAdapter(mediaGridAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateImages();
    }

    private void setGalleryType(Intent intent) {

        int intExtra = intent.getIntExtra(EXTRA_GALLERY_TYPE, GAlLERY_TYPE_PHOTO_AND_VIDEO);
        mStyleParams = intent.getParcelableExtra(PICKER_EDITOR_STYLE);
        galleryType = GalleryType.getGalleryTypeFromInt(intExtra);

        setTitle(galleryType.title);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    @SuppressLint("StaticFieldLeak")
    private void updateImages() {
        FileFilters.getData(this, galleryType, new OnCompletion<GalleryType, ArrayList<Media>>() {
            @Override
            public void onCompleted(GalleryType galleryType, ArrayList<Media> media) {
                mediaGridAdapter.setData(media);
            }
        });
    }

    private void setupAppBar() {
        setStatusBarColor(Color.BLACK);

        toolbar = findViewById(R.id.toolbar);

        // Set all of the Toolbar coloring
        if (mStyleParams != null) {
            toolbar.setBackgroundColor(mStyleParams.getAccentColor());
            toolbar.setTitleTextColor(mStyleParams.getTitleColor());
        }

        toolbar.setVisibility(View.VISIBLE);

        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_and_icn_back);
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate();
            stateButtonDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(stateButtonDrawable);
        }

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    @Override
    public void onClick(Media object) {

        if (object.getExtension() == FileExtension.Gif){
            Intent intent = new Intent();
            intent.putExtra(PickerEditor.RESULT_FILE, object.getPath());
            setResult(RESULT_OK, intent);
            finish();

            return;
        }

        if (object.isImage()) {
            PickerEditor.starEditor(GalleryActivity.this, object.getPath());
            return;
        }

        if (object.isVideo() && PickerEditorConfig.isConfigVideoTrimmingFeature()) {
            PickerEditor.starVideoEditor(this, object.getPath());
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(PickerEditor.RESULT_FILE, object.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }
}
