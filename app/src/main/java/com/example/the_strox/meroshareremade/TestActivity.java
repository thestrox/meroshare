package com.example.the_strox.meroshareremade;

import android.os.Bundle;
import android.widget.FrameLayout;

public class TestActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
}}
