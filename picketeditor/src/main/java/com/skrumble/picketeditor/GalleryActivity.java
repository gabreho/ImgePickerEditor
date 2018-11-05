package com.skrumble.picketeditor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.skrumble.picketeditor.picker.adapters.MainImageAdapter;
import com.skrumble.picketeditor.picker.interfaces.OnSelectionListener;
import com.skrumble.picketeditor.picker.modals.Img;
import com.skrumble.picketeditor.picker.utility.HeaderItemDecoration;
import com.skrumble.picketeditor.picker.utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;

public class GalleryActivity  extends AppCompatActivity {


    public static GalleryActivity activity;

    private RecyclerView recyclerView;
    private MainImageAdapter mainImageAdapter;
    private OnSelectionListener onSelectionListener;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        activity = this;

        Utility.getScreenSize(this);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);

        setupAppBar();

        onSelectionListener = new OnSelectionListener() {
            @Override
            public void onClick(Img Img, View view, int position) {
                PickerEditor.starEditor(GalleryActivity.this, Img.getUrl(), false);
            }

            @Override
            public void onLongClick(Img img, View view, int position) {

            }
        };

        mainImageAdapter = new MainImageAdapter(this);
        mainImageAdapter.addOnSelectionListener(onSelectionListener);

        recyclerView.addItemDecoration(new HeaderItemDecoration(this, mainImageAdapter));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, MainImageAdapter.SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mainImageAdapter.getItemViewType(position) == MainImageAdapter.HEADER) {
                    return MainImageAdapter.SPAN_COUNT;
                }
                return 1;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(mainImageAdapter);
        updateImages();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Utility.getScreenSize(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.getScreenSize(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.getScreenSize(this);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateImages() {
        mainImageAdapter.clearList();
        Cursor cursor = Utility.getCursor(GalleryActivity.this);
        ArrayList<Img> INSTANTLIST = new ArrayList<>();
        String header = "";
        int limit = 100;
        if (cursor.getCount() < 100) {
            limit = cursor.getCount();
        }
        int date = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        int data = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int contentUrl = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        Calendar calendar;
        for (int i = 0; i < limit; i++) {
            cursor.moveToNext();
            Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getInt(contentUrl));
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(date));
            String dateDifference = Utility.getDateDifference(GalleryActivity.this, calendar);
            if (!header.equalsIgnoreCase("" + dateDifference)) {
                header = "" + dateDifference;
                INSTANTLIST.add(new Img("" + dateDifference, "", "", ""));
            }
            INSTANTLIST.add(new Img("" + header, "" + path, cursor.getString(data), ""));
        }
        cursor.close();

        mainImageAdapter.addImageList(INSTANTLIST);
    }


    private void setupAppBar() {
        setStatusBarColor(Color.BLACK);

        toolbar = findViewById(R.id.toolbar);

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
            actionBar.setDisplayShowTitleEnabled(false);
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
