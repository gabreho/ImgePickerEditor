package com.skrumble.imagepicketeditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.skrumble.picketeditor.picker.activity.PickerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnTextViewClick(View view) {
        PickerActivity.start(MainActivity.this, 100, 5);
    }
}
