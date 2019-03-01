package com.skrumble.picketeditor.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.adapters.FileAdapter;
import com.skrumble.picketeditor.data_loaders.FileFilters;
import com.skrumble.picketeditor.data_loaders.MediaLoader;
import com.skrumble.picketeditor.enumeration.FileTypeTab;
import com.skrumble.picketeditor.enumeration.GalleryType;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnCompletion;

import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    FileAdapter fileAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file);
        setupAppBar();

        setTitle(R.string.attachement);

        tabLayout = findViewById(R.id.tab_layout);

        for (FileTypeTab typeTab: FileTypeTab.values()){
            tabLayout.addTab(tabLayout.newTab().setText(typeTab.title));
        }

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        fileAdapter = new FileAdapter();
        recyclerView.setAdapter(fileAdapter);

        loadData();
    }

    private void loadData() {
        FileFilters.getFiles(this, new OnCompletion<GalleryType, ArrayList<Media>>() {
            @Override
            public void onCompleted(GalleryType galleryType, ArrayList<Media> media) {
                fileAdapter.addFiles(media);
            }
        });
    }

    private void setupAppBar() {
        setStatusBarColor(Color.BLACK);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.ally_accent_color));

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
}
