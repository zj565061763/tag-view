package com.sd.demo.tag_view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.tag_view.FTagViewManager;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        FTagViewManager.getDefault().setDebug(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}