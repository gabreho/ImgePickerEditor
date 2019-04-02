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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.adapters.FileAdapter;
import com.skrumble.picketeditor.data_loaders.FileFilters;
import com.skrumble.picketeditor.enumeration.FileTypeTab;
import com.skrumble.picketeditor.enumeration.GalleryType;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnCompletion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    FileAdapter fileAdapter;

    private TabLayout.Tab currentTab;

    private ArrayList<Media> mediaArrayList = new ArrayList<>();

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

        currentTab = tabLayout.getTabAt(0);

        tabLayout.addOnTabSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        fileAdapter = new FileAdapter();
        recyclerView.setAdapter(fileAdapter);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_file, menu);

        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        SearchView mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.menu_title_search));
        mSearchView.setOnQueryTextListener(this);

        return false;
    }

    private void loadData() {
        FileFilters.getFiles(this, new OnCompletion<GalleryType, ArrayList<Media>>() {
            @Override
            public void onCompleted(GalleryType galleryType, ArrayList<Media> media) {
                mediaArrayList = media;
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

    public void filterByTab(FileTypeTab tab) {
        ArrayList<Media> finalList = new ArrayList<>();
        List<String> extensionList = new ArrayList<>();
        switch (tab) {
            case DOC:
                extensionList = Arrays.asList("xls", "doc", "ppt", "xlsx", "docx", "pptx");
                for (Media media: mediaArrayList){
                    if (extensionList.contains(media.getExtensionString())){
                        finalList.add(media);
                    }
                }

                fileAdapter.addFiles(finalList);

                break;
            case PDF:
                for (Media media: mediaArrayList){
                    if ("pdf".contains(media.getExtensionString())){
                        finalList.add(media);
                    }
                }

                fileAdapter.addFiles(finalList);

                break;
            case ZIP:
                for (Media media: mediaArrayList){
                    if ("zip".contains(media.getExtensionString())){
                        finalList.add(media);
                    }
                }

                fileAdapter.addFiles(finalList);

                break;
            case OTHERS:
                extensionList =Arrays.asList("pdf", "xls", "doc", "ppt", "xlsx", "docx", "pptx");

                for (Media media: mediaArrayList){
                    if (extensionList.contains(media.getExtensionString())){
                        continue;
                    }

                    finalList.add(media);
                }

                fileAdapter.addFiles(finalList);

                break;
            default:
                fileAdapter.addFiles(mediaArrayList);
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentTab = tab;

        String text;
        try{
            text = tab.getText().toString();
        }catch (Exception e){
            e.printStackTrace();
            text = "";
        }

        FileTypeTab tab1 = FileTypeTab.getFromString(this, text);
        filterByTab(tab1);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (currentTab == tab){
            recyclerView.smoothScrollToPosition(0);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        fileAdapter.filterData(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
