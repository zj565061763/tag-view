package com.sd.demo.tag_view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.tag_view.databinding.ActivityMainBinding
import com.sd.lib.tag_view.FTagViewManager

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        FTagViewManager.default.mIsDebug = true;
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onClick(v: View?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.viewTag.itemManager.destroyItem()
    }
}