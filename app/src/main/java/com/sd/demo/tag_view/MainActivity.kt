package com.sd.demo.tag_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.tag_view.databinding.ActivityMainBinding
import com.sd.lib.tag_view.FTagViewManager

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        FTagViewManager.getDefault().setDebug(true)
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding!!.viewTag.itemManager.destroyItem()
    }
}