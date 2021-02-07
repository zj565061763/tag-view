package com.sd.demo.tag_view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.demo.tag_view.databinding.ActivityMainBinding;
import com.sd.lib.tag_view.FTagViewManager;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        FTagViewManager.getDefault().setDebug(true);

        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mBinding.viewTag.getItemManager().destroyItem();
    }
}